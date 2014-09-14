package net.nuclearg.kyou.dom.serialize;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouArray;
import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.dom.KyouField;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.dom.KyouStruct;
import net.nuclearg.kyou.dom.visitor.KyouDomVisitor;
import net.nuclearg.kyou.util.ByteUtils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * XML序列化/反序列化实现类
 * <p>
 * 这个类是kyou中对{@link KyouDocument}的序列化/反序列化的默认实现
 * </p>
 * 
 * @author ng
 */
public class XmlDomSerializer implements KyouDomSerializer, KyouDomDeserializer {

    private static final List<String> NAME_REQUIRED_TAG = Arrays.asList("field", "struct", "array");

    @Override
    public void serialize(KyouDocument doc, OutputStream os) {
        if (doc == null)
            throw new KyouException("doc is null");
        if (os == null)
            throw new KyouException("output is null");

        String xml = this.serialize(doc);
        try {
            os.write(xml.getBytes(ByteUtils.UTF8));
        } catch (Exception ex) {
            throw new KyouException("serialize fail. doc: " + doc, ex);
        }
    }

    /**
     * 将报文数据序列化为xml
     * 
     * @param doc
     *            报文数据
     * @return 报文数据对应的xml
     */
    public String serialize(KyouDocument doc) {
        if (doc == null)
            throw new KyouException("doc is null");

        final StringBuilder buffer = new StringBuilder();
        try {
            doc.foreach(new SerializerVisitor(buffer));

            return buffer.toString();
        } catch (Exception ex) {
            throw new KyouException("serialize fail. doc: " + doc, ex);
        }
    }

    private static class SerializerVisitor implements KyouDomVisitor {
        private final StringBuilder builder;

        public SerializerVisitor(StringBuilder builder) {
            this.builder = builder;
        }

        @Override
        public void docStart(KyouDocument doc) {
            this.builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><document");
            this.appendAttributes(this.builder, doc);
            this.builder.append(">");
        }

        @Override
        public void docEnd(KyouDocument doc) {
            this.builder.append("</document>");
        }

        @Override
        public void struStart(KyouStruct stru) {
            this.builder.append("<struct name=\"").append(stru.name()).append("\"");
            this.appendAttributes(this.builder, stru);
            this.builder.append(">");
        }

        @Override
        public void struEnd(KyouStruct stru) {
            this.builder.append("</struct>");
        }

        @Override
        public void arrayStart(KyouArray array) {
            this.builder.append("<array name=\"").append(array.name()).append("\"");
            this.appendAttributes(this.builder, array);
            this.builder.append(">");

            this.builder.append("<prototype>");
            SerializerVisitor visitor = new SerializerVisitor(this.builder);
            array.prototype().foreach(visitor);
            this.builder.append("</prototype>");
        }

        @Override
        public void arrayEnd(KyouArray array) {
            this.builder.append("</array>");
        }

        @Override
        public void field(KyouField field) {
            this.builder.append("<field");
            this.appendAttributes(this.builder, field);
            this.builder.append(">");

            this.builder.append(StringEscapeUtils.escapeXml(field.value()));

            this.builder.append("</field>");
        }

        private void appendAttributes(StringBuilder builder, KyouItem item) {
            builder.append(" attributes=\"");

            for (Entry<String, String> attr : item.attributes().entrySet())
                try {
                    String k = URLEncoder.encode(attr.getKey(), "utf8");
                    String v = URLEncoder.encode(attr.getValue(), "utf8");

                    builder.append(k).append("=").append(v).append("&");
                } catch (Exception ex) {
                    throw new KyouException(ex);
                }

            builder.append("\"");
        }
    }

    @Override
    public KyouDocument deserialize(InputStream is) {
        return this.deserialize(new InputSource(is));
    }

    /**
     * 将xml解析为报文数据
     * 
     * @param in
     *            xml
     * @return 解析好的报文数据
     */
    public KyouDocument deserialize(InputSource in) {
        if (in == null)
            throw new KyouException("in is null");

        try {
            final DomBuilder builder = new DomBuilder();

            // 使用SAX进行解析
            XMLReader reader = XMLReaderFactory.createXMLReader();

            // 进行解析
            reader.setContentHandler(new DefaultHandler() {
                private final StringBuilder text = new StringBuilder();

                @Override
                public void startElement(String uri, String localName, String qName, Attributes xmlAttributes) throws SAXException {
                    String name = xmlAttributes.getValue("name");

                    if (NAME_REQUIRED_TAG.contains(localName) && StringUtils.isBlank(name))
                        throw new KyouException("name is blank. tag: " + localName);

                    String attributeStr = xmlAttributes.getValue("attributes");
                    Map<String, String> attributes = this.parseAttributes(attributeStr);

                    if (localName.equals("document"))
                        builder.beginDocument(attributes);
                    else if (localName.equals("struct"))
                        builder.beginStruct(name, attributes);
                    else if (localName.equals("array"))
                        builder.beginArray(name, attributes);
                    else if (localName.equals("prototype"))
                        builder.beginArrayPrototype();
                    else if (localName.equals("field")) {
                        builder.beginField(name, attributes);
                        this.text.delete(0, this.text.length());
                    }
                    else
                        throw new KyouException("unsupported xml tag: " + localName);
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (localName.equals("document"))
                        ;
                    else if (localName.equals("struct"))
                        builder.endStruct();
                    else if (localName.equals("array"))
                        builder.endArray();
                    else if (localName.equals("prototype"))
                        builder.endArrayPrototype();
                    else if (localName.equals("field")) {
                        String text = StringEscapeUtils.unescapeXml(this.text.toString());
                        builder.endField(text);
                    } else
                        throw new KyouException("unsupported xml tag: " + localName);
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    this.text.append(new String(ch, start, length));
                }

                @Override
                public void fatalError(SAXParseException ex) throws SAXException {
                    throw new KyouException("parse xml fail. ln: " + ex.getLineNumber() + ", col:" + ex.getColumnNumber() + ", msg: " + ex.getMessage(), ex);
                }

                private Map<String, String> parseAttributes(String attributeStr) {
                    if (StringUtils.isBlank(attributeStr))
                        return Collections.emptyMap();

                    Map<String, String> attributes = new HashMap<>();
                    for (String attribute : StringUtils.split(attributeStr, '&'))
                        try {
                            String[] kv = StringUtils.split(attribute, '=');
                            if (kv.length != 2)
                                throw new KyouException("attribute syntax error");

                            String k = URLDecoder.decode(kv[0], "utf8");
                            String v = URLDecoder.decode(kv[1], "utf8");

                            if (StringUtils.isBlank(k))
                                throw new KyouException("attribute name is blank");

                            if (attributes.containsKey(k))
                                throw new KyouException("atribute name duplicate. name: " + k);

                            attributes.put(k, v);
                        } catch (UnsupportedEncodingException ex) {
                            throw new KyouException(ex);
                        }

                    return attributes;
                }
            });
            reader.setErrorHandler((ErrorHandler) reader.getContentHandler());
            reader.parse(in);

            // 返回解析好的报文数据
            return builder.endDocument();
        } catch (Exception ex) {
            throw new KyouException("parse xml fail", ex);
        }
    }
}

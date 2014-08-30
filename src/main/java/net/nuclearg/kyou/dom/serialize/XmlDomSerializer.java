package net.nuclearg.kyou.dom.serialize;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map.Entry;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouArray;
import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.dom.KyouField;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.dom.KyouStruct;
import net.nuclearg.kyou.dom.visitor.KyouDomVisitor;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * XML序列化/反序列化实现类
 * <p>
 * 这个类是kyou中对KyouDocument和DataDocument的序列化/反序列化的默认实现
 * </p>
 * 
 * @author nuclearg
 */
public class XmlDomSerializer implements DomSerializer {
    @Override
    public void serialize(KyouDocument doc, OutputStream os) {
        if (doc == null)
            throw new KyouException("doc is null");
        if (os == null)
            throw new KyouException("output is null");

        final StringBuilder buffer = new StringBuilder();
        try {
            doc.foreach(new SerializerVisitor(buffer));

            os.write(buffer.toString().getBytes(Charset.forName("utf8")));
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
            builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><document");
            this.writeAttributes(doc);
            builder.append(">");
        }

        @Override
        public void docEnd(KyouDocument doc) {
            builder.append("</document>");
        }

        @Override
        public void struStart(KyouStruct stru) {
            builder.append("<struct name=\"").append(stru.name()).append("\"");
            if (!(stru.parent() instanceof KyouArray))
                this.writeAttributes(stru);
            builder.append(">");
        }

        @Override
        public void struEnd(KyouStruct stru) {
            builder.append("</struct>");
        }

        @Override
        public void arrayStart(KyouArray array) {
            builder.append("<array name=\"").append(array.name()).append("\"");
            this.writeAttributes(array);
            builder.append(">");

            builder.append("<prototype>");
            SerializerVisitor visitor = new SerializerVisitor(this.builder);
            array.prototype().foreach(visitor);
            builder.append("</prototype>");
        }

        @Override
        public void arrayEnd(KyouArray array) {
            builder.append("</array>");
        }

        @Override
        public void field(KyouField field) {
            builder.append("<field");
            this.writeAttributes(field);
            builder.append(">");

            builder.append(StringEscapeUtils.escapeXml(field.value()));

            builder.append("</field>");
        }

        private void writeAttributes(KyouItem item) {
            if (item.attributes().size() == 0)
                return;

            for (Entry<String, String> attr : item.attributes().entrySet())
                builder.append(" ").append(attr.getKey()).append("=\"").append(StringEscapeUtils.escapeXml(attr.getValue())).append("\"");
        }

    }

    @Override
    public KyouDocument deserialize(InputStream is) {
        if (is == null)
            throw new KyouException("input is null");

        return null;

        // try {
        // InputSource source = new InputSource(is);
        //
        // final KyouDomBuilder builder = new KyouDomBuilder();
        //
        // // 使用SAX进行解析
        // XMLReader reader = XMLReaderFactory.createXMLReader();
        //
        // // 进行解析
        // reader.setContentHandler(new DefaultHandler() {
        // private Map<String, String> atts = new LinkedHashMap<String, String>();
        //
        // @Override
        // public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // this.fillAttributes(attributes);
        //
        // if (localName.equals("document"))
        // ;
        // else if (localName.equals("struct"))
        // builder.beginStruct(this.atts);
        // else if (localName.equals("array"))
        // ;
        // else if (localName.equals("field"))
        // builder.field(this.atts);
        // else
        // throw new KyouException(KyouErr.Kyou.Serialization.InvalidKyouXmlTag, localName);
        // }
        //
        // @Override
        // public void endElement(String uri, String localName, String qName) throws SAXException {
        // if (localName.equals("struct"))
        // builder.endStruct();
        // }
        //
        // @Override
        // public void fatalError(SAXParseException ex) throws SAXException {
        // throw new KyouException(KyouErr.Base.Xml.SaxFail, "[" + ex.getLineNumber() + ":" + ex.getColumnNumber() + "] " + ex.getMessage());
        // }
        //
        // private void fillAttributes(Attributes attributes) {
        // atts.clear();
        // for (int i = 0; i < attributes.getLength(); i++)
        // atts.put(attributes.getLocalName(i), attributes.getValue(i));
        // }
        // });
        // reader.setErrorHandler((ErrorHandler) reader.getContentHandler());
        // reader.parse(source);
        //
        // // 返回解析好的KyouDocument
        // return builder.result();
        // } catch (Exception ex) {
        // throw new KyouException(KyouErr.Kyou.Serialization.XmlDeserializeKyouFail, ex);
        // }
    }
}

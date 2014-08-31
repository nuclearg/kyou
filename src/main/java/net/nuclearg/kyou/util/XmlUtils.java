package net.nuclearg.kyou.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.nuclearg.kyou.KyouException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 工具类 提供一些xml相关的工具类
 * 
 * @author ng
 */
public class XmlUtils {
    private static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    private static XPathFactory xpathFactory = XPathFactory.newInstance();

    /**
     * 从给定的输入流中解析出一篇XML文档
     * 
     * @param in
     *            要从中读取XML的输入
     * @return 从流中读取出的XML文档
     */
    public static Document load(InputSource in) {
        if (in == null)
            throw new KyouException("input is null");

        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            builder.setErrorHandler(new DefaultHandler() {
                @Override
                public void fatalError(SAXParseException ex) throws SAXException {
                    throw new KyouException("load xml fail. ln: " + ex.getLineNumber() + ", pos: " + ex.getColumnNumber() + " " + ex.getMessage());
                }
            });
            return builder.parse(in);
        } catch (Exception ex) {
            throw new KyouException("load xml fail", ex);
        }
    }

    /**
     * 使用xpath查询出指定的元素节点
     * 
     * @param base
     *            查询的起点
     * @param xpath
     *            XPath表达式
     * @return 查询出的节点或null
     */
    public static Element selectElement(Node base, String xpath) {
        if (base == null)
            throw new KyouException("empty xml node");
        if (StringUtils.isEmpty(xpath))
            throw new KyouException("xpath is empty");

        try {
            return (Element) xpathFactory.newXPath().evaluate(xpath, base, XPathConstants.NODE);
        } catch (Exception ex) {
            throw new KyouException("xpath fail. xpath: " + xpath, ex);
        }
    }

    /**
     * 使用xpath查询出指定的节点列表
     * 
     * @param base
     *            查询的起点
     * @param xpath
     *            XPath表达式
     * @return 查询出的节点列表
     */
    public static List<Element> selectElementList(Node base, String xpath) {
        if (base == null)
            throw new KyouException("empty xml node");
        if (StringUtils.isEmpty(xpath))
            throw new KyouException("xpath is empty");

        try {
            NodeList list = (NodeList) xpathFactory.newXPath().evaluate(xpath, base, XPathConstants.NODESET);

            List<Element> result = new ArrayList<Element>();
            for (int i = 0; i < list.getLength(); i++)
                result.add((Element) list.item(i));
            return result;
        } catch (Exception ex) {
            throw new KyouException("xpath fail. xpath: " + xpath, ex);
        }
    }

    /**
     * 使用xpath查询出指定的文本，查询出的文本的前后空格将被trim掉
     * 
     * @param base
     *            查询的起点
     * @param xpath
     *            XPath表达式
     * @return 查询出的字符串或null
     */
    public static String selectText(Node base, String xpath) {
        if (base == null)
            throw new KyouException("empty xml node. xpath: " + xpath);
        if (StringUtils.isEmpty(xpath))
            throw new KyouException("xpath is empty");

        try {
            Node node = (Node) xpathFactory.newXPath().evaluate(xpath, base, XPathConstants.NODE);
            return node == null ? null : node.getTextContent().trim();
        } catch (Exception ex) {
            throw new KyouException("xpath fail. xpath: " + xpath, ex);
        }
    }

    /**
     * 使用xpath查询指定的文本列表，查询出的文本的前后空格将被trim掉
     * 
     * @param base
     *            查询的起点
     * @param xpath
     *            XPath表达式
     * @return 查询出的字符串列表
     */
    public static List<String> selectTextList(Node base, String xpath) {
        if (base == null)
            throw new KyouException("empty xml node");
        if (StringUtils.isEmpty(xpath))
            throw new KyouException("xpath is empty");

        try {
            NodeList list = (NodeList) xpathFactory.newXPath().evaluate(xpath, base, XPathConstants.NODESET);

            List<String> result = new ArrayList<String>();
            for (int i = 0; i < list.getLength(); i++)
                result.add(list.item(i).getTextContent().trim());
            return result;
        } catch (Exception ex) {
            throw new KyouException("xpath fail. xpath: " + xpath, ex);
        }
    }

}

package com.wsgcya.coder.common.util;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.*;

public class XmlUtil {
    /**
     * 
     * @Title Dom2Map
     * @Class XmlUtil
     * @return Map<String,Object>
     * @param doc
     * @return
     * @Description xml转Map
     * @author qinshijiang@telincn.com
     * @throws DocumentException
     * @Date 2016年7月28日
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> Dom2Map(HttpServletRequest request) {
	Map<String, Object> map = new HashMap<String, Object>();
	try {
	    SAXReader reader = new SAXReader();
	    Document doc = reader.read(request.getInputStream());
	    if (doc == null)
		return map;
	    Element root = doc.getRootElement();
	    for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
		Element e = (Element) iterator.next();
		List list = e.elements();
		if (list.size() > 0) {
		    map.put(e.getName(), Dom2Map(e));
		}else
		    map.put(e.getName(), e.getText());
	    }
	}catch (Exception e) {
	    e.printStackTrace();
	}
	return map;
    }

    @SuppressWarnings("rawtypes")
    public static Map<String, Object> Dom2Map(String xml) {
	Map<String, Object> map = new HashMap<String, Object>();
	try {
	    SAXReader reader = new SAXReader();
	    Document doc = reader.read(new ByteArrayInputStream(xml.getBytes()));
	    if (doc == null)
		return map;
	    Element root = doc.getRootElement();
	    for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
		Element e = (Element) iterator.next();
		List list = e.elements();
		if (list.size() > 0) {
		    map.put(e.getName(), Dom2Map(e));
		}else
		    map.put(e.getName(), e.getText());
	    }
	}catch (Exception e) {
	    e.printStackTrace();
	}
	return map;
    }

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    private static Map Dom2Map(Element e) {
	Map map = new HashMap();
	List list = e.elements();
	if (list.size() > 0) {
	    for (int i = 0; i < list.size(); i++) {
		Element iter = (Element) list.get(i);
		List mapList = new ArrayList();

		if (iter.elements().size() > 0) {
		    Map m = Dom2Map(iter);
		    if (map.get(iter.getName()) != null) {
			Object obj = map.get(iter.getName());
			if (!obj.getClass().getName().equals("java.util.ArrayList")) {
			    mapList = new ArrayList();
			    mapList.add(obj);
			    mapList.add(m);
			}
			if (obj.getClass().getName().equals("java.util.ArrayList")) {
			    mapList = (List) obj;
			    mapList.add(m);
			}
			map.put(iter.getName(), mapList);
		    }else
			map.put(iter.getName(), m);
		}else {
		    if (map.get(iter.getName()) != null) {
			Object obj = map.get(iter.getName());
			if (!obj.getClass().getName().equals("java.util.ArrayList")) {
			    mapList = new ArrayList();
			    mapList.add(obj);
			    mapList.add(iter.getText());
			}
			if (obj.getClass().getName().equals("java.util.ArrayList")) {
			    mapList = (List) obj;
			    mapList.add(iter.getText());
			}
			map.put(iter.getName(), mapList);
		    }else
			map.put(iter.getName(), iter.getText());
		}
	    }
	}else
	    map.put(e.getName(), e.getText());
	return map;
    }

    /**
     * JavaBean转换成xml 默认编码UTF-8
     *
     * @param obj
     * @return
     */
    public static String convertToXml(Object obj) {
	return convertToXml(obj, "UTF-8");
    }

    /**
     * JavaBean转换成xml
     *
     * @param obj
     * @param encoding
     * @return
     */
    public static String convertToXml(Object obj, String encoding) {
	String result = null;
	try {
	    JAXBContext context = JAXBContext.newInstance(obj.getClass());
	    Marshaller marshaller = context.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	    marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

	    StringWriter writer = new StringWriter();
	    marshaller.marshal(obj, writer);
	    result = writer.toString();
	}catch (Exception e) {
	    e.printStackTrace();
	}

	return result;
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data
     *            Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
	DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	org.w3c.dom.Document document = documentBuilder.newDocument();
	org.w3c.dom.Element root = document.createElement("xml");
	document.appendChild(root);
	for (String key : data.keySet()) {
	    String value = data.get(key);
	    if (value == null) {
		value = "";
	    }
	    value = value.trim();
	    org.w3c.dom.Element filed = document.createElement(key);
	    filed.appendChild(document.createTextNode(value));
	    root.appendChild(filed);
	}
	TransformerFactory tf = TransformerFactory.newInstance();
	Transformer transformer = tf.newTransformer();
	DOMSource source = new DOMSource(document);
	transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	StringWriter writer = new StringWriter();
	StreamResult result = new StreamResult(writer);
	transformer.transform(source, result);
	String output = writer.getBuffer().toString(); 
	try {
	    writer.close();
	}catch (Exception ex) {
	}
	return output;
    }

}

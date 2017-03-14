package org.tpri.sc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>XML工具类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月18日
 */
public class XmlSupport{
	
	public static Logger logger = Logger.getLogger(XmlSupport.class);
	/**
	 * 根据xml字符串内容返回对应的根节点，如果解析错误，则返回null
	 * @param xmlContent
	 * @return
	 */
	public static Element getRootElement(String xmlContent){
		Document doc = getDocument(xmlContent);
		if (doc != null) {
			return doc.getRootElement();
		} else {
			return null;
		}
	}
	
	/**
	 * 根据xml字符串内容返回对应的Document对象，如果解析错误，则返回null
	 * @param xmlContent
	 * @return
	 */
	public static Document getDocument(String xmlContent) {
		if (xmlContent == null || xmlContent.equals("")) {
			return null;
		}
		xmlContent = filterInvalidXmlCharacter(xmlContent);
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xmlContent);;
		} catch (Exception e) {
			logger.debug(e);
		}
		return doc;
	}
	
	/**
	 * 根据url返回解析后的document对象
	 * @param url
	 * @return
	 * @throws DocumentException
	 */
	public static Document getDocument(URL url) {
        SAXReader reader = new SAXReader();
        Document doc = null;
		try {
			doc = reader.read(url);
		} catch (DocumentException e) {
			logger.debug(e);
		}
        return doc;
    }
	
	/**
	 * 根据url返回解析后的document对象
	 * @param url
	 * @return
	 * @throws DocumentException
	 */
	public static Document getDocumentBySAX(String url) {
        SAXReader reader = new SAXReader();
        Document doc = null;
		try {
			doc = reader.read(url);
		} catch (DocumentException e) {
			logger.debug(e);
		}
        return doc;
    }
	/**
	 * 根据根节点名称生成一个document并返回
	 * @param rootName
	 * @return
	 */
	public static Document buildDocumentByRootName(String rootName) {
		Document document = DocumentHelper.createDocument();
		document.addElement(rootName);
		return document;
	}
	
	/**
	 * 打印输出
	 * @param doc
	 * @return
	 */
	public static String getPrettyXmlString(Document doc) {
		if(doc == null) {
			return "";
		}
		StringWriter strWriter = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter xmlWriter = new XMLWriter(strWriter, format);
		try {
			xmlWriter.write(doc);
		} catch (Exception e) {
			logger.debug(e);
		}
		try {
			xmlWriter.close();
		} catch (IOException e) {
			logger.debug(e);
		}
		return strWriter.toString();
	}
	
	/**
	 * 将xml大字段中的内容保存到指定xml文件
	*@param doc
	*@param xmlFilePath
	*@param enCode
	*@return
	 */
	public static boolean saveXmlFile(String xmlContent,String xmlFilePath,String enCode) {
		if(xmlFilePath.isEmpty()) {
			return false;
		}
		Document doc = XmlSupport.getDocument(xmlContent);
		if (doc == null) 
			return false;
		
		boolean bOK = saveXmlFile(doc,xmlFilePath,enCode);
		if(!bOK) {
			return false;
		}
		return true;
	}

	/**
	 * 保存到xml文件
	*@param doc
	*@param xmlFilePath
	*@param enCode
	*@return
	 */
	public static boolean saveXmlFile(Document doc,String xmlFilePath,String enCode) {
		if(xmlFilePath.isEmpty()) {
			return false;
		}
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(enCode);
		File file = new File(xmlFilePath);//获得文件   
		
		if (file.exists()) {     
			 file.delete();     
		}  
		
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(new FileOutputStream(file), format);
			writer.write(doc);     
			writer.close();     
		} catch (UnsupportedEncodingException e) {
			logger.debug(e);
		} catch (FileNotFoundException e) {
			logger.debug(e);
		} catch (IOException e) {
			logger.debug(e);
		}  
		
		return true;
	}

	
    /**
     * 获取节点的属性值
     * @param element
     * @param attributeID
     * @return
     */
    public static String getNodeAttribute(Element element, String attributeID) {
        if( element == null || attributeID == null || attributeID.length() == 0 )
            return null;


        Attribute attribute = element.attribute(attributeID);
        if ( attribute == null )
            return null;

        return attribute.getValue();
    }

    /**
     * 过滤xml中的非法字符，替换成空字符串
     * @param xml
     * @return
     */
	private static String filterInvalidXmlCharacter(String xml) {
		if (xml == null) {
			return "";
		}
		char[] charactersInvalid = {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x0b,0x0c,0x0e,0x0f,0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,0x1a,0x1b,0x1c,0x1d,0x1e,0x1f};
		for (char c : charactersInvalid) {
			xml = xml.replaceAll(c + "", "");
		}
		return xml;
	}

}

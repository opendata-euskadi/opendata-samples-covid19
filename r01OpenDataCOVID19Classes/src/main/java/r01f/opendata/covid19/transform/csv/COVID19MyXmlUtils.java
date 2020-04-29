package r01f.opendata.covid19.transform.csv;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.IOException;
import java.io.InputStream;

public class COVID19MyXmlUtils{
  public static Document document(InputStream is) {
    try {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
    } catch (SAXException e) {
      throw new COVID19ParseException(e);
    }catch(IOException e){
    	throw new COVID19ParseException(e);
    }catch(ParserConfigurationException e){
    	throw new COVID19ParseException(e);
    }
  }

  public static NodeList searchForNodeList(Document document, String xpath) {
    try {
      return (NodeList) XPathFactory.newInstance().newXPath().compile(xpath)
          .evaluate(document, XPathConstants.NODESET);
    } catch (XPathExpressionException e) {
      throw new COVID19ParseException(e);
    }
  }

}

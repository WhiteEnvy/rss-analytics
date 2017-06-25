package rssToCsv;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import rssToCsv.Utils.EmailSender;

public class RssReader {
	static List<String> rssNews;
	static InputStream rssDoc;
	static List<RssItem> csvArr;
	static Document csvDoc;
	
	public static void createDB(){
		rssNews = new ArrayList<String>();
	}
	
	public static void getData(RssLink link){
		String rssSource = link.getSource();
		RssReader.createDB();
		URL newUrl = null;
		URLConnection newCon = null;
		
		try {
			newUrl = new URL(rssSource);
		} catch (MalformedURLException e) {
			EmailSender.SendEmail("Error with "+link.getSource(), e.toString());
			System.out.println(e);	
			RSSLinkManager.removeLinkByName(link.getName());
			return;
		}
       
		try {
			newCon = newUrl.openConnection();
		} catch (Exception e) {
			EmailSender.SendEmail("Error with "+link.getSource(), e.toString());
			System.out.println(e);
			RSSLinkManager.removeLinkByName(link.getName());	
			return;
		}
		
        try {
			rssDoc = newCon.getInputStream();
		} catch (Exception e) {
			EmailSender.SendEmail("Error with "+link.getSource(), e.toString());
			System.out.println(e);
			RSSLinkManager.removeLinkByName(link.getName());	
			return;
		}
	}
	
	public static void parse(RssLink rsslink) throws ParserConfigurationException, SAXException, IOException{
		String rssName = rsslink.getName();
		String rssSource = rsslink.getSource();
		
		csvArr = new ArrayList<RssItem>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        try{
        	csvDoc = builder.parse(RssReader.rssDoc);
        }
        catch(Exception e){
        	EmailSender.SendEmail("Error with "+rsslink.getSource(), e.toString());
        	RSSLinkManager.removeLinkByName(rssName);	
        	return;
        }
        
        
        NodeList items = csvDoc.getElementsByTagName("item");
        
        if(items.getLength() == 0){
        	EmailSender.SendEmail("Error with "+rsslink.getSource(), "This xml file isn't rss, there are no tags <item>");
        	RSSLinkManager.removeLinkByName(rssName);	
        	return;
        }
        
        for (int i = 0; i < items.getLength(); i++) {
        	NodeList list = items.item(i).getChildNodes();
        	String title = "";
    		String description = "";
    		String date = "";
    		String rss = rssName;
    		String source = rssSource;
    		String author = "";
    		String link = "";
        	
        	for(int j = 0; j < list.getLength(); j++){       		
        		Node elem = list.item(j);        		
        		switch(elem.getNodeName()){
        			case "title": title = Jsoup.parse(elem.getTextContent()).text(); break;
        			case "description": description = Jsoup.parse(elem.getTextContent()).text(); break;
        			case "pubDate": date = elem.getTextContent(); break;
        			case "author": author = elem.getTextContent(); break;
        			case "link": link = elem.getTextContent(); break;
        		}
        	}
        	
        	RssItem newItem = new RssItem(title, description, date, rss, source, author, link);
        	csvArr.add(newItem);
        }
	}
}

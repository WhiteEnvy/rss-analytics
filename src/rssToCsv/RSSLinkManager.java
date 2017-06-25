package rssToCsv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rssToCsv.Utils.FileManager;

public class RSSLinkManager {

	public static final RSSLinkManager instance = new RSSLinkManager();
	public static List<RssLink> rssLinks;
	private static final String fileName = "links.csv";

	private RSSLinkManager() {
		try {
			FileManager.createFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		rssLinks = new ArrayList<RssLink>();
		getLinksFromFile();
	}

	public static void getLinksFromFile() {
		
		List<String[]> links = CsvReader.parse(fileName, "\t");
		for (int i = 0; i < links.size(); i++) {			
			String[] link = links.get(i);
			RssLink obj = new RssLink(link[0], link[1], Integer.parseInt(link[2]),  Long.parseLong(link[3]), link[4], link[5]);
			rssLinks.add(obj);
		}
	}
	
	public static RssLink getLinkByName(String name) {
		for(int i = 0; i < rssLinks.size(); i++){
			if(rssLinks.get(i).getName().equals(name)){
				return rssLinks.get(i);
			}
		}		
		return null;
	}
	
	public static void removeLinkByName(String name){
		for(int i = 0; i < RSSLinkManager.rssLinks.size(); i++){
			if(RSSLinkManager.rssLinks.get(i).getName().equals(name)){
				RSSLinkManager.rssLinks.remove(i);
				break;
			}
		}
		RSSLinkManager.saveLinksToFile();
	}
	
	public static String getLinkNames() {
		String result = "";
		for(int i = 0; i < rssLinks.size(); i++){
			result += rssLinks.get(i).getName() + "\n";
		}
		
		return result;
	}
	
	public static String getRssDataByName(String name){
		RssLink link = RSSLinkManager.getLinkByName(name);
		if(link == null){
			return null;
		}
		
		String fileName = link.getCurrentFileName();
		String folderName = link.getFileFolder();
		
		List<String[]> items = CsvReader.parse(folderName + "/" + fileName, "\t");
		String result = "";
		
		for(int i = 0; i < items.size(); i++){
			result += Arrays.toString(items.get(i)) + "\n";
		}		
		
		return result;
	}

	public static void addLink(String name, String url, int interval, String fileName) {
		RssLink newLink = new RssLink(name, url, interval, fileName);
		rssLinks.add(newLink);
		saveLinksToFile();
	}

	public static void saveLinksToFile() {
		try (FileWriter file = new FileWriter("links.csv");
				BufferedWriter bw = new BufferedWriter(file);
				PrintWriter out = new PrintWriter(bw)) {
			for (int i = 0; i < rssLinks.size(); i++) {
				rssLinks.get(i).updateAllData();
				out.println(rssLinks.get(i).getAllData());
			}
			file.flush();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}

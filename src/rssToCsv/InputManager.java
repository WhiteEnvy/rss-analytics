package rssToCsv;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import rssToCsv.Utils.EmailSender;

public class InputManager {
	public static Scanner sc = new Scanner(System.in);	

	public static void init () {	
		
		if(EmailSender.Recipient.isEmpty()){
			System.out.println("\nWelcome! For correct work this application please add mails for report system\n\n");
			addMailAddress();
		}
		
		while(true){
			showMenu();
			
			String choose = sc.nextLine();
			switch(choose){
				case "1":	addNewRss(); break;
				case "2": showLinkNames(); break;
				case "3": showRssInfoByName(); break;
				case "4": showRssFeedByName(); break;
				case "5": changeRssInfoByName(); break;
				case "6":  removeRssLinkByName(); break;
				case "7":  addMailAddress(); break;
			}
		}
	}
	
	public static void showMenu() {
		System.out.println("---Menu---");
		System.out.println("1 - add new rss");
		System.out.println("2 - show all rss link names");
		System.out.println("3 - show rss info by name");
		System.out.println("4 - show rss feed by name");
		System.out.println("5 - change rss info");
		System.out.println("6 - remove rss by name");
		System.out.println("7 - add mail address for system");
		
		System.out.println("---/Menu---");
		System.out.print("Your choose >>");
		
	}
	
	private static void addMailAddress() {
		System.out.print("Enter email of receiver:");
		String receiver = sc.nextLine();
		
		if(!receiver.isEmpty()){
			EmailSender.Recipient = receiver;
		}
		
		try (FileOutputStream file = new FileOutputStream("config.csv");
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(file, "UTF8"));
				PrintWriter out = new PrintWriter(bw)) {

			out.println(EmailSender.Recipient);

			file.flush();

		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	private static void removeRssLinkByName(){
		System.out.print("Enter rss name:");
		String name = sc.nextLine();
		RssLink rssData = RSSLinkManager.getLinkByName(name);	
		
		if(rssData == null){
			System.out.println("ERROR: This rss doesn't exist");
			System.out.println("For continue - press 'Enter'");
			sc.nextLine();	
			return;
		}
		
		RSSLinkManager.removeLinkByName(name);		
		
		System.out.println("For continue - press 'Enter'");
		sc.nextLine();	
	}
	
	private static void showLinkNames(){
		String names = RSSLinkManager.getLinkNames();
		if(names.isEmpty()){
			System.out.println("No links");
		}
		else{
			System.out.println(names);
		}
		
		System.out.println("For continue - press 'Enter'");
		sc.nextLine();	
	}
	
	private static void changeRssInfoByName() {
		System.out.print("Enter rss name:");		
		RssLink rssData = RSSLinkManager.getLinkByName(sc.nextLine());		
		
		if(rssData == null){
			System.out.println("ERROR: This rss doesn't exist");
			System.out.println("For continue - press 'Enter'");
			sc.nextLine();	
			return;
		}
		
		String currentName =  rssData.getName();
		String currentSource =  rssData.getName();
		int currentInterval =  rssData.getUpdateTime();
		
		String name;
		String source;
		String interval;
		
		System.out.print("Enter new name or press 'Enter' for skip:");
		name = sc.nextLine().trim();
		System.out.print("Enter url or press 'Enter' for skip:");
		source = sc.nextLine().trim();
		System.out.print("Enter new interval or press 'Enter' for skip:");
		interval = sc.nextLine();
		
		RssLink obj = RSSLinkManager.getLinkByName(name);
		
		if(obj != null && name.equals(currentName) == false){
			System.out.println("ERROR: Link with this name already exists");
			System.out.println("For continue - press 'Enter'");
			sc.nextLine();	
			return;
		}
		
		if(name.isEmpty() == false){
			rssData.setName(name);
		}
		if(source.isEmpty() == false){
			rssData.setSource(source);
		}
		if(interval.isEmpty() == false){
			rssData.setUpdateTime(Integer.parseInt(interval));
		}		
		
		rssData.updateAllData();		
		RSSLinkManager.saveLinksToFile();
		
		System.out.println("For continue - press 'Enter'");
		sc.nextLine();	
		
	}
	
	private static void showRssFeedByName() {
		System.out.print("Enter rss name:");
		
		String rssData = RSSLinkManager.getRssDataByName(sc.nextLine());
		
		if(rssData == null){
			System.out.println("ERROR: This rss doesn't exist");
			return;
		}
		
		System.out.println(rssData);		
		System.out.println("For continue - press 'Enter'");
		sc.nextLine();		
	}
	
	private static void showRssInfoByName() {
		System.out.print("Enter rss name:");
		
		RssLink obj = RSSLinkManager.getLinkByName(sc.nextLine());
		
		if(obj == null){
			System.out.println("ERROR: This rss doesn't exist");
			System.out.println("For continue - press 'Enter'");
			sc.nextLine();
			return;
		}
		
		System.out.println("Source: " + obj.getSource());		
		System.out.println("Update interval" + obj.getUpdateTime());
		System.out.println("Last update: " + obj.getLastUpdate());
		
		System.out.println("For continue - press 'Enter'");
		sc.nextLine();	
	}
	
	private static void addNewRss() {
		String name;
		String source;
		String userInterval;	
		String fileName;
		int updateTime = RssLink.getDefaultUpdateTime();
		
		System.out.println("---Add New Rss---");
		System.out.print("Enter name:");
		name = sc.nextLine().trim();
		System.out.print("Enter url:");
		source = sc.nextLine().trim();
		System.out.print("Enter interval (optional) or press 'Enter':");
		userInterval = sc.nextLine();
		System.out.print("Enter file name:");
		fileName = sc.nextLine();
		
		RssLink obj = RSSLinkManager.getLinkByName(name);
		
		if(obj == null){			
			if(userInterval.isEmpty() == false){
				updateTime = Integer.parseInt(userInterval);
			}
			
			RSSLinkManager.addLink(name, source, updateTime, fileName);	
		}
		else{
			System.out.println("ERROR: Link with this name already exists");
		}
		
		System.out.println("For continue - press 'Enter'");
		sc.nextLine();
	}

}

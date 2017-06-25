package rssToCsv;

public class RssItem {
	String title;
	String description;
	String date;
	String rss;
	String source;
	String author;
	String link;
	String allData;
	
	public RssItem(String title, String description, String date, String rss, String source, String author, String link) {
		super();
		this.title = title;
		this.description = description;
		this.date = date;
		this.rss = rss;
		this.source = source;
		this.author = author;
		this.link = link;
		this.allData = title + "\t" + description + "\t" + date + "\t" + rss + "\t" + source + "\t" + author+ "\t" + link+ "\t";;
	}

	@Override
	public String toString() {
		return "titre: "+this.title +"\n"+"description: "+ this.description +
        		"\n"+"date: "+ this.date +"\n"+"rss: "+ this.rss +"\n"+"source: "+ this.source
        		+"\n"+"author: "+ this.author +"\n"+"link: "+ this.link;
	}

	
	
}

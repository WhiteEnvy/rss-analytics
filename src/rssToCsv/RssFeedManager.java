package rssToCsv;

import java.util.Date;
import java.util.List;

import rssToCsv.Utils.EmailSender;
import rssToCsv.Utils.FileManager;

public class RssFeedManager {

	public static void UpdateData() {
		List<RssLink> rssLinks = RSSLinkManager.instance.rssLinks;

		for (int i = 0; i < rssLinks.size(); i++) {
			try {
				CreateOrUpdateCsv(rssLinks.get(i));
			} catch (Exception e) {
				e.printStackTrace();
				EmailSender.SendEmail("Error", e.toString());
			}
		}
	}

	public static void CreateOrUpdateCsv(RssLink obj) throws Exception {
		String fileName = obj.getCurrentFileName();
		String folderName = obj.getFileFolder();
		String rssSource = obj.getSource();
		String rssName = obj.getName();

		RssReader.getData(obj);
		RssReader.parse(obj);

		Date currentDate = new Date();
		long difference = (currentDate.getTime() - obj.getLastUpdate()) / 1000;

		if (difference < obj.getUpdateTime()) {
			return;
		}

		obj.setLastUpdate(currentDate.getTime());
		RSSLinkManager.saveLinksToFile();

		if (FileManager.isFileOrDirectoryExists(folderName + "/" + fileName)) {
			Date lastDate = CsvReader.getLastDate(folderName + "/" + fileName);
			for (int i = 0; i < RssReader.csvArr.size(); i++) {
				Date itemDate = new Date(RssReader.csvArr.get(i).date);
				if (itemDate.after(lastDate) == false) {
					RssReader.csvArr.remove(i);
					i--;
				}
			}
			CsvReader.updateFile(obj, false);
		} else {
			CsvReader.updateFile(obj, true);
		}

		if(RssReader.rssDoc != null){
			RssReader.rssDoc.close();
		}		
	}

}

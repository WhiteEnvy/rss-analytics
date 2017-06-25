package rssToCsv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rssToCsv.Utils.FileManager;

public class CsvReader {
	static List<RssItem> csvArr;

	public static List<String[]> parse(String csvFileUrl, String separator) {
		String line = "";
		List<String[]> arrays = new ArrayList<String[]>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFileUrl))) {
			while ((line = br.readLine()) != null) {
				line = line.trim();
				String[] item = line.split(separator);
				arrays.add(item);
			}
			return arrays;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getLastDate(String csvFileUrl) {
		csvArr = new ArrayList<RssItem>();
		Date lastDate = new Date("Sat, 24 Jun 1980 00:49:42 GMT");
		List<String[]> items = CsvReader.parse(csvFileUrl, "\t");

		for (int i = 1; i < items.size(); i++) {
			String[] item = items.get(i);
			RssItem newItem = new RssItem(item[0], item[1], item[2], item[3], item[4], item[5], item[6]);
			csvArr.add(newItem);
			Date itemDate = new Date(item[2]);
			if (itemDate.after(lastDate)) {
				lastDate = itemDate;
			}
		}
		return lastDate;
	}

	public static void updateFile(RssLink link, boolean isNewFile) throws IOException {
		if(RssReader.csvArr.size() == 0){
			return;
		}
		
		String folderName = link.getFileFolder();
		FileManager.createFolder(folderName);
		String fileName = link.getCurrentFileName();

		for (int i = 0; i < RssReader.csvArr.size(); i++) {
			File currentFile = new File(folderName + "/" + fileName);
			if (currentFile.length() < 1000) {
				try (FileOutputStream file = new FileOutputStream(folderName + "/" + fileName, true);
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(file, "UTF8"));
						PrintWriter out = new PrintWriter(bw)) {

					if (isNewFile == true) {
						isNewFile = false;
						String header = "title\tdescription\tdate\trss\tsource\tauthor\tlink";
						out.println(header);
					}

					out.println(RssReader.csvArr.get(i).allData);

					file.flush();

				} catch (IOException e) {
					System.out.println(e);
				}
			} else {
				long countFiles = Files.list(Paths.get(folderName)).count();

				DateFormat formatter = new SimpleDateFormat("MM_dd_yyyy HH_mm");
				Date d = new Date();
				fileName = link.getOriginalFileName() + "_" + (countFiles + 1) + "_" + formatter.format(d) + ".csv";
				FileManager.createFile(folderName + "/" + fileName);

				link.setCurrentFileName(fileName);
				RSSLinkManager.saveLinksToFile();

				try (FileOutputStream file = new FileOutputStream(folderName + "/" + fileName, true);
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(file, "UTF8"));
						PrintWriter out = new PrintWriter(bw)) {

					String header = "title\tdescription\tdate\trss\tsource\tauthor\tlink";
					out.println(header);

					out.println(RssReader.csvArr.get(i).allData);

					file.flush();

				} catch (IOException e) {
					System.out.println(e);
				}
			}

		}

	}

}

package rssToCsv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rssToCsv.Utils.FileManager;

public class RssLink {
	private String name;
	private String source;
	private int updateTime;
	private long lastUpdate;
	private String currentFileName;
	private String allData;
	private String originalFileName;
    private String FileFolder;
    private static int defaultUpdateTime = 900;
    
	public RssLink(String name, String source, int updateTime, String currentFileName) {
		super();
		this.name = name;
		this.source = source;
		this.updateTime = updateTime;
		this.lastUpdate = (int) (new Date().getTime()/1000);
		this.originalFileName = currentFileName;
		this.currentFileName = currentFileName + ".csv";
		this.FileFolder = this.name;
		updateAllData();
	}
	
	public RssLink(String name, String source, int updateTime, long lastUpdate, String OriginalFileName, String currentFileName) {
		this.name = name;
		this.source = source;
		this.updateTime = updateTime;
		this.lastUpdate = lastUpdate;
		this.originalFileName = OriginalFileName;
		this.currentFileName = currentFileName;
		this.FileFolder = this.name;
		updateAllData();
	}
	
	public static int getDefaultUpdateTime() {
		return defaultUpdateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(int updateTime) {
		this.updateTime = updateTime;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}
	
	public void setLastUpdate(long lastUpdate){
		this.lastUpdate = lastUpdate;
	}

	public String getCurrentFileName() {
		return currentFileName;
	}

	public String getAllData() {
		return allData;
	}
	
	public void updateAllData() {
		this.allData = name + "\t" + source + "\t" + updateTime + "\t" + lastUpdate + "\t" + originalFileName + "\t" +currentFileName + "\t" + FileFolder;
	}

	public void setCurrentFileName(String currentFileName) {
		this.currentFileName = currentFileName;
	}
	
	public String getOriginalFileName() {
		return originalFileName;
	}
	
	public void setOriginalFileName(String fileName) {
		this.originalFileName = fileName;
	}
	
	public String getFileFolder() {
		return FileFolder;
	}
	
	public void setFileFolder(String folderName) {
		this.FileFolder = folderName;
	}
}

package rssToCsv.Utils;

import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

	public static void createFolder(String directoryPath, String directoryName) throws IOException {

		Path directoryFullPath = Paths.get(directoryPath, directoryName);

		if (!FileManager.IsFileOrDirectoryExists(directoryFullPath)) {
			Files.createDirectories(directoryFullPath);
		}
	}
	
	public static void createFolder(String directoryName) throws IOException {

		Path directoryFullPath = Paths.get(directoryName);

		if (!FileManager.IsFileOrDirectoryExists(directoryFullPath)) {
			Files.createDirectories(directoryFullPath);
		}
	}

	public static void createFile(String fileName) throws IOException {
		Path fileFullPath = Paths.get(fileName);
		createFile(fileFullPath);
	}

	public static void createFile(String filePath, String fileName) throws IOException {
		Path fileFullPath = Paths.get(filePath, fileName);
		createFile(fileFullPath);
	}

	public static void createFile(Path fullFilePath) throws IOException {

		if (!FileManager.IsFileOrDirectoryExists(fullFilePath)) {
			Files.createFile(fullFilePath);
		}
	}

	public static boolean isFileOrDirectoryExists(String objectStringFullPath) {
		Path objectFullPath = Paths.get(objectStringFullPath);
		return IsFileOrDirectoryExists(objectFullPath);
	}

	public static boolean IsFileOrDirectoryExists(Path objectFullPath) {
		return Files.exists(objectFullPath);
	}
}

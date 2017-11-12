package com.scraper.library;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Temp {

	public static void main(String[] args) {
		// final File folder = new File("/Users/asthammishra/Downloads/NH_Scans");
		// listFilesForFolder(folder);

		try {

			File f = new File("/Users/asthammishra/Downloads/untitled text 3.txt");

			System.out.println("Reading files using Apache IO:");

			List<String> lines = FileUtils.readLines(f, "UTF-8");

			boolean flag = true;

			for (String line : lines) {
				if (!line.equalsIgnoreCase("ARCHIVE/.DS_Store")) {
					if (flag) {
						System.out.print(line);
						flag = false;
					} else {
						System.out.println("\t\t" + line);
						flag = true;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void listFilesForFolder(final File folder) {

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				System.out.println(folder.getName() + File.separator + fileEntry.getName());
			}
		}
	}

}

package com.example.indiebeauty.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class FileProcessUtil {
	public static String changePathBasedOnOS(String path) {
		return path.replace("/", File.separator);
	}
	
	public static boolean isProductImageExistsInServer(String fileName) {
		String workingDirPath = System.getProperty("user.dir");
		String productImgPath = "/src/main/resources/static/img/product-img/";
		
		String filePath = workingDirPath + productImgPath + fileName;
		filePath = changePathBasedOnOS(filePath);
		File file = new File(filePath);
		
//		return Files.exists(path);
		return file.exists();
	}
}

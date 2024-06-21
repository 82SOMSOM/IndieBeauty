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
		
		Path path = Paths.get(filePath);
		File file = new File(filePath);
		
		System.out.println("================ isProductImageExistsInServer: " + path.toString());
		System.out.println("exits : " + file.exists());
		
//		return Files.exists(path);
		return file.exists();
	}
}

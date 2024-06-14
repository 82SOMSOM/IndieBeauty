package com.example.indiebeauty.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.indiebeauty.controller.UploadProduct;
import com.example.indiebeauty.domain.Category;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.ProductImage;
import com.example.indiebeauty.exception.FileUploadException;
import com.example.indiebeauty.repository.ProductImageRepository;
import com.example.indiebeauty.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private ProductImageRepository prodImgRepo;
	
	public List<Product> getProductByCategoryId(int categoryId) {
		return prodRepo.findByCategory_CategoryId(categoryId);
	}
	
	public Product getProductById(int productId) {
		Optional<Product> result =  prodRepo.findById(productId);
		if (result.isPresent()) {
			return result.get();
		} 
		return null;
	}
	
	public List<Product> getProductListByKeyword(String keyword) {
		List<Product> searchByProductName = prodRepo.findByNameLike(keyword);
		List<Product> searchByCategoryName = prodRepo.findByCategory_NameLike(keyword);
		
		List<Product> searchResult = new ArrayList<>(searchByProductName);
		searchResult.addAll(searchByCategoryName);
		
		return searchResult;
	}
	
	public List<Product> getRecentlyRegisteredProductList() {
		return prodRepo.findByOrderByDateDesc();
	}
	
	public List<Product> getRecentlyRegisteredProductListByCategory(int categoryId) {
		return prodRepo.findByCategory_CategoryIdOrderByDateDesc(categoryId);
	}
	
	public static String changePathBasedOnOS(String path) {
		return path.replace("/", File.separator);
	}
	
	public static String generateUniqueFileName(String originalFileName) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStamp = dateFormat.format(new Date());
		
		Random random = new Random();
		String randomNumer = Integer.toString(random.nextInt(Integer.MAX_VALUE));
		
		return (timeStamp + randomNumer + originalFileName);
	}
	
	@Transactional
	public String saveImage(MultipartFile file) throws FileUploadException {
		String workingDirPath = System.getProperty("user.dir");
		String productImgPath = "/src/main/resources/static/img/product-img";
		String fileName = generateUniqueFileName(file.getOriginalFilename());

		Path filePath = Paths.get(workingDirPath, changePathBasedOnOS(productImgPath), fileName);
		
		try {
			Files.copy(file.getInputStream(), filePath);
			return fileName;
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileUploadException("상품 이미지 등록에 실패했습니다. 관리자에게 문의하세요.");
		}
	}
	
	@Transactional
	public boolean registerProduct(UploadProduct uploadProduct) throws FileUploadException {
		int categoryId = uploadProduct.getCategoryId();
		String name = uploadProduct.getName();
		String description = uploadProduct.getDescription();
		Date date = new Date();
		int price = uploadProduct.getPrice();
		int stock = uploadProduct.getStock();
		
		Product product = new Product(0, new Category(categoryId, null), name, description, null, date, price, stock);
		
		Product newProduct = prodRepo.save(product);
		int newProductId = newProduct.getProductId();

		List<ProductImage> productImageList = new ArrayList<ProductImage>();
		for (MultipartFile image : uploadProduct.getDetailImageList()) {
			String imageFileName = saveImage(uploadProduct.getTitleImage());
			ProductImage productIamge = new ProductImage(0, newProductId, imageFileName, 0);
			productImageList.add(productIamge);
		}
		
		String titleImageFileName = saveImage(uploadProduct.getTitleImage());
		ProductImage productTitleImage = new ProductImage(0, newProductId, titleImageFileName, 1);
		productImageList.add(productTitleImage);
		
		for (ProductImage pi : productImageList) {
			prodImgRepo.save(pi);
		}
		
		return true;
	}
}
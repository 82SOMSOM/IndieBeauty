package com.example.indiebeauty.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.indiebeauty.controller.UploadProduct;
import com.example.indiebeauty.domain.Category;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.ProductImage;
import com.example.indiebeauty.exception.FileUploadException;
import com.example.indiebeauty.exception.NoSuchProductException;
import com.example.indiebeauty.repository.ProductImageRepository;
import com.example.indiebeauty.repository.ProductRepository;
import com.example.indiebeauty.util.FileProcessUtil;

@Service
@Transactional
public class ProductService {
	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private ProductImageRepository prodImgRepo;
	@Autowired
	private ProductImageService prodImgService;
	
	private static int itemsPerPage = 9;
	
	private static Pageable getPageableForShop(int pageNum, int imtemPerPage) {		// 상품 조회에 필요한 Pageable 객체 생성 메소드
		Sort sort = Sort.by(new Order(Sort.Direction.DESC, "productId"));
		Pageable pageable = PageRequest.of(pageNum, itemsPerPage, sort);
		
		return pageable;
	}

	@Transactional(readOnly = true)
	public Map<String, Object> getProductByCategoryIdWithTitleImage(int categoryId, int pageNum, int itemPerPage) {	// categoryId 기준 상품 반환 메소드 (ProductImage는 타이틀 이미지만 반환)
		Pageable pageable = getPageableForShop(pageNum - 1, itemPerPage);
		
		Page<Product> result = prodRepo.findByCategory_CategoryId(categoryId, pageable);
		int totalPages = result.getTotalPages();
		List<Product> products = result.getContent();
		
		for (Product product : products) {
			List<ProductImage> piList = product.getImageList();
			
			ProductImage titleImage = prodImgService.getTitleImage(product.getImageList());
			if (titleImage != null 	
					&& FileProcessUtil.isProductImageExistsInServer(titleImage.getImageUrl())) {
				piList = piList.stream()
	                       .filter(img -> img.getIsTitleImg() == 1)
	                       .collect(Collectors.toList());
			} else {
				System.out.println("Singleton 처리");
				List<ProductImage> noImageList = Collections.singletonList(new ProductImage(0, 0, "no-image.png", 0));
				product.setImageList(noImageList); 
			}
		}
		
		System.out.println("================ getProductByCategoryIdWithTitleImage");
		 for (Product product: products) {
			 System.out.println(product.getImageList());
		 }
		 
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 resultMap.put("products", products);
		 resultMap.put("totalPages", totalPages);
		
		return resultMap;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> getAllProductWithTitleImage(int pageNum, int itemPerPage) {	// 전체 상품 반환 메소드 (ProductImage는 타이틀 이미지만 반환)
		System.out.println("================ getAllProductWithTitleImage");
		
		Pageable pageable = getPageableForShop(pageNum - 1, itemPerPage);
		
		Page<Product> result = prodRepo.findAll(pageable);
		int totalPages = result.getTotalPages();
		List<Product> products = result.getContent();
		
		for (Product product : products) {
			System.out.println("결과 product" + product.toString());
			List<ProductImage> piList = product.getImageList();
			
			ProductImage titleImage = prodImgService.getTitleImage(product.getImageList());
			if (titleImage != null) {
				System.out.println(product.getName() + " title image: " + titleImage.toString());
			} else {
				System.out.println(product.getName() + " title image: null");
			}
			
			if (titleImage != null 	
					&& FileProcessUtil.isProductImageExistsInServer(titleImage.getImageUrl())) {
				piList = piList.stream()
	                       .filter(img -> img.getIsTitleImg() == 1)
	                       .collect(Collectors.toList());
			} else {
				System.out.println("Singleton 처리");
				List<ProductImage> noImageList = Collections.singletonList(new ProductImage(0, 0, "no-image.png", 0));
				product.setImageList(noImageList); 
			}
		}
		
		 for (Product product: products) {
			 System.out.println(product.getImageList());
		 }
		 
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 resultMap.put("products", products);
		 resultMap.put("totalPages", totalPages);
		
		return resultMap;
	}
	
	@Transactional(readOnly = true)
	public Product getProductById(int productId) throws NoSuchProductException {	// productId 기준 상품 반환 메소드
		Optional<Product> result =  prodRepo.findById(productId);
		if (result.isPresent()) {
			Product product = result.get();
			
			for (ProductImage pi : product.getImageList()) {
				if (!FileProcessUtil.isProductImageExistsInServer(pi.getImageUrl())) {
					pi.setImageUrl("no-image.png");
				}
				System.out.println(pi.toString());
			}
			
			return result.get();
		} else {
			throw new NoSuchProductException("상품이 없습니다.");
		}
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> getProductListByKeywordWithTitleImage(String keyword, int pageNum) {	// 상품 검색 메소드
		List<Product> searchByProductName = prodRepo.findByNameContainingIgnoreCase(keyword);
		List<Product> searchByCategoryName = prodRepo.findByCategory_NameContainingIgnoreCase(keyword);
		List<Product> searchByDescriptionName = prodRepo.findByDescriptionContainingIgnoreCase(keyword);
		
		// 상품 검색 시 정렬 기준 - 1. 이름, 2. 설명, 3. 카테고리
		List<Product> searchResult = new ArrayList<>(searchByProductName);
		searchResult.addAll(searchByDescriptionName);
		searchResult.addAll(searchByCategoryName);
		
		int searchResultSize = searchResult.size();
		int startIndex = pageNum * itemsPerPage;	// 페이지 첫 번째 인덱스
		int endIndex = Math.min(startIndex + itemsPerPage, searchResultSize);	// 페이지 마지막 인덱스
		
		List<Product> products = new ArrayList<>();
		for (int i = startIndex; i <= endIndex; i++) {	// pageNum 페이지에 보이는 상품 ArrayList에 담기
			products.add(searchResult.get(i));
		}
		
		for (Product product : products) {
			List<ProductImage> piList = product.getImageList();
			
			ProductImage titleImage = prodImgService.getTitleImage(product.getImageList());
			if (titleImage != null 	
					&& FileProcessUtil.isProductImageExistsInServer(titleImage.getImageUrl())) {
				piList = piList.stream()
	                       .filter(img -> img.getIsTitleImg() == 1)
	                       .collect(Collectors.toList());
			} else {
				System.out.println("Singleton 처리");
				List<ProductImage> noImageList = Collections.singletonList(new ProductImage(0, 0, "no-image.png", 0));
				product.setImageList(noImageList); 
			}
		}
		
		System.out.println("================ getProductByCategoryIdWithTitleImage");
		 for (Product product: searchResult) {
			 System.out.println(product.getImageList());
		 }
		 
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 resultMap.put("products", searchResult);
		 resultMap.put("totalPages", (searchResultSize + itemsPerPage - 1) / itemsPerPage);
		
		return resultMap;
	}
	
	@Transactional(readOnly = true)
	public List<Product> getRecentlyRegisteredProductList() {	// 최근 등록순 상품 반환 메소드
		return prodRepo.findByOrderByDateDesc();
	}
	
	@Transactional(readOnly = true)
	public List<Product> getRecentlyRegisteredProductListByCategory(int categoryId) {	// 카테고리 기준 최근 등록순 상품 반환 메소드
		return prodRepo.findByCategory_CategoryIdOrderByDateDesc(categoryId);
	}
	
	public static String changePathBasedOnOS(String path) {		// OS에 따른 경로 구분자 변환 메소드
		return path.replace("/", File.separator);
	}
	
	public static String generateUniqueFileName(String originalFileName) {	// 파일 이름 생성 메소드
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStamp = dateFormat.format(new Date());
		
		Random random = new Random();
		String randomNumer = Integer.toString(random.nextInt(Integer.MAX_VALUE));
		
		return (timeStamp + randomNumer + originalFileName);
	}
	
	public String saveImage(MultipartFile file) throws FileUploadException {	// 이미지 저장 메소드
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
	
	public int registerProduct(UploadProduct uploadProduct) throws FileUploadException {	// 상품 등록 메소드
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
			String imageFileName = saveImage(image);
			ProductImage productIamge = new ProductImage(0, newProductId, imageFileName, 0);
			productImageList.add(productIamge);
			
			System.out.println("========== registerProduct ===========");
			System.out.println(productIamge.toString());
		}
		
		String titleImageFileName = saveImage(uploadProduct.getTitleImage());
		ProductImage productTitleImage = new ProductImage(0, newProductId, titleImageFileName, 1);
		productImageList.add(productTitleImage);
		
		for (ProductImage pi : productImageList) {
			prodImgRepo.save(pi);
		}
		
		for (ProductImage pi : productImageList) {
			System.out.println(pi);
		}
		
		return newProductId;
	}
}
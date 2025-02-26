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
import com.example.indiebeauty.domain.SellerInfo;
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
	
	private static Pageable getPageableForShop(int pageNum, int itemPerPage) {		// 상품 조회에 필요한 Pageable 객체 생성 메소드
		Sort sort = Sort.by(new Order(Sort.Direction.DESC, "productId"));
		Pageable pageable = PageRequest.of(pageNum, itemPerPage, sort);
		
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
				List<ProductImage> noImageList = Collections.singletonList(new ProductImage(0, 0, "no-image.png", 0));
				product.setImageList(noImageList); 
			}
		}
		 
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 resultMap.put("products", products);
		 resultMap.put("totalPages", totalPages);
		
		return resultMap;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> getAllProductWithTitleImage(int pageNum, int itemPerPage) {	// 전체 상품 반환 메소드 (ProductImage는 타이틀 이미지만 반환)
		Pageable pageable = getPageableForShop(pageNum - 1, itemPerPage);
		
		Page<Product> result = prodRepo.findAll(pageable);
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
				List<ProductImage> noImageList = Collections.singletonList(new ProductImage(0, 0, "no-image.png", 0));
				product.setImageList(noImageList); 
			}
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
			}
			
			return result.get();
		} else {
			throw new NoSuchProductException("상품이 없습니다.");
		}
	}
	
	private static List<Product> mergeAndRemoveDuplicates(@SuppressWarnings("unchecked") List<Product>... lists) {
        HashMap<Integer, Product> productMap = new HashMap<>();

        // 각 리스트에서 중복 제거
        for (List<Product> productList : lists) {
            for (Product product : productList) {
                productMap.put(product.getProductId(), product);
            }
        }

        // HashMap에서 List로 변환
        return new ArrayList<>(productMap.values());
    }
	
	@Transactional(readOnly = true)
	public Map<String, Object> getProductListByKeywordWithTitleImage(String keyword, int pageNum, int itemPerPage) {	// 상품 검색 메소드
		List<Product> searchByProductName = prodRepo.findByNameContainingIgnoreCase(keyword);
		List<Product> searchByCategoryName = prodRepo.findByCategory_NameContainingIgnoreCase(keyword);
		List<Product> searchByDescriptionName = prodRepo.findByDescriptionContainingIgnoreCase(keyword);
		
		// 상품 검색 시 정렬 기준 - 1. 이름, 2. 설명, 3. 카테고리
		@SuppressWarnings("unchecked")
		List<Product> searchResult = mergeAndRemoveDuplicates(searchByProductName, searchByDescriptionName, searchByCategoryName);
		
		int searchResultSize = searchResult.size();
		int startIndex = (pageNum - 1) * itemPerPage;	// 페이지 첫 번째 인덱스
		int endIndex = Math.min(startIndex + itemPerPage, searchResultSize);	// 페이지 마지막 인덱스
		
		List<Product> products = new ArrayList<>();
		for (int i = startIndex; i <= endIndex; i++) {	// pageNum 페이지에 보이는 상품 ArrayList에 담기
			if (searchResult.size() > i) {
				products.add(searchResult.get(i));
			}
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
				List<ProductImage> noImageList = Collections.singletonList(new ProductImage(0, 0, "no-image.png", 0));
				product.setImageList(noImageList); 
			}
		}
		
		 
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 resultMap.put("products", searchResult);
		 resultMap.put("totalPages", (searchResultSize + itemPerPage - 1) / itemPerPage);
		
		return resultMap;
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
	
	public int registerProduct(UploadProduct uploadProduct, SellerInfo sellerInfo) throws FileUploadException {	// 상품 등록 메소드
		int categoryId = uploadProduct.getCategoryId();
		String name = uploadProduct.getName();
		String description = uploadProduct.getDescription();
		Date date = new Date();
		int price = uploadProduct.getPrice();
		int stock = uploadProduct.getStock();
		
		Product product = new Product(0, new Category(categoryId, null), name, description, null, date, price, stock, sellerInfo);
		
		Product newProduct = prodRepo.save(product);
		int newProductId = newProduct.getProductId();

		List<ProductImage> productImageList = new ArrayList<ProductImage>();
		for (MultipartFile image : uploadProduct.getDetailImageList()) {
			String imageFileName = saveImage(image);
			ProductImage productIamge = new ProductImage(0, newProductId, imageFileName, 0);
			productImageList.add(productIamge);
		}
		
		String titleImageFileName = saveImage(uploadProduct.getTitleImage());
		ProductImage productTitleImage = new ProductImage(0, newProductId, titleImageFileName, 1);
		productImageList.add(productTitleImage);
		
		for (ProductImage pi : productImageList) {
			prodImgRepo.save(pi);
		}
		
		return newProductId;
	}
}
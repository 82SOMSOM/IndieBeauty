package com.example.indiebeauty.service;

import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.ProductImage;
import com.example.indiebeauty.repository.ProductImageRepository;
import com.example.indiebeauty.repository.RankRepository;
import com.example.indiebeauty.util.FileProcessUtil;

@Service
public class RankService {
    
    @Autowired
    private RankRepository rankRepository;

    @Autowired
    private ProductImageService prodImgService;

    private static int itemsPerPage = 9;

    private static Pageable getPageableForShop(int pageNum, int itemsPerPage) {
        return PageRequest.of(pageNum, itemsPerPage);
    }

	@Transactional(readOnly = true)
	public Map<String, Object> getAllProductWithTitleImage(int pageNum, int itemPerPage) {	// 전체 상품 반환 메소드 (ProductImage는 타이틀 이미지만 반환)
		System.out.println("================ getAllProductWithTitleImage");
		
		Pageable pageable = getPageableForShop(pageNum - 1, itemPerPage);
		
		Page<Product> result = rankRepository.findAllOrderByAvgStarDesc(pageable);
		int totalPages = result.getTotalPages();
		List<Product> products = result.getContent();
		
		for (Product product : products) {
			System.out.println("rank 결과 product" + product.toString());
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
}

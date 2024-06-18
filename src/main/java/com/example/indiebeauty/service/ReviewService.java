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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.indiebeauty.controller.UploadProduct;
import com.example.indiebeauty.controller.UploadReview;
import com.example.indiebeauty.controller.UserSession;
import com.example.indiebeauty.domain.Category;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.ProductImage;
import com.example.indiebeauty.domain.Review;
import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.exception.FileUploadException;
import com.example.indiebeauty.repository.ProductImageRepository;
import com.example.indiebeauty.repository.ProductRepository;
import com.example.indiebeauty.repository.ReviewRepository;
import com.example.indiebeauty.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
	
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	
//	public Review saveReview(Review review) {
//		return reviewRepository.save(review);
//	}
	
	public void deleteReview(int reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public Optional<Review> findReviewById(int reviewId) {
        return reviewRepository.findById(reviewId);
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
		String productImgPath = "/src/main/resources/static/img/review-img";
		String fileName = generateUniqueFileName(file.getOriginalFilename());

		Path filePath = Paths.get(workingDirPath, changePathBasedOnOS(productImgPath), fileName);
		
		try {
			Files.copy(file.getInputStream(), filePath);
			return fileName;
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileUploadException("리뷰 이미지 등록에 실패했습니다. 관리자에게 문의하세요.");
		}
	}

	@Transactional
	public int registerReview(UploadReview uploadReview) throws FileUploadException {
		
        String userId = uploadReview.getUserId(); // 사용자 ID (String)
        Date reviewDate = new Date(); // 현재 날짜        
        String content = uploadReview.getContent(); // 리뷰 내용
        MultipartFile imageFile = uploadReview.getImageUrl();
        float star = uploadReview.getStar(); // 별점 (float)
        int productId = uploadReview.getProductId(); // 제품 ID (int)

        logger.info("User ID: {}", userId);
        logger.info("Product ID: {}", productId);
        logger.info("Review Date: {}", reviewDate);
        logger.info("Content: {}", content);
        logger.info("Image File Name: {}", imageFile != null ? imageFile.getOriginalFilename() : "No Image");
        logger.info("Star: {}", star);
        logger.info("여기까지 완료==============================================");

        // 이미지 파일 저장
        String imageUrl = saveImage(imageFile);

        // 유저 정보 및 제품 정보 로드
        Optional<UserInfo> userInfoOptional = userRepository.findById(userId);
        if (!userInfoOptional.isPresent()) {
            logger.error("User not found with ID: {}", userId);
            throw new IllegalArgumentException("User not found");
        }
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            logger.error("Product not found with ID: {}", productId);
            throw new IllegalArgumentException("Product not found");
        }

        UserInfo userInfo = userInfoOptional.get();
        Product product = productOptional.get();

        Review review = new Review();
        review.setReviewDate(reviewDate);
        review.setContent(content);
        review.setImageUrl(imageUrl);
        review.setStar(star);
        review.setUserId(userId);
        review.setProductId(productId);
        logger.info("UserInfo object loaded: {}", userInfo);
        logger.info("Product object loaded: {}", product);
        review.setUserInfo(userInfo);
        review.setProduct(product);
        logger.info("Review object with UserInfo and Product set: {}", review);

        logger.info("Review object created: {}", review);
        logger.info("==============================================");

        try {
            Review savedReview = reviewRepository.save(review);
            logger.info("Saved Review object: {}", savedReview);
            logger.info("==============================================");
            logger.info("Saved Review ID: {}", savedReview.getReviewId());
            return savedReview.getReviewId();
        } catch (Exception e) {
            logger.error("Error saving review: ", e);
            throw new RuntimeException("Error saving review", e);
        }
	}
}


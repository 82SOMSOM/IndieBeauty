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
import com.example.indiebeauty.controller.UploadReview;
import com.example.indiebeauty.domain.Category;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.ProductImage;
import com.example.indiebeauty.domain.Review;
import com.example.indiebeauty.exception.FileUploadException;
import com.example.indiebeauty.repository.ProductImageRepository;
import com.example.indiebeauty.repository.ProductRepository;
import com.example.indiebeauty.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
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
//<<<<<<< Updated upstream
//=======
//		
//		UserSession userSession = (UserSession) session.getAttribute("userSession");
//		
//		if (userSession == null) return "redirect:/login"; // 가입한 사용자만 review 작성 가능
//		
//		Product product = productService.getProductById(productId);
//        uploadReview.setProduct(product);
//        uploadReview.setUserId(userSession.getUserInfo().getUserid());
//		
//>>>>>>> Stashed changes
		// UploadReview에서 필요한 필드 추출
        String userId = uploadReview.getUserId(); // 사용자 ID (String)
        Date reviewDate = new Date(); // 현재 날짜
        String content = uploadReview.getContent(); // 리뷰 내용
        MultipartFile imageFile = uploadReview.getImageUrl();
        float star = uploadReview.getStar(); // 별점 (float)
        int productId = uploadReview.getProductId(); // 제품 ID (int)
        
        // 이미지 파일 저장
        String imageUrl = saveImage(imageFile);
        
        // 새 Review 객체 생성
        Review review = new Review();
        review.setUserId(userId);
        review.setReviewDate(reviewDate);
        review.setContent(content);
        review.setImageUrl(imageUrl); // 이미지 URL을 저장
        review.setStar(star);
        review.setProductId(productId);
        
        Review newReview = reviewRepository.save(review);

        // 리뷰 저장
        reviewRepository.save(review);
        int newReviewId = newReview.getReviewId();

        return newReviewId;
	}
}

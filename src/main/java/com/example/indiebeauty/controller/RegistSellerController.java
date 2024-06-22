package com.example.indiebeauty.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.example.indiebeauty.service.IndiebeautyFacade;
import com.example.indiebeauty.service.SellerFormValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class RegistSellerController {
	private static final Logger logger = LoggerFactory.getLogger(RegistSellerController.class);
	
	@Autowired
	private IndiebeautyFacade indiebeauty;
	public void setIndiebeautyFacade (IndiebeautyFacade indiebeauty) {
		this.indiebeauty = indiebeauty;
	}
	@Autowired
	private SellerFormValidator validator;
	public void setValidator(SellerFormValidator validator) {
		this.validator = validator;
	}
	
	@ModelAttribute("sellerForm")
	public SellerForm formBackingObject(HttpServletRequest request) throws Exception{
		logger.info("formbacking method 이동");
		SellerSession sellerSession = (SellerSession)WebUtils.getSessionAttribute(request, "sellerSession");
		if (sellerSession != null) {	// edit an existing user
			return new SellerForm(
				indiebeauty.getSellerInfo(sellerSession.getSellerInfo().getSellerid()));
		}
		else {	// create a new Seller
			return new SellerForm();
		}
	}
		
//	회원가입
	@GetMapping("/seller/checkSellerId")
	public ResponseEntity<?> checkSellerId(@RequestParam String sellerid) {
	    boolean isAvailable = !indiebeauty.existsSellerId(sellerid);
	    return ResponseEntity.ok().body(Map.of("available", isAvailable));
	}
	@RequestMapping(value = "/sellerSignin", method = RequestMethod.GET)
    public String showSellerRegistrationForm() {
		logger.info("판매자 회원가입페이지 이동");
        return "sellerSignin";  // 회원가입 페이지로 이동
    }
	@RequestMapping(value = "/seller/registerSeller", method = RequestMethod.POST)
	public String registerSeller(@ModelAttribute("sellerForm") SellerForm sellerForm, BindingResult result, HttpSession session) {
		logger.info("registerSeller method 이동");
		validator.validate(sellerForm, result);
		logger.info("Seller validator 이동");
		if (result.hasErrors()) {
			logger.error("error 발생");
			return "sellerSignin";
		}
		try {
			indiebeauty.insertSellerInfo(sellerForm.getSellerInfo());
			logger.info("Seller 회원가입 완료");
			return "redirect:/sellerLogin";
		} catch (DataIntegrityViolationException ex) {
			result.rejectValue("SellerInfo.sellerid", "SELLER_ID_ALREADY_EXISTS", "User ID already exists: choose a different ID.");
			return "sellerSignin";
		}
	}
	
//	판매자 정보수정
	@GetMapping("/showSellerInfo")
	public String showEditSellerForm(HttpServletRequest request, Model model) {
		SellerSession sellerSession = (SellerSession)WebUtils.getSessionAttribute(request, "sellerSession");
		logger.info("마이페이지 접근 시도 - 현재 sellerSession 상태: " + sellerSession);
        if (sellerSession != null && sellerSession.getSellerInfo() != null) {
        	SellerForm sellerForm = new SellerForm(indiebeauty.getSellerInfo(sellerSession.getSellerInfo().getSellerid()));
            logger.info("세션 있음 - 사용자 ID: " + sellerSession.getSellerInfo().getSellerid());
            model.addAttribute("sellerForm", sellerForm);
            logger.info("마이페이지로 이동");
            return "showSellerInfo";  // 정보 수정 페이지로 이동
        }else {
	        logger.info("Seller session 없음");
	        return "redirect:/login";  // 세션이 없으면 회원 가입 페이지로 리다이렉트
        }
	}
	@GetMapping("/editSellerInfo")
	public String editSellerInfo() {
		logger.info("Seller 회원정보 수정 페이지 이동");
        return "editSellerInfo";  // 회원가입 페이지로 이동
    }
	@PostMapping("/seller/editSeller")
	public String editSeller(@ModelAttribute("sellerForm") SellerForm sellerForm, BindingResult result, HttpSession session) {
        validator.validate(sellerForm, result);
        if (result.hasErrors()) {
            return "editSellerInfo";
        }
        try {
            indiebeauty.updateSellerInfo(sellerForm.getSellerInfo());
            logger.info("Seller 회원정보 수정 완료");
            return "redirect:/shop?pageNum=1";
        } catch (DataIntegrityViolationException ex) {
        	logger.error("Seller 회원정보 수정 에러");
            result.rejectValue("SellerInfo.sellerid", "ERROR", "An error occurred during the update.");
            return "editSellerInfo";
        }
    }
	
//  회원 탈퇴
//    @GetMapping("/seller/deleteSeller")
//    public String deleteSeller(HttpSession session, RedirectAttributes redirectAttrs) {
//        try {
//        	SellerSession sellerSession = (SellerSession) session.getAttribute("sellerSession");
//            if (sellerSession != null && sellerSession.getSellerInfo() != null) {
//                // 사용자와 관련된 모든 데이터를 먼저 삭제
//                indiebeauty.deleteAllSellerRelatedData(sellerSession.getSellerInfo().getSellerid());
//                // 사용자 정보 삭제
//                indiebeauty.deleteSellerInfo(sellerSession.getSellerInfo().getSellerid());
//                session.invalidate();
//                return "redirect:/sellerLogin";
//            } else {
//                return "redirect:/sellerLogin";
//            }
//        } catch (DataIntegrityViolationException ex) {
//            redirectAttrs.addFlashAttribute("error", "Failed to delete account. Please contact support.");
//            return "redirect:/showSellerInfo"; // 회원 정보 페이지로 리다이렉트
//        }
//    }
	
}

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
        return "sellerSignin";  // 회원가입 페이지로 이동
    }
	@RequestMapping(value = "/seller/registerSeller", method = RequestMethod.POST)
	public String registerSeller(@ModelAttribute("sellerForm") SellerForm sellerForm, BindingResult result, HttpSession session) {
		validator.validate(sellerForm, result);
		if (result.hasErrors()) {
			return "sellerSignin";
		}
		try {
			indiebeauty.insertSellerInfo(sellerForm.getSellerInfo());
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
        if (sellerSession != null && sellerSession.getSellerInfo() != null) {
        	SellerForm sellerForm = new SellerForm(indiebeauty.getSellerInfo(sellerSession.getSellerInfo().getSellerid()));
            model.addAttribute("sellerForm", sellerForm);
            return "showSellerInfo";  // 정보 수정 페이지로 이동
        }else {
	        return "redirect:/login";  // 세션이 없으면 회원 가입 페이지로 리다이렉트
        }
	}
	@GetMapping("/editSellerInfo")
	public String editSellerInfo() {
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
            return "redirect:/shop?pageNum=1";
        } catch (DataIntegrityViolationException ex) {
            result.rejectValue("SellerInfo.sellerid", "ERROR", "An error occurred during the update.");
            return "editSellerInfo";
        }
    }
	
}

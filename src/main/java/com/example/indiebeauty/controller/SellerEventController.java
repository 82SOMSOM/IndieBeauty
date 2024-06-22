package com.example.indiebeauty.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.indiebeauty.domain.SellerEvents;
import com.example.indiebeauty.domain.SellerInfo;
import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.exception.FileUploadException;
import com.example.indiebeauty.service.IndiebeautyFacade;
import com.example.indiebeauty.service.SellerEventService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@SessionAttributes({ "eventForm" })
public class SellerEventController {
	@Autowired
	private SellerEventService eventService;

	@Autowired
	private IndiebeautyFacade indiebeauty;

	@ModelAttribute("eventForm")
	public EventForm createEventForm() {
		return new EventForm();
	}

	@GetMapping("/uploadEvent")
	public String initNewEvent(HttpServletRequest request, @ModelAttribute("eventForm") EventForm eventForm,
			HttpSession session, RedirectAttributes ra) throws ModelAndViewDefiningException {
		SellerSession sellerSession = (SellerSession) session.getAttribute("sellerSession");

		if (sellerSession == null) {
			ra.addAttribute("msg", "로그인 후 참여 가능합니다.");
			ra.addAttribute("url", "/login");

			return "redirect:/upload-product/error";
		}

		SellerInfo sellerInfo = indiebeauty.getSellerInfo(sellerSession.getSellerInfo().getSellerid());
		session.setAttribute("sellerSession", sellerSession);
		eventForm.getEvent().initEvent(sellerInfo);
		return "uploadEvent";
	}

	@PostMapping("/uploadEvent")
	protected ModelAndView confirmEvent(@ModelAttribute("eventForm") EventForm eventForm, SessionStatus status)
			throws FileUploadException {
		boolean rslt = eventService.insertEvent(eventForm);

		System.out.println("이벤트 생성 결과 :" + rslt);
		
		ModelAndView mav = new ModelAndView("redirect:/viewAllEvents");
		mav.addObject("event", eventForm);
		status.setComplete();
		return mav;
	}

	@RequestMapping("/viewAllEvents")
	public ModelAndView getAllEvents(HttpServletRequest request,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum) throws Exception {

		Map<String, Object> resultMap = eventService.getEventsByEventId(pageNum);
		@SuppressWarnings("unchecked")
		List<SellerEvents> eventList = (List<SellerEvents>) resultMap.get("events");
		int totalPages = (int) resultMap.get("totalPages");

		ModelAndView mav = new ModelAndView("viewAllEvents");
		mav.addObject("eventList", eventList);
		mav.addObject("totalPages", totalPages);
		mav.addObject("currentPage", pageNum);

		return mav;
	}

	@Transactional
	@RequestMapping("/joinEvent/{eventId}")
	public String joinEvent(@PathVariable("eventId") int eventId, HttpServletRequest request, RedirectAttributes ra) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		SellerSession sellerSession = (SellerSession) request.getSession().getAttribute("sellerSession");
		if (sellerSession != null) {
			ra.addAttribute("msg", "판매자는 참여할 수 없습니다. 일반회원으로 로그인 부탁드립니다.");
			ra.addAttribute("url", "/login");

			return "redirect:/upload-product/error";
		} else if (userSession == null) {
			ra.addAttribute("msg", "로그인 후 참여 가능합니다.");
			ra.addAttribute("url", "/login");

			return "redirect:/upload-product/error";
		}

		UserInfo userInfo = indiebeauty.getUserInfo(userSession.getUserInfo().getUserid());
		SellerEvents event = eventService.getSellerEventsById(eventId);

		// 이미 참가한 이벤트이거나 인원이 다 찬 경우 join 불가
		if (event.getParticipants().size() >= event.getJoinCount()) {
			ra.addAttribute("msg", "참여 인원이 모두 찼습니다.");
			ra.addAttribute("url", "/viewAllEvents");

			return "redirect:/upload-product/error";
		} else if (event.getParticipants().contains(userInfo)) {
			ra.addAttribute("msg", "이미 참여한 이벤트입니다.");
			ra.addAttribute("url", "/viewAllEvents");

			return "redirect:/upload-product/error";
		}
		// 이벤트에 사용자 추가
		event.getParticipants().add(userInfo);

		return "redirect:/viewAllEvents";
	}
}

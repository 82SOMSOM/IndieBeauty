package com.example.indiebeauty.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.indiebeauty.controller.EventForm;
import com.example.indiebeauty.domain.Orders;
import com.example.indiebeauty.domain.SellerEvents;
import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.exception.FileUploadException;
import com.example.indiebeauty.repository.SellerEventsRepository;

@Service
public class SellerEventService {
	@Autowired
	private SellerEventsRepository sellerEventRepo;

	@Autowired
	private ProductService productSerivce;
	
private static final int PAGE_SIZE = 9; 
	
	private Pageable getPageableForEvents(int pageNum) {
		Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "eventId"));
		return (Pageable) PageRequest.of(pageNum, PAGE_SIZE, sort);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> getEventsByEventId(int pageNum) {
		Pageable pageable = getPageableForEvents(pageNum - 1); // pageNum은 1부터 시작하도록 변경
		Page<SellerEvents> result = sellerEventRepo.findAll(pageable);

		int totalPages = result.getTotalPages();
		List<SellerEvents> events = result.getContent();

		events.forEach(event -> events.size());

		// Map to store the results
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("events", events);
		resultMap.put("totalPages", totalPages);

		return resultMap;
	}


	public SellerEvents getSellerEventsById(int eventId) {
		return sellerEventRepo.getReferenceById(eventId);
	}

	public List<SellerEvents> getAllSellerEvents() {
		return sellerEventRepo.findByOrderByDateDesc();
	}


	@Transactional
	public boolean insertEvent(EventForm eventForm) throws FileUploadException {
		String sellerId = eventForm.getEvent().getSellerId();
		String title = eventForm.getEvent().getTitle();
		String content = eventForm.getEvent().getContent();
		java.util.Date date = eventForm.getEvent().getDate();
		int joinCount = eventForm.getEvent().getJoinCount();
		Set<UserInfo> participants = eventForm.getEvent().getParticipants();

		String imageFileName = productSerivce.saveImage(eventForm.getEventImage());

		SellerEvents sellerEvent = new SellerEvents(0, sellerId, title, content, imageFileName, date, joinCount,
				participants);

		SellerEvents newEvent = sellerEventRepo.save(sellerEvent);
		
		if(newEvent != null) {
			return true;
		}else {
			return false;
		}
	}

}

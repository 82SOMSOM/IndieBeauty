package com.example.indiebeauty.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.indiebeauty.controller.EventForm;
import com.example.indiebeauty.domain.SellerEvents;
import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.exception.FileUploadException;
import com.example.indiebeauty.repository.SellerEventsRepository;

import jakarta.transaction.Transactional;

@Service
public class SellerEventService {
	@Autowired
	private SellerEventsRepository sellerEventRepo;

	@Autowired
	private ProductService productSerivce;

	public SellerEvents getSellerEventsById(int eventId) {
		return sellerEventRepo.getReferenceById(eventId);
	}

	public List<SellerEvents> getAllSellerEvents() {
		return sellerEventRepo.findByOrderByDateDesc();
	}


	@Transactional
	public void insertEvent(EventForm eventForm) throws FileUploadException {
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
	}

}

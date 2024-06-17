package com.example.indiebeauty.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "sellerevents")
public class SellerEvents {
	@Id
	@SequenceGenerator(name = "event_seq_gen", sequenceName = "event_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq_gen")
	@Column(name = "eventid")
	@ManyToMany
	@JoinTable(name="JOINEVENT", joinColumns=@JoinColumn(name="eventId", referencedColumnName="eventid"),
	inverseJoinColumns=@JoinColumn(name="userId", referencedColumnName="userid"))
	private int eventId;

	@Column(name = "sellerid")
	private int sellerId;

	private String title;

	private String content;

	@Column(name = "eventimage")
	private String eventImage;

	@Temporal(TemporalType.DATE)
	@Column(name = "eventdate")
	private Date date;

	@Column(name = "joincount")
	private int joinCount;
}
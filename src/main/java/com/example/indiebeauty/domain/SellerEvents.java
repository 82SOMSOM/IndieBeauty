package com.example.indiebeauty.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sellerevents")
public class SellerEvents {
	@Id
	@SequenceGenerator(name = "event_seq_gen", sequenceName = "event_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq_gen")
	@Column(name = "eventid")
	private int eventId;

	@Column(name = "sellerid")
	private String sellerId;

	private String title;

	private String content;

	@Column(name = "eventimage")
	private String eventImage;

	@Temporal(TemporalType.DATE)
	@Column(name = "eventdate")
	private Date date;

	@Column(name = "joincount")
	private int joinCount;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "JOINEVENT", joinColumns = @JoinColumn(name = "eventid", referencedColumnName = "eventid"), inverseJoinColumns = @JoinColumn(name = "userid", referencedColumnName = "userid"))
	private Set<UserInfo> participants = new HashSet<>();

	public void initEvent(SellerInfo sellerInfo) {
		sellerId = sellerInfo.getSellerid();
		date = new Date();
	}
}
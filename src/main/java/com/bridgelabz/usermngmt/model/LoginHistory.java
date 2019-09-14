package com.bridgelabz.usermngmt.model;

import java.io.Serializable;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Component
@Data
@Entity
@Table(name = "LOGINHISTORY")
public class LoginHistory implements Serializable {

	private static final long serialVersionUID = 2354546334342731L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long loginHistoryId;

	private LocalDateTime localDateTime;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id")
	private User user;
}

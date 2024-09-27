package com.fenoreste.atms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class User implements Serializable{
  
	@Id
	private Integer id;	
	private String username;
	private String password;
	@Temporal(TemporalType.DATE)
	private Date create_at;
	private String email;
	

	private static final long serialVersionUID = 1L;
	
}

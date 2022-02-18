package com.learning.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.learning.dto.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@Table(name="user")

@NoArgsConstructor
public class User {
	
	public User(String username, String email, String name, String password,String address) {
		// TODO Auto-generated constructor stub
		this.username = username;
		this.email = email;
		this.name = name;
		this.password = password;
		this.address = address;
	}

	@Id
	@Column(name="userId",nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String username;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
	private String 	name;
	
	@NotNull
	@Length(min=6)
	private String password;
	
	@NotNull
	private String address;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "userId"),
	inverseJoinColumns = @JoinColumn(name  = "roleId"))
	private Set<Role> roles = new HashSet<Role>();

}

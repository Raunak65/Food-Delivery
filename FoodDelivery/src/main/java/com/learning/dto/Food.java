package com.learning.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@Table(name="food")
@AllArgsConstructor
@NoArgsConstructor
public class Food {
	
	@Id
	@Column(name="foodId",nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotNull
	
	private int foodCost;
	
	@NotNull
	private String foodName;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private FOODTYPE foodType;
	
	@NotNull
	@Length(min = 10)
	private  String description;
	
	@NotNull
	private String foodPic;
}

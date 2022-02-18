package com.learning.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.learning.dto.Food;
import com.learning.dto.FOODTYPE;
import com.learning.exception.AlreadyExistsException;
import com.learning.exception.IdNotFoundException;
@Repository
public interface FoodService  {

	public Food addFood(Food food) throws AlreadyExistsException;
	public Food getFoodById(int id) throws IdNotFoundException;
	public Food updateFoodById(int id,Food food) throws IdNotFoundException;
	public Optional<List<Food>> getFoodByFoodType(FOODTYPE foodType);
	public boolean deleteFoodById(int id) throws IdNotFoundException;
	public Optional<List<Food>> getAllFoods();
}

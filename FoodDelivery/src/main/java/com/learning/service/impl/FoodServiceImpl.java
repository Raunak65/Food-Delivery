package com.learning.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dto.Food;
import com.learning.dto.FOODTYPE;
import com.learning.exception.AlreadyExistsException;
import com.learning.exception.IdNotFoundException;
import com.learning.repository.FoodRepository;
import com.learning.service.FoodService;
@Service
public class FoodServiceImpl implements FoodService {
	@Autowired
	FoodRepository foodRepository;
	
	
	@Override
	@Transactional(rollbackFor = AlreadyExistsException.class)
	public Food addFood(Food food) throws AlreadyExistsException {
		// TODO Auto-generated method stub
		if(foodRepository.existsById(food.getId())) {
			throw new AlreadyExistsException("Food already exist.");
		}
		Food food2 = foodRepository.save(food);
		return food2;
	}

	@Override
	public Food getFoodById(int id) throws IdNotFoundException {
		// TODO Auto-generated method stub
		Optional<Food> optional =  foodRepository.findById(id);
		if(optional.isEmpty()) {
			throw new IdNotFoundException("Id not found Exception.");
		}
		return optional.get();
	}

	@Override
	public Food updateFoodById(int id,Food food) throws IdNotFoundException {
		// TODO Auto-generated method stub
		Optional<Food> optional = foodRepository.findById(id);
		if(optional.isEmpty()) {
			throw new IdNotFoundException("Id not found Exception.");
		}else {
			food.setId(id);
			Food food2 = foodRepository.save(food);
			return food2;
		}
	}

	@Override
	public Optional<List<Food>> getFoodByFoodType(FOODTYPE foodType) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(foodRepository.findByFoodType(foodType));
	}

	@Override
	public boolean deleteFoodById(int id) throws IdNotFoundException {
		// TODO Auto-generated method stub
		try {
			Food food = this.getFoodById(id);
			if(food == null) {
				throw new IdNotFoundException("record not found");
			}
			else {
				foodRepository.deleteById(id);
				return true;
			}
		} catch ( IdNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IdNotFoundException(e.getMessage());
		}
	}

	@Override
	public Optional<List<Food>> getAllFoods() {
		// TODO Auto-generated method stub
		return Optional.ofNullable(foodRepository.findAll());
	}

}

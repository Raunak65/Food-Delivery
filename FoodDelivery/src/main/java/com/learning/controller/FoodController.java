package com.learning.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.dto.Food;
import com.learning.dto.FOODTYPE;
import com.learning.exception.AlreadyExistsException;
import com.learning.exception.FoodTypeNotFound;
import com.learning.exception.IdNotFoundException;
import com.learning.service.FoodService;
@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class FoodController {
	@Autowired
	FoodService foodService;
	
	@PostMapping("/food")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addFood(@RequestBody Food food) {
		
		Food food2;
		try {
			food2 = foodService.addFood(food);
			return ResponseEntity.status(201).body(food2);
		} catch (AlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body("food already exist.");
		}
		
	}
	
	@GetMapping("/food")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<?> getAllFood(){
		Optional<List<Food>> optional = foodService.getAllFoods() ;
		if(optional.isEmpty()) {
			Map<String, String> map = new HashMap<>();
			map.put("message","no record found.");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(map);
		}
		return ResponseEntity.ok(optional.get());
	}
	
	@GetMapping("/food/{id}")
//	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<?> getFoodById(@PathVariable("id") int id) throws IdNotFoundException{
		Food food = foodService.getFoodById(id);
		if(food == null) {
			throw new IdNotFoundException("Id not found exception.");
		}
		else {
			return ResponseEntity.status(201).body(food);
		}
	}
	@GetMapping("/food/type/{foodType}")
	public ResponseEntity<?> getFoodByFoodType(@PathVariable("foodType") String foodType) throws FoodTypeNotFound{
		FOODTYPE food = FOODTYPE.valueOf(foodType);
		Optional<List<Food>> optional = foodService.getFoodByFoodType(food) ;
		if(optional.isEmpty()) {
			Map<String, String> map = new HashMap<>();
			map.put("message","no record found.");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(map);
		}
		return ResponseEntity.ok(optional.get());
	}
	
	@PutMapping("/food/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateFoodById(@PathVariable("id") int id,@RequestBody Food food) throws IdNotFoundException{
		Food food2;
		try {
			food2 = foodService.updateFoodById(id,food);
			return ResponseEntity.status(201).body(food2);
		} catch (IdNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Id not Found.");
		}
	}
	@DeleteMapping("/food/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteFoodById(@PathVariable("id") int id) throws IdNotFoundException{
		 boolean isRemoved = foodService.deleteFoodById(id);

	        if (isRemoved) {
	            return ResponseEntity.status(200).body("Food deleted Successfully");
	        }

	        return ResponseEntity.badRequest().body("Food with Id: "+id+" Not Found.");
	 }


}

package com.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.learning.dto.Food;
import com.learning.dto.FOODTYPE;
import com.learning.exception.AlreadyExistsException;
import com.learning.service.FoodService;
import com.learning.service.UserService;

@SpringBootApplication
public class FoodDeliveryApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = 
				SpringApplication.run(FoodDeliveryApplication.class, args);
		
		
		
	}

}

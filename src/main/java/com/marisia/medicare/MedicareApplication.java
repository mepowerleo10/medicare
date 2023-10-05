package com.marisia.medicare;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.marisia.medicare.service.AppUserService;
import com.marisia.medicare.service.payment.PaymentProperties;
import com.marisia.medicare.service.storage.StorageService;

@SpringBootApplication
public class MedicareApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicareApplication.class, args);
	}

	@Bean
	CommandLineRunner setUpInitialData(
			AppUserService userService,
			PaymentProperties paymentProperties,
			PasswordEncoder encoder) {

		return args -> {
			userService.initializeSystemAndAdminUsers(paymentProperties, encoder);
		};
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return args -> {
			storageService.init();
		};
	}

}

package com.marisia.medicare;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.marisia.medicare.model.AppUser;
import com.marisia.medicare.repository.AppUserRepository;
import com.marisia.medicare.repository.DrugRepository;
import com.marisia.medicare.service.payment.PaymentProperties;
import com.marisia.medicare.service.storage.StorageService;

@SpringBootApplication
public class MedicareApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicareApplication.class, args);
	}

	@Bean
	CommandLineRunner prePopulateData(DrugRepository drugRepository, AppUserRepository usersRepository,
			PasswordEncoder encoder, PaymentProperties paymentProperties) {
		return args -> {
			usersRepository
					.save(new AppUser(1, "system", encoder.encode("none"), paymentProperties.getAccountNumber(), "SYSTEM"));
			usersRepository.save(new AppUser(2, "admin", encoder.encode("123"), null, "USER,ADMIN"));
		};
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return args -> {
			storageService.init();
		};
	}

}

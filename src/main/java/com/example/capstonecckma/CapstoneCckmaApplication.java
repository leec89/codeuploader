package com.example.capstonecckma;

import com.example.capstonecckma.repositories.DocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CapstoneCckmaApplication implements CommandLineRunner {
		@Autowired
	DocRepository ob;
	public static void main(String[] args) {
		SpringApplication.run(CapstoneCckmaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}

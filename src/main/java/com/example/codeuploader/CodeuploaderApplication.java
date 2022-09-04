package com.example.codeuploader;

import com.example.codeuploader.repositories.DocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodeuploaderApplication implements CommandLineRunner {
		@Autowired
	DocRepository ob;
	public static void main(String[] args) {
		SpringApplication.run(CodeuploaderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}

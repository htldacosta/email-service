package com.email.email_service;

import com.email.email_service.controller.EmailController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceApplicationTests {

	@Autowired
	private EmailController emailController;


	@Test
	void contextLoads() {
	}

}

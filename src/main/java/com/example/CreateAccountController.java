package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/create-account")
public class CreateAccountController {
	@RequestMapping
	public String createAccount() {
		return "create-account"; // This should return the name of the view for creating an account
	}

}

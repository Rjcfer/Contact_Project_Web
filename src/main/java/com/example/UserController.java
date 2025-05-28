package com.example;

import com.example.accessingdatajpa.UserAccount;
import com.example.accessingdatajpa.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private PasswordEncoder passwordEncoder;
	private static final Logger log = LoggerFactory.getLogger(ServingWebContentApplication.class);

	@Autowired
	private UserAccountRepository userRepository;


	@PostMapping("/new")
	public String createUser(@RequestParam String username,
							 @RequestParam String password,
							 Model model) {
		String hashedPassword = passwordEncoder.encode(password);
		UserAccount user = new UserAccount(username, hashedPassword);

		log.info("Création de l'utilisateur : {}", user.getUsername());
		userRepository.save(user);

		model.addAttribute("message", "Compte créé avec succès !");
		return "login"; // Affiche la vue de connexion
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("user", new UserAccount());
		return "login"; // Retourne la page login.html
	}

	@PostMapping("/login")
	public String loginUser(@ModelAttribute UserAccount user,
							Model model) {
	log.warn("loginUser", user.getUsername());
		List<UserAccount> users = userRepository.findByUsername(user.getUsername());
		log.warn("User.size()", users.size());
		log.warn("test",users.get(0).getPassword());

		if(users.size() != 1) {
			model.addAttribute("error", "Nom d'utilisateur invalide.");
			return "login";
		}

		log.warn("Connexion de l'utilisateur : {}", user);
		log.warn("test",users.get(0).getPassword());
		if (!passwordEncoder.matches(user.getPassword(), users.get(0).getPassword())) {
			model.addAttribute("error", "Mot de passe incorrect.");
			return "login";
		}

		// Connexion réussie
		model.addAttribute("message", "Bienvenue " + users.get(0).getUsername() + " !");
		return "/contacts";
	}

}

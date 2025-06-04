package com.example.securingweb;

import com.example.accessingdatajpa.UserAccount;
import com.example.accessingdatajpa.UserAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((requests) -> requests
						// Public
						.requestMatchers("/", "/user/login", "/user/new", "/create-account", "/css/**", "/js/**").permitAll()
						.requestMatchers("/xml**").permitAll()
						// Private
						.anyRequest().authenticated()
				)
				.formLogin((form) -> form
						.loginPage("/user/login")
						.loginProcessingUrl("/user/login")
						.defaultSuccessUrl("/contacts", true)
						.failureUrl("/user/login?error")
						.permitAll()
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/user/login?logout")
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.permitAll()
				)
				// Désactiver CSRF pour l'API XML
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/xml**")
				);

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(UserAccountRepository userRepo) {
		return username -> {
			UserAccount user = userRepo.findByUsername(username);
			if (user == null) {
				throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
			}
			return User.withUsername(user.getUsername())
					.password(user.getPassword()) // déjà hashé
					.roles("USER")
					.build();
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
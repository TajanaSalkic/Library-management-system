package org.web.projekat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.web.projekat.models.Role;
import org.web.projekat.models.User;
import org.web.projekat.repositories.UserRepository;

@Controller
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            // Provjera da li korisnik već postoji
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Korisničko ime već postoji");
                return "redirect:/register";
            }
            
            // Postavi role i enkoduj password
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // Sačuvaj korisnika
            userRepository.save(user);
            
            redirectAttributes.addFlashAttribute("success", "Registracija uspješna! Možete se prijaviti.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Došlo je do greške prilikom registracije");
            return "redirect:/register";
        }
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
} 
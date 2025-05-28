package com.example;

import com.example.accessingdatajpa.Contact;
import com.example.accessingdatajpa.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactRepository repository;

    @GetMapping
    public String listContacts(Model model) {
        model.addAttribute("contacts", repository.findAll());
        return "contacts";
    }

    @GetMapping("/new")
    public String newContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "form";
    }

    @PostMapping("/save")
    public String saveContact(@ModelAttribute Contact contact) {
        repository.save(contact);
        return "redirect:/contacts";
    }

    @GetMapping("/edit/{id}")
    public String editContact(@PathVariable Long id, Model model) {
        Optional<Contact> contact = repository.findById(id);
        if (contact.isPresent()) {
            model.addAttribute("contact", contact.get());
            return "form";
        } else {
            return "redirect:/contacts";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/contacts";
    }
}

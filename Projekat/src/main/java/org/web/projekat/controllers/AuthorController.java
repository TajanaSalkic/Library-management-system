package org.web.projekat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.web.projekat.models.Author;
import org.web.projekat.models.Category;
import org.web.projekat.services.AuthorService;

import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/")
    public String showAuthors(Model model) {
        List<Author> authors = authorService.findAllAuthors();
        model.addAttribute("authors", authors);
        return "author_list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "add_author";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    @ResponseBody
    public List<Author> searchAuthors(@RequestParam("name") String name) {
        return authorService.findAuthorsByName(name);
    }

    @PostMapping("/add")
    public String addAuthor(@ModelAttribute("author") Author author, Model model) {
        // da li postoji autor s istim imenom
        if (authorService.findAuthorsByNameBool(author.getName())) {
            model.addAttribute("error", "Author with this name already exists");
            return "add_author"; // Redisplay the form with the error message
        }

        // sacuvaj novog ako nema nijednog
        authorService.saveAuthor(author);
        return "redirect:/authors/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editAuthor(@PathVariable("id") Long id, Model model) {
        Author author = authorService.findAuthorById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid author Id:" + id));
        model.addAttribute("author", author);
        return "edit_author";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateAuthor(@PathVariable("id") Long id, Author author) {
        author.setId(id);
        authorService.saveAuthor(author);
        return "redirect:/authors/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthorById(id);
        return "redirect:/authors/";
    }

}
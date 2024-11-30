package org.web.projekat.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.web.projekat.models.Author;
import org.web.projekat.models.Book;
import org.web.projekat.models.Category;
import org.web.projekat.services.AuthorService;
import org.web.projekat.services.BookService;

import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String showAuthors(Model model) {
        List<Author> authors = authorService.findAllAuthors();
        model.addAttribute("authors", authors);
        return "Authors/author_list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "Authors/add_author";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    @ResponseBody
    public List<Author> searchAuthors(@RequestParam("name") String name) {
        return authorService.findAuthorsByName(name);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addAuthor(
            @Valid @ModelAttribute("author") Author author,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "Authors/add_author";
        }

        // provjeriti da li postoji autor sa istim imenom
        if (authorService.findAuthorsByNameBool(author.getName())) {
            model.addAttribute("error", "Author with this name already exists");
            return "Authors/add_author";
        }

        authorService.saveAuthor(author);
        return "redirect:/authors/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editAuthor(@PathVariable("id") Long id, Model model) {
        Author author = authorService.findAuthorById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid author Id:" + id));
        model.addAttribute("author", author);
        return "Authors/edit_author";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateAuthor(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("author") Author author,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("author", author);
            return "Authors/edit_author";
        }

        author.setId(id);
        authorService.saveAuthor(author);
        return "redirect:/authors/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable("id") Long id) {
        Author author = authorService.findAuthorById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));

        for (Book book : author.getBooks()) {
            book.getAuthors().remove(author);
            bookService.saveBook(book);
        }

        authorService.deleteAuthorById(id);
        return "redirect:/authors/";
    }

}

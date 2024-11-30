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

import org.web.projekat.repositories.CategoryRepository;
import org.web.projekat.services.AuthorService;
import org.web.projekat.services.BookService;
import org.web.projekat.services.CategoryService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private AuthorService authorService;


    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;

    public BookController(AuthorService authorService) {
        this.authorService = authorService;
    }


    @GetMapping("/")
    public String showBooks(Model model) {
        List<Book> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "Books/book_list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        List<Author> authors = authorService.findAllAuthors();

        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categoryService.findAllCategories());
        return "Books/edit_book";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateBook(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("book") Book book,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAllAuthors());
            model.addAttribute("categories",  categoryService.findAllCategories());
            return "Books/edit_book";
        }


        bookService.saveBook(book);
        return "redirect:/books/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return "redirect:/books/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        List<Category> categories =  categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("authors", authorService.findAllAuthors());
        return "Books/add_book";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addBook(
            @Valid @ModelAttribute("book") Book book,
            BindingResult bindingResult,
            Model model) {

        // validacija da autori nisu prazni
        if (book.getAuthors() == null || book.getAuthors().isEmpty()) {
            bindingResult.rejectValue("authors", "error.book", "");
        }

        // validacija da kategorije nisu prazne
        if (book.getCategories() == null || book.getCategories().isEmpty()) {
            bindingResult.rejectValue("categories", "error.book", "");
        }

        // Provjera da li knjiga sa istim nazivom i autorom postoji
        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            for (Author author : book.getAuthors()) {
                if (bookService.existsByTitleAndAuthor(book.getTitle(), author)) {
                    bindingResult.rejectValue("title", "error.book", "Knjiga sa istim nazivom i autorom već postoji.");
                    break;
                }
            }
        }

        // errori
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAllAuthors());
            model.addAttribute("categories",  categoryService.findAllCategories());
            return "Books/add_book";
        }

        bookService.saveBook(book);
        return "redirect:/books/";
    }






}
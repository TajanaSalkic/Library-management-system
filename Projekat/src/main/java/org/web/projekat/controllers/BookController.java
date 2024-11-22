package org.web.projekat.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.web.projekat.models.Author;
import org.web.projekat.models.Book;
import org.web.projekat.models.Category;

import org.web.projekat.repositories.CategoryRepository;
import org.web.projekat.services.AuthorService;
import org.web.projekat.services.BookService;

import java.util.HashSet;
import java.util.List;


@Controller
@RequestMapping("/books")
public class BookController {

   /* @Autowired
    private BookRepository bookRepository;
*/
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthorService authorService;


    @Autowired
    private BookService bookService;

    public BookController(AuthorService authorService) {
        this.authorService = authorService;
    }


    @GetMapping("/")
    public String showBooks(Model model) {
        List<Book> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "book_list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("book", book);
        return "edit_book";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") Long id, Book book) {
        book.setId(id);
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
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("authors", authorService.findAllAuthors());
        return "add_book";
    }

    /*@PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book) {
        bookService.saveBook(book);  // Save the new book
        return "redirect:/books/";  // Redirect to the list of books
    }*/

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book,
                          @RequestParam("authorOption") String authorOption,
                          @RequestParam(value = "existingAuthorId", required = false) Long existingAuthorId,
                          @RequestParam(value = "newAuthorName", required = false) String newAuthorName) {

        if (book.getAuthors() == null) {
            book.setAuthors(new HashSet<>());
        }


        if ("existing".equals(authorOption) && existingAuthorId != null) {
            Author existingAuthor = authorService.findAuthorById(existingAuthorId)
                    .orElseThrow(() -> new RuntimeException("Author not found"));
            book.getAuthors().add(existingAuthor);  // Add the existing author to the book
        }

        if ("new".equals(authorOption) && newAuthorName != null && !newAuthorName.trim().isEmpty()) {
            Author newAuthor = new Author();
            newAuthor.setName(newAuthorName);
            authorService.saveAuthor(newAuthor);
            book.getAuthors().add(newAuthor);
        }

        bookService.saveBook(book);

        return "redirect:/books/";
    }


}

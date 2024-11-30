package org.web.projekat.services;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.projekat.models.Author;
import org.web.projekat.models.Book;
import org.web.projekat.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id.intValue());
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id.intValue());
    }

    public boolean existsByTitleAndAuthor(@NotNull @NotEmpty(message="Unesite Naslov") @Size(min = 1, max = 200, message = "Naslov mora biti između 1 i 200 karaktera") String title, Author author) {
        return bookRepository.existsByTitleAndAuthorsContaining(title, author);
    }
}

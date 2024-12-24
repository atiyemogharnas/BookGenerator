package com.library.bookgenerator.controller;

import com.library.bookgenerator.entity.Book;
import com.library.bookgenerator.entity.DTO.BookRequestDTo;
import com.library.bookgenerator.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/create-file")
    public ResponseEntity<String> createBookFile() {
        try {
            bookService.generateBooks("D:\\Educational Mahsan\\bookGenerator\\src\\main\\resources");
            return ResponseEntity.ok("Book files with content generated");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBook(@RequestBody BookRequestDTo bookRequestDTo) {
        try {
            bookService.createBook(bookRequestDTo);
            return ResponseEntity.ok("Book is created");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadBook(@RequestParam String filePath) {
        try {
            bookService.parseAndSaveBook(filePath);
            return ResponseEntity.ok("Book is parsing and save");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/find-by-content-special-word")
    public ResponseEntity<String> findBookByContentSpecialWord(@RequestParam String contentSpecialWord) {
        try {
            List<Book> books = bookService.findBooksByContentSpecialWord(contentSpecialWord);
            String booksAsString = books.stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.ok(booksAsString);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/find-by-content-exact-match")
    public ResponseEntity<String> findBookByContentExactMatch(@RequestParam String contentSpecialWord) {
        try {
            List<Book> books = bookService.findBooksByContentExactMatch(contentSpecialWord);
            String booksAsString = books.stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.ok(booksAsString);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/find-by-content-and-owner")
    public ResponseEntity<String> findBookByContentExactMatch(@RequestParam String owner, @RequestParam String content) {
        try {
            List<Book> books = bookService.findBooksByOwnerAndContent(owner, content);
            String booksAsString = books.stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.ok(booksAsString);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/find-by-wrong-content")
    public ResponseEntity<String> findBookByWrongContent(@RequestParam String content) {
        try {
            List<Book> books = bookService.findBooksByWrongContent(content);
            String booksAsString = books.stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.ok(booksAsString);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/find-by-space-content")
    public ResponseEntity<String> findBookBySpaceInContent() {
        try {
            List<Book> books = bookService.findBooksBySpaceInContent();
            String booksAsString = books.stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.ok(booksAsString);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/find-by-title-and-owner")
    public ResponseEntity<String> findBookByTitleAndOwner(@RequestParam String title, @RequestParam String owner) {
        try {
            bookService.findBookByTitleAndOwner(title, owner);
            return ResponseEntity.ok("Book found");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}

package com.library.bookgenerator.service;

import com.library.bookgenerator.entity.Book;
import com.library.bookgenerator.entity.DTO.BookRequestDTo;
import com.library.bookgenerator.repository.BookRepository;
import com.library.bookgenerator.utils.ConvertDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public void createBook(BookRequestDTo bookRequestDTo) {
        Book book = new Book(bookRequestDTo.getTitle(), bookRequestDTo.getContent(), ConvertDate.convertStringToDate(bookRequestDTo.getCreatedDate()), bookRequestDTo.getOwner());
        bookRepository.save(book);
    }

    public void generateBooks(String path) {
        List<Book> bookList = StreamSupport.stream(bookRepository.findAll().spliterator(), false).toList();
        createDirectory(path);
        for (Book book : bookList) {
            createBookFileWithContent(book, path);
        }

    }

    public void createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdir();
            if (created) {
                System.out.println("Directory created: " + path);
            } else {
                System.err.println("Failed to create directory: " + path);
            }
        }
    }

    public void createBookFileWithContent(Book book, String path) {
        String fileName = path + "\\" + book.getTitle().replaceAll("\\s+", "_") + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Title: " + book.getTitle());
            writer.newLine();
            writer.write("Owner: " + book.getOwner());
            writer.newLine();
            writer.write("Creation Date: " + book.getCreatedDate());
            writer.newLine();
            writer.newLine();

            writer.write("Content:");
            writer.newLine();
            writer.write(book.getContent());
            writer.newLine();

            System.out.println("File created: " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to write file: " + fileName);
            throw new RuntimeException(e);
        }
    }

    public void parseAndSaveBook(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            StringBuilder content = new StringBuilder();
            String title = "";
            String owner = "";
            Date createdDate = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Title")) {
                    title = line.substring(6).trim();
                } else if (line.startsWith("Owner")) {
                    owner = line.substring(7).trim();
                } else if (line.startsWith("Creation Date")) {
                    createdDate = ConvertDate.convertStrToDate(line.substring(14).trim());
                } else {
                    content.append(line).append("\n");
                }
            }

            Book book = new Book(title, content.toString(), createdDate, owner);
            bookRepository.save(book);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> findBooksByContentSpecialWord(String specialWord) {
        return bookRepository.findByContent(specialWord);
    }

    public List<Book> findBooksByContentExactMatch(String specialWord) {
        return bookRepository.findByExactMatch(specialWord);
    }

    public List<Book> findBooksByOwnerAndContent(String owner, String content) {
        return bookRepository.findByOwnerAndContent(owner, content);
    }

    public List<Book> findBooksByWrongContent(String content) {
        return bookRepository.findByWrongContent(content);
    }

    public List<Book> findBooksBySpaceInContent() {
        return bookRepository.findBooksBySpaceInContent();
    }

    public Book findBookByTitleAndOwner(String title, String owner) {
        Optional<Book> book = bookRepository.findBookByTitleAndOwner(title,owner);
        if (book.isPresent()){
            return book.get();
        }else {
            throw new RuntimeException("Book not found");
        }
    }
}

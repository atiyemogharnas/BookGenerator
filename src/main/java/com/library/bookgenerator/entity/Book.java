package com.library.bookgenerator.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName = "books")
@Setter
public class Book {
    @Id
    private String id;
    private String title;
    private String content;
    private Date createdDate;
    private String owner;

    public Book(String title, String content, Date createdDate, String owner) {
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", owner='" + owner + '\'' +
                '}';
    }
}

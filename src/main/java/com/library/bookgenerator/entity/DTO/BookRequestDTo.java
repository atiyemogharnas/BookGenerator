package com.library.bookgenerator.entity.DTO;

import lombok.Setter;

@Setter
public class BookRequestDTo {

    private String title;
    private String content;
    private String createdDate;
    private String owner;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getOwner() {
        return owner;
    }
}

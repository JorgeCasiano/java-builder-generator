package com.casiano.builder.example;

import com.casiano.builder.Builder;
import com.casiano.builder.BuilderMode;

@Builder(mode = BuilderMode.SETTER, copyBuilder = true)
public class BookModeSetter {

    private String isbn;
    private String title;
    private int pages;

    public BookModeSetter() {

    }

    public BookModeSetter(int pages) {
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPages() {
        return pages;
    }

}

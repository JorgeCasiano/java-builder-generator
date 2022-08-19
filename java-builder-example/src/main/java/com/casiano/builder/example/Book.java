package com.casiano.builder.example;

import com.casiano.builder.Builder;
import lombok.Getter;

@Builder
@Getter
public class Book {

    private String isbn;
    private String title;
    private int pages;

}

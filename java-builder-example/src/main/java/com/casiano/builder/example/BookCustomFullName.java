package com.casiano.builder.example;

import com.casiano.builder.Builder;
import lombok.Getter;

@Builder(name = Constants.CUSTOM_FULL_NAME_BUILDER)
@Getter
public class BookCustomFullName {

    private String isbn;
    private String title;
    private int pages;

}
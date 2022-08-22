package com.casiano.builder.example;

import com.casiano.builder.Builder;
import lombok.Getter;

@Builder(name = Constants.CUSTOM_NAME_BUILDER)
@Getter
public class BookCustomName {

    private String isbn;
    private String title;
    private int pages;

}
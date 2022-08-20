package com.casiano.builder.example;

import com.casiano.builder.Builder;
import com.casiano.builder.BuilderMode;
import lombok.Getter;

@Builder(mode = BuilderMode.FIELD)
@Getter
public class BookModeField {

    private String isbn;
    private String title;
    private int pages;

}

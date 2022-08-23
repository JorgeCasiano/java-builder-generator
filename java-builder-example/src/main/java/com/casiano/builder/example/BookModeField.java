package com.casiano.builder.example;

import com.casiano.builder.Builder;
import com.casiano.builder.BuilderMode;
import lombok.Data;

@Builder(mode = BuilderMode.FIELD, copyBuilder = true)
@Data
public class BookModeField {

    private String isbn;
    private String title;
    private int pages;

}

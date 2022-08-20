package com.casiano.builder.example;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class BookModeFieldBuilderTest {

    @Test
    void whenBuildBookWithBuilder_thenObjectHasPropertyValues() {
        BookModeField book = BookModeFieldBuilder.newBuilder()
                .isbn("an_isbn")
                .title("a_title")
                .pages(10)
                .build();

        assertThat(book, notNullValue());
        assertThat(book.getIsbn(), is("an_isbn"));
        assertThat(book.getTitle(), is("a_title"));
        assertThat(book.getPages(), is(10));
    }

}
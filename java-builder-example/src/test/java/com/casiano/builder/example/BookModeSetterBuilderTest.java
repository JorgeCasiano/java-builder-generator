package com.casiano.builder.example;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class BookModeSetterBuilderTest {

    @Test
    void whenBuildBookWithBuilder_modeSetter_thenObjectHasPropertyValues() {
        BookModeSetter book = BookModeSetterBuilder.newBuilder()
                .isbn("an_isbn")
                .title("a_title")
                .build();

        assertThat(book, notNullValue());
        assertThat(book.getIsbn(), is("an_isbn"));
        assertThat(book.getTitle(), is("a_title"));
    }

    @Test
    void whenGenerateBuilderWithModeSetter_generateAttributesFromSetter() {
        int fields = BookModeSetterBuilder.class.getDeclaredFields().length;
        assertThat(fields, is(2));
    }

}
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
    void whenBuildBookWithCopyBuilder_modeSetter_thenObjectHasPropertyValues() {
        BookModeSetter book = new BookModeSetter(10);
        book.setIsbn("an_isbn");
        book.setTitle("a_title");

        BookModeSetter result = BookModeSetterBuilder.copyBuilder(book)
                .isbn("new_isbn")
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getIsbn(), is("new_isbn"));
        assertThat(result.getPages(), is(10));
        assertThat(result.getTitle(), is("a_title"));
    }

    @Test
    void whenGenerateBuilderWithModeSetter_generateAttributesFromSetter() {
        int fields = BookModeSetterBuilder.class.getDeclaredFields().length;
        // 2 setters + 2 (initSetters) + 2 (target instance and generateWithCopy)
        assertThat(fields, is(2 + 2 + 2));
    }

}
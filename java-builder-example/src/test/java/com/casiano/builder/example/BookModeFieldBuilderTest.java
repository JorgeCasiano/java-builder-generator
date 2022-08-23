package com.casiano.builder.example;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class BookModeFieldBuilderTest {

    @Test
    void whenBuildBookWithBuilder_modeField_thenObjectHasPropertyValues() {
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

    @Test
    void whenBuildBookWithCopyBuilder_modeField_thenObjectHasPropertyValues() {
        BookModeField book = new BookModeField();
        book.setIsbn("an_isbn");
        book.setTitle("a_title");
        book.setPages(10);

        BookModeField result = BookModeFieldBuilder.copyBuilder(book)
                .pages(99)
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getPages(), is(99));
        assertThat(result.getIsbn(), is("an_isbn"));
        assertThat(result.getTitle(), is("a_title"));
    }

    @Test
    void whenGenerateBuilderWithModeField_generateAttributesFromFields() {
        int fields = BookModeFieldBuilder.class.getDeclaredFields().length;
        // 3 fields + 3 (initFields) + 2 (target instance and generateWithCopy)
        assertThat(fields, is(3 + 3 + 2));
    }

}
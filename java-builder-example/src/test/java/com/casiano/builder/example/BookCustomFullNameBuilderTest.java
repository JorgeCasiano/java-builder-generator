package com.casiano.builder.example;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class BookCustomFullNameBuilderTest {

    @Test
    void whenBuildBookWithCustomFullName_thenGenerateBuilderWithThisNameAndPackage() {
        BookCustomFullName book = com.example.BookCustomBuilder.newBuilder().build();

        assertThat(com.example.BookCustomBuilder.class.getName(), is(Constants.CUSTOM_FULL_NAME_BUILDER));
        assertThat(book, notNullValue());
    }

}
package com.casiano.builder.example;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class BookCustomNameBuilderTest {

    @Test
    void whenBuildBookWithCustomName_thenGenerateBuilderWithThisNameInSamePackage() {
        BookCustomName book = BookBuilder.newBuilder()
                .build();

        assertThat(BookBuilder.class.getSimpleName(), is(Constants.CUSTOM_NAME_BUILDER));
        assertThat(book, notNullValue());
    }

}
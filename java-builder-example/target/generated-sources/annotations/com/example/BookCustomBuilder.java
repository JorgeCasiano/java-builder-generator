package com.example;

public class BookCustomBuilder{

  private java.lang.String isbn;

  private java.lang.String title;

  private int pages;


  private com.casiano.builder.example.BookCustomFullName bookCustomFullName;

  private boolean generateWithCopy;

  private BookCustomBuilder(com.casiano.builder.example.BookCustomFullName bookCustomFullName, boolean generateWithCopy) {
    this.bookCustomFullName = bookCustomFullName;
    this.generateWithCopy = generateWithCopy;
  }

  public static BookCustomBuilder newBuilder() {
    return new BookCustomBuilder(new com.casiano.builder.example.BookCustomFullName(), false);
  }

  public com.casiano.builder.example.BookCustomFullName build() {
    try {
      java.lang.reflect.Field[] fields = this.bookCustomFullName.getClass().getDeclaredFields();
      setField(this.bookCustomFullName, fields[0], this.isbn);
      setField(this.bookCustomFullName, fields[1], this.title);
      setField(this.bookCustomFullName, fields[2], this.pages);
      return this.bookCustomFullName;
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public BookCustomBuilder isbn(java.lang.String isbn) {
    this.isbn = isbn;
    return this;
  }
  public BookCustomBuilder title(java.lang.String title) {
    this.title = title;
    return this;
  }
  public BookCustomBuilder pages(int pages) {
    this.pages = pages;
    return this;
  }

  private void setField(com.casiano.builder.example.BookCustomFullName bookCustomFullName , java.lang.reflect.Field field, Object value) throws IllegalAccessException {
    field.setAccessible(true);
    field.set(bookCustomFullName, value);
  }
}

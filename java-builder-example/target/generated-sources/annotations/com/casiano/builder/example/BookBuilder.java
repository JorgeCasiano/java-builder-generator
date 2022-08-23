package com.casiano.builder.example;

public class BookBuilder{

  private java.lang.String isbn;

  private java.lang.String title;

  private int pages;


  private com.casiano.builder.example.BookCustomName bookCustomName;

  private boolean generateWithCopy;

  private BookBuilder(com.casiano.builder.example.BookCustomName bookCustomName, boolean generateWithCopy) {
    this.bookCustomName = bookCustomName;
    this.generateWithCopy = generateWithCopy;
  }

  public static BookBuilder newBuilder() {
    return new BookBuilder(new com.casiano.builder.example.BookCustomName(), false);
  }

  public com.casiano.builder.example.BookCustomName build() {
    try {
      java.lang.reflect.Field[] fields = this.bookCustomName.getClass().getDeclaredFields();
      setField(this.bookCustomName, fields[0], this.isbn);
      setField(this.bookCustomName, fields[1], this.title);
      setField(this.bookCustomName, fields[2], this.pages);
      return this.bookCustomName;
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public BookBuilder isbn(java.lang.String isbn) {
    this.isbn = isbn;
    return this;
  }
  public BookBuilder title(java.lang.String title) {
    this.title = title;
    return this;
  }
  public BookBuilder pages(int pages) {
    this.pages = pages;
    return this;
  }

  private void setField(com.casiano.builder.example.BookCustomName bookCustomName , java.lang.reflect.Field field, Object value) throws IllegalAccessException {
    field.setAccessible(true);
    field.set(bookCustomName, value);
  }
}

package com.casiano.builder.example;

public class BookModeFieldBuilder{

  private java.lang.String isbn;

  private java.lang.String title;

  private int pages;


  private com.casiano.builder.example.BookModeField bookModeField;

  private boolean generateWithCopy;

  private boolean initisbn;

  private boolean inittitle;

  private boolean initpages;

  public static BookModeFieldBuilder copyBuilder(com.casiano.builder.example.BookModeField bookModeField) {
    return new BookModeFieldBuilder(bookModeField, true);
  }

  private BookModeFieldBuilder(com.casiano.builder.example.BookModeField bookModeField, boolean generateWithCopy) {
    this.bookModeField = bookModeField;
    this.generateWithCopy = generateWithCopy;
  }

  public static BookModeFieldBuilder newBuilder() {
    return new BookModeFieldBuilder(new com.casiano.builder.example.BookModeField(), false);
  }

  public com.casiano.builder.example.BookModeField build() {
    try {
      java.lang.reflect.Field[] fields = this.bookModeField.getClass().getDeclaredFields();
      if (!this.generateWithCopy || this.initisbn) {
        setField(this.bookModeField, fields[0], this.isbn);
      }
      if (!this.generateWithCopy || this.inittitle) {
        setField(this.bookModeField, fields[1], this.title);
      }
      if (!this.generateWithCopy || this.initpages) {
        setField(this.bookModeField, fields[2], this.pages);
      }
      return this.bookModeField;
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public BookModeFieldBuilder isbn(java.lang.String isbn) {
    this.initisbn = this.generateWithCopy;
    this.isbn = isbn;
    return this;
  }
  public BookModeFieldBuilder title(java.lang.String title) {
    this.inittitle = this.generateWithCopy;
    this.title = title;
    return this;
  }
  public BookModeFieldBuilder pages(int pages) {
    this.initpages = this.generateWithCopy;
    this.pages = pages;
    return this;
  }

  private void setField(com.casiano.builder.example.BookModeField bookModeField , java.lang.reflect.Field field, Object value) throws IllegalAccessException {
    field.setAccessible(true);
    field.set(bookModeField, value);
  }
}

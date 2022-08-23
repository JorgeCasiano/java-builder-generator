package com.casiano.builder.example;

public class BookModeSetterBuilder{

  private java.lang.String isbn;

  private java.lang.String title;


  private com.casiano.builder.example.BookModeSetter bookModeSetter;

  private boolean generateWithCopy;

  private boolean initisbn;

  private boolean inittitle;

  public static BookModeSetterBuilder copyBuilder(com.casiano.builder.example.BookModeSetter bookModeSetter) {
    return new BookModeSetterBuilder(bookModeSetter, true);
  }

  private BookModeSetterBuilder(com.casiano.builder.example.BookModeSetter bookModeSetter, boolean generateWithCopy) {
    this.bookModeSetter = bookModeSetter;
    this.generateWithCopy = generateWithCopy;
  }

  public static BookModeSetterBuilder newBuilder() {
    return new BookModeSetterBuilder(new com.casiano.builder.example.BookModeSetter(), false);
  }

  public com.casiano.builder.example.BookModeSetter build() {
    if (!this.generateWithCopy || this.initisbn) {
      this.bookModeSetter.setIsbn(this.isbn);
    }
    if (!this.generateWithCopy || this.inittitle) {
      this.bookModeSetter.setTitle(this.title);
    }
    return this.bookModeSetter;
  }

  public BookModeSetterBuilder isbn(java.lang.String isbn) {
    this.initisbn = this.generateWithCopy;
    this.isbn = isbn;
    return this;
  }
  public BookModeSetterBuilder title(java.lang.String title) {
    this.inittitle = this.generateWithCopy;
    this.title = title;
    return this;
  }

}

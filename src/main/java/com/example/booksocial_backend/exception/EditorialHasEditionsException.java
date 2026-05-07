package com.example.booksocial_backend.exception;

public class EditorialHasEditionsException extends RuntimeException {
  public EditorialHasEditionsException(String name, int count) {
    super("No se puede eliminar la editorial '" + name + "' porque tiene " + count + " edición(es) asociada(s)");
  }
}

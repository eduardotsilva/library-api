package com.eduardo.libraryapi.service;

import com.eduardo.libraryapi.api.model.entity.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book any);

    Optional<Book> getById(Long id);
}


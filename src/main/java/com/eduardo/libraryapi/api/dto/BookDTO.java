package com.eduardo.libraryapi.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;

}

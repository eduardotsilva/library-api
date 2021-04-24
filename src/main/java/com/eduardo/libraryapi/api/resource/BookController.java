package com.eduardo.libraryapi.api.resource;

import com.eduardo.libraryapi.api.dto.BookDTO;
import com.eduardo.libraryapi.api.model.entity.Book;
import com.eduardo.libraryapi.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    public BookController(BookService service) {
        this.service = service;
    }

    private BookService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create (@RequestBody BookDTO dto){
        Book entity =
                Book.builder()
                    .author(dto.getAuthor())
                    .title(dto.getTitle())
                    .isbn(dto.getIsbn())
                    .build();

        entity = service.save(entity);
        return BookDTO.builder()
                .id(entity.getId())
                .author(entity.getAuthor())
                .title(entity.getTitle())
                .isbn(entity.getIsbn())
                .build();
    }


}

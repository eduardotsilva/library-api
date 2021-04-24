package com.eduardo.libraryapi.service;

import com.eduardo.libraryapi.api.model.entity.Book;
import com.eduardo.libraryapi.model.repository.BookRepository;
import com.eduardo.libraryapi.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;
    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl( repository);
    }

    @Test
    @DisplayName("DEVE salvar um livro")
    public void saveBookTest(){

        //cenario
        Book book = Book.builder().isbn("123").author("Fulano").title("as aventuras").build();
        Mockito.when( repository.save(book) )
                        .thenReturn(
                            Book.builder()
                                .id(1L)
                                .isbn("123")
                                .author("Fulano")
                                .title("as aventuras")
                                .build());

        //execucao
        Book savedBook = service.save(book);

        //verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("as aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }

}

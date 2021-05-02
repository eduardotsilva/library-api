package com.eduardo.libraryapi.model.repository;

import com.eduardo.libraryapi.model.entity.Book;
import com.eduardo.libraryapi.service.EmailService;
import com.eduardo.libraryapi.service.LoanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BookRepository repository;

    @MockBean
    EmailService emailService;

    @MockBean
    LoanService loanService;

    @Test
    @DisplayName("DEVE retornar TRUE quando existir um livro na base com ibs informado")
    public void returnTrueWhenIsbnExists(){
        //cenario
        String isbn  = "123";

        Book book = createNewBook(isbn);
        testEntityManager.persist(book);

        //execucao
          boolean exists = repository.existsByIsbn(isbn);

        //verificacao
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("DEVE retornar FALSE quando não existir um livro na base com ibs informado")
    public void returnFalseWhenIsbnDoesntExists(){
        //cenario
        String isbn  = "123";

        //execucao
        boolean exists = repository.existsByIsbn(isbn);

        //verificacao
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("DEVE obter um livro por id")
    public void findByIdTest(){
        //cenario
        String isbn  = "123";
        Book book = createNewBook(isbn);
        testEntityManager.persist(book);

        //execucao
        Optional<Book> foundBook = repository.findById(book.getId());

        //verificação
        assertThat(foundBook.isPresent()).isTrue();


    }

    @Test
    @DisplayName("DEVE salvar um livro")
    public void saveBookTest(){
        String isbn  = "123";
        Book book = createNewBook(isbn);
        Book savedBook = repository.save(book);

        assertThat(savedBook.getId()).isNotNull();

    }

    @Test
    @DisplayName("DEVE deletar um livro")
    public void deleteBookTest(){
        //cenario
        String isbn  = "123";
        Book book = createNewBook(isbn);
        testEntityManager.persist(book);

        Book foundBook = testEntityManager.find(Book.class, book.getId());
        repository.delete(foundBook);

        Book deletedBook = testEntityManager.find(Book.class, book.getId());
        assertThat(deletedBook).isNull();

    }

    public static Book createNewBook(String isbn) {
        return Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
    }



}

package com.eduardo.libraryapi.model.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("DEVE retornar verdadeiro quando existir um livro na base com ibs informado")
    public void returnTrueWhenIsbnExists(){
        //cenario
        String isbn  = "123";

        //execucao
          boolean exists = repository.existsByIsbn(isbn);

        //verificacao
        assertThat(exists).isTrue();
    }
}

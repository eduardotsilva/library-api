package com.eduardo.libraryapi.model.repository;


import com.eduardo.libraryapi.model.entity.Book;
import com.eduardo.libraryapi.model.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.eduardo.libraryapi.model.repository.BookRepositoryTest.createNewBook;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    LoanRepository repository;

    @Test
    @DisplayName("DEVE retornar que o LIVRO ainda não foi devolvido do empréstimo")
    public void existsByBookAndNotReturnedTest(){

        //cenário
        Book book = createNewBook("123");
        entityManager.persist(book);

        Loan loan = Loan
                    .builder()
                    .book(book)
                    .customer("Fulano")
                    .loanDate(LocalDate.now())
                    .build();

        entityManager.persist(loan);

        //execução
        boolean exists = repository.existsByBookAndNotReturned(book);

        assertThat(exists).isTrue();
    }


}

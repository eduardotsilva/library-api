package com.eduardo.libraryapi.model.repository;


import com.eduardo.libraryapi.model.entity.Book;
import com.eduardo.libraryapi.model.entity.Loan;
import com.eduardo.libraryapi.service.EmailService;
import com.eduardo.libraryapi.service.LoanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

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

    @MockBean
    EmailService emailService;


    @Test
    @DisplayName("DEVE retornar que o LIVRO ainda não foi devolvido do empréstimo")
    public void existsByBookAndNotReturnedTest(){

        //cenário
        Loan loan = createAndPersisteLoan();
        Book book = loan.getBook();

        //execução
        boolean exists = repository.existsByBookAndNotReturned(book);
        assertThat(exists).isTrue();
    }


    @Test
    @DisplayName("DEVE buscar empréstimo pelo isbn do livro ou customer")
    public void findByBookIsbnOrCustomerTest(){
        //cenário
        Loan loan = createAndPersisteLoan();
        Book book = loan.getBook();

        Page<Loan> result = repository.findByBookIsbnOrCustomer(book.getIsbn(), loan.getCustomer(), PageRequest.of(0, 10));

        List<Loan> content = result.getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.contains(loan)).isTrue();
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);

    }

    public Loan createAndPersisteLoan(){
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
        return loan;
    }

}

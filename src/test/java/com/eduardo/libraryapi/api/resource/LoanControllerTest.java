package com.eduardo.libraryapi.api.resource;

import com.eduardo.libraryapi.api.dto.LoanDTO;
import com.eduardo.libraryapi.api.dto.LoanFilterDTO;
import com.eduardo.libraryapi.api.dto.ReturnedLoanDTO;
import com.eduardo.libraryapi.exception.BusinessException;
import com.eduardo.libraryapi.model.entity.Book;
import com.eduardo.libraryapi.model.entity.Loan;
import com.eduardo.libraryapi.service.BookService;
import com.eduardo.libraryapi.service.EmailService;
import com.eduardo.libraryapi.service.LoanService;
import com.eduardo.libraryapi.service.LoanServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = LoanController.class)
public class LoanControllerTest {

    static final String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private LoanService loanService;
    @MockBean
    private EmailService emailService;

    @Test
    @DisplayName("DEVE realizar um emprestimo de livro")
    public void createLoanTest() throws Exception{

        //cen??rio
        LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").email("customer@gmail.com").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Book book = Book.builder().id(1L).isbn("123").build();

        BDDMockito.given( bookService.getBookByIsbn("123") )
                .willReturn( Optional.of(book)  );

        Loan loan = Loan.builder().id(1L).customer("Fulano").loanDate(LocalDate.now()).book(book).build();

        BDDMockito.given( loanService.save(Mockito.any(Loan.class)) )
                .willReturn(loan);

        //execu????o

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verifica????o
        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

    }

    @Test
    @DisplayName("DEVE retornar erro ao tentar fazer um empr??stimo de um livro inexistente")
    public void invalidIsbnCreateLoandTest() throws Exception{

        //cen??rio
        LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given( bookService.getBookByIsbn("123") )
                .willReturn( Optional.empty()  );



        //execu????o

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verifica????o
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("erros", Matchers.hasSize(1)))
                .andExpect(jsonPath("erros[0]").value("Livro n??o encontrado para o ISBN"))
        ;
    }

    @Test
    @DisplayName("DEVE retornar erro ao tentar fazer um empr??stimo de um livro j?? emprestado")
    public void LoanedBookErrorOnCreateLoanTest() throws Exception{

        //cen??rio
        LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Book book = Book.builder().id(1L).isbn("123").build();

        BDDMockito.given( bookService.getBookByIsbn("123") )
                .willReturn( Optional.of(book)  );

        BDDMockito.given( loanService.save(Mockito.any(Loan.class)))
                .willThrow(new BusinessException("Livro j?? emprestado"));

        //execu????o

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verifica????o
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("erros", Matchers.hasSize(1)))
                .andExpect(jsonPath("erros[0]").value("Livro j?? emprestado"))
        ;
    }

    @Test
    @DisplayName("DEVE devolver o empr??stimo do livro ")
    public void returnBookTest() throws Exception {
        //cen??rio { returned: true }

        ReturnedLoanDTO dto = ReturnedLoanDTO
                            .builder()
                            .returned(true)
                            .build();

        String json = new ObjectMapper().writeValueAsString(dto);

        Loan loa = Loan.builder().id(1L).build();
        BDDMockito.given(loanService.getById(Mockito.anyLong()))
                            .willReturn(Optional.of(loa));

        mvc
                .perform(
                        patch(LOAN_API.concat("/1"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)

                ).andExpect(status().isOk());

        Mockito.verify(loanService, Mockito.times(1)).update(loa);
    }



    @Test
    @DisplayName("DEVE retornar 404 quando tentar devolver um livro inexistente")
    public void returnInexistentBookTest() throws Exception {
        //cen??rio { returned: true }

        ReturnedLoanDTO dto = ReturnedLoanDTO
                .builder()
                .returned(true)
                .build();

        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(loanService.getById(Mockito.anyLong()))
                .willReturn(Optional.empty());

        mvc
                .perform(
                        patch(LOAN_API.concat("/1"))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)

                ).andExpect(status().isNotFound());

    }


    @Test
    @DisplayName("DEVE filtrar livros")
    public void findLoansTest() throws Exception {
        Long id = 1L;


        Loan loan = LoanServiceTest.createLoan();
        loan.setId(id);
        Book book = Book.builder().id(1L).isbn("321").author("Fulano").title("As aventuras").build();
        loan.setBook(book);
        loan.setReturned(true);

        BDDMockito.given( loanService.find( Mockito.any(LoanFilterDTO.class),Mockito.any(Pageable.class)) )
                .willReturn( new PageImpl<Loan>(Arrays.asList(loan), PageRequest.of(0,10), 1));


        String queryString = String.format("?isbn=%s&customer=%s&page=0&size=10",
                book.getIsbn(),loan.getCustomer());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LOAN_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(10))
                .andExpect(jsonPath("pageable.pageNumber").value(0))
        ;

    }



}

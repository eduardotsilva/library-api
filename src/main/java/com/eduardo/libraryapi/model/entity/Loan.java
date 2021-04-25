package com.eduardo.libraryapi.model.entity;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Loan {

    private Long id;
    private String customer;
    private Book book;
    private LocalDate loanDate;
    private Boolean returned;


}

package com.desafio.customerservice.util;

import com.desafio.customerservice.dto.PageableResponseDTO;
import com.desafio.customerservice.enumerator.DocumentTypeEnum;
import com.desafio.customerservice.model.Customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUtils {
    public static PageableResponseDTO generatePageable() {
        return PageableResponseDTO.builder()
                ._limit(10)
                ._offset(0L)
                ._pageNumber(1)
                ._pageElements(1)
                ._totalPages(1)
                ._totalElements(1L)
                ._moreElements(false)
                .build();
    };


    public static List<Customer> generateListOfCustomers() {
        List<Customer> customerList = new ArrayList<>();

        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Teste da silva")
                .document("123456789")
                .documentType(DocumentTypeEnum.PF)
                .address("Endereço de teste, numero 1")
                .email("teste@teste.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Fulano da silva")
                .document("12345678000187")
                .documentType(DocumentTypeEnum.PJ)
                .address("Endereço de teste, numero 2")
                .email("teste2@teste.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Siclano da silva")
                .document("987654321")
                .documentType(DocumentTypeEnum.PF)
                .address("Endereço de teste, numero 3")
                .email("teste3@teste.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        customerList.add(customer1);
        customerList.add(customer2);
        customerList.add(customer3);

        return customerList;
    }
}

package com.desafio.customerservice.service.impl;

import com.desafio.customerservice.dto.CreateCustomerRequestDTO;
import com.desafio.customerservice.dto.CustomerResponseDTO;
import com.desafio.customerservice.dto.PageResponseDTO;
import com.desafio.customerservice.exception.CustomBusinessException;
import com.desafio.customerservice.mapper.CustomerRequestMapperImpl;
import com.desafio.customerservice.mapper.CustomerResponseMapperImpl;
import com.desafio.customerservice.mapper.impl.PageableMapperImpl;
import com.desafio.customerservice.model.Customer;
import com.desafio.customerservice.repository.CustomerRepository;
import com.desafio.customerservice.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @Spy
    private PageableMapperImpl pageableMapper;

    @Spy
    private CustomerResponseMapperImpl customerResponseMapper;

    @Spy
    private CustomerRequestMapperImpl customerRequestMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void testShouldReturnAllCustomersWhenInvoked() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<Customer> expectedReturn = generatePage(pageRequest);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedReturn);

        PageResponseDTO<CustomerResponseDTO> customers = customerService.getCustomers(
                "teste",
                "123456789",
                1,
                10,
                "name,asc");

        verify(repository).findAll(any(Specification.class), any(Pageable.class));

        assertEquals(expectedReturn.getContent().size(),
                customers.get_content().size(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getId(),
                customers.get_content().get(0).getId(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getName(),
                customers.get_content().get(0).getName(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getAddress(),
                customers.get_content().get(0).getAddress(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getDocument(),
                customers.get_content().get(0).getDocument(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getDocumentType().name(),
                customers.get_content().get(0).getDocumentType(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getCreatedAt(),
                customers.get_content().get(0).getCreatedAt(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getUpdatedAt(),
                customers.get_content().get(0).getUpdatedAt(),
                "Should be equal");
    }

    @Test
    void testShouldReturnAllCustomersWithDefaultOrderWhenInvokedWithGenericSort() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<Customer> expectedReturn = generatePage(pageRequest);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedReturn);

        PageResponseDTO<CustomerResponseDTO> customers = customerService.getCustomers(
                "teste",
                "123456789",
                1,
                10,
                null);

        verify(repository).findAll(any(Specification.class), any(Pageable.class));

        assertEquals(expectedReturn.getContent().size(),
                customers.get_content().size(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getId(),
                customers.get_content().get(0).getId(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getName(),
                customers.get_content().get(0).getName(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getAddress(),
                customers.get_content().get(0).getAddress(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getDocument(),
                customers.get_content().get(0).getDocument(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getDocumentType().name(),
                customers.get_content().get(0).getDocumentType(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getCreatedAt(),
                customers.get_content().get(0).getCreatedAt(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getUpdatedAt(),
                customers.get_content().get(0).getUpdatedAt(),
                "Should be equal");
    }

    private Page<Customer> generatePage(PageRequest pageRequest) {

        List<Customer> customerList = TestUtils.generateListOfCustomers();

        return new PageImpl<>(customerList, pageRequest, customerList.size());
    }

    @Test
    void testShouldCreateCustomerWhenMethodIsInvoked() {
        Customer expected = Customer.builder().id(UUID.randomUUID()).build();
        CreateCustomerRequestDTO requestDTO = CreateCustomerRequestDTO.builder()
                .name("Teste da silva")
                .document("123456789")
                .documentType("PF")
                .address("Rua de teste")
                .email("teste@teste.com")
                .build();

        when(repository.save(any())).thenReturn(expected);
        CustomerResponseDTO responseDTO = customerService.create(requestDTO);

        verify(repository).save(any());

        assertEquals(expected.getId(), responseDTO.getId(), "Should be equal");
    }

    @Test
    void testShouldReturnExceptionWhenAlreadyExistsCustomerWithSameDocument() {
        Customer customer = Customer.builder().id(UUID.randomUUID()).build();
        CreateCustomerRequestDTO requestDTO = CreateCustomerRequestDTO.builder()
                .name("Teste da silva")
                .document("123456789")
                .documentType("PF")
                .address("Rua de teste")
                .email("teste@teste.com")
                .build();

        when(repository.findByDocument(any())).thenReturn(Optional.of(customer));

        CustomBusinessException exception = assertThrows(
                CustomBusinessException.class,
                () -> customerService.create(requestDTO),
                "Should throw an exception");

        assertTrue(exception.getMessage().contains("Já existe um cliente cadastrado com este documento"),
                "Should be true");
    }

    @Test
    void testShouldReturnCustomerWhenIdExists() {
        Customer expected = Customer.builder()
                .id(UUID.fromString("1a5ef3f5-55f3-439a-8095-710ae589ad51"))
                .build();

        when(repository.findById(any())).thenReturn(Optional.of(expected));

        CustomerResponseDTO customer = customerService.getById(
                UUID.fromString("1a5ef3f5-55f3-439a-8095-710ae589ad51")
        );

        verify(repository).findById(any());

        assertEquals(expected.getId(), customer.getId(), "Should be equal");
    }

    @Test
    void testShouldReturnExceptionWhenIdDoesntExist() {
        CustomBusinessException exception = assertThrows(
                CustomBusinessException.class,
                () -> customerService.getById(UUID.randomUUID()),
                "Should throw an exception");

        assertTrue(exception.getMessage().contains("não encontrado"), "Should be true");
    }
}
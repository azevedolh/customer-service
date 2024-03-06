package com.desafio.customerservice.service;

import com.desafio.customerservice.dto.CreateCustomerRequestDTO;
import com.desafio.customerservice.dto.CustomerResponseDTO;
import com.desafio.customerservice.dto.PageResponseDTO;
import com.desafio.customerservice.model.Customer;

import java.util.UUID;

public interface CustomerService {

    PageResponseDTO getCustomers(String name, String document, Integer page, Integer size, String sort);

    CustomerResponseDTO create(CreateCustomerRequestDTO customer);

    CustomerResponseDTO getById(UUID id);
}

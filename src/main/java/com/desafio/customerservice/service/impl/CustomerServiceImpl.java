package com.desafio.customerservice.service.impl;


import com.desafio.customerservice.dto.CreateCustomerRequestDTO;
import com.desafio.customerservice.dto.CustomerResponseDTO;
import com.desafio.customerservice.dto.PageResponseDTO;
import com.desafio.customerservice.enumerator.DocumentTypeEnum;
import com.desafio.customerservice.exception.CustomBusinessException;
import com.desafio.customerservice.mapper.CustomerRequestMapper;
import com.desafio.customerservice.mapper.CustomerResponseMapper;
import com.desafio.customerservice.mapper.PageableMapper;
import com.desafio.customerservice.model.Customer;
import com.desafio.customerservice.repository.CustomerRepository;
import com.desafio.customerservice.service.CustomerService;
import com.desafio.customerservice.specification.CustomerSpecifications;
import com.desafio.customerservice.util.MessageUtil;
import com.desafio.customerservice.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.desafio.customerservice.util.ConstantUtil.SORT_BY_CREATED_AT;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private PageableMapper pageableMapper;
    private CustomerResponseMapper customerResponseMapper;
    private CustomerRequestMapper customerRequestMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,
                               PageableMapper pageableMapper,
                               CustomerResponseMapper customerResponseMapper,
                               CustomerRequestMapper customerRequestMapper) {
        this.customerRepository = customerRepository;
        this.pageableMapper = pageableMapper;
        this.customerResponseMapper = customerResponseMapper;
        this.customerRequestMapper = customerRequestMapper;
    }

    @Override
    public PageResponseDTO getCustomers(String name, String document, Integer page, Integer size, String sort) {
        Sort sortProperties = PaginationUtil.getSort(sort, Sort.Direction.DESC, SORT_BY_CREATED_AT);

        PageRequest pageRequest = PageRequest.of(page - 1, size, sortProperties);
        PageResponseDTO pageResponseDTO = new PageResponseDTO();
        Page<Customer> customerPage = customerRepository.findAll(CustomerSpecifications.generateDinamic(name, document), pageRequest);

        if (customerPage != null) {
            pageResponseDTO.set_pageable(pageableMapper.toDto(customerPage));
            pageResponseDTO.set_content(customerResponseMapper.toDto(customerPage.getContent()));
        }

        return pageResponseDTO;
    }

    @Override
    public CustomerResponseDTO create(CreateCustomerRequestDTO customerRequest) {
        Optional<Customer> existingCustomer = customerRepository.findByDocument(customerRequest.getDocument());

        if (existingCustomer.isPresent()) {
            String message = MessageUtil.getMessage("customer.already.exists");
            throw new CustomBusinessException(message);
        }

        if (!DocumentTypeEnum.isValid(customerRequest.getDocumentType())) {
            String message = MessageUtil.getMessage("customer.document.type");
            String details = MessageUtil.getMessage("customer.document.type.details");
            throw new CustomBusinessException(message, details);
        }

        Customer customer = customerRequestMapper.toEntity(customerRequest);

        customer = customerRepository.save(customer);
        return customerResponseMapper.toDto(customer);
    }

    @Override
    public Customer getById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        if (customerOptional.isEmpty()) {
            String message = MessageUtil.getMessage("customer.not.found", id.toString());
            throw new CustomBusinessException(HttpStatus.NOT_FOUND, message);
        }

        return customerOptional.get();
    }
}

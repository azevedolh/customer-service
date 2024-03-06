package com.desafio.customerservice.controller;

import com.desafio.customerservice.dto.CreateCustomerRequestDTO;
import com.desafio.customerservice.dto.CustomerResponseDTO;
import com.desafio.customerservice.dto.PageResponseDTO;
import com.desafio.customerservice.dto.PostResponseDTO;
import com.desafio.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "_sort", required = false) String sort,
            @RequestParam(value = "document", required = false) String document,
            @RequestParam(value = "name", required = false) String name) {
        return new ResponseEntity<PageResponseDTO>(customerService.getCustomers(name, document, page, size, sort), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> create(
            @RequestBody @Valid CreateCustomerRequestDTO customer) {
        CustomerResponseDTO createdCustomer = customerService.create(customer);

        URI locationResource = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCustomer.getId())
                .toUri();
        log.info("Successfully created Customer with ID: " + createdCustomer.getId());
        return ResponseEntity.created(locationResource).body(PostResponseDTO.builder().id(createdCustomer.getId()).build());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable("customerId") UUID customerId) {
        return new ResponseEntity<CustomerResponseDTO>(customerService.getById(customerId), HttpStatus.OK);
    }
}

package com.desafio.customerservice.mapper;

import com.desafio.customerservice.dto.CustomerResponseDTO;
import com.desafio.customerservice.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerResponseMapper extends EntityMapper<CustomerResponseDTO, Customer> {
}

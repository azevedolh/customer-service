package com.desafio.customerservice.mapper;

import com.desafio.customerservice.dto.CreateCustomerRequestDTO;
import com.desafio.customerservice.model.Customer;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CustomerRequestMapper extends EntityMapper<CreateCustomerRequestDTO, Customer> {
}

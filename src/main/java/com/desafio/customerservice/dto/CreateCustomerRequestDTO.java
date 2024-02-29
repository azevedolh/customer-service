package com.desafio.customerservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequestDTO {

    @NotNull
    private String name;

    @NotNull
    private String document;

    @NotNull
    private String documentType;

    @NotNull
    private String address;
}

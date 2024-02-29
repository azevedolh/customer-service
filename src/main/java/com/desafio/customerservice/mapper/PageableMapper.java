package com.desafio.customerservice.mapper;

import com.desafio.customerservice.dto.PageableResponseDTO;
import org.springframework.data.domain.Page;

public interface PageableMapper {
    PageableResponseDTO toDto(Page<?> page);
}

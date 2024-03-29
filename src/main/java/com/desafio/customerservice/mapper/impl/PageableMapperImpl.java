package com.desafio.customerservice.mapper.impl;

import com.desafio.customerservice.dto.PageableResponseDTO;
import com.desafio.customerservice.mapper.PageableMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageableMapperImpl implements PageableMapper {

    @Override
    public PageableResponseDTO toDto(Page<?> page) {
        return PageableResponseDTO.builder()
                ._limit(page.getPageable().getPageSize())
                ._offset(page.getPageable().getOffset())
                ._pageNumber(page.getNumber() + 1)
                ._pageElements(page.getNumberOfElements())
                ._totalPages(page.getTotalPages())
                ._totalElements(page.getTotalElements())
                ._moreElements(!page.isLast())
                .build();
    }
}

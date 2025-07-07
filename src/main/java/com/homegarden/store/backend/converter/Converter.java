package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Product;

public interface Converter<Entity, RequestDto, ResponseDto> {

    Entity toEntity(RequestDto dto);

    ResponseDto toDto(Entity entity);

}

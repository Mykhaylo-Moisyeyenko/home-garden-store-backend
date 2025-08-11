package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.TopCancelledProductDto;

import java.util.List;

public interface ReportService {

    List<TopCancelledProductDto> getTopCancelledProducts();


}
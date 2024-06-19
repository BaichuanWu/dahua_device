package com.datang.konus.core.service;

import com.datang.konus.core.dto.DaHuaPunchDTO;
import com.datang.konus.core.dto.TimeClockDetailDTO;

import java.util.List;

public interface TimeClockDetailService {

    Integer insertTimeClockDetailWithCustomerId(TimeClockDetailDTO detailDTO);

    Integer doRecord(List<DaHuaPunchDTO> punchDTOList);
}

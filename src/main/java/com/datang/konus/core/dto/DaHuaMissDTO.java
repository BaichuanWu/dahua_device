package com.datang.konus.core.dto;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DaHuaMissDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    List<BigInteger> disconnectIDs;
}

package com.datang.konus.core.service;

import com.datang.konus.core.dto.DaHuaHistoryDTO;

import java.math.BigInteger;
import java.util.List;

public interface MissHistoryService {

    List<DaHuaHistoryDTO> MissPunchTimeRecords();

    Integer MarkHistoryDealed(List<BigInteger> disconnectIDs);

}

package com.datang.konus.core.service;

import com.datang.konus.core.dto.SystemSequenceDTO;

import java.math.BigInteger;
import java.util.List;

public interface SystemSequenceService {

    SystemSequenceDTO obtainSequnceByName(String sequenceName);

    List<BigInteger> initNextMendixIdsByTable(String table, Integer steps);
}

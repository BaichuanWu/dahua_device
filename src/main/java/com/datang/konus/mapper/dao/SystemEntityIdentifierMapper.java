package com.datang.konus.mapper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datang.konus.mapper.entity.SystemEntityEntity;
import com.datang.konus.mapper.entity.SystemEntityIdentifierEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Mapper
@Repository
public interface SystemEntityIdentifierMapper extends BaseMapper<SystemEntityIdentifierEntity> {

    public String initTableId(@Param("shordid") Short shordId,
                              @Param("objectsequence") BigInteger objectSequence);
}
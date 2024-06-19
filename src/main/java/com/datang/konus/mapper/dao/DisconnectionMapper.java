package com.datang.konus.mapper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datang.konus.mapper.entity.DisconnectionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Jane Shi
 * @since 2022-01-06
 */
@Mapper
@Repository
public interface DisconnectionMapper extends BaseMapper<DisconnectionEntity> {

    Integer markLoadedWithUpdateBatchByIds(List<BigInteger> IDs);
}

package com.datang.konus.mapper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datang.konus.mapper.entity.EntranceDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author caowu
 * @since 2023/8/16
 */
@Mapper
@Repository
public interface EntranceDetailMapper extends BaseMapper<EntranceDetailEntity> {
}

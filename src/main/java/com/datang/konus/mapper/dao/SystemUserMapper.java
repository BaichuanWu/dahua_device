package com.datang.konus.mapper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datang.konus.mapper.entity.SystemUserEntity;
import com.datang.konus.mapper.entity.TimeClockDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
public interface SystemUserMapper extends BaseMapper<SystemUserEntity> {

}

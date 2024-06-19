package com.datang.konus.mapper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datang.konus.mapper.entity.SystemSequenceEntity;
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
public interface SystemSequenceMapper extends BaseMapper<SystemSequenceEntity> {

}

package com.datang.konus.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.datang.konus.core.service.SystemSequenceService;
import com.datang.konus.core.dto.SystemSequenceDTO;
import com.datang.konus.mapper.dao.SystemEntityIdentifierMapper;
import com.datang.konus.mapper.dao.SystemEntityMapper;
import com.datang.konus.mapper.dao.SystemSequenceMapper;
import com.datang.konus.mapper.entity.SystemEntityEntity;
import com.datang.konus.mapper.entity.SystemEntityIdentifierEntity;
import com.datang.konus.mapper.entity.SystemSequenceEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SystemSequenceServImpl implements SystemSequenceService {

    @Autowired
    private SystemSequenceMapper sequenceMapper;
    @Autowired
    SystemEntityMapper entityMapper;
    @Autowired
    SystemEntityIdentifierMapper entityIdentifierMapper;

    public SystemSequenceDTO obtainSequnceByName(String sequenceName) {

        SystemSequenceDTO systemSequenceDTO = new SystemSequenceDTO();
        if (StringUtils.isBlank(sequenceName)){
            return systemSequenceDTO;
        }

        LambdaQueryWrapper<SystemSequenceEntity> sequenceEntityQueryWrapper = new LambdaQueryWrapper<>();
        sequenceEntityQueryWrapper.eq(SystemSequenceEntity::getName,sequenceName);
        SystemSequenceEntity sequenceEntity = sequenceMapper.selectOne(sequenceEntityQueryWrapper);

        if (!ObjectUtils.isEmpty(sequenceEntity)) {
            BeanUtils.copyProperties(sequenceEntity, systemSequenceDTO);
        }
        return systemSequenceDTO;
    }

    /**
     * 产生mendix table默认id的下一个id号码
     * @param table
     * @return
     */
    public synchronized List<BigInteger> initNextMendixIdsByTable(String table, Integer steps){
        if (StringUtils.isBlank(table)){
            return null;
        }

        LambdaQueryWrapper<SystemEntityEntity> entityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        entityLambdaQueryWrapper.eq(SystemEntityEntity::getTableName,table);
        SystemEntityEntity entityEntity =  entityMapper.selectOne(entityLambdaQueryWrapper);
        if (com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(entityEntity)){
            log.error("no this table :  %s",table);
            return null;
        }

        LambdaQueryWrapper<SystemEntityIdentifierEntity> identifierEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        identifierEntityLambdaQueryWrapper.eq(SystemEntityIdentifierEntity::getId,entityEntity.getId());
        SystemEntityIdentifierEntity identifierEntity =  entityIdentifierMapper.selectOne(identifierEntityLambdaQueryWrapper);

        List<BigInteger> tableIDs = new ArrayList<>();
        for (int i = 0; i < steps; i++) {
            String tableId = entityIdentifierMapper.initTableId(identifierEntity.getShortId()
                    , identifierEntity.getObjectSequence().add(BigInteger.valueOf(i)).add(BigInteger.valueOf(1l * 5000l * 1000l)));
            tableIDs.add(new BigInteger(tableId));
        }
        identifierEntity.setObjectSequence(identifierEntity.getObjectSequence().add(BigInteger.valueOf(steps)));
        entityIdentifierMapper.updateById(identifierEntity);

        return tableIDs;
    }
}

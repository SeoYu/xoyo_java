package com.vicent.xoyo.service.impl;

import com.vicent.xoyo.entity.Log;
import com.vicent.xoyo.mapper.LogMapper;
import com.vicent.xoyo.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuyu
 * @since 2020-09-16
 */
@Slf4j
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Log log) {
        this.save(log);
        return 1;
    }
}

package com.vicent.xoyo.service;

import com.vicent.xoyo.entity.Log;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyu
 * @since 2020-09-16
 */
public interface ILogService extends IService<Log> {
    int insert(Log log);
}

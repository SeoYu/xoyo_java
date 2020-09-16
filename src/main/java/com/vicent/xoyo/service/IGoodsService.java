package com.vicent.xoyo.service;

import com.vicent.xoyo.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyu
 * @since 2020-09-16
 */
public interface IGoodsService extends IService<Goods> {
    void asyncLoadSchedule();

    boolean insert(Goods good);

    boolean update(Goods good);
}

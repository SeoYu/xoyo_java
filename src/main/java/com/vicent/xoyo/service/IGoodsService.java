package com.vicent.xoyo.service;

import com.vicent.xoyo.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyu
 * @since 2020-09-16
 */
public interface IGoodsService extends IService<Goods> {
    void asyncLoadSchedule(Map<String, Object> body);

    void asyncDeleteSchedule();

    void asyncLoadDetail(String id);

    boolean insert(Goods good);

    boolean update(Goods good);

    List<Map<String,Object>> list(Map<String, Object> body);

    int listCount(Map<String, Object> body);
}

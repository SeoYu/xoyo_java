package com.vicent.xoyo.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.vicent.xoyo.constant.CampEnum;
import com.vicent.xoyo.constant.MenPaiEnum;
import com.vicent.xoyo.constant.PriceEnum;
import com.vicent.xoyo.constant.SexEnum;
import com.vicent.xoyo.entity.Goods;
import com.vicent.xoyo.mapper.GoodsMapper;
import com.vicent.xoyo.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vicent.xoyo.service.impl.Thread.GoodsLoad;
import com.vicent.xoyo.utils.Jx3Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private static final String GOODS_KEY = "GOODS:ALL";

    @Override
    @Scheduled(fixedDelay = 1000*60*60*24)
    @Async
    public void asyncLoadSchedule() {
        System.out.println("asyncLoadSchedule 同步开始");
        // 多线程
        Jx3Api jx3Api = new Jx3Api();
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.newExecutor(10, 20);
        for (PriceEnum priceEnum : PriceEnum.values()) {
            if (priceEnum == PriceEnum.ALL){
                continue;
            }
            for (MenPaiEnum menPaiEnum : MenPaiEnum.values()) {
                if (menPaiEnum == MenPaiEnum.ALL){
                    continue;
                }
                for (SexEnum sexEnum : SexEnum.values()) {
                    for (CampEnum campEnum : CampEnum.values()) {
                        Map<String,Object> body = new HashMap<String, Object>();
                        body.put("price",priceEnum.getCode());
                        body.put("role_sect",menPaiEnum.getCode());
                        body.put("role_sect",menPaiEnum.getCode());
                        body.put("role_camp",campEnum.getCode());
                        body.put("role_shape",sexEnum.getCode());

                        GoodsLoad goodsLoad = new GoodsLoad(this, jx3Api, priceEnum, menPaiEnum, sexEnum, campEnum, body, redisTemplate);
                        threadPoolExecutor.execute(goodsLoad);

                    }
                }
            }
        }

        threadPoolExecutor.shutdown();
        System.out.println("asyncLoadSchedule 同步结束");
    }

    @Override
    public boolean insert(Goods good) {
        String lock = GOODS_KEY +":"+good.getConsignmentId() + ":LOCK";
        if (redisTemplate.hasKey(lock)) {
            return true;
        }
        redisTemplate.opsForValue().set(lock,1,3, TimeUnit.SECONDS);
        boolean save = this.save(good);
        redisTemplate.delete(lock);
        return save;
    }

    @Override
    public boolean update(Goods good) {
        String lock = GOODS_KEY +":"+good.getConsignmentId() + ":LOCK";
        if (redisTemplate.hasKey(lock)) {
            return true;
        }
        redisTemplate.opsForValue().set(lock,1,3, TimeUnit.SECONDS);
        boolean save = this.save(good);
        redisTemplate.delete(lock);
        return save;
    }
}

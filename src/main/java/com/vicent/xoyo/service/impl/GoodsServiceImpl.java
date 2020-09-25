package com.vicent.xoyo.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vicent.xoyo.constant.*;
import com.vicent.xoyo.entity.GoodInfo;
import com.vicent.xoyo.entity.Goods;
import com.vicent.xoyo.mapper.GoodsMapper;
import com.vicent.xoyo.service.IGoodInfoService;
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

import java.sql.Wrapper;
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

    @Autowired
    IGoodInfoService goodInfoService;

    private static final String GOODS_KEY = "GOODS:ALL";

    @Override
//    @Scheduled(fixedDelay = 1000*60*60*24)
    @Scheduled(cron = "0 0 6 * * ?")
    @Async
    public void asyncLoadSchedule() {
        System.out.println("asyncLoadSchedule 同步开始");
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.newExecutor(20, 1000);
        try {
            // 多线程
            Jx3Api jx3Api = new Jx3Api();
            Map<String,Object> baseInfo = new HashMap<String, Object>();
            baseInfo.put("ts_session_id","NCie440L1zrJ5nffJTtyTFVsH9fv86FAdCCMQauV");
            baseInfo.put("xoyokey","ocgWZW2%26%261Lf3Ef3EWWW%26%26s3xee902%3D5PggPZ%26sf9E02%3D3EEW%26%26se9Kg5.H5H.5%26xNZUWU9%3DPPE5%3D3c%3DxsPE-1fWfgWE5u.HP0W%26u93gP%3DWW3%263");
            jx3Api.setBaseInfo(baseInfo);
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
//                            for (ZoneEnum zoneEnum : ZoneEnum.values()) {
                                Map<String,Object> body = new HashMap<String, Object>();
                                body.put("price",priceEnum.getCode());
                                body.put("role_sect",menPaiEnum.getCode());
                                body.put("role_camp",campEnum.getCode());
                                body.put("role_shape",sexEnum.getCode());
//                                body.put("zone_id",zoneEnum.getZone());
                                body.put("zone_id","");
//                                body.put("server_id",zoneEnum.getServer());
                                body.put("server_id","");

                                GoodsLoad goodsLoad = new GoodsLoad(this, jx3Api, priceEnum, menPaiEnum, sexEnum, campEnum,ZoneEnum.ONE, body, redisTemplate);
                                threadPoolExecutor.execute(goodsLoad);
//                            }
                        }
                    }
                }
            }
        } finally {
            threadPoolExecutor.shutdown();
        }
        System.out.println("asyncLoadSchedule 同步结束");
    }

    @Override
    @Scheduled(cron = "0 30 6 * * ?")
    public void asyncDeleteSchedule() {

    }

    @Override
    public void asyncLoadDetail(String id) {
        String lockKey = GOODS_KEY +":"+ id + ":INFO:LOCK";
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, 1, 5, TimeUnit.SECONDS);
        if (lock){
            Jx3Api jx3Api = new Jx3Api();
            Map<String,Object> body = new HashMap<String, Object>();
            body.put("consignment_id",id);
            body.put("additional_key","role_appearance_info");
            Map<String, Object> appearance = jx3Api.takeDetail(body);
            body.put("additional_key","role_adventure_info");
            Map<String, Object> adventure = jx3Api.takeDetail(body);


            // 奇遇信息结构
            List<Map<String, Object>> adventureList = MapUtil.get(adventure, "adventure", new TypeReference<List<Map<String, Object>>>() {
            });
            StringBuilder adventureStr = new StringBuilder();
            for (Map<String, Object> adventureInfo : adventureList) {
                if (adventureInfo.containsKey("classify") && 2 == MapUtil.getInt(adventureInfo,"classify").intValue()){
                    // 大奇遇
                    adventureStr.append(MapUtil.getStr(adventureInfo,"name"));
                    adventureStr.append("|");
                }
            }

            // 外观信息解构
            Map<String, Object> appearanceInfo = MapUtil.get(appearance, "appearance", new TypeReference<Map<String, Object>>() {
            });
            List<Map<String, Object>> hairs = MapUtil.get(appearanceInfo, "hair", new TypeReference<List<Map<String, Object>>>() {
            });
            List<Map<String, Object>> backCloaks = MapUtil.get(appearanceInfo, "backCloak", new TypeReference<List<Map<String, Object>>>() {
            });
            List<Map<String, Object>> shopExteriors = MapUtil.get(appearanceInfo, "shopExterior", new TypeReference<List<Map<String, Object>>>() {
            });
            List<Map<String, Object>> horses = MapUtil.get(appearanceInfo, "horse", new TypeReference<List<Map<String, Object>>>() {
            });
            List<Map<String, Object>> pets = MapUtil.get(appearanceInfo, "pet", new TypeReference<List<Map<String, Object>>>() {
            });
            StringBuilder hairStr = new StringBuilder();
            for (Map<String, Object> hair : hairs) {
                hairStr.append(MapUtil.getStr(hair,"name"));
                hairStr.append("|");
            }
            StringBuilder backCloakStr = new StringBuilder();
            for (Map<String, Object> backCloak : backCloaks) {
                backCloakStr.append(MapUtil.getStr(backCloak,"name"));
                backCloakStr.append("|");
            }
            StringBuilder shopExteriorStr = new StringBuilder();
            for (Map<String, Object> shopExterior : shopExteriors) {
                shopExteriorStr.append(MapUtil.getStr(shopExterior,"name"));
                shopExteriorStr.append("|");
            }
            StringBuilder horseStr = new StringBuilder();
            for (Map<String, Object> horse : horses) {
                horseStr.append(MapUtil.getStr(horse,"name"));
                horseStr.append("|");
            }
            StringBuilder petStr = new StringBuilder();
            for (Map<String, Object> pet : pets) {
                petStr.append(MapUtil.getStr(pet,"name"));
                petStr.append("|");
            }

            GoodInfo goodInfo = GoodInfo.builder()
                .consignmentId(id)
                .adventure(adventureStr.toString())
                .hair(hairStr.toString())
                .backCloak(backCloakStr.toString())
                .horse(horseStr.toString())
                .pet(petStr.toString())
                .shopExterior(shopExteriorStr.toString())
                .build();

            try {
                if (redisTemplate.opsForHash().hasKey("GOODS:INFO",id)){
                    QueryWrapper<GoodInfo> consignment_id = new QueryWrapper<GoodInfo>().eq("consignment_id", goodInfo.getConsignmentId());
                    boolean update = goodInfoService.update(goodInfo, consignment_id);
                }else{
                    boolean save = goodInfoService.save(goodInfo);
                }
            }catch (Exception e){

            }
            redisTemplate.opsForHash().put("GOODS:INFO",id,goodInfo);
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    public boolean insert(Goods good) {
        String lockKey = GOODS_KEY +":"+good.getConsignmentId() + ":LOCK";
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, 1, 3, TimeUnit.SECONDS);
        if (!lock){
            return true;
        }
        try {
            boolean save = this.save(good);
            redisTemplate.delete(lockKey);
            return save;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean update(Goods good) {
        String lockKey = GOODS_KEY +":"+good.getConsignmentId() + ":LOCK";
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, 1, 3, TimeUnit.SECONDS);
        if (!lock){
            return true;
        }
        try {

            QueryWrapper<Goods> consignment_id = new QueryWrapper<Goods>().eq("consignment_id", good.getConsignmentId());
            boolean save = this.update(good, consignment_id);
            redisTemplate.delete(lockKey);
            return save;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> list(Map<String, Object> body) {
        List<Map<String, Object>> list = this.getBaseMapper().list(body);
        return list;
    }

    @Override
    public int listCount(Map<String, Object> body) {
        return this.getBaseMapper().listCount(body);
    }
}

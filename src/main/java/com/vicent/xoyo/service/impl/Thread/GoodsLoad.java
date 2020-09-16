package com.vicent.xoyo.service.impl.Thread;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import com.vicent.xoyo.constant.CampEnum;
import com.vicent.xoyo.constant.MenPaiEnum;
import com.vicent.xoyo.constant.PriceEnum;
import com.vicent.xoyo.constant.SexEnum;
import com.vicent.xoyo.entity.Goods;
import com.vicent.xoyo.service.IGoodsService;
import com.vicent.xoyo.utils.Jx3Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * @author vincent
 * @description
 * @Date 2020/9/16 5:26 下午
 */
@Data
@AllArgsConstructor
public class GoodsLoad implements Runnable {
    private static final String GOODS_KEY = "GOODS:ALL";

    IGoodsService goodsService;

    Jx3Api jx3Api;

    PriceEnum priceEnum;

    MenPaiEnum menPaiEnum;

    SexEnum sexEnum;

    CampEnum campEnum;

    Map<String, Object> body;

    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run() {
        List<Map<String, Object>> list = jx3Api.takeList(body);
        System.out.println(priceEnum.getDesc() + " " + menPaiEnum.getDesc() + " " + sexEnum.getDesc() + " " + campEnum.getDesc() + " " + list.size());
        for (Map<String, Object> info : list) {
            LocalDateTime remaining_time = LocalDateTime.now().plus(MapUtil.getLong(info, "remaining_time").longValue(), ChronoUnit.SECONDS);
            Map<String, Object> attrs = MapUtil.get(info, "attrs", new TypeReference<Map<String, Object>>() {
            });
            Goods good = Goods.builder()
                .consignmentId(MapUtil.getStr(info, "consignment_id"))
                .followedNum(MapUtil.getInt(info, "followed_num"))
                .info(MapUtil.getStr(info, "info"))
                .zoneId(MapUtil.getStr(info, "zone_id"))
                .remainingTime(remaining_time)
                .state(MapUtil.getInt(info, "state"))
                .serverId(MapUtil.getStr(info, "server_id"))
                .isFollowed(MapUtil.getInt(info, "is_followed"))
                .singleUnitPrice(MapUtil.getStr(info, "single_unit_price"))
                .thumb(MapUtil.getStr(info, "thumb"))
                .roleCamp(MapUtil.getStr(attrs, "role_camp"))
                .roleShape(MapUtil.getStr(attrs, "role_shape"))
                .roleSect(MapUtil.getStr(attrs, "role_sect"))
                .roleExperiencePoint(MapUtil.getStr(attrs, "role_experience_point"))
                .build();
            boolean save = true;
            // db
            if (!redisTemplate.opsForHash().hasKey(GOODS_KEY, good.getConsignmentId())) {
                save = goodsService.insert(good);
            }
            if (save) {
                // redis
                redisTemplate.opsForHash().put(GOODS_KEY, good.getConsignmentId(), good);
            }
        }
    }
}

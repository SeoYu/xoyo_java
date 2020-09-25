package com.vicent.xoyo.controller;


import com.vicent.xoyo.entity.Result;
import com.vicent.xoyo.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuyu
 * @since 2020-09-16
 */
@Slf4j
@RestController
@RequestMapping("/xoyo/goods")
public class GoodsController {
    @Autowired
    IGoodsService goodsService;

    @PostMapping("/list")
    public Result list(@RequestBody Map<String,Object> body){

        List<Map<String, Object>> list = goodsService.list(body);
        int count = goodsService.listCount(body);

        Map<String, Object> res = new HashMap<>();
        res.put("list",list);
        res.put("count",count);

        return Result.success(res);
    }
}

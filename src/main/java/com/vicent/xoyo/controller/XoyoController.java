package com.vicent.xoyo.controller;

import com.vicent.xoyo.service.IGoodsService;
import com.vicent.xoyo.utils.Jx3Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vincent
 * @description
 * @Date 2020/9/15 3:22 下午
 */
@RestController
@Slf4j
public class XoyoController {

    @Autowired
    IGoodsService goodsService;

    @PostMapping("/submitForm")
    public Map<String, Object> submitForm(@RequestBody Map<String,Object> body){
        HashMap<String, Object> response = new HashMap<>();
        response.put("code","0");
        try {
            Jx3Api api = new Jx3Api();
            api = api.setBaseInfo(body);
            String orderId = api.takeOrder(body);
            if (!"SUCCESS".equals(orderId)){
                throw new Exception(orderId);
            }
            body.put("order_id",orderId);
            String payUrl = api.takePay(body);
            response.put("url",payUrl);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            response.put("code","-1");
            response.put("msg",e.getMessage());
        }
        return response;
    }

    @PostMapping("/admin/load")
    public String load(@RequestBody Map<String,Object> body){
        goodsService.asyncLoadSchedule();
        return "success";
    }
}

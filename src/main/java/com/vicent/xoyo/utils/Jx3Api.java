package com.vicent.xoyo.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vincent
 * @description
 * @Date 2020/9/15 11:05 上午
 */
public class Jx3Api {

    private static final String CREAT_URL = "https://api-wanbaolou.xoyo.com/api/buyer/order/create";
    private static final String PAY_URL = "https://api-wanbaolou.xoyo.com/api/buyer/order/pay";
    private static final String LIST_URL = "https://api-wanbaolou.xoyo.com/api/buyer/goods/list";
    private static final String DETAIL_URL = "https://api-wanbaolou.xoyo.com/api/buyer/goods/additional_data";
    private static final String SUCCESS = "SUCCESS";
    private HttpCookie ts_session_id_;
    private HttpCookie xoyokey_;
    private String orderId;

    public Jx3Api setBaseInfo(Map<String, Object> body){
        this.ts_session_id_ = new HttpCookie("ts_session_id_",MapUtil.getStr(body,"ts_session_id"));
        this.xoyokey_ = new HttpCookie("xoyokey_",MapUtil.getStr(body,"xoyokey"));
        return this;
    }

    public String takeOrder(Map<String, Object> body){
        Map<String,Object> param = new HashMap<String, Object>(){{
            put("buy_type",0);
            put("req_id", IdUtil.fastUUID());
            put("total_price",MapUtil.getStr(body,"total_price")); // 总价
            put("total_quantity",1); // 总量
            put("total_unit_count",1); // 总产品数 去重
            put("order_type",2);
            put("service_fee_info",new HashMap<String, Object>(){{
                put("separation_service_fee",MapUtil.getStr(body,"separation_service_fee")); // 手续费 新账号 90 老账号 180
                put("transfer_service_fee",0); // 转服费用
            }});
            put("consignee_info",new HashMap<String, Object>(){{
                put("zone_id",MapUtil.getStr(body,"zone_id"));
                put("server_id",MapUtil.getStr(body,"server_id"));
//                server_id: "gate0807" 绝代
//                zone_id: "z08" 电信8
            }});
            put("list",new ArrayList<Map<String, Object>>(){{
                add(new HashMap<String, Object>(){{
                    put("count",1);
                    put("id",MapUtil.getStr(body,"id")); // 够买的号 id
                }});
            }});
        }};
        HttpResponse execute = HttpRequest.post(CREAT_URL).cookie(ts_session_id_,xoyokey_).body(JSONUtil.toJsonStr(param)).execute();
        System.out.println(JSONUtil.toJsonStr(execute.body()));
        String response = execute.body();
        Map<String, Object> convert = Convert.convert(new TypeReference<Map<String, Object>>() {
        }, JSONUtil.parseObj(response));
        if (!SUCCESS.equals(MapUtil.getStr(convert,"msg"))){
            return MapUtil.getStr(convert,"msg");
        }
        Map<String, Object> data = MapUtil.get(convert, "data", new TypeReference<Map<String, Object>>() {
        });

        this.orderId = MapUtil.getStr(data,"order_id");
        return MapUtil.getStr(convert,"msg");
    }

    public String takePay(Map<String, Object> body){
        Map<String,Object> param1 = new HashMap<String, Object>(){{
            put("req_id",IdUtil.fastUUID());
            put("order_id",orderId);
            put("pay_way_code","alipay_qr");
            put("order_type",2);
            put("__ts__",System.currentTimeMillis());// 时间戳
            put("callback","__xfe12");
        }};
        HttpResponse execute = HttpRequest.get(PAY_URL).cookie(ts_session_id_, xoyokey_).form(param1).execute();
        System.out.println(JSONUtil.toJsonStr(execute.body()));
        String response = execute.body();
        response = response.replace("__xfe12(", "").replace(");", "");
        Map<String, Object> convert = Convert.convert(new TypeReference<Map<String, Object>>() {
        }, JSONUtil.parseObj(response));
        if (!SUCCESS.equals(MapUtil.getStr(convert,"msg"))){
            return MapUtil.getStr(convert,"msg");
        }
        Map<String, Object> data = MapUtil.get(convert, "data", new TypeReference<Map<String, Object>>() {
        });

        return MapUtil.getStr(data,"pay_attach");
    }

    public List<Map<String, Object>> takeList(Map<String, Object> body){
        Map<String,Object> param1 = new HashMap<String, Object>(){{
            put("req_id",IdUtil.fastUUID());
            put("zone_id",MapUtil.getStr(body,"zone_id"));
            put("server_id",MapUtil.getStr(body,"server_id"));
            put("filter[price]",MapUtil.getStr(body,"price"));
            put("filter[state]",0);
            put("filter[tags]",0);
            put("filter[role_sect]",MapUtil.getStr(body,"role_sect"));
            put("filter[role_shape]",MapUtil.getStr(body,"role_shape"));
            put("filter[role_camp]",MapUtil.getStr(body,"role_camp"));
            put("filter[role_equipment_point]",0);
            put("filter[role_experience_point]",0);
            put("game","jx3");
            put("page",1);
            put("size",500);
            put("goods_type",2);
            put("sort[price]",1);
            put("__ts__",System.currentTimeMillis());// 时间戳
        }};
        HttpResponse execute = HttpRequest.get(LIST_URL).cookie(ts_session_id_, xoyokey_).form(param1).execute();
//        System.out.println(JSONUtil.toJsonStr(execute.body()));
        String response = execute.body();
        Map<String, Object> convert = Convert.convert(new TypeReference<Map<String, Object>>() {
        }, JSONUtil.parseObj(response));
        if (!SUCCESS.equals(MapUtil.getStr(convert,"msg"))){
            return null;
        }
        Map<String, Object> data = MapUtil.get(convert, "data", new TypeReference<Map<String, Object>>() {
        });

        return MapUtil.get(data, "list", new TypeReference<List<Map<String, Object>>>() {
        });
    }

    public Map<String, Object> takeDetail(Map<String, Object> body){
        Map<String,Object> param1 = new HashMap<String, Object>(){{
            put("req_id",IdUtil.fastUUID());
            put("consignment_id",MapUtil.getStr(body,"consignment_id"));
            put("goods_type",2);
//            role_base_info
//            role_equipment_info
//            role_appearance_info
//            role_adventure_info
            put("additional_key",MapUtil.getStr(body,"additional_key"));
            put("__ts__",System.currentTimeMillis());// 时间戳
        }};
        HttpResponse execute = HttpRequest.get(DETAIL_URL).cookie(ts_session_id_, xoyokey_).form(param1).execute();
        String response = execute.body();
        Map<String, Object> convert = Convert.convert(new TypeReference<Map<String, Object>>() {
        }, JSONUtil.parseObj(response));
        if (!SUCCESS.equals(MapUtil.getStr(convert,"msg"))){
            return null;
        }
        Map<String, Object> data = MapUtil.get(convert, "data", new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> additional_data = MapUtil.get(data, "additional_data", new TypeReference<Map<String, Object>>() {
        });
        return additional_data;
    }
}

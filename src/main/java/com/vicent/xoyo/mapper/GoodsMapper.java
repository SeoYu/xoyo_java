package com.vicent.xoyo.mapper;

import com.vicent.xoyo.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xuyu
 * @since 2020-09-16
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    List<Map<String, Object>> list(Map<String, Object> body);

    int listCount(Map<String, Object> body);

}

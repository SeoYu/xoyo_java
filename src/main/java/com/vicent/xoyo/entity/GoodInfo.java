package com.vicent.xoyo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xuyu
 * @since 2020-09-16
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String consignmentId;

    /**
     * 头发
     */
    private String hair;

    /**
     * 成衣
     */
    @TableField("shopExterior")
    private String shopExterior;

    /**
     * 披风
     */
    @TableField("backCloak")
    private String backCloak;

    /**
     * 坐骑
     */
    private String horse;

    /**
     * 挂宠
     */
    private String pet;

    /**
     * 奇遇
     */
    private String adventure;


}

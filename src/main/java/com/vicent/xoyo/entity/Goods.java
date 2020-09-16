package com.vicent.xoyo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品 id
     */
    private String consignmentId;

    /**
     * 关注数
     */
    private Integer followedNum;

    /**
     * info
     */
    private String info;

    /**
     * 是否关注
     */
    private Integer isFollowed;

    /**
     * 1
     */
    private Integer remainUnitCount;

    /**
     * 剩余时间
     */
    private LocalDateTime remainingTime;

    /**
     * 角色名
     */
    private String sellerRoleName;

    /**
     * 服
     */
    private String serverId;

    /**
     * 价格
     */
    private String singleUnitPrice;

    /**
     * 1
     */
    private Integer singleUnitSize;

    /**
     * 4 公示期 5 在售
     */
    private Integer state;

    /**
     * 标签数组
     */
    private String tags;

    /**
     * 头像
     */
    private String thumb;

    /**
     * 区
     */
    private String zoneId;

    /**
     * 阵营
     */
    private String roleCamp;

    /**
     * 体型
     */
    private String roleShape;

    /**
     * 门派
     */
    private String roleSect;

    /**
     * 资历
     */
    private String roleExperiencePoint;


}

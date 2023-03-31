package com.base.sbc.pdm.dao.material;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.pdm.entity.material.Material;
import com.base.sbc.pdm.entity.material.MaterialDetails;
import com.base.sbc.pdm.entity.material.MaterialLabel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:48:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialAllDto extends MaterialDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 状态查询数组
     */
    private String[] statusList;
    /**
     * 所有标签
     */
    private List<MaterialLabel> labels;

    /**
     * 查询条件标签id集合
     */
    private String[] labelIds;

    /**
     * 收藏id
     */
    private String collectId;

    /**
     * ids 查询的id集合
     */
    private List<String> ids;

    /**
     * 尺码筛选
     */
    private String sizeId;

    /**
     * 颜色筛选
     */
    private String colorId;

    /**
     * 所在部门
     */
    private String deptName;

    /**
     * 图片地址
     */
    private String pictureUrl;
    /**
     * 图片格式
     */
    private String pictureFormat;
    /**
     * 图片大小
     */
    private String pictureSize;
    /**
     * 素材文件名
     */
    private String materialName;
    /**
     * 素材编码
     */
    private String materialCode;
    /**
     * 所属素材库
     */
    private String materialLibrary;
    /**
     * 所属素材库名称
     */
    private String libraryName;

    /**
     * 审核状态（0：未审核，1：审核通过，2：审核不通过）
     */
    private String status;
    /**
     * 所属分类id
     */
    private String materialType;
    /**
     * 所属分类名称
     */
    private String typeName;
    /**
     * 年份
     */
    private String particularYear;
    /**
     * 月份
     */
    private String month;
    /**
     * 来源地
     */
    private String source;
    /**
     * 季节
     */
    private String season;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 品牌规模
     */
    private String brandScale;
    /**
     * 缺点
     */
    private String drawback;
    /**
     * 风险评估
     */
    private String assess;

    public Material toMaterial() {
        Material material = new Material();
        BeanUtil.copyProperties(this, material);
        return material;
    }

    public MaterialDetails toMaterialDetails() {
        MaterialDetails details = new MaterialDetails();
        BeanUtil.copyProperties(this, details);
        return details;
    }
}

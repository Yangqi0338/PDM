package com.base.sbc.module.patternmaking.enums;

/**
 * 类描述：打版管理状态
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.enums.EnumNodeStatus
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-31 10:16
 */
public enum EnumNodeStatus {

    DESIGN_SEND("款式设计", "设计下发"),
    TECHNICAL_ROOM_RECEIVED("技术中心", "已接收"),
    TECHNICAL_ROOM_SEND("技术中心", "版房主管下发"),
    SAMPLE_TASK_WAITING_RECEIVE("打版任务", "待接收"),
    SAMPLE_TASK_RECEIVED("打版任务", "已接收"),
    SAMPLE_TASK_IN_VERSION("打版任务", "打版中"),
    SAMPLE_TASK_VERSION_COMPLETE("打版任务", "打版完成"),
    GARMENT_CUTTING_KITTING("样衣任务", "物料齐套"),
    GARMENT_CUTTING_WAITING_RECEIVED("样衣任务", "裁剪待接收"),
    GARMENT_CUTTING_RECEIVED("样衣任务", "裁剪已接收"),
    GARMENT_CUTTING_STARTED("样衣任务", "裁剪开始"),
    GARMENT_CUTTING_COMPLETE("样衣任务", "裁剪完成"),
    GARMENT_WAITING_ASSIGNMENT("样衣任务", "待分配"),
    GARMENT_ASSIGNED("样衣任务", "已分配"),
    GARMENT_SEWING_STARTED("样衣任务", "车缝开始"),
    GARMENT_SEWING_COMPLETE("样衣任务", "车缝完成"),
    GARMENT_COMPLETE("样衣任务", "样衣完成"),
    GARMENT_SEWING_NOT_START("样衣任务", "车缝未开始"),
    GARMENT_SEWING_ING("样衣任务", "车缝进行中");


    private final String node;
    private final String status;

    EnumNodeStatus(String node, String status) {
        this.node = node;
        this.status = status;
    }

    public String getNode() {
        return node;
    }

    public String getStatus() {
        return status;
    }

    public static EnumNodeStatus byNodeStatus(String node, String status) {
        for (EnumNodeStatus statusEnum : EnumNodeStatus.values()) {
            if (statusEnum.getNode().equals(node) && statusEnum.getStatus().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }

}

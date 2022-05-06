package com.qingyan.traffictool.entity;

import lombok.Data;

@Data
public class Record {
    /**
     * 主键
     */
    private Long bh;
    /**
     * 访问路径
     */
    private String url;
    /**
     * 成功访问次数
     */
    private int number;
}

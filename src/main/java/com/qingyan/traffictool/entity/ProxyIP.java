package com.qingyan.traffictool.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProxyIP {
    /**
     * 代理地址
     */
    private String address;
    /**
     * 代理端口
     */
    private String port;
}



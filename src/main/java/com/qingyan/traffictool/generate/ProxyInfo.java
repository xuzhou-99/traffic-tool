package com.qingyan.traffictool.generate;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * proxy_info
 *
 * @author
 */
@Data
public class ProxyInfo implements Serializable {
    /**
     * 主键
     */
    private String bh;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 是否有效，1有效，0失效
     */
    private Integer valid;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 修改时间
     */
    private LocalDateTime updatetime;

    private static final long serialVersionUID = 1L;
}

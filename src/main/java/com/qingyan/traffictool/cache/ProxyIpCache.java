package com.qingyan.traffictool.cache;

import java.util.ArrayList;
import java.util.List;

import com.qingyan.traffictool.entity.ProxyIP;

import lombok.Getter;
import lombok.Setter;

/**
 * ProxyIpCache
 *
 * @author xuzhou
 * @version v1.0.0
 * @date 2022/4/29 17:17
 */
@Setter
@Getter
public class ProxyIpCache {

    public static List<ProxyIP> proxyIPList = new ArrayList<>();
}

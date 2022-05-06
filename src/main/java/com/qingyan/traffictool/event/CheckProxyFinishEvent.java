package com.qingyan.traffictool.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.qingyan.traffictool.generate.ProxyInfo;

import lombok.Getter;

/**
 * CheckProxyFinishEvent
 * 代理有效性检查结束事件
 *
 * @author xuzhou
 * @version v1.0.0
 * @date 2022/5/6 23:26
 */
@Getter
public class CheckProxyFinishEvent extends ApplicationEvent {

    private final List<ProxyInfo> proxyInfoList;

    public CheckProxyFinishEvent(Object source, List<ProxyInfo> proxyInfoList) {
        super(source);
        this.proxyInfoList = proxyInfoList;
    }

}

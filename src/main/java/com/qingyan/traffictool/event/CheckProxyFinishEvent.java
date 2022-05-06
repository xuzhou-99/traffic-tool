package com.qingyan.traffictool.event;

import java.time.Clock;
import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.qingyan.traffictool.generate.ProxyInfo;

import lombok.Getter;

/**
 * CheckProxyFinishEvent
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

    public CheckProxyFinishEvent(Object source, Clock clock, List<ProxyInfo> proxyInfoList) {
        super(source, clock);
        this.proxyInfoList = proxyInfoList;
    }
}

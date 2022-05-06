package com.qingyan.traffictool.event;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.qingyan.traffictool.generate.ProxyInfo;
import com.qingyan.traffictool.generate.ProxyInfoDaoExtend;

/**
 * CheckProxyListener
 *
 * @author xuzhou
 * @version v1.0.0
 * @date 2022/5/6 23:27
 */
@Component
public class CheckProxyListener implements ApplicationListener<CheckProxyFinishEvent> {

    @Resource
    private ProxyInfoDaoExtend proxyInfoDaoExtend;

    @Override
    public void onApplicationEvent(CheckProxyFinishEvent event) {
        List<ProxyInfo> proxyInfoList = event.getProxyInfoList();
        if (!proxyInfoList.isEmpty()) {
            proxyInfoDaoExtend.batchUpdate(proxyInfoList);
        }
    }
}

package com.qingyan.traffictool.task;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.qingyan.traffictool.event.CheckProxyFinishEvent;
import com.qingyan.traffictool.generate.ProxyInfo;
import com.qingyan.traffictool.generate.ProxyInfoDaoExtend;
import com.qingyan.traffictool.util.BuildUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xuzhou
 */
@Slf4j
@Component
public class ProxyRequestTask {

    @Resource
    private ApplicationContext context;

    /**
     * 批量代理IP有效检测
     *
     * @param proxyInfoList
     * @param reqUrl
     */
    public void checkProxyIpOnce(List<ProxyInfo> proxyInfoList, String reqUrl) {
        BuildUtil.checkProxyIp(proxyInfoList, reqUrl);
        if (proxyInfoList.size() > 0) {
            context.publishEvent(new CheckProxyFinishEvent(this, proxyInfoList));
        }
    }

}

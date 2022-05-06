package com.qingyan.traffictool.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qingyan.traffictool.cache.ProxyIpCache;
import com.qingyan.traffictool.entity.ProxyIP;
import com.qingyan.traffictool.generate.ProxyInfo;
import com.qingyan.traffictool.generate.ProxyInfoDaoExtend;
import com.qingyan.traffictool.task.CheckProxyRunnable;
import com.qingyan.traffictool.task.ProxyRequestRunnable;
import com.qingyan.traffictool.task.ProxyRequestTask;
import com.qingyan.traffictool.task.ThreadPoolConfig;
import com.qingyan.traffictool.util.ProxyGetUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * CheckService
 *
 * @author xuzhou
 * @version v1.0.0
 * @date 2022/5/6 17:18
 */
@Service
@Slf4j
public class CheckService {

    @Resource
    private ProxyInfoDaoExtend proxyInfoDaoExtend;

    @Resource
    private ThreadPoolConfig threadPoolConfig;

    @Resource
    private ProxyRequestTask proxyRequestTask;

    @Value("${proxy.url}")
    private String proxyUrl;

    @Value("${proxy.checkUrl}")
    private String checkUrl;

    /**
     * 爬取代理-从第三方网站获取
     */
    public void findProxyIps() {
        List<ProxyIP> proxyIpList = ProxyGetUtil.getProxyIP(proxyUrl);
        ProxyIpCache.proxyIPList.addAll(proxyIpList);
        LocalDateTime dateTime = LocalDateTime.now();
        List<ProxyInfo> proxyInfoList = proxyIpList.stream()
                .map(o -> {
                    ProxyInfo proxyInfo = new ProxyInfo();
                    proxyInfo.setBh(o.getAddress());
                    proxyInfo.setIp(o.getAddress());
                    proxyInfo.setPort(o.getPort());
                    proxyInfo.setValid(0);
                    proxyInfo.setCreatetime(dateTime);
                    proxyInfo.setUpdatetime(dateTime);
                    return proxyInfo;
                }).collect(Collectors.toList());
        if (!proxyInfoList.isEmpty()) {
            proxyInfoDaoExtend.batchInsert(proxyInfoList);
        }
    }

    /**
     * 校验代理有效性
     */
    public void check() {
        List<ProxyInfo> proxyInfoList = proxyInfoDaoExtend.selectAll();
        if (proxyInfoList.size() > 100) {
            log.info("----------------- 【多线程】开始校验代理Ip是否有效 -------------------");
            int size = proxyInfoList.size();
            int batchNum = 10;
            int length = size / batchNum;
            int start = 0;
            int end = length;
            for (int i = 1; i <= batchNum; i++) {
                List<ProxyInfo> proxyInfos = proxyInfoList.subList(start, end);
                log.info("----------------- 开始校验第 {} 批 size = {} -------------------", i, proxyInfos.size());
                threadPoolConfig.threadPool.execute(new CheckProxyRunnable(checkUrl, proxyInfos, proxyRequestTask));

                start = end;
                end += length;
                if (end >= proxyInfoList.size()) {
                    end = proxyInfoList.size();
                }
            }

        } else {
            log.info("----------------- 【单线程】开始校验代理Ip是否有效 -------------------");
            threadPoolConfig.threadPool.execute(new CheckProxyRunnable(checkUrl, proxyInfoList, proxyRequestTask));
        }
    }

    /**
     * 新增访问量
     *
     * @param url 访问地址
     * @param num 访问次数
     */
    public void addTraffic(String url, int num) {
        threadPoolConfig.threadPool.execute(new ProxyRequestRunnable(url, num, proxyInfoDaoExtend));
    }
}

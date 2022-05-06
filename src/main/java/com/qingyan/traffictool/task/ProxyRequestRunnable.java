package com.qingyan.traffictool.task;

import java.util.List;
import java.util.stream.Collectors;

import com.qingyan.traffictool.entity.ProxyIP;
import com.qingyan.traffictool.generate.ProxyInfo;
import com.qingyan.traffictool.generate.ProxyInfoDaoExtend;
import com.qingyan.traffictool.util.BuildUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * ProxyRequestRunnable
 *
 * @author xuzhou
 * @version v1.0.0
 * @date 2022/5/5 18:33
 */
@Slf4j
public class ProxyRequestRunnable implements Runnable {

    private final String url;

    private final int number;

    private final ProxyInfoDaoExtend proxyInfoDaoExtend;

    public ProxyRequestRunnable(String url, int number, ProxyInfoDaoExtend proxyInfoDaoExtend) {
        this.url = url;
        this.number = number;
        this.proxyInfoDaoExtend = proxyInfoDaoExtend;
    }

    @Override
    public void run() {
        List<ProxyInfo> proxyInfoList = proxyInfoDaoExtend.selectUseFullProxy();
        log.info("-------- 可用代理 {} 个 --------", proxyInfoList.size());
        List<ProxyIP> ipList = proxyInfoList.stream()
                .map(o -> new ProxyIP(o.getIp(), o.getPort())).collect(Collectors.toList());

        //当前访问总次数
        int now = 0;
        //访问成功次数
        int count = 0;
        //状态标识 1:开始,0:结束
        int status = 1;

        for (int i = 1; ; i++) {
            log.info("--------第 {} 批代理IP访问开始--------", i);
            for (ProxyIP proxyIP : ipList) {
                if (count < number) {
                    now++;
                    log.info("现在是 {} 第 {} 次访问", url, now);
                    log.info("使用的代理为------{}:{}", proxyIP.getAddress(), proxyIP.getPort());
                    try {
                        boolean result = BuildUtil.sendProxyGet(proxyIP.getAddress(), Integer.parseInt(proxyIP.getPort()), url);
                        if (result) {
                            Thread.sleep(10000);
                            count++;
                            log.info(url + "成功访问次数: " + count);
                            log.info("代理IP：{}   端口：{}", proxyIP.getAddress(), proxyIP.getPort());
                        } else {
                            log.info("代理GET请求发送异常！");
                            log.info("代理IP：{}   端口：{}", proxyIP.getAddress(), proxyIP.getPort());
                        }
                    } catch (Exception ignored) {

                    }
                    log.info("--------本次访问结束--------");
                } else {
                    status = 0;
                    break;
                }
            }
            log.info("--------第 {} 批代理IP访问结束--------", i);
            if (status == 0) {
                break;
            }
        }

        log.info("-------- {} 访问结束，共访问了 {} 次--------", url, count);
    }
}

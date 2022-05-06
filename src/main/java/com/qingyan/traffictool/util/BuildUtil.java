package com.qingyan.traffictool.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import com.qingyan.traffictool.generate.ProxyInfo;
import com.qingyan.traffictool.task.ThreadPoolConfig;

import cn.altaria.base.http.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * BuildUtil
 *
 * @author xuzhou
 * @version v1.0.0
 * @date 2022/4/29 16:49
 */
@Slf4j
@Component
public class BuildUtil {

    @Resource
    private ThreadPoolConfig threadPoolConfig;

    public static HttpGet buildGet(String url, String host, Integer port) {
        HttpGet httpGet = new HttpGet(url);
        HttpHost httpHost = new HttpHost(host, port);
        RequestConfig config = RequestConfig.custom()
//                .setConnectTimeout(10000)
//                .setSocketTimeout(30000)
                .setProxy(httpHost)
                .build();
        httpGet.setConfig(config);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        return httpGet;
    }

    public static boolean sendProxyGet(String address, int parseInt, String url) {
        HttpGet httpGet = buildGet(url, address, parseInt);
        try (CloseableHttpResponse execute = HttpClientUtils.httpClient.execute(httpGet);) {
            if (execute != null && execute.getStatusLine().getStatusCode() == 200) {
                return true;
            }
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
//            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 批量代理IP有效检测
     *
     * @param proxyIpMap
     * @param reqUrl
     */
    public static void checkProxyIp(Map<String, Integer> proxyIpMap, String reqUrl) {

        log.info("----------------- 开始校验代理Ip是否有效 -------------------");
        for (String proxyHost : proxyIpMap.keySet()) {
            Integer proxyPort = proxyIpMap.get(proxyHost);
            log.info("----------------- 开始校验IP={}，Port={} -------------------", proxyHost, proxyPort);
            int statusCode = 0;

            HttpGet httpGet = new HttpGet(reqUrl);
            HttpHost httpHost = new HttpHost(proxyHost, proxyPort);
            RequestConfig config = RequestConfig.custom()
                    .setProxy(httpHost)
                    .build();
            httpGet.setConfig(config);
            try (CloseableHttpResponse execute = HttpClientUtils.httpClient.execute(httpGet);) {
                if (execute != null) {
                    statusCode = execute.getStatusLine().getStatusCode();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
                log.error("Error: {}", e.getMessage());
            }
            System.out.format("%s:%s-->%s\n", proxyHost, proxyPort, statusCode);
        }
    }

    /**
     * 批量代理IP有效检测
     *
     * @param proxyInfoList
     * @param reqUrl
     */
    public static void checkProxyIp(List<ProxyInfo> proxyInfoList, String reqUrl) {

        log.info("----------------- 开始校验代理Ip是否有效 -------------------");
        for (ProxyInfo proxyInfo : proxyInfoList) {
            int proxyPort = Integer.parseInt(proxyInfo.getPort());
            String proxyHost = proxyInfo.getIp();
            log.info("----------------- 开始校验IP={}，Port={} -------------------", proxyHost, proxyPort);

            int statusCode = 0;
            HttpGet httpGet = new HttpGet(reqUrl);
            HttpHost httpHost = new HttpHost(proxyHost, proxyPort);
            RequestConfig config = RequestConfig.custom()
                    .setProxy(httpHost)
                    .build();
            httpGet.setConfig(config);
            try (CloseableHttpResponse execute = HttpClientUtils.httpClient.execute(httpGet);) {
                if (execute != null) {
                    statusCode = execute.getStatusLine().getStatusCode();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
                log.error("Error: {}", e.getMessage());
            }
            if (statusCode == 200) {
                proxyInfo.setValid(1);
            }else {
                proxyInfo.setValid(0);
            }
            proxyInfo.setUpdatetime(LocalDateTime.now());
            log.info("{}:{} --> {}", proxyHost, proxyPort, statusCode);
        }
    }

    /**
     * 代理IP有效检测
     *
     * @param proxyIp
     * @param proxyPort
     * @param reqUrl
     */
    public static void checkProxyIp(String proxyIp, int proxyPort, String reqUrl) {
        Map<String, Integer> proxyIpMap = new HashMap<>();
        proxyIpMap.put(proxyIp, proxyPort);
        checkProxyIp(proxyIpMap, reqUrl);
    }

    public static void main(String[] args) {

        Map<String, Integer> proxyIpMap = new HashMap<>();
        proxyIpMap.put("152.136.62.181", 9999);
        proxyIpMap.put("27.42.168.46", 55481);
        proxyIpMap.put("183.247.215.218", 30001);

        checkProxyIp(proxyIpMap, "http://www.baidu.com");

    }


}

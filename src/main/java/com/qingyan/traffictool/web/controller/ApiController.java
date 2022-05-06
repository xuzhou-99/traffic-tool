package com.qingyan.traffictool.web.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qingyan.traffictool.cache.ProxyIpCache;
import com.qingyan.traffictool.entity.ProxyIP;
import com.qingyan.traffictool.generate.ProxyInfo;
import com.qingyan.traffictool.generate.ProxyInfoDaoExtend;
import com.qingyan.traffictool.service.CheckService;
import com.qingyan.traffictool.util.BuildUtil;

import cn.altaria.base.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * ApiController
 *
 * @author xuzhou
 * @version v1.0.0
 * @date 2022/4/29 16:30
 */
@Slf4j
@Controller
public class ApiController {

    @Resource
    private ProxyInfoDaoExtend proxyInfoDaoExtend;

    @Resource
    private CheckService checkService;


    @RequestMapping(value = "/find")
    @ResponseBody
    public ApiResponse findProxyIps() {
        checkService.findProxyIps();
        return ApiResponse.ofSuccess();
    }


    @RequestMapping(value = "/check")
    @ResponseBody
    public ApiResponse check() {
        checkService.check();
        return ApiResponse.ofSuccess();
    }

    @RequestMapping(value = "/start")
    @ResponseBody
    public void addTraffic(@RequestParam(value = "url") String url,
                           @RequestParam(value = "num") int num) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            //
        }
        checkService.addTraffic(url, num);
    }

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(@RequestParam(value = "url") String url, @RequestParam(value = "host") String host, @RequestParam(value = "port") Integer port) {
        HttpGet httpGet = BuildUtil
                .buildGet(url, host, port);
        String result = "fail";
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse execute = httpClient.execute(httpGet);) {

            if (execute.getStatusLine().getStatusCode() == 200) {
                result = "success";
            }
            log.info(result);
        } catch (IOException e) {
            log.info(result);
            log.info("error: ", e);
        }
        return result;
    }

    @RequestMapping(value = "/testInsert")
    @ResponseBody
    public ApiResponse testInsert() {
        List<ProxyIP> proxyIpList = new ArrayList<>();
        ProxyIP proxyIP = new ProxyIP();
        proxyIP.setAddress(UUID.randomUUID().toString());
        proxyIP.setPort("8080");
        proxyIpList.add(proxyIP);

        ProxyIP proxyIP2 = new ProxyIP();
        proxyIP2.setAddress(UUID.randomUUID().toString());
        proxyIP2.setPort("8080");
        proxyIpList.add(proxyIP2);

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
        return ApiResponse.ofSuccess();
    }

    @RequestMapping(value = "/testUpdate")
    @ResponseBody
    public ApiResponse testUpdate() {


        List<ProxyInfo> proxyInfoList = new ArrayList<>();
        ProxyInfo proxyIP = new ProxyInfo();
        proxyIP.setBh(UUID.randomUUID().toString());
        proxyIP.setIp(proxyIP.getIp());
        proxyIP.setPort("8080");
        proxyInfoList.add(proxyIP);

        ProxyInfo proxyIP2 = new ProxyInfo();
        proxyIP2.setBh(UUID.randomUUID().toString());
        proxyIP2.setIp(proxyIP.getIp());
        proxyIP2.setPort("8080");
        proxyInfoList.add(proxyIP2);

        proxyInfoDaoExtend.batchUpdate(proxyInfoList);

        return ApiResponse.ofSuccess();
    }
}

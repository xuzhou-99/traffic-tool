package com.qingyan.traffictool.web.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qingyan.traffictool.service.CheckService;
import com.qingyan.traffictool.util.BuildUtil;

import cn.altaria.base.response.ApiDataResponse;
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
    private CheckService checkService;

    /**
     * 获取代理
     *
     * @return 操作结果
     */
    @RequestMapping(value = "/find")
    @ResponseBody
    public ApiResponse findProxyIps() {
        checkService.findProxyIps();
        return ApiResponse.ofSuccess();
    }

    /**
     * 检查代理有效性
     *
     * @return 操作结果
     */
    @RequestMapping(value = "/check")
    @ResponseBody
    public ApiResponse check() {
        checkService.check();
        return ApiResponse.ofSuccess();
    }

    /**
     * 检查代理有效性
     *
     * @return 操作结果
     */
    @GetMapping(value = "/proxyList")
    @ResponseBody
    public ApiResponse proxyList() {
        return ApiDataResponse.ofSuccess(checkService.proxyList());
    }

    /**
     * 开始新增浏览量
     *
     * @param url 请求地址
     * @param num 浏览次数
     */
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

    /**
     * 测试代理访问
     *
     * @param url  请求地址
     * @param host 代理ip
     * @param port 代理端口
     * @return 访问结果
     */
    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(@RequestParam(value = "url") String url,
                       @RequestParam(value = "host") String host,
                       @RequestParam(value = "port") Integer port) {
        HttpGet httpGet = BuildUtil
                .buildGet(url, host, port);
        String result = "fail";
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse execute = httpClient.execute(httpGet)) {
            if (execute.getStatusLine().getStatusCode() == 200) {
                result = "success";
            }
        } catch (IOException e) {
            log.error("error: ", e);
        }
        log.info("本次访问结果：{}", result);
        return result;
    }
}

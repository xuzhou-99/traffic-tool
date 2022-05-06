package com.qingyan.traffictool.task;

import java.util.List;

import com.qingyan.traffictool.generate.ProxyInfo;
import com.qingyan.traffictool.util.BuildUtil;

/**
 * CheckProxyRunnable
 *
 * @author xuzhou
 * @version v1.0.0
 * @date 2022/5/5 17:25
 */
public class CheckProxyRunnable implements Runnable {

    private final List<ProxyInfo> proxyInfoList;

    private final String requestUrl;

    private ProxyRequestTask proxyRequestTask;

    public CheckProxyRunnable(String requestUrl, List<ProxyInfo> proxyInfoList, ProxyRequestTask proxyRequestTask) {
        this.requestUrl = requestUrl;
        this.proxyInfoList = proxyInfoList;
        this.proxyRequestTask = proxyRequestTask;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        proxyRequestTask.checkProxyIpOnce(proxyInfoList, requestUrl);
    }
}

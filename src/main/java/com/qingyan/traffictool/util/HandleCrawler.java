package com.qingyan.traffictool.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;


import com.qingyan.traffictool.common.Constant;

import cn.altaria.base.http.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandleCrawler {

    private static Pattern pattern1 = Pattern.compile("(?<=__jsluid_h=).+?(?=; max-age=)");

    private static Pattern pattern2 = Pattern.compile("(?<=window.onload=setTimeout\\(\").+?(?=\", 200\\))");

    private static Pattern pattern3 = Pattern.compile("(?<=__jsl_clearance=).+?(?=;Expires=)");

    public static void setCookie() {
        try {
            CloseableHttpResponse response = HttpClientUtils.httpClient.execute(new HttpGet(Constant.proxyUrl));
            if (response.getStatusLine().getStatusCode() == 521) {
                String jsluidCookie = getJsluidCookie(response.getAllHeaders());
                log.info("jsluidCookie is :" + jsluidCookie);

                HttpEntity entity = response.getEntity();
                String html = EntityUtils.toString(entity, "utf-8");
                String functionFirst = handleFirst(html);
                functionFirst = functionFirst.substring(0, functionFirst.indexOf("qaq();")) + "qaq();";
                log.info("functionFirst is :" + functionFirst);

                //获取JavaScript执行引擎
                ScriptEngineManager m = new ScriptEngineManager();
                //执行JavaScript代码
                ScriptEngine engine = m.getEngineByName("JavaScript");
                String origin = (String) engine.eval(functionFirst);

                String secondName = origin.substring(4, origin.indexOf("="));
                String functionSecond = handleDocument(handleSecond(origin, secondName));
                log.info("functionSecond is :" + functionSecond);
                if (!functionSecond.contains("window")) {
                    String real = (String) engine.eval(functionSecond);
                    String jslclearance = getJslclearance(real);
                    log.info("jslclearance is :" + jslclearance);

                    Constant.COOKIE = "__jsluid_h=" + jsluidCookie + "; __jsl_clearance=" + jslclearance;
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            log.error("获取Cookie失败", e);
        }
    }

    private static String getJsluidCookie(Header[] headers) {
        String jsluidCookie = null;
        for (Header header : headers) {
            if ("Set-Cookie".equals(header.getName())) {
                jsluidCookie = header.getValue();
                break;
            }
        }

        Matcher matcher = pattern1.matcher(jsluidCookie);
        while (matcher.find()) {
            jsluidCookie = matcher.group(0);
        }
        return jsluidCookie;
    }

    private static String getRunString(String html) {

        Matcher matcher = pattern2.matcher(html);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    private static String handleFirst(String html) {
        String handleFirst = html.replace("eval(y.replace(/\\b\\w+\\b/g, function(y){return x[f(y,z)-1]||(\"_\"+y)}));break}catch(_){}", "function aa(){return y.replace(/\\b\\w+\\b/g, function(y){return x[f(y,z)-1]||(\"_\"+y)});}break}catch(_){}return aa();}qaq();");
        String function = handleFirst.replace("<script>", "").replace("</script>", "");
        return "function qaq(){" + function;
    }

    private static String handleSecond(String html, String name) {
        return html.replace("setTimeout('location.href=location.pathname+location.search.replace(/[\\?|&]captcha-challenge/,\\'\\')',1500);document.cookie=", "return ")
                .replace(";if((function(){try{return !!window.addEventListener;}catch(e){return false;}})()){document.addEventListener('DOMContentLoaded'," + name + ",false)}else{document.attachEvent('onreadystatechange'," + name + ")}", ";" + name + "();")
                .replace("(window.headless+[]+[[]][0]).charAt(8)", "'d'")
                .replace("window['__p'+'hantom'+'as']", "'f'")
                .replace("(window['callP'+'hantom']+[]+[[]][0]).charAt(-~[-~[-~~~!{}+((+!-{})<<(+!-{}))+((+!-{})<<(+!-{}))]])", "'e'")
                .replace("(window['callP'+'hantom']+[]).charAt(~~[])", "'u'")
                .replace(name + ".firstChild.href", "\"https:///\"")
                .replace(name + ".match(/https?:\\/\\//)[0]", "\"https://\"");
    }

    private static String handleDocument(String html) {
        if (html.contains("document.createElement") && html.contains("firstChild.href")) {
            return html.replace(html.substring(html.indexOf("document.createElement"), html.indexOf("firstChild.href") + 15), "\"https:///\"");
        } else if (html.contains("document.createElement")) {
            return html.replace(html.substring(html.indexOf("document.createElement"), html.indexOf("\"https:///\"")), "");
        } else {
            return html;
        }
    }

    private static String getJslclearance(String real) {
        String jslclearance = null;

        Matcher matcher = pattern3.matcher(real);
        while (matcher.find()) {
            jslclearance = matcher.group(0);
        }
        return jslclearance;
    }
}

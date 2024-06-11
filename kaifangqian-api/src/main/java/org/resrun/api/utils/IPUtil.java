package org.resrun.api.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author : zhenghuihan
 * create at:  2022/5/30  17:32
 * @description: ip工具类
 */

public class IPUtil {

    private static Logger log = LoggerFactory.getLogger(IPUtil.class);

    private static final String UNKNOWN = "unknown";
    private static boolean ipLocal = false;
    private static Searcher searcher;

    /**
     * 常用接口
     */
    public static class Url {
        // IP归属地查询
        public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    }

    static {
        SpringContextHolder.addCallBacks(() -> {
            ipLocal = SpringContextHolder.getProperties("ip.local-parsing", false, Boolean.class);
            if (ipLocal) {
                String dbPath = "/ip2region/ip2region.xdb";
                // 1、从 dbPath 加载整个 xdb 到内存。
                byte[] cBuff;
                try {
                    ClassPathResource classPathResource = new ClassPathResource(dbPath);
                    cBuff = IOUtils.stream2Byte(classPathResource.getInputStream());
                } catch (Exception e) {
                    System.out.printf("failed to load content from `%s`: %s\n", dbPath, e);
                    return;
                }

                // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
                try {
                    searcher = Searcher.newWithBuffer(cBuff);
                } catch (Exception e) {
                    System.out.printf("failed to create content cached searcher: %s\n", e);
                    return;
                }
            }
        });
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getCityInfo(String ip) {
        if (ipLocal) {
            return getLocalCityInfo(ip);
        } else {
            return getHttpCityInfo(ip);
        }
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getLocalCityInfo(String ip) {
        try {
            log.info("解析ip地址开始");
            if (searcher != null) {
                String adress = searcher.search(ip);
                if (MyStringUtils.isNotBlank(adress) && adress.contains("|")) {
                    return adress.substring(0, adress.lastIndexOf("|"));
                }
                return adress;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析ip地址报错了");
        }
        return null;
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getHttpCityInfo(String ip) {
        try {
            String api = String.format(Url.IP_URL, ip);
            JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
            return object.get("addr", String.class);
        } catch (Exception e) {
            log.error("IP在线解析失败！");
            return null;
        }
    }

    /**
     * 获取ip地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
        }
        return ip;
    }

    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }

        return ip;
    }
}
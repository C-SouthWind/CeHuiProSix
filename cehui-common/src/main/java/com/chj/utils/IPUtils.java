package com.chj.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：chj
 * @date ：Created in 2020/5/27 17:30
 * @params :
 */
public class IPUtils {

    private IPUtils(){}

    private static final String UNKNOWN = "unknown";

    public static String getIpAddr(HttpServletRequest request){
        //x-forwarded-for就是ip地址
        String ip = request.getHeader("x-forwarded-for");
        //判断是否使用代理
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            //本地ip
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            //网络ip
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            //内网ip
             ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}

























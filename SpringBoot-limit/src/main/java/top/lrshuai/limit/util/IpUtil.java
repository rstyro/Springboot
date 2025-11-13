package top.lrshuai.limit.util;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {

    /**
     * 获取客户端真实IP地址
     * 优先级: X-Forwarded-For -> X-Real-IP -> Proxy-Client-IP -> WL-Proxy-Client-IP -> RemoteAddr
     * @param request HttpServletRequest对象
     * @return 客户端的真实IP地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = null;

        // 1. 优先检查X-Forwarded-For头部
        ip = getIpFromHeader(request, "X-Forwarded-For");
        if (isValidIp(ip)) {
            // 取第一个非unknown的有效IP
            String[] ips = ip.split(",");
            for (String i : ips) {
                i = i.trim();
                if (isValidIp(i) && !"unknown".equalsIgnoreCase(i)) {
                    return i; // 返回第一个有效的客户端IP
                }
            }
        }

        // 2. 检查其他头部，按优先级排序
        String[] headers = {"X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
        for (String header : headers) {
            ip = getIpFromHeader(request, header);
            if (isValidIp(ip)) {
                return ip;
            }
        }

        // 3. 最后使用远程地址
        ip = request.getRemoteAddr();
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip; // 处理本地IPv6回环地址
    }

    /**
     * 从请求头中获取IP值
     */
    public static String getIpFromHeader(HttpServletRequest request, String headerName) {
        String ip = request.getHeader(headerName);
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            return null;
        }
        return ip.trim();
    }

    /**
     * 基础IP地址有效性验证
     */
    private static boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        // 基础格式验证，可根据需要增强（例如使用正则表达式或InetAddress验证）
        return ip.chars().allMatch(ch -> ch == '.' || Character.isDigit(ch) || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F') || ch == ':');
    }
}

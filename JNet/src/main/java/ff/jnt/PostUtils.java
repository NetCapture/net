package ff.jnt;

import ff.jnt.utils.HttpType;
import ff.jnt.utils.SSLConfig;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Copyright © 2020 analysys Inc. All rights reserved.
 * @Description: http post请求
 * @Version: 1.0
 * @Create: 2020-12-08 15:19:41
 * @author: sanbo
 */
public class PostUtils {

    public static void main(String[] args) {


        Map<String, String> reqHeaderMap = new HashMap<String, String>();
        reqHeaderMap.put("Content-Type", "application/json;charset=UTF-8");
        reqHeaderMap.put("User-Agent", "Gitee file Upload from Java");
        String uploadUrl = "https://gitee.com/api/v5/repos/safei/ceshi/contents/xx/aaa.txt";
        String data = "{\"access_token\":\"6fc360a0bb92c092e0ba02c460ecc699\",\"content\":\"55yf55qE5bCx5piv5rWL6K+V5LiA5LiL\",\"message\":\"test\"}";
        Proxy proxy = null;
        int timeout = 10 * 1000;
        post(HttpType.POST, timeout, uploadUrl, proxy, reqHeaderMap, data);

    }

    /**
     * post request:
     * 1. a).getConnection b).parser args and add RequestProperty 3).connect
     * 2. a).post data b).listen the code,
     * 3. process failed case or success case(parser the response)
     *
     * @param method
     * @param timeout
     * @param uploadUrl
     * @param proxy
     * @param reqHeaderMap
     * @param data
     */
    private static void post(String method, int timeout, String uploadUrl, Proxy proxy, Map<String, String> reqHeaderMap, String data) {
        // 1. getConnection
        getConnection(method, timeout, uploadUrl, proxy, reqHeaderMap);
    }

    private static HttpURLConnection getConnection(String method, int timeout, String urlString, Proxy proxy, Map<String, String> reqHeaderMap) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);

            if (urlString.startsWith("https")) {
                if (proxy != null) {
                    conn = (HttpsURLConnection) url.openConnection(proxy);
                } else {
                    conn = (HttpsURLConnection) url.openConnection();
                }
                ((HttpsURLConnection) conn).setHostnameVerifier(SSLConfig.NOT_VERYFY);
                ((HttpsURLConnection) conn).setSSLSocketFactory(SSLConfig.getSSLFactory());
            } else {
                if (proxy != null) {
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    conn = (HttpURLConnection) url.openConnection();
                }
            }

            conn.setRequestMethod(method);
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            if (reqHeaderMap != null) {
                Iterator<Map.Entry<String, String>> iterator = reqHeaderMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    conn.addRequestProperty(entry.getKey(), entry.getValue());
                }

            }


        } catch (Throwable e) {
            e.printStackTrace();
        }
        return conn;
    }

}

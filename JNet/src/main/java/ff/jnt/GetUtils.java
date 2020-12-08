package ff.jnt;


import ff.jnt.utils.Closer;
import ff.jnt.utils.HttpType;
import ff.jnt.utils.SSLConfig;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;

/**
 * @Copyright © 2020 analysys Inc. All rights reserved.
 * @Description: get请求
 * @Version: 1.0
 * @Create: Jul 30, 2020 2:31:10 PM
 * @author: sanbo
 */
public class GetUtils {


    static {
        // V2rayU
        p = new Proxy(Type.SOCKS, new InetSocketAddress("127.0.0.1", 1080));
        // Q2ray
//        p = new Proxy(Type.SOCKS, new InetSocketAddress("127.0.0.1", 1089));
    }

    public static void main(String[] args) {
        // String s =
        // "https://www.lt20664.xyz:20000/api/evmess?&proto=v2&platform=android&ver=1.1.5&deviceid=00421d92d077f60dunknown&unicode=00421d92d077f60dunknown&t=1595992068435&code=M9GXQZ&recomm_code=&f=2020-07-29&install=2020-07-29&token=&xf_fans=&area=3";
        // System.out.println(get(s));

//         String result = get("http://pv.sohu.com/cityjson?ie=utf-8");
//         System.out.println(result);
        String result = get("https://www.google.com");
        System.out.println(result);
    }


    public static String get(String urlString) {
        return get(urlString, true, null);
    }

    public static String get(String urlString, Proxy proxy) {
        return get(urlString, false, proxy);
    }

    public static String get(String urlString, boolean isUseProxy) {
        return get(urlString, isUseProxy, null);
    }

    private static String get(String urlString, boolean isUseProxy, Proxy proxy) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);

            if (urlString.startsWith("https")) {
                if ((isUseProxy && p != null)) {
                    conn = (HttpsURLConnection) url.openConnection(p);
                }
                if (conn == null && proxy != null) {
                    conn = (HttpsURLConnection) url.openConnection(proxy);
                }
                if (conn == null) {
                    conn = (HttpsURLConnection) url.openConnection();
                }
                // 2;url.openconnection
                ((HttpsURLConnection) conn).setHostnameVerifier(SSLConfig.NOT_VERYFY);
                ((HttpsURLConnection) conn).setSSLSocketFactory(SSLConfig.getSSLFactory());
                // 3
                conn.setRequestMethod(HttpType.GET);
                conn.setConnectTimeout(10 * 1000);
            } else {

                if ((isUseProxy && p != null)) {
                    conn = (HttpURLConnection) url.openConnection(p);
                }
                if (conn == null && proxy != null) {
                    conn = (HttpURLConnection) url.openConnection(proxy);
                }
                if (conn == null) {
                    conn = (HttpURLConnection) url.openConnection();
                }
                // 2
                conn.setRequestMethod(HttpType.GET);
                conn.setConnectTimeout(10 * 1000);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return realGet(urlString, conn);
    }

    private static String realGet(String urlString, HttpURLConnection conn) {
        String result = "";
        BufferedReader br = null;
        InputStream inputStream = null;
        InputStreamReader isr = null;
        try {
            if (isNetWorkDebug) {
                System.out.println("request url:\t" + urlString);
            }
            // 4
            int code = conn.getResponseCode();
            if (isNetWorkDebug) {
                System.out.println("request url:\t" + urlString + "\nresponse code:\t" + code);
            }

            if (code == 200) {
                inputStream = conn.getInputStream();
                StringBuilder sb = new StringBuilder();
                String line;
                isr = new InputStreamReader(inputStream);
                br = new BufferedReader(isr);
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();

            }
        } catch (Throwable e) {
            if (isNetWorkDebug) {
                e.printStackTrace();
            }
        } finally {
            Closer.close(br, inputStream, isr);
        }
        return result;
    }


    private static final boolean isNetWorkDebug = true;
    private static Proxy p = null;
}

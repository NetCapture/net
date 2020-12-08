package ff.jnt;

import ff.jnt.utils.HttpType;
import ff.jnt.utils.SSLConfig;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * @Copyright © 2020 analysys Inc. All rights reserved.
 * @Description: http post请求
 * @Version: 1.0
 * @Create: 2020-12-08 15:19:41
 * @author: sanbo
 */
public class PostTest {

    public static void main(String[] args) {

        try {
            String ContentType = "application/json;charset=UTF-8";
            String ul = "https://gitee.com/api/v5/repos/safei/ceshi/contents/xx/aaa.txt";
//            String ul = "https://gitee.com/api/v5/repos/safei/ceshi/contents/%2Fxx%2Faaa.txt";
            String data = "{\"access_token\":\"6fc360a0bb92c092e0ba02c460ecc699\",\"content\":\"55yf55qE5bCx5piv5rWL6K+V5LiA5LiL\",\"message\":\"test\"}";

            URL url = new URL(ul);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", ContentType);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(true);
            connection.connect();
//            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
////            dos.writeBytes(data);
//            dos.write(data.getBytes(StandardCharsets.UTF_8));
//            dos.flush();
//            dos.close();


            PrintWriter pw = new PrintWriter(connection.getOutputStream());
            pw.print(data);
            pw.flush();
            pw.close();
            System.out.println(connection.getResponseCode());

            if (connection.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } else {
                System.out.println(connection.getResponseMessage());
            }


        } catch (Throwable e) {
            e.printStackTrace();
        }


    }

    /**
     * post请求
     *
     * @param urlString    请求地址
     * @param chart        编码
     * @param proxy        proxy
     * @param reqHeaderMap 请求头的键值对
     * @param postData     请求数据
     */
    private static void post(String urlString, String chart, Proxy proxy, Map<String, String> reqHeaderMap, String postData) {
        HttpsURLConnection conn = null;
        DataOutputStream dataOutputStream = null;
        try {
            URL url = new URL(urlString);
            if (proxy != null) {
                conn = (HttpsURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpsURLConnection) url.openConnection();
            }
            conn.setHostnameVerifier(SSLConfig.NOT_VERYFY);
            conn.setSSLSocketFactory(SSLConfig.getSSLFactory());
            conn.setRequestMethod(HttpType.PUT);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            if (reqHeaderMap != null) {
                Iterator<Map.Entry<String, String>> iterator = reqHeaderMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    // System.out.println("iteratorEntry : key :" + entry.getKey() + "---> value :"+entry.getValue());
                    conn.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            dataOutputStream = new DataOutputStream(conn.getOutputStream());
//            dataOutputStream.write(Base64.getEncoder().encode(postData.getBytes(chart)));
            String s = "{\"access_token\":\"6fc360a0bb92c092e0ba02c460ecc699\",\"content\":\"5rWL6K+V55qE5YaF5a65\",\"message\":\"测试\"}";
            dataOutputStream.writeBytes(s);
            dataOutputStream.flush();
            dataOutputStream.close();
            //对outputStream的写操作，又必须要在inputStream的读操作之前
            InputStream inputStream = conn.getInputStream();// <===注意，实际发送请求的代码段就在这里

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, chart));
            String lines;
            StringBuffer response = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                response.append(lines);
                response.append("\r\n");
            }
            System.out.println(response.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

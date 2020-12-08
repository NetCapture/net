package ff.jnt.juewang;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Put {


    public static void main(String[] args) {
        String postData = "{\"access_token\":\"6fc360a0bb92c092e0ba02c460ecc699\",\"content\":\"5rWL6K+V55qE5YaF5a65\",\"message\":\"测试\"}";
        String url = "https://gitee.com/api/v5/repos/safei/ceshi/contents/img/aaa.txt";
        String token = "";
        getUploadInformation(url, postData, token);
    }
    /*
     *  httpUrl 服务器地址
     *  jsonParam 请求参数JSON
     *  authorization Token 如果没有token可以不传
     */

    public static String getUploadInformation(String httpUrl, String jsonParam, String authorization) {
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL url = new URL("https://gitee.com/api/v5/repos/safei/ceshi/contents/img%2Faaa.txt");
            System.out.println(url);
            if (url != null) {
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 请求格式
                    conn.setConnectTimeout(120000);
                    conn.setReadTimeout(120000);
                    // 设置
                    conn.setDoOutput(true); // 需要输出
                    conn.setDoInput(true); // 需要输入
                    conn.setUseCaches(false); // 不允许缓存

                    conn.setRequestMethod("POST"); // 设置PUT方式连接

                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.connect();

                    // 传输数据
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                    String body = "{\"access_token\":\"6fc360a0bb92c092e0ba02c460ecc699\",\"content\":\"55yf55qE5bCx5piv5rWL6K+V5LiA5LiL\",\"message\":\"测试\"}";
                    dos.writeBytes(body);

                    dos.flush();

                    if (conn.getResponseCode() == 200) {

                        // 定义BufferedReader输入流来读取URL的响应

                        in = new BufferedReader(new InputStreamReader(
                                conn.getInputStream()));

                        String line;

                        while ((line = in.readLine()) != null) {
                            result += line;
                        }
                    } else {
                        System.out.println("请求失败----Code:" + conn.getResponseCode()
                                + "; Message:" + conn.getResponseMessage()
                        );
                    }
                    conn.disconnect();// 断开连接
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
        }
        return result;
    }
}

package ff.jnt.github;

import com.sun.xml.internal.messaging.saaj.util.Base64;
import ff.jnt.utils.F;
import ff.jnt.utils.HttpType;
import ff.jnt.utils.SSLConfig;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

/**
 * @Copyright © 2020 analysys Inc. All rights reserved.
 * @Description: http post请求
 * @Version: 1.0
 * @Create: 2020-12-08 15:19:41
 * @author: sanbo
 */
public class UploadToGitee {

    public static void main(String[] args) {

        try {
            String ContentType = "application/json;charset=UTF-8";
            String ul = "https://gitee.com/api/v5/repos/safei/ceshi/contents/xx/q.txt";
            String data = "{\"access_token\":\"6fc360a0bb92c092e0ba02c460ecc699\",\"content\":\"%s\",\"message\":\"test\"}";
            String result = new BASE64Encoder().encode(F.readContent("a.txt").getBytes(StandardCharsets.UTF_8));
            // 据RFC 822规定，每76个字符，还需要加上一个回车换行
            // 有时就因为这些换行弄得出了问题，解决办法如下，替换所有换行和回车
            // result = result.replaceAll("[\\s*\t\n\r]", "");
            result = result.replaceAll("[\\s*]", "");
            String finalData = String.format(data, result);


            URL url = new URL(ul);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", ContentType);
            connection.setRequestProperty("User-Agent", "Gitee File Uploader App");

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
            pw.print(finalData);
            pw.flush();
            pw.close();
            System.out.println(connection.getResponseCode());

            if (connection.getResponseCode() == 200) {
                System.out.println(connection.getResponseMessage());
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

}

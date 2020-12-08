package ff.jnt.juewang;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Copyright © 2020 analysys Inc. All rights reserved.
 * @Description: 上传github文件
 * @Version: 1.0
 * @Create: 2020-12-07 11:15:13
 * @author: Administrator
 */
public class UploadToGitee {


    public static void main(String args[]) {

        try {
            File f = new File("pom.xml");
//        System.out.println(f.getAbsolutePath() + "----->" + f.exists());

            String url = "https://gitee.com/api/v5/repos/safei/ceshi/contents/img/pic.jpg";
            String token = "6fc360a0bb92c092e0ba02c460ecc699";
            boolean rs = create(url, f, token);
//        boolean rs = update(url, f, token);
            System.out.println("rs:" + rs);

            //		String url = args[0];
//		File file = new File(args[1]);
//		String token = args[2];
//		create(url, file, token);
//		//update(url, file, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 由于没有考虑sha，故而只能新建文件，而不能更新文件(更新文件需要先get访问得到sha，然后再put)
     *
     * @param url   https://api.github.com/repos/:owner/:repo/contents/:path
     * @param file  需确保文件存在
     * @param token 用于鉴权
     * @return
     */
    public static boolean create(String url, File file, String token) {
        long begin = System.currentTimeMillis();
        System.out.println("上传开始...");
        //StringBuffer result = new StringBuffer();
        BufferedReader in = null;
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(120000);
            conn.setReadTimeout(120000);
            // 设置
            conn.setDoOutput(true); // 需要输出
            conn.setDoInput(true); // 需要输入
            conn.setUseCaches(false); // 不允许缓存
            conn.setRequestMethod("PUT"); // 设置PUT方式连接

            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//            conn.setRequestProperty("Authorization", "token " + token);
            conn.setRequestProperty("User-Agent", "Github File Uploader App");
            conn.connect();
            // 传输数据
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // 传输json头部
            dos.writeBytes("{\"access_token\":\""+token+"\",\"message\":\"Create By Java\",\"content\":\"");
//            // 传输文件内容
//            byte[] buffer = new byte[1024 * 1002]; // 3的倍数
//            RandomAccessFile raf = new RandomAccessFile(file, "r");
//            long size = raf.read(buffer);
//            while (size > -1) {
//                if (size == buffer.length) {
//                    dos.write(Base64.getEncoder().encode(buffer));
//                } else {
//                    byte tmp[] = new byte[(int) size];
//                    System.arraycopy(buffer, 0, tmp, 0, (int) size);
//                    dos.write(Base64.getEncoder().encode(tmp));
//                }
//                size = raf.read(buffer);
//            }
//            raf.close();
            dos.writeBytes("5rWL6K+V55qE5YaF5a65");
            // 传输json尾部
            dos.writeBytes("\"}");
            dos.flush();
            dos.close();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                //result.append(line).append("\n");
            }
        } catch (Exception e) {
            System.out.println("发送PUT请求出现异常！");
            e.printStackTrace();
            return false;
        } finally {
            try {
                in.close();
            } catch (Exception e2) {
            }
        }
        long end = System.currentTimeMillis();
        System.out.printf("上传结束，耗时 %ds\n", (end - begin) / 1000);
        //result.toString()
        return true;
    }

    /**
     * 更新文件(先get访问得到sha，然后再put)
     *
     * @param url   https://api.github.com/repos/:owner/:repo/contents/:path
     * @param file  需确保文件存在
     * @param token 用于鉴权
     * @return
     */
    public static boolean update(String url, File file, String token) {
        long begin = System.currentTimeMillis();
        System.out.println("获取文件SHA...");
        String sha = getSHA(url);
        if (sha == null)
            return false;
        long end = System.currentTimeMillis();
        System.out.printf("获取文件SHA 耗时 %ds\n", (end - begin) / 1000);
        System.out.println("上传开始...");
        //StringBuffer result = new StringBuffer();
        BufferedReader in = null;
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(120000);
            conn.setReadTimeout(120000);
            // 设置
            conn.setDoOutput(true); // 需要输出
            conn.setDoInput(true); // 需要输入
            conn.setUseCaches(false); // 不允许缓存
            conn.setRequestMethod("PUT"); // 设置PUT方式连接

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "token " + token);
            conn.setRequestProperty("User-Agent", "Github File Uploader App");
            conn.connect();
            // 传输数据
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // 传输json头部
            dos.writeBytes("{\"message\":\"update By Java\",\"sha\":\"" + sha + "\",\"content\":\"");
            // 传输文件内容
            byte[] buffer = new byte[1024 * 1002]; // 3的倍数
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            long size = raf.read(buffer);
            while (size > -1) {
                if (size == buffer.length) {
                    dos.write(Base64.getEncoder().encode(buffer));
                } else {
                    byte tmp[] = new byte[(int) size];
                    System.arraycopy(buffer, 0, tmp, 0, (int) size);
                    dos.write(Base64.getEncoder().encode(tmp));
                }
                size = raf.read(buffer);
            }
            raf.close();
            // 传输json尾部
            dos.writeBytes("\"}");
            dos.flush();
            dos.close();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                //result.append(line).append("\n");
            }
        } catch (Exception e) {
            System.out.println("发送PUT请求出现异常！");
            e.printStackTrace();
            return false;
        } finally {
            try {
                in.close();
            } catch (Exception e2) {
            }
        }
        end = System.currentTimeMillis();
        System.out.printf("上传结束，耗时 %ds\n", (end - begin) / 1000);
        //result.toString()
        return true;
    }

    /**
     * 获取url 对应的SHA
     *
     * @param url
     * @param token
     * @return
     */
    static Pattern pattern = Pattern.compile("\"sha\": *\"([^\"]+)\"");

    public static String getSHA(String url) {
        StringBuffer result = new StringBuffer();
        BufferedReader in = null;
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(120000);
            conn.setReadTimeout(120000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Github File Uploader App");
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line).append("\n");
            }
            Matcher matcher = pattern.matcher(result.toString());
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            System.out.println("请求SHA出现异常！");
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e2) {
            }
        }
        return null;
    }

}

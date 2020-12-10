package ff.jnt.cs;

import ff.jnt.NetUtils;
import ff.jnt.utils.HttpType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Copyright © 2020 analysys Inc. All rights reserved.
 * @Description: github接口： https://developer.github.com/v3/repos/contents/
 * @Version: 1.0
 * @Create: 2020-12-09 15:13:01
 * @author: sanbo
 */
public class GithubHelper {

    String token="bf9da1f3ff898fbeab57730517d29ed0c4c48cbb";

//    public static void main(String[] args) {
//        String res = createFile("hhhaiai", "Picture", "/test/7.txt",token, "真的就是测试一下", "Upload from Java");
//        System.out.println("链接: " + res);
////        String sha = getSha("hhhaiai", "Picture", "/test/3.txt",token);
////        System.out.println("SHA:" + sha);
//
////        updateContent("hhhaiai", "Picture", "/test/3.txt",token, "#1 what‘s that? 土鳖", "Upload from Java");
////        deleteFile("hhhaiai", "Picture", "/test/1.txt",token, "delete s", "who", "lary@gg.com");
////        deleteFile("hhhaiai", "Picture", "/test/2.txt",token, "delete s", "who", "lary@gg.com");
////        deleteFile("hhhaiai", "Picture", "/test/3.txt",token, "delete s", "who", "lary@gg.com");
////        deleteFile("hhhaiai", "Picture", "/test/6.txt",token, "delete s", "who", "lary@gg.com");
//    }

    private static void updateContent(String owner, String repo, String path, String token, String contentWillBase64, String commitMsg) {
        updateContent(owner, repo, path, token, contentWillBase64, commitMsg, "", "");
    }

    private static String updateContent(String owner, String repo, String path, String token, String contentWillBase64, String commitMsg, String username, String email) {
        String content = Base64.getEncoder().encodeToString(contentWillBase64.getBytes(StandardCharsets.UTF_8));
        // 据RFC 822规定，每76个字符，还需要加上一个回车换行
        // 有时就因为这些换行弄得出了问题，解决办法如下，替换所有换行和回车
        // result = result.replaceAll("[\\s*\t\n\r]", "");
        content = content.replaceAll("[\\s*]", "");
        Map<String, String> reqHeaderMap = new HashMap<String, String>();
        reqHeaderMap.put("Content-Type", "application/json;charset=UTF-8");
        reqHeaderMap.put("Authorization", "token " + token);
        reqHeaderMap.put("User-Agent", "Github File Uploader App");
        String base = "https://api.github.com/repos/%s/%s/contents%s";
        String uploadUrl = String.format(base, owner, repo, path);
//        System.out.println(uploadUrl);
        String sha = getSha(owner, repo, path, token);

        String hasUserInfoBase = "{\"content\":\"%s\",\"message\":\"%s\" ,\"sha\":\"%s\" ,\"committer\":{ \"name\":\"%s\",\"email\":\"%s\" }}";
        String hasNoUserInfoBase = "{\"content\":\"%s\",\"message\":\"%s\", \"sha\":\"%s\" }";
        String data = String.format(hasNoUserInfoBase, content, commitMsg, sha);
        if (!NetUtils.isEmpty(username) && !NetUtils.isEmpty(email)) {
            data = String.format(hasUserInfoBase, content, commitMsg, sha, username, email);
        }
        System.out.println(data);
        int timeout = 10 * 1000;
        return NetUtils.request(HttpType.PUT, timeout, uploadUrl, null, reqHeaderMap, data);
    }


    private static String deleteFile(String owner, String repo, String path, String token, String commitMsg) {
        return deleteFile(owner, repo, path, token, commitMsg, "", "");
    }

    private static String deleteFile(String owner, String repo, String path, String token, String commitMsg, String username, String email) {

        Map<String, String> reqHeaderMap = new HashMap<String, String>();
        reqHeaderMap.put("Content-Type", "application/json;charset=UTF-8");
        reqHeaderMap.put("Authorization", "token " + token);
        reqHeaderMap.put("User-Agent", "Github File Delete App");
        String base = "https://api.github.com/repos/%s/%s/contents%s";
        String uploadUrl = String.format(base, owner, repo, path);
//        System.out.println(uploadUrl);
        String sha = getSha(owner, repo, path, token);

        String hasUserInfoBase = "{\"message\":\"%s\" ,\"sha\":\"%s\" ,\"committer\":{ \"name\":\"%s\",\"email\":\"%s\" }}";
        String hasNoUserInfoBase = "{\"message\":\"%s\", \"sha\":\"%s\" }";
        String data = String.format(hasNoUserInfoBase, commitMsg, sha);
        if (!NetUtils.isEmpty(username) && !NetUtils.isEmpty(email)) {
            data = String.format(hasUserInfoBase, commitMsg, sha, username, email);
        }
//        System.out.println(data);
        int timeout = 10 * 1000;
        return NetUtils.request(HttpType.DELETE, timeout, uploadUrl, null, reqHeaderMap, data);
    }

    /**
     * gitee创建文件. need POST
     *
     * @param owner             用户名
     * @param repo              项目名
     * @param path              创建文件针对对应项目的相对路径
     * @param token             授权token
     * @param contentWillBase64 原始字符串
     * @param commitMsg         提交msg
     */
    private static String createFile(String owner, String repo, String path, String token, String contentWillBase64, String commitMsg) {
        return createFile(owner, repo, path, token, contentWillBase64, commitMsg, "", "");
    }

    private static String createFile(String owner, String repo, String path, String token, String contentWillBase64, String commitMsg, String username, String email) {
        try {
            String content = Base64.getEncoder().encodeToString(contentWillBase64.getBytes(StandardCharsets.UTF_8));
            // 据RFC 822规定，每76个字符，还需要加上一个回车换行
            // 有时就因为这些换行弄得出了问题，解决办法如下，替换所有换行和回车
            // result = result.replaceAll("[\\s*\t\n\r]", "");
            content = content.replaceAll("[\\s*]", "");
            Map<String, String> reqHeaderMap = new HashMap<String, String>();
            reqHeaderMap.put("Content-Type", "application/json;charset=UTF-8");
            reqHeaderMap.put("Authorization", "token " + token);
            reqHeaderMap.put("User-Agent", "Github File Uploader App");
            String base = "https://api.github.com/repos/%s/%s/contents%s";
            String uploadUrl = String.format(base, owner, repo, path);
            System.out.println(uploadUrl);

            String hasUserInfoBase = "{\"content\":\"%s\",\"message\":\"%s\" ,\"committer\":{ \"name\":\"%s\",\"email\":\"%s\" }}";
            String hasNoUserInfoBase = "{\"content\":\"%s\",\"message\":\"%s\" }";
            String data = String.format(hasNoUserInfoBase, content, commitMsg);
            if (!NetUtils.isEmpty(username) && !NetUtils.isEmpty(email)) {
                data = String.format(hasUserInfoBase, content, commitMsg, username, email);
            }
//        System.out.println(data);
            int timeout = 10 * 1000;
            String res = NetUtils.request(HttpType.PUT, timeout, uploadUrl, null, reqHeaderMap, data);

//            JSONObject obj = JSON.parseObject(res);
//            if (obj.size() > 0) {
//                return obj.getJSONObject("content").getString("download_url");
//            }

            Matcher matcher = Pattern.compile("\"download_url\": *\"([^\"]+)\"").matcher(res.toString());
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String getSha(String owner, String repo, String path, String token) {
        try {
//            String base = "https://gitee.com/api/v5/repos/%s/%s/contents/%s?access_token=%s";
            String base = "https://api.github.com/repos/%s/%s/contents%s";
            String requestUrl = String.format(base, owner, repo, path, token);
//            System.out.println(requestUrl);
            Map<String, String> reqHeaderMap = new HashMap<String, String>();
            reqHeaderMap.put("Content-Type", "application/json;charset=UTF-8");
//        reqHeaderMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
            reqHeaderMap.put("User-Agent", "Gitee");
//        reqHeaderMap.put("Accept-Encoding", "gzip, deflate, br");
//        reqHeaderMap.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            int timeout = 10 * 1000;

            String result = NetUtils.request(HttpType.GET, timeout, requestUrl, null, reqHeaderMap, null);
////            System.out.println("result:" +result);
//            JSONObject obj = JSON.parseObject(result);
////            System.out.println(obj);
//            if (obj.size() > 0) {
//                String url = obj.getString("url");
////                System.out.println(url);
////                System.out.println(requestUrl);
//                if (url.contains(requestUrl)) {
////                    System.out.println("same requet url");
//                    return obj.getString("sha");
//                } else {
//                    System.err.println("request url diff!");
//                }
//            } else {
//                System.err.println("response json len is 0");
//            }

            Matcher matcher = Pattern.compile("\"sha\": *\"([^\"]+)\"").matcher(result.toString());
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return "";
    }
}

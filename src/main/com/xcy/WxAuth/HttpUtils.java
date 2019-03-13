package com.xcy.WxAuth;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * @author xcy
 * @version V1.0.0
 * @description HTTP工具类
 * @date 2019/03/13 16:11
 */
public class HttpUtils {

    private static Log log = LogFactory.getLog(HttpUtils.class);

    /**
     * 定义编码格式 UTF-8
     */
    public static final String URL_PARAM_DECODECHARSET_UTF8 = "UTF-8";

    /**
     * 定义编码格式 GBK
     */
    public static final String URL_PARAM_DECODECHARSET_GBK = "GBK";

    private static final String URL_PARAM_CONNECT_FLAG = "&";

    private static final String EMPTY = "";

    private static MultiThreadedHttpConnectionManager connectionManager = null;

    private static int connectionTimeOut = 25000;

    private static int socketTimeOut = 25000;

    private static int maxConnectionPerHost = 20;

    private static int maxTotalConnections = 20;

    private static HttpClient client;

    static {
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
        connectionManager.getParams().setSoTimeout(socketTimeOut);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
        client = new HttpClient(connectionManager);
    }

    /**
     * POST方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String urlPost(String url, Map<String, String> params, String enc) {

        String response = EMPTY;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            postMethod.getParams().setParameter(
                    HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            //将表单的值放入postMethod中  
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                String value = params.get(key);
                //System.out.println(key + ":" + value);
                postMethod.addParameter(key, value);
            }
            //执行postMethod  
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = postMethod.getResponseBodyAsString();
            } else {
                log.error("响应状态码 = " + postMethod.getStatusCode());
            }
        } catch (HttpException e) {
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("发生网络异常", e);
            e.printStackTrace();
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }

        return response;
    }

    /**
     * POST方式提交数据  按照接口参数顺序提交
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws Exception
     * @throws IOException IO异常
     */
    public static String urlPost(String url, Map<String, String> params, String enc, String[] paramArray) throws Exception {

        String response = EMPTY;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            postMethod.getParams().setParameter(
                    HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            //将表单的值放入postMethod中  
            if (paramArray == null) {
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    String value = params.get(key);
                    //System.out.println(key + ":" + value);
                    postMethod.addParameter(key, value);
                }
            } else {
                for (String str : paramArray) {
                    if (params.containsKey(str)) {
                        postMethod.addParameter(str, params.get(str));
                    } else {
                        throw new Exception("post 参数不存在");
                    }

                }
            }
            //执行postMethod  
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = postMethod.getResponseBodyAsString();
            } else {
                log.error("响应状态码 = " + postMethod.getStatusCode());
            }
        } catch (HttpException e) {
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("发生网络异常", e);
            e.printStackTrace();
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }

        return response.replaceAll(":null", ":\"\"");
    }

    public static String urlPost2(String url, Map<String, String> params, String enc, String[] paramArray) throws Exception {

        String response = EMPTY;
        PostMethod postMethod = null;
        try { 
        	/*StringBuffer strtTotalURL = new StringBuffer(EMPTY);  
            
            if(strtTotalURL.indexOf("?") == -1) {  
              strtTotalURL.append(url).append("?").append(getUrl(params, enc));  
            } else {  
                strtTotalURL.append(url).append("&").append(getUrl(params, enc));  
            }  
            log.debug("POST请求URL = \n" + strtTotalURL.toString());
            postMethod = new PostMethod(strtTotalURL.toString());  
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);  */
            postMethod = new PostMethod(url);
            postMethod.getParams().setParameter(
                    HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            //将表单的值放入postMethod中
            if (paramArray == null) {
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    String value = params.get(key);
                    //System.out.println(key + ":" + value);
                    postMethod.addParameter(key, value);
                }
            } else {
                for (String str : paramArray) {
                    if (params.containsKey(str)) {
                        postMethod.addParameter(str, params.get(str));
                    } else {
                        throw new Exception("post 参数不存在");
                    }

                }
            }
            //执行postMethod  
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = postMethod.getResponseBodyAsString();
            } else {
                log.error("响应状态码 = " + postMethod.getStatusCode());
            }
        } catch (HttpException e) {
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("发生网络异常", e);
            e.printStackTrace();
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }

        return response;
    }

    /**
     * GET方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String urlGet(String url, Map<String, String> params, String enc) {

        String response = EMPTY;
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer(EMPTY);

        if (strtTotalURL.indexOf("?") == -1) {
            strtTotalURL.append(url).append("?").append(getUrl(params, enc));
        } else {
            strtTotalURL.append(url).append("&").append(getUrl(params, enc));
        }
        log.debug("GET请求URL = \n" + strtTotalURL.toString());

        try {
            getMethod = new GetMethod(strtTotalURL.toString());
            getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            //执行getMethod  
            int statusCode = client.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = getMethod.getResponseBodyAsString();
            } else {
                log.debug("响应状态码 = " + getMethod.getStatusCode());
            }
        } catch (HttpException e) {
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("发生网络异常", e);
            e.printStackTrace();
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
                getMethod = null;
            }
        }

        return response;
    }

    /**
     * 据Map生成URL字符串
     *
     * @param map      Map
     * @param valueEnc URL编码
     * @return URL
     */
    private static String getUrl(Map<String, String> map, String valueEnc) {

        if (null == map || map.keySet().size() == 0) {
            return (EMPTY);
        }
        StringBuffer url = new StringBuffer();
        Set<String> keys = map.keySet();
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            String key = it.next();
            if (map.containsKey(key)) {
                String val = map.get(key);
                String str = val != null ? val : EMPTY;
                try {
                    str = URLEncoder.encode(str, valueEnc);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                url.append(key).append("=").append(str).append(URL_PARAM_CONNECT_FLAG);
            }
        }
        String strURL = EMPTY;
        strURL = url.toString();
        if (URL_PARAM_CONNECT_FLAG.equals(EMPTY + strURL.charAt(strURL.length() - 1))) {
            strURL = strURL.substring(0, strURL.length() - 1);
        }

        return (strURL);
    }

    /**
     * 通过GET方式发起http请求
     */
    public static String requestByGetMethod(String url) {
        HttpClient client = new HttpClient();
        StringBuilder sb = new StringBuilder();
        InputStream ins = null;
        GetMethod method = new GetMethod(url);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        try {
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                ins = method.getResponseBodyAsStream();
                byte[] b = new byte[1024];
                int r_len = 0;
                while ((r_len = ins.read(b)) > 0) {
                    sb.append(new String(b, 0, r_len, method.getResponseCharSet()));
                }
            } else {
                System.err.println("Response Code: " + statusCode);
            }
        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
        } finally {
            method.releaseConnection();
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();

    }

    /**
     * post 请求
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static String requestByPostMethod(String url, Object param) throws IOException {
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(url);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl
                .openConnection();
        // 设置是否向connection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true
        connection.setDoOutput(true);
        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // Set the post method. Default is GET
        connection.setRequestMethod("POST");
        // Post cannot use caches
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        // 进行编码
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
        connection.connect();
        PrintWriter out = new PrintWriter(connection.getOutputStream());
        out.print(param);
        out.flush();
        out.close(); // flush and close
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), "UTF-8"));
        StringBuffer line = new StringBuffer("");
        String tempLine = "";
        while ((tempLine = reader.readLine()) != null) {
            line.append(tempLine);
        }
        reader.close();
        connection.disconnect();
        return line.toString();
    }

    /**
     * 接口发送json数据
     *
     * @param url      路径 https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=TOKEN
     * @param jsonData json数据
     * @return
     * @throws Exception
     */
    public static String sendPostJson(String url, String jsonData) throws Exception {

        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response = "";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(jsonData);
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response += lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();
            log.info(response.toString());
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return response;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=18_4dxafjnK5a7KPCxFlzgmVLqw_Stp82FuPbNp0SvFUcmhL6AiP4pYljh1RPDF0IHzR7dNQkS5H3En5cIWm3WXGOBQ6ILnY1ugWgogLUIukcl6hdkAjDVgHpjY8Q4JQ0byh8Lp_JpMCYCwfUxcOSLhAGAZDW";
        String jsonData = "{\"touser\":\"o3dJW0kbS-yn84mhTqeTgjvU_2LU\",\"template_id\":\"68Wj0TwDaa9b5gvR2C3cvO3Vjwk9rsuxWEeX419jE5U\",\"url\":\"http://weixin.qq.com/download\",\"topcolor\":\"#FF0000\",\"data\":{\"first\":{\"value\":\"测试测试\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"巧克力\",\"color\":\"#173177\"},\"keyword2\": {\"value\":\"ddddd112\",\"color\":\"#173177\"},\"keyword3\": {\"value\":\"2\",\"color\":\"#173177\"},\"keyword4\":{\"value\":\"2018年7月2日12:32:44\",\"color\":\"#173177\"},\"keyword5\": {\"value\":\"2018年7月2日15:33:44\",\"color\":\"#173177\"},\"remark\":{\"value\":\"备注嘻嘻嘻\",\"color\":\"#173177\"}}}";
        String result = HttpUtils.sendPostJson(url, jsonData);
        System.out.println(result);
    }
}  

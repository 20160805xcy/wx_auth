package com.xcy.WxAuth;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author xcy
 * @date 2019/02/14 9:20
 * @description AuthUtil
 * @since V1.0.0
 */
public class AuthUtil {
    //正式
    public static final String APPID = "wx4e053ba522130561";
    public static final String APPSECRET = "6fe967d6c5410d0a56a4b25bcec71737";

    //测试
    //public static final String APPID = "wxfeba05fe24d223e5";
    //public static final String APPSECRET = "fdb82bd2d47f70196e5ae11a19bf796b";

    public static JSONObject doGetJson(String url) throws IOException {
        JSONObject jsonObject = null;
        //首先初始化HttpClient对象
        DefaultHttpClient client = new DefaultHttpClient();
        //通过get方式进行提交
        HttpGet httpGet = new HttpGet(url);
        //通过HTTPClient的execute方法进行发送请求
        HttpResponse response = client.execute(httpGet);
        //从response里面拿自己想要的结果
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            jsonObject = JSONObject.parseObject(result);
        }
        //把链接释放掉
        httpGet.releaseConnection();
        return jsonObject;
    }

}

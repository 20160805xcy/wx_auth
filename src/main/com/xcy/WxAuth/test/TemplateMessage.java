package com.xcy.WxAuth.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xcy.WxAuth.util.HttpUtils;

/**
 * @author xcy
 * @version V1.0.0
 * @description 模板消息
 * @date 2019/03/13 16:11
 */
public class TemplateMessage {
    //测试号
    static String appid = "wxfeba05fe24d223e5";
    static String secret = "fdb82bd2d47f70196e5ae11a19bf796b";

    public static String access_token;

    static {
        String newToken = getToken(appid, secret);
        if (newToken == null) {
            newToken = getToken(appid, secret);
        }
        access_token = newToken;
    }

    public static void main(String[] args) throws Exception {
        //String jsonData = toTemplateMsgText();
        String jsonData = toOrderTemplateMsgText();
        System.out.println(" == " + jsonData);
        //String access_token = "18_WNPM8uAEI3yC3pWdG-sFgS6ws6seusr2bj8XRvY6MYliBvsAGNXDyCK3-gSoJGTPDkypY2h2ft_wVhHogRzjFQIHmlb0U1msNae66iR4BCrIajXTk1FNDfXEILZQTOOKAkAOKmA7SWFtAj0QJGXjABANKB";
        String ret = SendTemplateMessage(access_token, jsonData);
        //成功返回值:  {"errcode":0,"errmsg":"ok","msgid":352440046713847808}
        System.out.println(ret);
    }

    /**
     * 正式账号: 订单发货通知 - 根据模板返回Json数据格式内容
     * @return
     */
    public static String toTemplateMsgText() {
        String oppenId = "ojGm2jiQAlpaeIGjKtlUOc1RC734";
        String templateId = "2NIBKvdN6ZWxMBdamRSDFgCMirRC-ytGGO7f4j9qVhA";
        String url = "";
        String firstdata = "发货通知";
        String keyword1 = "华富洋供应链";
        String keyword2 = "MN88010036";
        String keyword3 = "99";
        String keyword4 = "2019年2月13日";
        String keyword5 = "2019年2月13日";
        String remark = "预计到货时间:2天内 请注意查收";
        String jsonText = "{\"touser\":\"OPENID\",\"template_id\":\"TemplateId\",\"url\":\"Url\",\"topcolor\":\"#743A3A\",\"data\":{\"first\":{\"value\":\"Firstdata\",\"color\":\"#FF0000\"},\"keyword1\":{\"value\":\"Keyword1\",\"color\":\"#C4C400\"},\"keyword2\":{\"value\":\"Keyword2\",\"color\":\"#0000FF\"},\"keyword3\":{\"value\":\"Keyword3\",\"color\":\"#008000\"},\"keyword4\":{\"value\":\"Keyword4\",\"color\":\"#C4C400\"},\"keyword5\": {\"value\":\"Keyword5\",\"color\":\"#173177\"},\"remark\":{\"value\":\"Remark\",\"color\":\"#008000\"}}}";
        jsonText = jsonText.replace("OPENID", oppenId).replace("TemplateId", templateId).replace("Url", url).replace("Firstdata", firstdata).replace("Keyword1", keyword1).replace("Keyword2", keyword2).replace("Keyword3", keyword3).replace("Keyword4", keyword4).replace("Keyword5", keyword5).replace("Remark", remark);
        return jsonText;
    }

    /**
     * 测试账号: 下单成功通知 - 根据模板返回Json数据格式内容
     * @return
     */
    public static String toOrderTemplateMsgText() {
        String oppenId = "o3dJW0kbS-yn84mhTqeTgjvU_2LU";
        String templateId = "68Wj0TwDaa9b5gvR2C3cvO3Vjwk9rsuxWEeX419jE5U";
        String url = "";
        String firstdata = "下单成功通知";
        String keyword1 = "三线三边  180x250cm  6张 喷绘 已经下单，我们会尽快安排制作，预计2016年2月3日12:50交货";
        String keyword2 = "2018年7月5日";
        String keyword3 = "18617151926";
        String remark = "预计到货时间:2天内 请注意查收";
        String jsonText = "{\"touser\":\"OPENID\",\"template_id\":\"TemplateId\",\"url\":\"Url\",\"topcolor\":\"#743A3A\",\"data\":{\"first\":{\"value\":\"Firstdata\",\"color\":\"#FF0000\"},\"keyword1\":{\"value\":\"Keyword1\",\"color\":\"#C4C400\"},\"keyword2\":{\"value\":\"Keyword2\",\"color\":\"#0000FF\"},\"keyword3\":{\"value\":\"Keyword3\",\"color\":\"#00FFFF\"},\"remark\":{\"value\":\"Remark\",\"color\":\"#00800\"}}}";
        jsonText = jsonText.replace("OPENID", oppenId).replace("TemplateId", templateId).replace("Url", url).replace("Firstdata", firstdata).replace("Keyword1", keyword1).replace("Keyword2", keyword2).replace("Keyword3", keyword3).replace("Remark", remark);
        return jsonText;
    }

    /**
     * 获取token
     * @param appid
     * @param secret
     * @return
     */
    public static String getToken(String appid, String secret) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret + "";
        String result = HttpUtils.requestByGetMethod(url);
        JSONObject jsonObject = JSON.parseObject(result);
        String token = (String) jsonObject.get("access_token");
        System.out.println("token是: " + token);
        return token;
    }

    /**
     * 发送模板消息
     * @param access_token
     * @param jsonData
     * @return
     * @throws Exception
     */
    public static String SendTemplateMessage(String access_token, String jsonData) throws Exception {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token;
        String result = HttpUtils.sendPostJson(url, jsonData);
        return result;
    }
}

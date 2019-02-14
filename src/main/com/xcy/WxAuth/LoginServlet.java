package com.xcy.WxAuth;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author xcy
 * @date 2019/02/14 9:30
 * @description
 * @since V1.0.0
 */
@WebServlet("/wxLogin")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //第一步：引导用户进入授权页面同意授权，获取code

        //回调地址
        //String backUrl = "http://wechat2018.nat300.top/WxAuth/callBack";  //第1种情况使用
        String backUrl = "http://wechat2018.nat300.top/WxAuth/wxCallBack";//第2种情况使用，这里是web.xml中的路径

        //授权页面地址
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + AuthUtil.APPID
                + "&redirect_uri=" + URLEncoder.encode(backUrl)
                + "&response_type=code"
                + "&scope=snsapi_userinfo"
                + "&state=STATE#wechat_redirect";

        //重定向到授权页面
        response.sendRedirect(url);
    }
}

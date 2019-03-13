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
        //第1种情况: 直接取根据token获取到的用户信息
        //String backUrl = "http://wechat2018.nat300.top/WxAuth/callBack";
        //第2种情况: 用户要先绑定当前系统才能继续访问,这里访问方式配置在web.xml中
        String backUrl = "http://wechat2018.nat300.top/WxAuth/wxCallBack";

        /**
         * 应用授权作用域:
         * snsapi_base (不弹出授权页面,直接跳转,只能获取用户openid);
         * snsapi_userinfo(弹出授权页面,可通过openid拿到昵称,性别,所在地.并且,即使在未关注的情况下,只要用户授权,也能获取其信息)
         */
        //授权页面地址
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + AuthUtil.APPID
                + "&redirect_uri=" + URLEncoder.encode(backUrl,"UTF-8")
                + "&response_type=code"
                + "&scope=snsapi_userinfo"
                + "&state=STATE#wechat_redirect";

        //重定向到授权页面
        response.sendRedirect(url);
    }
}

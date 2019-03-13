package com.xcy.WxAuth.controller;


import com.alibaba.fastjson.JSONObject;
import com.xcy.WxAuth.util.AuthUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author xcy
 * @description 回调地址
 * @date 2019/02/14 10:07
 * @version V1.0.0
 */
//@WebServlet("/callBack")
public class CallBackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String dbUrl;
    private String driverClassName;
    private String userName;
    private String passWord;

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * 第一种情况下,需要将下面初始化方式注释掉
     * 初始化数据库
     * @param config
     */
    /**   **/
    @Override
    public void init(ServletConfig config) {
        try {
            this.dbUrl = config.getInitParameter("dbUrl");
            this.driverClassName = config.getInitParameter("driverClassName");
            this.userName = config.getInitParameter("userName");
            this.passWord = config.getInitParameter("passWord");
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 回调后的方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //第二步：通过code换取网页授权access_token

        //从request里面获取code参数(当微信服务器访问回调地址的时候，会把code参数传递过来)
        String code = request.getParameter("code");

        System.out.println("code:" + code);

        //获取code后，请求以下链接获取access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + AuthUtil.APPID
                + "&secret=" + AuthUtil.APPSECRET
                + "&code=" + code
                + "&grant_type=authorization_code";

        //通过网络请求方法来请求上面这个接口
        JSONObject jsonObject = AuthUtil.doGetJson(url);

        System.out.println("返回的access_token[含openId]的jsonObject: " + jsonObject);

        //从返回的JSON数据中取出access_token和openid，拉取用户信息时用
        String token = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");

        //第三步：刷新access_token（如果需要）

        //第四步：拉取用户信息(需scope为 snsapi_userinfo)
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token
                + "&openid=" + openid
                + "&lang=zh_CN";
        //通过网络请求方法来请求上面这个接口
        JSONObject userInfo = AuthUtil.doGetJson(infoUrl);
        String wxNickName = userInfo.getString("nickname");

        System.out.println(userInfo);


        //第1种情况： 使用从token种获取到的微信用户信息直接展示到页面,无需注册和绑定,直接跳转
        //request.setAttribute("info", userInfo);
        //request.getRequestDispatcher("/success1.jsp").forward(request, response);


        //第2种情况： 将微信与当前系统的账号进行绑定(需将第1种情况和@WebServlet("/callBack")注释掉)
        //第一步，根据当前openid查询数据库，看是否该账号已经进行绑定
        /** **/
        try {
            String nickname = getNickName(openid);
            if (!"".equals(nickname)) {
                //已绑定
                request.setAttribute("nickname", nickname);
                System.out.println("马上传递到前端页面的nickname: " + nickname);
                request.getRequestDispatcher("/success2.jsp").forward(request, response);
            } else {
                //未绑定
                request.setAttribute("nickname", wxNickName);
                request.setAttribute("openid", openid);
                System.out.println("马上传递到前端页面的openid: " + openid);
                request.getRequestDispatcher("/binding.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * post方法,用来接受前端的表单提交请求.(绑定操作时用)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String openid = request.getParameter("openid");
        String nickName = request.getParameter("nickname");
        System.out.println("账户: " + account + " 密码: " + password + " openId: " + openid + "昵称: " + nickName);

        try {
            int temp = updateUser(account, password, openid, nickName);
            if (temp > 0) {
                String nickname = getNickName(openid);
                request.setAttribute("nickname", nickname);
                request.getRequestDispatcher("/success2.jsp").forward(request, response);
                System.out.println("账号绑定成功");
            } else {
                System.out.println("账号绑定失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据库的查询-根据openid查询该用户是否已经绑定
     * @param openid
     * @return 用户的昵称
     * @throws SQLException
     */
    public String getNickName(String openid) throws SQLException {
        String nickName = "";
        conn = DriverManager.getConnection(dbUrl, userName, passWord);
        String sql = "select nickname from user where openid = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, openid);
        rs = ps.executeQuery();
        while (rs.next()) {
            nickName = rs.getString("nickname");
        }
        rs.close();
        ps.close();
        conn.close();

        return nickName;
    }

    /**
     * 数据库的修改(openid的绑定)
     * @param account 账户名
     * @param password 密码
     * @param openid id
     * @param nickName 昵称
     * @return
     * @throws SQLException
     */
    public int updateUser(String account, String password, String openid, String nickName) throws SQLException {

        //创建数据库链接
        conn = DriverManager.getConnection(dbUrl, userName, passWord);
        //绑定的前提是要我们要有自己客户数据表,然后多加一个openid字段,通过客户名称和密码进行绑定.
        //String sql = "update user set openid = ? where account = ? and password = ?";
        //下面这行是直接根据用户名,密码,openid,昵称进行存数据库操作.纯粹是为了好玩,真实业务并不是这么玩的.
        String sql = "insert into user(openid, account, password, nickname) values (?, ?, ?, ?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, openid);
        ps.setString(2, account);
        ps.setString(3, password);
        ps.setString(4, nickName);
        int temp = ps.executeUpdate();
        //关闭链接
        rs.close();
        ps.close();
        conn.close();
        return temp;
    }

}


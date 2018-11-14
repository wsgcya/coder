package com.wsgcya.coder.wechat.wechataccess.controller;

import com.wsgcya.coder.common.constant.MessageConstants;
import com.wsgcya.coder.common.util.StringUtils;
import com.wsgcya.coder.common.util.XmlUtil;
import com.wsgcya.coder.wechat.wechataccess.service.WechatApiService;
import com.wsgcya.coder.wechat.wechataccess.service.WechatEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping(value = "/wechat")
public class WechatController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WechatApiService wechatApiService;
    @Autowired
    private WechatEventService wechatEventService;
    /**
     * @return
     * @Title access
     * @Class WechatController
     * @Description基础接入连接
     * @author ganchao
     * @Date 2016年7月28日
     */
    @RequestMapping(value = "/access")
    @ResponseBody
    public String access(@Param("signature") String signature, @Param("timestamp") String timestamp, @Param("nonce") String nonce,
                         @Param("echostr") String echostr, HttpServletRequest request) {
        boolean checkflag = wechatApiService.checkSignature(signature, timestamp, nonce);
        if (checkflag) {
            if (echostr == null) {
                Map<String, Object> requestMap = XmlUtil.Dom2Map(request);
                requestMap.put(
                        MessageConstants.WECHAT_MSGKEY_MSGREPETITIVEID,
                        requestMap.get(MessageConstants.WECHAT_MSGKEY_FROMUSERNAME) + timestamp
                );
                String res = wechatEventService.wechatEvent(requestMap);
                logger.info("wx call queryString:{} requestbody:{} res:{}", request.getQueryString(), requestMap.toString(), res);
                return res;
            } else {
                return echostr;
            }
        } else {
            logger.warn("wx call sign error queryString:{}", request.getQueryString());
        }
        return "sign error";
    }

    /**
     * @param code
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @Title authInfoForOut
     * @Class WechatController
     * @Description 微信对外授权验证参数为backurl:回调地址,scp:scope类型
     * @author ganchao
     * @Date 2016年9月24日
     */
    @RequestMapping(value = "/auth")
    public String authInfoForOut(@Param("code") String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String backurl = request.getParameter("backurl");
        String scope = request.getParameter("scp");
        String wechatinfo = "";
        HttpSession session = request.getSession();
        if (StringUtils.isNotBlank(scope) && "snsapi_userinfo".equals(scope)) {
            wechatinfo = "openid=" + session.getAttribute("openid") + "&nickname="
                    + StringUtils.urlEncode((String) session.getAttribute("pinickname")) + "&headimgurl="
                    + StringUtils.urlEncode((String) session.getAttribute("piheadurl"));
        } else {
            wechatinfo = "openid=" + session.getAttribute("openid");
        }
        String splitChar = backurl.indexOf("?") == -1 ? "?" : "&";
        backurl += splitChar + wechatinfo;
        response.sendRedirect(backurl);
        return null;
    }

    /**
     * @param nowUrl
     * @return
     * @Title getJsapiSignature
     * @Class WechatController
     * @Description 获取JSAPI
     * @author ganchao
     * @Date 2016年9月27日
     */
    @RequestMapping(value = "/jsapi")
    @ResponseBody
    public String getJsapiSignature(@RequestParam("url") String nowUrl) {
        return wechatApiService.getJsapiSignature(nowUrl);
    }
}

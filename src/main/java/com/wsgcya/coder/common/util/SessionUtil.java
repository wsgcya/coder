package com.wsgcya.coder.common.util;


import com.wsgcya.coder.common.constant.ProjectConstants;

import javax.servlet.http.HttpSession;

/**
 * session工具 ,用于规范session中数据存取的操作。
 *
 * @author yewangwang@telincn.com
 * @date 2018-01-11 15:53
 */
public class SessionUtil {
    private HttpSession session;

    public static SessionUtil build(HttpSession session) {
        return new SessionUtil(session);
    }

    public SessionUtil(HttpSession session) {
        this.session = session;
    }

    public String getJumpToken() {
        return getJumpToken(false);
    }

    private String getJumpToken(boolean useAndInvalid) {
        String jumptoken = (String) session.getAttribute(ProjectConstants.JUMP_TOKEN);
        if (useAndInvalid) {
            session.removeAttribute(ProjectConstants.JUMP_TOKEN);
        }
        return jumptoken;
    }

    public void setJumpToken(String jumptoken) {
        if (StringUtils.isBlank(jumptoken)) {
            return;
        }
        session.setAttribute(ProjectConstants.JUMP_TOKEN, jumptoken);
    }

    public void removeJumpToken() {
        session.removeAttribute(ProjectConstants.JUMP_TOKEN);
    }

    public String getOpenid() {
        return (String) session.getAttribute(ProjectConstants.ACCOUNT_OPENID);
    }

    public void setOpenid(String openid) {
        session.setAttribute(ProjectConstants.ACCOUNT_OPENID, openid);
    }

    public String getNickname() {
        return (String) session.getAttribute("pinickname");
    }

    public String getHeadImageUrl() {
        return (String) session.getAttribute("piheadurl");
    }

    public String getUnionid(){return (String) session.getAttribute("unionid");}

    public String getFrom() {
        return getFrom(false);
    }

    /**
     * @param useAndInvalid true 获取并移除原来的键值对
     * @return
     */
    public String getFrom(boolean useAndInvalid) {
        String from = (String) session.getAttribute(ProjectConstants.FROM);
        if (useAndInvalid) {
            session.removeAttribute(ProjectConstants.FROM);
        }
        return from;
    }

    public void setFrom(String from) {
        session.setAttribute(ProjectConstants.FROM, from);
    }

    public void setUtmDomain(String thirdDomain) {
        session.setAttribute(ProjectConstants.UTM_DOMAIN, thirdDomain);
    }

    public String getUtmDomain() {
        return (String) session.getAttribute(ProjectConstants.UTM_DOMAIN);
    }

    /**
     * 设置追踪数据
     *
     * @param key
     * @param value
     */
    public void setUtm(String key, String value) {
        session.setAttribute(key, value);
    }

    /**
     * 获取追踪数据
     *
     * @param utmKey {@link ProjectConstants#UTM_DOMAIN}
     * @return
     */
    public String getUtm(String utmKey) {
        return (String) session.getAttribute(utmKey);
    }
}

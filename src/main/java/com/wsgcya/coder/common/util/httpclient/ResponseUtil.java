package com.wsgcya.coder.common.util.httpclient;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author hzwaso
 */
public class ResponseUtil {
    private static Logger logger = Logger.getLogger(ResponseUtil.class);

    public static void reply(HttpServletResponse resp, String str) {
        PrintWriter pw = null;
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        try {
            pw = resp.getWriter();
            pw.write(str);
            pw.flush();
        } catch (IOException e) {
            logger.error("response返回消息体异常", e);
        } finally {
            if (null != pw) {
                pw.close();
            }
        }
    }
}

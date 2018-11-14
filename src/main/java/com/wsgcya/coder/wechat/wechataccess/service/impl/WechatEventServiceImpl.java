package com.wsgcya.coder.wechat.wechataccess.service.impl;

import com.wsgcya.coder.common.constant.MessageConstants;
import com.wsgcya.coder.wechat.wechataccess.service.WechatEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 微信事件处理类
 *
 * @author
 */
@Service
public class WechatEventServiceImpl implements WechatEventService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 微信消息处理
     */
    @Override
    public String wechatEvent(Map<String, Object> body) {
        String res = "";
        String msgType = (String) body.get(MessageConstants.WECHAT_MSGKEY_MSGTYPE);
        if (MessageConstants.MESSAGE_TYPE_EVENT.equals(msgType)) {
            // 进入事件处理
            res = eventDeal(body);
        } else {
            // 进入消息处理
            switch (msgType) {
                case MessageConstants.MESSAGE_TYPE_TEXT:
                    //   res = miniProService.sendTempsessionMessage(body);
                    //    res = miniProService.sendTempsessionMessageForBxl(body);
                    break;
                case MessageConstants.MESSAGE_TYPE_IMAGE:
                    break;
                case MessageConstants.MESSAGE_TYPE_VOICE:
                    break;
                // 自动回复及客服相关的处理
                //     res = replyService.doHandler(body);
                case MessageConstants.MESSAGE_TYPE_VIDEO:
                case MessageConstants.MESSAGE_TYPE_SHORTVIDEO:
                    // 视频类数据独立处理
                    break;
                case MessageConstants.MESSAGE_TYPE_LOCATION:
                    //     res = locationService.doHandler(body);
                case MessageConstants.MESSAGE_TYPE_LINK:
                    break;
                default:
            }
        }
        return res;
    }

    /**
     * @param body
     * @return
     * @Title eventDeal
     * @Class WechatEventServiceImpl
     * @Description 事件处理
     * @author qinshijiang@telincn.com
     * @Date 2016年9月30日
     */
    private String eventDeal(Map<String, Object> body) {
        String res = "";
        String event = body.get(MessageConstants.WECHAT_MSGKEY_EVENT).toString();
        switch (event) {
            case MessageConstants.MESSAGE_TYPE_LOCATION_NEW:
                //         res = locationService.doHandler(body);
                break;
            case MessageConstants.EVENT_TYPE_SUBSCRIBE:
                //       res = subscribeService.doHandler(body);
                break;
            case MessageConstants.EVENT_TYPE_UNSUBSCRIBE:
                //       unsubscribeService.doHandler(body);
                break;
            case MessageConstants.EVENT_TYPE_SCAN:
                //      res = scanService.doHandler(body);
                break;
            case MessageConstants.EVENT_TYPE_CLICK:
                // 菜单点击统计
                //   statMenuClick(body);

                //   res = clickService.doHandler(body);
                break;
            case MessageConstants.EVENT_TYPE_VIEW:
                // 增加view计数统计
                // redisService.addViewStat(body.get(MessageConstants.WECHAT_MSGKEY_EVENTKEY).toString());
                // 菜单点击统计
                //   statMenuClick(body);
                break;
            case MessageConstants.EVENT_TYPE_TEMPLATESENDJOBFINISH:
            case MessageConstants.EVENT_TYPE_SHAKEAROUNDUSERSHAKE:
                // 其他已知放弃数据处理
                return "";
            case MessageConstants.EVENT_TYPE_CARDPASSCHECK:
            case MessageConstants.EVENT_TYPE_CARDNOTPASSCHECK:
            case MessageConstants.EVENT_TYPE_USERGETCARD:
            case MessageConstants.EVENT_TYPE_USERDELCARD:
            case MessageConstants.EVENT_TYPE_USERGIFTINGCARD:
            case MessageConstants.EVENT_TYPE_USERCONSUMECARD:
            case MessageConstants.EVENT_TYPE_USER_ENTER_SESSION_FROM_CARD:
            case MessageConstants.EVENT_TYPE_USERVIEWCARD:
            case MessageConstants.EVENT_TYPE_UPDATEMEMBERCARD:
            case MessageConstants.EVENT_TYPE_CARDSKUREMIND:
            case MessageConstants.EVENT_TYPE_CARDPAYORDER:
            case MessageConstants.EVENT_TYPE_USERPAYFROMPAYCELL:
            case MessageConstants.EVENT_TYPE_SUBMITMEMBERCARDUSERINFO:
                //  cardService.doHandler(body);
                break;
            case MessageConstants.EVENT_TYPE_USERENTERTEMPSESSION:

                break;
            default:
                break;
        }
        return res;
    }

    /**
     * 统计 菜单点击
     *
     * @param body
     */
    /*private void statMenuClick(Map<String, Object> body) {
        if (globalConfig.getMenuClickStat()) {
            // 获取event_key
            String eventKey = (String) body.get(MessageConstants.WECHAT_MSGKEY_EVENTKEY);
            if (StringUtils.isNotBlank(eventKey)) {
                String today = DateUtil.parseDateTime(new Date(), "yyyyMMdd");
                redisService.hashIncr(RedisConstants.MENU_CLICK_PREFIX + today, eventKey);
            }
        }
    }*/
}

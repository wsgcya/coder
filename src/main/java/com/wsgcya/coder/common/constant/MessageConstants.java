package com.wsgcya.coder.common.constant;

/**
 * 
 * @Description 微信消息常量
 * @author ganchao
 * @Date 2016年12月15日
 */
public class MessageConstants {
    /**
     * 消息类型：文本
     */
    public static final String MESSAGE_TYPE_TEXT = "text";

    /**
     * 消息类型：音乐
     */
    public static final String MESSAGE_TYPE_MUSIC = "music";

    /**
     * 消息类型：图文
     */
    public static final String MESSAGE_TYPE_NEWS = "news";

    /**
     * 消息类型：图片
     */
    public static final String MESSAGE_TYPE_IMAGE = "image";

    /**
     * 消息类型：链接
     */
    public static final String MESSAGE_TYPE_LINK = "link";

    /**
     * 消息类型：地理位置
     */
    public static final String MESSAGE_TYPE_LOCATION = "location";

    public static final String MESSAGE_TYPE_LOCATION_NEW = "LOCATION";

    /**
     * 消息类型：音频
     */
    public static final String MESSAGE_TYPE_VOICE = "voice";

    /**
     * 消息类型：视频
     */
    public static final String MESSAGE_TYPE_VIDEO = "video";

    /**
     * 消息类型: 短视频
     */
    public static final String MESSAGE_TYPE_SHORTVIDEO = "shortvideo";

    /**
     * 消息类型：事件
     */
    public static final String MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(订阅)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";

    /**
     * 事件类型：VIEW(自定义菜单 URl 视图)
     */
    public static final String EVENT_TYPE_VIEW = "VIEW";

    /**
     * 事件类型：LOCATION(上报地理位置事件)
     */
    public static final String EVENT_TYPE_LOCATION = "LOCATION";

    /**
     * 事件类型：SCAN(扫描二维码)
     */
    public static final String EVENT_TYPE_SCAN = "SCAN";

    /**
     * 事件类型：TEMPLATESENDJOBFINISH(模板消息发送完成)
     */
    public static final String EVENT_TYPE_TEMPLATESENDJOBFINISH = "templatesendjobfinish";

    /**
     * 事件类型：shakearoundusershake(摇一摇周边)
     */
    public static final String EVENT_TYPE_SHAKEAROUNDUSERSHAKE = "shakearoundusershake";

    /**
     * 事件类型：scancode_push(扫描事件推送)
     */
    public static final String EVENT_TYPE_SCANCODEPUSH = "scancode_push";

    /**
     * 事件类型：scancode_waitmsg(扫描事件等待消息回应)
     */
    public static final String EVENT_TYPE_SCANCODEWAITMSG = "scancode_waitmsg";

    /**
     * 事件类型：CLICK(企业号自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_QYHCLICK = "click";
    /**
     * 事件类型:(card_pass_check)卡券通过审核
     */
    public static final String EVENT_TYPE_CARDPASSCHECK = "card_pass_check";
    /**
     * 事件类型:(card_not_pass_check)卡券未通过审核
     */
    public static final String EVENT_TYPE_CARDNOTPASSCHECK = "card_not_pass_check";
    /**
     * 事件类型:(user_get_card)用户获得卡券
     */
    public static final String EVENT_TYPE_USERGETCARD = "user_get_card";
    /**
     * 事件类型:(user_del_card)用户删除卡券
     */
    public static final String EVENT_TYPE_USERDELCARD = "user_del_card";

    /**
     * 事件类型:(user_gifting_card)用户转赠卡券
     */
    public static final String EVENT_TYPE_USERGIFTINGCARD = "user_gifting_card";
    /**
     * 事件类型:(user_consume_card)用户核销卡券
     */
    public static final String EVENT_TYPE_USERCONSUMECARD = "user_consume_card";
    /**
     * 事件类型：(user_enter_session_from_card)用户从卡券进入页面
     */
    public static final String EVENT_TYPE_USER_ENTER_SESSION_FROM_CARD = "user_enter_session_from_card";
    /**
     * 事件类型：(user_view_card)用户点击会员卡
     */
    public static final String EVENT_TYPE_USERVIEWCARD = "user_view_card";
    /**
     * 事件类型：(update_member_card)会员卡内容更新
     */
    public static final String EVENT_TYPE_UPDATEMEMBERCARD = "update_member_card";
    /**
     * 事件类型：(card_sku_remind)卡券库存报警
     */
    public static final String EVENT_TYPE_CARDSKUREMIND = "card_sku_remind";
    /**
     * 事件类型：(card_pay_order)券点流水详情事件
     */
    public static final String EVENT_TYPE_CARDPAYORDER = "card_pay_order";
    /**
     * 事件类型：(user_pay_from_pay_cell)微信买单事件
     */
    public static final String EVENT_TYPE_USERPAYFROMPAYCELL = "user_pay_from_pay_cell";
    /**
     * 事件类型：(submit_membercard_user_info)会员卡激活事件
     */
    public static final String EVENT_TYPE_SUBMITMEMBERCARDUSERINFO = "submit_membercard_user_info";

    /**
     * 小程序事件类型：(user_enter_tempsession)进入会话事件
     */
    public static final String EVENT_TYPE_USERENTERTEMPSESSION = "user_enter_tempsession";
    /**
     * 微信消息key:ToUserName(开发者微信号)
     */
    public static final String WECHAT_MSGKEY_TOUSERNAME = "ToUserName";
    /**
     * 微信消息key:FromUserName(发送方账号openid)
     */
    public static final String WECHAT_MSGKEY_FROMUSERNAME = "FromUserName";
    /**
     * 微信小程序sessionfrom
     */
    public static final String WECHAT_MSGKEY_SESSIONFROM = "SessionFrom";
    /**
     * 微信消息key:msgtype(消息类型)
     */
    public static final String WECHAT_MSGKEY_MSGTYPE = "MsgType";
    /**
     * 微信消息key:CreateTime(消息创建时间)
     */
    public static final String WECHAT_MSGKEY_CREATETIME = "CreateTime";
    /**
     * 微信消息key:Content(文本消息内容)
     */
    public static final String WECHAT_MSGKEY_CONTENT = "Content";
    /**
     * 微信消息key:PicUrl(图片链接)
     */
    public static final String WECHAT_MSGKEY_PICURL = "PicUrl";
    /**
     * 微信消息key:MediaId(媒体ID)
     */
    public static final String WECHAT_MSGKEY_MEDIAID = "MediaId";
    /**
     * 微信消息key:Format(语音消息格式如amr，speex等)
     */
    public static final String WECHAT_MSGKEY_FORMAT = "Format";
    /**
     * 微信消息key:Recognition(语音消息自动识别后结果)
     */
    public static final String WECHAT_MSGKEY_RECOGNITION = "Recognition";
    /**
     * 微信消息key:ThumbMediaId(视频缩略图ID)
     */
    public static final String WECHAT_MSGKEY_THUMBMEDIAID = "ThumbMediaId";
    /**
     * 微信消息key:Event(事件类型)
     */
    public static final String WECHAT_MSGKEY_EVENT = "Event";
    /**
     * 微信消息key:EventKey(事件key)
     */
    public static final String WECHAT_MSGKEY_EVENTKEY = "EventKey";
    /**
     * 微信消息key:EventKey(二维码Ticket)
     */
    public static final String WECHAT_MSGKEY_TICKET = "Ticket";

    /**
     * 微信消息key:EventKey(地理位置纬度)
     */
    public static final String WECHAT_MSGKEY_LATITUDE = "Latitude";
    /**
     * 微信消息key:EventKey(地理位置经度)
     */
    public static final String WECHAT_MSGKEY_LONGITUDE = "Longitude";
    /**
     * 微信消息key:EventKey(地理位置精度)
     */
    public static final String WECHAT_MSGKEY_PRECISION = "Precision";
    /**
     * 微信消息key:MsgID(消息id)
     */
    public static final String WECHAT_MSGKEY_MSGID = "MsgID";
    /**
     * 微信消息key:Status(发送状态)
     */
    public static final String WECHAT_MSGKEY_STATUS = "Status";
    /**
     * 微信消息key:msgRepetitiveId(排重ID,由MAP解析后由openid+timestamp生成)
     */
    public static final String WECHAT_MSGKEY_MSGREPETITIVEID = "msgRepetitiveId";
    /**
     * 微信事件key:二维码前缀
     */
    public static final String EVETNKEY_PRE_QRSCENE = "qrscene_";

    /**
     * 微信消息key:CardId(卡券ID)
     */
    public static final String WECHAT_MSGKEY_CARDID = "CardId";
    /**
     * 微信消息key:UserCardCode(卡券Code)
     */
    public static final String WECHAT_MSGKEY_USERCARDCODE = "UserCardCode";
    /**
     * 微信消息key:IsGiveByFriend(卡券是否为转赠领取)
     */
    public static final String WECHAT_MSGKEY_ISGIVEBYFRIEND = "IsGiveByFriend";
    /**
     * 微信消息key:FriendUserName(卡券转赠用户的openid)
     */
    public static final String WECHAT_MSGKEY_FRIENDUSERNAME = "FriendUserName";

    /**
     * 微信消息key:OldUserCardCode(卡券转赠前的code)
     */
    public static final String WECHAT_MSGKEY_OLDUSERCARDCODE = "OldUserCardCode";
    /**
     * 微信消息key:OuterStr(领取场景值用于领取渠道数据统计。可在生成二维码接口及添加Addcard接口中自定义该字段的字符串值。)
     */
    public static final String WECHAT_MSGKEY_OUTERSTR = "OuterStr";

}

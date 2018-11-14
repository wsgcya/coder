package com.wsgcya.coder.common.util;

/**
 * 接口返回的错误信息转换工具类
 * 
 * @author wangzhe
 *
 */
public class ErrMsgConvertUtil {
    public static String ErrMsgConvert(String errorMsg) {
	if (errorMsg.indexOf("欠停") != -1 || errorMsg.indexOf("客户下有欠费产品") != -1 || errorMsg.indexOf("已欠费") != -1) {// 停机
	    return "您的话费已欠费停机，请充值。";
	}else if (errorMsg.indexOf("余额不足") != -1 || errorMsg.indexOf("需要充值才能办理") != -1) {
	    return "您的话费余额不足，请充值。";
	}else if (errorMsg.indexOf("与[NewiFree]销售品互斥") != -1 || errorMsg.indexOf("500003141：ifree套餐组") != -1
		|| errorMsg.indexOf("受理订购业务需要具备权限") != -1) {
	    return "该流量包为NewiFree流量包，您不能购买该流量包。";
	}else if (errorMsg.indexOf("购物车校验失败") != -1 || errorMsg.indexOf("下架") != -1 || errorMsg.indexOf("库存不足或兑换码无效") != -1) {
	    return "当前活动已结束。";
	}else if (errorMsg.indexOf("dubbo") != -1 || errorMsg.indexOf("繁忙") != -1 || errorMsg.indexOf("timed out") != -1
		|| errorMsg.indexOf("开小差") != -1 || errorMsg.indexOf("超时") != -1 || errorMsg.indexOf("调用") != -1 || errorMsg.indexOf("异常") != -1
		|| errorMsg.indexOf("Exception") != -1 || errorMsg.indexOf("exception") != -1 || errorMsg.indexOf("算费") != -1
		|| errorMsg.indexOf("错误信息:.SQL代码:-1") != -1) {
	    return "订购太火爆，服务器累瘫了。请重新订购。";
	}else if (errorMsg.indexOf("获取服务角色失败") != -1 || errorMsg.indexOf("业务层服务失败") != -1
		|| errorMsg.indexOf("Content is not allowed in prolog") != -1 || errorMsg.indexOf("Index: 0, Size: 0") != -1
		|| errorMsg.indexOf("PermGen space") != -1 || errorMsg.indexOf("no entry found") != -1 || errorMsg.indexOf("余额列表为空") != -1) {
	    return "订购太火爆，服务器累瘫了。请重新订购。";
	}else if (errorMsg.indexOf("已停机") != -1 || errorMsg.indexOf("109008707762") != -1) {// 黑名单
	    return "您当前号码有欠费尚未结清，不能订购任何流量包，请致电10000查询。";
	}else if (errorMsg.indexOf("实名制验证") != -1 || errorMsg.indexOf("未实名客户") != -1) {
	    return "您当前号码没有进行实名制认证，不能订购任何流量包。";
	}else if (errorMsg.indexOf("互斥") != -1) {// 互斥
	    return "您套餐内已有流量包与当前流量包互斥，回到加流量订购其他流量包吧。";
	}else if (errorMsg.indexOf("已订购") != -1 || errorMsg.indexOf("重新订购") != -1 || errorMsg.indexOf("已经下发过") != -1) {// 重复订购
	    return "您已经订购过该流量包，不能重复订购。";
	}else if (errorMsg.indexOf("未完成的订单") != -1 || errorMsg.indexOf("在途单") != -1 || errorMsg.indexOf("未处理动作") != -1
		|| errorMsg.indexOf("撤消的业务动作") != -1) {// 有未生效订单
	    return "您有一个订购的流量包尚未生效，生效前不可订购其他流量包。";
	}else if (errorMsg.indexOf("订购次数") != -1) {
	    return "您已经超过该流量包当月订购次数。";
	}else if (errorMsg.indexOf("挂失") != -1) {
	    return "您当前号码已挂失，不能订购任何流量包。";
	}else if (errorMsg.indexOf("未激活") != -1) {
	    return "您当前号码未激活，不能订购任何流量包。";
	}else if (errorMsg.indexOf("报停") != -1) {
	    return "您当前号码已报停，不能订购任何流量包。";
	}else if (errorMsg.indexOf("仅手机用户可以购买") != -1) {
	    return "请使用电信手机号码登录。";
	}else if (errorMsg.indexOf("用户不存在") != -1 || errorMsg.indexOf("用户信息不存在") != -1 || errorMsg.indexOf("拆机权限") != -1) {
	    return "您当前号码已失效，不能订购任何流量包，请致电10000查询。";
	}else if (errorMsg.indexOf("1元包日") != -1) {
	    return "您已享有1元包天套餐优惠，不能订购任何流量包。";
	}else if (errorMsg.indexOf("一卡双号虚号码") != -1) {
	    return "您当前的号码为小号，不能订购省内流量包。";
	}else if (errorMsg.indexOf("632003729") != -1) {
	    return "该流量包为爱电视专属流量包，您不能购买该流量包。";
	}else if (errorMsg.indexOf("已受理") != -1) {
	    return "亲，您已开通该业务，不需要重复开通哦~";
	}else if (errorMsg.indexOf("没有查询到降速销售品信息") != -1) {
	    return "您的上网速率正常，无需恢复高速上网。";
	}else if(errorMsg.indexOf("[恢复高速上网服务,恢复高速上网服务]内的任意一个销售品的任意成员或特定成员，才可以订购[10元包2GB省内流量包]")!= -1){
	    return "套餐外超出流量费用达50元且开通恢复高速上网服务，才可以订购[10元包2GB省内流量包]";
	}else if(errorMsg.indexOf("是否为真4G用户，暂时不能订购")!= -1){
	    return "该服务每月仅限开通一次，您之前已开通，暂时无法订购";
	}else if(errorMsg.indexOf("只能预付费用户办理】")!= -1){
	    return "该服务只能预付费用户办理，您是后付费用户，暂时无法订购";
	}
	else {
	    return "您的流量包订购失败，详情请咨询人工客服。";
	}
    }
}

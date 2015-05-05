package com.way.util;

import java.util.HashMap;

import android.content.Context;

public class ReplyMessage{
	
	static String[] keySet = {
		"开盘价和收盘价", 	//1
		"证券交易时间", 		//2
		"如何委托买卖", 		//3
		"指定交易", 		//4
		"认购份额",		//5
		"开放式基金",		//6
		"申购新股",		//7
		"ST股票",			//8
		"新股认购中签",		//9
		"配股漏配"			//10
		};
	
	static String[] replyMessage = {
		//1   开盘价和收盘价
		"开盘价是当日第一笔交易的成交价格。  \n收盘价是当日最后一笔交易（含最后一笔交易）前一分钟所有交易的成交量加权平均价",
		//2 证券交易时间
		"每个交易日：\n9：15 —— 9：25 集合竞价（9：15～9：20可以申报和撤销；9：20～9：25可以申报，不可以撤销）\n9：30 —— 11：30 前市，连续竞价\n13：00 —— 15：00 后市，连续竞价"+
		"\n其它时间交易系统不接受申报单。（如：9：25-9：30不接受申报单和撤单）"+
		"\n对于停牌一小时的股票，在停牌期间（9：30-10：30）交易系统不接受该股票的申报单和撤单。"+
		"\n大宗交易的交易时间为本所交易日的15:00-15:30，本所在上述时间内受理大宗交易申报。"+
		"\n大宗交易用户可在交易日的14：30开始登陆本所大宗交易电子系统，进行开始前的准备工作:"+
		"大宗交易用户可在交易日的15：30-16：00通过本所大宗交易电子系统查询当天大宗交易情况或接收当天成交数据（交易规则的大宗交易从9：30开始，但尚未实施）。",
		//3 如何委托买卖
		"选择证券营业部→签定有关协议（《指定交易协议》和《证券买卖代理协议》）→开立资金帐户（存入足够的资金）→进行证券买卖。",
		//4 指定交易
		"根据２００７年５月１５日实施的《上海证券交易所指定交易实施细则》(编者注， 下同)，" +
		"指定交易是指凡在本所市场进行证券交易的投资者，必须事先指定本所市场某一交易参与人，+" +
		"作为其证券交易的唯一受托人，并由该交易参与人通过其特定 的参与者交易业务单元方可参与本所市场证券交易的制度。",
		//5 认购份额
		"在基金合同生效后的两个工作日后，投资者可以通过券商营业部查询自己的认购份额。",
		//6 开放式基金
		"开放式基金乃针对封闭式基金而言。开放式基金最大的特点是基金规模不固定。基金发行时不设规模上限，投资者可以在发行期内认购基金。+" +
		"基金成立后，投资者还可以申购或赎回基金，申购将导致基金规模的扩大，而赎回将导致基金规模的缩小。+" +
		"申购、赎回价格为基金净值加减手续费。开放式基金一般不上市交易，仅在销售网点申购赎回。",
		//7 申购新股
		"新股申购下限是1000股，且申购必须是1000股或其整数倍。新股申购上限，具体在发行公告中有规定。投资者进行新股申购时不能超过申购上限，否则被视作无效委托无法申购。",
		//8 ST股票
		"意即“特别处理”。该政策针对的对象是出现财务状况或其他状况异常的。1998年4月22日，沪深交易所宣布，+" +
		"将对财务状况或其它状况出现异常的上市公司股票交易进行特别处理(Special treatment)，由于“特别处理”，在简称前冠以“ST”，因此这类股票称为ST股。",
		//9 新股认购中签
		"可登录上证所赢富网 “新股配号中签结果查询”页面进行查询，详情请点击：\nhttp://www.sseinfo.com/。",
		//10 配股漏配
		"如果您拥有配股权，就可以自主选择配股或者放弃配股。若不愿放弃配股权，就必须在配股挂牌期参加配股，过期就作为自愿放弃处理，不能缴款补配。"
	};	
	
	
	static HashMap<String, String> replyMap = new HashMap<String, String>();
	
	static {
		for(int i=0; i< keySet.length; i++){
			replyMap.put(keySet[i], replyMessage[i]);
		}
		
	}
	
	public static boolean needAutoReply(Context context) {
		// TODO Auto-generated method stub
		if(PreferenceUtils.getPrefString(context, PreferenceConstants.ACCOUNT, null).equals(PreferenceConstants.DEFAULT_JABBER)){
			return true;
		}else{
			return false;
		}
			
	}

	public static String getReplyMessage(String incommingMsg) {
		// TODO Auto-generated method stub
		
		int i;
		
		for(i=0; i<keySet.length; i++){
			if(incommingMsg.contains(keySet[i])){
				break;
			}		
		}
		
		if(i == keySet.length){
			return "(☆＿☆) 亲爱的，火星文jimi还不会呢，您可以教教我么";
		}else{
			return replyMap.get(keySet[i]);
		}
	}

}

package com.sumslack.thrift.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sumslack.common.sign.SignManager;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;

public class CloudAPILaunch {
	public static void main(String[] args) {
		String app_id = "894344910875";
		String app_seckey = "9ee3b6066bb55ac7aee3476dec29afbb";
		String time_stamp=new Date().getTime()/1000 + "";
		String nonce_str = RandomUtil.randomNumbers(8);
		Map<String,String> params = new HashMap();
		params.put("app_id", app_id);
		params.put("time_stamp", time_stamp);
		params.put("nonce_str", nonce_str);
		String sign = SignManager.getIntance().getSign(app_seckey, params);
		params.put("sign", sign);
		String url = "http://wstest.idbhost.com/finIndex/seg/standard";
		//业务接口参数列表
		params.put("t", "我们都是社会主义接班人");
		String ret = HttpUtil.post(url, SignManager.getIntance().formatParam2Map(params));
		System.out.println(ret);
	}
}

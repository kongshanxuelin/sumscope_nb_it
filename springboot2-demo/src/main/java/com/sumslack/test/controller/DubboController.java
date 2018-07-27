package com.sumslack.test.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sumslack.cloud.api.BondService;
import com.sumslack.cloud.bean.BondDetailBean;
@RestController()
@RequestMapping("/api/v2")
public class DubboController {
	@Reference(version = "2.0.1")
	private BondService bondService;
	
	@GetMapping(value="dubbo")
	public @ResponseBody Map testDubbo(HttpServletResponse response) {
		Map retMap = new HashMap();
		retMap.put("ret", true);
		BondDetailBean bond = bondService.getBondDetailByBondKey("A0000012012CORLEB01");
		retMap.put("bond", bond);
		return retMap;
	}
}

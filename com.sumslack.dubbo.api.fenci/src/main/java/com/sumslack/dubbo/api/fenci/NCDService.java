package com.sumslack.dubbo.api.fenci;

import java.util.Map;

public interface NCDService {
	/**
	 * 将一段文字转换成结构化的NCD报价
	 * @param text
	 * @return
	 */
	public Map analysis(String text);
}

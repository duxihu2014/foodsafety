package com.otec.foodsafety.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtils {
	// private static final Logger LOG =
	// LoggerFactory.getLogger(JSONUtils.class);

	private static SerializerFeature[] features = new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat };

	public static String toJson(Object target) {
		return JSON.toJSONString(target, features);
	}

	/**
	 * 灏嗗璞″睘鎬у?涓簄ull鐨勯粯璁よ緭鍑轰负""
	 * @param target
	 * @param writeNull
	 * @return
	 */
	public static String toJson(Object target, boolean writeNull) {
		if (writeNull) {
			ValueFilter filter = new ValueFilter() {

				@Override
				public Object process(Object object, String name, Object value) {
					if (value == null) {
						return "";
					}
					return value;
				}

			};

			return JSON.toJSONString(target, filter);
		} else {
			return toJson(target);
		}

	}

	public static List<String> toCombo(Map<String, String> map) {
		List<String> list = new ArrayList<String>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String str = "{\"text\":\"" + entry.getValue() + "\",\"value\":\""
					+ entry.getKey() + "\"}";
			list.add(str);
		}
		return list;
	}
	
	public static List<Map<String,String>> toComboList(Map<String,String> map){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			Map<String,String> item = new HashMap<String,String>();
			item.put("text", entry.getValue());
			item.put("value", entry.getKey());
			list.add(item);
			
		}
		return list;
		
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		return JSON.parseObject(json, clazz);
	}

	public static final <T> List<T> parseArray(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz);
	}

	public static void main(String args[]) {
		/*
		 * Map map = new HashMap<String,String>(); map.put("db", "1982-01-1");
		 * map.put("1b", "1982-01-1"); System.out.println(toJson(map));
		 * System.out.println(toJson(toCombo(map)));
		 */
	}
}

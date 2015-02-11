package com.liuruoqiao.helloweather.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonTool {

	/**
	 * @param key
	 * @param result
	 * @return 实况的数据
	 */
	public static List<String> getList(String key, String result) {
		List<String> list = new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject jsonObject2 = jsonObject.getJSONObject(key);
			String realtime_weather = jsonObject2.getString("l1");
			list.add(realtime_weather);
			String realtime_humidity = jsonObject2.getString("l2");
			list.add(realtime_humidity);
			String realtime_wind = jsonObject2.getString("l3");
			list.add(realtime_wind);
			String wind_direction = jsonObject2.getString("l4");
			list.add(wind_direction);
			String realtime = jsonObject2.getString("l7");
			list.add(realtime);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}

	/**
	 * @param key
	 * @param jsonString
	 * @return 天气指数的数据 返回的json数据： "i":[ { "i1":"ct", "i2":"穿衣指数", “i3”:”” ,
	 *         "i4":"冷", }, { "i1":"xc", "i2":"洗车指数", “i3”:”” , "i4":"适宜", } ]
	 */
	public static List<Map<String, String>> getListMap(String key,
			String jsonString) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {

			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray(key);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				Iterator iterator = jsonObject2.keys();
				while (iterator.hasNext()) {
					String json_key = (String) iterator.next();
					String json_value = (String) jsonObject2.get(json_key);
					if (json_value == null) {
						json_value = "";
					}
					map.put(json_key, json_value);
				}
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * @param key
	 * @param secondary_key
	 * @param jsonString
	 * @return 预报的数据 返回的json数据： "f":{ "f0":"201203061100", "f1":[ {//第一天预报数据
	 *         "fa":"01", "fb":"01", "fc":"11", "fd":"0", "fe":"4", "ff":"4",
	 *         "fg":"1", "fh":"0", "fi":"06:44|18:21" }, {//第二天预报数据 . . . . }]}
	 */
	public static List<Map<String, String>> getForecastListMap(String key,
			String secondary_key, String jsonString) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONObject jsonObject_f = jsonObject.getJSONObject(key);
			JSONArray jsonArray = jsonObject_f.getJSONArray(secondary_key);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				Iterator iterator = jsonObject2.keys();
				while (iterator.hasNext()) {
					String json_key = (String) iterator.next();
					String json_value = (String) jsonObject2.get(json_key);
					if (json_value == null) {
						json_value = "";
					}
					map.put(json_key, json_value);
				}
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

}

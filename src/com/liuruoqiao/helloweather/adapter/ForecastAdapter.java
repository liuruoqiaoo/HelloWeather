package com.liuruoqiao.helloweather.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.helloweather.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Ruoqiao 预报list的适配器
 * 
 */
public class ForecastAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, String>> result_list;
	private List<Map<String, String>> weather_list;
	private LayoutInflater inflater;
	private String[] wind_direction_arr = new String[] { "无持续风向", "东北风", "东风",
			"东南风", "南风", "西南风", "西风", "西北风", "北风", "旋转风" };
	private String[] day_arr = new String[] { "今", "明", "后" };
	private String[] weather_arr = new String[] { "晴", "多云", "阴", "阵雨", "雷阵雨",
			"雷阵雨伴有冰雹", "雨夹雪", "小雨", "中雨", "大雨", "暴雨", "大暴雨", "特大暴雨", "阵雪",
			"小雪", "中雪", "大雪", "暴雪", "冻雨", "沙尘暴", "小到中雨", "中到大雨", "大到暴雨",
			"暴雨到大暴雨", "大暴雨到特大暴雨", "小到中雪", "中雪到大雪", "大学到暴雪", "浮尘", "扬沙", "强沙尘暴" };

	public ForecastAdapter(Context context,
			List<Map<String, String>> result_list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.result_list = result_list;
		inflater = LayoutInflater.from(context);
		initWeatherList();

	}

	/**
	 * 天气现象有对应的key和vlaue，解析后获得key，要通过key找到value
	 */
	private void initWeatherList() {
		// TODO Auto-generated method stub
		weather_list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < weather_arr.length; i++) {
			if (i < 10)
				map.put("0" + Integer.toString(i), weather_arr[i]);
			else
				map.put(Integer.toString(i), weather_arr[i]);
		}
		map.put("53", "霾");
		map.put("99", "无");
		weather_list.add(map);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return result_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return result_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class ViewHolder {
		TextView day, day_temp, day_weather, night_wind, night, night_temp,
				night_weather, day_wind;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			// 将xml实例化为view对象
			convertView = inflater.inflate(R.layout.forecastlist_show, null);
			holder = new ViewHolder();
			holder.day = (TextView) convertView.findViewById(R.id.day);
			holder.day_temp = (TextView) convertView
					.findViewById(R.id.day_temp);
			holder.day_weather = (TextView) convertView
					.findViewById(R.id.day_weather);
			holder.day_wind = (TextView) convertView
					.findViewById(R.id.day_wind);
			holder.night = (TextView) convertView.findViewById(R.id.night);
			holder.night_temp = (TextView) convertView
					.findViewById(R.id.night_temp);
			holder.night_weather = (TextView) convertView
					.findViewById(R.id.night_weather);
			holder.night_wind = (TextView) convertView
					.findViewById(R.id.night_wind);
			convertView.setTag(holder);

		} else {
			// 加快加载速度，不用每次重复加载界面
			holder = (ViewHolder) convertView.getTag();
		}
		// 6点过后，白天的预报为空，显示无法获取白天天气
		if (position == 0 && result_list.get(position).get("fc").equals("")) {

			holder.day.setText("今日白天已过，无法获得相应数据");
			holder.day_temp.setText("");
			holder.day_weather.setText("");
			holder.day_wind.setText("");
		} else {
			holder.day.setText(day_arr[position] + "日白天");
			holder.day_temp.setText(result_list.get(position).get("fc") + "°");
			holder.day_weather.setText(getWeatherText(position, "fa"));
			holder.day_wind.setText(getWindText(position, "fe", "fg"));
		}
		holder.night.setText(day_arr[position] + "日夜间");
		holder.night_temp.setText(result_list.get(position).get("fd") + "°");
		holder.night_weather.setText(getWeatherText(position, "fb"));
		holder.night_wind.setText(getWindText(position, "ff", "fh"));

		return convertView;
	}

	private String getWeatherText(int position, String string) {
		// TODO Auto-generated method stub
		String weather = weather_list.get(0).get(
				result_list.get(position).get(string));
		return weather;
	}

	private String getWindText(int position, String direction, String force) {
		// TODO Auto-generated method stub
		String wind_direction = wind_direction_arr[Integer.parseInt(result_list
				.get(position).get(direction))];
		String wind = wind_direction + result_list.get(position).get(force)
				+ "级";
		return wind;
	}

}

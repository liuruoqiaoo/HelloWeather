package com.liuruoqiao.helloweather.ui;

import java.util.Calendar;
import java.util.List;

import com.example.helloweather.R;
import com.liuruoqiao.helloweather.http.HttpClientGet;
import com.liuruoqiao.helloweather.http.NetWorkCheck;
import com.liuruoqiao.helloweather.showlist.ShowList;
import com.liuruoqiao.helloweather.utils.JsonTool;
import com.liuruoqiao.helloweather.utils.UrlUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity {
	private TextView cityName, realtime_weather, realtime_humidity, date,
			realtime, realtime_wind;
	private ListView forecast_list;
	private Button btn_life, btn_change;
	private static final int CHANGECITY = 2;
	private String city_id = "101280101";// 默认广州
	private int city_num = 4;// 默认广州
	private String url_observe;
	private Toast mToast = null;
	private NetWorkCheck net;
	private String[] wind_direction_arr = new String[] { "无持续风向", "东北风", "东风",
			"东南风", "南风", "西南风", "西风", "西北风", "北风", "旋转风" };
	private String[] week_day = new String[] { "天", "一", "二", "三", "四", "五",
			"六" };
	private String[] city_url = new String[] { "101010100", "101030100",
			"101020100", "101040100", "101280101", "101210101", "101310201",
			"101190401", "101230201", "101110101", "101120201", "101190101",
			"101050101", "101070101" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_weather);
		init();
		// 防止突然断网，导致无法网络连接而报错
		net = new NetWorkCheck(WeatherActivity.this);
		if (net.isNetWorkAvailable()) {
			MyTask myTaskObserve = new MyTask(WeatherActivity.this);
			// 获得url
			url_observe = UrlUtils.getInterfaceURL(city_id, "observe");
			myTaskObserve.execute(url_observe);// 执行异步任务
			btn_life.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(WeatherActivity.this,
							LifeActivity.class);
					// 跳转到“生活指数”界面，并传递相关的值
					Bundle bundle = new Bundle();
					bundle.putString("city_id", city_id);
					bundle.putInt("city_num", city_num);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			btn_change.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 跳转到“热门城市”界面，获取对应的城市值返回本界面
					Intent intent = new Intent(WeatherActivity.this,
							ChangeCityActivity.class);
					startActivityForResult(intent, CHANGECITY);
				}
			});
			// ShowList是专门构造listView内容的一个类
			ShowList showList = new ShowList(WeatherActivity.this,
					forecast_list, city_id);
			showList.showForecastList();
		} else {
			showToast("当前网络不可用，请检查网络设置");
		}

	}

	private void init() {
		// TODO Auto-generated method stub
		realtime_weather = (TextView) findViewById(R.id.realtime_weather);
		realtime = (TextView) findViewById(R.id.realtime);
		realtime_wind = (TextView) findViewById(R.id.realtime_wind);
		realtime_humidity = (TextView) findViewById(R.id.realtime_humidity);
		date = (TextView) findViewById(R.id.date);
		cityName = (TextView) findViewById(R.id.city_name);
		forecast_list = (ListView) findViewById(R.id.forecast_list);
		btn_life = (Button) findViewById(R.id.button_life);
		btn_change = (Button) findViewById(R.id.button_change);
	}

	/**
	 * @author Ruoqiao 异步任务，发送请求以及对返回结果处理
	 * 
	 */
	private class MyTask extends AsyncTask<String, Integer, String> {

		private Context context;

		public MyTask(Context context) {
			this.context = context;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return HttpClientGet.HttpClientGet(context, params[0]);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showToast("正在加载天气数据，请稍后");
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result.equals("data error") || result == null) {
				Log.i("ShowList", "实况的后结果报错" + result);
			} else {
				List<String> result_list = JsonTool.getList("l", result);
				setRealText(result_list);
			}

		}

	}

	/**
	 * @param string
	 *            由于toast会被重复使用多次，这样写不用次次都穿context
	 */
	private void showToast(String string) {
		// TODO Auto-generated method stub
		if (mToast == null) {
			mToast = Toast.makeText(WeatherActivity.this, string,
					Toast.LENGTH_SHORT);
		} else {
			mToast.setText(string);
		}
		mToast.show();
	}

	/**
	 * @param result_list
	 *            返回json解析后的结果后，对相应的textView进行设置
	 */
	private void setRealText(List<String> result_list) {
		// TODO Auto-generated method stub
		realtime_weather.setText(result_list.get(0) + "°");
		realtime.setText(result_list.get(4) + "发布");
		realtime_humidity.setText("湿度" + result_list.get(1) + "%");
		String wind_direction = wind_direction_arr[Integer
				.parseInt((String) result_list.get(3))];
		realtime_wind.setText(wind_direction + result_list.get(2) + "级");
		date.setText(getDateString());
	}

	/**
	 * @return 获得系统的日期
	 */
	private String getDateString() {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		String date = Integer.toString(year) + "年" + Integer.toString(month)
				+ "月" + Integer.toString(day) + "日" + "星期" + week_day[week];
		return date;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == 2) {
			Bundle bundle = data.getExtras();
			city_num = bundle.getInt("city_num");
			city_id = city_url[city_num];
			net = new NetWorkCheck(WeatherActivity.this);
			if (net.isNetWorkAvailable()) {
			renewWeather(bundle.getString("city"), city_num);
			} else {
				showToast("当前网络不可用，请检查网络设置");
			}
		}
	}

	/**
	 * @param string
	 * @param city_num
	 *            选择城市后，界面的信息要更新
	 */
	private void renewWeather(String string, int city_num) {
		// TODO Auto-generated method stub

		cityName.setText(string);
		// 因为城市不同，需要重新得到一个url
		url_observe = UrlUtils.getInterfaceURL(city_url[city_num], "observe");
		MyTask myTaskChangeCity = new MyTask(WeatherActivity.this);
		myTaskChangeCity.execute(url_observe);
		// 相对的预报list的信息也要更换
		ShowList showList2 = new ShowList(WeatherActivity.this, forecast_list,
				city_id);
		showList2.showForecastList();
	}

}
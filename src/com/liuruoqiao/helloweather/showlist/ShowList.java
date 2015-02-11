package com.liuruoqiao.helloweather.showlist;

import java.util.List;
import java.util.Map;

import com.liuruoqiao.helloweather.adapter.ForecastAdapter;
import com.liuruoqiao.helloweather.http.HttpClientGet;
import com.liuruoqiao.helloweather.utils.JsonTool;
import com.liuruoqiao.helloweather.utils.UrlUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ShowList {
	private Context context;
	private ListView list;
	private String city_id;
	private ForecastAdapter adapter;
	private List<Map<String, String>> result_list;

	/**
	 * @param context
	 *            weatheractivity的上下文
	 * @param forecast_list
	 *            weatheractivity中的listview
	 * @param city_id
	 *            weatheractivity此时的city编号
	 */
	public ShowList(Context context, ListView forecast_list, String city_id) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = forecast_list;
		this.city_id = city_id;
	}

	/**
	 * weatheractivity中调用此方法来展示预报list
	 */
	public void showForecastList() {
		String url_forecast = UrlUtils.getInterfaceURL(city_id, "forecast_f");
		MyTask myForecastTask = new MyTask(context);
		myForecastTask.execute(url_forecast);

	}

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
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			result_list = JsonTool.getForecastListMap("f", "f1", result);
			if (result_list != null) {
				// 将解析好之后的result_list传给适配器
				adapter = new ForecastAdapter(context, result_list);
				list.setAdapter(adapter);
				System.out.println(result_list);
			} else {
				Log.i("ShowList", "预报的list为空");
			}
		}

	}

}

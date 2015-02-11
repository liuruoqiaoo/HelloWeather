package com.liuruoqiao.helloweather.ui;

import java.util.List;
import java.util.Map;

import com.example.helloweather.R;
import com.liuruoqiao.helloweather.http.HttpClientGet;
import com.liuruoqiao.helloweather.http.NetWorkCheck;
import com.liuruoqiao.helloweather.utils.JsonTool;
import com.liuruoqiao.helloweather.utils.UrlUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class LifeActivity extends Activity {
	private MyTask myTask;
	private String city_id;
	private Toast mToast;
	private int city_num;
	private TextView index_cloth, index_comfortable, index_train,
			index_cloth_grade, index_comfortable_grade, index_train_grade,
			index_train_content, index_comfortable_content,
			index_cloth_content, cityName;
	private String[] city_name = new String[] { "北京", "天津", "上海", "重庆", "广州",
			"杭州", "三亚", "苏州", "厦门", "西安", "青岛", "南京", "哈尔滨", "沈阳"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_life);
		init();
		// 防止突然断网，导致无法网络连接而报错
		NetWorkCheck net = new NetWorkCheck(LifeActivity.this);
		if (net.isNetWorkAvailable()) {
			initCityIdAndNum();
			cityName.setText(city_name[city_num]);
			myTask = new MyTask(LifeActivity.this);
			String url_index = UrlUtils.getInterfaceURL(city_id, "index_f");
			myTask.execute(url_index);
		} else {
			showToast("当前网络不可用，请检查网络设置");
		}

	}

	private void init() {
		// TODO Auto-generated method stub
		cityName = (TextView) findViewById(R.id.index_city);
		index_cloth = (TextView) findViewById(R.id.index_cloth);
		index_comfortable = (TextView) findViewById(R.id.index_comfortable);
		index_train = (TextView) findViewById(R.id.index_train);
		index_cloth_grade = (TextView) findViewById(R.id.index_cloth_grade);
		index_comfortable_grade = (TextView) findViewById(R.id.index_comfortable_grade);
		index_train_grade = (TextView) findViewById(R.id.index_train_grade);
		index_train_content = (TextView) findViewById(R.id.index_train_content);
		index_comfortable_content = (TextView) findViewById(R.id.index_comfortable_content);
		index_cloth_content = (TextView) findViewById(R.id.index_cloth_content);
	}

	/**
	 * 获得上一个activity传递过来的信息
	 */
	private void initCityIdAndNum() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		city_id = bundle.getString("city_id");
		city_num = bundle.getInt("city_num");
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
			showToast("正在加载生活指数数据，请稍后");
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			List<Map<String, String>> result_list = JsonTool.getListMap("i",
					result);
			System.out.println("指数" + result_list);
			if (result_list != null)
				setIndexText(result_list);

		}

	}

	/**
	 * @param result_list
	 *            设置对应的textView的内容，用key获得value
	 */
	private void setIndexText(List<Map<String, String>> result_list) {
		// TODO Auto-generated method stub
		String star = "★";
		String star_grade = "★指数级别:";
		index_cloth.setText(result_list.get(0).get("i2"));
		index_cloth_grade.setText(star_grade + result_list.get(0).get("i4"));
		index_cloth_content.setText(star + result_list.get(0).get("i5"));
		index_train.setText(result_list.get(1).get("i2"));
		index_train_grade.setText(star_grade + result_list.get(1).get("i4"));
		index_train_content.setText(star + result_list.get(1).get("i5"));
		index_comfortable.setText(result_list.get(2).get("i2"));
		index_comfortable_grade.setText(star_grade
				+ result_list.get(2).get("i4"));
		index_comfortable_content.setText(star + result_list.get(2).get("i5"));

	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		if (mToast == null) {
			mToast = Toast.makeText(LifeActivity.this, string,
					Toast.LENGTH_SHORT);
		} else {
			mToast.setText(string);
		}
		mToast.show();
	}

}

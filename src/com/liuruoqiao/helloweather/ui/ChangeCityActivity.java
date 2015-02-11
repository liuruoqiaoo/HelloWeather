package com.liuruoqiao.helloweather.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.helloweather.R;
import com.liuruoqiao.helloweather.adapter.CityAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChangeCityActivity extends Activity {

	private ListView city;
	private CityAdapter adapter;
	private List<String> city_list;
	private String[] city_name = new String[] { "北京", "天津", "上海", "重庆", "广州",
			"杭州", "三亚", "苏州", "厦门", "西安", "青岛", "南京", "哈尔滨", "沈阳" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);  

		setContentView(R.layout.change_city);
		city = (ListView) findViewById(R.id.list_city);
		city_list = new ArrayList<String>();
		for (String ss : city_name) {
			city_list.add(ss);
		}
		adapter = new CityAdapter(this, city_list);
		city.setAdapter(adapter);// 使用适配器展示listView
		city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				/*
				 * Toast.makeText(ChangeCity.this, "选择" + city_name[position],
				 * Toast.LENGTH_SHORT).show();
				 */
				// 把数据传回给原来的activity
				Intent intent = getIntent();
				Bundle bundle = new Bundle();
				bundle.putString("city", city_name[position]);
				bundle.putInt("city_num", position);
				intent.putExtras(bundle);
				ChangeCityActivity.this.setResult(2, intent);
				ChangeCityActivity.this.finish();
			}
		});
	}

}

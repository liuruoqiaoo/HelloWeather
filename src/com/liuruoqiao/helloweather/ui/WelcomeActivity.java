package com.liuruoqiao.helloweather.ui;

import com.example.helloweather.R;
import com.liuruoqiao.helloweather.http.NetWorkCheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class WelcomeActivity extends Activity {
	TextView net_warning;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		handler = new Handler();
		start();
		net_warning = (TextView) findViewById(R.id.wel_text);

	}

	private void start() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {

				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 前往主界面之前要判断网络是否可以使用
				NetWorkCheck net = new NetWorkCheck(WelcomeActivity.this);
				if (net.isNetWorkAvailable()) {
					Intent intent = new Intent(WelcomeActivity.this,
							WeatherActivity.class);
					startActivity(intent);
					finish();// 这是用来结束activity的，这个必须写，不然点返回键的时候就会返回到这里来了
				} else {
					handler.post(runnableUi);
				}
			}

		}.start();
	}

	// 构建Runnable对象，在runnable中更新界面
	Runnable runnableUi = new Runnable() {
		@Override
		public void run() {
			// 更新界面
			net_warning.setVisibility(View.VISIBLE);
		}

	};

}

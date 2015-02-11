package com.liuruoqiao.helloweather.adapter;

import java.util.List;

import com.example.helloweather.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CityAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;
	private LayoutInflater inflate;

	public CityAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
		inflate = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class ViewHolder {
		TextView text;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LinearLayout.inflate(context, R.layout.list_show,
					null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView
					.findViewById(R.id.list_show_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setText(list.get(position));
		return convertView;
	}

}

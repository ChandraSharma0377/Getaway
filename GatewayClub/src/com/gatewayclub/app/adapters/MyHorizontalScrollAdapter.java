package com.gatewayclub.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gatewayclub.app.R;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.pojos.PropertyImageDto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyHorizontalScrollAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<PropertyImageDto> imalelist;
	private Context context;

	public MyHorizontalScrollAdapter(Context context, ArrayList<PropertyImageDto> imalelist) {

		this.imalelist = imalelist;
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;

		if (convertView == null) {
			// Inflate the view since it does not exist
			convertView = mInflater.inflate(R.layout.list_item, parent, false);

			// Create and save off the holder in the tag so we get quick access
			// to inner fields
			// This must be done for performance reasons
			holder = new Holder();
			holder.ivscroll = (ImageView) convertView.findViewById(R.id.iv_thumb);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Picasso.with(context).load(Commons.IMAGE_BASE_URL+imalelist.get(position).getPropertyImageUrl()).resize(100,100).into(holder.ivscroll);
		convertView.setId(position);

		return convertView;
	}

	/** View holder for the views we need access to */
	private static class Holder {
		public ImageView ivscroll;
	}

	@Override
	public int getCount() {
		return imalelist.size();
	}

	@Override
	public Object getItem(int position) {
		return imalelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
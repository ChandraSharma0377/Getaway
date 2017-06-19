package com.gatewayclub.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gatewayclub.app.R;
import com.gatewayclub.app.pojos.GridItem;

import java.util.ArrayList;

public class GridViewImgTxtAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();

	public GridViewImgTxtAdapter(Context mContext, ArrayList<GridItem> mGridData) {
		this.mContext = mContext;
		this.mGridData = mGridData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(R.layout.grid_item_img_txt_layout, parent, false);
			holder = new ViewHolder();
			holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
			holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		GridItem item = mGridData.get(position);
		holder.titleTextView.setText(item.getTitle());
		holder.imageView.setImageResource(item.getImageID());
		return row;
	}

	static class ViewHolder {
		TextView titleTextView;
		ImageView imageView;
	}

	@Override
	public int getCount() {
		return mGridData.size();
	}

	@Override
	public Object getItem(int position) {
		return mGridData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
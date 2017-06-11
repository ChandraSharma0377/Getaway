package com.gatewayclub.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gatewayclub.app.R;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.pojos.GridItemDto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<GridItemDto> {

	private Context mContext;
	private int layoutResourceId;
	private ArrayList<GridItemDto> mGridData = new ArrayList<GridItemDto>();

	public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<GridItemDto> mGridData) {
		super(mContext, layoutResourceId, mGridData);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.mGridData = mGridData;
	}
	public void setGridData(ArrayList<GridItemDto> mGridData) {
		this.mGridData = mGridData;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
			holder.titleTextView=(TextView)row.findViewById(R.id.grid_item_title);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		GridItemDto item = mGridData.get(position);
		if(layoutResourceId==R.layout.grid_item_img_txt_layout)
			Picasso.with(mContext).load(item.getImage()).resize(140,140).into(holder.imageView);
		else
		Picasso.with(mContext).load(Commons.IMAGE_BASE_URL+item.getImage()).resize(140,140).into(holder.imageView);
		holder.titleTextView.setText(item.getTitle());
		return row;
	}

	static class ViewHolder {
		TextView titleTextView;
		ImageView imageView;
	}
}
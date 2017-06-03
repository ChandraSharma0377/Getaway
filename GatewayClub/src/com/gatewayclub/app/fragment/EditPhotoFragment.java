
package com.gatewayclub.app.fragment;

import java.util.ArrayList;

import com.gatewayclub.app.R;
import com.gatewayclub.app.adapters.GridViewAdapter;
import com.gatewayclub.app.helper.ExpandableHeightGridView;
import com.gatewayclub.app.pojos.GridItemDto;
import com.gatewayclub.app.pojos.PropertyDto;
import com.gatewayclub.app.pojos.PropertyImageDto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

public class EditPhotoFragment extends BaseFragment {

	private ExpandableHeightGridView mGridView;
	private ArrayList<PropertyImageDto> imagelist = new ArrayList<PropertyImageDto>();
	private GridViewAdapter mGridAdapter;
	private ArrayList<GridItemDto> mGridData;

	public EditPhotoFragment(ArrayList<PropertyImageDto> imagelist) {
		this.imagelist=imagelist;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
		super.onCreateView(inflater, container, args);
		View view = inflater.inflate(R.layout.frag_editphoto, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		mGridView = (ExpandableHeightGridView) view.findViewById(R.id.gridView);
		mGridData = new ArrayList<>();
		for (int i = 0; i < imagelist.size(); i++) {
			GridItemDto item = new GridItemDto();
			item.setTitle("");
			item.setImage(imagelist.get(i).getPropertyImageUrl());
			mGridData.add(item);
		}
		// Initialize with empty data
		mGridView.setExpanded(true);
		mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
		mGridView.setAdapter(mGridAdapter);
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				// Get item at position
				GridItemDto item = (GridItemDto) parent.getItemAtPosition(position);

				// Pass the image title and url to DetailsActivity
				// Intent intent = new Intent(MainActivity.this,
				// DetailsActivity.class);
				// intent.putExtra("title", item.getTitle());
				// intent.putExtra("image", item.getImage());
				//
				// //Start details activity
				// startActivity(intent);
			}
		});

		addView(view);
		return rootview;
	}

	@Override
	protected int getSelectedID() {
		return iv_edit.getId();
	}

	@Override
	protected void propertySelected(PropertyDto pdto) {
		// TODO Auto-generated method stub
		
	}
}

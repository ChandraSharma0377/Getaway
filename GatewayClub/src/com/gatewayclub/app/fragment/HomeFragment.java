package com.gatewayclub.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gatewayclub.app.R;
import com.gatewayclub.app.adapters.GridViewImgTxtAdapter;
import com.gatewayclub.app.adapters.PropertySpinAdapter;
import com.gatewayclub.app.adapters.LocationSpinAdapter;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ExpandableHeightGridView;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.main.MainActivityOptions;
import com.gatewayclub.app.pojos.GridItem;
import com.gatewayclub.app.pojos.LocationDto;
import com.gatewayclub.app.pojos.PropertyDto;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class HomeFragment extends Fragment implements View.OnClickListener {
	private ProgressDialog progressDialog;
	protected ImageView iv_back, iv_cross, iv_book_sum;
	protected View rootview;
	private Spinner sp_location, sp_property;
	private ExpandableHeightGridView mGridView;
	private GridViewImgTxtAdapter mGridAdapter;
	private ArrayList<GridItem> mGridData;
	private TextView tv_name,tv_pending,tv_total_count,tv_mobile;
	private ArrayList<PropertyDto> propertyList = new ArrayList<PropertyDto>();
	private ImageView iv_thumb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

		rootview = inflater.inflate(R.layout.frag_home, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		iv_thumb = (ImageView) rootview.findViewById(R.id.iv_thumb);
		iv_back = (ImageView) rootview.findViewById(R.id.iv_back);
		iv_cross = (ImageView) rootview.findViewById(R.id.iv_cross);
		iv_book_sum = (ImageView) rootview.findViewById(R.id.iv_book_sum);
		tv_name = (TextView) rootview.findViewById(R.id.tv_name);
		tv_mobile = (TextView) rootview.findViewById(R.id.tv_mobile);
		tv_total_count = (TextView) rootview.findViewById(R.id.tv_total_count);
		tv_pending = (TextView) rootview.findViewById(R.id.tv_pending);
		tv_name.setText("Welcome " + MainActivity.getMainScreenActivity().getUserName());
		tv_mobile.setText( MainActivity.getMainScreenActivity().getMobileNo());
		tv_total_count.setText("Total business till now : " + MainActivity.getMainScreenActivity().getApproveCount());
		tv_pending.setText("Awaiting approval : " + MainActivity.getMainScreenActivity().getPendingCount());
		iv_back.setOnClickListener(this);
		iv_cross.setOnClickListener(this);
		iv_book_sum.setOnClickListener(this);

		mGridView = (ExpandableHeightGridView) rootview.findViewById(R.id.gridView);
		intialzeGrid();
		sp_location = (Spinner) rootview.findViewById(R.id.sp_location);
		sp_property = (Spinner) rootview.findViewById(R.id.sp_property);
		sp_property.setEnabled(false);
		sp_property.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				TextView selectedText = (TextView) parent.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.WHITE);
				}
				if (position > 0) {
					Picasso.with(getActivity())
							.load(Commons.IMAGE_BASE_URL + propertyList.get(position).getPropertyImage())
							.resize(100, 100).into(iv_thumb);
				}

			}

		});
		sp_location.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				TextView selectedText = (TextView) parent.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.WHITE);
				}
				if (position > 0) {

					HashMap<String, String> postDataParams = new HashMap<String, String>();
					postDataParams.put("pmOwnerID", MainActivity.getMainScreenActivity().getUserID());
					postDataParams.put("lmID", Commons.locationLists.get(position).getLocationID());
					new LocationTask(postDataParams, false).execute(Commons.GET_PROPERTY_LIST);
					sp_property.setEnabled(true);
				} else {
					sp_property.setEnabled(false);
					initPropertySpinner();
				}

			}

		});

		// Spinner Drop down elements
		HashMap<String, String> postDataParams = new HashMap<String, String>();
		postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
		new LocationTask(postDataParams, true).execute(Commons.GET_LOCATION_LIST);
		initPropertySpinner();

		return rootview;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_cross:
			new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
					.setMessage("Are you sure you want to exit?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MainActivity.getMainScreenActivity().finish();
						}
					}).setNegativeButton("No", null).show();

			break;
		case R.id.iv_back:
			//MainActivity.getMainScreenActivity().onBackPressed();
			new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Logout")
			.setMessage("Are you sure you want to logout?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MainActivity.getSharePreferance().edit().clear().commit();
					MainActivity.getMainScreenActivity().finish();
				}
			}).setNegativeButton("No", null).show();
			break;
		case R.id.iv_book_sum:
			Intent in =new Intent(getActivity(),MainActivityOptions.class);
			in.putExtra("CLASSNAME", -1);
			startActivity(in);
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	private void intialzeGrid() {
		mGridView.setExpanded(true);
		mGridData = new ArrayList<>();

		mGridData.add(new GridItem(getString(R.string.books), R.drawable.book_unselected));
		mGridData.add(new GridItem(getString(R.string.calander), R.drawable.calender_unselected));
		mGridData.add(new GridItem(getString(R.string.alert), R.drawable.alerts_unselected));
		mGridData.add(new GridItem(getString(R.string.edit), R.drawable.edit_unselected));
		mGridData.add(new GridItem(getString(R.string.creivew), R.drawable.customer_review_unselected));
		mGridData.add(new GridItem(getString(R.string.agent), R.drawable.agents_unselected));

		mGridAdapter = new GridViewImgTxtAdapter(getActivity(), mGridData);
		mGridView.setAdapter(mGridAdapter);
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				int pos = position;
				Intent in =new Intent(getActivity(),MainActivityOptions.class);
				in.putExtra("CLASSNAME", pos);
				startActivity(in);
				
				
			}
		});
	}

	private class LocationTask extends AsyncProcess {
		private boolean isLocationSpinner = false;

		public LocationTask(HashMap<String, String> postDataParams, boolean isLocationSpinner) {
			super(postDataParams);
			this.isLocationSpinner = isLocationSpinner;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(getActivity(), "", "please wait...");
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setOnCancelListener(cancelListener);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (200 == responseCode) {

				String value = result.replace("\\", "");
				if (value.length() > 2)
					value = value.substring(1, value.length() - 1);
				try {
					JSONArray jaaray = new JSONArray(value);
					if (isLocationSpinner) {
						Commons.locationLists.clear();
						for (int i = 0; i < jaaray.length(); i++) {
							try {
								JSONObject jo = jaaray.getJSONObject(i);
								Commons.locationLists
										.add(new LocationDto(jo.getString("lmId"), jo.getString("lmlocation")));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						Commons.locationLists.add(0, new LocationDto("-1", "Select Location"));
						LocationSpinAdapter dataAdapter = new LocationSpinAdapter(getActivity(), Commons.locationLists);
						sp_location.setAdapter(dataAdapter);
						if (Commons.locationLists.size() == 2)
							sp_location.setSelection(1);
					} else {
						propertyList.clear();
						for (int i = 0; i < jaaray.length(); i++) {
							try {
								JSONObject jo = jaaray.getJSONObject(i);
								propertyList.add(new PropertyDto(jo.getString("pmId"), jo.getString("pmAddress"),
										jo.getString("pmImage"), jo.getString("MinimumCapacity"),
										jo.getString("pmMaxOccupancy")));
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
						propertyList.add(0, new PropertyDto("-1", "Select Property", "", "", ""));
						PropertySpinAdapter propertyAdapter = new PropertySpinAdapter(getActivity(), propertyList);
						sp_property.setAdapter(propertyAdapter);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (isLocationSpinner) {

					} else {
						initPropertySpinner();
					}
					ShowAlertInformation.showDialog(getActivity(), "Error", "No data found");
					progressDialog.dismiss();
				}
				System.out.println("LocationTask result is : " + (result == null ? "" : result));
				progressDialog.dismiss();
			} else {
				Log.i("LocationTask response", result == null ? "" : result);
				ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
				progressDialog.dismiss();
			}

		}

		DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				// if (null != lat) {
				// lat.cancel(true);
				// System.out.println("refe" + lat.isCancelled());
				// lat = null;
				// // activity.getSupportFragmentManager().popBackStack();
				// }
			}
		};
	}

	private void initPropertySpinner() {
		propertyList.clear();
		propertyList.add(0, new PropertyDto("-1", "Select Property", "", "", ""));
		PropertySpinAdapter propertyAdapter = new PropertySpinAdapter(getActivity(), propertyList);
		sp_property.setAdapter(propertyAdapter);
	}
}

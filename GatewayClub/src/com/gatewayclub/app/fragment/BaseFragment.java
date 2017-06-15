package com.gatewayclub.app.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gatewayclub.app.R;
import com.gatewayclub.app.adapters.LocationSpinAdapter;
import com.gatewayclub.app.adapters.PropertySpinAdapter;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.main.MainActivityOptions;
import com.gatewayclub.app.pojos.PropertyDto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

	private RelativeLayout ll_main;
	protected ImageView iv_book, iv_calender, iv_alert, iv_edit, iv_customerreview, iv_agent, iv_back, iv_cross,
			iv_book_sum;
	protected View rootview;
	protected Spinner sp_location, sp_property;
	private TextView tv_name, tv_pending, tv_total_count,tv_mobile;
	private ProgressDialog progressDialog;
	private ArrayList<PropertyDto> propertyList = new ArrayList<PropertyDto>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

		rootview = inflater.inflate(R.layout.frag_base, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		tv_name = (TextView) rootview.findViewById(R.id.tv_name);
		tv_name.setText("Welcome " + MainActivity.getMainScreenActivity().getUserName());
		tv_mobile = (TextView) rootview.findViewById(R.id.tv_mobile);
		tv_total_count = (TextView) rootview.findViewById(R.id.tv_total_count);
		tv_pending = (TextView) rootview.findViewById(R.id.tv_pending);
		tv_mobile.setText( MainActivity.getMainScreenActivity().getMobileNo());
		tv_total_count.setText("Total business till now : " + MainActivity.getMainScreenActivity().getApproveCount());
		tv_pending.setText("Awaiting approval : " + MainActivity.getMainScreenActivity().getPendingCount());
		iv_book = (ImageView) rootview.findViewById(R.id.iv_book);
		iv_calender = (ImageView) rootview.findViewById(R.id.iv_calender);
		iv_alert = (ImageView) rootview.findViewById(R.id.iv_alert);
		iv_edit = (ImageView) rootview.findViewById(R.id.iv_edit);
		iv_customerreview = (ImageView) rootview.findViewById(R.id.iv_customerreview);
		iv_agent = (ImageView) rootview.findViewById(R.id.iv_agent);
		ll_main = (RelativeLayout) rootview.findViewById(R.id.ll_main);
		iv_back = (ImageView) rootview.findViewById(R.id.iv_back);
		iv_cross = (ImageView) rootview.findViewById(R.id.iv_cross);
		iv_book_sum = (ImageView) rootview.findViewById(R.id.iv_book_sum);
		sp_location = (Spinner) rootview.findViewById(R.id.sp_location);

		sp_property = (Spinner) rootview.findViewById(R.id.sp_property);
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
					propertySelected(propertyList.get(position));
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
					postDataParams.put("lmID", Commons.locationLists.get(position).getLocationID());
					postDataParams.put("pmOwnerID", MainActivity.getMainScreenActivity().getUserID());
					new PropertyTask(postDataParams).execute(Commons.GET_PROPERTY_LIST);
					sp_property.setEnabled(true);
					locationSelect( Commons.locationLists.get(position).getLocationName());
				} else {
					sp_property.setEnabled(false);
					initPropertySpinner();
					locationSelect("");
				}
			}

		});

		LocationSpinAdapter dataAdapter = new LocationSpinAdapter(getActivity(), Commons.locationLists);
		sp_location.setAdapter(dataAdapter);
		if (Commons.locationLists.size() == 2)
			sp_location.setSelection(1);
		initPropertySpinner();

		iv_book.setOnClickListener(this);
		iv_calender.setOnClickListener(this);
		iv_alert.setOnClickListener(this);
		iv_edit.setOnClickListener(this);
		iv_customerreview.setOnClickListener(this);
		iv_agent.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		iv_cross.setOnClickListener(this);
		iv_book_sum.setOnClickListener(this);
		return rootview;
	}

	protected void addView(View view) {
		ll_main.addView(view);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_book:
			MainActivityOptions.getMainScreenActivity().changeNavigationContentFragment(new BookFragment(), false);
			break;

		case R.id.iv_calender:
			MainActivityOptions.getMainScreenActivity().changeNavigationContentFragment(new CalenderFragment(), false);
			break;
		case R.id.iv_alert:
			MainActivityOptions.getMainScreenActivity().changeNavigationContentFragment(new AlertFragment(), false);
			break;

		case R.id.iv_edit:
			MainActivityOptions.getMainScreenActivity().changeNavigationContentFragment(new EditFragment(), false);
			break;

		case R.id.iv_customerreview:
			MainActivityOptions.getMainScreenActivity().changeNavigationContentFragment(new CustomerReviewFragment(),
					false);
			break;
		case R.id.iv_agent:
			MainActivityOptions.getMainScreenActivity().changeNavigationContentFragment(new AgentFragment(), false);
			break;

		case R.id.iv_cross:
			new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
					.setMessage("Are you sure you want to exit?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MainActivityOptions.getMainScreenActivity().finish();
							MainActivity.getMainScreenActivity().finish();
						}
					}).setNegativeButton("No", null).show();

			break;
		case R.id.iv_back:
			new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Logout")
			.setMessage("Are you sure you want to logout?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MainActivity.getSharePreferance().edit().clear().commit();
					MainActivity.getMainScreenActivity().finish();
					MainActivityOptions.getMainScreenActivity().finish();
				}
			}).setNegativeButton("No", null).show();
			//MainActivityOptions.getMainScreenActivity().onBackPressed();
			// MainActivity.getMainScreenActivity().onBackPressed();
			break;
		case R.id.iv_book_sum:
			MainActivityOptions.getMainScreenActivity().changeNavigationContentFragment(new BookingSumFragment(),
					false);
			break;
		default:
			break;
		}
	}

	private void clearButtonColor(int id) {

		iv_book.setBackgroundResource(R.drawable.book_unselected);
		iv_calender.setBackgroundResource(R.drawable.calender_unselected);
		iv_alert.setBackgroundResource(R.drawable.alerts_unselected);
		iv_edit.setBackgroundResource(R.drawable.edit_unselected);
		iv_customerreview.setBackgroundResource(R.drawable.customer_review_unselected);
		iv_agent.setBackgroundResource(R.drawable.agents_unselected);
		switch (id) {
		case R.id.iv_book:
			iv_book.setBackgroundResource(R.drawable.book_selected);
			break;

		case R.id.iv_calender:
			iv_calender.setBackgroundResource(R.drawable.calender_selected);
			break;
		case R.id.iv_alert:
			iv_alert.setBackgroundResource(R.drawable.alerts_selected);
			break;

		case R.id.iv_edit:
			iv_edit.setBackgroundResource(R.drawable.edit_selected);
			break;

		case R.id.iv_customerreview:
			iv_customerreview.setBackgroundResource(R.drawable.customer_review_selected);
			break;
		case R.id.iv_agent:
			iv_agent.setBackgroundResource(R.drawable.agents_selected);
			break;

		default:
			break;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		clearButtonColor(getSelectedID());
	}

	protected void hideSpinner() {
		sp_location.setVisibility(View.GONE);
		sp_property.setVisibility(View.GONE);
	}

	protected abstract int getSelectedID();

	protected abstract void propertySelected(PropertyDto pdto);

	private class PropertyTask extends AsyncProcess {

		public PropertyTask(HashMap<String, String> postDataParams) {
			super(postDataParams);
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

				} catch (Exception e) {
					e.printStackTrace();
					initPropertySpinner();

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

	protected void showDatePicker(final boolean ismonth, final TextView tv) {

		final int MAX_YEAR = 2099;
		final int MIN_YEAR = 2000;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		Calendar cal = Calendar.getInstance();

		View dialog = inflater.inflate(R.layout.dialog, null);
		final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
		final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

		monthPicker.setMinValue(1);
		monthPicker.setMaxValue(12);
		monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

		int year = cal.get(Calendar.YEAR);
		yearPicker.setMinValue(year);
		yearPicker.setMaxValue(MAX_YEAR);
		yearPicker.setMinValue(MIN_YEAR);
		yearPicker.setValue(year);

		if (ismonth)
			monthPicker.setVisibility(View.VISIBLE);
		else
			yearPicker.setVisibility(View.VISIBLE);
		builder.setView(dialog)
				// Add action buttons
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (ismonth)
							tv.setText(Integer.toString(monthPicker.getValue()));
						else
							tv.setText(Integer.toString(yearPicker.getValue()));
						fetchdata(tv);
						dialog.cancel();
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create();
		builder.show();
	}

	protected void fetchdata(TextView tv) {
	}

	public abstract void locationSelect(String location);
}

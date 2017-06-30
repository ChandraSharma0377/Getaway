
package com.gatewayclub.app.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gatewayclub.app.R;
import com.gatewayclub.app.adapters.AgentSpinAdapter;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.pojos.AgentDto;
import com.gatewayclub.app.pojos.PropertyDto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BookingSumFragment extends BaseFragment implements OnClickListener {

	private RelativeLayout ll_month1, ll_year1, ll_month2, ll_year2;
	private TextView tv_month1, tv_year1, tv_month2, tv_year2;
	private TextView edt_amount1, edt_amount2, edt_count1, edt_count2;
	private Spinner sp_booked_cancel,sp_booked_confirm;
	private int ACCEPT = 0;
	private int REJECT = 1;
	private int AGENT_CALL = 2;
	private ArrayList<AgentDto> agentlist = new ArrayList<AgentDto>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
		super.onCreateView(inflater, container, args);
		View view = inflater.inflate(R.layout.frag_booking_sum, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		tv_month1 = (TextView) view.findViewById(R.id.tv_month1);
		ll_month1 = (RelativeLayout) view.findViewById(R.id.ll_month1);
		tv_year1 = (TextView) view.findViewById(R.id.tv_year1);
		ll_year1 = (RelativeLayout) view.findViewById(R.id.ll_year1);

		tv_month2 = (TextView) view.findViewById(R.id.tv_month2);
		ll_month2 = (RelativeLayout) view.findViewById(R.id.ll_month2);
		tv_year2 = (TextView) view.findViewById(R.id.tv_year2);
		ll_year2 = (RelativeLayout) view.findViewById(R.id.ll_year2);

		edt_amount1 = (TextView) view.findViewById(R.id.edt_amount1);
		edt_amount2 = (TextView) view.findViewById(R.id.edt_amount2);
		edt_count1 = (TextView) view.findViewById(R.id.edt_count1);
		edt_count2 = (TextView) view.findViewById(R.id.edt_count2);

		ll_month1.setOnClickListener(this);
		ll_year1.setOnClickListener(this);
		ll_month2.setOnClickListener(this);
		ll_year2.setOnClickListener(this);
		iv_book_sum.setBackgroundResource(R.drawable.book_sum_select);

		sp_booked_confirm = (Spinner) view.findViewById(R.id.sp_booked_confirm);
		sp_booked_cancel = (Spinner) view.findViewById(R.id.sp_booked_cancel);
		sp_booked_confirm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
			}

		});
		sp_booked_cancel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
			}

		});
		hideSpinner();
		if (MainActivity.getNetworkHelper().isOnline()) {
			HashMap<String, String> postDataParams = new HashMap<String, String>();
			postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
			new GetBookingTask(postDataParams, AGENT_CALL).execute(Commons.GET_AGENT_LIST);

		} else {
			ShowAlertInformation.showNetworkDialog(getActivity());
		}
		addView(view);
		return rootview;
	}

	@Override
	protected int getSelectedID() {
		return -1;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		int id = v.getId();
		switch (id) {
		case R.id.ll_month1:
			showDatePicker(true, tv_month1);
			break;
		case R.id.ll_year1:
			showDatePicker(false, tv_year1);
			break;
		case R.id.ll_month2:
			showDatePicker(true, tv_month2);
			break;
		case R.id.ll_year2:
			showDatePicker(false, tv_year2);
			break;
		default:
			break;
		}
	}

	@Override
	protected void propertySelected(PropertyDto pdto) {

	}

	@Override
	protected void fetchdata(TextView tv) {
		// month : "mm"
		// year : "yyyy"
		// statusVal : "Approved" or "Reject"
		if (tv.getId() == R.id.tv_month1 || tv.getId() == R.id.tv_year1) {
			if (!tv_month1.getText().equals("Month") && !tv_year1.getText().equals("Year")) {
				if (MainActivity.getNetworkHelper().isOnline()) {
					HashMap<String, String> postDataParams = new HashMap<String, String>();
					postDataParams.put("pmOwnerId", MainActivity.getMainScreenActivity().getUserID());
					postDataParams.put("month", tv_month1.getText().toString());
					postDataParams.put("year", tv_year1.getText().toString());
					if (sp_booked_confirm.getSelectedItemPosition() == 0)
						postDataParams.put("agentId", MainActivity.getMainScreenActivity().getUserID());
					else
						postDataParams.put("agentId", agentlist.get(sp_booked_confirm.getSelectedItemPosition()).getAgentID());
					postDataParams.put("statusVal", "Approved");
					new GetBookingTask(postDataParams, ACCEPT).execute(Commons.BOOKING_SUMMARY);
				} else {
					ShowAlertInformation.showNetworkDialog(getActivity());
				}
			}
		} else if (tv.getId() == R.id.tv_month2 || tv.getId() == R.id.tv_year2) {
			if (!tv_month2.getText().equals("Month") && !tv_year2.getText().equals("Year")) {
				if (MainActivity.getNetworkHelper().isOnline()) {
					HashMap<String, String> postDataParams = new HashMap<String, String>();
					postDataParams.put("pmOwnerId", MainActivity.getMainScreenActivity().getUserID());
					postDataParams.put("month", tv_month2.getText().toString());
					postDataParams.put("year", tv_year2.getText().toString());
					if (sp_booked_cancel.getSelectedItemPosition() == 0)
						postDataParams.put("agentId", MainActivity.getMainScreenActivity().getUserID());
					else
						postDataParams.put("agentId", agentlist.get(sp_booked_cancel.getSelectedItemPosition()).getAgentID());
					postDataParams.put("statusVal", "Reject");
					new GetBookingTask(postDataParams, REJECT).execute(Commons.BOOKING_SUMMARY);
				} else {
					ShowAlertInformation.showNetworkDialog(getActivity());
				}
			}
		}
	}

	private class GetBookingTask extends AsyncProcess {

		private ProgressDialog progressDialog;
		private int senario = -1;

		public GetBookingTask(HashMap<String, String> postDataParams, int senario) {
			super(postDataParams);
			this.senario = senario;
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
			if (senario == ACCEPT) {
				if (200 == responseCode) {
					String value = result.replace("\\", "");
					if (value.length() > 2)
						value = value.substring(1, value.length() - 1);
					try {
						JSONObject jo = new JSONObject(value);
						String status = jo.getString("status");
						if (status.equals("Success")) {
							progressDialog.dismiss();
							String count = jo.getString("id");
							String amount = jo.getString("total");
							edt_count1.setText(count);
							edt_amount1.setText(amount);

						} else {
							edt_count1.setText("");
							edt_amount1.setText("");
							progressDialog.dismiss();
							ShowAlertInformation.showDialog(getActivity(), "Error", "Failed to get  the data.");
						}
					} catch (Exception e) {
						e.printStackTrace();
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
			if (senario == REJECT) {
				if (200 == responseCode) {
					String value = result.replace("\\", "");
					if (value.length() > 2)
						value = value.substring(1, value.length() - 1);
					try {
						JSONObject jo = new JSONObject(value);
						String status = jo.getString("status");
						if (status.equals("Success")) {
							progressDialog.dismiss();
							String count = jo.getString("id");
							String amount = jo.getString("amount");
							edt_count2.setText(count);
							edt_amount2.setText(amount);
						} else {
							edt_count2.setText("");
							edt_amount2.setText("");
							progressDialog.dismiss();
							ShowAlertInformation.showDialog(getActivity(), "Error", "Failed to get  the data.");
						}
					} catch (Exception e) {
						e.printStackTrace();
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
			if (senario == AGENT_CALL) {
				if (200 == responseCode) {

					String value = result.replace("\\", "");
					if (value.length() > 2)
						value = value.substring(1, value.length() - 1);
					try {
						JSONArray jaaray = new JSONArray(value);
						agentlist.clear();
						for (int i = 0; i < jaaray.length(); i++) {
							try {
								JSONObject jo = jaaray.getJSONObject(i);
								agentlist.add(new AgentDto(jo.getString("Id"), jo.getString("emailId")));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						agentlist.add(0, new AgentDto("-1", "Self"));
						initAgentSpinner();
						progressDialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
						agentlist.clear();
						agentlist.add(0, new AgentDto("-1", "Self"));
						initAgentSpinner();
						ShowAlertInformation.showDialog(getActivity(), "Error", "Error in data !!!");
						progressDialog.dismiss();
					}
					System.out.println("AgentTask result is : " + (result == null ? "" : result));
					progressDialog.dismiss();
				} else {
					agentlist.clear();
					agentlist.add(0, new AgentDto("-1", "Self"));
					initAgentSpinner();
					Log.i("AgentTask response", result == null ? "" : result);
					ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
					progressDialog.dismiss();
				}
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
	@Override
	public void locationSelect(String location) {

	}
	private void initAgentSpinner() {
		AgentSpinAdapter propertyAdapter = new AgentSpinAdapter(getActivity(), agentlist);
		sp_booked_cancel.setAdapter(propertyAdapter);
		sp_booked_confirm.setAdapter(propertyAdapter);

	}
}

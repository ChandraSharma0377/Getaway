
package com.gatewayclub.app.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gatewayclub.app.R;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.main.MainActivityOptions;
import com.gatewayclub.app.pojos.CalanderDateDto;
import com.gatewayclub.app.pojos.PropertyDto;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class CalenderFragment extends BaseFragment {

	private Button btn_approve, btn_reject;
	private TextView tv_booking_info, tv_booking_done, tv_start_date, tv_end_date;
	private ImageView iv_thumb;
	private EditText edt_remark,edt_noofadult, edt_noofchildren;
	private CaldroidFragment caldroidFragment;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
	String previouskey = "";
	String currentkey = "";
	private CalanderDateDto selectedcdd;
	private int ACCEPT = 0;
	private int REJECT = 1;
	private int SEND_MSG=2;

	private HashMap<String, HashMap<Date, CalanderDateDto>> cdata = new HashMap<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
		super.onCreateView(inflater, container, args);
		View view = inflater.inflate(R.layout.frag_calander, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
		btn_approve = (Button) view.findViewById(R.id.btn_approve);
		btn_reject = (Button) view.findViewById(R.id.btn_reject);
		edt_remark = (EditText) view.findViewById(R.id.edt_remark);
		edt_noofadult = (EditText) view.findViewById(R.id.edt_noofadult);
		edt_noofchildren = (EditText) view.findViewById(R.id.edt_noofchildren);
		tv_booking_done = (TextView) view.findViewById(R.id.tv_booking_done);
		tv_booking_done.setText(Html.fromHtml("<b> Booking done by</b>  <font color=\"#00ADEF\">GETAWAY CLUB</font>"));
		tv_booking_info = (TextView) view.findViewById(R.id.tv_booking_info);
		tv_booking_info.setText(Html.fromHtml("<b> Booking info</b> <br/> payment, tax and comission details "));

		tv_start_date = (TextView) view.findViewById(R.id.tv_start_date);
		tv_end_date = (TextView) view.findViewById(R.id.tv_end_date);
		btn_approve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (selectedcdd != null) {
					
					if (selectedcdd.getAdminApproval().equalsIgnoreCase("Awaiting Approval")){
//||selectedcdd.getAdminApproval().equalsIgnoreCase("Awaiting Payment")
						if (MainActivity.getNetworkHelper().isOnline()) {
							try {
								HashMap<String, String> postDataParams = new HashMap<String, String>();
								postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
								postDataParams.put("pbID", selectedcdd.getPbId());
								postDataParams.put("status", "Awaiting Payment");
								postDataParams.put("reason", "");
								new UpdatePropertyTask(postDataParams, ACCEPT).execute(Commons.UPDATE_PROPERTY);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
						}
					}else{
						ShowAlertInformation.showDialog(getActivity(), "Error", "Not Allow to Approved");
					}
				} else {
					ShowAlertInformation.showDialog(getActivity(), "Error", "Please select some date to approve");
				}
			}
		});
		btn_reject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (selectedcdd != null) {

					if (edt_remark.getText().toString().trim().equals("")) {
						edt_remark.setVisibility(View.VISIBLE);
						ShowAlertInformation.showDialog(getActivity(), "Error", "Please enter remark");
					} else {

						if (MainActivity.getNetworkHelper().isOnline()) {
							try {
								HashMap<String, String> postDataParams = new HashMap<String, String>();
								postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
								postDataParams.put("pbID", selectedcdd.getPbId());
								postDataParams.put("status", "Reject");
								postDataParams.put("reason", edt_remark.getText().toString().trim());
								new UpdatePropertyTask(postDataParams, REJECT).execute(Commons.UPDATE_PROPERTY);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							ShowAlertInformation.showDialog(getActivity(), "Network error",
									getString(R.string.offline));
						}
					}
				} else {
					ShowAlertInformation.showDialog(getActivity(), "Error", "Please select some date to reject");
				}

			}
		});
		caldroidFragment = new CaldroidFragment();

		Bundle bundle = new Bundle();
		Calendar cal = Calendar.getInstance();
		bundle.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
		bundle.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
		bundle.putBoolean(CaldroidFragment.ENABLE_SWIPE, false);
		bundle.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
		caldroidFragment.setArguments(bundle);
		// setCustomResourceForDates();

		// Attach to the activity
		FragmentTransaction ft = MainActivityOptions.getMainScreenActivity().getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.calendarView, caldroidFragment);
		ft.addToBackStack(null);
		ft.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				Toast.makeText(getActivity(), formatter.format(date), Toast.LENGTH_SHORT).show();

				if (cdata.get(currentkey) != null) {
					HashMap<Date, CalanderDateDto> cdto = cdata.get(currentkey);
					selectedcdd = cdto.get(date);
					if(null!=selectedcdd){
					tv_start_date.setText(dateString(selectedcdd.getFromDate()));
					tv_end_date.setText(dateString(selectedcdd.getToDate()));
					edt_noofadult.setText(selectedcdd.getPbNoOfAdult());
					edt_noofchildren.setText(selectedcdd.getPbNoOfChildren());
					}else{
						tv_start_date.setText(dateString(""));
						tv_end_date.setText(dateString(""));
						edt_noofadult.setText("");
						edt_noofchildren.setText("");
					}
				}
			}

			@Override
			public void onChangeMonth(int month, int year) {
				String text = "month: " + month + " year: " + year;
				if (!previouskey.equals("")) {
					clearCurrentDateColor(previouskey);
					currentkey = Commons.getMonthName(month) + String.valueOf(year);
					setCurrentDateColor(currentkey);
				}
				// Toast.makeText(getActivity(), text,
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClickDate(Date date, View view) {
				// Toast.makeText(getActivity(),
				// "Long click " + formatter.format(date),
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCaldroidViewCreated() {
				if (caldroidFragment.getLeftArrowButton() != null) {
					// Toast.makeText(getActivity(),
					// "Caldroid view is created", Toast.LENGTH_SHORT)
					// .show();
				}
			}

		};

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);

		addView(view);
		return rootview;
	}

	@Override
	protected int getSelectedID() {
		return iv_calender.getId();
	}

	@Override
	protected void propertySelected(PropertyDto pdto) {
		HashMap<String, String> postDataParams = new HashMap<String, String>();
		postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
		postDataParams.put("pmID", pdto.getPropertyID());
		new CalanderTask(postDataParams).execute(Commons.GET_CALANDER_DETAILS);
		Picasso.with(getActivity()).load(Commons.IMAGE_BASE_URL + pdto.getPropertyImage()).resize(100, 100)
				.into(iv_thumb);
	}

	private class CalanderTask extends AsyncProcess {

		private ProgressDialog progressDialog;

		public CalanderTask(HashMap<String, String> postDataParams) {
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

					try {
						cdata.clear();
						for (int i = 0; i < jaaray.length(); i++) {
							JSONObject jo = jaaray.getJSONObject(i);
							String pbFromDate = jo.getString("pbFromDate");
							String pbToDate = jo.getString("pbToDate");
							String AdminApproval = jo.getString("AdminApproval");
							String pbId = jo.getString("pbId");
							String mobile=jo.getString("Mobile");
							String PbNoOfAdult =jo.getString("PbNoOfAdult");
							String PbNoOfChildren =jo.getString("PbNoOfChildren");
							CalanderDateDto cdd = new CalanderDateDto(pbFromDate, pbToDate, AdminApproval, pbId,mobile,PbNoOfAdult,PbNoOfChildren);
							if (!pbFromDate.equals("") && pbFromDate.contains(" ") && !pbToDate.equals("")
									&& pbToDate.contains(" ")) {
								setCalanderStatus(pbFromDate.split(" ")[0], pbToDate.split(" ")[0], AdminApproval, cdd);
							}
//							tv_start_date.setText(dateString(pbFromDate));
//							tv_end_date.setText(dateString(pbToDate));
						}

						setCurrentDateColor("");
						caldroidFragment.refreshView();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
					tv_start_date.setText("");
					tv_end_date.setText("");
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

	private void setCalanderStatus(String str_date1, String end_date1, String status, CalanderDateDto cdd) {
		List<Date> dates = new ArrayList<Date>();

		// String str_date ="27/08/2010";
		// String end_date ="02/09/2010";
		String str_date = str_date1.replaceAll("-","/");
		String end_date = end_date1.replaceAll("-","/");
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date startDate = (Date) formatter.parse(str_date);
			Date endDate = (Date) formatter.parse(end_date);

			long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
			long endTime = endDate.getTime(); // create your endtime here,
												// possibly using Calendar or
												// Date
			long curTime = startDate.getTime();
			while (curTime <= endTime) {
				dates.add(new Date(curTime));
				curTime += interval;
			}

			for (int i = 0; i < dates.size(); i++) {
				Date date = (Date) dates.get(i);
				String ds = formatter.format(date);
				System.out.println(" Date is ..." + ds + " & status is :" + status);
				String key = "";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

				simpleDateFormat.applyPattern("MMM");
				key = simpleDateFormat.format(date);
				simpleDateFormat.applyPattern("yyyy");
				key = key + simpleDateFormat.format(date);

				if (cdata.get(key) == null) {
					HashMap<Date, CalanderDateDto> cdto = new HashMap();
					cdto.put(date, cdd);
					cdata.put(key, cdto);
				} else {
					HashMap<Date, CalanderDateDto> cdto = cdata.get(key);
					cdto.put(date, cdd);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setCurrentDateColor(String string) {
		// TODO Auto-generated method stub
		Date date1 = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		simpleDateFormat.applyPattern("MMM");
		String key = simpleDateFormat.format(date1);
		simpleDateFormat.applyPattern("yyyy");
		key = key + simpleDateFormat.format(date1);
		if (!string.equals(""))
			key = string;
		else
		currentkey=key;
		previouskey = key;
		HashMap<Date, CalanderDateDto> cdto = cdata.get(key);
		Drawable red = getResources().getDrawable(R.drawable.red_cell);
		Drawable yellow = getResources().getDrawable(R.drawable.yellow_cell);
		Drawable orange = getResources().getDrawable(R.drawable.orange_cell);
		if (null != cdto)
			for (Entry<Date, CalanderDateDto> entry : cdto.entrySet()) {
				Date keys = entry.getKey();
				CalanderDateDto value = entry.getValue();
				if (value.getAdminApproval().equalsIgnoreCase("Awaiting Approval")) {
					caldroidFragment.setBackgroundDrawableForDate(orange, keys);
				}else if(value.getAdminApproval().equalsIgnoreCase("Awaiting Payment")){
					caldroidFragment.setBackgroundDrawableForDate(yellow, keys);
				} else if(value.getAdminApproval().equalsIgnoreCase("Booked")) {
					caldroidFragment.setBackgroundDrawableForDate(red, keys);
				}
			}
		caldroidFragment.refreshView();
	}

	private void clearCurrentDateColor(String key) {

		HashMap<Date, CalanderDateDto> cdto = cdata.get(key);
		if (null != cdto)
			for (Entry<Date, CalanderDateDto> entry : cdto.entrySet()) {
				Date keys = entry.getKey();
				caldroidFragment.clearBackgroundDrawableForDate(keys);
			}
		caldroidFragment.refreshView();
	}

	private class UpdatePropertyTask extends AsyncProcess {

		private ProgressDialog progressDialog;
		private int senario = -1;

		public UpdatePropertyTask(HashMap<String, String> postDataParams, int senario) {
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
							HashMap<String, String> postDataParams = new HashMap<String, String>();
							new UpdatePropertyTask(postDataParams, SEND_MSG).execute(Commons.sendMessage(selectedcdd.getMobile(),selectedcdd.getFromDate()+"-"+selectedcdd.getToDate()," accepted."));
							ShowAlertInformation.showDialogAndFinish(getActivity(), "Success", "Status updated successfully");
							
						} else {
							progressDialog.dismiss();
							ShowAlertInformation.showDialog(getActivity(), "Error", "Failed to update Status.");
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
							edt_remark.setVisibility(View.GONE);
							edt_remark.setText("");
							HashMap<String, String> postDataParams = new HashMap<String, String>();
							new UpdatePropertyTask(postDataParams, SEND_MSG).execute(Commons.sendMessage(selectedcdd.getMobile(),selectedcdd.getFromDate()+"-"+selectedcdd.getToDate()," rejected."));
						
							ShowAlertInformation.showDialogAndFinish(getActivity(), "Success", "Status updated successfully");
						} else {
							progressDialog.dismiss();
							ShowAlertInformation.showDialog(getActivity(), "Error", "Failed to update Status.");
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
			if (senario == SEND_MSG) {
				if (200 == responseCode) {
					ShowAlertInformation.showDialog(getActivity(), "Success", "Message send to customer successfully");
					progressDialog.dismiss();
				} else {
					ShowAlertInformation.showDialog(getActivity(), "Error", "Error in message sending.");
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
	
	private String dateString(String date){
		String output="";
		try {
			if(date.contains(" ")){
			String as = date.split(" ")[0];
			String sd[] = as.split("/");
			output= sd[1] + "/" + sd[0] + "/" + sd[2];
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	@Override
	public void locationSelect(String location) {

	}
}

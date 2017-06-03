
package com.gatewayclub.app.fragment;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gatewayclub.app.R;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.pojos.PropertyDto;
import com.squareup.picasso.Picasso;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerReviewFragment extends BaseFragment {

	private ProgressDialog progressDialog;
	private TextView tv_alert_detail;
	private ImageView iv_thumb;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
		super.onCreateView(inflater, container, args);
		View view = inflater.inflate(R.layout.frag_customer, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
		tv_alert_detail = (TextView) view.findViewById(R.id.tv_alert_detail);
		tv_alert_detail.setText("");
		addView(view);

		HashMap<String, String> postDataParams = new HashMap<String, String>();
		postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
		new ReviewTask(postDataParams).execute(Commons.GET_CUSTOMER_REVIEW);
		return rootview;
	}

	@Override
	protected int getSelectedID() {
		return iv_customerreview.getId();
	}

	private class ReviewTask extends AsyncProcess {

		public ReviewTask(HashMap<String, String> postDataParams) {
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
						JSONObject jo = jaaray.getJSONObject(0);
						// [{
						// "status": "Success",
						// "Id": "1",
						// "CustId": "2",
						// "CustomerName": "Reets",
						// "Email": "reetesh.c.ocs@gmail.com",
						// "Mobile": "1234567888",
						// "Message": "this is our testing part please ignore
						// it.........!!!",
						// "AdminFeedback": "Approve"
						// }]

						String CustomerName = jo.getString("CustomerName");
						String Email = jo.getString("Email");
						String Mobile = jo.getString("Mobile");
						String Message = jo.getString("Message");

						tv_alert_detail.setText(Html.fromHtml(
								"CustomerName : " + CustomerName + "<br/> \r\n" + "Email : " + Email + "<br/> \r\n"
										+ "Mobile : " + Mobile + "<br/> \r\n" + "Message : " + Message + "<br/> \r\n"));

					} catch (Exception e) {
						e.printStackTrace();
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
	protected void propertySelected(PropertyDto pdto) {
		// TODO Auto-generated method stub
		Picasso.with(getActivity()).load(Commons.IMAGE_BASE_URL + pdto.getPropertyImage()).resize(100, 100)
		.into(iv_thumb);
	}
}

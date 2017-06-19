package com.gatewayclub.app.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.gatewayclub.app.R;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginFragment extends Fragment {

	private EditText edt_uname, edt_pwd;
	private CheckBox cb_remember;
	private Button btn_login;
	private LoginTask lat;
	private ProgressDialog progressDialog;

	public LoginFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

		View view = inflater.inflate(R.layout.lay_login, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		btn_login = (Button) view.findViewById(R.id.edt_login);
		edt_uname = (EditText) view.findViewById(R.id.edt_uname);
		edt_pwd = (EditText) view.findViewById(R.id.edt_pwd);
		cb_remember = (CheckBox) view.findViewById(R.id.cb_remember);
		// tv_forget_pwd=(TextView)view.findViewById(R.id.tv_forget_pwd);
		// tv_forget_pwd.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// MainActivity.getMainScreenActivity().changeNavigationContentFragment(new
		// ForgetPwdFragment(), false);
		// }
		// });
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String uname = edt_uname.getText().toString().trim();
				String pwd = edt_pwd.getText().toString().trim();
				edt_uname.setError(null);
				edt_pwd.setError(null);
				if (uname.equals("")) {

					edt_uname.setError("Please enter user name");
				} else if (pwd.equals("")) {

					edt_pwd.setError("Please enter password");
				} else {
					if (MainActivity.getNetworkHelper().isOnline()) {

						HashMap<String, String> postDataParams = new HashMap<String, String>();

						postDataParams.put("uLoginID", uname);
						postDataParams.put("uPassword", pwd);
						new LoginTask(postDataParams).execute(Commons.LOGIN_URL);
					} else {
						ShowAlertInformation.showNetworkDialog(getActivity());
					}
				}

			}
		});
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		MainActivity.getMainScreenActivity().actionBarTitle.setText("");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private class LoginTask extends AsyncProcess {

		public LoginTask(HashMap<String, String> postDataParams) {
			super(postDataParams);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(getActivity(), "", "login please wait...");
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

					JSONObject jo = new JSONObject(value);
					// "status": "Success",
					// "uUID": "1",
					// "uName": "Reetesh",
					// "uType": "2",
					// "uIsActive": "1"
					String status = jo.getString("status");

					if (status.equals("Success")) {
						String User_ID = jo.getString("uUID");
						String User_Name = jo.getString("uName");
						String User_Type = jo.getString("uType");
						String Is_user_active = jo.getString("uIsActive");
						String pendingCount =jo.getString("pendingCount");
						String approvedCount =jo.getString("approvedCount");
						String userMobile =jo.getString("userMobile");
						
				
						
						if (cb_remember.isChecked()) {
							MainActivity.getMainScreenActivity().setSharPreferancename(User_Name, User_ID, User_Type,
									Is_user_active, true,pendingCount,approvedCount,userMobile);
							// public void setSharPreferancename(String
							// userName, String userId, String userType,String
							// IsUserActive, boolean isLogin)
						} else {
							MainActivity.getMainScreenActivity().setSharPreferancename(User_Name, User_ID, User_Type,
									Is_user_active, false,pendingCount,approvedCount,userMobile);
						}

						MainActivity.getMainScreenActivity().changeNavigationContentFragment(new HomeFragment(), false);

					} else {
						progressDialog.dismiss();
						ShowAlertInformation.showDialog(getActivity(), "Error", "Invalid Credentials");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowAlertInformation.showDialog(getActivity(), "Error", "No data found");
					progressDialog.dismiss();
				}
				System.out.println("LoginTask result is : " + (result == null ? "" : result));
				progressDialog.dismiss();
			} else {
				Log.i("LoginTask response", result == null ? "" : result);
				ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
				progressDialog.dismiss();
			}

		}

		OnCancelListener cancelListener = new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				if (null != lat) {
					lat.cancel(true);
					System.out.println("refe" + lat.isCancelled());
					lat = null;
					// activity.getSupportFragmentManager().popBackStack();
				}
			}
		};
	}

}

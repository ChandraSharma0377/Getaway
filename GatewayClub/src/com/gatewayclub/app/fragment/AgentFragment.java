
package com.gatewayclub.app.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.gatewayclub.app.R;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.pojos.PropertyDto;

import org.json.JSONObject;

import java.util.HashMap;

public class AgentFragment extends BaseFragment {

	private Button btn_contact;
	private EditText edt_name, edt_address, edt_contact_person, edt_contact_no, edt_email, edt_agency_code;
	private int AGENT = 1;
	private int SEND_MSG=2;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
		super.onCreateView(inflater, container, args);
		View view = inflater.inflate(R.layout.frag_agent, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		edt_name = (EditText) view.findViewById(R.id.edt_name);
		edt_address = (EditText) view.findViewById(R.id.edt_address);
		edt_contact_person = (EditText) view.findViewById(R.id.edt_contact_person);
		edt_contact_no = (EditText) view.findViewById(R.id.edt_contact_no);
		edt_email = (EditText) view.findViewById(R.id.edt_email);
		edt_agency_code = (EditText) view.findViewById(R.id.edt_agency_code);
		btn_contact = (Button) view.findViewById(R.id.btn_contact);
		hideSpinner();
		btn_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isValid = true;
				String email = edt_email.getText().toString().trim();
				String name = edt_name.getText().toString().trim();
				String address = edt_address.getText().toString().trim();
				String contactperson = edt_contact_person.getText().toString().trim();
				String contactno = edt_contact_no.getText().toString().trim();
				String agencycode = edt_agency_code.getText().toString().trim();
				edt_email.setError(null);
				edt_name.setError(null);
				edt_address.setError(null);
				edt_contact_person.setError(null);
				edt_contact_no.setError(null);
				edt_agency_code.setError(null);
				if (email.equals("")) {
					isValid = false;
					edt_email.setError("Please enter email id");
				}
				if (!email.equals("") && !Commons.isValidEmail(email)) {
					isValid = false;
					edt_email.setError("Please enter valid email id");
				}
				if (!contactno.equals("") && contactno.length() < 10) {
					isValid = false;
					edt_contact_no.setError("Please enter valid mobile no");
				}
				if (contactno.equals("")) {
					isValid = false;
					edt_contact_no.setError("Please enter mobile no");
				}
				if (name.equals("")) {
					isValid = false;
					edt_name.setError("Please enter name");
				}
				if (address.equals("")) {
					isValid = false;
					edt_address.setError("Please enter address");
				}
				if (contactperson.equals("")) {
					isValid = false;
					edt_contact_person.setError("Please enter contact person");
				}
				if (agencycode.equals("")) {
					isValid = false;
					edt_agency_code.setError("Please enter agency code");
				}
				if (isValid) {
					if (MainActivity.getNetworkHelper().isOnline()) {
						HashMap<String, String> postDataParams = new HashMap<String, String>();
						postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
						postDataParams.put("agentName", name);
						postDataParams.put("address", address);
						postDataParams.put("contactPerson", contactperson);
						postDataParams.put("emailID", email);
						postDataParams.put("agencyCode", agencycode);
						postDataParams.put("contactPersonNo", contactno);
						new AddAgentTask(postDataParams,AGENT).execute(Commons.ADD_AGENT);
					} else {
						ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
					}
				}

			}
		});
		addView(view);
		return rootview;
	}

	@Override
	protected int getSelectedID() {
		return iv_agent.getId();
	}

	@Override
	protected void propertySelected(PropertyDto pdto) {
		// TODO Auto-generated method stub

	}

	@Override
	public void locationSelect(String location) {

	}

	private class AddAgentTask extends AsyncProcess {

		ProgressDialog progressDialog;
		private int senario = -1;
		public AddAgentTask(HashMap<String, String> postDataParams, int senario) {
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
			if (senario == AGENT) {
			if (200 == responseCode) {

				String value = result.replace("\\", "");
				if (value.length() > 2)
					value = value.substring(1, value.length() - 1);
				try {
					JSONObject jo = new JSONObject(value);
					String status = jo.getString("status");

					if (status.equals("Success")) {
						ShowAlertInformation.showDialogAndFinish(getActivity(), "Success", "Agent added successfully!!!");
						HashMap<String, String> postDataParams = new HashMap<String, String>();
						new AddAgentTask(postDataParams, SEND_MSG).execute(Commons.sendAgentMessage(edt_contact_no.getText().toString().trim(),edt_name.getText().toString().trim()));
						
//						edt_email.setText("");
//						edt_name.setText("");
//						edt_address.setText("");
//						edt_contact_person.setText("");
//						edt_contact_no.setText("");
//						edt_agency_code.setText("");
					} else {

						ShowAlertInformation.showDialog(getActivity(), "Error", "Updation failed !!!");
					}
					progressDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();

					ShowAlertInformation.showDialog(getActivity(),"Error", "Updation failed !!!");
					progressDialog.dismiss();
				}
				System.out.println("LocationTask result is : " + (result == null ? "" : result));
				progressDialog.dismiss();
			} else {
				Log.i("LocationTask response", result == null ? "" : result);
				ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
				progressDialog.dismiss();
			}
			
		}if (senario == SEND_MSG) {
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

}

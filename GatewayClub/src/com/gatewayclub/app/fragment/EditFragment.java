
package com.gatewayclub.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gatewayclub.app.R;
import com.gatewayclub.app.adapters.MyHorizontalScrollAdapter;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.MyHorizontalScrollview;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.pojos.PropertyDto;
import com.gatewayclub.app.pojos.PropertyImageDto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class EditFragment extends BaseFragment implements View.OnClickListener {

	private Button btn_editphoto;
	private ImageButton btnPrev, btnNext;
	private int currIndex = 0;
	private ArrayList<PropertyImageDto> propertyimagelist = new ArrayList<PropertyImageDto>();
	private MyHorizontalScrollview myHorizontalScrollview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
		super.onCreateView(inflater, container, args);
		View view = inflater.inflate(R.layout.frag_edit, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		btn_editphoto = (Button) view.findViewById(R.id.btn_editphoto);
		myHorizontalScrollview = (MyHorizontalScrollview) view.findViewById(R.id.horizontalScrollView);
		btnNext = (ImageButton) view.findViewById(R.id.btnNext);
		btnPrev = (ImageButton) view.findViewById(R.id.btnPrev);
		btnNext.setOnClickListener(this);
		btnPrev.setOnClickListener(this);
		btn_editphoto.setOnClickListener(this);
		addView(view);
		return rootview;
	}

	@Override
	protected int getSelectedID() {
		return iv_edit.getId();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		int id = v.getId();
		switch (id) {

		case R.id.btnNext:
			if (currIndex < propertyimagelist.size()) {
				myHorizontalScrollview.setCenter(currIndex);
				currIndex++;

			}
			break;
		case R.id.btnPrev:
			if (currIndex != 0) {
				currIndex--;
				myHorizontalScrollview.setCenter(currIndex);

			}
			break;

		case R.id.btn_editphoto:
			MainActivity.getMainScreenActivity().changeNavigationContentFragment(new EditPhotoFragment(propertyimagelist), true);
			break;
		default:
			break;
		}
	}

	@Override
	protected void propertySelected(PropertyDto pdto) {
		if (MainActivity.getNetworkHelper().isOnline()) {
			HashMap<String, String> postDataParams = new HashMap<String, String>();
			postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
			postDataParams.put("pmID ",pdto.getPropertyID());
			new GetImageTask(postDataParams).execute(Commons.GET_BUNGLOWIMAGE_LIST);
		} else {
			ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
		}
		
	}
	private class GetImageTask extends AsyncProcess {

		ProgressDialog progressDialog;

		public GetImageTask(HashMap<String, String> postDataParams) {
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

					propertyimagelist.clear();
					for (int i = 0; i < jaaray.length(); i++) {
						try {
							JSONObject jo = jaaray.getJSONObject(i);
							propertyimagelist.add(new PropertyImageDto(jo.getString("pdId"), jo.getString("pdImage")));
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					MyHorizontalScrollAdapter adapter = new MyHorizontalScrollAdapter(getActivity(), propertyimagelist);
					myHorizontalScrollview.setAdapter(getActivity(), adapter);
					progressDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();

					ShowAlertInformation.showDialog(getActivity(),"Error", "Error in data !!!");
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
}

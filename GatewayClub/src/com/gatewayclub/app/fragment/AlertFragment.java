
package com.gatewayclub.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gatewayclub.app.R;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.pojos.PropertyDto;
import com.squareup.picasso.Picasso;

public class AlertFragment extends BaseFragment {

	private Button btn_respond;
	private TextView tv_alert_detail;
	private ImageView iv_thumb;
	private TextView tv_location;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
		super.onCreateView(inflater, container, args);
		View view = inflater.inflate(R.layout.frag_alert, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
		tv_alert_detail = (TextView) view.findViewById(R.id.tv_alert_detail);
		tv_location =(TextView)view.findViewById(R.id.tv_location);
		tv_alert_detail.setText(
				"1. Sleeping accomodation , including bathrooms, living rooms and suites need to be locked after well"
						+ "\n" + "2 Modification required in furnished accomodation for long stay guests." + "\n"
						+ "3 Bathroom fittings need to repair");
		btn_respond = (Button) view.findViewById(R.id.btn_respond);
		btn_respond.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		addView(view);
		return rootview;
	}

	@Override
	protected int getSelectedID() {
		return iv_alert.getId();
	}

	@Override
	protected void propertySelected(PropertyDto pdto) {
		Picasso.with(getActivity()).load(Commons.IMAGE_BASE_URL + pdto.getPropertyImage()).resize(100, 100)
		.into(iv_thumb);
	}
	@Override
	public void locationSelect(String location) {
		tv_location.setText("Location :"+location);
	}
}

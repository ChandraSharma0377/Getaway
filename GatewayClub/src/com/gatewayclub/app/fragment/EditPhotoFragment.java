
package com.gatewayclub.app.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gatewayclub.app.R;
import com.gatewayclub.app.adapters.GridViewAdapter;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ExpandableHeightGridView;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.main.MainActivityOptions;
import com.gatewayclub.app.pojos.GridItemDto;
import com.gatewayclub.app.pojos.PropertyDto;
import com.gatewayclub.app.pojos.PropertyImageDto;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class EditPhotoFragment extends BaseFragment {

	private ExpandableHeightGridView mGridView;
	private ArrayList<PropertyImageDto> imagelist = new ArrayList<PropertyImageDto>();
	private GridViewAdapter mGridAdapter;
	private ArrayList<GridItemDto> mGridData;
	private int PICK_IMAGE_REQUEST = 111;
	private int PICK_IMAGE_REQUEST_POPUP = 112;
	private  Button btn_addmore;
	private Bitmap bitmap;
	private Dialog dialog;
	private String pmID="";
	private ImageView iv_upload;
	private String URL ="http://www.thegetawayclub.in/Test_web_application/WebService1.asmx/uploadImage";



	public EditPhotoFragment(ArrayList<PropertyImageDto> imagelist , String pmID) {
		this.imagelist=imagelist;
		this.pmID  = pmID;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
		super.onCreateView(inflater, container, args);
		View view = inflater.inflate(R.layout.frag_editphoto, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		mGridView = (ExpandableHeightGridView) view.findViewById(R.id.gridView);
		btn_addmore = (Button) view.findViewById(R.id.btn_addmore);
		btn_addmore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addImageDialog();
			}
		});
		mGridData = new ArrayList<>();
		for (int i = 0; i < imagelist.size(); i++) {
			GridItemDto item = new GridItemDto();
			item.setTitle("");
			item.setImage(imagelist.get(i).getPropertyImageUrl());
			item.setImageID(imagelist.get(i).getPropertyImageID());
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
	@Override
	public void locationSelect(String location) {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			Uri filePath = data.getData();

			try {
				//getting image from gallery
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
				mGridAdapter.updateImage(bitmap);
				bitmap=null;
				//Setting image to ImageView
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (requestCode == PICK_IMAGE_REQUEST_POPUP && resultCode == RESULT_OK && data != null && data.getData() != null) {
			Uri filePath = data.getData();

			try {
				bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
				if(null != iv_upload){
					iv_upload.setImageBitmap(bitmap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void addImageDialog() {

		dialog = new Dialog(getActivity(), android.R.style.Theme_Dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.upload_img_dialog);
		dialog.setCanceledOnTouchOutside(true);
		iv_upload = (ImageView) dialog.findViewById(R.id.iv_upload);
		Button btn_add = (Button) dialog.findViewById(R.id.btn_add);
		Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//data : image_data
				//fName :  "image name"
				//currentDate : "1987-03-16"
			//	pmID : "property ID"
				if(null != bitmap) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					byte[] imageBytes = baos.toByteArray();
					final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
					HashMap<String, String> postDataParams = new HashMap<String, String>();
					postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
					postDataParams.put("pmID", pmID);
					postDataParams.put("data", imageString);
					postDataParams.put("currentDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					postDataParams.put("fName", MainActivity.getMainScreenActivity().getUserName() + System.currentTimeMillis());
					if (MainActivity.getNetworkHelper().isOnline()) {
						new UploadImageTask(postDataParams).execute(Commons.UPLOAD_NEW_IMAGE);
					} else {
						ShowAlertInformation.showNetworkDialog(getActivity());
					}
				}else{
					Toast.makeText(getActivity(), "Please select an image to upload.", Toast.LENGTH_LONG).show();
				}

			}
		});
		btn_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(MainActivityOptions.getMainScreenActivity().isExternalStoragePermissionAllowed()){
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_PICK);
					getActivity().startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST_POPUP);
				}else{
					MainActivityOptions.getMainScreenActivity().requestExternalStoragePermission();
				}
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		dialog.show();
	}

	private class UploadImageTask extends AsyncProcess {

		ProgressDialog progressDialog;
		HashMap<String, String> parameters;
		public UploadImageTask(HashMap<String, String> postDataParams) {
			super(postDataParams);
			parameters = postDataParams;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(MainActivityOptions.getMainScreenActivity(), "", "Image uploading, please wait...");
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
					String status = jo.getString("status");
					if (status.equals("Success")) {
						Toast.makeText(getActivity(), "Image uploaded successful", Toast.LENGTH_LONG).show();
						bitmap = null;
						if(null != dialog && dialog.isShowing())
							dialog.dismiss();
					} else {
						Toast.makeText(getActivity(), "Image uploaded unsuccessful", Toast.LENGTH_LONG).show();
					}
					progressDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
					ShowAlertInformation.showDialog(getActivity(), "Error", "Updation failed !!!");
					progressDialog.dismiss();
				}
				System.out.println("UploadImageTask result is : " + (result == null ? "" : result));
				progressDialog.dismiss();
			} else {
				Log.i("UploadImageTask result", result == null ? "" : result);
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

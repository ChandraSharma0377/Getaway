package com.gatewayclub.app.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gatewayclub.app.R;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.main.MainActivityOptions;
import com.gatewayclub.app.pojos.GridItemDto;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class GridViewAdapter extends ArrayAdapter<GridItemDto> {

	private Context mContext;
	private int layoutResourceId;
	private ArrayList<GridItemDto> mGridData = new ArrayList<GridItemDto>();
	private ImageView iv;
	private int position_upload = -1;
	private int position_delete=-1;
	private Bitmap bitmap;

	private int UPLOAD = 0;
	private int DELETE = 1;

	public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<GridItemDto> mGridData) {
		super(mContext, layoutResourceId, mGridData);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.mGridData = mGridData;
	}
	public void setGridData(ArrayList<GridItemDto> mGridData) {
		this.mGridData = mGridData;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final ViewHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
			holder.titleTextView=(TextView)row.findViewById(R.id.grid_item_title);
			if(layoutResourceId==R.layout.grid_item_layout){
				holder.btn_change = (Button) row.findViewById(R.id.btn_change);
				holder.btn_upload = (Button) row.findViewById(R.id.btn_upload);
				holder.iv_delete = (ImageView) row.findViewById(R.id.iv_delete);
			}
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		final GridItemDto item = mGridData.get(position);
		if(layoutResourceId==R.layout.grid_item_img_txt_layout) {
			Picasso.with(mContext).load(item.getImage()).resize(140, 140).into(holder.imageView);
		}
		else {
			Picasso.with(mContext).load(Commons.IMAGE_BASE_URL + item.getImage()).placeholder(R.drawable.placeholder).resize(140, 140).into(holder.imageView);
			holder.btn_change.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(MainActivityOptions.getMainScreenActivity().isExternalStoragePermissionAllowed()){
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_PICK);
						((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "Select Image"), 111);
						iv=holder.imageView;
						position_upload =position;
					}else{
						MainActivityOptions.getMainScreenActivity().requestExternalStoragePermission();
					}
				}
			});
			holder.btn_upload.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(position_upload == position&& null != bitmap){
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
						byte[] imageBytes = baos.toByteArray();
						final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
						HashMap<String, String> parameters = null;
						try{
							parameters = new HashMap<String, String>();
							parameters.put("pbID", item.getImageID());
							parameters.put("data", imageString);
							parameters.put("fName", MainActivity.getMainScreenActivity().getUserName()+System.currentTimeMillis());
							new UploadImageTask(parameters,UPLOAD).execute(Commons.UPLOAD_IMAGE);
						}catch (Exception ex){
							System.out.println(ex.toString());
						}
					}else{
						Toast.makeText(mContext, "Nothing to upload", Toast.LENGTH_LONG).show();
					}
				}
			});
			holder.iv_delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(position_upload != position) {
						new AlertDialog.Builder(mContext).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Delete Image")
								.setMessage("Are you sure you want to delete this image?")
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										HashMap<String, String> parameters = null;
										try {
											position_delete = position;
											parameters = new HashMap<String, String>();
											parameters.put("pbID", item.getImageID());
											new UploadImageTask(parameters, DELETE).execute(Commons.DELETE_IMAGE);
										} catch (Exception ex) {
											System.out.println(ex.toString());
										}
										//dialog.dismiss();
									}
								}).setNegativeButton("No", null).show();
					}
				}
			});
		}
		holder.titleTextView.setText(item.getTitle());
		return row;
	}

	static class ViewHolder {
		TextView titleTextView;
		ImageView imageView;
		Button btn_change;
		Button btn_upload;
		ImageView iv_delete;
	}

	public void updateImage(Bitmap bitmap){
		if(null!=iv){
			this.bitmap=bitmap;
			iv.setImageBitmap(bitmap);
		}
	}

	private class UploadImageTask extends AsyncProcess {

		ProgressDialog progressDialog;
		HashMap<String, String> parameters;
		int senario = -1;
		public UploadImageTask(HashMap<String, String> postDataParams,int senario) {
			super(postDataParams);
			parameters = postDataParams;
			this.senario=senario;

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
				if (senario == UPLOAD) {
					if (200 == responseCode) {

						String value = result.replace("\\", "");
						if (value.length() > 2)
							value = value.substring(1, value.length() - 1);
						try {
							JSONObject jo = new JSONObject(value);
							String status = jo.getString("status");
							if (status.equals("Success")) {
								Toast.makeText(mContext, "Image uploaded successful", Toast.LENGTH_LONG).show();
								mGridData.get(position_upload).setImage(parameters.get("fName") + ".jpg");
								notifyDataSetChanged();
							} else {
								Toast.makeText(mContext, "Image uploaded unsuccessful", Toast.LENGTH_LONG).show();
							}
							position_upload = -1;
							progressDialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
							ShowAlertInformation.showDialog(mContext, "Error", "Updation failed !!!");
							progressDialog.dismiss();
						}
						System.out.println("UploadImageTask result is : " + (result == null ? "" : result));
						progressDialog.dismiss();
					} else {
						Log.i("UploadImageTask result", result == null ? "" : result);
						ShowAlertInformation.showDialog(mContext, "Error", "Error");
						progressDialog.dismiss();
					}
				}else if (senario == DELETE) {
					if (200 == responseCode) {

						String value = result.replace("\\", "");
						if (value.length() > 2)
							value = value.substring(1, value.length() - 1);
						try {
							JSONObject jo = new JSONObject(value);
							String status = jo.getString("status");
							if (status.equals("Success")) {
								Toast.makeText(mContext, "Image deleted successful", Toast.LENGTH_LONG).show();
								mGridData.remove(position_delete);
								notifyDataSetChanged();
							} else {
								Toast.makeText(mContext, "Image deleted unsuccessful", Toast.LENGTH_LONG).show();
							}
							position_delete=-1;
							progressDialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
							ShowAlertInformation.showDialog(mContext, "Error", "Updation failed !!!");
							progressDialog.dismiss();
						}
						System.out.println("UploadImageTask result is : " + (result == null ? "" : result));
						progressDialog.dismiss();
					} else {
						Log.i("UploadImageTask result", result == null ? "" : result);
						ShowAlertInformation.showDialog(mContext, "Error", "Error");
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
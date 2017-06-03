package com.gatewayclub.app.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.gatewayclub.app.R;
import com.gatewayclub.app.main.MainActivityOptions;


public class ShowAlertInformation {

	public static void showDialog(Context context, String Title, String Message) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				context);

		alertDialog.setTitle(Title);

		alertDialog.setMessage(Message);

		alertDialog.setIcon(R.drawable.app_icon_2);

		alertDialog.setNegativeButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
					}
				});

		alertDialog.show();
	}

	public static void showDialogAndFinish(Context context, String Title, String Message) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				context);

		alertDialog.setTitle(Title);

		alertDialog.setMessage(Message);

		alertDialog.setIcon(R.drawable.app_icon_2);

		alertDialog.setNegativeButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						MainActivityOptions.getMainScreenActivity().onBackPressed();
					}
				});

		alertDialog.show();
	}
}

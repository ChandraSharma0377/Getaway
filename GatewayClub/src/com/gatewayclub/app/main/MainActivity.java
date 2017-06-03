package com.gatewayclub.app.main;

import java.util.HashMap;
import java.util.Stack;

import com.gatewayclub.app.R;
import com.gatewayclub.app.fragment.HomeFragment;
import com.gatewayclub.app.fragment.LoginFragment;
import com.gatewayclub.app.helper.NetworkHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private Boolean exit = false;
	private static MainActivity mainActivity;
	private static NetworkHelper networkHelper;
	public static final String MyPREFERENCES = "AppPref";
	private static SharedPreferences sharedpreferences;

	protected Fragment mFrag;
	protected Fragment cFrag, rootFragment;
	private HashMap<String, Stack<Fragment>> mFragmentsStack;
	public TextView actionBarTitle;

	private final String IS_LOGIN = "IsLoggedIn";
	private final String KEY_USER_TYPE = "userType";
	private final String KEY_USER_NAME = "userName";
	private final String KEY_USER_ID = "userId";
	private final String IS_USER_ACTIVE = "IsUserActive";
	
	private final String KEY_PENDING_COUNT = "pendingcount";
	private final String KEY_APPROVE_COUNT = "approvecount";
	private final String KEY_MOBILE = "mobile";
	

	protected static final String CONTENT_TAG = "contenFragments";
	public static MainActivity getMainScreenActivity() {
		return mainActivity;
	}

	public static NetworkHelper getNetworkHelper() {
		return networkHelper;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_main);
		mainActivity = this;
		networkHelper = new NetworkHelper(this);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		mFragmentsStack = new HashMap<String, Stack<Fragment>>();
		mFragmentsStack.put(CONTENT_TAG, new Stack<Fragment>());
		ActionBarSetup();
		if (isLoggedIn())
			changeNavigationContentFragment(new HomeFragment(), false);
		else
			changeNavigationContentFragment(new LoginFragment(), false);

		onNewIntent(getIntent());

	}

	private void ActionBarSetup() {
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar_view);
		getSupportActionBar().setElevation(0);
		View v = getSupportActionBar().getCustomView();

		actionBarTitle = (TextView) v.findViewById(R.id.title);

		Toolbar parent = (Toolbar) v.getParent();
		parent.setContentInsetsAbsolute(0, 0);
		// actionBarTitle.setText(getString(R.string.app_name));

	}

	public void hideHomeIcon() {
		// btn_search.setVisibility(View.INVISIBLE);
		// btn_refresh.setVisibility(View.INVISIBLE);
		// btn_logout.setVisibility(View.VISIBLE);
		// btn_exit.setVisibility(View.VISIBLE);
		// toplay.setVisibility(View.VISIBLE);
	}

	public void showHomeIcon(boolean isLogin) {
		// if(isLogin){
		// btn_search.setVisibility(View.INVISIBLE);
		// btn_refresh.setVisibility(View.INVISIBLE);
		// btn_logout.setVisibility(View.INVISIBLE);
		// toplay.setVisibility(View.GONE);
		// }else{
		// btn_search.setVisibility(View.VISIBLE);
		// btn_refresh.setVisibility(View.VISIBLE);
		// btn_logout.setVisibility(View.VISIBLE);
		// toplay.setVisibility(View.VISIBLE);
		// }
		// btn_exit.setVisibility(View.VISIBLE);
	}

	OnClickListener logoClik = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			// case R.id.iv_home:
			// changeNavigationContentFragment(new LandingFragment(), false);
			// break;
			// case R.id.backBtn:
			// removeFragment();
			// break;
			// case R.id.btn_exit:
			// new
			// AlertDialog.Builder(mainActivity).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
			// .setMessage("Are you sure you want to exit?")
			// .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// finish();
			// }
			// }).setNegativeButton("No", null).show();
			// break;
			// case R.id.btn_logout:
			// SharedPreferences.Editor editor =
			// MainActivity.getSharePreferance().edit();
			// editor.clear();
			// editor.commit();
			// restartActivity();
			// break;
			default:
				break;
			}
		}
	};

	public void changeNavigationContentFragment(Fragment frgm, boolean shouldAdd) {

		if (shouldAdd) {

			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			if (null != fm.findFragmentById(R.id.main_fragment))
				ft.hide(fm.findFragmentById(R.id.main_fragment));
			ft.add(R.id.main_fragment, frgm, frgm.getClass().getSimpleName());
			ft.addToBackStack(null);
			// ft.commitAllowingStateLoss();
			ft.commit();
			mFragmentsStack.get(CONTENT_TAG).push(frgm);
		} else {
			mFragmentsStack.get(CONTENT_TAG).clear();
			FragmentManager manager = getSupportFragmentManager();
			manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			manager.beginTransaction().replace(R.id.main_fragment, frgm).commitAllowingStateLoss();
			rootFragment = frgm;
		}

		cFrag = frgm;

	}

	// Checking exitFlag on Back press, if exitFlag is true removing all
	// fragments from back stack and exiting app.
	// if exitFlag is false then allowing default behavior. That is removing
	// current Fragment and loading previous
	// Fragment.
	@Override
	public void onBackPressed() {
		removeFragment();
	}

	public void removeFragment() {
		int statckSize = mFragmentsStack.get(CONTENT_TAG).size();
		if (statckSize == 0) {
			if (exit) {
				finish(); // finish activity
			} else {
				Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
				exit = true;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						exit = false;
					}
				}, 3 * 1000);

			}

		} else {
			Fragment fragment;
			if (statckSize > 1)
				fragment = mFragmentsStack.get(CONTENT_TAG).elementAt(statckSize - 2);
			else
				fragment = mFragmentsStack.get(CONTENT_TAG).elementAt(statckSize - 1);
			mFragmentsStack.get(CONTENT_TAG).pop();
			FragmentManager fm = this.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.detach(cFrag);
			ft.commitAllowingStateLoss();
			if (statckSize > 1) {
				cFrag = fragment;
				fragment.onResume();
			} else {
				cFrag = rootFragment;
				rootFragment.onResume();
			}
			super.onBackPressed();
		}

	}

	public static SharedPreferences getSharePreferance() {
		return sharedpreferences;
	}

	public String getUserName() {
		return sharedpreferences.getString(KEY_USER_NAME, "");
	}

	public String getUserID() {
		return sharedpreferences.getString(KEY_USER_ID, "");
	}

	public String getUserType() {
		return sharedpreferences.getString(KEY_USER_TYPE, "");
	}

	public boolean isLoggedIn() {
		return sharedpreferences.getBoolean(IS_LOGIN, false);
	}

	public boolean isUserActive() {
		return sharedpreferences.getBoolean(IS_USER_ACTIVE, false);
	}
	public String getPendingCount() {
		return sharedpreferences.getString(KEY_PENDING_COUNT, "");
	}

	public String getApproveCount() {
		return sharedpreferences.getString(KEY_APPROVE_COUNT, "");
	}

	public String getMobileNo() {
		return sharedpreferences.getString(KEY_MOBILE, "");
	}
	public void setSharPreferancename(String userName, String userId, String userType, String IsUserActive,
			boolean isLogin,String pendingCount,String approvedCount,String mobile) {
		Editor editor = sharedpreferences.edit();
		editor.putString(KEY_USER_NAME, userName);
		editor.putString(KEY_USER_ID, userId);
		editor.putString(KEY_USER_TYPE, userType);
		editor.putBoolean(IS_USER_ACTIVE, IsUserActive.equals("1"));
		editor.putBoolean(IS_LOGIN, isLogin);
		
		editor.putString(KEY_PENDING_COUNT, pendingCount);
		editor.putString(KEY_APPROVE_COUNT, approvedCount);
		editor.putString(KEY_MOBILE, mobile);
		editor.commit();
	}

	public static void redirectToFragment(Fragment fragment) {
		Fragment VF = fragment;
		MainActivity.getMainScreenActivity().changeNavigationContentFragment(VF, true);

	}

	private void restartActivity() {
		Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}

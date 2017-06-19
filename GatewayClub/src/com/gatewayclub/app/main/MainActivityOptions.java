package com.gatewayclub.app.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gatewayclub.app.R;
import com.gatewayclub.app.fragment.AgentFragment;
import com.gatewayclub.app.fragment.AlertFragment;
import com.gatewayclub.app.fragment.BookFragment;
import com.gatewayclub.app.fragment.BookingSumFragment;
import com.gatewayclub.app.fragment.CalenderFragment;
import com.gatewayclub.app.fragment.CustomerReviewFragment;
import com.gatewayclub.app.fragment.EditFragment;
import com.gatewayclub.app.helper.NetworkHelper;

import java.util.HashMap;
import java.util.Stack;

public class MainActivityOptions extends ActionBarActivity {

	private static MainActivityOptions mainActivity;
	private static NetworkHelper networkHelper;

	protected Fragment mFrag;
	protected Fragment cFrag, rootFragment;
	private HashMap<String, Stack<Fragment>> mFragmentsStack;
	public TextView actionBarTitle;
	protected static final String CONTENT_TAG = "contenFragments";

	public static MainActivityOptions getMainScreenActivity() {
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

		mFragmentsStack = new HashMap<String, Stack<Fragment>>();
		mFragmentsStack.put(CONTENT_TAG, new Stack<Fragment>());
		ActionBarSetup();
		int classname =getIntent().getIntExtra("CLASSNAME",0);

		changeNavigationContentFragment(getFragment(classname), false);

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

			finish(); // finish activity

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

	public static void redirectToFragment(Fragment fragment) {
		Fragment VF = fragment;
		MainActivityOptions.getMainScreenActivity().changeNavigationContentFragment(VF, true);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private Fragment getFragment(int pos ){
		Fragment fr = null;
		switch (pos) {
		
		case 0:
			fr=new BookFragment();
			break;

		case 1:
	
			fr= new CalenderFragment();
			break;
		case 2:
			fr=new AlertFragment();
			break;

		case 3:
			fr=new EditFragment();
			break;

		case 4:
		fr=new CustomerReviewFragment();
//					true);
			break;
		case 5:
			fr=new AgentFragment();
			break;
		case -1:
			fr=new BookingSumFragment();
			break;
		default:
			break;
		}
		return fr;
	}
}

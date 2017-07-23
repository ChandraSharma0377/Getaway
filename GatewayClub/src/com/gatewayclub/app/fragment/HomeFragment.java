package com.gatewayclub.app.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gatewayclub.app.R;
import com.gatewayclub.app.adapters.GridViewImgTxtAdapter;
import com.gatewayclub.app.adapters.LocationSpinAdapter;
import com.gatewayclub.app.adapters.PropertySpinAdapter;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ExpandableHeightGridView;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.main.MainActivityOptions;
import com.gatewayclub.app.pojos.GridItem;
import com.gatewayclub.app.pojos.LocationDto;
import com.gatewayclub.app.pojos.PropertyDto;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private ProgressDialog progressDialog;
    protected ImageView iv_back, iv_cross, iv_book_sum;
    protected View rootview;
    private Spinner sp_location, sp_property;
    private ExpandableHeightGridView mGridView;
    private GridViewImgTxtAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private TextView tv_name, tv_pending, tv_total_count, tv_mobile;
    private ArrayList<PropertyDto> propertyList = new ArrayList<PropertyDto>();
    private ImageView iv_thumb;
    private TextView tv_location;
    private BarChart bar_chart;
    private float[] graphData = new float[12];
    private int PROPERTY_CALL = 1;
    private int LOCATION_CALL = 0;
    private int GRAPH_CALL = 2;

//    public static final int[] MATERIAL_COLORS = {
//            rgb("#3498db"),
//            rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db")
//    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

        rootview = inflater.inflate(R.layout.frag_home, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        iv_thumb = (ImageView) rootview.findViewById(R.id.iv_thumb);
        iv_back = (ImageView) rootview.findViewById(R.id.iv_back);
        iv_cross = (ImageView) rootview.findViewById(R.id.iv_cross);
        iv_book_sum = (ImageView) rootview.findViewById(R.id.iv_book_sum);
        tv_name = (TextView) rootview.findViewById(R.id.tv_name);
        tv_mobile = (TextView) rootview.findViewById(R.id.tv_mobile);
        tv_total_count = (TextView) rootview.findViewById(R.id.tv_total_count);
        tv_pending = (TextView) rootview.findViewById(R.id.tv_pending);
        tv_location = (TextView) rootview.findViewById(R.id.tv_location);
        tv_name.setText("Welcome " + MainActivity.getMainScreenActivity().getUserName());
        tv_mobile.setText(MainActivity.getMainScreenActivity().getMobileNo());
        tv_total_count.setText("Total business till now : " + MainActivity.getMainScreenActivity().getApproveCount());
        tv_pending.setText("Awaiting approval : " + MainActivity.getMainScreenActivity().getPendingCount());
        iv_back.setOnClickListener(this);
        iv_cross.setOnClickListener(this);
        iv_book_sum.setOnClickListener(this);

        mGridView = (ExpandableHeightGridView) rootview.findViewById(R.id.gridView);
        intialzeGrid();
        bar_chart = (BarChart) rootview.findViewById(R.id.bar_chart);
        resetBarData();
        //initBarChart();
        BarData data = new BarData(getXAxisValues(), getDataSet(graphData));
        bar_chart.setData(data);
        bar_chart.setDescription("");
        bar_chart.setPinchZoom(false);
        bar_chart.setDrawGridBackground(false);
        bar_chart.setTouchEnabled(false);

        YAxis rightAxis = bar_chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);

        XAxis xAxis = bar_chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(90);
        bar_chart.invalidate();
        //setBarData(Commons.getCurrentYear());
        // To hide the bottom color description option
        bar_chart.getLegend().setEnabled(false);
        sp_location = (Spinner) rootview.findViewById(R.id.sp_location);
        sp_property = (Spinner) rootview.findViewById(R.id.sp_property);
        sp_property.setEnabled(false);
        sp_property.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                }
                if (position > 0) {
                    Picasso.with(getActivity())
                            .load(Commons.IMAGE_BASE_URL + propertyList.get(position).getPropertyImage())
                            .resize(100, 100).into(iv_thumb);
                    HashMap<String, String> postDataParams = new HashMap<String, String>();
                    postDataParams.put("pmOwnerID", MainActivity.getMainScreenActivity().getUserID());
                    postDataParams.put("pmID", propertyList.get(position).getPropertyID());
                    new LocationTask(postDataParams, GRAPH_CALL).execute(Commons.GET_GRAPH_INFO);
                }

            }

        });
        sp_location.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                }
                if (position > 0) {

                    HashMap<String, String> postDataParams = new HashMap<String, String>();
                    postDataParams.put("pmOwnerID", MainActivity.getMainScreenActivity().getUserID());
                    postDataParams.put("lmID", Commons.locationLists.get(position).getLocationID());
                    new LocationTask(postDataParams, PROPERTY_CALL).execute(Commons.GET_PROPERTY_LIST);
                    sp_property.setEnabled(true);
                    tv_location.setText(getString(R.string.location) + Commons.locationLists.get(position).getLocationName()
                            + "\nYear - " + Commons.getCurrentYear());
                } else {
                    sp_property.setEnabled(false);
                    initPropertySpinner();
                    tv_location.setText("");
                }

            }

        });

        if (MainActivity.getNetworkHelper().isOnline()) {
            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
            new LocationTask(postDataParams, LOCATION_CALL).execute(Commons.GET_LOCATION_LIST);
        } else {
            ShowAlertInformation.showNetworkDialog(getActivity());
        }
        initPropertySpinner();

        return rootview;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_cross:
                new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.getMainScreenActivity().finish();
                            }
                        }).setNegativeButton("No", null).show();

                break;
            case R.id.iv_back:
                //MainActivity.getMainScreenActivity().onBackPressed();
                new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.getSharePreferance().edit().clear().commit();
                                MainActivity.getMainScreenActivity().finish();
                            }
                        }).setNegativeButton("No", null).show();
                break;
            case R.id.iv_book_sum:
                Intent in = new Intent(getActivity(), MainActivityOptions.class);
                in.putExtra("CLASSNAME", -1);
                startActivity(in);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void intialzeGrid() {
        mGridView.setExpanded(true);
        mGridData = new ArrayList<>();

        mGridData.add(new GridItem(getString(R.string.books), R.drawable.book_unselected));
        mGridData.add(new GridItem(getString(R.string.calander), R.drawable.calender_unselected));
        mGridData.add(new GridItem(getString(R.string.alert), R.drawable.alerts_unselected));
        mGridData.add(new GridItem(getString(R.string.edit), R.drawable.edit_unselected));
        mGridData.add(new GridItem(getString(R.string.creivew), R.drawable.customer_review_unselected));
        mGridData.add(new GridItem(getString(R.string.agent), R.drawable.agents_unselected));

        mGridAdapter = new GridViewImgTxtAdapter(getActivity(), mGridData);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                int pos = position;
                Intent in = new Intent(getActivity(), MainActivityOptions.class);
                in.putExtra("CLASSNAME", pos);
                startActivity(in);


            }
        });
    }

    private class LocationTask extends AsyncProcess {
        private int senario = -1;

        public LocationTask(HashMap<String, String> postDataParams, int senario) {
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

            if (200 == responseCode) {

                String value = result.replace("\\", "");
                if (value.length() > 2)
                    value = value.substring(1, value.length() - 1);
                try {

                    if (senario == LOCATION_CALL) {
                        Commons.locationLists.clear();
                        JSONArray jaaray = new JSONArray(value);
                        for (int i = 0; i < jaaray.length(); i++) {
                            try {
                                JSONObject jo = jaaray.getJSONObject(i);
                                Commons.locationLists
                                        .add(new LocationDto(jo.getString("lmId"), jo.getString("lmlocation")));
                                String pendingCount = jo.getString("pendingCount");
                                String approvedCount = jo.getString("approvedCount");
                                MainActivity.getMainScreenActivity().setApproveCount(approvedCount);
                                MainActivity.getMainScreenActivity().setPendingCount(pendingCount);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Commons.locationLists.add(0, new LocationDto("-1", "Select Location"));
                        LocationSpinAdapter dataAdapter = new LocationSpinAdapter(getActivity(), Commons.locationLists);
                        sp_location.setAdapter(dataAdapter);
                        if (Commons.locationLists.size() == 2)
                            sp_location.setSelection(1);
                    } else if(senario == PROPERTY_CALL){
                        propertyList.clear();
                        JSONArray jaaray = new JSONArray(value);
                        for (int i = 0; i < jaaray.length(); i++) {
                            try {
                                JSONObject jo = jaaray.getJSONObject(i);
                                propertyList.add(new PropertyDto(jo.getString("pmId"), jo.getString("pmAddress"),
                                        jo.getString("pmImage"), jo.getString("MinimumCapacity"),
                                        jo.getString("pmMaxOccupancy")));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        propertyList.add(0, new PropertyDto("-1", "Select Property", "", "", ""));
                        PropertySpinAdapter propertyAdapter = new PropertySpinAdapter(getActivity(), propertyList);
                        sp_property.setAdapter(propertyAdapter);
                    }else if(senario == GRAPH_CALL){

                        try {
                            JSONObject jo = new JSONObject(value);
                            String status = jo.getString("status");
                            if (status.equals("Success")) {
                                progressDialog.dismiss();
                                String year = jo.getString("Year");
                                graphData[0] = convertValue(jo.getString("January"));
                                graphData[1] = convertValue(jo.getString("February"));
                                graphData[2] = convertValue(jo.getString("March"));
                                graphData[3] = convertValue(jo.getString("April"));
                                graphData[4] = convertValue(jo.getString("May"));
                                graphData[5] = convertValue(jo.getString("June"));
                                graphData[6] = convertValue(jo.getString("July"));
                                graphData[7] = convertValue(jo.getString("August"));
                                graphData[8] = convertValue(jo.getString("September"));
                                graphData[9] = convertValue(jo.getString("October"));
                                graphData[10] = convertValue(jo.getString("November"));
                                graphData[11] = convertValue(jo.getString("December"));
                                //setBarData(year);

                                BarData data = new BarData(getXAxisValues(), getDataSet(graphData));
                                bar_chart.setData(data);
                                bar_chart.invalidate();
                            } else {
                                progressDialog.dismiss();
                                ShowAlertInformation.showDialog(getActivity(), "Error", "Failed to get  the data.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ShowAlertInformation.showDialog(getActivity(), "Error", "No data found");
                            progressDialog.dismiss();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (senario == PROPERTY_CALL) {
                        initPropertySpinner();
                    }
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
            tv_total_count.setText("Total business till now : " + MainActivity.getMainScreenActivity().getApproveCount());
            tv_pending.setText("Awaiting approval : " + MainActivity.getMainScreenActivity().getPendingCount());
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

    private void initPropertySpinner() {
        propertyList.clear();
        propertyList.add(0, new PropertyDto("-1", "Select Property", "", "", ""));
        PropertySpinAdapter propertyAdapter = new PropertySpinAdapter(getActivity(), propertyList);
        sp_property.setAdapter(propertyAdapter);
    }

//    private void setBarData(String year) {
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//
//        for (int i = 0; i < graphData.length; i++) {
//            yVals1.add(new BarEntry(i + 1, graphData[i]));
//        }
//            BarDataSet barDataSet = new BarDataSet(yVals1, "The Year "+year);
//            barDataSet.setDrawIcons(false);
//            barDataSet.setColors(MATERIAL_COLORS);
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(barDataSet);
//
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(10f);
//            //data.setValueTypeface(mTfLight);
//            data.setBarWidth(0.5f);
//            bar_chart.setData(data);
//            bar_chart.getData().notifyDataChanged();
//            bar_chart.notifyDataSetChanged();
//        for (IDataSet set : bar_chart.getData().getDataSets())
//            set.setDrawValues(true);
//            bar_chart.invalidate();
//    }

//    private void initBarChart() {
//
//        bar_chart.setDrawBarShadow(false);
//        bar_chart.setDrawValueAboveBar(true);
//        bar_chart.getDescription().setEnabled(false);
//        bar_chart.setMaxVisibleValueCount(12);
//        bar_chart.setPinchZoom(false);
//        bar_chart.setDrawGridBackground(false);
//        bar_chart.setTouchEnabled(false);
//
//        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(bar_chart);
//
//        XAxis xAxis = bar_chart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        //  xAxis.setTypeface(mTfLight);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(false);
//        xAxis.setGranularity(1f); // only intervals of 1 day
//        xAxis.setLabelCount(12);
//        xAxis.setLabelRotationAngle(90.0f);
//        xAxis.setValueFormatter(xAxisFormatter);
//
//
//        IAxisValueFormatter custom = new MyAxisValueFormatter();
//
//        YAxis leftAxis = bar_chart.getAxisLeft();
//        // leftAxis.setTypeface(mTfLight);
//        leftAxis.setLabelCount(20, false);
//        leftAxis.setValueFormatter(custom);
//        leftAxis.setDrawGridLines(true);
//        leftAxis.setDrawAxisLine(true);
//        leftAxis.setDrawLabels(true);
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        leftAxis.setSpaceTop(15f);
//        leftAxis.setAxisMinimum(0.0f); // this replaces setStartAtZero(true)
//
//        YAxis rightAxis = bar_chart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
//        //rightAxis.setTypeface(mTfLight);
//        rightAxis.setLabelCount(20, false);
//        rightAxis.setValueFormatter(custom);
//        rightAxis.setSpaceTop(15f);
//        rightAxis.setDrawAxisLine(false);
//        rightAxis.setDrawLabels(false);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        Legend l = bar_chart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//
//        l.setForm(Legend.LegendForm.SQUARE);
//        l.setFormSize(9f);
//        l.setTextSize(11f);
//        l.setXEntrySpace(4f);
//
//    }

    private float convertValue(String val) {
        try {
            if (val.trim().equals(""))
                return 0f;
            else
                return Float.valueOf(val);
        } catch (Exception e) {
            e.printStackTrace();
            return 0f;
        }
    }

    private void resetBarData(){
        for (int i =0 ;i < 12 ; i++)
            graphData[i] = 0f;
    }


    private ArrayList<BarDataSet> getDataSet(float[]data) {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(data[0], 0); // Jan
        valueSet.add(v2e1);
        BarEntry v2e2 = new BarEntry(data[1], 1); // Feb
        valueSet.add(v2e2);
        BarEntry v2e3 = new BarEntry(data[2], 2); // Mar
        valueSet.add(v2e3);
        BarEntry v2e4 = new BarEntry(data[3], 3); // Apr
        valueSet.add(v2e4);
        BarEntry v2e5 = new BarEntry(data[4], 4); // May
        valueSet.add(v2e5);
        BarEntry v2e6 = new BarEntry(data[5], 5); // Jun
        valueSet.add(v2e6);
        BarEntry v1e1 = new BarEntry(data[6], 6); // Jan
        valueSet.add(v1e1);
        BarEntry v1e2 = new BarEntry(data[7], 7); // Feb
        valueSet.add(v1e2);
        BarEntry v1e3 = new BarEntry(data[8], 8); // Mar
        valueSet.add(v1e3);
        BarEntry v1e4 = new BarEntry(data[9], 9); // Apr
        valueSet.add(v1e4);
        BarEntry v1e5 = new BarEntry(data[10], 10); // May
        valueSet.add(v1e5);
        BarEntry v1e6 = new BarEntry(data[11], 11); // Jun
        valueSet.add(v1e6);
         int[] COLORFUL_COLORS = {
                 Color.rgb(0,173,239)
        };

        BarDataSet barDataSet = new BarDataSet(valueSet, "");
        barDataSet.setColors(COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");
        xAxis.add("SEP");
        xAxis.add("OCT");
        xAxis.add("NOV");
        xAxis.add("DEC");
        return xAxis;
    }
}

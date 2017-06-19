
package com.gatewayclub.app.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gatewayclub.app.R;
import com.gatewayclub.app.adapters.AgentSpinAdapter;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.main.MainActivityOptions;
import com.gatewayclub.app.pojos.AgentDto;
import com.gatewayclub.app.pojos.CalanderDateDto;
import com.gatewayclub.app.pojos.PropertyDto;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class BookFragment extends BaseFragment implements View.OnClickListener {

    private Button btn_book;
    private CheckBox cb_yes, cb_no;
    private Spinner sp_agent;
    private TextView tv_checkin_time, tv_checkin_date, tv_checkout_date, tv_checkout_time, tv_location, tv_totalperson;
    private EditText edt_noofadult, edt_noofchildren, edt_customername, edt_mobile, edt_email, edt_address;
    private ArrayList<AgentDto> agentlist = new ArrayList<AgentDto>();
    private int PROPERTY_SELECT = 1;
    private int AGENT_CALL = 0;
    private int ADD_BOOKING = 2;
    private int GET_CUSTOMER_INFO = 3;
    private int SEND_MSG = 4;
    private int CALANDER_DETAILS = 5;
    private String prID = "";
    private String prRatePerNight = "";
    private String prExtraPersonRate = "";
    private String WeekendRate = "";
    private String ExtraWeekendRate = "";
    private String ExtraHolidayRate = "";
    private String FoodRatePerPerson = "";
    private PropertyDto pdto;
    private String customer_id = "";
    private ImageView iv_thumb;
    private CaldroidFragment caldroidFragment;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    private String previouskey = "";
    private String currentkey = "";
    //	private CalanderDateDto selectedcdd;
    private HashMap<String, HashMap<Date, CalanderDateDto>> cdata = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        super.onCreateView(inflater, container, args);
        View view = inflater.inflate(R.layout.frag_book, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        btn_book = (Button) view.findViewById(R.id.btn_book);
        sp_agent = (Spinner) view.findViewById(R.id.sp_agent);
        iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
        // cb_yes = (CheckBox) view.findViewById(R.id.cb_yes);
        // cb_no = (CheckBox) view.findViewById(R.id.cb_no);
        tv_checkin_time = (TextView) view.findViewById(R.id.tv_checkin_time);
        tv_checkin_date = (TextView) view.findViewById(R.id.tv_checkin_date);
        tv_checkout_date = (TextView) view.findViewById(R.id.tv_checkout_date);
        tv_checkout_time = (TextView) view.findViewById(R.id.tv_checkout_time);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_totalperson = (TextView) view.findViewById(R.id.tv_totalperson);
        edt_customername = (EditText) view.findViewById(R.id.edt_customername);
        edt_mobile = (EditText) view.findViewById(R.id.edt_mobile);
        edt_address = (EditText) view.findViewById(R.id.edt_address);
        edt_noofadult = (EditText) view.findViewById(R.id.edt_noofadult);
        edt_noofchildren = (EditText) view.findViewById(R.id.edt_noofchildren);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        tv_checkin_date.setOnClickListener(this);
        tv_checkout_date.setOnClickListener(this);
        // tv_checkin_time.setOnClickListener(this);
        // tv_checkout_time.setOnClickListener(this);
        edt_mobile.addTextChangedListener(new GenericTextWatcher1(edt_mobile));

        btn_book.setOnClickListener(this);
        edt_noofadult.addTextChangedListener(new GenericTextWatcher(edt_noofadult));
        edt_noofchildren.addTextChangedListener(new GenericTextWatcher(edt_noofchildren));
        addView(view);
        caldroidFragment = new CaldroidFragment();

        Bundle bundle = new Bundle();
        Calendar cal = Calendar.getInstance();
        bundle.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        bundle.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        bundle.putBoolean(CaldroidFragment.ENABLE_SWIPE, false);
        bundle.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
        caldroidFragment.setArguments(bundle);
        // setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction ft = MainActivityOptions.getMainScreenActivity().getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.calendarView, caldroidFragment);
        ft.addToBackStack(null);
        ft.commit();
        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
//				Toast.makeText(getActivity(), formatter.format(date), Toast.LENGTH_SHORT).show();
//
//				if (cdata.get(currentkey) != null) {
//					HashMap<Date, CalanderDateDto> cdto = cdata.get(currentkey);
//					selectedcdd = cdto.get(date);
//					
//				}
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                if (!previouskey.equals("")) {
                    clearCurrentDateColor(previouskey);
                    currentkey = Commons.getMonthName(month) + String.valueOf(year);
                    setCurrentDateColor(currentkey);
                }
                // Toast.makeText(getActivity(), text,
                // Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                // Toast.makeText(getActivity(),
                // "Long click " + formatter.format(date),
                // Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    // Toast.makeText(getActivity(),
                    // "Caldroid view is created", Toast.LENGTH_SHORT)
                    // .show();
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        if (MainActivity.getNetworkHelper().isOnline()) {
            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
            new GetAgentTask(postDataParams, AGENT_CALL).execute(Commons.GET_AGENT_LIST);
        } else {
            ShowAlertInformation.showNetworkDialog(getActivity());
        }
        initAgentSpinner();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {

            case R.id.btn_book:
                validateandsubmit();
                break;
            case R.id.tv_checkin_time:
                showTimePicker(getActivity(), tv_checkin_time);
                break;
            case R.id.tv_checkin_date:
                showDatePicker(getActivity(), tv_checkin_date);
                break;
            case R.id.tv_checkout_date:
                showDatePicker(getActivity(), tv_checkout_date);
                break;
            case R.id.tv_checkout_time:
                showTimePicker(getActivity(), tv_checkout_time);
                break;
            default:
                break;
        }
    }


    private void validateandsubmit() {

        boolean isValid = true;
        String noofadult = edt_noofadult.getText().toString().trim();
        String noofchildren = edt_noofchildren.getText().toString().trim();
        String name = edt_customername.getText().toString().trim();
        String address = edt_address.getText().toString().trim();
        String mobile = edt_mobile.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        edt_noofadult.setError(null);
        edt_noofchildren.setError(null);
        edt_customername.setError(null);
        edt_address.setError(null);
        edt_mobile.setError(null);
        edt_email.setError(null);
        if (email.equals("")) {
            isValid = false;
            edt_email.setError("Please enter email id");
        }
        if (!email.equals("") && !Commons.isValidEmail(email)) {
            isValid = false;
            edt_email.setError("Please enter valid email id");
        }
        if (noofadult.equals("")) {
            isValid = false;
            edt_noofadult.setError("Please enter no of Adults.");
        }
        if (noofchildren.equals("")) {
            isValid = false;
            edt_noofchildren.setError("Please enter no of Children.");
        }
        if (!mobile.equals("") && mobile.length() < 10) {
            isValid = false;
            edt_mobile.setError("Please enter valid mobile no");
        }
        if (mobile.equals("")) {
            isValid = false;
            edt_mobile.setError("Please enter mobile no");
        }
        if (name.equals("")) {
            isValid = false;
            edt_customername.setError("Please enter name");
        }
        if (address.equals("")) {
            isValid = false;
            edt_address.setError("Please enter address");
        }
        if (sp_location.getSelectedItemPosition() == 0) {
            isValid = false;
            Toast.makeText(getActivity(), "Please select location", Toast.LENGTH_LONG).show();
            ;

        }
        if (sp_property.getSelectedItemPosition() == 0) {
            isValid = false;
            Toast.makeText(getActivity(), "Please select property", Toast.LENGTH_LONG).show();

        }
        if (tv_checkin_time.getText().toString().equals("")) {
            isValid = false;
            Toast.makeText(getActivity(), "Please select checkin Time", Toast.LENGTH_LONG).show();
        }
        if (tv_checkout_time.getText().equals("")) {
            isValid = false;
            Toast.makeText(getActivity(), "Please select checkout Time", Toast.LENGTH_LONG).show();
        }
        if (tv_checkin_date.getText().equals("")) {
            isValid = false;
            Toast.makeText(getActivity(), "Please select checkin date", Toast.LENGTH_LONG).show();
        }
        if (tv_checkout_date.getText().equals("")) {
            isValid = false;
            Toast.makeText(getActivity(), "Please select checkout date", Toast.LENGTH_LONG).show();
        } else if (!tv_checkin_date.getText().equals("")
                && !Commons.isValidDateFromToday(tv_checkin_date.getText().toString())) {
            isValid = false;
            SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String todayDate = dfDate.format(date);
            Toast.makeText(getActivity(), "Checking date should be greater than or equal " + todayDate,
                    Toast.LENGTH_LONG).show();
        } else if (!tv_checkin_date.getText().equals("")
                && !Commons.isValidDate(tv_checkin_date.getText().toString(), tv_checkout_date.getText().toString())) {
            isValid = false;
            Toast.makeText(getActivity(), "Checking out should be greater than or equal Checking date",
                    Toast.LENGTH_LONG).show();
        }
        if (!tv_checkin_date.getText().toString().equals("") && !tv_checkout_date.getText().toString().equals("")) {
            isValid = validateBookingDate(tv_checkin_date.getText().toString(), tv_checkout_date.getText().toString());
            if (!isValid)
                Toast.makeText(getActivity(), "Booking is not available on selected date",
                        Toast.LENGTH_LONG).show();
        }
        if (isValid) {
            if (MainActivity.getNetworkHelper().isOnline()) {
                HashMap<String, String> postDataParams = new HashMap<String, String>();
                postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
                postDataParams.put("prID", prID);
                postDataParams.put("pbFromDate", Commons
                        .formatDateFromOnetoAnother(tv_checkin_date.getText().toString(), "dd/mm/yyyy", "yyyy-mm-dd"));// "2017-02-21");
                // //
                // yyyy/mm/dd
                postDataParams.put("pbToDate", Commons.formatDateFromOnetoAnother(tv_checkout_date.getText().toString(),
                        "dd/mm/yyyy", "yyyy-mm-dd"));
                postDataParams.put("pbDateOfBooking", Commons.getCurrentDate("yyyy-mm-dd"));
                postDataParams.put("pbTotalAmount", WeekendRate);
                postDataParams.put("pbTotalTaxAmount", WeekendRate);
                if (sp_agent.getSelectedItemPosition() == 0)
                    postDataParams.put("pbCreatedBy", MainActivity.getMainScreenActivity().getUserID());
                else
                    postDataParams.put("pbCreatedBy", agentlist.get(sp_agent.getSelectedItemPosition()).getAgentID());
                postDataParams.put("pbCustomerId", customer_id);
                postDataParams.put("BookAmount", WeekendRate);
                postDataParams.put("pmID", pdto.getPropertyID());

                postDataParams.put("PbCustomerName", edt_customername.getText().toString().trim());
                postDataParams.put("PbCustomerEmail", edt_email.getText().toString().trim());
                postDataParams.put("PbCustomerNumber", edt_mobile.getText().toString().trim());

                postDataParams.put("PbNoOfAdult", edt_noofadult.getText().toString().trim());
                postDataParams.put("PbNoOfChildren", edt_noofchildren.getText().toString().trim());
                new GetAgentTask(postDataParams, ADD_BOOKING).execute(Commons.ADD_BOOKING);
            } else {
                ShowAlertInformation.showNetworkDialog(getActivity());
            }
        }

    }

    @Override
    protected int getSelectedID() {
        return iv_book.getId();
    }

    public void showDatePicker(Context ctx, final TextView tv) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();

        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                tv.setText(dayOfMonth + "/" + checkDigit((monthOfYear + 1)) + "/" + year);

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void showTimePicker(Context ctx, final TextView tv) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                tv.setText(hourOfDay + ":" + minute);
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    @Override
    protected void propertySelected(PropertyDto pdto) {
        this.pdto = pdto;
        HashMap<String, String> postDataParams = new HashMap<String, String>();
        postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
        postDataParams.put("pmId", pdto.getPropertyID());
        new GetAgentTask(postDataParams, PROPERTY_SELECT).execute(Commons.GET_PROPERTY_RATE_LIST);
        new CalanderTask(postDataParams).execute(Commons.GET_CALANDER_DETAILS);
        Picasso.with(getActivity()).load(Commons.IMAGE_BASE_URL + pdto.getPropertyImage()).resize(100, 100)
                .into(iv_thumb);

    }

    private class GetAgentTask extends AsyncProcess {
        private int senario = -1;
        private ProgressDialog progressDialog1;

        public GetAgentTask(HashMap<String, String> postDataParams, int senario) {
            super(postDataParams);
            this.senario = senario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog1 = ProgressDialog.show(getActivity(), "", "please wait...");
            progressDialog1.setCancelable(true);
            progressDialog1.setCanceledOnTouchOutside(false);
            progressDialog1.setOnCancelListener(cancelListener);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (senario == AGENT_CALL) {
                if (200 == responseCode) {

                    String value = result.replace("\\", "");
                    if (value.length() > 2)
                        value = value.substring(1, value.length() - 1);
                    try {
                        JSONArray jaaray = new JSONArray(value);

                        agentlist.clear();

                        for (int i = 0; i < jaaray.length(); i++) {
                            try {
                                JSONObject jo = jaaray.getJSONObject(i);
                                agentlist.add(new AgentDto(jo.getString("Id"), jo.getString("emailId")));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        agentlist.add(0, new AgentDto("-1", "Self"));
                        AgentSpinAdapter propertyAdapter = new AgentSpinAdapter(getActivity(), agentlist);
                        sp_agent.setAdapter(propertyAdapter);
                        progressDialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        agentlist.add(0, new AgentDto("-1", "Self"));
                        AgentSpinAdapter propertyAdapter = new AgentSpinAdapter(getActivity(), agentlist);
                        sp_agent.setAdapter(propertyAdapter);
                        ShowAlertInformation.showDialog(getActivity(), "Error", "Error in data !!!");
                        progressDialog1.dismiss();
                    }
                    System.out.println("AgentTask result is : " + (result == null ? "" : result));
                    progressDialog1.dismiss();
                } else {
                    initAgentSpinner();
                    Log.i("AgentTask response", result == null ? "" : result);
                    ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
                    progressDialog1.dismiss();
                }
            }
            if (senario == ADD_BOOKING) {
                if (200 == responseCode) {

                    String value = result.replace("\\", "");
                    if (value.length() > 2)
                        value = value.substring(1, value.length() - 1);
                    try {
                        JSONObject jo = new JSONObject(value);
                        String status = jo.getString("status");
                        if (status.equals("Success")) {

                            ShowAlertInformation.showDialogAndFinish(getActivity(), "Success", "Booking done successfully !!!");
                            HashMap<String, String> postDataParams = new HashMap<String, String>();
                            new GetAgentTask(postDataParams, SEND_MSG).execute(Commons.sendMessage(
                                    edt_mobile.getText().toString(),
                                    tv_checkin_date.getText().toString() + "-" + tv_checkout_date.getText().toString(),
                                    " Confirmed."));
                            edt_noofadult.setText("");
                            edt_noofchildren.setText("");
                            edt_customername.setText("");
                            edt_address.setText("");
                            edt_mobile.setText("");
                            edt_email.setText("");
                            tv_checkin_date.setText("");
                            tv_checkout_date.setText("");
                            customer_id = "";
                        } else {
                            ShowAlertInformation.showDialog(getActivity(), "Error", "Booking failed");
                        }
                        progressDialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ShowAlertInformation.showDialog(getActivity(), "Error", "Booking failed");
                        progressDialog1.dismiss();
                    }
                    System.out.println("BookingTask result is : " + (result == null ? "" : result));
                    progressDialog1.dismiss();
                } else {
                    initAgentSpinner();
                    Log.i("LocationTask response", result == null ? "" : result);
                    ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
                    progressDialog1.dismiss();
                }
            } else if (senario == PROPERTY_SELECT) {
                if (200 == responseCode) {

                    String value = result.replace("\\", "");
                    if (value.length() > 2)
                        value = value.substring(1, value.length() - 1);
                    try {
                        JSONArray jaaray = new JSONArray(value);
                        try {
                            JSONObject jo = jaaray.getJSONObject(0);
                            String status = jo.getString("status");

                            if (status.equals("Success")) {
                                prID = jo.getString("prID");
                                prRatePerNight = jo.getString("prRatePerNight");
                                prExtraPersonRate = jo.getString("prExtraPersonRate");
                                WeekendRate = jo.getString("WeekendRate");
                                ExtraWeekendRate = jo.getString("ExtraWeekendRate");
                                ExtraHolidayRate = jo.getString("ExtraHolidayRate");
                                FoodRatePerPerson = jo.getString("FoodRatePerPerson");

                                tv_location.setText("Location : " + pdto.getPropertyName()
                                        + "\nMonth - " + Commons.getCurrentMonth() + " \nWeek Days Rates-" + prRatePerNight + "\nWeek End Rates-"
                                        + WeekendRate + "\nMinimum No of Adults-" + pdto.getPropertymin_capacity()
                                        + "\nMaximum No Of Adults-" + pdto.getPropertymax_capacity());

                            } else {
                                ShowAlertInformation.showDialog(getActivity(), "Error", "Error in data !!!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        progressDialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ShowAlertInformation.showDialog(getActivity(), "Error", "Error in data !!!");
                        progressDialog1.dismiss();
                    }
                    System.out.println("property rate result is : " + (result == null ? "" : result));
                    progressDialog1.dismiss();
                } else {
                    Log.i("property rate  response", result == null ? "" : result);
                    ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
                    progressDialog1.dismiss();
                }
            } else if (senario == GET_CUSTOMER_INFO) {
                if (200 == responseCode) {

                    String value = result.replace("\\", "");
                    if (value.length() > 2)
                        value = value.substring(1, value.length() - 1);
                    try {
                        JSONArray jaaray = new JSONArray(value);
                        try {
                            JSONObject jo = jaaray.getJSONObject(0);
                            String status = jo.getString("status");

                            if (status.equals("Success")) {

                                // "[{\"id\": \"4\",\"Name\":
                                // \"ashok\",\"Email\":
                                // \"ashok.yadav@gmail.com\"}]"
                                edt_customername.setText(jo.getString("Name"));
                                edt_email.setText(jo.getString("Email"));
                                customer_id = jo.getString("id");

                            } else {
                                ShowAlertInformation.showDialog(getActivity(), "Error", "User info not found");
                                edt_customername.setText("");
                                edt_email.setText("");
                                customer_id = "";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            customer_id = "";
                        }

                        progressDialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ShowAlertInformation.showDialog(getActivity(), "Error", "User info not found");
                        progressDialog1.dismiss();
                    }
                    System.out.println("property rate result is : " + (result == null ? "" : result));
                    progressDialog1.dismiss();
                } else {
                    Log.i("customer info response", result == null ? "" : result);
                    ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
                    progressDialog1.dismiss();
                }
            }
            if (senario == SEND_MSG) {
                if (200 == responseCode) {
                    ShowAlertInformation.showDialog(getActivity(), "Success", "Message send to customer successfully");
                    progressDialog1.dismiss();
                } else {
                    ShowAlertInformation.showDialog(getActivity(), "Error", "Error in message sending.");
                    progressDialog1.dismiss();
                }
            }

            if (senario == AGENT_CALL) {
                if (200 == responseCode) {

                    String value = result.replace("\\", "");
                    if (value.length() > 2)
                        value = value.substring(1, value.length() - 1);
                    try {
                        JSONArray jaaray = new JSONArray(value);

                        agentlist.clear();

                        for (int i = 0; i < jaaray.length(); i++) {
                            try {
                                JSONObject jo = jaaray.getJSONObject(i);
                                agentlist.add(new AgentDto(jo.getString("Id"), jo.getString("emailId")));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        agentlist.add(0, new AgentDto("-1", "Self"));
                        AgentSpinAdapter propertyAdapter = new AgentSpinAdapter(getActivity(), agentlist);
                        sp_agent.setAdapter(propertyAdapter);
                        progressDialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        agentlist.add(0, new AgentDto("-1", "Self"));
                        AgentSpinAdapter propertyAdapter = new AgentSpinAdapter(getActivity(), agentlist);
                        sp_agent.setAdapter(propertyAdapter);
                        ShowAlertInformation.showDialog(getActivity(), "Error", "Error in data !!!");
                        progressDialog1.dismiss();
                    }
                    System.out.println("LocationTask result is : " + (result == null ? "" : result));
                    progressDialog1.dismiss();
                } else {
                    initAgentSpinner();
                    Log.i("LocationTask response", result == null ? "" : result);
                    ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
                    progressDialog1.dismiss();
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

    private void initAgentSpinner() {
        agentlist.clear();
        agentlist.add(0, new AgentDto("-1", "Self"));
        AgentSpinAdapter propertyAdapter = new AgentSpinAdapter(getActivity(), agentlist);
        sp_agent.setAdapter(propertyAdapter);

    }

    private int getValue(EditText edt) {
        String val = edt.getText().toString().trim();
        if (val.equals(""))
            return 0;
        else
            return Integer.parseInt(val);

    }

    private class GenericTextWatcher implements TextWatcher {

        private EditText genericEditText;

        public GenericTextWatcher(EditText edt) {
            genericEditText = edt;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            genericEditText.removeTextChangedListener(this);
            tv_totalperson.setText(String.valueOf(getValue(edt_noofadult) + getValue(edt_noofchildren)));
            try {
                if (Integer.parseInt(
                        pdto.getPropertymin_capacity()) < (getValue(edt_noofadult) + getValue(edt_noofchildren) / 2))
                    Toast.makeText(getActivity(), "No of person exceeded, Extra Charge will be apply",
                            Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // TODO: handle exception
            }
            genericEditText.addTextChangedListener(this);

        }

    }

    private class GenericTextWatcher1 implements TextWatcher {

        private EditText genericEditText;

        public GenericTextWatcher1(EditText edt) {
            genericEditText = edt;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            genericEditText.removeTextChangedListener(this);
            if (s.length() == 10) {
                HashMap<String, String> postDataParams = new HashMap<String, String>();
                postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
                postDataParams.put("phoneNumber", edt_mobile.getText().toString());
                new GetAgentTask(postDataParams, GET_CUSTOMER_INFO).execute(Commons.GET_CUSTOMER_DETAILS);
            }
            genericEditText.addTextChangedListener(this);

        }

    }

    private class CalanderTask extends AsyncProcess {

        private ProgressDialog progressDialog;

        public CalanderTask(HashMap<String, String> postDataParams) {
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

                    try {
                        cdata.clear();
                        for (int i = 0; i < jaaray.length(); i++) {
                            JSONObject jo = jaaray.getJSONObject(i);
                            String pbFromDate = jo.getString("pbFromDate");
                            String pbToDate = jo.getString("pbToDate");
                            String AdminApproval = jo.getString("AdminApproval");
                            String pbId = jo.getString("pbId");
                            String mobile = jo.getString("Mobile");
                            String PbNoOfAdult = jo.getString("PbNoOfAdult");
                            String PbNoOfChildren = jo.getString("PbNoOfChildren");
                            CalanderDateDto cdd = new CalanderDateDto(pbFromDate, pbToDate, AdminApproval, pbId, mobile,
                                    PbNoOfAdult, PbNoOfChildren);
                            if (!pbFromDate.equals("") && pbFromDate.contains(" ") && !pbToDate.equals("")
                                    && pbToDate.contains(" ")) {
                                setCalanderStatus(pbFromDate.split(" ")[0], pbToDate.split(" ")[0], AdminApproval, cdd);
                            }
                            // tv_start_date.setText(dateString(pbFromDate));
                            // tv_end_date.setText(dateString(pbToDate));
                        }

                        setCurrentDateColor("");
                        caldroidFragment.refreshView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
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

    private void setCalanderStatus(String str_date1, String end_date1, String status, CalanderDateDto cdd) {
        List<Date> dates = new ArrayList<Date>();

        // String str_date ="27/08/2010";
        // String end_date ="02/09/2010";
        String str_date = str_date1.replaceAll("-", "/");
        String end_date = end_date1.replaceAll("-", "/");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date startDate = (Date) formatter.parse(str_date);
            Date endDate = (Date) formatter.parse(end_date);

            long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here,
            // possibly using Calendar or
            // Date
            long curTime = startDate.getTime();
            while (curTime <= endTime) {
                dates.add(new Date(curTime));
                curTime += interval;
            }

            for (int i = 0; i < dates.size(); i++) {
                Date date = (Date) dates.get(i);
                String ds = formatter.format(date);
                System.out.println(" Date is ..." + ds + " & status is :" + status);
                String key = "";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

                simpleDateFormat.applyPattern("MMM");
                key = simpleDateFormat.format(date);
                simpleDateFormat.applyPattern("yyyy");
                key = key + simpleDateFormat.format(date);

                if (cdata.get(key) == null) {
                    HashMap<Date, CalanderDateDto> cdto = new HashMap();
                    cdto.put(date, cdd);
                    cdata.put(key, cdto);
                } else {
                    HashMap<Date, CalanderDateDto> cdto = cdata.get(key);
                    cdto.put(date, cdd);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearCurrentDateColor(String key) {

        HashMap<Date, CalanderDateDto> cdto = cdata.get(key);
        if (null != cdto)
            for (Entry<Date, CalanderDateDto> entry : cdto.entrySet()) {
                Date keys = entry.getKey();
                caldroidFragment.clearBackgroundDrawableForDate(keys);
            }
        caldroidFragment.refreshView();
    }

    private void setCurrentDateColor(String string) {
        // TODO Auto-generated method stub
        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("MMM");
        String key = simpleDateFormat.format(date1);
        simpleDateFormat.applyPattern("yyyy");
        key = key + simpleDateFormat.format(date1);
        if (!string.equals(""))
            key = string;
        else
            currentkey = key;
        previouskey = key;
        HashMap<Date, CalanderDateDto> cdto = cdata.get(key);
        Drawable red = getResources().getDrawable(R.drawable.red_cell);
        Drawable yellow = getResources().getDrawable(R.drawable.yellow_cell);
        Drawable orange = getResources().getDrawable(R.drawable.orange_cell);
        if (null != cdto)
            for (Entry<Date, CalanderDateDto> entry : cdto.entrySet()) {
                Date keys = entry.getKey();
                CalanderDateDto value = entry.getValue();
                if (value.getAdminApproval().equalsIgnoreCase("Awaiting Approval")) {
                    caldroidFragment.setBackgroundDrawableForDate(orange, keys);
                } else if (value.getAdminApproval().equalsIgnoreCase("Awaiting Payment")) {
                    caldroidFragment.setBackgroundDrawableForDate(yellow, keys);
                } else if (value.getAdminApproval().equalsIgnoreCase("Booked")) {
                    caldroidFragment.setBackgroundDrawableForDate(red, keys);
                }
            }
        caldroidFragment.refreshView();
    }

    private boolean validateBookingDate(String fromdate, String todate) {

        List<Date> dates = new ArrayList<Date>();

        // String str_date ="27/08/2010";
        // String end_date ="02/09/2010";
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date startDate = (Date) formatter.parse(fromdate);//sd[1]+"/"+sd[0]+"/"+sd[2]);
            Date endDate = (Date) formatter.parse(todate);//ed[1]+"/"+ed[0]+"/"+ed[2]);

            long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here,
            // possibly using Calendar or
            // Date
            long curTime = startDate.getTime();
            while (curTime <= endTime) {
                dates.add(new Date(curTime));
                curTime += interval;
            }
            for (int i = 0; i < dates.size(); i++) {
                Date date = (Date) dates.get(i);
                String ds = formatter.format(date);
                String key = "";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

                simpleDateFormat.applyPattern("MMM");
                key = simpleDateFormat.format(date);
                simpleDateFormat.applyPattern("yyyy");
                key = key + simpleDateFormat.format(date);
                CalanderDateDto selectedcdd = null;
                if (cdata.get(currentkey) != null) {
                    HashMap<Date, CalanderDateDto> cdto = cdata.get(currentkey);
                    selectedcdd = cdto.get(date);
                }
                if (selectedcdd == null) {
                    Log.v("find date is :", "null");
                } else {
                    Log.v("find date is :", "having valued");
                    return false;
                }
            }
//			for (int i = 0; i < dates.size(); i++) {
//				Date date = (Date) dates.get(i);
//				String ds = formatter.format(date);
//				String key = "";
//				SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
//
//				simpleDateFormat.applyPattern("MMM");
//				key = simpleDateFormat.format(date);
//				simpleDateFormat.applyPattern("yyyy");
//				key = key + simpleDateFormat.format(date);
//
//				if (cdata.get(key) == null) {
//					Log.v("find date is :","null");
//				} else {
//					Log.v("find date is :","having valued");
//					return false;
//				}
//
//			}
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void locationSelect(String location) {

    }
}

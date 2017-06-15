
package com.gatewayclub.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gatewayclub.app.R;
import com.gatewayclub.app.adapters.GridViewAdapter;
import com.gatewayclub.app.adapters.MyHorizontalScrollAdapter;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ExpandableHeightGridView;
import com.gatewayclub.app.helper.MyHorizontalScrollview;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.pojos.Bedroom;
import com.gatewayclub.app.pojos.FacilityDto;
import com.gatewayclub.app.pojos.Food;
import com.gatewayclub.app.pojos.GridItemDto;
import com.gatewayclub.app.pojos.Hall;
import com.gatewayclub.app.pojos.IndoorGames;
import com.gatewayclub.app.pojos.Kitchen;
import com.gatewayclub.app.pojos.Medical;
import com.gatewayclub.app.pojos.OutdoorGames;
import com.gatewayclub.app.pojos.PropertyDto;
import com.gatewayclub.app.pojos.PropertyImageDto;
import com.gatewayclub.app.pojos.Tourism;
import com.gatewayclub.app.pojos.Transport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class EditFragment extends BaseFragment implements View.OnClickListener {

    private Button btn_editphoto, btn_submit;
    private ImageButton btnPrev, btnNext;
    private int currIndex = 0;
    private ArrayList<PropertyImageDto> propertyimagelist = new ArrayList<PropertyImageDto>();
    private MyHorizontalScrollview myHorizontalScrollview;
    private ExpandableHeightGridView mGridView;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItemDto> mGridData;
    private EditText edt_weekdays, edt_extra_person, edt_weekend_holiday;
    private String prRatePerNight, prExtraPersonRate, WeekendRate, ExtraWeekendRate, ExtraHolidayRate, FoodRatePerPerson;
    private PropertyDto pdto;
    private int GET_IMAGE_LIST = 0;
    private int SUBMIT_RATE = 1;
    private int UPDATE_DATA = 2;
    private FacilityDto facilityDto;
    private TextView tv_location;

    private final int CASE_BEDROOM = 0;
    private final int CASE_HALL = 1;
    private final int CASE_KITCHEN = 2;
    private final int CASE_FOOD = 3;
    private final int CASE_TRANSPORT = 4;
    private final int CASE_MEDICAL = 5;
    private final int CASE_INDOOR = 6;
    private final int CASE_OUTDOOR = 7;
    private final int CASE_TOURSIM = 8;

    String[] gridtitle = {"Bedroom", "Hall", "Kitchen", "Food", "Transport", "Medical", "Indoor Games", "Outdoor Games", "Tourism"};
    String[] hall = new String[]{"Hall No.", "Hall Size", "Bathroom", "Gyser", "Western toilet", "Indian toilet", "AC", "Balcony", "TV", "Music System", "Dinning table", "Fan", "Terrace Varandah"};
    String[] bedroom = new String[]{"Bedroom No.", "Bed Size", "Bathroom", "Gyser", "Western toilet", "Indian toilet", "AC", "Balcony", "TV", "Jacuzzi", "Fan", "Curtains"};
    String[] kitchen = new String[]{"Microwave", "Utensils", "Water filter","Cooking Gas", "crockery", "Drinking glasses", "Refrigerator"};
    String[] food = new String[]{"Non veg", "Jain food", "Veg food", "Inhouse cooking", "catering service", "self cooking"};
    String[] medical = new String[]{"Doctor", "Medical shop", "Hospital", "Large Hospital"};
    String[] indoorGames = new String[]{"Carrom", "Table Tennis", "Chess", "Ludo", "Playing cards", "Monopoly", "Video Games"};
    String[] outdoorGames = new String[]{"Cricket", "Cricket Equipments", "football", "football equipments", "badminton", "badminton equipments"};
    String[] transport = new String[]{"Taxi", "Autostand", "Busstand", "Railway station"};
    String[] tourism = new String[]{"Beach", "Nearest River", "Famous temple", "Historical Place", "Lake / River", "Boating", "Amusement park", "Horse Ridding",
            "Water sport", "Forest", "Sanctuary", "Tourist point"};

    String data[][] = new String[][]{bedroom, hall, kitchen, food, transport, medical, indoorGames, outdoorGames, tourism};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        super.onCreateView(inflater, container, args);
        View view = inflater.inflate(R.layout.frag_edit, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        btn_editphoto = (Button) view.findViewById(R.id.btn_editphoto);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        myHorizontalScrollview = (MyHorizontalScrollview) view.findViewById(R.id.horizontalScrollView);
        mGridView = (ExpandableHeightGridView) view.findViewById(R.id.mGridView);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);
        btnPrev = (ImageButton) view.findViewById(R.id.btnPrev);
        edt_weekdays = (EditText) view.findViewById(R.id.edt_weekdays);
        edt_extra_person = (EditText) view.findViewById(R.id.edt_extra_person);
        edt_weekend_holiday = (EditText) view.findViewById(R.id.edt_weekend_holiday);
        tv_location =(TextView)view.findViewById(R.id.tv_location);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_editphoto.setOnClickListener(this);


        initGrid();
        // Initialize with empty data
        mGridView.setExpanded(true);
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_img_txt_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(null!=pdto) {
                    GridItemDto item = (GridItemDto) parent.getItemAtPosition(position);
                    facilityDialog(getActivity(), gridtitle[position], item.getFacility(), position);
                }else{
                    Toast.makeText(getActivity(),"Please select Property first. ",Toast.LENGTH_LONG).show();
                }
            }
        });
        addView(view);
        return rootview;
    }

    private void initGrid() {

        mGridData = new ArrayList<>();
        for (int i = 0; i < gridtitle.length; i++) {
            GridItemDto item = new GridItemDto();
            item.setTitle(gridtitle[i]);
            item.setImage("http://www.nag.co.in/images/house.png");
            item.setFacility(data[i]);
            mGridData.add(item);
        }
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
            case R.id.btn_submit:

                if ((edt_extra_person.getText().toString().trim().equals(prExtraPersonRate) && edt_weekdays.getText().toString().trim().equals(prRatePerNight)
                        && edt_weekend_holiday.getText().toString().trim().equals(WeekendRate))) {
                    //do nothing
                } else {
                    if ((edt_extra_person.getText().toString().trim().equals("") || edt_weekdays.getText().toString().trim().equals("") || edt_weekend_holiday.getText().toString().trim().equals(""))) {
                        ShowAlertInformation.showDialog(getActivity(), "Mandatory fields", "Blank price not allowed");
                    } else {
                        if (MainActivity.getNetworkHelper().isOnline()) {
                            HashMap<String, String> postDataParams = new HashMap<String, String>();
                            postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
                            postDataParams.put("pmId", pdto.getPropertyID());
                            postDataParams.put("pmWeekend", edt_weekend_holiday.getText().toString());
                            postDataParams.put("pmWeekday", edt_weekdays.getText().toString());
                            postDataParams.put("pmExtraperson", edt_extra_person.getText().toString());
                            new GetImageTask(postDataParams, SUBMIT_RATE).execute(Commons.UPDATE_RATES);

                        } else {
                            ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void propertySelected(PropertyDto pdto) {
        this.pdto = pdto;
        callImageData();

    }

    private void callImageData() {
        if (MainActivity.getNetworkHelper().isOnline()) {
            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
            postDataParams.put("pmID", pdto.getPropertyID());
            new GetImageTask(postDataParams, GET_IMAGE_LIST).execute(Commons.GET_BUNGLOWIMAGE_LIST);

        } else {
            ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
        }
    }

    private class GetImageTask extends AsyncProcess {

        ProgressDialog progressDialog;
        int senario = -1;

        public GetImageTask(HashMap<String, String> postDataParams, int senario) {
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

                if (senario == GET_IMAGE_LIST) {
                    try {
                        JSONObject jsonObject = new JSONObject(value);
                        JSONArray jaaray = jsonObject.getJSONArray("Images");
                        prRatePerNight = jsonObject.getString("prRatePerNight");
                        prExtraPersonRate = jsonObject.getString("prExtraPersonRate");
                        WeekendRate = jsonObject.getString("WeekendRate");
                        ExtraWeekendRate = jsonObject.getString("ExtraWeekendRate");
                        ExtraHolidayRate = jsonObject.getString("ExtraHolidayRate");
                        FoodRatePerPerson = jsonObject.getString("FoodRatePerPerson");
                        propertyimagelist.clear();
                        for (int i = 0; i < jaaray.length(); i++) {
                            try {
                                JSONObject jo = jaaray.getJSONObject(i);
                                propertyimagelist.add(new PropertyImageDto(jo.getString("pdId"), jo.getString("pdImage")));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        facilityDto = new FacilityDto();

                        //Transport
                        Transport tp = new Transport();
                        JSONObject jtp = jsonObject.getJSONObject("Transport");
                        tp.setPMTransAutoStand(jtp.getString("PMTransAutoStand"));
                        tp.setPMTransBusStand(jtp.getString("PMTransBusStand"));
                        tp.setPMTransRailwayStation(jtp.getString("PMTransRailwayStation"));
                        tp.setPMTransTaxi(jtp.getString("PMTransTaxi"));
                        facilityDto.setTransport(tp);

                        //Medical
                        Medical md = new Medical();
                        JSONObject jtm = jsonObject.getJSONObject("Medical");
                        md.setPMedDoctor(jtm.getString("PMedDoctor"));
                        md.setPMedHospital(jtm.getString("PMedMedicalShop"));
                        md.setPMedLargeHospital(jtm.getString("PMedLargeHospital"));
                        md.setPMedMedicalShop(jtm.getString("PMedHospital"));
                        facilityDto.setMedical(md);

                        //IndoorGames
                        IndoorGames ig = new IndoorGames();
                        JSONObject jtig = jsonObject.getJSONObject("IndoorGames");
                        ig.setPGCarrom(jtig.getString("PGCarrom"));
                        ig.setPGChess(jtig.getString("PGChess"));
                        ig.setPGLudo(jtig.getString("PGLudo"));
                        ig.setPGMonopoly(jtig.getString("PGMonopoly"));
                        ig.setPGPlayingCards(jtig.getString("PGPlayingCards"));
                        ig.setPGTableTennis(jtig.getString("PGTableTennis"));
                        ig.setPGVideoGames(jtig.getString("PGVideoGames"));
                        facilityDto.setIndoorGames(ig);

                        //outdoors
                        OutdoorGames og = new OutdoorGames();
                        JSONObject jtog = jsonObject.getJSONObject("OutdoorGames");
                        og.setPOGBadminton(jtog.getString("POGBadminton"));
                        og.setPOGBadmintonEquipment(jtog.getString("POGBadmintonEquipment"));
                        og.setPOGBasketball(jtog.getString("POGBasketball"));
                        og.setPOGBasketballEquipment(jtog.getString("POGBasketballEquipment"));
                        og.setPOGCricket(jtog.getString("POGCricket"));
                        og.setPOGCricketEquipment(jtog.getString("POGCricketEquipment"));
                        og.setPOGFootball(jtog.getString("POGFootball"));
                        og.setPOGFootballEquipment(jtog.getString("POGFootballEquipment"));
                        facilityDto.setOutdoorGames(og);
                        //Tourism
                        Tourism tm = new Tourism();
                        JSONObject jttm = jsonObject.getJSONObject("Tourism");
                        tm.setPTNearestAmusmentPark(jttm.getString("PTNearestAmusmentPark"));
                        tm.setPTNearestBeach(jttm.getString("PTNearestBeach"));
                        tm.setPTNearestBoating(jttm.getString("PTNearestBoating"));
                        tm.setPTNearestFamousTemple(jttm.getString("PTNearestFamousTemple"));
                        tm.setPTNearestForest(jttm.getString("PTNearestForest"));
                        tm.setPTNearestHistoricalPlace(jttm.getString("PTNearestHistoricalPlace"));
                        tm.setPTNearestHorseRiding(jttm.getString("PTNearestHorseRiding"));
                        tm.setPTNearestLakeDam(jttm.getString("PTNearestLakeDam"));
                        tm.setPTNearestRiver(jttm.getString("PTNearestRiver"));
                        tm.setPTNearestsanctuary(jttm.getString("PTNearestsanctuary"));
                        tm.setPTNearestTouristPoints(jttm.getString("PTNearestTouristPoints"));
                        tm.setPTNearestWatersports(jttm.getString("PTNearestWatersports"));
                        facilityDto.setTourism(tm);

                        //Bedroom
                        Bedroom bedroom = new Bedroom();
                        JSONObject jobjbdm = jsonObject.getJSONObject("BedRoom");
                        bedroom.setPbdBedRoomNo(jobjbdm.getString("PbdBedRoomNo"));
                        bedroom.setPbdBedSize(jobjbdm.getString("PbdBedSize"));
                        bedroom.setPbdBathroom(jobjbdm.getString("PbdBathroom"));
                        bedroom.setPbdGeyser(jobjbdm.getString("PbdGeyser"));
                        bedroom.setPbdWaternToilet(jobjbdm.getString("PbdWaternToilet"));
                        bedroom.setPbdIndianToilet(jobjbdm.getString("PbdIndianToilet"));
                        bedroom.setPbdAC(jobjbdm.getString("PbdAC"));
                        bedroom.setPbdBalcony(jobjbdm.getString("PbdBalcony"));
                        bedroom.setPbdTV(jobjbdm.getString("PbdTV"));
                        bedroom.setPbdJacuzzi(jobjbdm.getString("PbdJacuzzi"));
                        bedroom.setPbdFAN(jobjbdm.getString("PbdFAN"));
                        bedroom.setPbdCurtains(jobjbdm.getString("PbdCurtains"));
                        facilityDto.setBedroom(bedroom);

                        //Kitchen
                        Kitchen kitchen = new Kitchen();
                        JSONObject jobjktc = jsonObject.getJSONObject("Kitchen");
                        kitchen.setPkdMicrowave(jobjktc.getString("PkdMicrowave"));
                        kitchen.setPkdUtensils(jobjktc.getString("PkdUtensils"));
                        kitchen.setPkdWaterFilter(jobjktc.getString("PkdWaterFilter"));
                        kitchen.setPkdCookingGas(jobjktc.getString("PkdCookingGas"));
                        kitchen.setPkdCrockery(jobjktc.getString("PkdCrockery"));
                        kitchen.setPkdDrinkingGlasses(jobjktc.getString("PkdDrinkingGlasses"));
                        kitchen.setPkdRefrigarator(jobjktc.getString("PkdRefrigarator"));
                        facilityDto.setKitchen(kitchen);

                        //Food
                        Food food = new Food();
                        JSONObject jobjfood = jsonObject.getJSONObject("Food");
                        food.setFDFNonVeg(jobjfood.getString("FDFNonVeg"));
                        food.setFDFJainFood(jobjfood.getString("FDFJainFood"));
                        food.setFDFVeg(jobjfood.getString("FDFVeg"));
                        food.setFDFInhouseCooking(jobjfood.getString("FDFInhouseCooking"));
                        food.setFDFCateringService(jobjfood.getString("FDFCateringService"));
                        food.setFDFSelfCooking(jobjfood.getString("FDFSelfCooking"));
                        facilityDto.setFood(food);

                        //Hall
                        Hall hall = new Hall();
                        JSONObject jobjhall = jsonObject.getJSONObject("Hall");

                        hall.setHallNo(jobjhall.getString("HallNo"));
                        hall.setPhdHallSize(jobjhall.getString("phdHallSize"));
                        hall.setPhdBathroom(jobjhall.getString("PhdBathroom"));
                        hall.setPhdGeyser(jobjhall.getString("PhdGeyser"));
                        hall.setPhdWesternToilet(jobjhall.getString("PhdWesternToilet"));
                        hall.setPhdIndianToilet(jobjhall.getString("PhdIndianToilet"));
                        hall.setPhdAC(jobjhall.getString("PhdAC"));
                        hall.setPhdBalcony(jobjhall.getString("PhdBalcony"));
                        hall.setPhdTV(jobjhall.getString("PhdTV"));
                        hall.setPhdBasicMusicSystem(jobjhall.getString("PhdBasicMusicSystem"));
                        hall.setPhdDiningTable(jobjhall.getString("PhdDiningTable"));
                        hall.setPhdFAN(jobjhall.getString("PhdFAN"));
                        hall.setPhdTerraceVerandah(jobjhall.getString("PhdTerraceVerandah"));
                        facilityDto.setHall(hall);

                        edt_weekdays.setText(prRatePerNight);
                        edt_extra_person.setText(prExtraPersonRate);
                        edt_weekend_holiday.setText(WeekendRate);
                        MyHorizontalScrollAdapter adapter = new MyHorizontalScrollAdapter(getActivity(), propertyimagelist);
                        myHorizontalScrollview.setAdapter(getActivity(), adapter);
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ShowAlertInformation.showDialog(getActivity(), "Error", "Error in data !!!");
                        progressDialog.dismiss();
                    }
                } else if (senario == SUBMIT_RATE) {
                    try {
                        JSONObject jo = new JSONObject(value);
                        String status = jo.getString("status");
                        if (status.equals("Success")) {
                            ShowAlertInformation.showDialog(getActivity(), "Success", "Price updated successfully!!!");
                        } else {
                            ShowAlertInformation.showDialog(getActivity(), "Error", "Error while updating the data !!!");
                        }
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (senario == UPDATE_DATA) {
                    try {
                        Log.v("Response of dialog",value);
                        JSONObject jo = new JSONObject(value);
                        String status = jo.getString("status");
                        if (status.equals("Success")) {
                            ShowAlertInformation.showDialog(getActivity(), "Success", "Info updated successfully!!!");
                        } else {
                            ShowAlertInformation.showDialog(getActivity(), "Error", "Error while updating the data !!!");
                        }
                        progressDialog.dismiss();
                        callImageData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    private void facilityDialog(final Context mContext, String title, String[] items, final int position) {

        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.facility_dialog);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout ll_center = (LinearLayout) dialog.findViewById(R.id.ll_center);
        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_header);
        tv_header.setText(title);
        final ArrayList<View> rb_list = new ArrayList<>();
        Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> postDataParams = new HashMap<String, String>();
                postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
                postDataParams.put("pmID", pdto.getPropertyID());
                postDataParams.put("statusUpdate", "0");
                String url = "";
                switch (position) {
                    case CASE_MEDICAL: {
                        url = "updateMedicalFacility";
                        postDataParams.put("PMedDoctor", ((RadioButton)rb_list.get(0)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PMedMedicalShop", ((RadioButton)rb_list.get(1)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PMedHospital", ((RadioButton)rb_list.get(2)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PMedLargeHospital", ((RadioButton)rb_list.get(3)).isChecked() ? "YES" : "NO");
                    }
                    break;
                    case CASE_TRANSPORT: {
                        url = "updateTransportFacility";
                        postDataParams.put("PMTransTaxi", ((RadioButton)rb_list.get(0)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PMTransAutoStand", ((RadioButton)rb_list.get(1)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PMTransBusStand", ((RadioButton)rb_list.get(2)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PMTransRailwayStation", ((RadioButton)rb_list.get(3)).isChecked() ? "YES" : "NO");
                    }
                    break;
                    case CASE_INDOOR: {
                        url = "updateIndoorGamesFacility";
                        postDataParams.put("PGCarrom", ((RadioButton)rb_list.get(0)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PGTableTennis", ((RadioButton)rb_list.get(1)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PGChess", ((RadioButton)rb_list.get(2)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PGLudo", ((RadioButton)rb_list.get(3)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PGPlayingCards", ((RadioButton)rb_list.get(4)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PGMonopoly", ((RadioButton)rb_list.get(5)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PGVideoGames", ((RadioButton)rb_list.get(6)).isChecked() ? "YES" : "NO");
                    }
                    break;
                    case CASE_OUTDOOR: {
                        url = "updateOutdoorGamesFacility";
                        postDataParams.put("POGCricket", ((RadioButton)rb_list.get(0)).isChecked() ? "YES" : "NO");
                        postDataParams.put("POGCricketEquipment", ((RadioButton)rb_list.get(1)).isChecked() ? "YES" : "NO");
                        postDataParams.put("POGFootball", ((RadioButton)rb_list.get(2)).isChecked() ? "YES" : "NO");
                        postDataParams.put("POGFootballEquipment", ((RadioButton)rb_list.get(3)).isChecked() ? "YES" : "NO");
                        postDataParams.put("POGBadminton", ((RadioButton)rb_list.get(4)).isChecked() ? "YES" : "NO");
                        postDataParams.put("POGBadmintonEquipment", ((RadioButton)rb_list.get(5)).isChecked() ? "YES" : "NO");
                        //	postDataParams.put("POGBasketball", rb_list.get(6).isChecked()?"YES":"NO");
                        //	postDataParams.put("POGBasketballEquipment", rb_list.get(7).isChecked()?"YES":"NO");
                    }
                    break;
                    case CASE_TOURSIM: {
                        url = "updateTourismFacility";
                        postDataParams.put("PTNearestBeach", ((RadioButton)rb_list.get(0)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PTNearestRiver", ((RadioButton)rb_list.get(1)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PTNearestFamousTemple", ((RadioButton)rb_list.get(2)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PTNearestHistoricalPlace", ((RadioButton)rb_list.get(3)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PTNearestLakeDam", ((RadioButton)rb_list.get(4)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PTNearestBoating", ((RadioButton)rb_list.get(5)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PTNearestAmusmentPark", ((RadioButton)rb_list.get(6)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PTNearestHorseRiding", ((RadioButton)rb_list.get(7)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PTNearestWatersports", ((RadioButton)rb_list.get(8)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PTNearestForest", ((RadioButton)rb_list.get(9)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PTNearestsanctuary", ((RadioButton)rb_list.get(10)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PTNearestTouristPoints", ((RadioButton)rb_list.get(11)).isChecked() ? "YES" : "NO");
                    }
                    break;
                    case CASE_BEDROOM: {
                        url = "updateRoomFacility";
                        postDataParams.put("PbdBedRoomNo", ((Spinner)rb_list.get(0)).getSelectedItem().toString());
                        postDataParams.put("PbdBedSize", ((Spinner)rb_list.get(1)).getSelectedItem().toString());
                        postDataParams.put("PbdBathroom", ((Spinner)rb_list.get(2)).getSelectedItem().toString());
                        postDataParams.put("PbdGeyser", ((RadioButton)rb_list.get(3)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PbdWaternToilet", ((Spinner)rb_list.get(4)).getSelectedItem().toString());
                        postDataParams.put("PbdIndianToilet", ((Spinner)rb_list.get(5)).getSelectedItem().toString());
                        postDataParams.put("PbdAC", ((RadioButton)rb_list.get(6)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PbdBalcony", ((RadioButton)rb_list.get(7)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PbdTV", ((RadioButton)rb_list.get(8)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PbdJacuzzi", ((RadioButton)rb_list.get(9)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PbdCurtains", ((RadioButton)rb_list.get(10)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PbdFAN", ((RadioButton)rb_list.get(11)).isChecked() ? "YES" : "NO");
                    }
                    break;
                    case CASE_KITCHEN: {
                        url = "updateKitchenFacility";
                        postDataParams.put("PkdMicrowave", ((RadioButton)rb_list.get(0)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PkdUtensils", ((RadioButton)rb_list.get(1)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PkdWaterFilter", ((RadioButton)rb_list.get(2)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PkdCookingGas", ((RadioButton)rb_list.get(3)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PkdCrockery", ((RadioButton)rb_list.get(4)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PkdDrinkingGlasses", ((RadioButton)rb_list.get(5)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PkdRefrigarator", ((RadioButton)rb_list.get(6)).isChecked() ? "YES" : "NO");
                    }
                    break;

                    case CASE_FOOD: {
                        url = "updateFoodFacility";
                        postDataParams.put("FDFNonVeg", ((RadioButton)rb_list.get(0)).isChecked() ? "YES" : "NO");
                        postDataParams.put("FDFJainFood", ((RadioButton)rb_list.get(1)).isChecked() ? "YES" : "NO");
                        postDataParams.put("FDFVeg", ((RadioButton)rb_list.get(2)).isChecked() ? "YES" : "NO");
                        postDataParams.put("FDFInhouseCooking", ((RadioButton)rb_list.get(3)).isChecked() ? "YES" : "NO");
                        postDataParams.put("FDFCateringService", ((RadioButton)rb_list.get(4)).isChecked() ? "YES" : "NO");
                        postDataParams.put("FDFSelfCooking", ((RadioButton)rb_list.get(5)).isChecked() ? "YES" : "NO");
                    }
                    break;
                    case CASE_HALL: {
                        url = "updateHallFacility";
                        postDataParams.put("HallNo", ((Spinner)rb_list.get(0)).getSelectedItem().toString());
                        postDataParams.put("phdHallSize", ((Spinner)rb_list.get(1)).getSelectedItem().toString());
                        postDataParams.put("PhdBathroom", ((Spinner)rb_list.get(2)).getSelectedItem().toString());
                        postDataParams.put("PhdGeyser", ((RadioButton)rb_list.get(3)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PhdWesternToilet", ((Spinner)rb_list.get(4)).getSelectedItem().toString());
                        postDataParams.put("PhdIndianToilet", ((Spinner)rb_list.get(5)).getSelectedItem().toString());
                        postDataParams.put("PhdAC", ((RadioButton)rb_list.get(6)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PhdBalcony", ((RadioButton)rb_list.get(7)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PhdTV", ((RadioButton)rb_list.get(8)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PhdBasicMusicSystem", ((RadioButton)rb_list.get(9)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PhdDiningTable", ((RadioButton)rb_list.get(10)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PhdFAN", ((RadioButton)rb_list.get(11)).isChecked() ? "YES" : "NO");
                        postDataParams.put("PhdTerraceVerandah", ((RadioButton)rb_list.get(12)).isChecked() ? "YES" : "NO");
                    }
                    break;
                    default:

                }
                if (MainActivity.getNetworkHelper().isOnline()) {
                    new GetImageTask(postDataParams, UPDATE_DATA).execute(Commons.BASE_URL + url);
                    dialog.dismiss();
                } else {
                    ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
                }

            }
        });

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        for (int i = 0; i < items.length; i++) {
            View v = inflater.inflate(R.layout.cb_lay, null);
           // RadioGroup rg_yesno = (RadioGroup)v.findViewById(R.id.rg_yesno);
            RadioButton rb_yes = (RadioButton) v.findViewById(R.id.rb_yes);
            RadioButton rb_no = (RadioButton) v.findViewById(R.id.rb_no);
            rb_list.add(rb_yes);
            switch (position) {
                case CASE_TRANSPORT: {
                    if (i == 0) {
                        setCheck(facilityDto.getTransport().getPMTransTaxi(), rb_yes, rb_no);
                    } else if (i == 1) {
                        setCheck(facilityDto.getTransport().getPMTransAutoStand(), rb_yes, rb_no);
                    } else if (i == 2) {
                        setCheck(facilityDto.getTransport().getPMTransBusStand(), rb_yes, rb_no);
                    } else if (i == 3) {
                        setCheck(facilityDto.getTransport().getPMTransRailwayStation(), rb_yes, rb_no);
                    }
                }
                break;
                case CASE_MEDICAL: {
                    if (i == 0) {
                        setCheck(facilityDto.getMedical().getPMedDoctor(), rb_yes, rb_no);
                    } else if (i == 1) {
                        setCheck(facilityDto.getMedical().getPMedHospital(), rb_yes, rb_no);
                    } else if (i == 2) {
                        setCheck(facilityDto.getMedical().getPMedLargeHospital(), rb_yes, rb_no);
                    } else if (i == 3) {
                        setCheck(facilityDto.getMedical().getPMedMedicalShop(), rb_yes, rb_no);
                    }
                }
                break;
                case CASE_INDOOR: {
                    if (i == 0) {
                        setCheck(facilityDto.getIndoorGames().getPGCarrom(), rb_yes, rb_no);
                    } else if (i == 1) {
                        setCheck(facilityDto.getIndoorGames().getPGTableTennis(), rb_yes, rb_no);
                    } else if (i == 2) {
                        setCheck(facilityDto.getIndoorGames().getPGChess(), rb_yes, rb_no);
                    } else if (i == 3) {
                        setCheck(facilityDto.getIndoorGames().getPGLudo(), rb_yes, rb_no);
                    } else if (i == 4) {
                        setCheck(facilityDto.getIndoorGames().getPGPlayingCards(), rb_yes, rb_no);
                    } else if (i == 5) {
                        setCheck(facilityDto.getIndoorGames().getPGMonopoly(), rb_yes, rb_no);
                    } else if (i == 6) {
                        setCheck(facilityDto.getIndoorGames().getPGVideoGames(), rb_yes, rb_no);
                    }
                }
                break;
                case CASE_OUTDOOR: {
                    if (i == 0) {
                        setCheck(facilityDto.getOutdoorGames().getPOGCricket(), rb_yes, rb_no);
                    } else if (i == 1) {
                        setCheck(facilityDto.getOutdoorGames().getPOGCricketEquipment(), rb_yes, rb_no);
                    } else if (i == 2) {
                        setCheck(facilityDto.getOutdoorGames().getPOGFootball(), rb_yes, rb_no);
                    } else if (i == 3) {
                        setCheck(facilityDto.getOutdoorGames().getPOGFootballEquipment(), rb_yes, rb_no);
                    } else if (i == 4) {
                        setCheck(facilityDto.getOutdoorGames().getPOGBadminton(), rb_yes, rb_no);
                    } else if (i == 5) {
                        setCheck(facilityDto.getOutdoorGames().getPOGBadmintonEquipment(), rb_yes, rb_no);
                    } else if (i == 6) {
                        setCheck(facilityDto.getOutdoorGames().getPOGBasketball(), rb_yes, rb_no);
                    } else if (i == 7) {
                        setCheck(facilityDto.getOutdoorGames().getPOGBasketballEquipment(), rb_yes, rb_no);
                    }
                }
                break;
                case CASE_TOURSIM: {
                    if (i == 0) {
                        setCheck(facilityDto.getTourism().getPTNearestBeach(), rb_yes, rb_no);
                    } else if (i == 1) {
                        setCheck(facilityDto.getTourism().getPTNearestRiver(), rb_yes, rb_no);
                    } else if (i == 2) {
                        setCheck(facilityDto.getTourism().getPTNearestFamousTemple(), rb_yes, rb_no);
                    } else if (i == 3) {
                        setCheck(facilityDto.getTourism().getPTNearestHistoricalPlace(), rb_yes, rb_no);
                    } else if (i == 4) {
                        setCheck(facilityDto.getTourism().getPTNearestLakeDam(), rb_yes, rb_no);
                    } else if (i == 5) {
                        setCheck(facilityDto.getTourism().getPTNearestBoating(), rb_yes, rb_no);
                    } else if (i == 6) {
                        setCheck(facilityDto.getTourism().getPTNearestAmusmentPark(), rb_yes, rb_no);
                    } else if (i == 7) {
                        setCheck(facilityDto.getTourism().getPTNearestHorseRiding(), rb_yes, rb_no);
                    } else if (i == 8) {
                        setCheck(facilityDto.getTourism().getPTNearestWatersports(), rb_yes, rb_no);
                    } else if (i == 9) {
                        setCheck(facilityDto.getTourism().getPTNearestForest(), rb_yes, rb_no);
                    } else if (i == 10) {
                        setCheck(facilityDto.getTourism().getPTNearestsanctuary(), rb_yes, rb_no);
                    } else if (i == 11) {
                        setCheck(facilityDto.getTourism().getPTNearestTouristPoints(), rb_yes, rb_no);
                    }
                }
                break;
                case CASE_BEDROOM: {
                     if (i == 0) {
                        //setCheck(facilityDto.getBedroom().getPbdBedRoomNo(), rb_yes, rb_no);
                       ((RadioGroup)v.findViewById(R.id.rg_yesno)).setVisibility(View.GONE);
                        ((Spinner)v.findViewById(R.id.sp_number)).setVisibility(View.VISIBLE);
                        setSpinValue(facilityDto.getBedroom().getPbdBedRoomNo(),(Spinner)v.findViewById(R.id.sp_number));
                         rb_list.set(i,(Spinner)v.findViewById(R.id.sp_number));
                    } else if (i == 1) {
                       // setCheck(facilityDto.getBedroom().getPbdBedSize(), rb_yes, rb_no);
                        ((RadioGroup)v.findViewById(R.id.rg_yesno)).setVisibility(View.GONE);
                        ((Spinner)v.findViewById(R.id.sp_size)).setVisibility(View.VISIBLE);
                         setSpinValue(facilityDto.getBedroom().getPbdBedSize(),(Spinner)v.findViewById(R.id.sp_size));
                         rb_list.set(i,(Spinner)v.findViewById(R.id.sp_size));
                    } else if (i == 2) {
                        //setCheck(facilityDto.getBedroom().getPbdBathroom(), rb_yes, rb_no);
                        ((RadioGroup)v.findViewById(R.id.rg_yesno)).setVisibility(View.GONE);
                        ((Spinner)v.findViewById(R.id.sp_number)).setVisibility(View.VISIBLE);
                         setSpinValue(facilityDto.getBedroom().getPbdBathroom(),(Spinner)v.findViewById(R.id.sp_number));
                         rb_list.set(i,(Spinner)v.findViewById(R.id.sp_number));
                    } else if (i == 3) {
                      setCheck(facilityDto.getBedroom().getPbdGeyser(), rb_yes, rb_no);
                    } else if (i == 4) {
                       // setCheck(facilityDto.getBedroom().getPbdWaternToilet(), rb_yes, rb_no);
                        ((RadioGroup)v.findViewById(R.id.rg_yesno)).setVisibility(View.GONE);
                        ((Spinner)v.findViewById(R.id.sp_number)).setVisibility(View.VISIBLE);
                         setSpinValue(facilityDto.getBedroom().getPbdWaternToilet(),(Spinner)v.findViewById(R.id.sp_number));
                         rb_list.set(i,(Spinner)v.findViewById(R.id.sp_number));
                    } else if (i == 5) {
                      //setCheck(facilityDto.getBedroom().getPbdIndianToilet(), rb_yes, rb_no);
                        ((RadioGroup)v.findViewById(R.id.rg_yesno)).setVisibility(View.GONE);
                        ((Spinner)v.findViewById(R.id.sp_number)).setVisibility(View.VISIBLE);
                         setSpinValue(facilityDto.getBedroom().getPbdIndianToilet(),(Spinner)v.findViewById(R.id.sp_number));
                         rb_list.set(i,(Spinner)v.findViewById(R.id.sp_number));
                    } else if (i == 6) {
                        setCheck(facilityDto.getBedroom().getPbdAC(), rb_yes, rb_no);
                    } else if (i == 7) {
                        setCheck(facilityDto.getBedroom().getPbdBalcony(), rb_yes, rb_no);
                    } else if (i == 8) {
                        setCheck(facilityDto.getBedroom().getPbdTV(), rb_yes, rb_no);
                    } else if (i == 9) {
                        setCheck(facilityDto.getBedroom().getPbdJacuzzi(), rb_yes, rb_no);
                    } else if (i == 10) {
                        setCheck(facilityDto.getBedroom().getPbdFAN(), rb_yes, rb_no);
                    } else if (i == 11) {
                        setCheck(facilityDto.getBedroom().getPbdCurtains(), rb_yes, rb_no);
                    }
                }
                break;
                case CASE_KITCHEN: {
                    if (i == 0) {
                        setCheck(facilityDto.getKitchen().getPkdMicrowave(), rb_yes, rb_no);
                    } else if (i == 1) {
                        setCheck(facilityDto.getKitchen().getPkdUtensils(), rb_yes, rb_no);
                    } else if (i == 2) {
                        setCheck(facilityDto.getKitchen().getPkdWaterFilter(), rb_yes, rb_no);
                    } else if (i == 3) {
                        setCheck(facilityDto.getKitchen().getPkdCookingGas(), rb_yes, rb_no);
                    } else if (i == 4) {
                        setCheck(facilityDto.getKitchen().getPkdCrockery(), rb_yes, rb_no);
                    } else if (i == 5) {
                        setCheck(facilityDto.getKitchen().getPkdDrinkingGlasses(), rb_yes, rb_no);
                    } else if (i == 6) {
                        setCheck(facilityDto.getKitchen().getPkdRefrigarator(), rb_yes, rb_no);
                    }
                }
                break;
                case CASE_FOOD: {
                    if (i == 0) {
                        setCheck(facilityDto.getFood().getFDFNonVeg(), rb_yes, rb_no);
                    } else if (i == 1) {
                        setCheck(facilityDto.getFood().getFDFJainFood(), rb_yes, rb_no);
                    } else if (i == 2) {
                        setCheck(facilityDto.getFood().getFDFVeg(), rb_yes, rb_no);
                    } else if (i == 3) {
                        setCheck(facilityDto.getFood().getFDFInhouseCooking(), rb_yes, rb_no);
                    } else if (i == 4) {
                        setCheck(facilityDto.getFood().getFDFCateringService(), rb_yes, rb_no);
                    } else if (i == 5) {
                        setCheck(facilityDto.getFood().getFDFSelfCooking(), rb_yes, rb_no);
                    }
                }
                break;

                case CASE_HALL: {
                    if (i == 0) {
                       // setCheck(facilityDto.getHall().getHallNo(), rb_yes, rb_no);
                        ((RadioGroup)v.findViewById(R.id.rg_yesno)).setVisibility(View.GONE);
                        ((Spinner)v.findViewById(R.id.sp_number)).setVisibility(View.VISIBLE);
                        setSpinValue(facilityDto.getHall().getHallNo(),(Spinner)v.findViewById(R.id.sp_number));
                        rb_list.set(i,(Spinner)v.findViewById(R.id.sp_number));
                    } else if (i == 1) {
                        //setCheck(facilityDto.getHall().getPhdHallSize(), rb_yes, rb_no);
                        ((RadioGroup)v.findViewById(R.id.rg_yesno)).setVisibility(View.GONE);
                        ((Spinner)v.findViewById(R.id.sp_size)).setVisibility(View.VISIBLE);
                        setSpinValue(facilityDto.getHall().getPhdHallSize(),(Spinner)v.findViewById(R.id.sp_size));
                        rb_list.set(i,(Spinner)v.findViewById(R.id.sp_size));
                    } else if (i == 2) {
                      //  setCheck(facilityDto.getHall().getPhdBathroom(), rb_yes, rb_no);
                        ((RadioGroup)v.findViewById(R.id.rg_yesno)).setVisibility(View.GONE);
                        ((Spinner)v.findViewById(R.id.sp_number)).setVisibility(View.VISIBLE);
                        setSpinValue(facilityDto.getHall().getPhdBathroom(),(Spinner)v.findViewById(R.id.sp_number));
                        rb_list.set(i,(Spinner)v.findViewById(R.id.sp_number));
                    } else if (i == 3) {
                        setCheck(facilityDto.getHall().getPhdGeyser(), rb_yes, rb_no);
                    } else if (i == 4) {
                       // setCheck(facilityDto.getHall().getPhdWesternToilet(), rb_yes, rb_no);
                        ((RadioGroup)v.findViewById(R.id.rg_yesno)).setVisibility(View.GONE);
                        ((Spinner)v.findViewById(R.id.sp_number)).setVisibility(View.VISIBLE);
                        setSpinValue(facilityDto.getHall().getPhdWesternToilet(),(Spinner)v.findViewById(R.id.sp_number));
                        rb_list.set(i,(Spinner)v.findViewById(R.id.sp_number));
                    } else if (i == 5) {
                       // setCheck(facilityDto.getHall().getPhdIndianToilet(), rb_yes, rb_no);
                        ((RadioGroup)v.findViewById(R.id.rg_yesno)).setVisibility(View.GONE);
                        ((Spinner)v.findViewById(R.id.sp_number)).setVisibility(View.VISIBLE);
                        setSpinValue(facilityDto.getHall().getPhdIndianToilet(),(Spinner)v.findViewById(R.id.sp_number));
                        rb_list.set(i,(Spinner)v.findViewById(R.id.sp_number));
                    } else if (i == 6) {
                        setCheck(facilityDto.getHall().getPhdAC(), rb_yes, rb_no);
                    } else if (i == 7) {
                        setCheck(facilityDto.getHall().getPhdBalcony(), rb_yes, rb_no);
                    } else if (i == 8) {
                        setCheck(facilityDto.getHall().getPhdTV(), rb_yes, rb_no);
                    } else if (i == 9) {
                        setCheck(facilityDto.getHall().getPhdBasicMusicSystem(), rb_yes, rb_no);
                    } else if (i == 10) {
                        setCheck(facilityDto.getHall().getPhdDiningTable(), rb_yes, rb_no);
                    } else if (i == 11) {
                        setCheck(facilityDto.getHall().getPhdFAN(), rb_yes, rb_no);
                    } else if (i == 12) {
                        setCheck(facilityDto.getHall().getPhdTerraceVerandah(), rb_yes, rb_no);
                    }
                }
                break;
                default:
            }
            ll_center.addView(v);
            TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_title.setText(items[i]);
        }


        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void setCheck(String yesno, View cb_yes, View cb_no) {
        if (yesno.equalsIgnoreCase("YES")) {
            ((RadioButton)cb_yes).setChecked(true);
        } else {
            ((RadioButton)cb_no).setChecked(true);
        }
    }

    private void setSpinValue(String value, View sp){
        int id =((Spinner)sp).getId();
        String values[]=null;
        if(id==R.id.sp_size){
            values = getResources().getStringArray(R.array.array_size);
        }else{
            values = getResources().getStringArray(R.array.array_number);
        }
       int pos = Arrays.asList(values).indexOf(value.trim());
        ((Spinner)sp).setSelection(pos);
    }

    @Override
    public void locationSelect(String location) {
        tv_location.setText(location);
    }
}

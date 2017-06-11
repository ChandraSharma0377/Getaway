
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
import android.widget.TextView;

import com.gatewayclub.app.R;
import com.gatewayclub.app.adapters.GridViewAdapter;
import com.gatewayclub.app.adapters.MyHorizontalScrollAdapter;
import com.gatewayclub.app.asynctask.AsyncProcess;
import com.gatewayclub.app.helper.Commons;
import com.gatewayclub.app.helper.ExpandableHeightGridView;
import com.gatewayclub.app.helper.MyHorizontalScrollview;
import com.gatewayclub.app.helper.ShowAlertInformation;
import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.pojos.FacilityDto;
import com.gatewayclub.app.pojos.GridItemDto;
import com.gatewayclub.app.pojos.IndoorGames;
import com.gatewayclub.app.pojos.Medical;
import com.gatewayclub.app.pojos.OutdoorGames;
import com.gatewayclub.app.pojos.PropertyDto;
import com.gatewayclub.app.pojos.PropertyImageDto;
import com.gatewayclub.app.pojos.Tourism;
import com.gatewayclub.app.pojos.Transport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EditFragment extends BaseFragment implements View.OnClickListener {

	private Button btn_editphoto,btn_submit;
	private ImageButton btnPrev, btnNext;
	private int currIndex = 0;
	private ArrayList<PropertyImageDto> propertyimagelist = new ArrayList<PropertyImageDto>();
	private MyHorizontalScrollview myHorizontalScrollview;
	private ExpandableHeightGridView mGridView ;
	private GridViewAdapter mGridAdapter;
	private ArrayList<GridItemDto> mGridData;
	private EditText edt_weekdays,edt_extra_person,edt_weekend_holiday;
	private String 	prRatePerNight ,prExtraPersonRate ,WeekendRate ,ExtraWeekendRate,ExtraHolidayRate ,FoodRatePerPerson;
	private PropertyDto pdto;
	private int GET_IMAGE_LIST=0;
	private int SUBMIT_RATE=1;
	private int UPDATE_DATA=2;
    private FacilityDto facilityDto;

	//String [] gridtitle ={"Bedroom","Hall","Kitchen","Comman Area","Facilities","Games","Medical","Transport","Entertainment","Tourism"};
	String [] gridtitle ={"Transport","Medical","IndoorGames","Outdoodr Games","Tourism"};
	String [] hall = new String[]{	"Size",	"Bathroom",	"Gyser","Western toilet","Indian toilet","AC","Balcony","TV","Music System","Dinning table","Fan",	"Terrace Varandah"};
	String [] bedroom = new String[]{	"room no","Size","Bathroom","Gyser","Western toilet","Indian toilet","AC","Balcony","TV","Jacuzzi",	"Curtains",	"Fan"};
	String [] kitchen = new String []{	"Microwave","Utensils",	"Water filter",	"crockery",	"Drinking glasses",	"Refrigerator"};
	String [] food = new String[]{	"Non veg","Jain food","Veg food","Inhouse cooking","catering service","self cooking"};
	String [] medical = new String[]{"Doctor","Medical shop","Hospital","Large Hospital"};
	String [] indoorGames = new String[]{"Carrom","Table Tennis","Chess","Ludo",	"Playing cards","Monopoly",	"Video Games"};
	String [] outdoorGames = new String[]{"Cricket","Cricket Equipments","football","football equipments","badminton","badminton equipments"};
	String [] transport = new String[]{	"Taxi","Autostand","Busstand","Railway station"};
	String [] tourism = new String[]{	"Beach","Nearest River","Famous temple","Historical Place","Lake / River","Boating","Amusement park","Horse Ridding",
			"Water sport","Forest","Sanctuary","Tourist point"};

   // String data[][]=new String[][]{bedroom,hall,kitchen,indoorGames,medical,transport,tourism,outdoorGames,food,food};

	String data[][]=new String[][]{transport,medical,indoorGames,outdoorGames,tourism};





	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
		super.onCreateView(inflater, container, args);
		View view = inflater.inflate(R.layout.frag_edit, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		btn_editphoto = (Button) view.findViewById(R.id.btn_editphoto);
		btn_submit=(Button)view.findViewById(R.id.btn_submit);
		myHorizontalScrollview = (MyHorizontalScrollview) view.findViewById(R.id.horizontalScrollView);
		mGridView =(ExpandableHeightGridView)view.findViewById(R.id.mGridView);
		btnNext = (ImageButton) view.findViewById(R.id.btnNext);
		btnPrev = (ImageButton) view.findViewById(R.id.btnPrev);
		edt_weekdays =(EditText)view.findViewById(R.id.edt_weekdays);
		edt_extra_person =(EditText)view.findViewById(R.id.edt_extra_person);
		edt_weekend_holiday =(EditText)view.findViewById(R.id.edt_weekend_holiday);
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
				GridItemDto item = (GridItemDto) parent.getItemAtPosition(position);
				facilityDialog(getActivity(),gridtitle[position],item.getFacility(),position);
			}
		});
		addView(view);
		return rootview;
	}

	private void initGrid() {

		mGridData = new ArrayList<>();
		for (int i=0;i<gridtitle.length;i++){
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

				if((edt_extra_person.getText().toString().trim().equals(prExtraPersonRate)&&edt_weekdays.getText().toString().trim().equals(prRatePerNight)
						&&edt_weekend_holiday.getText().toString().trim().equals(WeekendRate))){
					//do nothing
				}else {
					if ((edt_extra_person.getText().toString().trim().equals("") || edt_weekdays.getText().toString().trim().equals("") || edt_weekend_holiday.getText().toString().trim().equals(""))) {
						ShowAlertInformation.showDialog(getActivity(), "Mandatory fields", "Blank price not allowed");
					}else{
						if (MainActivity.getNetworkHelper().isOnline()) {
							HashMap<String, String> postDataParams = new HashMap<String, String>();
							postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
							postDataParams.put("pmId",pdto.getPropertyID());
							postDataParams.put("pmWeekend",edt_weekend_holiday.getText().toString());
							postDataParams.put("pmWeekday",edt_weekdays.getText().toString());
							postDataParams.put("pmExtraperson",edt_extra_person.getText().toString());
							new GetImageTask(postDataParams,SUBMIT_RATE).execute(Commons.UPDATE_RATES);

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
		this.pdto=pdto;
		if (MainActivity.getNetworkHelper().isOnline()) {
			HashMap<String, String> postDataParams = new HashMap<String, String>();
			postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
			postDataParams.put("pmID",pdto.getPropertyID());
			new GetImageTask(postDataParams,GET_IMAGE_LIST).execute(Commons.GET_BUNGLOWIMAGE_LIST);

		} else {
			ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
		}
		
	}
	private class GetImageTask extends AsyncProcess {

		ProgressDialog progressDialog;
		int senario=-1;
		public GetImageTask(HashMap<String, String> postDataParams,int senario) {
			super(postDataParams);
			this.senario=senario;
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

				if(senario==GET_IMAGE_LIST) {
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
						facilityDto=new FacilityDto();

						//Transport
						Transport tp =new Transport();
						JSONObject jtp =jsonObject.getJSONObject("Transport");
						tp.setPMTransAutoStand(jtp.getString("PMTransAutoStand"));
						tp.setPMTransBusStand(jtp.getString("PMTransBusStand"));
						tp.setPMTransRailwayStation(jtp.getString("PMTransRailwayStation"));
						tp.setPMTransTaxi(jtp.getString("PMTransTaxi"));
						facilityDto.setTransport(tp);

						//Medical
						Medical md =new Medical();
						JSONObject jtm =jsonObject.getJSONObject("Medical");
						md.setPMedDoctor(jtm.getString("PMedDoctor"));
						md.setPMedHospital(jtm.getString("PMedMedicalShop"));
						md.setPMedLargeHospital(jtm.getString("PMedLargeHospital"));
						md.setPMedMedicalShop(jtm.getString("PMedHospital"));
						facilityDto.setMedical(md);

						//IndoorGames
						IndoorGames ig= new IndoorGames();
						JSONObject jtig =jsonObject.getJSONObject("IndoorGames");
						ig.setPGCarrom(jtig.getString("PGCarrom"));
						ig.setPGChess(jtig.getString("PGChess"));
						ig.setPGLudo(jtig.getString("PGLudo"));
						ig.setPGMonopoly(jtig.getString("PGMonopoly"));
						ig.setPGPlayingCards(jtig.getString("PGPlayingCards"));
						ig.setPGTableTennis(jtig.getString("PGTableTennis"));
						ig.setPGVideoGames(jtig.getString("PGVideoGames"));
						facilityDto.setIndoorGames(ig);

						//outdoors
						OutdoorGames og =new OutdoorGames();
						JSONObject jtog =jsonObject.getJSONObject("OutdoorGames");
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
						JSONObject jttm =jsonObject.getJSONObject("Tourism");
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
				}else if (senario==SUBMIT_RATE){
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
				}else if (senario==UPDATE_DATA){
					try {
						JSONObject jo = new JSONObject(value);
						String status = jo.getString("status");
						if (status.equals("Success")) {
							ShowAlertInformation.showDialog(getActivity(), "Success", "Info updated successfully!!!");
						} else {
							ShowAlertInformation.showDialog(getActivity(), "Error", "Error while updating the data !!!");
						}
						progressDialog.dismiss();
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
		TextView tv_header = (TextView)dialog.findViewById(R.id.tv_header);
		tv_header.setText(title);
		final ArrayList<RadioButton> rb_list=new ArrayList<>();
		Button btn_submit = (Button)dialog.findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String, String> postDataParams = new HashMap<String, String>();
				postDataParams.put("ownerId", MainActivity.getMainScreenActivity().getUserID());
				postDataParams.put("pmID",pdto.getPropertyID());
				postDataParams.put("statusUpdate", "0");
				String url="";
				if(position==1){
					url = "updateMedicalFacility";
					postDataParams.put("PMedDoctor",rb_list.get(0).isChecked()?"YES":"NO");
					postDataParams.put("PMedMedicalShop", rb_list.get(1).isChecked()?"YES":"NO");
					postDataParams.put("PMedHospital",rb_list.get(2).isChecked()?"YES":"NO");
					postDataParams.put("PMedLargeHospital", rb_list.get(3).isChecked()?"YES":"NO");
				}else if(position==0){
					url = "updateTransportFacility";
					postDataParams.put("PMTransTaxi",rb_list.get(0).isChecked()?"YES":"NO");
					postDataParams.put("PMTransAutoStand", rb_list.get(1).isChecked()?"YES":"NO");
					postDataParams.put("PMTransBusStand",rb_list.get(2).isChecked()?"YES":"NO");
					postDataParams.put("PMTransRailwayStation", rb_list.get(3).isChecked()?"YES":"NO");
				}else if(position==2){
					url="updateIndoorGamesFacility";
					postDataParams.put("PGCarrom",rb_list.get(0).isChecked()?"YES":"NO");
					postDataParams.put("PGTableTennis", rb_list.get(1).isChecked()?"YES":"NO");
					postDataParams.put("PGChess",rb_list.get(2).isChecked()?"YES":"NO");
					postDataParams.put("PGLudo", rb_list.get(3).isChecked()?"YES":"NO");
					postDataParams.put("PGPlayingCards", rb_list.get(4).isChecked()?"YES":"NO");
					postDataParams.put("PGMonopoly",rb_list.get(5).isChecked()?"YES":"NO");
					postDataParams.put("PGVideoGames", rb_list.get(6).isChecked()?"YES":"NO");
				}else if(position==3){
					url="updateOutdoorGamesFacility";
					postDataParams.put("POGCricket",rb_list.get(0).isChecked()?"YES":"NO");
					postDataParams.put("POGCricketEquipment", rb_list.get(1).isChecked()?"YES":"NO");
					postDataParams.put("POGFootball",rb_list.get(2).isChecked()?"YES":"NO");
					postDataParams.put("POGFootballEquipment", rb_list.get(3).isChecked()?"YES":"NO");
					postDataParams.put("POGBadminton", rb_list.get(4).isChecked()?"YES":"NO");
					postDataParams.put("POGBadmintonEquipment",rb_list.get(5).isChecked()?"YES":"NO");
				//	postDataParams.put("POGBasketball", rb_list.get(6).isChecked()?"YES":"NO");
				//	postDataParams.put("POGBasketballEquipment", rb_list.get(7).isChecked()?"YES":"NO");
				}else if(position==4){
					url="updateTourismFacility";
					postDataParams.put("PTNearestBeach",rb_list.get(0).isChecked()?"YES":"NO");
					postDataParams.put("PTNearestRiver", rb_list.get(1).isChecked()?"YES":"NO");
					postDataParams.put("PTNearestFamousTemple",rb_list.get(2).isChecked()?"YES":"NO");
					postDataParams.put("PTNearestHistoricalPlace", rb_list.get(3).isChecked()?"YES":"NO");
					postDataParams.put("PTNearestLakeDam", rb_list.get(4).isChecked()?"YES":"NO");
					postDataParams.put("PTNearestBoating",rb_list.get(5).isChecked()?"YES":"NO");
					postDataParams.put("PTNearestAmusmentPark", rb_list.get(6).isChecked()?"YES":"NO");
					postDataParams.put("PTNearestHorseRiding", rb_list.get(7).isChecked()?"YES":"NO");
					postDataParams.put("PTNearestWatersports", rb_list.get(8).isChecked()?"YES":"NO");
					postDataParams.put("PTNearestForest",rb_list.get(9).isChecked()?"YES":"NO");
					postDataParams.put("PTNearestsanctuary", rb_list.get(10).isChecked()?"YES":"NO");
					postDataParams.put("PTNearestTouristPoints", rb_list.get(11).isChecked()?"YES":"NO");
				}
				if (MainActivity.getNetworkHelper().isOnline()) {
					new GetImageTask(postDataParams,UPDATE_DATA).execute(Commons.BASE_URL+url);
					dialog.dismiss();
				} else {
					ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
				}

			}
		});

		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		for (int i = 0; i< items.length; i++) {
			View v = inflater.inflate(R.layout.cb_lay, null);

            RadioButton rb_yes = (RadioButton)v.findViewById(R.id.rb_yes);
            RadioButton rb_no = (RadioButton)v.findViewById(R.id.rb_no);
			rb_list.add(rb_yes);
			if(position==0) {
				if (i == 0) {
					setCheck(facilityDto.getTransport().getPMTransTaxi(), rb_yes, rb_no);
				} else if (i == 1) {
					setCheck(facilityDto.getTransport().getPMTransAutoStand(), rb_yes, rb_no);
				} else if (i == 2) {
					setCheck(facilityDto.getTransport().getPMTransBusStand(), rb_yes, rb_no);
				} else if (i == 3) {
					setCheck(facilityDto.getTransport().getPMTransRailwayStation(), rb_yes, rb_no);
				}
			}else if(position==1){
				if(i==0){
					setCheck(facilityDto.getMedical().getPMedDoctor(),rb_yes,rb_no);
				}else if(i==1){
					setCheck(facilityDto.getMedical().getPMedHospital(),rb_yes,rb_no);
				}else if(i==2){
					setCheck(facilityDto.getMedical().getPMedLargeHospital(),rb_yes,rb_no);
				}else if(i==3){
					setCheck(facilityDto.getMedical().getPMedMedicalShop(),rb_yes,rb_no);
				}
			}else if(position==2){
				if(i==0){
					setCheck(facilityDto.getIndoorGames().getPGCarrom(),rb_yes,rb_no);
				}else if(i==1){
					setCheck(facilityDto.getIndoorGames().getPGTableTennis(),rb_yes,rb_no);
				}else if(i==2){
					setCheck(facilityDto.getIndoorGames().getPGChess(),rb_yes,rb_no);
				}else if(i==3){
					setCheck(facilityDto.getIndoorGames().getPGLudo(),rb_yes,rb_no);
				}else if(i==4){
					setCheck(facilityDto.getIndoorGames().getPGPlayingCards(),rb_yes,rb_no);
				}else if(i==5){
					setCheck(facilityDto.getIndoorGames().getPGMonopoly(),rb_yes,rb_no);
				}else if(i==6){
					setCheck(facilityDto.getIndoorGames().getPGVideoGames(),rb_yes,rb_no);
				}
			}else if(position==3){
				if(i==0){
					setCheck(facilityDto.getOutdoorGames().getPOGCricket(),rb_yes,rb_no);
				}else if(i==1){
					setCheck(facilityDto.getOutdoorGames().getPOGCricketEquipment(),rb_yes,rb_no);
				}else if(i==2){
					setCheck(facilityDto.getOutdoorGames().getPOGFootball(),rb_yes,rb_no);
				}else if(i==3){
					setCheck(facilityDto.getOutdoorGames().getPOGFootballEquipment(),rb_yes,rb_no);
				}else if(i==4){
					setCheck(facilityDto.getOutdoorGames().getPOGBadminton(),rb_yes,rb_no);
				}else if(i==5){
					setCheck(facilityDto.getOutdoorGames().getPOGBadmintonEquipment(),rb_yes,rb_no);
				}else if(i==6){
					setCheck(facilityDto.getOutdoorGames().getPOGBasketball(),rb_yes,rb_no);
				}else if(i==7){
					setCheck(facilityDto.getOutdoorGames().getPOGBasketballEquipment(),rb_yes,rb_no);
				}
			}else if(position==4){
				if(i==0){
					setCheck(facilityDto.getTourism().getPTNearestBeach(),rb_yes,rb_no);
				}else if(i==1){
					setCheck(facilityDto.getTourism().getPTNearestRiver(),rb_yes,rb_no);
				}else if(i==2){
					setCheck(facilityDto.getTourism().getPTNearestFamousTemple(),rb_yes,rb_no);
				}else if(i==3){
					setCheck(facilityDto.getTourism().getPTNearestHistoricalPlace(),rb_yes,rb_no);
				}else if(i==4){
					setCheck(facilityDto.getTourism().getPTNearestLakeDam(),rb_yes,rb_no);
				}else if(i==5){
					setCheck(facilityDto.getTourism().getPTNearestBoating(),rb_yes,rb_no);
				}else if(i==6){
					setCheck(facilityDto.getTourism().getPTNearestAmusmentPark(),rb_yes,rb_no);
				}else if(i==7){
					setCheck(facilityDto.getTourism().getPTNearestHorseRiding(),rb_yes,rb_no);
				}else if(i==8){
					setCheck(facilityDto.getTourism().getPTNearestWatersports(),rb_yes,rb_no);
				}else if(i==9){
					setCheck(facilityDto.getTourism().getPTNearestForest(),rb_yes,rb_no);
				}else if(i==10){
					setCheck(facilityDto.getTourism().getPTNearestsanctuary(),rb_yes,rb_no);
				}else if(i==11){
					setCheck(facilityDto.getTourism().getPTNearestTouristPoints(),rb_yes,rb_no);
				}
			}
			ll_center.addView(v);
			TextView tv_title = (TextView)v.findViewById(R.id.tv_title);
			tv_title.setText(items[i]);
		}



		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		dialog.show();
	}

	private void setCheck(String yesno, RadioButton cb_yes,RadioButton cb_no){
		if(yesno.equalsIgnoreCase("YES")){
			cb_yes.setChecked(true);
		}else{
			cb_no.setChecked(true);
		}

	}
}

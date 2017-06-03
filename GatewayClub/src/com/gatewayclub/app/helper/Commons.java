package com.gatewayclub.app.helper;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.gatewayclub.app.main.MainActivity;
import com.gatewayclub.app.pojos.LocationDto;

public class Commons {

	private static final String BASE_URL = "http://www.thegetawayclub.in/Test_web_application/WebService1.asmx/";
	public static final String LOGIN_URL = BASE_URL + "GetUserInfo";
	public static final String GET_CALANDER_DETAILS = BASE_URL + "getbookinginfo";
	public static final String GET_CUSTOMER_REVIEW = BASE_URL + "getFeedback";
	public static final String GET_PROPERTY_LIST = BASE_URL + "getProperty";
	public static final String GET_LOCATION_LIST = BASE_URL + "getlocation";
	public static final String ADD_AGENT = BASE_URL + "addAgent";
	public static final String GET_BUNGLOWIMAGE_LIST = BASE_URL + "getBunglowImages";
	public static final String GET_AGENT_LIST = BASE_URL + "getAgentList";
	public static final String IMAGE_BASE_URL = "http://thegetawayclub.in/BunglowImages/";
	public static final String ADD_BOOKING = BASE_URL + "addBooking";
	public static final String GET_PROPERTY_RATE_LIST = BASE_URL + "getPropertyRates";
	public static final String GET_CUSTOMER_DETAILS = BASE_URL + "getCustomerDetails";
	public static final String UPDATE_PROPERTY = BASE_URL + "updateStatus";
	public static final String BOOKING_SUMMARY = BASE_URL + "getStatusCount";

	public static ArrayList<LocationDto> locationLists = new ArrayList<>();

	public static boolean isValidEmail(CharSequence target) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}

	public static boolean isValidDate(String firstDate, String secondDate) {

		// public static boolean CheckDates("2012-07-12", "2012-06-12)" {
		SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
		boolean b = false;
		try {
			if (dfDate.parse(firstDate).before(dfDate.parse(secondDate))) {
				b = true;// If start date is before end date
			} else if (dfDate.parse(firstDate).equals(dfDate.parse(secondDate))) {
				b = true;// If two dates are equal
			} else {
				b = false; // If start date is after the end date
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}

	public static boolean isValidDateFromToday(String secondDate) {
		SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String firstDate = dfDate.format(date);
		// public static boolean CheckDates("2012-07-12", "2012-06-12)" {

		boolean b = false;
		try {
			if (dfDate.parse(firstDate).before(dfDate.parse(secondDate))) {
				b = true;// If start date is before end date
			} else if (dfDate.parse(firstDate).equals(dfDate.parse(secondDate))) {
				b = true;// If two dates are equal
			} else {
				b = false; // If start date is after the end date
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}

	public static String formatDateFromOnetoAnother(String date, String givenformat, String resultformat) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(givenformat);
			SimpleDateFormat sdf1 = new SimpleDateFormat(resultformat);
			return sdf1.format(sdf.parse(date));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public static String getCurrentDate(String format) {
		Date date = new Date();
		String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return modifiedDate;
	}

	public static String sendMessage(String mobileNo, String bookingdates,String status) {
		String msg = "http://ocs-sms.com/submitsms.jsp?user=getawy&key=8a454412f5XX&mobile=" + mobileNo + "&message="
				+ "Dear Customer , Your Bungalow Booking for Dates " + bookingdates
			//	+ " approved. Please login to our web site http://www.Getaway Club.in Or Check your email for link. Your payment towards booking is appropriated. Window for making payment is 24 Hours . "
				+ status
				+ "&senderid=GETAWY&accusage=1&MT=4";
		return msg;
	}
	public static String sendAgentMessage(String mobileNo, String agentname) {
		String msg = "http://ocs-sms.com/submitsms.jsp?user=getawy&key=8a454412f5XX&mobile=" + mobileNo + "&message="+
					  "Dear Agent "+agentname+", "+
                      "Mr. Bungalow Owner "+MainActivity.getMainScreenActivity().getUserName()+" has register you for renting our BUNGALOW PROPERTIES."+
                      "Kindly download app from play store with name Bungalow Owner App ."+ "&senderid=GETAWY&accusage=1&MT=4";
		return msg;
	}
	public static String getMonthName(int month) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat month_date = new SimpleDateFormat("MMM");
		cal.set(Calendar.MONTH, month - 1);
		String month_name = month_date.format(cal.getTime());
		return month_name;
	}
}

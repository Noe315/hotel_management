package LGoodDatePicker;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.optionalusertools.TimeChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import com.github.lgooddatepicker.zinternaltools.TimeChangeEvent;

import db.DBQueryBooking;

import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class HandleDateTimeChange implements DateChangeListener, TimeChangeListener
{
	private DatePicker dateCheckIn, dateCheckOut;
	private TimePicker timeCheckIn, timeCheckOut;
	
	private ArrayList<String> bookingRoomIDAL = new ArrayList<String>();
	private JComboBox bookingRoomIDComboBox;
	private String dateCheckInStr;
	private String dateCheckOutStr;
	private String timeCheckInStr;
	private String timeCheckOutStr;
	private String datetimeCheckInStr = dateCheckInStr + " " + timeCheckInStr;
	private String datetimeCheckOutStr = dateCheckOutStr + " " + timeCheckOutStr;
	
	private DBQueryBooking dbB;
	
	public void SetConnection(Connection connectionToSet)
	{
		dbB = new DBQueryBooking(connectionToSet);
	}
	
	public HandleDateTimeChange(DatePicker cstrDateCheckIn, TimePicker cstrTimeCheckIn, DatePicker cstrDateCheckOut,
								TimePicker cstrTimeCheckOut, JComboBox cstrBookingRoomIDComboBox)
	{
		this.dateCheckIn = cstrDateCheckIn;
		this.timeCheckIn = cstrTimeCheckIn;
		this.dateCheckOut = cstrDateCheckOut;
		this.timeCheckOut = cstrTimeCheckOut;
		this.bookingRoomIDComboBox = cstrBookingRoomIDComboBox;
		
		String dateFormatCommonEra = "YYYY-MM-dd";
		String dateFormatBeforeCommonEra = "uuuu-MM-dd";
		
		DatePickerSettings dpSettingsCheckIn = new DatePickerSettings();
		dpSettingsCheckIn.setFormatForDatesCommonEra(dateFormatCommonEra);
		dpSettingsCheckIn.setFormatForDatesBeforeCommonEra(dateFormatBeforeCommonEra);
		dpSettingsCheckIn.setAllowKeyboardEditing(false);
		
		DatePickerSettings dpSettingsCheckOut = new DatePickerSettings();
		dpSettingsCheckOut.setFormatForDatesCommonEra(dateFormatCommonEra);
		dpSettingsCheckOut.setFormatForDatesBeforeCommonEra(dateFormatBeforeCommonEra);
		dpSettingsCheckOut.setAllowKeyboardEditing(false);
		
		VetoPolicyCheckIn vpi = new VetoPolicyCheckIn(dateCheckIn.getText(), dateCheckOut.getText());
		dateCheckIn.setSettings(dpSettingsCheckIn);
		dpSettingsCheckIn.setVetoPolicy(vpi);

		VetoPolicyCheckOut vpo = new VetoPolicyCheckOut(dateCheckIn.getText(), dateCheckOut.getText());
		dateCheckOut.setSettings(dpSettingsCheckOut);
		dpSettingsCheckOut.setVetoPolicy(vpo);
	}
	
	@Override
	public void dateChanged(DateChangeEvent event)
	{
		System.out.println("dateIn: " + dateCheckIn.getText() + " dateOut: " + dateCheckOut.getText());
		this.datetimeCheckInStr = dateCheckIn.getText() + " " + timeCheckIn.getText();
		this.datetimeCheckOutStr = dateCheckOut.getText() + " " + timeCheckOut.getText();
		
		bookingRoomIDAL.clear();

		bookingRoomIDAL = dbB.GetRoomIDAvailableAL(datetimeCheckInStr, datetimeCheckOutStr);
		bookingRoomIDComboBox.setModel(new DefaultComboBoxModel(bookingRoomIDAL.toArray()));
	}
	
	@Override
	public void timeChanged(TimeChangeEvent event)
	{
		this.datetimeCheckInStr = dateCheckIn.getText() + " " + timeCheckIn.getText();
		this.datetimeCheckOutStr = dateCheckOut.getText() + " " + timeCheckOut.getText();
		
		bookingRoomIDAL.clear();
		
		bookingRoomIDAL = dbB.GetRoomIDAvailableAL(datetimeCheckInStr, datetimeCheckOutStr);
		bookingRoomIDComboBox.setModel(new DefaultComboBoxModel(bookingRoomIDAL.toArray()));	
	}
}
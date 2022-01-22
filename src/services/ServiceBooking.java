package services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

import dataClasses.Booking;
import db.DBQueryBooking;

public class ServiceBooking
{
	private DBQueryBooking dbB;
	private ArrayList<Booking> AL;
	private ArrayList<String> customerIDAL;
	private ArrayList<String> bookingIDAL;
	
	public void SetConnection(Connection connectionToSet)
	{
		dbB = new DBQueryBooking(connectionToSet);
	}
	
	public ArrayList<Booking> GetAL()
	{
		AL = dbB.GetInfoFromDB();
		return AL;
	}
	
	public ArrayList<String> GetCustomerIDAL()
	{
		customerIDAL = dbB.GetCustomerIDAL();
		return customerIDAL;
	}
	
	public String GetNewBookingID()
	{
		ArrayList<String> bookingIDList = GetBookingIDAL();
		ArrayList<Integer> bookingIDListNumbersOnly = new ArrayList<Integer>();
		
		for (int i = 0; i < bookingIDList.size(); i++)
		{
			String currentBookingID = bookingIDList.get(i);
			String subStrOfBookingID = currentBookingID.substring(1, currentBookingID.length());
			
			int numberPartOfCurrentBookingID = Integer.parseInt(subStrOfBookingID);
			bookingIDListNumbersOnly.add(numberPartOfCurrentBookingID);
		}
		
		int newBookingIDNumber = Collections.max(bookingIDListNumbersOnly) + 1;
		String newBookingID = "B" + newBookingIDNumber;
		
		return newBookingID;
	}
	
	public ArrayList<String> GetBookingIDAL()
	{
		bookingIDAL = dbB.GetBookingIDAL();
		return bookingIDAL;
	}
	
	public Booking GetBookingInfoToDelete(Booking bookingToDelete)
	{
		Booking info = dbB.GetBookingInfoToDelete(bookingToDelete);
		return info;
	}
	
	public void AddRecord(Booking newBooking)
	{
		dbB.AddObjectToDB(newBooking);
	}
	
	public void DeleteRecord(Booking bookingToDelete)
	{
		dbB.DeleteInfoFromDB(bookingToDelete);
	}
}
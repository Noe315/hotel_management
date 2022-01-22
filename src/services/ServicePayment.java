package services;

import java.sql.Connection;
import java.util.ArrayList;

import dataClasses.Payment;
import db.DBQueryPayment;

public class ServicePayment
{
	private DBQueryPayment dbP;
	private ArrayList<Payment> AL;
	private ArrayList<String> bookingIDAL;
	private ArrayList<String> allBookingIDAL;
	
	public void SetConnection(Connection connectionToSet)
	{
		dbP = new DBQueryPayment(connectionToSet);
	}
	
	public ArrayList<Payment> GetAL()
	{
		AL = dbP.GetInfoFromDB();
		return AL;
	}
	
	public ArrayList<String> GetNonPaymentBookingIDAL()
	{
		bookingIDAL = dbP.GetNonPaymentBookingIDAL();
		return bookingIDAL;
	}
	
	public Payment GetInfoNonPaymentBooking(Payment paymentID)
	{
		Payment info = dbP.GetInfoNonPaymentBooking(paymentID);
		return info;
	}
	
	public ArrayList<String> GetAllBookingIDAL()
	{
		allBookingIDAL = dbP.GetAllBookingIDAL();
		return allBookingIDAL;
	}
	
	public void AddRecord(Payment newPayment)
	{
		dbP.AddObjectToDB(newPayment);
	}
	
	public Payment GetInfoPaymentToDelete(Payment paymentToDelete)
	{
		Payment info = dbP.GetInfoPaymentToDelete(paymentToDelete);
		return info;
	}
	
	public void DeleteRecord(Payment paymentToDelete)
	{
		dbP.DeleteInfoFromDB(paymentToDelete);
	}
}
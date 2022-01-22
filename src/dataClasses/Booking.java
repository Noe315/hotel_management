package dataClasses;

public class Booking
{
	private String ID, customerID, roomID, bookingDate, checkInDate, checkOutDate, paymentStatus;
	
	public Booking()
	{
		
	}
	
	public Booking(String cstrID, String cstrCustomerID, String cstrRoomID, String cstrBookingDate, String cstrCheckInDate,
				   String cstrCheckOutDate, String cstrPaymentStatus)
	{
		this.ID = cstrID;
		this.customerID = cstrCustomerID;
		this.roomID = cstrRoomID;
		this.bookingDate = cstrBookingDate;
		this.checkInDate = cstrCheckInDate;
		this.checkOutDate = cstrCheckOutDate;
		this.paymentStatus = cstrPaymentStatus;
	}
	
	public String GetBookingID()
	{
		return ID;
	}
	
	public String GetCustomerID()
	{
		return customerID;
	}
	
	public String GetRoomID()
	{
		return roomID;
	}
	
	public String GetBookingDate()
	{
		return bookingDate;
	}
	
	public String GetCheckInDate()
	{
		return checkInDate;
	}
	
	public String GetCheckOutDate()
	{
		return checkOutDate;
	}
	
	public String GetPaymentStatus()
	{
		return paymentStatus;
	}
	
	public void SetBookingID(String bookingIDToSet)
	{
		this.ID = bookingIDToSet;
	}
	
	public void SetCustomerID(String customerIDToSet)
	{
		this.customerID = customerIDToSet;
	}
	
	public void SetRoomID(String roomIDToSet)
	{
		this.roomID = roomIDToSet;
	}
	
	public void SetBookingDate(String bookingDateToSet)
	{
		this.bookingDate = bookingDateToSet;
	}
	
	public void SetCheckInDate(String checkInDateToSet)
	{
		this.checkInDate = checkInDateToSet;
	}
	
	public void SetCheckOutDate(String checkOutDateToSet)
	{
		this.checkOutDate = checkOutDateToSet;
	}
	
	public void SetPaymentStatus(String paymentStatusToSet)
	{
		this.paymentStatus = paymentStatusToSet;
	}
}
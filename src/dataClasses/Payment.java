package dataClasses;

public class Payment
{
	private String paymentDate, bookingID, customerID, roomID, bookingDate, checkInDate, checkOutDate, description;
	private int amount;
	
	public Payment()
	{
		
	}
	
	public Payment(String cstrPaymentDate, String cstrBookingID, String cstrCustomerID, String cstrRoomID, String cstrBookingDate,
				   String cstrCheckInDate, String cstrCheckOutDate, int cstrAmount, String cstrDescription)
	{
		this.paymentDate = cstrPaymentDate;
		this.bookingID = cstrBookingID;
		this.customerID = cstrCustomerID;
		this.roomID = cstrRoomID;
		this.bookingDate = cstrBookingDate;
		this.checkInDate = cstrCheckInDate;
		this.checkOutDate = cstrCheckOutDate;
		this.amount = cstrAmount;
		this.description = cstrDescription;
	}
	
	public String GetPaymentDate()
	{
		return paymentDate;
	}
	
	public String GetBookingID()
	{
		return bookingID;
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
	
	public int GetAmount()
	{
		return amount;
	}
	
	public String GetDescription()
	{
		return description;
	}
	
	public void SetPaymentDate(String paymentDateToSet)
	{
		this.paymentDate = paymentDateToSet;
	}
	
	public void SetBookingID(String bookingIDToSet)
	{
		this.bookingID = bookingIDToSet;
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
	
	public void SetAmount(int amountToSet)
	{
		this.amount = amountToSet;
	}
	
	public void SetDescription(String descriptionToSet)
	{
		this.description = descriptionToSet;
	}
}
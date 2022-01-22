package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataClasses.Payment;

public class DBQueryPayment
{
	private Connection connection;
	private ArrayList<Payment> paymentAL = new ArrayList<Payment>();
	private ArrayList<String> paymentBookingIDAL = new ArrayList<String>();
	private ArrayList<String> paymentAllBookingIDAL = new ArrayList<String>();
	
	public DBQueryPayment(Connection connectionToSet)
	{
		this.connection = connectionToSet;
	}

	public ArrayList<Payment> GetInfoFromDB()
	{
		String queryPayment= "SELECT * FROM hotel.payment ORDER BY LENGTH(payment_date), payment_date;";
		try
		{
			//Queries and ResultSet
			Statement stPayment = connection.createStatement();
			ResultSet rsPayment = stPayment.executeQuery(queryPayment);
			
			while (rsPayment.next())
			{
				Payment p = new Payment();
				p.SetPaymentDate(rsPayment.getString("payment_date"));
				p.SetBookingID(rsPayment.getString("booking_id"));
				p.SetCustomerID(rsPayment.getString("customer_id"));
				p.SetRoomID(rsPayment.getString("room_id"));
				p.SetBookingDate(rsPayment.getString("booking_date"));
				p.SetCheckInDate(rsPayment.getString("check_in"));
				p.SetCheckOutDate(rsPayment.getString("check_out"));
				p.SetAmount(rsPayment.getInt("payment_amount"));
				p.SetDescription(rsPayment.getString("payment_description"));
				
				paymentAL.add(p);
			}
			
			return paymentAL;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<String> GetNonPaymentBookingIDAL()
	{
		try
		{
			paymentBookingIDAL.clear();
			String queryPaymentBookingIDList = "SELECT booking_id FROM booking WHERE (payment_status IS NULL);";
			PreparedStatement psPaymentBookingIDList = connection.prepareStatement(queryPaymentBookingIDList);
			ResultSet rsPaymentBookingIDList = psPaymentBookingIDList.executeQuery();
			while(rsPaymentBookingIDList.next())
			{
				paymentBookingIDAL.add(rsPaymentBookingIDList.getString("booking_id"));
			}
			
			return paymentBookingIDAL;
		}
		catch (SQLException e3)
		{
			e3.printStackTrace();
		}
		
		return null;
	}
	
	public Payment GetInfoNonPaymentBooking(Payment paymentID)
	{
		Payment info = new Payment();
		
		String queryPaymentCustomerID = "SELECT customer_id FROM booking WHERE booking_id = '" + paymentID.GetBookingID() + "'";
		String queryPaymentRoomID = "SELECT room_id FROM booking WHERE booking_id = '" + paymentID.GetBookingID() + "'";
		String queryPaymentBookingDate = "SELECT booking_date FROM booking WHERE booking_id = '" + paymentID.GetBookingID() + "'";
		String queryPaymentCheckIn = "SELECT check_in FROM booking WHERE booking_id = '" + paymentID.GetBookingID() + "'";
		String queryPaymentCheckOut = "SELECT check_out FROM booking WHERE booking_id = '" + paymentID.GetBookingID() + "'";
		
		try
		{
			PreparedStatement psGetPaymentCustomerID = connection.prepareStatement(queryPaymentCustomerID);
			PreparedStatement psGetPaymentRoomID = connection.prepareStatement(queryPaymentRoomID);
			PreparedStatement psGetPaymentBookingDate = connection.prepareStatement(queryPaymentBookingDate);
			PreparedStatement psGetPaymentCheckIn = connection.prepareStatement(queryPaymentCheckIn);
			PreparedStatement psGetPaymentCheckOut = connection.prepareStatement(queryPaymentCheckOut);
			
			ResultSet rsPaymentCustomerID = psGetPaymentCustomerID.executeQuery();
			ResultSet rsPaymentRoomID = psGetPaymentRoomID.executeQuery();
			ResultSet rsPaymentBookingDate= psGetPaymentBookingDate.executeQuery();
			ResultSet rsPaymentCheckIn = psGetPaymentCheckIn.executeQuery();
			ResultSet rsPaymentCheckOut = psGetPaymentCheckOut.executeQuery();
			
			if (rsPaymentCustomerID.next())
			{
				info.SetCustomerID(rsPaymentCustomerID.getString("customer_id"));
			}
			if (rsPaymentRoomID.next())
			{
				info.SetRoomID(rsPaymentRoomID.getString("room_id"));
			}
			if (rsPaymentBookingDate.next())
			{
				info.SetBookingDate(rsPaymentBookingDate.getString("booking_date"));
			}
			if (rsPaymentCheckIn.next())
			{
				info.SetCheckInDate(rsPaymentCheckIn.getString("check_in"));
			}
			if (rsPaymentCheckOut.next())
			{
				info.SetCheckOutDate(rsPaymentCheckOut.getString("check_out"));
			}
			
			return info;
		}
		catch (SQLException sqle2)
		{
			sqle2.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<String> GetAllBookingIDAL()
	{
		try
		{
			String queryPaymentBookingIDList = "SELECT booking_id FROM payment ORDER BY LENGTH(booking_id), booking_id;";
			PreparedStatement psPaymentBookingIDList = connection.prepareStatement(queryPaymentBookingIDList);
			ResultSet rsPaymentBookingIDList = psPaymentBookingIDList.executeQuery();
			while(rsPaymentBookingIDList.next())
			{
				paymentAllBookingIDAL.add(rsPaymentBookingIDList.getString("booking_id"));
			}
			
			return paymentAllBookingIDAL;
		}
		catch (SQLException e3)
		{
			e3.printStackTrace();
		}
		
		return null;
	}

	public void AddObjectToDB(Payment newPayment)
	{
		String querySavePayment;
		if (newPayment.GetDescription() == null)
		{
			querySavePayment = "INSERT INTO payment (payment_date, booking_id, customer_id, room_id, booking_date, " +
							   "check_in, check_out, payment_amount) VALUES (" +
		   					   "'" + newPayment.GetPaymentDate() + "', " + 
		   					   "'" + newPayment.GetBookingID() + "', " +
		   					   "'" + newPayment.GetCustomerID() + "', " +
		   					   "'" + newPayment.GetRoomID() + "', " +
		   					   "'" + newPayment.GetBookingDate() + "', " +
		   					   "'" + newPayment.GetCheckInDate() + "', " +
		   					   "'" + newPayment.GetCheckOutDate() + "', " +
		   					   "'" + newPayment.GetAmount() + "');";
		}
		else
		{
			querySavePayment = "INSERT INTO payment (payment_date, booking_id, customer_id, room_id, booking_date, " +
							   "check_in, check_out, payment_amount, payment_description) VALUES (" +
		   					   "'" + newPayment.GetPaymentDate() + "', " + 
		   					   "'" + newPayment.GetBookingID() + "', " +
		   					   "'" + newPayment.GetCustomerID() + "', " +
		   					   "'" + newPayment.GetRoomID() + "', " +
		   					   "'" + newPayment.GetBookingDate() + "', " +
		   					   "'" + newPayment.GetCheckInDate() + "', " +
		   					   "'" + newPayment.GetCheckOutDate() + "', " +
		   					   "'" + newPayment.GetAmount() + "', " +
		   					   "'" + newPayment.GetDescription() + "');";
		}
		try
		{
			String queryUpdateBookingPaymentStatus = "UPDATE booking SET payment_status = true WHERE (booking_id = '" +
													  newPayment.GetBookingID() + "');";
					 
			PreparedStatement psUpdateBookingPaymentStatus = connection.prepareStatement(queryUpdateBookingPaymentStatus);
			psUpdateBookingPaymentStatus.executeUpdate();
			PreparedStatement psSavePayment = connection.prepareStatement(querySavePayment);
			psSavePayment.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public Payment GetInfoPaymentToDelete(Payment paymentToDelete)
	{
		Payment info = new Payment();
		
		String queryGetPaymentCustomerID = "SELECT customer_id FROM payment WHERE booking_id = '" + paymentToDelete.GetBookingID() + "'";
		String queryGetPaymentRoomID = "SELECT room_id FROM payment WHERE booking_id = '" + paymentToDelete.GetBookingID() + "'";
		String queryGetPaymentBookingDate = "SELECT booking_date FROM payment WHERE booking_id = '" + paymentToDelete.GetBookingID() + "'";
		String queryGetPaymentCheckIn = "SELECT check_in FROM payment WHERE booking_id = '" + paymentToDelete.GetBookingID() + "'";
		String queryGetPaymentCheckOut = "SELECT check_out FROM payment WHERE booking_id = '" + paymentToDelete.GetBookingID() + "'";
		String queryGetPaymentDate = "SELECT payment_date FROM payment WHERE booking_id = '" + paymentToDelete.GetBookingID() + "'";
		String queryGetPaymentAmount = "SELECT payment_amount FROM payment WHERE booking_id = '" + paymentToDelete.GetBookingID() + "'";
		String queryGetPaymentDescription = "SELECT payment_description FROM payment WHERE booking_id = '" + paymentToDelete.GetBookingID() + "'";
		
		try
		{	
			PreparedStatement psGetPaymentCustomerID = connection.prepareStatement(queryGetPaymentCustomerID);
			PreparedStatement psGetPaymentRoomID = connection.prepareStatement(queryGetPaymentRoomID);	
			PreparedStatement psGetPaymentBookingDate = connection.prepareStatement(queryGetPaymentBookingDate);
			PreparedStatement psGetPaymentCheckIn = connection.prepareStatement(queryGetPaymentCheckIn);
			PreparedStatement psGetPaymentCheckOut = connection.prepareStatement(queryGetPaymentCheckOut);
			PreparedStatement psGetPaymentDate = connection.prepareStatement(queryGetPaymentDate);
			PreparedStatement psGetPaymentAmount = connection.prepareStatement(queryGetPaymentAmount);
			PreparedStatement psGetPaymentDescription = connection.prepareStatement(queryGetPaymentDescription);
			
			ResultSet rsPaymentCustomerID = psGetPaymentCustomerID.executeQuery();
			ResultSet rsPaymentRoomID = psGetPaymentRoomID.executeQuery();
			ResultSet rsPaymentBookingDate = psGetPaymentBookingDate.executeQuery();
			ResultSet rsPaymentCheckIn = psGetPaymentCheckIn.executeQuery();
			ResultSet rsPaymentCheckOut = psGetPaymentCheckOut.executeQuery();
			ResultSet rsPaymentDate = psGetPaymentDate.executeQuery();
			ResultSet rsPaymentAmount = psGetPaymentAmount.executeQuery();
			ResultSet rsPaymentDescription = psGetPaymentDescription.executeQuery();
			
			if (rsPaymentCustomerID.next())
			{
				info.SetCustomerID(rsPaymentCustomerID.getString("customer_id"));
			}
			if (rsPaymentRoomID.next())
			{
				info.SetRoomID(rsPaymentRoomID.getString("room_id"));
			}
			if (rsPaymentBookingDate.next())
			{
				info.SetBookingDate(rsPaymentBookingDate.getString("booking_date"));
			}
			if (rsPaymentCheckIn.next())
			{
				info.SetCheckInDate(rsPaymentCheckIn.getString("check_in"));
			}
			if (rsPaymentCheckOut.next())
			{
				info.SetCheckOutDate(rsPaymentCheckOut.getString("check_out"));
			}
			if (rsPaymentDate.next())
			{
				info.SetPaymentDate(rsPaymentDate.getString("payment_date"));
			}
			if (rsPaymentAmount.next())
			{
				info.SetAmount(Integer.parseInt(rsPaymentAmount.getString("payment_amount")));
			}
			if (rsPaymentDescription.next())
			{
				info.SetDescription(rsPaymentDescription.getString("payment_description"));
			}
			
			return info;
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
		
		return null;
	}

	public void DeleteInfoFromDB(Payment paymentToDelete)
	{
		try
		{
			String queryDeletePayment = "DELETE FROM payment WHERE booking_id = '" + paymentToDelete.GetBookingID() + "'";
			PreparedStatement psDeletePayment = connection.prepareStatement(queryDeletePayment);
			psDeletePayment.execute();
			
			String queryDeleteBooking = "UPDATE booking SET payment_status = NULL WHERE booking_id = '" + paymentToDelete.GetBookingID() + "'";
			PreparedStatement psDeleteBooking = connection.prepareStatement(queryDeleteBooking);
			psDeleteBooking.execute();
		}
		catch (SQLException sqle1)
		{
			sqle1.printStackTrace();
		}
	}
}
package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataClasses.Booking;

public class DBQueryBooking
{
	private Connection connection;
	private ArrayList<Booking> bookingAL = new ArrayList<Booking>();
	private ArrayList<String> customerIDAL = new ArrayList<String>();
	private ArrayList<String> bookingIDAL = new ArrayList<String>();
	private ArrayList<String> bookingRoomIDAL = new ArrayList<String>();
	
	public DBQueryBooking(Connection connectionToSet)
	{
		this.connection = connectionToSet;
	}

	public ArrayList<Booking> GetInfoFromDB()
	{
		String queryBooking = "SELECT * FROM booking ORDER BY LENGTH(booking_id), booking_id;";
		try
		{
			Statement stBooking = connection.createStatement();
			ResultSet rsBooking = stBooking.executeQuery(queryBooking);
			
			while (rsBooking.next())
			{
				Booking b = new Booking();
				b.SetBookingID(rsBooking.getString("booking_id"));
				b.SetCustomerID(rsBooking.getString("customer_id"));
				b.SetRoomID(rsBooking.getString("room_id"));
				b.SetBookingDate(rsBooking.getString("booking_date"));
				b.SetCheckInDate(rsBooking.getString("check_in"));
				b.SetCheckOutDate(rsBooking.getString("check_out"));
				b.SetPaymentStatus(rsBooking.getString("payment_status"));
				
				bookingAL.add(b);
			}
			
			return bookingAL;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<String> GetCustomerIDAL()
	{
		try
		{
			String queryBookingCustomerIDList = "SELECT customer_id FROM customer_info ORDER BY customer_id";
			PreparedStatement psBookingCustomerIDList = connection.prepareStatement(queryBookingCustomerIDList);
			ResultSet rsBookingCustomerIDList = psBookingCustomerIDList.executeQuery();
			while (rsBookingCustomerIDList.next())
			{
				customerIDAL.add(rsBookingCustomerIDList.getString("customer_id"));
			}
			
			return customerIDAL;
		}
		catch(SQLException e3)
		{
			e3.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<String> GetBookingIDAL()
	{
		try
		{
			bookingIDAL.clear();
			
			String queryBookingIDList = "SELECT booking_id FROM booking ORDER BY LENGTH(booking_id), booking_id;";
			PreparedStatement psBookingIDList = connection.prepareStatement(queryBookingIDList);
			ResultSet rsBookingIDList = psBookingIDList.executeQuery();
			while(rsBookingIDList.next())
			{
				bookingIDAL.add(rsBookingIDList.getString("booking_id"));
			}
			
			return bookingIDAL;
		}
		catch (SQLException e3)
		{
			e3.printStackTrace();
		}
		
		return null;
	}

	public void AddObjectToDB(Booking newBooking)
	{
		//SQL query to save to Database
		String querySaveCustomerInfo = "INSERT INTO booking (booking_id, customer_id, room_id, booking_date, check_in, check_out) VALUES" +
									   "('" + newBooking.GetBookingID() + "', " +
				   					   "'" + newBooking.GetCustomerID() + "', " +
				   					   "'" + newBooking.GetRoomID() + "', " +
				   					   "'" + newBooking.GetBookingDate() + "', " +
				   					   "'" + newBooking.GetCheckInDate() + "', " +
				   					   "'" + newBooking.GetCheckOutDate() + "');";
		try {
			PreparedStatement psSaveCustomerInfo = connection.prepareStatement(querySaveCustomerInfo);
			psSaveCustomerInfo.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public Booking GetBookingInfoToDelete(Booking bookingToDelete)
	{
		Booking info = new Booking();
		String queryGetBookingCustomerID = "SELECT customer_id FROM booking WHERE booking_id = '" + bookingToDelete.GetBookingID() + "';";
		String queryGetBookingRoomID = "SELECT room_id FROM booking WHERE booking_id = '" + bookingToDelete.GetBookingID() + "';";
		String queryGetBookingDate = "SELECT booking_date FROM booking WHERE booking_id = '" + bookingToDelete.GetBookingID() + "';";
		String queryGetBookingCheckIn = "SELECT check_in FROM booking WHERE booking_id = '" + bookingToDelete.GetBookingID() + "';";
		String queryGetBookingCheckOut = "SELECT check_out FROM booking WHERE booking_id = '" + bookingToDelete.GetBookingID() + "';";
		String queryGetBookingPaymentStatus = "SELECT payment_status FROM booking WHERE booking_id = '" + bookingToDelete.GetBookingID() + "';";
		
		try
		{
			PreparedStatement psGetBookingCustomerID = connection.prepareStatement(queryGetBookingCustomerID);
			PreparedStatement psGetBookingRoomID = connection.prepareStatement(queryGetBookingRoomID);
			PreparedStatement psGetBookingDate = connection.prepareStatement(queryGetBookingDate);
			PreparedStatement psGetBookingCheckIn = connection.prepareStatement(queryGetBookingCheckIn);
			PreparedStatement psGetBookingCheckOut = connection.prepareStatement(queryGetBookingCheckOut);
			PreparedStatement psGetBookingPaymentStatus = connection.prepareStatement(queryGetBookingPaymentStatus);
			
			ResultSet rsBookingCustomerID = psGetBookingCustomerID.executeQuery();
			ResultSet rsBookingRoomID= psGetBookingRoomID.executeQuery();
			ResultSet rsBookingDate = psGetBookingDate.executeQuery();
			ResultSet rsBookingCheckIn = psGetBookingCheckIn.executeQuery();
			ResultSet rsBookingCheckOut = psGetBookingCheckOut.executeQuery();
			ResultSet rsBookingPaymentStatus = psGetBookingPaymentStatus.executeQuery();
			
			if (rsBookingCustomerID.next())
			{
				info.SetCustomerID(rsBookingCustomerID.getString("customer_id"));
			}
			if (rsBookingRoomID.next())
			{
				info.SetRoomID(rsBookingRoomID.getString("room_id"));
			}
			if (rsBookingDate.next())
			{
				info.SetBookingDate(rsBookingDate.getString("booking_date"));
			}
			if (rsBookingCheckIn.next())
			{
				info.SetCheckInDate(rsBookingCheckIn.getString("check_in"));
			}
			if (rsBookingCheckOut.next())
			{
				info.SetCheckOutDate(rsBookingCheckOut.getString("check_out"));
			}
			if (rsBookingPaymentStatus.next())
			{
				String paymentStatus = "";
				if (rsBookingPaymentStatus.getString("payment_status") != null)
				{
					paymentStatus = "Paid";
				}
				info.SetPaymentStatus(paymentStatus);
			}
			
			return info;
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
		
		return null;
	}

	public void DeleteInfoFromDB(Booking bookingToDelete)
	{
		String queryDeleteBooking = "DELETE FROM booking WHERE booking_id = '" + bookingToDelete.GetBookingID() + "'";
		try
		{
			PreparedStatement psDeleteBooking = connection.prepareStatement(queryDeleteBooking);
			psDeleteBooking.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> GetRoomIDAvailableAL(String datetimeCheckInStr, String datetimeCheckOutStr)
	{
		try
		{
			String queryBookingRoomIDList = "SELECT room_id FROM room WHERE room_id NOT IN (SELECT booking.room_id FROM booking WHERE " +
											"(check_in >= '" + datetimeCheckInStr + "' AND " +
											"check_in <= '" +  datetimeCheckOutStr + "') OR " +
											"(check_in <= '" + datetimeCheckInStr + "' AND " +
											"check_out >= '" + datetimeCheckOutStr + "') OR " +
											"(check_out <= '" + datetimeCheckOutStr + "' AND " +
											"check_out >= '" + datetimeCheckInStr + "') OR " +
											"(check_out >= '" + datetimeCheckOutStr + "' AND " +
											"check_in <= '" + datetimeCheckInStr + "'));";
			PreparedStatement psBookingRoomIDList = connection.prepareStatement(queryBookingRoomIDList);
			ResultSet rsBookingRoomIDList = psBookingRoomIDList.executeQuery();
			while(rsBookingRoomIDList.next())
			{
				this.bookingRoomIDAL.add(rsBookingRoomIDList.getString("room_id"));
			}
			
			return bookingRoomIDAL;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
package db;
import java.sql.*;
import java.util.ArrayList;

import dataClasses.CustomerInfo;

public class DBQueryCustomer
{
	private Connection connection;
	private ArrayList<CustomerInfo> customerAL = new ArrayList<CustomerInfo>();
	
	public DBQueryCustomer(Connection connectionToSet)
	{
		this.connection = connectionToSet;
	}
	
	public ArrayList<CustomerInfo> GetInfoFromDB()
	{
		String queryCustomerInfo = "SELECT * FROM hotel.customer_info ORDER BY name;";
		try
		{
			//Queries and ResultSet
			Statement stCustomerInfo = connection.createStatement();
			ResultSet rsCustomerInfo = stCustomerInfo.executeQuery(queryCustomerInfo);
			
			while (rsCustomerInfo.next())
			{
				CustomerInfo ci = new CustomerInfo();
				ci.SetID(rsCustomerInfo.getString("customer_id"));
				ci.SetName(rsCustomerInfo.getString("name"));
				ci.SetAddress(rsCustomerInfo.getString("address"));
				ci.SetPhone(rsCustomerInfo.getString("phone_number"));
				
				customerAL.add(ci);
			}
			
			return customerAL;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<String> GetIDAL()
	{
		try
		{
			ArrayList<String> IDAL = new ArrayList<String>();
			String queryCustomerIDList = "SELECT customer_id FROM customer_info ORDER BY customer_id;";
			PreparedStatement psCustomerIDList = connection.prepareStatement(queryCustomerIDList);
			ResultSet rsCustomerIDList = psCustomerIDList.executeQuery();
			while(rsCustomerIDList.next())
			{
				IDAL.add(rsCustomerIDList.getString("customer_id"));
			}
			
			return IDAL;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean CheckInputAdd(CustomerInfo newCustomer)
	{
		String checkCustomerInfoQuery = "SELECT * FROM customer_info WHERE customer_id = " + newCustomer.GetID() +
				" OR phone_number = " + newCustomer.GetPhone();
		PreparedStatement psCheckCInfo;
		try
		{
			psCheckCInfo = connection.prepareStatement(checkCustomerInfoQuery);
			ResultSet rsCheckCInfo = psCheckCInfo.executeQuery();
			if (rsCheckCInfo.next())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void AddObjectToDB(CustomerInfo newCustomer)
	{
		String querySaveCustomerInfo = "INSERT INTO customer_info (customer_id, name, address, phone_number)" +
				   "VALUES(" +
				   "'" + newCustomer.GetID() + "', " +
				   "'" + newCustomer.GetName() + "', " +
				   "'" + newCustomer.GetAddress() + "', " +
				   "'" + newCustomer.GetPhone() + "');";
		try
		{
			PreparedStatement psSaveCustomerInfo = connection.prepareStatement(querySaveCustomerInfo);
			psSaveCustomerInfo.executeUpdate();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
	}
	
	public CustomerInfo GetCustomerInfoToDelete(CustomerInfo customerToDelete)
	{
		CustomerInfo info = new CustomerInfo();
		String queryGetCustomerName = "SELECT name FROM customer_info WHERE customer_id = " + customerToDelete.GetID();
		String queryGetCustomerAddress = "SELECT address FROM customer_info WHERE customer_id = " + customerToDelete.GetID();
		String queryGetCustomerPhone = "SELECT phone_number FROM customer_info WHERE customer_id = " + customerToDelete.GetID();
		
		try
		{
			PreparedStatement psGetCustomerName = connection.prepareStatement(queryGetCustomerName);
			PreparedStatement psGetCustomerAddress = connection.prepareStatement(queryGetCustomerAddress);
			PreparedStatement psGetCustomerPhone = connection.prepareStatement(queryGetCustomerPhone);
			
			ResultSet rsCustomerName = psGetCustomerName.executeQuery();
			ResultSet rsCustomerAddress = psGetCustomerAddress.executeQuery();
			ResultSet rsCustomerPhone = psGetCustomerPhone.executeQuery();
			
			if (rsCustomerName.next())
			{
				info.SetName(rsCustomerName.getString("name"));
			}
			if (rsCustomerAddress.next())
			{
				info.SetAddress(rsCustomerAddress.getString("address"));
			}
			if (rsCustomerPhone.next())
			{
				info.SetPhone(rsCustomerPhone.getString("phone_number"));
			}
			
			return info;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void DeleteInfoFromDB(CustomerInfo customerToDelete)
	{
		String queryDeleteCustomer = "DELETE FROM customer_info WHERE customer_id = " + customerToDelete.GetID();
		try
		{
			PreparedStatement psDeleteCustomer = connection.prepareStatement(queryDeleteCustomer);
			psDeleteCustomer.execute();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}
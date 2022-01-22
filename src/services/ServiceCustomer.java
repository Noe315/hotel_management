package services;

import java.sql.Connection;
import java.util.ArrayList;

import dataClasses.CustomerInfo;
import db.DBQueryCustomer;

public class ServiceCustomer
{
	private DBQueryCustomer dbC;
	private ArrayList<CustomerInfo> AL;
	private ArrayList<String> IDAL;
	
	public void SetConnection(Connection connectionToSet)
	{
		dbC = new DBQueryCustomer(connectionToSet);
	}
	
	public ArrayList<CustomerInfo> GetAL()
	{
		AL = dbC.GetInfoFromDB();
		return AL;
	}
	
	public ArrayList<String> GetIDAL()
	{
		IDAL = dbC.GetIDAL();
		return IDAL;
	}
	
	public boolean CheckInputAdd(CustomerInfo newCustomer)
	{
		boolean isRecordExist = dbC.CheckInputAdd(newCustomer);
		return isRecordExist;
	}
	
	public void AddRecord(CustomerInfo newCustomer)
	{
		dbC.AddObjectToDB(newCustomer);
	}
	
	public CustomerInfo GetCustomerInfoToDelete(CustomerInfo customerToDelete)
	{
		 CustomerInfo info = dbC.GetCustomerInfoToDelete(customerToDelete);
		 return info;
	}
	
	public void DeleteRecord(CustomerInfo customerToDelete)
	{
		dbC.DeleteInfoFromDB(customerToDelete);
	}
}
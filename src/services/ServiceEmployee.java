package services;

import java.sql.Connection;
import java.util.ArrayList;

import dataClasses.Employee;
import dataClasses.Intern;
import dataClasses.Official;
import db.DBQueryEmployee;

public class ServiceEmployee
{
	private DBQueryEmployee dbE;
	private ArrayList<Intern> internAL;
	private ArrayList<Official> officialAL;
	private ArrayList<String> employeeIDAL;
	private ArrayList<String> officialIDAL;
	private ArrayList<String> internIDAL;
	
	public void SetConnection(Connection connectionToSet)
	{
		dbE = new DBQueryEmployee(connectionToSet);
	}
	
	public ArrayList<Intern> GetInternAL()
	{
		internAL = dbE.GetInfoInternFromDB();
		return internAL;
	}
	
	public ArrayList<Official> GetOfficialAL()
	{
		officialAL = dbE.GetInfoOfficialFromDB();
		return officialAL;
	}
	
	public boolean CheckInputAdd(Employee newEmployee)
	{
		boolean isRecordExist = dbE.CheckInputAdd(newEmployee);
		return isRecordExist;
	}
	
	public void AddRecordIntern(Intern newIntern)
	{
		dbE.AddInternToDB(newIntern);
	}
	
	public void AddRecordOfficial(Official newOfficial)
	{
		dbE.AddOfficialToDB(newOfficial);
	}
	
	public ArrayList<String> GetEmployeeIDAL()
	{
		employeeIDAL = dbE.GetEmployeeIDAL();
		return employeeIDAL;
	}
	
	public Intern GetInfoIntern(Intern intern)
	{
		Intern info = dbE.GetInfoIntern(intern);
		return info;
	}
	
	public Official GetInfoOfficial(Official official)
	{
		Official info = dbE.GetInfoOfficial(official);
		return info;
	}
	
	public String GetEmployeeType(String employeeID)
	{
		String type = dbE.GetEmployeeType(employeeID);
		return type;
	}
	
	public void DeleteRecord(String employeeIDToDelete)
	{
		dbE.DeleteInfoFromDB(employeeIDToDelete);
	}
	
	public ArrayList<String> GetOfficialIDAL()
	{
		officialIDAL = dbE.GetOfficialIDAL();
		return officialIDAL;
	}
	
	public ArrayList<String> GetInternIDAL()
	{
		internIDAL = dbE.GetInternIDAL();
		return internIDAL;
	}
	
	public void ChangeSalary(Official officialIDToChangeSalary, int amount)
	{
		dbE.ChangeSalary(officialIDToChangeSalary, amount);
	}
	
	public void MakeOfficial(Intern internToPromote, int salary)
	{
		dbE.MakeOfficial(internToPromote, salary);
	}
}
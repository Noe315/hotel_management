package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataClasses.Employee;
import dataClasses.Intern;
import dataClasses.Official;

public class DBQueryEmployee
{
	private Connection connection;
	private ArrayList<Intern> internAL = new ArrayList<Intern>();
	private ArrayList<Official> officialAL = new ArrayList<Official>();
	private ArrayList<String> employeeIDAL = new ArrayList<String>();
	private ArrayList<String> officialIDAL = new ArrayList<String>();
	private ArrayList<String> internIDAL = new ArrayList<String>();
	
	public DBQueryEmployee(Connection connectionToSet)
	{
		this.connection = connectionToSet;
	}
	
	public ArrayList<Intern> GetInfoInternFromDB()
	{
		String queryIntern = "SELECT employee.employee_id, name, address, phone_number, position, duration FROM employee " + 
				 			 "JOIN intern ON employee.employee_id = intern.employee_id ORDER BY name;";
		try
		{
			Statement stIntern = connection.createStatement();
			ResultSet rsIntern = stIntern.executeQuery(queryIntern);
			
			internAL.clear();
			while (rsIntern.next())
			{
				Intern i = new Intern();
				i.SetID(rsIntern.getString("position").substring(0, 3).toUpperCase() + rsIntern.getString("employee_id"));
				i.SetName(rsIntern.getString("name"));
				i.SetAddress(rsIntern.getString("address"));
				i.SetPhone(rsIntern.getString("phone_number"));
				i.SetPosition(rsIntern.getString("position"));
				i.SetDuration(Integer.parseInt(rsIntern.getString("duration")));
				
				internAL.add(i);
			}
			
			return internAL;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<Official> GetInfoOfficialFromDB()
	{
		String queryOfficial = "SELECT employee.employee_id, name, address, phone_number, position, salary FROM employee " + 
				   			   "JOIN official ON employee.employee_id = official.employee_id ORDER BY name;";
		try
		{
			Statement stOfficial = connection.createStatement();
			ResultSet rsOfficial = stOfficial.executeQuery(queryOfficial);
			
			officialAL.clear();
			while (rsOfficial.next())
			{
				Official o = new Official();
				o.SetID(rsOfficial.getString("position").substring(0, 3).toUpperCase() + rsOfficial.getString("employee_id"));
				o.SetName(rsOfficial.getString("name"));
				o.SetAddress(rsOfficial.getString("address"));
				o.SetPhone(rsOfficial.getString("phone_number"));
				o.SetPosition(rsOfficial.getString("position"));
				o.SetSalary(Integer.parseInt(rsOfficial.getString("salary")));
				
				officialAL.add(o);
			}
			
			return officialAL;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<String> GetEmployeeIDAL()
	{
		try
		{
			employeeIDAL.clear();
			String queryEmployeeIDList = "SELECT employee_id, position FROM employee ORDER BY employee_id;";
			PreparedStatement psEmployeeIDList = connection.prepareStatement(queryEmployeeIDList);
			ResultSet rsEmployeeIDList = psEmployeeIDList.executeQuery();
			
			while(rsEmployeeIDList.next())
			{
				employeeIDAL.add(rsEmployeeIDList.getString("position").substring(0, 3).toUpperCase() +
								 rsEmployeeIDList.getString("employee_id"));
			}
			
			return employeeIDAL;
		}
		catch (SQLException e3)
		{
			e3.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<String> GetOfficialIDAL()
	{
		try
		{
			officialIDAL.clear();
			String queryOfficialIDList = "SELECT employee.employee_id, position FROM employee " + 
										 "JOIN official ON employee.employee_id = official.employee_id ORDER BY employee_id;";
			PreparedStatement psOfficialIDList = connection.prepareStatement(queryOfficialIDList);
			ResultSet rsOfficialIDList = psOfficialIDList.executeQuery();
			
			while(rsOfficialIDList.next())
			{
				officialIDAL.add(rsOfficialIDList.getString("position").substring(0, 3).toUpperCase() +
								 rsOfficialIDList.getString("employee_id"));
			}
			
			return officialIDAL;
		}
		catch (SQLException e3)
		{
			e3.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<String> GetInternIDAL()
	{
		try
		{
			internIDAL.clear();
			String queryInternIDList = "SELECT employee.employee_id, position FROM employee " + 
									   "JOIN intern ON employee.employee_id = intern.employee_id ORDER BY employee_id;";
			PreparedStatement psInternIDList = connection.prepareStatement(queryInternIDList);
			ResultSet rsInternIDList = psInternIDList.executeQuery();
			
			while(rsInternIDList.next())
			{
				internIDAL.add(rsInternIDList.getString("position").substring(0, 3).toUpperCase() +
							   rsInternIDList.getString("employee_id"));
			}
			
			return internIDAL;
		}
		catch (SQLException e3)
		{
			e3.printStackTrace();
		}
		
		return null;
	}
	
	public boolean CheckInputAdd(Employee newEmployee)
	{
		try
		{
			String checkEmployeeInfoQuery = "SELECT * FROM employee WHERE employee_id = '" + newEmployee.GetID() + "'" +
											" OR phone_number = '" + newEmployee.GetPhone() + "';";
			PreparedStatement psCheckEInfo = connection.prepareStatement(checkEmployeeInfoQuery);
			ResultSet rsCheckEInfo = psCheckEInfo.executeQuery();
			
			if (rsCheckEInfo.next())
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

	public void AddInternToDB(Intern newIntern)
	{
		String newInternPosition = newIntern.GetPosition().substring(0,1).toUpperCase() + newIntern.GetPosition().substring(1);
		
		String querySaveEmployeeInfo = "INSERT INTO employee (employee_id, name, address, phone_number, position) VALUES (" +
									   "'" + newIntern.GetID() + "', " +
									   "'" + newIntern.GetName() + "', " +
									   "'" + newIntern.GetAddress() + "', " +
									   "'" + newIntern.GetPhone() + "', " +
									   "'" + newInternPosition + "');";
		String querySaveInternInfo = "INSERT INTO intern (employee_id, duration) VALUES (" +
									 "'" + newIntern.GetID() + "', " +
									 "'" + newIntern.GetDuration() + "');";
		try
		{
			PreparedStatement psSaveEmployeeInfo = connection.prepareStatement(querySaveEmployeeInfo);
			psSaveEmployeeInfo.executeUpdate();
			
			PreparedStatement psSaveInternInfo = connection.prepareStatement(querySaveInternInfo);
			psSaveInternInfo.executeUpdate();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
	}
	
	public void AddOfficialToDB(Official newOfficial)
	{
		String newEmployeePosition = newOfficial.GetPosition().substring(0,1).toUpperCase() + newOfficial.GetPosition().substring(1);
		
		String querySaveEmployeeInfo = "INSERT INTO employee (employee_id, name, address, phone_number, position) VALUES(" +
									   "'" + newOfficial.GetID() + "', " +
									   "'" + newOfficial.GetName() + "', " +
									   "'" + newOfficial.GetAddress() + "', " +
									   "'" + newOfficial.GetPhone() + "', " +
									   "'" + newEmployeePosition + "');";
		String querySaveOfficialInfo = "INSERT INTO official (employee_id, salary) VALUES (" +
									   "'" + newOfficial.GetID() + "', " +
									   "'" + newOfficial.GetSalary() + "');";
		try
		{
			PreparedStatement psSaveEmployeeInfo = connection.prepareStatement(querySaveEmployeeInfo);
			psSaveEmployeeInfo.executeUpdate();
			
			PreparedStatement psSaveOfficialInfo = connection.prepareStatement(querySaveOfficialInfo);
			psSaveOfficialInfo.executeUpdate();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
	}
	
	public String GetEmployeeType(String employeeID)
	{
		String queryGetEmployeeTypeOfficial = "SELECT employee.employee_id, name, address, phone_number, position, salary " +
											  "FROM employee " + 
											  "JOIN official ON employee.employee_id = official.employee_id " +
											  "WHERE official.employee_id = '" + employeeID + "' ORDER BY name;";
		String queryGetEmployeeTypeIntern = "SELECT employee.employee_id, name, address, phone_number, position, duration " +
											"FROM employee " +
											"JOIN intern ON employee.employee_id = intern.employee_id " +
											"WHERE intern.employee_id = '" + employeeID + "' ORDER BY name;";
		String type = "";
		try
		{
			PreparedStatement psGetTypeOfficial = connection.prepareStatement(queryGetEmployeeTypeOfficial);
			PreparedStatement psGetTypeIntern = connection.prepareStatement(queryGetEmployeeTypeIntern);
			
			ResultSet rsTypeOfficial = psGetTypeOfficial.executeQuery();
			ResultSet rsTypeIntern = psGetTypeIntern.executeQuery();
			
			if (rsTypeOfficial.next())
			{
				type = "Official";
			}
			else if (rsTypeIntern.next())
			{
				type = "Intern";
			}
			return type;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Intern GetInfoIntern(Intern intern)
	{
		Intern info = new Intern();
		try
		{
			String queryGetEmployeeName = "SELECT name FROM employee WHERE employee_id = '" + intern.GetID() + "';";
			String queryGetEmployeeAddress = "SELECT address FROM employee WHERE employee_id = '" + intern.GetID() + "';";
			String queryGetEmployeePhone = "SELECT phone_number FROM employee WHERE employee_id = '" + intern.GetID() + "';";
			String queryGetEmployeePosition = "SELECT position FROM employee WHERE employee_id = '" + intern.GetID() + "';";
			String queryGetInternDuration = "SELECT duration FROM intern WHERE employee_id = '" + intern.GetID() + "';";
			
			PreparedStatement psGetEmployeeName = connection.prepareStatement(queryGetEmployeeName);
			PreparedStatement psGetEmployeeAddress = connection.prepareStatement(queryGetEmployeeAddress);
			PreparedStatement psGetEmployeePhone = connection.prepareStatement(queryGetEmployeePhone);
			PreparedStatement psGetEmployeePosition = connection.prepareStatement(queryGetEmployeePosition);
			PreparedStatement psGetInternDuration = connection.prepareStatement(queryGetInternDuration);
			
			ResultSet rsEmployeeName = psGetEmployeeName.executeQuery();
			ResultSet rsEmployeeAddress = psGetEmployeeAddress.executeQuery();
			ResultSet rsEmployeePhone = psGetEmployeePhone.executeQuery();
			ResultSet rsEmployeePosition = psGetEmployeePosition.executeQuery();
			ResultSet rsInternDuration = psGetInternDuration.executeQuery();
			
			if (rsEmployeeName.next())
			{
				info.SetName(rsEmployeeName.getString("name"));
			}
			if (rsEmployeeAddress.next())
			{
				info.SetAddress(rsEmployeeAddress.getString("address"));
			}
			if (rsEmployeePhone.next())
			{
				info.SetPhone(rsEmployeePhone.getString("phone_number"));
			}
			if (rsEmployeePosition.next())
			{
				info.SetPosition(rsEmployeePosition.getString("position"));
			}
			if (rsInternDuration.next())
			{
				info.SetDuration(Integer.parseInt(rsInternDuration.getString("duration")));
			}
			
			return info;
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
		
		return null;
	}
	
	public Official GetInfoOfficial(Official official)
	{
		Official info = new Official();
		try
		{
			String queryGetEmployeeName = "SELECT name FROM employee WHERE employee_id = '" + official.GetID() + "';";
			String queryGetEmployeeAddress = "SELECT address FROM employee WHERE employee_id = '" + official.GetID() + "';";
			String queryGetEmployeePhone = "SELECT phone_number FROM employee WHERE employee_id = '" + official.GetID() + "';";
			String queryGetEmployeePosition = "SELECT position FROM employee WHERE employee_id = '" + official.GetID() + "';";
			String queryGetOfficialSalary = "SELECT salary FROM official WHERE employee_id = '" + official.GetID() + "';";
			
			PreparedStatement psGetEmployeeName = connection.prepareStatement(queryGetEmployeeName);
			PreparedStatement psGetEmployeeAddress = connection.prepareStatement(queryGetEmployeeAddress);
			PreparedStatement psGetEmployeePhone = connection.prepareStatement(queryGetEmployeePhone);
			PreparedStatement psGetEmployeePosition = connection.prepareStatement(queryGetEmployeePosition);
			PreparedStatement psGetOfficialSalary = connection.prepareStatement(queryGetOfficialSalary);
			
			ResultSet rsEmployeeName = psGetEmployeeName.executeQuery();
			ResultSet rsEmployeeAddress = psGetEmployeeAddress.executeQuery();
			ResultSet rsEmployeePhone = psGetEmployeePhone.executeQuery();
			ResultSet rsEmployeePosition = psGetEmployeePosition.executeQuery();
			ResultSet rsOfficialSalary = psGetOfficialSalary.executeQuery();
			
			if (rsEmployeeName.next())
			{
				info.SetName(rsEmployeeName.getString("name"));
			}
			if (rsEmployeeAddress.next())
			{
				info.SetAddress(rsEmployeeAddress.getString("address"));
			}
			if (rsEmployeePhone.next())
			{
				info.SetPhone(rsEmployeePhone.getString("phone_number"));
			}
			if (rsEmployeePosition.next())
			{
				info.SetPosition(rsEmployeePosition.getString("position"));
			}
			if (rsOfficialSalary.next())
			{
				info.SetSalary(Integer.parseInt(rsOfficialSalary.getString("salary")));
			}
			
			return info;
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
		
		return null;
	}

	public void DeleteInfoFromDB(String employeeIDToDelete)
	{
		try
		{
			String queryDeleteEmployee = "DELETE FROM employee WHERE employee_id = '" + employeeIDToDelete + "';";
			PreparedStatement psDeleteEmployee = connection.prepareStatement(queryDeleteEmployee);
			psDeleteEmployee.execute();
		}
		catch (SQLException sqle1)
		{
			sqle1.printStackTrace();
		}
	}
	
	public void ChangeSalary(Official officialIDToChangeSalary, int amount)
	{
		String queryChangeSalary = "UPDATE official SET salary = " + amount + " WHERE employee_id = '" + officialIDToChangeSalary.GetID() + "';";
		
		try
		{
			PreparedStatement psChangeSalary = connection.prepareStatement(queryChangeSalary);
			psChangeSalary.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void MakeOfficial(Intern internToPromote, int salary)
	{
		String queryAddToOfficial = "INSERT INTO official (employee_id, salary) VALUES (" +
									"'" + internToPromote.GetID() + "', " +
									"'" + salary + "');";
		String queryDeletePromotedIntern = "DELETE FROM intern WHERE employee_id = '" + internToPromote.GetID() + "';";
		
		try
		{
			PreparedStatement psAddToOfficial = connection.prepareStatement(queryAddToOfficial);
			PreparedStatement psDeletePromotedIntern = connection.prepareStatement(queryDeletePromotedIntern);
			
			psAddToOfficial.executeUpdate();
			psDeletePromotedIntern.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
package dataClasses;

public class Official extends Employee
{
	private int salary;
	
	public void SetSalary(int salaryToSet)
	{
		this.salary = salaryToSet;
	}
	
	public int GetSalary()
	{
		return this.salary;
	}
}
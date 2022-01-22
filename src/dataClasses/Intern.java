package dataClasses;

import services.ServiceEmployee;

public class Intern extends Employee
{
	private int duration;
	private ServiceEmployee serviceE;
	
	public void MakeOfficial(int salary, ServiceEmployee service)
	{
		serviceE = service;
		serviceE.MakeOfficial(this, salary);
	}
	
	public void SetDuration(int durationToSet)
	{
		this.duration = durationToSet;
	}
	
	public int GetDuration()
	{
		return this.duration;
	}
}
package dataClasses;

public abstract class Employee
{
	protected String ID, name, address, phone, position;
	
	public Employee()
	{
		
	}
	
	public Employee(String cstrID, String cstrName, String cstrAddress, String cstrPhone, String cstrPosition)
	{
		this.ID = cstrID;
		this.name = cstrName;
		this.address = cstrAddress;
		this.phone = cstrPhone;
		this.position = cstrPosition;
	}
	
	public String GetID()
	{
		return ID;
	}
	
	public String GetName()
	{
		return name;
	}
	
	public String GetAddress()
	{
		return address;
	}
	
	public String GetPhone()
	{
		return phone;
	}
	
	public String GetPosition()
	{
		return position;
	}
	
	public void SetID(String IDToSet)
	{
		this.ID = IDToSet;
	}
	
	public void SetName(String nameToSet)
	{
		this.name = nameToSet;
	}
	
	public void SetAddress(String addressToSet)
	{
		this.address = addressToSet;
	}
	
	public void SetPhone(String phoneToSet)
	{
		this.phone = phoneToSet;
	}
	
	public void SetPosition(String positionToSet)
	{
		this.position = positionToSet;
	}
}
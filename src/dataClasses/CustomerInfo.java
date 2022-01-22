package dataClasses;

public class CustomerInfo
{
	private String ID, name, address, phone;
	
	public CustomerInfo()
	{
		
	}
	
	public CustomerInfo(String cstrID, String cstrName, String cstrAddress, String cstrPhone)
	{
		this.ID = cstrID;
		this.name = cstrName;
		this.address = cstrAddress;
		this.phone = cstrPhone;
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
}
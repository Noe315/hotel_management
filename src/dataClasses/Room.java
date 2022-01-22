package dataClasses;

public class Room
{
	private String ID, description;
	private int cost;
	
	public Room()
	{
		
	}
	
	public Room(String cstrID, String cstrDescription, int cstrCost)
	{
		this.ID = cstrID;
		this.description = cstrDescription;
		this.cost = cstrCost;
	}
	
	public String GetID()
	{
		return ID;
	}
	
	public String GetDescription()
	{
		return description;
	}
	
	public int GetCost()
	{
		return cost;
	}
	
	public void SetID(String IDToSet)
	{
		this.ID = IDToSet;
	}
	
	public void SetDescription(String descriptionToSet)
	{
		this.description = descriptionToSet;
	}
	
	public void SetCost(int costToSet)
	{
		this.cost = costToSet;
	}
}
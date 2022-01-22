package services;

import java.sql.Connection;
import java.util.ArrayList;

import dataClasses.Room;
import db.DBQueryRoom;

public class ServiceRoom {
	private DBQueryRoom dbR;
	private ArrayList<Room> AL;
	private ArrayList<String> roomIDAL;
	
	public ArrayList<Room> GetAL()
	{
		AL = dbR.GetInfoFromDB();
		return AL;
	}
	
	public void SetConnection(Connection connectionToSet)
	{
		dbR = new DBQueryRoom(connectionToSet);
	}
	
	public boolean CheckInputAdd(Room roomToAdd)
	{
		boolean isRecordExist = dbR.CheckInputAdd(roomToAdd);
		return isRecordExist;
	}
	
	public void AddRecord(Room roomToAdd)
	{
		dbR.AddObjectToDB(roomToAdd);
	}
	
	public ArrayList<String> GetRoomIDAL()
	{
		roomIDAL = dbR.GetRoomIDAL();
		return roomIDAL;
	}
	
	public Room GetRoomInfo(Room room)
	{
		Room info = dbR.GetRoomInfo(room);
		return info;
	}
	
	public void DeleteRecord(Room roomToDelete)
	{
		dbR.DeleteInfoFromDB(roomToDelete);
	}
}

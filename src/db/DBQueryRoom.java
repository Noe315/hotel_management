package db;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataClasses.Room;

public class DBQueryRoom
{
	private Connection connection;
	private ArrayList<Room> roomAL = new ArrayList<Room>();
	private ArrayList<String> roomIDAL = new ArrayList<String>();
	
	public DBQueryRoom(Connection connectionToSet)
	{
		this.connection = connectionToSet;
	}

	public ArrayList<Room> GetInfoFromDB()
	{
		String queryRoom = "SELECT * FROM hotel.room;";
		try
		{
			//Queries and ResultSet
			Statement stRoom = connection.createStatement();
			ResultSet rsRoom = stRoom.executeQuery(queryRoom);
			
			while (rsRoom.next())
			{
				Room r = new Room();
				r.SetID(rsRoom.getString("room_id"));
				r.SetDescription(rsRoom.getString("description"));
				r.SetCost(rsRoom.getInt("cost"));
				
				roomAL.add(r);
			}
			
			return roomAL;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean CheckInputAdd(Room roomToAdd)
	{
		String checkRoomIDQuery = "SELECT * FROM room WHERE room_id = '" + roomToAdd.GetID() + "'";
		try
		{
			PreparedStatement psCheckRoomID = connection.prepareStatement(checkRoomIDQuery);
			ResultSet rsCheckRoomID = psCheckRoomID.executeQuery();
			
			if (rsCheckRoomID.next())
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
	
	public void AddObjectToDB(Room roomToAdd)
	{
		//SQL query to save to Database
		String querySaveRoom = "INSERT INTO room (room_id, description, cost) VALUES(" +
				   			   "'" + roomToAdd.GetID() + "', " +
				   			   "'" + roomToAdd.GetDescription() + "', " +
				   			   "'" + roomToAdd.GetCost() + "');";
		try
		{
			PreparedStatement psSaveRoom = connection.prepareStatement(querySaveRoom);
			psSaveRoom.executeUpdate();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
	}
	
	public ArrayList<String> GetRoomIDAL()
	{
		String queryRoomIDList = "SELECT room_id FROM room ORDER BY room_id;";
		try
		{
			roomIDAL.clear();
			PreparedStatement psRoomIDList = connection.prepareStatement(queryRoomIDList);
			ResultSet rsRoomIDList = psRoomIDList.executeQuery();
			while(rsRoomIDList.next())
			{
				roomIDAL.add(rsRoomIDList.getString("room_id"));
			}
			
			return roomIDAL;
		}
		catch (SQLException e3)
		{
			e3.printStackTrace();
		}
		
		return null;
	}
	
	public Room GetRoomInfo(Room room)
	{
		Room info = new Room();
		String queryGetRoomDescription = "SELECT description FROM room WHERE room_id = '" + room.GetID() + "'";
		String queryGetRoomCost = "SELECT cost FROM room WHERE room_id = '" + room.GetID() + "'";
		try
		{
			PreparedStatement psGetRoomDescription = connection.prepareStatement(queryGetRoomDescription);
			PreparedStatement psGetRoomCost= connection.prepareStatement(queryGetRoomCost);
			
			ResultSet rsRoomDescription = psGetRoomDescription.executeQuery();
			ResultSet rsRoomCost = psGetRoomCost.executeQuery();
											
			if (rsRoomDescription.next())
			{
				info.SetDescription(rsRoomDescription.getString("description"));
			}
			if (rsRoomCost.next())
			{
				info.SetCost(Integer.parseInt(rsRoomCost.getString("cost")));
			}
			
			return info;
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
		
		return null;
	}

	public void DeleteInfoFromDB(Room roomToDelete)
	{
		String queryDeleteRoom = "DELETE FROM room WHERE room_id = '" + roomToDelete.GetID() + "'";
		try
		{
			PreparedStatement psDeleteRoom = connection.prepareStatement(queryDeleteRoom);
			psDeleteRoom.execute();
		}
		catch (SQLException sqle1)
		{
			sqle1.printStackTrace();
		}
	}
}
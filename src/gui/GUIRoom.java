package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dataClasses.Room;
import services.ServiceRoom;

public class GUIRoom
{
	private ServiceRoom serviceR = new ServiceRoom();
	private ArrayList<Room> roomAL;
	private DefaultTableModel modelBooking, modelPayment;
	
	//Create button to add Booking
	private JPanel panelRoom = new JPanel();
	private JPanel panelRoomButtons = new JPanel();
	
	//Booking tab
	private DefaultTableModel modelRoom = new DefaultTableModel();
	private JTable tableRoom = new JTable(modelRoom);
	private JScrollPane scrollRoom = new JScrollPane(tableRoom);
	
	//Buttons of Booking tab
	private JButton buttonAddRoom = new JButton("Add Room");
	private JButton buttonDeleteRoom = new JButton("Delete Room");
	
	public GUIRoom(Connection connectionToSet)
	{
		serviceR.SetConnection(connectionToSet);
		this.roomAL = serviceR.GetAL();
	}
	
	public void SetModelBooking(DefaultTableModel modelBookingToSet)
	{
		this.modelBooking = modelBookingToSet;
	}
	
	public void SetModelPayment(DefaultTableModel modelPaymentToSet)
	{
		this.modelPayment = modelPaymentToSet;
	}
	
	public JPanel Draw() {
		//Set layout of buttons panel
		panelRoomButtons.setLayout(new BoxLayout(panelRoomButtons, BoxLayout.Y_AXIS));
		
		// Create columns for Booking tab
		modelRoom.addColumn("Room Number");
		modelRoom.addColumn("Description"); 
		modelRoom.addColumn("Cost");
		
		//Set layout of table
		scrollRoom.setPreferredSize(new Dimension(1000,300));
		//scrollEmployee.setViewportView(tableEmployee);
		//panelEmployee.setLayout(new FlowLayout());
		//tableRoom.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableRoom.getColumnModel().getColumn(0).setPreferredWidth(100);
		tableRoom.getColumnModel().getColumn(1).setPreferredWidth(800);
		tableRoom.getColumnModel().getColumn(2).setPreferredWidth(100);
		
		//Prevent user from editing table cell
		tableRoom.setDefaultEditor(Object.class, null);
		
		//Iterator to traverse through array list
		Iterator<Room> iter = null;
		iter = roomAL.listIterator();
		
		while (iter.hasNext())
		{
			String ID, description;
			int cost;
			Room r = iter.next();
			ID = r.GetID();
			description = r.GetDescription();
			cost = r.GetCost();
			
			modelRoom.addRow(new Object[] {ID, description, cost});
		}
		
		panelRoom.add(scrollRoom);
		panelRoomButtons.add(buttonAddRoom);
		panelRoomButtons.add(buttonDeleteRoom);
		panelRoom.add(panelRoomButtons);
		
		AddBehaviorToAddButton();
		AddBehaviorToDeleteButton();
		
		return panelRoom;
	}
	
	private void AddBehaviorToAddButton()
	{
		buttonAddRoom.addActionListener(new ActionListener()
		{  
			public void actionPerformed(ActionEvent e){
				//JTextFields for Room Info input
				JTextField roomID = new JTextField(20);
				JTextField roomDescription = new JTextField(100);
				roomDescription.setColumns(30);
				JTextField roomCost = new JTextField();
				
				//Panel for Room Info input
				JPanel inputPanel = new JPanel();
				inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
				inputPanel.add(new JLabel("Room ID:"));
				inputPanel.add(roomID);
				inputPanel.add(new JLabel("Room Description:"));
				inputPanel.add(roomDescription);
				inputPanel.add(new JLabel("Room Cost:"));
				inputPanel.add(roomCost);
				
				//Show a JOptionPane for input
				int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Room Info", JOptionPane.OK_CANCEL_OPTION);
				//When the user presses "OK" button of JOptionPane
				if (result == JOptionPane.OK_OPTION)
				{
					//Infinite loop only breaks when the user inputs valid Room Info or "Cancel"/"Close" button is pressed
					while (true)
					{
						//Check if room cost is an integer or not
						boolean isRoomCostValid = false;
						try
						{
							int roomCostInt = Integer.parseInt(roomCost.getText());
							if (roomCostInt > 0)
							{
								isRoomCostValid = true;
							}
							else
							{
								isRoomCostValid = false;
							}
						}
						catch (NumberFormatException nfe)
						{
							nfe.printStackTrace();
						}
						
						//Check for input validity
						boolean isRoomIDValid = Pattern.matches("^[0-9]*[aAbB]$", roomID.getText());	//ID valid
						boolean isRoomIDNull = roomID.getText().equals("");								//The field is not empty
						boolean isRoomDescriptionNull = roomDescription.getText().equals("");			//The field is not empty
						boolean isRoomCostNull = roomCost.getText().equals("");							//The field is not empty
						
						if (isRoomIDValid == true &&
							isRoomCostValid == true &&
							isRoomIDNull == false &&
							isRoomDescriptionNull == false &&
							isRoomCostNull == false)
						{
							Room roomToAdd = new Room();
							String roomIDToAdd = roomID.getText().substring(0, roomID.getText().length()-1) +
												 roomID.getText().substring(roomID.getText().length()-1).toUpperCase();
							roomToAdd.SetID(roomIDToAdd);
							roomToAdd.SetCost(Integer.parseInt(roomCost.getText()));
							roomToAdd.SetDescription(roomDescription.getText());
							
							boolean isRecordExist = serviceR.CheckInputAdd(roomToAdd);
							if (isRecordExist == true)
							{
								JOptionPane.showMessageDialog(inputPanel, "Room ID already existed.",
															  "Invalid Info", JOptionPane.ERROR_MESSAGE);
								int n = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Room Info", JOptionPane.OK_CANCEL_OPTION);
								if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
								{
									break;
								}
							}
							else
							{
								serviceR.AddRecord(roomToAdd);
								JOptionPane.showMessageDialog(inputPanel, "Added new record.");
								UpdateAdd(roomToAdd);
								break;
							}
						}
						else
						{
							JOptionPane.showMessageDialog(inputPanel, "Input is invalid.", "Invalid Info", JOptionPane.ERROR_MESSAGE);
							int n = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Room Info", JOptionPane.OK_CANCEL_OPTION);
							if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
							{
								break;
							}
						}
					}
				}
			}  
		});
	}

	public void UpdateAdd(Room roomToAdd)
	{
		modelRoom.addRow(new Object[] {roomToAdd.GetID(),
								 	   roomToAdd.GetDescription(),
								 	   roomToAdd.GetCost()});
	}
	
	private void AddBehaviorToDeleteButton()
	{
		buttonDeleteRoom.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel deletePanel = new JPanel();
				deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
				
				//Dropdown list for Payment ID
				ArrayList<String> roomIDAL = serviceR.GetRoomIDAL();
				JComboBox roomIDComboBox = new JComboBox(roomIDAL.toArray());
				
				JTextField roomDescription = new JTextField();
				roomDescription.setEditable(false);
				JTextField roomCost = new JTextField();
				roomCost.setEditable(false);
				
				roomIDComboBox.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String roomIDSelected = (String) roomIDComboBox.getSelectedItem();
						Room roomToDelete = new Room();
						roomToDelete.SetID(roomIDSelected);
						
						Room info = serviceR.GetRoomInfo(roomToDelete);
						roomDescription.setText(info.GetDescription());
						roomCost.setText(Integer.toString(info.GetCost()));
					}
				});
				
				deletePanel.add(new JLabel("Room ID:"));
				deletePanel.add(roomIDComboBox);
				deletePanel.add(new JLabel("Room Description:"));
				deletePanel.add(roomDescription);
				deletePanel.add(new JLabel("Room Cost:"));
				deletePanel.add(roomCost);
				
				int result = JOptionPane.showConfirmDialog(null, deletePanel, "Select Room To Delete", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{
					while (true)
					{
						int n = JOptionPane.showConfirmDialog(null, "Do you want to delete this ROOM?\n" +
															  "This will delete every BOOKING and PAYMENT related to this ROOM.",
															  "Delete Booking", JOptionPane.OK_CANCEL_OPTION,
															  JOptionPane.WARNING_MESSAGE);
						if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
						{
							break;
						}
						else
						{
							String roomIDSelected = (String)roomIDComboBox.getSelectedItem().toString();
							Room roomToDelete = new Room();
							roomToDelete.SetID(roomIDSelected);
							
							String roomDescriptionSelected = roomDescription.getText();
							
							boolean isRoomDescriptionSelectedNull = roomDescriptionSelected.equals("");
							
							if (isRoomDescriptionSelectedNull == false)
							{
								serviceR.DeleteRecord(roomToDelete);
								JOptionPane.showMessageDialog(deletePanel, "Deleted record.");
								UpdateDelete(roomToDelete);
								break;
							}
							else
							{
								JOptionPane.showMessageDialog(deletePanel, "Room description must not be empty.", "Invalid Info", JOptionPane.ERROR_MESSAGE);
								int n2 = JOptionPane.showConfirmDialog(null, deletePanel, "Select Room To Delete", JOptionPane.OK_CANCEL_OPTION);
								if (n2 == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
								{
									break;
								}
							}
						}
					}
				}
			}
		});
	}

	public void UpdateDelete(Room roomToDelete)
	{
		int rowToDelete = -1;
		for (int i = modelRoom.getRowCount() - 1; i >= 0; --i)
		{
			if (modelRoom.getValueAt(i, 0).equals(roomToDelete.GetID()))
			{
				rowToDelete = i;
				break;
			}
		}
		modelRoom.removeRow(rowToDelete);
		
		int rowBookingToDelete = -1;
		for (int i = modelBooking.getRowCount() - 1; i >= 0; --i)
		{
			if (modelBooking.getValueAt(i, 2).equals(roomToDelete.GetID()))
			{
				rowBookingToDelete = i;
				modelBooking.removeRow(rowBookingToDelete);
			}
		}
		
		int rowPaymentToDelete = -1;
		for (int i = modelPayment.getRowCount() - 1; i >= 0; --i)
		{
			if (modelPayment.getValueAt(i, 3).equals(roomToDelete.GetID()))
			{
				rowPaymentToDelete = i;
				modelPayment.removeRow(rowPaymentToDelete);
			}
		}
	}
}
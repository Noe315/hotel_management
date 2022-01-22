package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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

import dataClasses.Payment;
import services.ServicePayment;

public class GUIPayment
{
	private ServicePayment serviceP = new ServicePayment();
	private ArrayList<Payment> paymentAL;
	private DefaultTableModel modelBooking;
	
	//Create button to add Booking
	private JPanel panelPayment = new JPanel();
	private JPanel panelPaymentButtons = new JPanel();
	
	//Booking tab
	private DefaultTableModel modelPayment = new DefaultTableModel();
	private JTable tablePayment = new JTable(modelPayment);
	private JScrollPane scrollPayment = new JScrollPane(tablePayment);
	
	//Buttons of Booking tab
	private JButton buttonAddPayment = new JButton("Add Payment");
	private JButton buttonDeletePayment = new JButton("Delete Payment");
	
	public GUIPayment(Connection connectionToSet)
	{
		serviceP.SetConnection(connectionToSet);
		this.paymentAL = serviceP.GetAL();
	}
	
	public void SetModelBooking(DefaultTableModel cstrModelBooking)
	{
		this.modelBooking = cstrModelBooking;
	}
	
	public DefaultTableModel GetModelPayment()
	{
		return this.modelPayment;
	}

	public JPanel Draw() {
		//Set layout of buttons panel
		panelPaymentButtons.setLayout(new BoxLayout(panelPaymentButtons, BoxLayout.Y_AXIS));
		
		// Create columns for Booking tab
		modelPayment.addColumn("Payment Date");
		modelPayment.addColumn("Booking ID"); 
		modelPayment.addColumn("Customer ID");
		modelPayment.addColumn("Room ID");
		modelPayment.addColumn("Booking Date");
		modelPayment.addColumn("Check In");
		modelPayment.addColumn("Check Out");
		modelPayment.addColumn("Payment Amount");
		modelPayment.addColumn("Payment Description");
		
		//Set layout of table
		scrollPayment.setPreferredSize(new Dimension(1200,300));
		//scrollEmployee.setViewportView(tableEmployee);
		//panelEmployee.setLayout(new FlowLayout());
		tablePayment.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePayment.getColumnModel().getColumn(0).setPreferredWidth(170);
		tablePayment.getColumnModel().getColumn(1).setPreferredWidth(80);
		tablePayment.getColumnModel().getColumn(2).setPreferredWidth(100);
		tablePayment.getColumnModel().getColumn(3).setPreferredWidth(100);
		tablePayment.getColumnModel().getColumn(4).setPreferredWidth(170);
		tablePayment.getColumnModel().getColumn(5).setPreferredWidth(170);
		tablePayment.getColumnModel().getColumn(6).setPreferredWidth(170);
		tablePayment.getColumnModel().getColumn(7).setPreferredWidth(110);
		tablePayment.getColumnModel().getColumn(8).setPreferredWidth(150);
		
		//Prevent user from editing table cell
		tablePayment.setDefaultEditor(Object.class, null);
		
		//Iterator to traverse through array list
		Iterator<Payment> iter = null;
		iter = paymentAL.listIterator();
		
		while (iter.hasNext())
		{
			String paymentDate, bookingID, customerID, roomID, bookingDate, checkInDate, checkOutDate, paymentDescription;
			int paymentAmount;
			Payment p = iter.next();
			paymentDate = p.GetPaymentDate();
			bookingID = p.GetBookingID();
			customerID = p.GetCustomerID();
			bookingDate = p.GetBookingDate();
			roomID = p.GetRoomID();
			checkInDate = p.GetCheckInDate();
			checkOutDate = p.GetCheckOutDate();
			paymentAmount = p.GetAmount();
			paymentDescription = p.GetDescription();
			
			modelPayment.addRow(new Object[] {paymentDate, bookingID, customerID, roomID,
											  bookingDate, checkInDate, checkOutDate, paymentAmount, paymentDescription});
		}
		
		panelPayment.add(scrollPayment);
		panelPaymentButtons.add(buttonAddPayment);
		panelPaymentButtons.add(buttonDeletePayment);
		panelPayment.add(panelPaymentButtons);
		
		AddBehaviorToAddButton();
		AddBehaviorToDeleteButton();
		
		return panelPayment;
	}

	private void AddBehaviorToAddButton()
	{
		buttonAddPayment.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				//JTextFields for Payment Info input
				JTextField paymentAmount = new JTextField();
				JTextField paymentDescription = new JTextField(100);
				paymentDescription.setColumns(30);
				
				JTextField paymentCustomerID = new JTextField();
				paymentCustomerID.setEditable(false);
				JTextField paymentRoomID = new JTextField();
				paymentRoomID.setEditable(false);
				JTextField paymentBookingDate = new JTextField();
				paymentBookingDate.setEditable(false);
				JTextField paymentCheckIn = new JTextField();
				paymentCheckIn.setEditable(false);
				JTextField paymentCheckOut = new JTextField();
				paymentCheckOut.setEditable(false);
				
				//Dropdown list for Booking ID of Payment table
				ArrayList<String> paymentBookingIDAL = serviceP.GetNonPaymentBookingIDAL();
				JComboBox paymentBookingIDComboBox = new JComboBox(paymentBookingIDAL.toArray());
				
				paymentBookingIDComboBox.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String paymentBookingIDValue = (String)paymentBookingIDComboBox.getSelectedItem().toString();
						Payment currentPayment = new Payment();
						currentPayment.SetBookingID(paymentBookingIDValue);
						
						Payment info = serviceP.GetInfoNonPaymentBooking(currentPayment);
						paymentCustomerID.setText(info.GetCustomerID());
						paymentAmount.setText(Integer.toString(info.GetAmount()));
						paymentDescription.setText(info.GetDescription());
						paymentRoomID.setText(info.GetRoomID());
						paymentBookingDate.setText(info.GetBookingDate());
						paymentCheckIn.setText(info.GetCheckInDate());
						paymentCheckOut.setText(info.GetCheckOutDate());
					}
				});
				
				//Panel for Payment input
				JPanel inputPanel = new JPanel();
				inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
				inputPanel.add(new JLabel("Payment Booking ID:"));
				inputPanel.add(paymentBookingIDComboBox);
				inputPanel.add(new JLabel("Payment Customer ID:"));
				inputPanel.add(paymentCustomerID);
				inputPanel.add(new JLabel("Payment Room ID:"));
				inputPanel.add(paymentRoomID);
				inputPanel.add(new JLabel("Payment Booking Date:"));
				inputPanel.add(paymentBookingDate);
				inputPanel.add(new JLabel("Payment Check-in:"));
				inputPanel.add(paymentCheckIn);
				inputPanel.add(new JLabel("Payment Check-out:"));
				inputPanel.add(paymentCheckOut);
				inputPanel.add(new JLabel("Payment Amount:"));
				inputPanel.add(paymentAmount);
				inputPanel.add(new JLabel("Payment Description:"));
				inputPanel.add(paymentDescription);
				
				//Show a JOptionPane for input
				int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Payment Info", JOptionPane.OK_CANCEL_OPTION);
				
				//When the user presses "OK" button of JOptionPane
				if (result == JOptionPane.OK_OPTION)
				{
					//Infinite loop only breaks when the user inputs valid Room Info or "Cancel"/"Close" button is pressed
					while (true)
					{
						//Get Booking ID, Employee ID, Payment Date, and Payment Time for Booking table
						String paymentBookingIDValue = (String)paymentBookingIDComboBox.getSelectedItem().toString();
						String paymentCustomerIDValue = paymentCustomerID.getText();
						
						//Check for input validity
						boolean isPaymentAmountNull = paymentAmount.getText().equals("");								//The field is not empty
						boolean isPaymentCustomerIDNull = paymentCustomerIDValue.equals("");
						
						//Check if payment amount is a positive integer or not
						boolean isPaymentAmountValid = false;
						try
						{
							int paymentAmountInt = Integer.parseInt(paymentAmount.getText());
							if (paymentAmountInt > 0)
							{
								isPaymentAmountValid = true;
							}
							else
							{
								isPaymentAmountValid = false;
							}
						}
						catch (NumberFormatException nfe)
						{
							nfe.printStackTrace();
						}
						
						//Check for input validity
						if (isPaymentAmountValid == true &&
							isPaymentAmountNull == false &&
							isPaymentCustomerIDNull == false)
						{	
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date currentDate = new Date();
							String paymentDateTime = sdf.format(currentDate);
									
							boolean isPaymentDescriptionNull = paymentDescription.getText().equals("");
							//SQL query to save to Database
							Payment newPayment = new Payment();
							newPayment.SetPaymentDate(paymentDateTime);
							newPayment.SetBookingID(paymentBookingIDValue);
							newPayment.SetCustomerID(paymentCustomerIDValue);
							newPayment.SetRoomID(paymentRoomID.getText());
							newPayment.SetBookingDate(paymentBookingDate.getText());
							newPayment.SetCheckInDate(paymentCheckIn.getText());
							newPayment.SetCheckOutDate(paymentCheckOut.getText());
							newPayment.SetAmount(Integer.parseInt(paymentAmount.getText()));
							newPayment.SetDescription(paymentDescription.getText());
							
							serviceP.AddRecord(newPayment);
							
							JOptionPane.showMessageDialog(inputPanel, "Added new record.");
							
							//Update Payment table
							UpdateAdd(newPayment);
							break;
						}
						else
						{
							JOptionPane.showMessageDialog(inputPanel, "Input is invalid.", "Invalid Info", JOptionPane.ERROR_MESSAGE);
							int n = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Payment Info", JOptionPane.OK_CANCEL_OPTION);
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
	
	public void UpdateAdd(Payment newPayment)
	{
		modelPayment.addRow(new Object[] {newPayment.GetPaymentDate(),
										  newPayment.GetBookingID(),
										  newPayment.GetCustomerID(),
										  newPayment.GetRoomID(),
										  newPayment.GetBookingDate(),
										  newPayment.GetCheckInDate(),
										  newPayment.GetCheckOutDate(),
										  newPayment.GetAmount(),
										  newPayment.GetDescription()});

		int rowBookingToUpdate = -1;
		for (int i = modelBooking.getRowCount() - 1; i >= 0; --i)
		{
			if (modelBooking.getValueAt(i, 0).equals(newPayment.GetBookingID()))
			{
				rowBookingToUpdate = i;
				break;
			}
		}
		modelBooking.setValueAt("Paid", rowBookingToUpdate, 6);
	}
	
	private void AddBehaviorToDeleteButton()
	{
		buttonDeletePayment.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel deletePanel = new JPanel();
				deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
				
				//Dropdown list for Payment ID
				ArrayList<String> paymentBookingIDAL = serviceP.GetAllBookingIDAL();
				JComboBox paymentBookingIDComboBox = new JComboBox(paymentBookingIDAL.toArray());
				
				JTextField paymentCustomerID = new JTextField();
				paymentCustomerID.setEditable(false);
				JTextField paymentRoomID = new JTextField();
				paymentRoomID.setEditable(false);
				JTextField paymentBookingDate = new JTextField();
				paymentBookingDate.setEditable(false);
				JTextField paymentCheckIn = new JTextField();
				paymentCheckIn.setEditable(false);
				JTextField paymentCheckOut = new JTextField();
				paymentCheckOut.setEditable(false);
				JTextField paymentDate = new JTextField();
				paymentDate.setEditable(false);
				JTextField paymentAmount = new JTextField();
				paymentAmount.setEditable(false);
				JTextField paymentDescription = new JTextField();
				paymentDescription.setEditable(false);
				
				paymentBookingIDComboBox.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String paymentBookingIDSelected = (String) paymentBookingIDComboBox.getSelectedItem();
						Payment paymentToDelete = new Payment();
						paymentToDelete.SetBookingID(paymentBookingIDSelected);
						
						Payment info = new Payment();
						info = serviceP.GetInfoPaymentToDelete(paymentToDelete);
						paymentCustomerID.setText(info.GetCustomerID());
						paymentRoomID.setText(info.GetRoomID());
						paymentBookingDate.setText(info.GetBookingDate());
						paymentCheckIn.setText(info.GetCheckInDate());
						paymentCheckOut.setText(info.GetCheckOutDate());
						paymentDate.setText(info.GetPaymentDate());
						paymentAmount.setText(Integer.toString(info.GetAmount()));
						paymentDescription.setText(info.GetDescription());
					}
				});
				
				deletePanel.add(new JLabel("Booking ID:"));
				deletePanel.add(paymentBookingIDComboBox);
				deletePanel.add(new JLabel("Customer ID:"));
				deletePanel.add(paymentCustomerID);
				deletePanel.add(new JLabel("Room ID:"));
				deletePanel.add(paymentRoomID);
				deletePanel.add(new JLabel("Booking Date:"));
				deletePanel.add(paymentBookingDate);
				deletePanel.add(new JLabel("Check-in:"));
				deletePanel.add(paymentCheckIn);
				deletePanel.add(new JLabel("Check-out:"));
				deletePanel.add(paymentCheckOut);
				deletePanel.add(new JLabel("Payment Date:"));
				deletePanel.add(paymentDate);
				deletePanel.add(new JLabel("Payment Amount:"));
				deletePanel.add(paymentAmount);
				deletePanel.add(new JLabel("Payment Description:"));
				deletePanel.add(paymentDescription);
				
				int result = JOptionPane.showConfirmDialog(null, deletePanel, "Select Room To Delete", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{
					while (true)
					{
						int n = JOptionPane.showConfirmDialog(null, "Delete a payment will also delete its corresponding value in BOOKING table, "
															  + "do you want to continue?", "Delete Payment", JOptionPane.OK_CANCEL_OPTION,
															  JOptionPane.WARNING_MESSAGE);
						if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
						{
							break;
						}
						else
						{
							String paymentBookingIDSelected = (String)paymentBookingIDComboBox.getSelectedItem().toString();
							String paymentCustomerIDSelected = paymentCustomerID.getText();
	
							boolean isPaymentCustomerIDSelectedNull = paymentCustomerIDSelected.equals("");
							
							if (isPaymentCustomerIDSelectedNull == false)
							{
								//Delete
								Payment paymentToDelete = new Payment();
								paymentToDelete.SetBookingID(paymentBookingIDSelected);
								serviceP.DeleteRecord(paymentToDelete);
								
								//Delete dialog
								JOptionPane.showMessageDialog(deletePanel, "Deleted record.");
								
								//Update tables
								UpdateDelete(paymentToDelete);
								
								break;
							}
							else
							{
								JOptionPane.showMessageDialog(deletePanel, "Customer ID must not be empty.", "Invalid Info", JOptionPane.ERROR_MESSAGE);
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

	public void UpdateDelete(Payment paymentToDelete)
	{
		int rowPaymentToDelete = -1;
		for (int i = modelPayment.getRowCount() - 1; i >= 0; --i) {
            if (modelPayment.getValueAt(i, 1).equals(paymentToDelete.GetBookingID()))
            {
                rowPaymentToDelete = i;
                break;
            }
	    }
		modelPayment.removeRow(rowPaymentToDelete);
		
		int rowBookingToDelete = -1;
		for (int i = modelBooking.getRowCount() - 1; i >= 0; --i)
		{
			if (modelBooking.getValueAt(i, 0).equals(paymentToDelete.GetBookingID()))
			{
				rowBookingToDelete = i;
				break;
			}
		}
		modelBooking.setValueAt("", rowBookingToDelete, 6);
	}
}
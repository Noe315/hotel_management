package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

import LGoodDatePicker.HandleDateTimeChange;
import dataClasses.Booking;
import services.ServiceBooking;

public class GUIBooking
{
	private Connection connection;
	private ServiceBooking serviceB = new ServiceBooking();
	private ArrayList<Booking> bookingAL;
	private DefaultTableModel modelPayment;
	
	//Create button to add Booking
	private JPanel panelBooking = new JPanel();
	private JPanel panelBookingButtons = new JPanel();
	
	//Booking tab
	private DefaultTableModel modelBooking = new DefaultTableModel();
	private JTable tableBooking = new JTable(modelBooking);
	private JScrollPane scrollBooking = new JScrollPane(tableBooking);
	
	//Buttons of Booking tab
	private JButton buttonAddBooking = new JButton("Add Booking");
	private JButton buttonDeleteBooking = new JButton("Delete Booking");
	
	public GUIBooking(Connection connectionToSet)
	{
		this.connection = connectionToSet;
		serviceB.SetConnection(connectionToSet);
		this.bookingAL = serviceB.GetAL();
	}
	
	public void SetModelPayment(DefaultTableModel cstrModelPayment)
	{
		this.modelPayment = cstrModelPayment;
	}
	
	public JPanel Draw() {
		//Set layout of buttons panel
		panelBookingButtons.setLayout(new BoxLayout(panelBookingButtons, BoxLayout.Y_AXIS));
		
		// Create columns for Booking tab 
		modelBooking.addColumn("Booking ID"); 
		modelBooking.addColumn("Customer ID");
		modelBooking.addColumn("Room ID");
		modelBooking.addColumn("Booking Date");
		modelBooking.addColumn("Check In");
		modelBooking.addColumn("Check Out");
		modelBooking.addColumn("Payment Status");
		
		//Set layout of table
		scrollBooking.setPreferredSize(new Dimension(1100,300));
		//scrollEmployee.setViewportView(tableEmployee);
		//panelEmployee.setLayout(new FlowLayout());
		tableBooking.getColumnModel().getColumn(0).setPreferredWidth(50);
		tableBooking.getColumnModel().getColumn(1).setPreferredWidth(80);
		tableBooking.getColumnModel().getColumn(2).setPreferredWidth(100);
		tableBooking.getColumnModel().getColumn(3).setPreferredWidth(150);
		tableBooking.getColumnModel().getColumn(4).setPreferredWidth(150);
		tableBooking.getColumnModel().getColumn(5).setPreferredWidth(150);
		tableBooking.getColumnModel().getColumn(6).setPreferredWidth(100);
		
		//Prevent user from editing table cell
		tableBooking.setDefaultEditor(Object.class, null);
		
		//Iterator to traverse through array list
		Iterator<Booking> iter = null;
		iter = bookingAL.listIterator();
		
		while (iter.hasNext())
		{
			String ID, customerID, roomID, bookingDate, checkInDate, checkOutDate, paymentStatus;
			Booking b = iter.next();
			ID = b.GetBookingID();
			customerID = b.GetCustomerID();
			bookingDate = b.GetBookingDate();
			roomID = b.GetRoomID();
			checkInDate = b.GetCheckInDate();
			checkOutDate = b.GetCheckOutDate();
			paymentStatus = b.GetPaymentStatus();
			
			if (paymentStatus != null)
			{
				paymentStatus = "Paid";
			}
			
			modelBooking.addRow(new Object[] {ID, customerID, roomID, bookingDate, checkInDate, checkOutDate, paymentStatus});
		}
		
		panelBooking.add(scrollBooking);
		panelBookingButtons.add(buttonAddBooking);
		panelBookingButtons.add(buttonDeleteBooking);
		panelBooking.add(panelBookingButtons);
		
		AddBehaviorToAddButton();
		AddBehaviorToDeleteButton();
		
		return panelBooking;
	}
	
	public DefaultTableModel GetModelBooking()
	{
		return this.modelBooking;
	}
	
	private void AddBehaviorToAddButton()
	{
		buttonAddBooking.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				
				//JTextFields, DatePickers, TimePickers, and TimePickerSettings for Booking input
				
				//Dropdown list for Customer ID
				ArrayList<String> bookingCustomerIDAL = serviceB.GetCustomerIDAL();
				JComboBox bookingCustomerIDComboBox = new JComboBox(bookingCustomerIDAL.toArray());
				
				//TimePicker
				TimePickerSettings tpSettingsCheckIn = new TimePickerSettings();
				tpSettingsCheckIn.use24HourClockFormat();
				tpSettingsCheckIn.setInitialTimeToNow();
				TimePicker checkInTime = new TimePicker(tpSettingsCheckIn);
				
				TimePickerSettings tpSettingsCheckOut = new TimePickerSettings();
				tpSettingsCheckOut.use24HourClockFormat();
				tpSettingsCheckOut.setInitialTimeToNow();
				TimePicker checkOutTime = new TimePicker(tpSettingsCheckOut);
				
				//DatePicker
				String dateFormatCommonEra = "YYYY-MM-dd";
				String dateFormatBeforeCommonEra = "uuuu-MM-dd";
				
				DatePickerSettings dpSettingsCheckIn = new DatePickerSettings();
				dpSettingsCheckIn.setFormatForDatesCommonEra(dateFormatCommonEra);
				dpSettingsCheckIn.setFormatForDatesBeforeCommonEra(dateFormatBeforeCommonEra);
				dpSettingsCheckIn.setAllowKeyboardEditing(false);
				DatePicker checkInDate = new DatePicker(dpSettingsCheckIn);
				checkInDate.setDateToToday();
				
				DatePickerSettings dpSettingsCheckOut = new DatePickerSettings();
				dpSettingsCheckOut.setFormatForDatesCommonEra(dateFormatCommonEra);
				dpSettingsCheckOut.setFormatForDatesBeforeCommonEra(dateFormatBeforeCommonEra);
				dpSettingsCheckOut.setAllowKeyboardEditing(false);
				DatePicker checkOutDate = new DatePicker(dpSettingsCheckOut);
				checkOutDate.setDateToToday();
				
				//Panel for Customer Info input
				JPanel inputPanel = new JPanel();
				inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
				inputPanel.add(new JLabel("Cutomer ID:"));
				inputPanel.add(bookingCustomerIDComboBox);
				inputPanel.add(new JLabel("Check-in Date:"));
				inputPanel.add(checkInDate);
				inputPanel.add(new JLabel("Check-in Time:"));
				inputPanel.add(checkInTime);
				inputPanel.add(new JLabel("Check-out Date:"));
				inputPanel.add(checkOutDate);
				inputPanel.add(new JLabel("Check-out Time:"));
				inputPanel.add(checkOutTime);
				
				JComboBox bookingRoomIDComboBox = new JComboBox();
				
				HandleDateTimeChange dtc = new HandleDateTimeChange(checkInDate, checkInTime, checkOutDate, checkOutTime,
																	bookingRoomIDComboBox);
				dtc.SetConnection(connection);
				checkInDate.addDateChangeListener(dtc);
				checkOutDate.addDateChangeListener(dtc);
				checkInTime.addTimeChangeListener(dtc);
				checkOutTime.addTimeChangeListener(dtc);
				
				inputPanel.add(new JLabel("Room ID:"));
				inputPanel.add(bookingRoomIDComboBox);
				
				//Show a JOptionPane for input
				int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Booking Info", JOptionPane.OK_CANCEL_OPTION);
				//When the user presses "OK" button of JOptionPane
				if (result == JOptionPane.OK_OPTION)
				{
					//Infinite loop only breaks when the user inputs valid Customer Info or "Cancel"/"Close" button is pressed
					while (true)
					{
						//Get Customer ID, Room ID, Booking Date, and Booking Time for Booking table
						String bookingCustomerIDValue = (String)bookingCustomerIDComboBox.getSelectedItem().toString();
						String checkInDateTime = checkInDate.getText() + " " + checkInTime.getText();
						String checkOutDateTime = checkOutDate.getText() + " " + checkOutTime.getText();
						
						//Check if room ID is null
						boolean isBookingRoomIDNull = true;
						if (bookingRoomIDComboBox.getSelectedIndex() != -1)
						{
							isBookingRoomIDNull = false;
						}
						
						//Check for input validity of Check-in date and time
						boolean isCheckInDateNull = checkInDate.getText().equals("");								//The field is not empty
						boolean isCheckInTimeNull = checkInTime.getText().equals("");								//The field is not empty
						boolean isCheckInDateTimeValid = false;
						try
						{
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							sdf.parse(checkInDateTime);
							isCheckInDateTimeValid = true;
						}
						catch (ParseException pe)
						{
							pe.printStackTrace();
						}
						
						//Check for input validity of Check-out date and time
						boolean isCheckOutDateNull = checkOutDate.getText().equals("");								//The field is not empty
						boolean isCheckOutTimeNull = checkOutTime.getText().equals("");								//The field is not empty
						boolean isCheckOutDateTimeValid = false;
						try
						{
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							sdf.parse(checkOutDateTime);
							isCheckOutDateTimeValid = true;
						}
						catch (ParseException pe)
						{
							pe.printStackTrace();
						}
						
						//Check if check-out date and time is BEFORE check-in date and time
						boolean isCheckInOutDateTimeValid = false;
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
						LocalDate dateTimeCheckInLD = LocalDate.parse(checkInDateTime, dtf);
						LocalDate dateTimeCheckOutLD = LocalDate.parse(checkOutDateTime, dtf);
						if (dateTimeCheckOutLD.isBefore(dateTimeCheckInLD) || dateTimeCheckOutLD.isEqual(dateTimeCheckInLD))
						{
							isCheckInOutDateTimeValid = false;
						}
						else
						{
							isCheckInOutDateTimeValid = true;
						}
						
						//Check for input validity
						if (isCheckInDateTimeValid == true &&
							isCheckOutDateTimeValid == true &&
							isCheckInOutDateTimeValid == true &&
							isCheckInDateNull == false &&
							isCheckInTimeNull == false &&
							isCheckOutDateNull == false &&
							isCheckOutTimeNull == false &&
							isBookingRoomIDNull == false)
						{								
							//Automatically add current date and time of Booking creation
							Date currentDateTime = new Date();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String bookingDateTime = sdf.format(currentDateTime);
							String bookingRoomIDValue = (String)bookingRoomIDComboBox.getSelectedItem().toString();
							
							String newBookingID = serviceB.GetNewBookingID();
							Booking newBooking = new Booking();
							newBooking.SetBookingID(newBookingID);
							newBooking.SetCustomerID(bookingCustomerIDValue);
							newBooking.SetBookingDate(bookingDateTime);
							newBooking.SetCheckInDate(checkInDateTime);
							newBooking.SetCheckOutDate(checkOutDateTime);
							newBooking.SetRoomID(bookingRoomIDValue);
							
							//SQL query to save to Database
							serviceB.AddRecord(newBooking);
							
							JOptionPane.showMessageDialog(inputPanel, "Added new record.");
							
							//Update Booking table
							UpdateAdd(newBooking);
							break;
						}
						else
						{
							JOptionPane.showMessageDialog(inputPanel, "Input is invalid.", "Invalid Info", JOptionPane.ERROR_MESSAGE);
							int n = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Booking Info", JOptionPane.OK_CANCEL_OPTION);
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

	private void UpdateAdd(Booking newBooking)
	{
		modelBooking.addRow(new Object[] {newBooking.GetBookingID(),
										  newBooking.GetCustomerID(),
										  newBooking.GetRoomID(),
										  newBooking.GetBookingDate(),
										  newBooking.GetCheckInDate(),
										  newBooking.GetCheckOutDate()});
	}
	
	private void AddBehaviorToDeleteButton()
	{
		buttonDeleteBooking.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel deletePanel = new JPanel();
				deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
				
				//Dropdown list for Payment ID
				ArrayList<String> bookingIDAL = serviceB.GetBookingIDAL();
				JComboBox bookingIDComboBox = new JComboBox(bookingIDAL.toArray());
				
				JTextField bookingCustomerID = new JTextField();
				bookingCustomerID.setEditable(false);
				JTextField bookingRoomID = new JTextField();
				bookingRoomID.setEditable(false);
				JTextField bookingDate= new JTextField();
				bookingDate.setEditable(false);
				JTextField bookingCheckIn = new JTextField();
				bookingCheckIn.setEditable(false);
				JTextField bookingCheckOut = new JTextField();
				bookingCheckOut.setEditable(false);
				JTextField bookingPaymentStatus = new JTextField();
				bookingPaymentStatus.setEditable(false);
				
				bookingIDComboBox.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String bookingIDSelected = (String) bookingIDComboBox.getSelectedItem();
						Booking bookingToDelete = new Booking();
						bookingToDelete.SetBookingID(bookingIDSelected);
						
						Booking info = serviceB.GetBookingInfoToDelete(bookingToDelete);
						bookingCustomerID.setText(info.GetCustomerID());
						bookingRoomID.setText(info.GetRoomID());
						bookingDate.setText(info.GetBookingDate());
						bookingCheckIn.setText(info.GetCheckInDate());
						bookingCheckOut.setText(info.GetCheckOutDate());
						bookingPaymentStatus.setText(info.GetPaymentStatus());
					}
				});
				
				deletePanel.add(new JLabel("Booking ID:"));
				deletePanel.add(bookingIDComboBox);
				deletePanel.add(new JLabel("Customer ID:"));
				deletePanel.add(bookingCustomerID);
				deletePanel.add(new JLabel("Room ID:"));
				deletePanel.add(bookingRoomID);
				deletePanel.add(new JLabel("Booking Date:"));
				deletePanel.add(bookingDate);
				deletePanel.add(new JLabel("Check-in:"));
				deletePanel.add(bookingCheckIn);
				deletePanel.add(new JLabel("Check-out:"));
				deletePanel.add(bookingCheckOut);
				deletePanel.add(new JLabel("Payment Status:"));
				deletePanel.add(bookingPaymentStatus);
				
				int result = JOptionPane.showConfirmDialog(null, deletePanel, "Select Booking To Delete", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{
					while (true)
					{
						int n = JOptionPane.showConfirmDialog(null, "Delete a BOOKING will also delete its corresponding value in " +
															  "PAYMENT table.\n Do you want to continue?", "Delete Booking", JOptionPane.OK_CANCEL_OPTION,
															  JOptionPane.WARNING_MESSAGE);
						if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
						{
							break;
						}
						else
						{
							Booking bookingToDelete = new Booking();
							String bookingIDSelected = (String)bookingIDComboBox.getSelectedItem().toString();
							String bookingCustomerIDSelected = bookingCustomerID.getText();
							bookingToDelete.SetBookingID(bookingIDSelected);
							
							boolean isBookingCustomerIDSelectedNull = bookingCustomerIDSelected.equals("");
							
							if (isBookingCustomerIDSelectedNull == false)
							{
								serviceB.DeleteRecord(bookingToDelete);
								JOptionPane.showMessageDialog(deletePanel, "Deleted record.");
								UpdateDelete(bookingToDelete);
								
								break;
							}
							else
							{
								JOptionPane.showMessageDialog(deletePanel, "Customer ID must not be empty.",
										  "Invalid Info", JOptionPane.ERROR_MESSAGE);
								int n2 = JOptionPane.showConfirmDialog(null, deletePanel, "Select Booking To Delete",
																	  JOptionPane.OK_CANCEL_OPTION);
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

	private void UpdateDelete(Booking bookingToDelete)
	{
		int rowBookingToDelete = -1;
		for (int i = modelBooking.getRowCount() - 1; i >= 0; --i)
		{
			if (modelBooking.getValueAt(i, 0).equals(bookingToDelete.GetBookingID()))
			{
				rowBookingToDelete = i;
				break;
			}
		}
		modelBooking.removeRow(rowBookingToDelete);
		
		/*GUIPayment guiPayment = new GUIPayment();
		DefaultTableModel modelPayment = guiPayment.GetModelPayment();*/
		int rowPaymentToDelete = -1;
		for (int i = modelPayment.getRowCount() - 1; i >= 0; --i)
		{
			if (modelPayment.getValueAt(i, 1).equals(bookingToDelete.GetBookingID()))
			{
				rowPaymentToDelete = i;
				break;
			}
		}
		if (rowPaymentToDelete != -1)
		{
			modelPayment.removeRow(rowPaymentToDelete);
		}
	}
}
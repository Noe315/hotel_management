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

import dataClasses.CustomerInfo;
import services.ServiceCustomer;

public class GUICustomer
{
	private ServiceCustomer serviceC = new ServiceCustomer();
	private ArrayList<CustomerInfo> customerAL;
	private CustomerInfo newCustomer;
	private DefaultTableModel modelBooking, modelPayment;

	//Customer tab
	private JPanel panelCustomerInfo = new JPanel();
	private JPanel panelCustomerInfoButtons = new JPanel();
	
	private DefaultTableModel modelCustomer = new DefaultTableModel();
	private JTable tableCustomer = new JTable(modelCustomer);
	private JScrollPane scrollCustomer= new JScrollPane(tableCustomer);
	
	private JButton buttonAddCustomerInfo = new JButton("Add Customer");
	private JButton buttonDeleteCustomerInfo = new JButton("Delete Customer");
	
	public GUICustomer(Connection connectionToSet)
	{
		serviceC.SetConnection(connectionToSet);
		this.customerAL = serviceC.GetAL();
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
		panelCustomerInfoButtons.setLayout(new BoxLayout(panelCustomerInfoButtons, BoxLayout.Y_AXIS));
		
		// Create columns for Customer tab
		modelCustomer.addColumn("ID Card Number");
		modelCustomer.addColumn("Name");
		modelCustomer.addColumn("Address");
		modelCustomer.addColumn("Phone Number");
		
		//Set layout of table
		scrollCustomer.setPreferredSize(new Dimension(600,300));
		//scrollEmployee.setViewportView(tableEmployee);
		//panelEmployee.setLayout(new FlowLayout());
		//tableCustomer.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableCustomer.getColumnModel().getColumn(0).setPreferredWidth(100);
		tableCustomer.getColumnModel().getColumn(1).setPreferredWidth(200);
		tableCustomer.getColumnModel().getColumn(2).setPreferredWidth(200);
		tableCustomer.getColumnModel().getColumn(3).setPreferredWidth(100);
		
		//Prevent user from editing table cell
		tableCustomer.setDefaultEditor(Object.class, null);
		
		//Iterator to traverse through array list
		Iterator<CustomerInfo> iter = null;
		iter = customerAL.listIterator();
		
		while (iter.hasNext())
		{
			String ID, name, address, phone;
			CustomerInfo ci = iter.next();
			ID = ci.GetID();
			name = ci.GetName();
			address = ci.GetAddress();
			phone = ci.GetPhone();
			
			modelCustomer.addRow(new Object[] {ID, name, address, phone});
		}
		
		panelCustomerInfo.add(scrollCustomer);
		panelCustomerInfoButtons.add(buttonAddCustomerInfo);
		panelCustomerInfoButtons.add(buttonDeleteCustomerInfo);
		panelCustomerInfo.add(panelCustomerInfoButtons);
		
		AddBehaviorToAddButton();
		AddBehaviorToDeleteButton();
		
		return panelCustomerInfo;
	}
	
	private void AddBehaviorToAddButton()
	{
		buttonAddCustomerInfo.addActionListener(new ActionListener()
		{  
			public void actionPerformed(ActionEvent e){
				//JTextFields for Customer Info input
				JTextField customerID = new JTextField(20);
				JTextField customerName = new JTextField(30);
				JTextField customerAddress = new JTextField(50);
				JTextField customerPhone = new JTextField(15);
				
				//Panel for Customer Info input
				JPanel inputPanel = new JPanel();
				inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
				inputPanel.add(new JLabel("Customer ID:"));
				inputPanel.add(customerID);
				inputPanel.add(new JLabel("Cutomer Full Name:"));
				inputPanel.add(customerName);
				inputPanel.add(new JLabel("Customer Address:"));
				inputPanel.add(customerAddress);
				inputPanel.add(new JLabel("Customer Phone:"));
				inputPanel.add(customerPhone);
				
				//Show a JOptionPane for input
				int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Customer Info", JOptionPane.OK_CANCEL_OPTION);
				//When the user presses "OK" button of JOptionPane
				if (result == JOptionPane.OK_OPTION)
				{
					//Infinite loop only breaks when the user inputs valid Customer Info or "Cancel"/"Close" button is pressed
					while (true)
					{
						//Check for input validity
						boolean isPhoneValid = Pattern.matches("^[0-9]*$", customerPhone.getText());	//Phone only contains numbers
						boolean isCustomerIDValid = Pattern.matches("^[0-9]*$", customerID.getText());	//ID only contains numbers
						boolean isCustomerIDNull = customerID.getText().equals("");						//The field is not empty
						boolean isCustomerNameNull = customerName.getText().equals("");					//The field is not empty
						boolean isCustomerAddressNull = customerAddress.getText().equals("");			//The field is not empty
						boolean isCustomerPhoneNull = customerPhone.getText().equals("");				//The field is not empty
						
						//Check for input validity
						if (isPhoneValid == true &&
							isCustomerIDValid == true &&
							isCustomerIDNull == false &&
							isCustomerNameNull == false &&
							isCustomerAddressNull == false &&
							isCustomerPhoneNull == false)
						{
							newCustomer = new CustomerInfo();
							newCustomer.SetID(customerID.getText());
							newCustomer.SetName(customerName.getText());
							newCustomer.SetAddress(customerAddress.getText());
							newCustomer.SetPhone(customerPhone.getText());
							boolean isRecordExist = serviceC.CheckInputAdd(newCustomer);
							
							if (isRecordExist)
							{
								JOptionPane.showMessageDialog(inputPanel, "Customer ID or Phone Number is invalid.",
															  "Invalid Info", JOptionPane.ERROR_MESSAGE);
								int n = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Customer Info",
																	  JOptionPane.OK_CANCEL_OPTION);
								if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
								{
									break;
								}
							}
							//If this condition is met, that means the record DOES NOT exist.
							else
							{
								//SQL query to save to Database
								serviceC.AddRecord(newCustomer);
								
								//Successfully added new record dialog
								JOptionPane.showMessageDialog(inputPanel, "Added new record.");
								
								//Update Customer Info table
								UpdateAdd(newCustomer);
								
								break;
							}
						}
						else
						{
							JOptionPane.showMessageDialog(inputPanel, "Input is invalid.",
									  "Invalid Info", JOptionPane.ERROR_MESSAGE);
							int n = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Customer Info",
																  JOptionPane.OK_CANCEL_OPTION);
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

	private void UpdateAdd(CustomerInfo customerToAdd)
	{
		modelCustomer.addRow(new Object[] {customerToAdd.GetID(),
							  			   customerToAdd.GetName(),
							  			   customerToAdd.GetAddress(),
							  			   customerToAdd.GetPhone()});
	}
	
	private void AddBehaviorToDeleteButton()
	{
		buttonDeleteCustomerInfo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel deletePanel = new JPanel();
				deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
				
				//Dropdown list for Payment ID
				ArrayList<String> customerIDAL = serviceC.GetIDAL();
				JComboBox customerIDComboBox = new JComboBox(customerIDAL.toArray());
				
				JTextField customerName = new JTextField();
				customerName.setEditable(false);
				JTextField customerAddress = new JTextField();
				customerAddress.setEditable(false);
				JTextField customerPhone = new JTextField();
				customerPhone.setEditable(false);
				
				customerIDComboBox.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String customerIDSelected = (String) customerIDComboBox.getSelectedItem();
						CustomerInfo customerToDelete = new CustomerInfo();
						customerToDelete.SetID(customerIDSelected);
						
						CustomerInfo info = serviceC.GetCustomerInfoToDelete(customerToDelete);
						customerName.setText(info.GetName());
						customerAddress.setText(info.GetAddress());
						customerPhone.setText(info.GetPhone());
					}
				});
				
				deletePanel.add(new JLabel("Customer ID:"));
				deletePanel.add(customerIDComboBox);
				deletePanel.add(new JLabel("Customer Name:"));
				deletePanel.add(customerName);
				deletePanel.add(new JLabel("Customer Address:"));
				deletePanel.add(customerAddress);
				deletePanel.add(new JLabel("Customer Phone:"));
				deletePanel.add(customerPhone);
				
				int result = JOptionPane.showConfirmDialog(null, deletePanel, "Select Customer To Delete", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{
					while (true)
					{
						int n = JOptionPane.showConfirmDialog(null, "Do you want to delete this CUSTOMER?\n" +
															  "This will also delete every BOOKING and PAYMENT related to this CUSTOMER.",
															  "Delete Customer", JOptionPane.OK_CANCEL_OPTION,
															  JOptionPane.WARNING_MESSAGE);
						if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
						{
							break;
						}
						else
						{
							CustomerInfo customerToDelete = new CustomerInfo();
							String customerIDSelected = (String)customerIDComboBox.getSelectedItem().toString();
							String customerNameSelected = customerName.getText();
							
							customerToDelete.SetID(customerIDSelected);
							
							boolean isCustomerNameSelectedNull = customerNameSelected.equals("");
							
							if (isCustomerNameSelectedNull == false)
							{
								serviceC.DeleteRecord(customerToDelete);
								UpdateDelete(customerToDelete);
								
								JOptionPane.showMessageDialog(deletePanel, "Deleted record.");
								
								break;
							}
							else
							{
								JOptionPane.showMessageDialog(deletePanel, "Customer name must not be empty.", "Invalid Info", JOptionPane.ERROR_MESSAGE);
								int n2 = JOptionPane.showConfirmDialog(null, deletePanel, "Select Customer To Delete", JOptionPane.OK_CANCEL_OPTION);
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

	private void UpdateDelete(CustomerInfo customerToDelete)
	{
		int rowToDelete = -1;
		for (int i = modelCustomer.getRowCount() - 1; i >= 0; --i)
		{
			if (modelCustomer.getValueAt(i, 0).equals(customerToDelete.GetID()))
			{
				rowToDelete = i;
				break;
			}
		}
		modelCustomer.removeRow(rowToDelete);
		
		int rowBookingToDelete = -1;
		for (int i = modelBooking.getRowCount() - 1; i >= 0; --i)
		{
			if (modelBooking.getValueAt(i, 1).equals(customerToDelete.GetID()))
			{
				rowBookingToDelete = i;
				modelBooking.removeRow(rowBookingToDelete);
			}
		}
		
		int rowPaymentToDelete = -1;
		for (int i = modelPayment.getRowCount() - 1; i >= 0; --i)
		{
			if (modelPayment.getValueAt(i, 2).equals(customerToDelete.GetID()))
			{
				rowPaymentToDelete = i;
				modelPayment.removeRow(rowPaymentToDelete);
			}
		}
	}
}
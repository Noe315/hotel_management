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

import dataClasses.Intern;
import dataClasses.Official;
import services.ServiceEmployee;

public class GUIEmployee
{
	private ServiceEmployee serviceE = new ServiceEmployee();
	private ArrayList<Intern> internAL;
	private ArrayList<Official> officialAL;

	//Customer tab
	private JPanel panelEmployee = new JPanel();
	private JPanel panelEmployeeButtons = new JPanel();
	
	private DefaultTableModel modelEmployee = new DefaultTableModel();
	private JTable tableEmployee = new JTable(modelEmployee);
	private JScrollPane scrollEmployee = new JScrollPane(tableEmployee);
	
	private JButton buttonAddEmployee = new JButton("Add Employee");
	private JButton buttonDeleteEmployee = new JButton("Delete Employee");
	private JButton buttonMakeOfficial = new JButton("Make Official");
	private JButton buttonSetSalary = new JButton("Set Salary"); 
	
	public GUIEmployee(Connection connectionToSet)
	{
		serviceE.SetConnection(connectionToSet);
		this.internAL = serviceE.GetInternAL();
		this.officialAL = serviceE.GetOfficialAL();
	}
	
	public JPanel Draw() {
		//Set layout of buttons panel
		panelEmployeeButtons.setLayout(new BoxLayout(panelEmployeeButtons, BoxLayout.Y_AXIS));
		
		// Create columns for Customer tab
		modelEmployee.addColumn("ID Card Number");
		modelEmployee.addColumn("Name");
		modelEmployee.addColumn("Address");
		modelEmployee.addColumn("Phone Number");
		modelEmployee.addColumn("Position");
		modelEmployee.addColumn("Employee Type");
		modelEmployee.addColumn("Intern Duration (in months)");
		modelEmployee.addColumn("Official Employee Salary (in dollars)");
		
		//Prevent user from editing table cell
		tableEmployee.setDefaultEditor(Object.class, null);
		
		//Set layout of table
		scrollEmployee.setPreferredSize(new Dimension(1200,300));
		//scrollEmployee.setViewportView(tableEmployee);
		//panelEmployee.setLayout(new FlowLayout());
		tableEmployee.getColumnModel().getColumn(0).setPreferredWidth(100);
		tableEmployee.getColumnModel().getColumn(1).setPreferredWidth(80);
		tableEmployee.getColumnModel().getColumn(2).setPreferredWidth(200);
		tableEmployee.getColumnModel().getColumn(3).setPreferredWidth(100);
		tableEmployee.getColumnModel().getColumn(4).setPreferredWidth(100);
		tableEmployee.getColumnModel().getColumn(5).setPreferredWidth(100);
		tableEmployee.getColumnModel().getColumn(6).setPreferredWidth(180);
		tableEmployee.getColumnModel().getColumn(7).setPreferredWidth(200);
		
		//Iterator to traverse through array list
		Iterator<Intern> iterIntern = null;
		iterIntern = internAL.listIterator();
		Iterator<Official> iterOfficial = null;
		iterOfficial = officialAL.listIterator();
		
		while (iterIntern.hasNext())
		{
			String ID, name, address, phone, position, type, salary = "";
			int duration;
			Intern i = iterIntern.next();
			ID = i.GetID();
			name = i.GetName();
			address = i.GetAddress();
			phone = i.GetPhone();
			position = i.GetPosition();
			type = i.getClass().getSimpleName();
			duration = i.GetDuration();
			
			modelEmployee.addRow(new Object[] {ID, name, address, phone, position, type, duration, salary});
		}
		
		while (iterOfficial.hasNext())
		{
			String ID, name, address, phone, position, type, duration = "";
			int salary;
			
			Official o = iterOfficial.next();
			ID = o.GetID();
			name = o.GetName();
			address = o.GetAddress();
			phone = o.GetPhone();
			position = o.GetPosition();
			type = o.getClass().getSimpleName();
			salary = o.GetSalary();
			
			modelEmployee.addRow(new Object[] {ID, name, address, phone, position, type, duration, salary});
		}
		
		panelEmployee.add(scrollEmployee);
		panelEmployeeButtons.add(buttonAddEmployee);
		panelEmployeeButtons.add(buttonDeleteEmployee);
		panelEmployeeButtons.add(buttonSetSalary);
		panelEmployeeButtons.add(buttonMakeOfficial);
		panelEmployee.add(panelEmployeeButtons);
		
		AddBehaviorToAddButton();
		AddBehaviorToDeleteButton();
		AddBehaviorToSetSalaryButton();
		AddBehaviorToMakeOfficialButton();
		
		return panelEmployee;
	}
	
	private void AddBehaviorToAddButton()
	{
		buttonAddEmployee.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				//JTextFields for Employee input
				JTextField employeeID = new JTextField(20);
				JTextField employeeName = new JTextField(30);
				JTextField employeeAddress = new JTextField(50);
				JTextField employeePhone = new JTextField(15);
				JTextField employeePosition = new JTextField(20);
				JTextField employeeInternDuration = new JTextField(10);
				JTextField employeeOfficialSalary = new JTextField(10);
				employeeOfficialSalary.setEditable(false);
				
				String[] employeeTypeList = {"Intern", "Official"};
				JComboBox employeeTypeComboBox = new JComboBox(employeeTypeList);
				employeeTypeComboBox.setSelectedIndex(0);
				
				employeeTypeComboBox.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String employeeTypeSelected = (String) employeeTypeComboBox.getSelectedItem();
						if (employeeTypeSelected.equalsIgnoreCase("intern"))
						{
							employeeInternDuration.setEditable(true);
							employeeOfficialSalary.setEditable(false);
						}
						else
						{
							employeeOfficialSalary.setEditable(true);
							employeeInternDuration.setEditable(false);
						}
					}
				});
				
				//Panel for Customer Info input
				JPanel inputPanel = new JPanel();
				inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
				inputPanel.add(new JLabel("Employee ID:"));
				inputPanel.add(employeeID);
				inputPanel.add(new JLabel("Employee Full Name:"));
				inputPanel.add(employeeName);
				inputPanel.add(new JLabel("Employee Address:"));
				inputPanel.add(employeeAddress);
				inputPanel.add(new JLabel("Employee Phone:"));
				inputPanel.add(employeePhone);
				inputPanel.add(new JLabel("Employee Position: "));
				inputPanel.add(employeePosition);
				inputPanel.add(new JLabel("Employee Type: "));
				inputPanel.add(employeeTypeComboBox);
				inputPanel.add(new JLabel("Duration (in months): "));
				inputPanel.add(employeeInternDuration);
				inputPanel.add(new JLabel("Salary (in dollars): "));
				inputPanel.add(employeeOfficialSalary);
				
				//Show a JOptionPane for input
				int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Employee Info", JOptionPane.OK_CANCEL_OPTION);
				//When the user presses "OK" button of JOptionPane
				if (result == JOptionPane.OK_OPTION)
				{
					//Infinite loop only breaks when the user inputs valid Customer Info or "Cancel"/"Close" button is pressed
					while (true)
					{
						//Check for input validity
						boolean isEmployeePhoneValid = Pattern.matches("^[0-9]*$", employeePhone.getText());	//Phone only contains numbers
						boolean isEmployeeIDValid = Pattern.matches("^[0-9]*$", employeeID.getText());	//ID only contains numbers
						boolean isEmployeeIDNull = employeeID.getText().equals("");						//The field is not empty
						boolean isEmployeeNameNull = employeeName.getText().equals("");					//The field is not empty
						boolean isEmployeeAddressNull = employeeAddress.getText().equals("");			//The field is not empty
						boolean isEmployeePhoneNull = employeePhone.getText().equals("");				//The field is not empty
						boolean isEmployeePositionNull = employeePosition.getText().equals("");			//The field is not empty
						boolean isEmployeeInternDurationNull = employeeInternDuration.getText().equals("");
						boolean isEmployeeOfficialSalaryNull = employeeOfficialSalary.getText().equals("");
						
						String employeeTypeSelected = (String)employeeTypeComboBox.getSelectedItem().toString();
						
						if (employeeTypeSelected.equalsIgnoreCase("intern"))
						{
							if (isEmployeePhoneValid == true &&
								isEmployeeIDValid == true &&
								isEmployeeIDNull == false &&
								isEmployeeNameNull == false &&
								isEmployeeAddressNull == false &&
								isEmployeePhoneNull == false &&
								isEmployeePositionNull == false &&
								isEmployeeInternDurationNull == false)
							{
								Intern newEmployee = new Intern();
								newEmployee.SetID(employeeID.getText());
								newEmployee.SetName(employeeName.getText());
								newEmployee.SetAddress(employeeAddress.getText());
								newEmployee.SetPhone(employeePhone.getText());
								newEmployee.SetPosition(employeePosition.getText().substring(0, 1).toUpperCase() +
														employeePosition.getText().substring(1));
								newEmployee.SetDuration(Integer.parseInt(employeeInternDuration.getText()));
								
								boolean isRecordExist = serviceE.CheckInputAdd(newEmployee);
								
								if (isRecordExist == true)
								{
									JOptionPane.showMessageDialog(inputPanel, "Employee ID or Phone Number already existed.",
											  					  "Invalid Info", JOptionPane.ERROR_MESSAGE);
									int n = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Employee Info",
																		  JOptionPane.OK_CANCEL_OPTION);
									if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
									{
										break;
									}
								}
								else
								{
									serviceE.AddRecordIntern(newEmployee);
									JOptionPane.showMessageDialog(inputPanel, "Added new record.");
									UpdateAddIntern(newEmployee);
									break;
								}
							}
							else
							{
								JOptionPane.showMessageDialog(inputPanel, "Input is invalid.", "Invalid Info",
															  JOptionPane.ERROR_MESSAGE);
								int n = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Employee Info",
																	  JOptionPane.OK_CANCEL_OPTION);
								if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
								{
									break;
								}
							}
						}
						else
						{
							if (isEmployeePhoneValid == true &&
								isEmployeeIDValid == true &&
								isEmployeeIDNull == false &&
								isEmployeeNameNull == false &&
								isEmployeeAddressNull == false &&
								isEmployeePhoneNull == false &&
								isEmployeePositionNull == false &&
								isEmployeeOfficialSalaryNull == false)
							{
								Official newEmployee = new Official();
								newEmployee.SetID(employeeID.getText());
								newEmployee.SetName(employeeName.getText());
								newEmployee.SetAddress(employeeAddress.getText());
								newEmployee.SetPhone(employeePhone.getText());
								newEmployee.SetPosition(employeePosition.getText().substring(0, 1).toUpperCase() +
														employeePosition.getText().substring(1));
								newEmployee.SetSalary(Integer.parseInt(employeeOfficialSalary.getText()));
								
								boolean isRecordExist = serviceE.CheckInputAdd(newEmployee);
								
								if (isRecordExist == true)
								{
									JOptionPane.showMessageDialog(inputPanel, "Employee ID or Phone Number already existed.",
											  					  "Invalid Info", JOptionPane.ERROR_MESSAGE);
									int n = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Employee Info",
																		  JOptionPane.OK_CANCEL_OPTION);
									if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
									{
										break;
									}
								}
								else
								{
									serviceE.AddRecordOfficial(newEmployee);
									JOptionPane.showMessageDialog(inputPanel, "Added new record.");
									UpdateAddOfficial(newEmployee);
									break;
								}
							}
							else
							{
								JOptionPane.showMessageDialog(inputPanel, "Input is invalid.", "Invalid Info",
										  JOptionPane.ERROR_MESSAGE);
								int n = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Employee Info",
																	  JOptionPane.OK_CANCEL_OPTION);
								if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
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

	private void UpdateAddIntern(Intern newEmployee)
	{
		modelEmployee.addRow(new Object[] {newEmployee.GetPosition().substring(0, 3).toUpperCase() + newEmployee.GetID(),
										   newEmployee.GetName(),
										   newEmployee.GetAddress(),
										   newEmployee.GetPhone(),
										   newEmployee.GetPosition(),
										   newEmployee.getClass().getSimpleName(),
										   newEmployee.GetDuration(),
										   ""});
	}
	
	private void UpdateAddOfficial(Official newEmployee)
	{
		modelEmployee.addRow(new Object[] {newEmployee.GetPosition().substring(0, 3).toUpperCase() + newEmployee.GetID(),
										   newEmployee.GetName(),
										   newEmployee.GetAddress(),
										   newEmployee.GetPhone(),
										   newEmployee.GetPosition(),
										   newEmployee.getClass().getSimpleName(),
										   "",
										   newEmployee.GetSalary()});
	}
	
	private void AddBehaviorToDeleteButton()
	{
		buttonDeleteEmployee.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel deletePanel = new JPanel();
				deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
				
				//Dropdown list for Employee ID
				ArrayList<String> employeeIDAL = serviceE.GetEmployeeIDAL();
				JComboBox employeeIDComboBox = new JComboBox(employeeIDAL.toArray());
				
				JTextField employeeName = new JTextField();
				employeeName.setEditable(false);
				JTextField employeeAddress = new JTextField();
				employeeAddress.setEditable(false);
				JTextField employeePhone = new JTextField();
				employeePhone.setEditable(false);
				JTextField employeePosition = new JTextField();
				employeePosition.setEditable(false);
				JTextField employeeInternDuration = new JTextField();
				employeeInternDuration.setEditable(false);
				JTextField employeeOfficialSalary = new JTextField();
				employeeOfficialSalary.setEditable(false);
				
				employeeIDComboBox.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String employeeIDSelected = (String) employeeIDComboBox.getSelectedItem();
						String employeeIDTrimmed = employeeIDSelected.substring(3);
						String employeeType = serviceE.GetEmployeeType(employeeIDTrimmed);
						Intern internToDelete;
						Official officialToDelete;
						
						if (employeeType.equalsIgnoreCase("intern"))
						{
							internToDelete = new Intern();
							internToDelete.SetID(employeeIDTrimmed);
							
							Intern info = serviceE.GetInfoIntern(internToDelete);
							employeeName.setText(info.GetName());
							employeeAddress.setText(info.GetAddress());
							employeePhone.setText(info.GetPhone());
							employeePosition.setText(info.GetPosition());
							employeeInternDuration.setText(Integer.toString(info.GetDuration()));
							employeeOfficialSalary.setText("");
						}
						else
						{
							officialToDelete = new Official();
							officialToDelete.SetID(employeeIDTrimmed);
							
							Official info = serviceE.GetInfoOfficial(officialToDelete);
							employeeName.setText(info.GetName());
							employeeAddress.setText(info.GetAddress());
							employeePhone.setText(info.GetPhone());
							employeePosition.setText(info.GetPosition());
							employeeOfficialSalary.setText(Integer.toString(info.GetSalary()));
							employeeInternDuration.setText("");
						}
					}
				});
				
				deletePanel.add(new JLabel("Employee ID:"));
				deletePanel.add(employeeIDComboBox);
				deletePanel.add(new JLabel("Employee Name:"));
				deletePanel.add(employeeName);
				deletePanel.add(new JLabel("Employee Address:"));
				deletePanel.add(employeeAddress);
				deletePanel.add(new JLabel("Employee Phone:"));
				deletePanel.add(employeePhone);
				deletePanel.add(new JLabel("Employee Position:"));
				deletePanel.add(employeePosition);
				deletePanel.add(new JLabel("Intern duration (in months):"));
				deletePanel.add(employeeInternDuration);
				deletePanel.add(new JLabel("Official Employee Salary (in dollars):"));
				deletePanel.add(employeeOfficialSalary);
				
				int result = JOptionPane.showConfirmDialog(null, deletePanel, "Select Employee To Delete", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{
					while (true)
					{
						int n = JOptionPane.showConfirmDialog(null, "Do you want to delete this employee from the database?",
															  "Delete Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
						if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
						{
							break;
						}
						else
						{
							String employeeIDSelected = (String)employeeIDComboBox.getSelectedItem().toString();
							String employeeIDTrimmed = employeeIDSelected.substring(3);
							String employeeNameSelected = employeeName.getText();
							
							boolean isEmployeeNameSelectedNull = employeeNameSelected.equals("");
							
							if (isEmployeeNameSelectedNull == false)
							{
								serviceE.DeleteRecord(employeeIDTrimmed);
								JOptionPane.showMessageDialog(deletePanel, "Deleted record.");
								UpdateDelete(employeeIDSelected);
								break;
							}
							else
							{
								JOptionPane.showMessageDialog(deletePanel, "Employee Name must not be empty.", "Invalid Info", JOptionPane.ERROR_MESSAGE);
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

	private void UpdateDelete(String employeeIDToDelete)
	{
		int rowToDelete = -1;
		for (int i = modelEmployee.getRowCount() - 1; i >= 0; --i) {
            if (modelEmployee.getValueAt(i, 0).equals(employeeIDToDelete)) {
                rowToDelete = i;
                break;
            }
	    }
		modelEmployee.removeRow(rowToDelete);
	}
	
	private void AddBehaviorToSetSalaryButton()
	{
		buttonSetSalary.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel salaryPanel = new JPanel();
				salaryPanel.setLayout(new BoxLayout(salaryPanel, BoxLayout.Y_AXIS));
				
				ArrayList<String> officialIDAL = serviceE.GetOfficialIDAL();
				JComboBox officialIDComboBox = new JComboBox(officialIDAL.toArray());
				
				JTextField officialName = new JTextField();
				officialName.setEditable(false);
				JTextField officialAddress = new JTextField();
				officialAddress.setEditable(false);
				JTextField officialPhone = new JTextField();
				officialPhone.setEditable(false);
				JTextField officialPosition = new JTextField();
				officialPosition.setEditable(false);
				JTextField officialSalary = new JTextField();
				officialSalary.setEditable(false);
				JTextField officialNewSalary = new JTextField();
				
				officialIDComboBox.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String officialIDSelected = (String) officialIDComboBox.getSelectedItem();
						String officialIDTrimmed = officialIDSelected.substring(3);
						
						Official officialToSetSalary;
						
						officialToSetSalary = new Official();
						officialToSetSalary.SetID(officialIDTrimmed);
						
						Official info = serviceE.GetInfoOfficial(officialToSetSalary);
						officialName.setText(info.GetName());
						officialAddress.setText(info.GetAddress());
						officialPhone.setText(info.GetPhone());
						officialPosition.setText(info.GetPosition());
						officialSalary.setText(Integer.toString(info.GetSalary()));
					}
				});
				
				salaryPanel.add(new JLabel("Employee ID:"));
				salaryPanel.add(officialIDComboBox);
				salaryPanel.add(new JLabel("Employee Name:"));
				salaryPanel.add(officialName);
				salaryPanel.add(new JLabel("Employee Address:"));
				salaryPanel.add(officialAddress);
				salaryPanel.add(new JLabel("Employee Phone:"));
				salaryPanel.add(officialPhone);
				salaryPanel.add(new JLabel("Employee Position:"));
				salaryPanel.add(officialPosition);
				salaryPanel.add(new JLabel("Current Salary (in dollars):"));
				salaryPanel.add(officialSalary);
				salaryPanel.add(new JLabel("New salary (in dollars):"));
				salaryPanel.add(officialNewSalary);
				
				int result = JOptionPane.showConfirmDialog(null, salaryPanel, "Update Salary", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{
					while (true)
					{
						int n = JOptionPane.showConfirmDialog(null, "Do you want to update this employee's salary?",
															  "Change Salary", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
						if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
						{
							break;
						}
						else
						{
							String officialIDSelected = (String) officialIDComboBox.getSelectedItem();
							String officialIDTrimmed = officialIDSelected.substring(3);
							
							Official officialToChangeSalary = new Official();
							officialToChangeSalary.SetID(officialIDTrimmed);
							officialToChangeSalary.SetPosition(officialPosition.getText());
							
							String officialNameSelected = officialName.getText();
							int officialNewSalaryInt = 0;
							
							boolean isOfficialNameSelectedNull = officialNameSelected.equals("");
							boolean isOfficialNewSalaryNull = officialNewSalary.equals("");
							
							boolean isOfficialNewSalaryValid = false;
							try
							{
								officialNewSalaryInt = Integer.parseInt(officialNewSalary.getText());
								if (officialNewSalaryInt > 0)
								{
									isOfficialNewSalaryValid = true;
								}
								else
								{
									isOfficialNewSalaryValid = false;
								}
							}
							catch (NumberFormatException nfe)
							{
								nfe.printStackTrace();
							}
							
							if (isOfficialNewSalaryValid == true &&
								isOfficialNewSalaryNull == false &&
								isOfficialNameSelectedNull == false)
							{
								serviceE.ChangeSalary(officialToChangeSalary, officialNewSalaryInt);
								JOptionPane.showMessageDialog(salaryPanel, "Updated new salary.");
								UpdateSalary(officialToChangeSalary, officialNewSalaryInt);
								break;
							}
							else
							{
								JOptionPane.showMessageDialog(salaryPanel, "Input is invalid.", "Invalid Info", JOptionPane.ERROR_MESSAGE);
								int n2 = JOptionPane.showConfirmDialog(null, salaryPanel, "Update Salary", JOptionPane.OK_CANCEL_OPTION);
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
	
	private void UpdateSalary(Official officialToChangeSalary, int amount)
	{
		String id = officialToChangeSalary.GetPosition().substring(0, 3).toUpperCase() + officialToChangeSalary.GetID();
		int rowToUpdate = -1;
		for (int i = modelEmployee.getRowCount() - 1; i >= 0; --i) {
            if (modelEmployee.getValueAt(i, 0).equals(id))
            {
                rowToUpdate = i;
                break;
            }
	    }
		modelEmployee.setValueAt(amount, rowToUpdate, 7);
	}
	
	private void AddBehaviorToMakeOfficialButton()
	{
		buttonMakeOfficial.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel internPanel = new JPanel();
				internPanel.setLayout(new BoxLayout(internPanel, BoxLayout.Y_AXIS));
				
				ArrayList<String> internIDAL = serviceE.GetInternIDAL();
				JComboBox internIDComboBox = new JComboBox(internIDAL.toArray());
				
				JTextField internName = new JTextField();
				internName.setEditable(false);
				JTextField internAddress = new JTextField();
				internAddress.setEditable(false);
				JTextField internPhone = new JTextField();
				internPhone.setEditable(false);
				JTextField internPosition = new JTextField();
				internPosition.setEditable(false);
				JTextField internDuration = new JTextField();
				internDuration.setEditable(false);
				JTextField internSalary = new JTextField();
				
				internIDComboBox.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String internIDSelected = (String) internIDComboBox.getSelectedItem();
						String internIDTrimmed = internIDSelected.substring(3);
						
						Intern internToMakeOfficial = new Intern();
						internToMakeOfficial.SetID(internIDTrimmed);
						
						Intern info = serviceE.GetInfoIntern(internToMakeOfficial);
						internName.setText(info.GetName());
						internAddress.setText(info.GetAddress());
						internPhone.setText(info.GetPhone());
						internPosition.setText(info.GetPosition());
						internDuration.setText(Integer.toString(info.GetDuration()));
					}
				});
				
				internPanel.add(new JLabel("Employee ID:"));
				internPanel.add(internIDComboBox);
				internPanel.add(new JLabel("Employee Name:"));
				internPanel.add(internName);
				internPanel.add(new JLabel("Employee Address:"));
				internPanel.add(internAddress);
				internPanel.add(new JLabel("Employee Phone:"));
				internPanel.add(internPhone);
				internPanel.add(new JLabel("Employee Position:"));
				internPanel.add(internPosition);
				internPanel.add(new JLabel("Duration (in months):"));
				internPanel.add(internDuration);
				internPanel.add(new JLabel("New Official Salary:"));
				internPanel.add(internSalary);
				
				int result = JOptionPane.showConfirmDialog(null, internPanel, "Make Intern Official", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{
					while (true)
					{
						int n = JOptionPane.showConfirmDialog(null, "Do you want to make this employee official?",
															  "Make Official", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
						if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
						{
							break;
						}
						else
						{
							String internIDSelected = (String) internIDComboBox.getSelectedItem();
							String internIDTrimmed = internIDSelected.substring(3);
							
							Intern internToPromote = new Intern();
							internToPromote.SetID(internIDTrimmed);
							internToPromote.SetPosition(internPosition.getText());
							
							String internNameSelected = internName.getText();
							int internSalaryInt = 0;
							
							boolean isInternNameSelectedNull = internNameSelected.equals("");
							boolean isInternSalaryNull = internSalary.equals("");
							
							boolean isInternSalaryValid = false;
							try
							{
								internSalaryInt = Integer.parseInt(internSalary.getText());
								if (internSalaryInt > 0)
								{
									isInternSalaryValid = true;
								}
								else
								{
									isInternSalaryValid = false;
								}
							}
							catch (NumberFormatException nfe)
							{
								nfe.printStackTrace();
							}
							
							if (isInternSalaryValid == true &&
								isInternSalaryNull == false &&
								isInternNameSelectedNull == false)
							{
								internToPromote.MakeOfficial(internSalaryInt, serviceE);
								JOptionPane.showMessageDialog(internPanel, "Intern has been promoted.");
								UpdateIntern(internToPromote, internSalaryInt);
								break;
							}
							else
							{
								JOptionPane.showMessageDialog(internPanel, "Input is invalid.", "Invalid Info", JOptionPane.ERROR_MESSAGE);
								int n2 = JOptionPane.showConfirmDialog(null, internPanel, "Update Salary", JOptionPane.OK_CANCEL_OPTION);
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
	
	private void UpdateIntern(Intern internToPromote, int salary)
	{
		String id = internToPromote.GetPosition().substring(0, 3).toUpperCase() + internToPromote.GetID();
		int rowToUpdate = -1;
		for (int i = modelEmployee.getRowCount() - 1; i >= 0; --i) {
            if (modelEmployee.getValueAt(i, 0).equals(id))
            {
                rowToUpdate = i;
                break;
            }
	    }
		modelEmployee.setValueAt(salary, rowToUpdate, 7);
		modelEmployee.setValueAt("", rowToUpdate, 6);
		modelEmployee.setValueAt("Official", rowToUpdate, 5);
	}
}
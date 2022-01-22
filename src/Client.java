import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import db.DBConnection;
import gui.GUIBooking;
import gui.GUICustomer;
import gui.GUIEmployee;
import gui.GUIPayment;
import gui.GUIRoom;

public class Client
{
	public static void main(String[] args) throws SQLException
	{
		DBConnection dbConnection = new DBConnection();
		Connection connection = dbConnection.GetConnection();
		
		GUICustomer guiC = new GUICustomer(connection);
		GUIBooking guiB = new GUIBooking(connection);
		GUIPayment guiP = new GUIPayment(connection);
		GUIRoom guiR = new GUIRoom(connection);
		GUIEmployee guiE = new GUIEmployee(connection);
		
		/*guiC.SetConnection(connection);
		guiB.SetConnection(connection);
		guiP.SetConnection(connection);
		guiR.SetConnection(connection);
		guiE.SetConnection(connection);*/
		
		guiB.SetModelPayment(guiP.GetModelPayment());
		guiP.SetModelBooking(guiB.GetModelBooking());
		
		guiR.SetModelBooking(guiB.GetModelBooking());
		guiR.SetModelPayment(guiP.GetModelPayment());
		
		guiC.SetModelBooking(guiB.GetModelBooking());
		guiC.SetModelPayment(guiP.GetModelPayment());
		
		JFrame mainFrame = new JFrame("Hotel Management");
		JTabbedPane tabs = new JTabbedPane();
		
		tabs.add("Customer Info", guiC.Draw());
		tabs.add("Booking", guiB.Draw());
		tabs.add("Payment", guiP.Draw());
		tabs.add("Room", guiR.Draw());
		tabs.add("Employee", guiE.Draw());
		
		mainFrame.add(tabs);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setVisible(true);
		
		mainFrame.addWindowListener(new WindowAdapter()
		{
		    @Override
		    public void windowClosing(WindowEvent e)
		    {
		        super.windowClosing(e);
		        try
		        {
		        	dbConnection.CloseConnection();
		        }
		        catch (SQLException ee)
		        {
		        	ee.printStackTrace();
		        }
		    }
		});
	}
}
package LGoodDatePicker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;

public class VetoPolicyCheckOut implements DateVetoPolicy {

	public String dateCheckInStr, dateCheckOutStr;
	public VetoPolicyCheckOut(String cstrDatetimeCheckInStr, String cstrDatetimeCheckOutStr)
	{
		this.dateCheckInStr = cstrDatetimeCheckInStr;
		this.dateCheckOutStr = cstrDatetimeCheckOutStr;
	}
	
	@Override
	public boolean isDateAllowed(LocalDate date)
	{
		String pattern = "yyyy-MM-dd";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
		LocalDate dateCheckIn = LocalDate.parse(dateCheckInStr, dtf);
		
		if (date.isAfter(dateCheckIn))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}

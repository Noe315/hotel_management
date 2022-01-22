package LGoodDatePicker;
import java.time.LocalDate;

import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;

public class VetoPolicyCheckIn implements DateVetoPolicy {

	public String dateCheckInStr, dateCheckOutStr;
	public VetoPolicyCheckIn(String cstrDatetimeCheckInStr, String cstrDatetimeCheckOutStr)
	{
		this.dateCheckInStr = cstrDatetimeCheckInStr;
		this.dateCheckOutStr = cstrDatetimeCheckOutStr;
	}
	
	@Override
	public boolean isDateAllowed(LocalDate date)
	{
		LocalDate ld = LocalDate.now();
		
		if (date.isAfter(ld) || date.isEqual(ld))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}

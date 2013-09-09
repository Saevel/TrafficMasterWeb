package trafficmaster.helpers;

import java.util.Date;

public class DateUtils {

	private DateUtils() {
		;
	}
	
	public static final int getTimeDifferenceInMinutes(Date subtractor, Date subtracted) {
		
		if(subtractor == null || subtracted == null) {
			throw new IllegalArgumentException("DateUtils|getTimeDifferenceInMinutes|" +
					"neither the subtactor nor the subtracted can be null!");
		}
		
		return ( subtractor.getMinutes() + 60*subtractor.getHours() 
				- subtracted.getMinutes() - 60*subtracted.getHours());
	}
	
	
	public static final void addDelay(int hours, int minutes, Date target) {
		hours += target.getHours();
		minutes += target.getMinutes();
		
		while(minutes>=60) {
			hours += 1;
			minutes -= 60;
		}
		
		target.setHours(hours);
		target.setMinutes(minutes);
	}
	
	public static final double timeToMinutes(Date date) {
		return (date.getMinutes() + 60*date.getHours());
	}
}

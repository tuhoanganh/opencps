package org.opencps.holidayconfig.util;

import java.util.Calendar;
import java.util.Date;

public class HolidayCheckUtils{
	
	/**
	* checkActionDateOver *
	* @param startDate : ngày bắt đầu
	* @param endDate : ngày kết thúc
	* @param daysDuration : số ngày xử lý cho phép
	* @param return : số ngày xử lý quá hạn
	*/
	
	public static int checkActionDateOver(Date startDate,Date endDate,int daysDuration){
		
		int dateOverNumbers = 0;
		
		if(daysDuration >0){
			
			Calendar endDayCal = Calendar.getInstance();
			endDayCal.setTime(endDate);
			
			Calendar endDateMax = HolidayUtils.getEndDate(startDate, daysDuration);
			
			int endDay = endDayCal.get(Calendar.DATE);
			int endDayMax = endDateMax.get(Calendar.DATE);
			
			dateOverNumbers = endDayMax - endDay;
			
			if(dateOverNumbers >0){
				return 0;
			}else{
				
				return Math.abs(dateOverNumbers);
			}
			
		}
		
		return dateOverNumbers;
		
	}
	
	
}
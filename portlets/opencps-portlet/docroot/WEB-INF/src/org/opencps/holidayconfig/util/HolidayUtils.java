package org.opencps.holidayconfig.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;










import org.opencps.holidayconfig.model.HolidayConfig;
import org.opencps.holidayconfig.service.HolidayConfigLocalServiceUtil;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

public class HolidayUtils {
	
	private static Log _log = LogFactoryUtil.getLog(HolidayUtils.class);
	
	public static Calendar getEndDate(Date baseDate,long daysDuration){
		
		if(baseDate == null){
	        baseDate = new Date();
	    }
		
		Calendar baseDateCal = Calendar.getInstance();
	    baseDateCal.setTime(baseDate);
		
		
		try{
		
			for(int i=0;i<daysDuration;i++){
				
				baseDateCal.add(Calendar.DATE,1);
				
				baseDateCal = checkDay(baseDateCal,baseDate,null);
				
			}
		}catch(Exception e){
			_log.error(e);
		}
		
		return baseDateCal;
	}
	
	private static Calendar checkDay (Calendar baseDateCal,Date baseDate,List<HolidayConfig> holidayConfigList){
		
		
		boolean isHoliday = false;
		
		try{
		
			if(Validator.isNull(holidayConfigList) || (holidayConfigList.size() <= 0)){
				holidayConfigList = HolidayConfigLocalServiceUtil.getHolidayConfig(1);
			}
			
			isHoliday = isHoliday(baseDateCal,holidayConfigList);
	    	
			
			if(baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY 
					|| baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || isHoliday)
			{
	    	
		    	baseDateCal = isHolidayCal(baseDateCal,holidayConfigList);
		    	
		    	baseDateCal = checkSaturday(baseDateCal);
		    	
		    	baseDateCal = checkSunday(baseDateCal);
		    	
		    	checkDay(baseDateCal,baseDate,holidayConfigList);
			}else{
				
			}
		}catch (Exception e){
			_log.error(e);
		}
		
		return baseDateCal;
	}
	
	private static Calendar checkSaturday (Calendar baseDateCal){
		
		if(baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
    		baseDateCal.add(Calendar.DATE,2);
        }
		return baseDateCal;
	}
	
	private static Calendar checkSunday (Calendar baseDateCal){
		
		if(baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
    		baseDateCal.add(Calendar.DATE,1);		           
        }
		return baseDateCal;
	}
	
	
	
	private static Calendar isHolidayCal(Calendar baseDateCal,List<HolidayConfig> holidayConfigList){
		
       
       int baseDate = 0;
       int holidayDate = 0;
       Calendar holidayCal = Calendar.getInstance();
        
       try {
    	   
    	   if(Validator.isNull(holidayConfigList) || (holidayConfigList.size() <=0)){
   			
				holidayConfigList = HolidayConfigLocalServiceUtil.getHolidayConfig(1);
			}
			
			for(int i =0;i<holidayConfigList.size();i++){
				
				
				holidayCal.setTime(holidayConfigList.get(i).getHoliday());
	    		
				baseDate = baseDateCal.get(Calendar.DATE);
				holidayDate = holidayCal.get(Calendar.DATE);

	    		if(baseDate == holidayDate){
	    			baseDateCal.add(Calendar.DATE,1);
	    		}
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			_log.info(e);
		}
		return baseDateCal;
	}
	
	
	
	private static boolean isHoliday(Calendar baseDateCal,List<HolidayConfig> holidayConfigList){
		
		
		int baseDate = 0;
		int holidayDate = 0;
		Calendar holidayCal = Calendar.getInstance();

		try{
			
			if(Validator.isNull(holidayConfigList) || (holidayConfigList.size() <=0)){
			
				holidayConfigList = HolidayConfigLocalServiceUtil.getHolidayConfig(1);
			}
			
			for(int i =0;i<holidayConfigList.size();i++){
				
				holidayCal.setTime(holidayConfigList.get(i).getHoliday());
				
				baseDate = baseDateCal.get(Calendar.DATE);
				holidayDate = holidayCal.get(Calendar.DATE);
				
	    		if(baseDate == holidayDate){
	    			return true;
	    		}
	    	}
		}catch(Exception e){
			_log.error(e);
		}
		
		return false;
		
	}
}
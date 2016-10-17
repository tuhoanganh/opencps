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
	
	public static Date getNgayHenTraHoSo(Date baseDate, long numberOfDays) {
		
		if(baseDate == null){
	        baseDate = new Date();
	    }
	    
	    Calendar baseDateCal = Calendar.getInstance();
	    baseDateCal.setTime(baseDate);
	    
	    HolidayConfig holiday = null;
	        
	    try{
		    
		    	
		    	for(int i=0;i<numberOfDays;i++){			    	
		    		
			    	baseDateCal.add(Calendar.DATE,1);
			    	
		    		//holiday = checkHoliday1(baseDateCal);
		    		
		    		if(baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||Validator.isNotNull(holiday)){
		    			//baseDateCal = checkDay(baseDateCal);
		    			
		    		}		    	
			    }    	
		      	
	    }catch(Exception e){
	    	_log.error(e);
	    }
	    
	    return baseDateCal.getTime();
 
	}
	
	public static Calendar getEndDate(Date baseDate,long daysDuration){
		
		if(baseDate == null){
	        baseDate = new Date();
	    }
		
		Calendar baseDateCal = Calendar.getInstance();
	    baseDateCal.setTime(baseDate);
		
		boolean isHoliday = false;
		
		try{
		
			for(int i=0;i<daysDuration;i++){
				baseDateCal.add(Calendar.DATE,1);
				
				isHoliday = isHoliday(baseDateCal);
				
				if(baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY 
						|| baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY 
						||isHoliday){
	    			baseDateCal = checkDay(baseDateCal,baseDate);
	    			
	    		}
				
			}
		}catch(Exception e){
			_log.error(e);
		}
		
		return baseDateCal;
	}
	
	private static Calendar checkDay (Calendar baseDateCal,Date baseDate){
		
		
		boolean isHoliday = false;
		isHoliday = isHoliday(baseDateCal);
    	
		
		if(baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || isHoliday)
		{
    	
	    	baseDateCal = isHolidayCal(baseDateCal);
	    	
	    	baseDateCal = checkSaturday(baseDateCal);
	    	
	    	baseDateCal = checkSunday(baseDateCal);
	    	
	    	checkDay(baseDateCal,baseDate);
		}else{
			
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
	
	
	
	private static Calendar isHolidayCal(Calendar baseDateCal){
		
       
       int baseDate = 0;
       int holidayDate = 0;
       Calendar holidayCal = Calendar.getInstance();
        
       try {
    	   
    	   List<HolidayConfig> holidayConfigList = HolidayConfigLocalServiceUtil.getHolidayConfig(1);
			
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
	
	
	
	private static boolean isHoliday(Calendar baseDateCal){
		
		
		int baseDate = 0;
		int holidayDate = 0;
		Calendar holidayCal = Calendar.getInstance();

		try{
			
			List<HolidayConfig> holidayConfigList = HolidayConfigLocalServiceUtil.getHolidayConfig(1);
			
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
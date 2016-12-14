<%@page import="org.opencps.statisticsmgt.util.StatisticsUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.statisticsmgt.util.StatisticsUtil.StatisticsFieldNumber"%>
<%@page import="org.opencps.statisticsmgt.bean.DossierStatisticsBean"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil"%>
<%@ include file="../init.jsp" %>

This is the <b>Statistics</b> portlet in View mode.

<%
	int initYear = 2016;

	List total = new ArrayList<Object>();
	
	for(int m = 1; m <= 12; m++){
		
		List receiveds1 = DossiersStatisticsLocalServiceUtil.generalStatistics(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.ReceivedNumber.toString(), -1);
		List ontimes1 = DossiersStatisticsLocalServiceUtil.generalStatistics(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.OntimeNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_ONTIME);
		List overtimes1 = DossiersStatisticsLocalServiceUtil.generalStatistics(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.OvertimeNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_LATE);
		List processings1 = DossiersStatisticsLocalServiceUtil.generalStatistics(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.ProcessingNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_UNEXPIRED);
		List delayings1 = DossiersStatisticsLocalServiceUtil.generalStatistics(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.DelayingNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_EXPIRED);
		
		if(receiveds1 != null){
			total.addAll(receiveds1);
		}
		
		if(ontimes1 != null){
			total.addAll(ontimes1);
		}
		
		if(overtimes1 != null){
			total.addAll(overtimes1);
		}
		
		if(processings1 != null){
			total.addAll(processings1);
		}
		
		if(delayings1 != null){
			total.addAll(delayings1);
		}
		
		
		List receiveds2 = DossiersStatisticsLocalServiceUtil.statisticsByDomain(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.ReceivedNumber.toString(), -1);
		List ontimes2 = DossiersStatisticsLocalServiceUtil.statisticsByDomain(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.OntimeNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_ONTIME);
		List overtimes2= DossiersStatisticsLocalServiceUtil.statisticsByDomain(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.OvertimeNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_LATE);
		List processings2 = DossiersStatisticsLocalServiceUtil.statisticsByDomain(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.ProcessingNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_UNEXPIRED);
		List delayings2 = DossiersStatisticsLocalServiceUtil.statisticsByDomain(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.DelayingNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_EXPIRED);
		
		if(receiveds2 != null){
			total.addAll(receiveds2);
		}
		
		if(ontimes2 != null){
			total.addAll(ontimes2);
		}
		
		if(overtimes2 != null){
			total.addAll(overtimes2);
		}
		
		if(processings2 != null){
			total.addAll(processings2);
		}
		
		if(delayings2 != null){
			total.addAll(delayings2);
		}
		
		
		List receiveds3 = DossiersStatisticsLocalServiceUtil.statisticsByGovAgency(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.ReceivedNumber.toString(), -1);
		List ontimes3 = DossiersStatisticsLocalServiceUtil.statisticsByGovAgency(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.OntimeNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_ONTIME);
		List overtimes3= DossiersStatisticsLocalServiceUtil.statisticsByGovAgency(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.OvertimeNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_LATE);
		List processings3 = DossiersStatisticsLocalServiceUtil.statisticsByGovAgency(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.ProcessingNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_UNEXPIRED);
		List delayings3 = DossiersStatisticsLocalServiceUtil.statisticsByGovAgency(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.DelayingNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_EXPIRED);
		
		if(receiveds3 != null){
			total.addAll(receiveds3);
		}
		
		if(ontimes3 != null){
			total.addAll(ontimes3);
		}
		
		if(overtimes3 != null){
			total.addAll(overtimes3);
		}
		
		if(processings3 != null){
			total.addAll(processings3);
		}
		
		if(delayings3 != null){
			total.addAll(delayings3);
		}
		
	}
	
	//StatisticsUtil.getDossiersStatistics(total);
%>

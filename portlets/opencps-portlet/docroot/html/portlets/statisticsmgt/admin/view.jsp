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
		List receiveds = DossiersStatisticsLocalServiceUtil.statisticsByDomain(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.ReceivedNumber.toString(), -1);
		List ontimes = DossiersStatisticsLocalServiceUtil.statisticsByDomain(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.OntimeNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_ONTIME);
		List overtimes = DossiersStatisticsLocalServiceUtil.statisticsByDomain(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.OvertimeNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_LATE);
		List processings = DossiersStatisticsLocalServiceUtil.statisticsByDomain(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.ProcessingNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_UNEXPIRED);
		List delayings = DossiersStatisticsLocalServiceUtil.statisticsByDomain(scopeGroupId, m, initYear, 
				StatisticsFieldNumber.DelayingNumber.toString(), PortletConstants.DOSSIER_DELAY_STATUS_EXPIRED);
		
		if(receiveds != null){
			total.addAll(receiveds);
		}
		
		if(ontimes != null){
			total.addAll(ontimes);
		}
		
		if(overtimes != null){
			total.addAll(overtimes);
		}
		
		if(processings != null){
			total.addAll(processings);
		}
		
		if(delayings != null){
			total.addAll(delayings);
		}
		
	}
	
	StatisticsUtil.getDossiersStatistics(total);
%>

<%@page import="org.opencps.statisticsmgt.bean.DossierStatisticsBean"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil"%>
<%@ include file="../init.jsp" %>

This is the <b>Statistics</b> portlet in View mode.

<%
	long count =  DossiersStatisticsLocalServiceUtil.countReceivedByYear(scopeGroupId, 2016);
	System.out.println(count);
	List statistics = DossiersStatisticsLocalServiceUtil.statisticsReceivedByYear(scopeGroupId, 2016);
	if(statistics != null){
		for(Object obj : statistics){
			DossierStatisticsBean dossierStatisticsBean = (DossierStatisticsBean)obj;
			System.out.println(dossierStatisticsBean.getProcessOrderId());
		}
	}
%>

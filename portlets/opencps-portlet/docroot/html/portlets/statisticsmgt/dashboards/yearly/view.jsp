<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil"%>
<%@page import="org.opencps.statisticsmgt.model.DossiersStatistics"%>
<%@ include file="../../init.jsp" %>

<%
	List<DossiersStatistics> dossiersStatistics = new ArrayList<DossiersStatistics>();
	try {
		for(int i = 1; i <= 12; i++){
			DossiersStatistics statistics = DossiersStatisticsLocalServiceUtil
					.getDossiersStatisticsByGC_DC_M_Y_L(StringPool.BLANK,
							StringPool.BLANK, i, currentYear, 0);
			dossiersStatistics.add(statistics);
		}
	} catch (Exception e) {

	}
%>

<c:choose>
	<c:when test="<%=dossiersStatistics != null && !dossiersStatistics.isEmpty() %>">
		<%
			int remainingNumber = 0;
			int receivedNumber = 0;
			int ontimeNumber = 0;
			int overtimeNumber = 0;
			int processingNumber = 0;
			int delayingNumber = 0;
			
			for(DossiersStatistics statistics : dossiersStatistics){
				remainingNumber += statistics.getReceivedNumber();
				ontimeNumber += statistics.getOntimeNumber();
				overtimeNumber += statistics.getOvertimeNumber();
			}
			
			processingNumber += dossiersStatistics.get(dossiersStatistics.size() - 1).getProcessingNumber();
			delayingNumber += dossiersStatistics.get(dossiersStatistics.size() - 1).getDelayingNumber();
		%>
		<div class="widget-wrapper">
			<div class="widget-header">
				<span class="span8">
					<liferay-ui:message key="stats-in-year"/>
				</span>
				<span class="span4"><%=currentYear %></span>
			</div>
			
			<div class="widget-content">
				<span class="span8">
					<liferay-ui:message key="remaining-number"/>
				</span>
				<span class="span4"><%=remainingNumber %></span>
			</div>
			
			<div class="widget-content">
				<span class="span8">
					<liferay-ui:message key="received-number"/>
				</span>
				<span class="span4"><%=receivedNumber %></span>
			</div>
			
			<div class="widget-content">
				<span class="span8">
					<liferay-ui:message key="ontime-number"/>
				</span>
				<span class="span4"><%=ontimeNumber %></span>
			</div>
			
			<div class="widget-content">
				<span class="span8">
					<liferay-ui:message key="overtime-number"/>
				</span>
				<span class="span4"><%=overtimeNumber %></span>
			</div>
			
			<div class="widget-content">
				<span class="span8">
					<liferay-ui:message key="processing-number"/>
				</span>
				<span class="span4"><%=processingNumber %></span>
			</div>
			
			<div class="widget-content">
				<span class="span8">
					<liferay-ui:message key="delaying-number"/>
				</span>
				<span class="span4"><%=delayingNumber %></span>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-alert"><liferay-ui:message key="not-found-stats"/></div>
	</c:otherwise>
</c:choose>
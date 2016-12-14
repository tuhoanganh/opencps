<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil"%>
<%@page import="org.opencps.statisticsmgt.model.DossiersStatistics"%>
<%@ include file="../../init.jsp" %>

<%
	List<DossiersStatistics> dossiersStatistics = new ArrayList<DossiersStatistics>();
	try {
		dossiersStatistics = DossiersStatisticsLocalServiceUtil
				.getDossiersStatisticsByGC_DC_Y(StringPool.BETWEEN,
						StringPool.BLANK, currentYear);
	} catch (Exception e) {

	}
%>

<c:choose>
	<c:when test="<%=dossiersStatistics != null && !dossiersStatistics.isEmpty() %>">
		
	</c:when>
	<c:otherwise>
		<div class=""></div>
	</c:otherwise>
</c:choose>
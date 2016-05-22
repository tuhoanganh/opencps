/**
* OpenCPS is the open source Core Public Services software
* Copyright (C) 2016-present OpenCPS community

* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
* You should have received a copy of the GNU Affero General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
*/
package org.opencps.jasperreport.util;

import java.util.Map;

import org.opencps.jasperreport.compile.JRReportTemplate;
import org.opencps.jasperreport.datasource.JRJSONDataSource;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * @author trungnt
 */
public class JRReportUtil {

	public synchronized static String createReportPDFfFile(
	    String jrxmlTemplate, String jsonData, Map<String, Object> parameters,
	    String outputDestination, String exportName) {

		String sourceFileName = outputDestination + exportName;
		try {
			JasperReport reportTemplate = JRReportTemplate
			    .getJasperReport(jrxmlTemplate);
			JRJSONDataSource dataSource = JRJSONDataSource
			    .getInstance(jsonData);
			JasperPrint jasperPrint =
			    getJasperPrint(reportTemplate, parameters, dataSource);

			return exportPdfFile(jasperPrint, sourceFileName);
		}
		catch (Exception e) {
			_log
			    .error(e);

			return StringPool.BLANK;

		}

	}

	protected static JasperPrint getJasperPrint(
	    JasperReport jrReportTemplate, Map<String, Object> parameters,
	    JRJSONDataSource dataSource)
	    throws JRException {

		JasperPrint jasperPrint = JasperFillManager
		    .fillReport(jrReportTemplate, null, dataSource);

		return jasperPrint;
	}

	protected static JasperPrint getJasperPrint(
	    JRReportTemplate jrReportTemplate, Map<String, Object> parameters,
	    JRJSONDataSource dataSource)
	    throws JRException {

		JasperPrint jasperPrint = JasperFillManager
		    .fillReport(jrReportTemplate, null, dataSource);

		return jasperPrint;
	}

	protected static String exportPdfFile(
	    JasperPrint jasperPrint, String sourceFileName)
	    throws JRException {

		JasperExportManager
		    .exportReportToPdfFile(jasperPrint, sourceFileName);

		return sourceFileName;

	}

	private static Log _log = LogFactoryUtil
	    .getLog(JRReportUtil.class
	        .getName());
}

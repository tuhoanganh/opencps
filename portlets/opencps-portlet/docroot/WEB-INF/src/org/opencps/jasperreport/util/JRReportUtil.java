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

/**
 * @author trungnt
 *
 */
public class JRReportUtil {

	public static JasperPrint getJasperPrint(
	    JRReportTemplate jrReportTemplate, Map<String, Object> parameters,
	    JRJSONDataSource dataSource) {

		JasperPrint jasperPrint = null;
		try {
			jasperPrint = JasperFillManager
			    .fillReport(jrReportTemplate, null, dataSource);

		}
		catch (Exception e) {
			_log
			    .error(e);
		}
		return jasperPrint;
	}
	
	public static String exportReportToPdfFile(
	    JasperPrint jasperPrint, String outputDestination, String exportName) {

		try {
			JasperExportManager
			    .exportReportToPdfFile(
			        jasperPrint, outputDestination + exportName);
			return outputDestination + exportName;
		}
		catch (JRException e) {
			_log
			    .error(e);
			return StringPool.BLANK;
		}
	}

	private static Log _log = LogFactoryUtil
	    .getLog(JRReportUtil.class
	        .getName());
}


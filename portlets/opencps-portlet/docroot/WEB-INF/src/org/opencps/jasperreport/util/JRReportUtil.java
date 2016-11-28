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

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

import org.opencps.jasperreport.compile.JRReportTemplate;
import org.opencps.jasperreport.datasource.JRJSONDataSource;
import org.opencps.util.JsonUtils;
import org.opencps.util.PortletConstants.DestinationRoot;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author trungnt
 */
public class JRReportUtil {

	public static enum DocType {
		DOC(".doc"), DOCX(".docx"), HTML(".html"), ODT(".odt"), PDF(".pdf"), PNG(
				".png"), PPTX(".pptx"), RTF(".rtf"), XLS(".xls"), XLSX(".xlsx");
		private String value;

		DocType(String value) {

			this.value = value;
		}

		public String getValue() {

			return value;
		}

		@Override
		public String toString() {

			return this.getValue();
		}

		public static DocType getEnum(String value) {

			for (DocType v : values())
				if (v.getValue().equalsIgnoreCase(value))
					return v;
			throw new IllegalArgumentException();
		}
	}

	private static Log _log = LogFactoryUtil.getLog(JRReportUtil.class
			.getName());

	private static final Float ZOOM_2X = Float.valueOf(2);

	/**
	 * @param jrxmlTemplate
	 * @param jsonData
	 * @param parameters
	 * @param outputDestination
	 * @param exportName
	 * @param docType
	 * @return
	 */
	public static String createReportFile(String jrxmlTemplate,
			String jsonData, Map<String, Object> parameters,
			String outputDestination, String exportName, DocType docType) {

		String sourceFileName = outputDestination + exportName;
		try {
			// fix json enter char
			jsonData = JsonUtils.quoteHTML(jsonData);
			JasperReport reportTemplate = JRReportTemplate
					.getJasperReport(jrxmlTemplate);
			JRJSONDataSource dataSource = JRJSONDataSource
					.getInstance(jsonData);

			JasperPrint jasperPrint = getJasperPrint(reportTemplate,
					parameters, dataSource);

			return exportReport(jasperPrint, sourceFileName, docType);
		} catch (Exception e) {
			_log.error(e);

			return StringPool.BLANK;

		}
	}

	/**
	 * @param jrxmlTemplate
	 * @param jsonData
	 * @param parameters
	 * @param outputDestination
	 * @param exportName
	 * @return
	 */
	public static String createReportPDFFile(String jrxmlTemplate,
			String jsonData, Map<String, Object> parameters,
			String outputDestination, String exportName) {

		String sourceFileName = outputDestination + exportName;
		try {
			// fix json enter char
			jsonData = JsonUtils.quoteHTML(jsonData);
			JasperReport reportTemplate = JRReportTemplate
					.getJasperReport(jrxmlTemplate);
			JRJSONDataSource dataSource = JRJSONDataSource
					.getInstance(jsonData);

			JasperPrint jasperPrint = getJasperPrint(reportTemplate,
					parameters, dataSource);

			return exportPdfFile(jasperPrint, sourceFileName);
		} catch (Exception e) {
			_log.error(e);

			return StringPool.BLANK;

		}
	}

	/**
	 * @param jasperPrint
	 * @param sourceFileName
	 * @return
	 * @throws JRException
	 */
	protected static String exportPdfFile(JasperPrint jasperPrint,
			String sourceFileName) throws JRException {

		JasperExportManager.exportReportToPdfFile(jasperPrint, sourceFileName);

		return sourceFileName;

	}

	/**
	 * @param jasperPrint
	 * @param sourceFileName
	 * @param docType
	 * @return
	 * @throws JRException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected static String exportReport(JasperPrint jasperPrint,
			String sourceFileName, DocType docType) throws JRException,
			FileNotFoundException, IOException {

		switch (docType) {
		case PDF:
			sourceFileName = exportPdfFile(jasperPrint, sourceFileName);
			break;
		case RTF:
			JRRtfExporter rtfExporter = new JRRtfExporter();

			rtfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			rtfExporter.setExporterOutput(new SimpleWriterExporterOutput(
					sourceFileName));

			rtfExporter.exportReport();
			break;
		case XLS:
			JRXlsExporter xlsExporter = new JRXlsExporter();
			// xlsExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,
			// jasperPrint);
			// xlsExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,
			// os);
			xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
					sourceFileName));
			// SimpleXlsExporterConfiguration configuration = new
			// SimpleXlsExporterConfiguration();
			// xlsExporter.setConfiguration(configuration);
			// xlsExporter.setParameter(
			// JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			// xlsExporter.setParameter(
			// JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
			// Boolean.FALSE);
			// xlsExporter.setParameter(
			// JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
			// Boolean.TRUE);
			xlsExporter.exportReport();
			break;
		case XLSX:
			JRXlsxExporter xlsxExporter = new JRXlsxExporter();
			xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			xlsxExporter
					.setExporterOutput(new SimpleOutputStreamExporterOutput(
							sourceFileName));
			xlsxExporter.exportReport();
			break;
		case ODT:
			JROdtExporter odtExporter = new JROdtExporter();
			odtExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			odtExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
					sourceFileName));
			odtExporter.exportReport();
			break;
		case PNG:
			BufferedImage pageImage = new BufferedImage(
					(int) (jasperPrint.getPageWidth() * ZOOM_2X + 1),
					(int) (jasperPrint.getPageHeight() * ZOOM_2X + 1),
					BufferedImage.TYPE_INT_RGB);
			JRGraphics2DExporter exporter = new JRGraphics2DExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D,
					pageImage.getGraphics());
			exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO,
					ZOOM_2X);
			exporter.setParameter(JRExporterParameter.PAGE_INDEX,
					Integer.valueOf(0));
			exporter.exportReport();
			ImageIO.write(pageImage, "png",
					new FileOutputStream(sourceFileName));
			break;
		case HTML:
			HtmlExporter htmlExporter = new HtmlExporter();

			htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(
					sourceFileName));
			break;
		case DOCX:
			JRDocxExporter docxExporter = new JRDocxExporter();
			docxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			docxExporter
					.setExporterOutput(new SimpleOutputStreamExporterOutput(
							sourceFileName));
			break;
		case PPTX:
			JRPptxExporter pptxExporter = new JRPptxExporter();
			pptxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			pptxExporter
					.setExporterOutput(new SimpleOutputStreamExporterOutput(
							sourceFileName));
			break;
		case DOC:
			JRDocxExporter docExporter = new JRDocxExporter();

			SimpleDocxReportConfiguration simpleDocxReportConfiguration = new SimpleDocxReportConfiguration();
			simpleDocxReportConfiguration.setFlexibleRowHeight(true);
			docExporter.setConfiguration(simpleDocxReportConfiguration);

			ExporterInput inp = new SimpleExporterInput(jasperPrint);
			docExporter.setExporterInput(inp);

			OutputStreamExporterOutput outputStreamExporterOutput = new SimpleOutputStreamExporterOutput(
					sourceFileName);
			docExporter.setExporterOutput(outputStreamExporterOutput);

			docExporter.exportReport();
			break;
		default:
			break;
		}

		return sourceFileName;
	}

	/**
	 * @param jrReportTemplate
	 * @param parameters
	 * @param dataSource
	 * @return
	 * @throws JRException
	 */
	protected static JasperPrint getJasperPrint(JasperReport jrReportTemplate,
			Map<String, Object> parameters, JRJSONDataSource dataSource)
			throws JRException {

		JasperPrint jasperPrint = JasperFillManager.fillReport(
				jrReportTemplate, null, dataSource);

		return jasperPrint;
	}

	/**
	 * @param jrReportTemplate
	 * @param parameters
	 * @param dataSource
	 * @return
	 * @throws JRException
	 */
	protected static JasperPrint getJasperPrint(
			JRReportTemplate jrReportTemplate, Map<String, Object> parameters,
			JRJSONDataSource dataSource) throws JRException {

		JasperPrint jasperPrint = JasperFillManager.fillReport(
				jrReportTemplate, null, dataSource);

		return jasperPrint;
	}

	/**
	 * @param response
	 * @param writer
	 * @param jrxmlTemplate
	 * @param jsonData
	 * @param parameters
	 */
	public static void renderReportHTMLStream(HttpServletResponse response,
			PrintWriter writer, String jrxmlTemplate, String jsonData,
			Map<String, Object> parameters) {

		try {
			JasperReport reportTemplate = JRReportTemplate
					.getJasperReport(jrxmlTemplate);
			JRJSONDataSource dataSource = JRJSONDataSource
					.getInstance(jsonData);
			JasperPrint jasperPrint = getJasperPrint(reportTemplate,
					parameters, dataSource);

			HtmlExporter exporter = new HtmlExporter();

			SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(
					writer);
			SimpleExporterInput exporterInput = new SimpleExporterInput(
					jasperPrint);
			exporter.setExporterInput(exporterInput);

			exporter.setExporterOutput(exporterOutput);

			exporter.exportReport();

		} catch (Exception e) {
			_log.error(e);

		}
	}
}

package org.opencps.statisticsmgt.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.opencps.statisticsmgt.bean.DossierStatisticsBean;
import org.opencps.statisticsmgt.model.DossiersStatistics;
import org.opencps.statisticsmgt.model.impl.DossiersStatisticsImpl;
import org.opencps.statisticsmgt.service.persistence.DossiersStatisticsFinder;

import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class StatisticsUtil {

	public static final String RECEIVED = "received";
	public static final String FINISHED = "finished";
	public static final String PROCESSING = "processing";

	public static enum StatisticsFieldNumber {
		ReceivedNumber, OntimeNumber, OvertimeNumber, ProcessingNumber, DelayingNumber, RemainingNumber
	}

	/**
	 * @param field
	 * @param delayStatus
	 * @return
	 */
	public static String getFilterCondition(String field, int... delayStatus) {
		String filter = StringPool.BLANK;
		StatisticsFieldNumber fieldNumber = StatisticsFieldNumber
				.valueOf(field);
		switch (fieldNumber) {
		case ReceivedNumber:
			filter = CustomSQLUtil.get(DossiersStatisticsFinder.class.getName()
					+ StringPool.PERIOD + StringPool.OPEN_BRACKET + RECEIVED
					+ StringPool.CLOSE_BRACKET);
			break;
		case OntimeNumber:
			filter = CustomSQLUtil.get(DossiersStatisticsFinder.class.getName()
					+ StringPool.PERIOD + StringPool.OPEN_BRACKET + FINISHED
					+ StringPool.CLOSE_BRACKET);
			break;
		case OvertimeNumber:
			filter = CustomSQLUtil.get(DossiersStatisticsFinder.class.getName()
					+ StringPool.PERIOD + StringPool.OPEN_BRACKET + FINISHED
					+ StringPool.CLOSE_BRACKET);
			break;

		case ProcessingNumber:
			filter = CustomSQLUtil.get(DossiersStatisticsFinder.class.getName()
					+ StringPool.PERIOD + StringPool.OPEN_BRACKET + PROCESSING
					+ StringPool.CLOSE_BRACKET);
			break;
		case DelayingNumber:
			filter = CustomSQLUtil.get(DossiersStatisticsFinder.class.getName()
					+ StringPool.PERIOD + StringPool.OPEN_BRACKET + PROCESSING
					+ StringPool.CLOSE_BRACKET);
			break;

		default:
			break;
		}
		return filter;
	}

	/**
	 * @param fieldName
	 * @return
	 */
	public static String getSetterMethodName(String fieldName) {
		String methodName = "set" + fieldName;
		return methodName;
	}

	/**
	 * @param q
	 * @param columnDataTypes
	 * @param cacheable
	 * @return
	 */
	public static SQLQuery bindingProperties(SQLQuery q,
			String[] columnDataTypes, boolean cacheable) {
		q.setCacheable(cacheable);

		if (columnDataTypes != null) {
			for (int i = 0; i < columnDataTypes.length; i++) {
				String columnName = "COL" + i;
				Type type = getDataType(columnDataTypes[i]);
				q.addScalar(columnName, type);
			}
		}

		return q;
	}

	/**
	 * @param typeLabel
	 * @return
	 */
	public static Type getDataType(String typeLabel) {

		return Type.valueOf(typeLabel.trim());
	}

	/**
	 * @param columnName
	 * @param coulmnDataType
	 * @param field
	 * @param delayStatus
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Method getMethod(String columnName, String coulmnDataType,
			String field, int... delayStatus) throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		String methodName = "set";
		columnName = columnName.trim();
		coulmnDataType = coulmnDataType.trim();

		columnName = columnName.substring(0,
				columnName.indexOf(StringPool.SPACE));

		if (columnName.contains("d.")) {
			String temp = columnName.substring(
					columnName.lastIndexOf(StringPool.PERIOD) + 1,
					columnName.length());
			temp = StringUtil.upperCaseFirstLetter(temp);
			methodName += "Domain" + temp;
		} else if (columnName.contains("g.")) {
			String temp = columnName.substring(
					columnName.lastIndexOf(StringPool.PERIOD) + 1,
					columnName.length());
			temp = StringUtil.upperCaseFirstLetter(temp);
			methodName += "Gov" + temp;
		} else if (columnName
				.contains("count(opencps_processorder.processOrderId)")) {
			methodName = getSetterMethodName(field);
		} else {
			String temp = columnName.substring(
					columnName.lastIndexOf(StringPool.PERIOD) + 1,
					columnName.length());
			temp = StringUtil.upperCaseFirstLetter(temp);
			methodName += temp;
		}

		Class<?> clazz = getClazz(coulmnDataType);

		Method method = null;
		try {
			if (clazz != null) {
				method = DossierStatisticsBean.class.getMethod(methodName,
						clazz);
			} else {
				method = DossierStatisticsBean.class.getMethod(methodName);
			}
		} catch (Exception e) {

		}

		return method;
	}

	/**
	 * @param typeLabel
	 * @return
	 */
	public static Class<?> getClazz(String typeLabel) {
		Class<?> clazz = null;
		Type type = Type.valueOf(typeLabel);
		switch (type) {

		case STRING:
			clazz = String.class;
			break;

		case LONG:
			clazz = long.class;
			break;

		case INTEGER:
			clazz = int.class;
			break;

		case DATE:
			clazz = Date.class;
			break;

		case SHORT:
			clazz = short.class;
			break;

		default:
			break;
		}
		return clazz;
	}

	/**
	 * @param map
	 * @return
	 */
	public static List<DossiersStatistics> getDossiersStatistics(List data) {
		List<DossiersStatistics> dossiersStatistics = new ArrayList<DossiersStatistics>();
		HashMap<String, DossiersStatistics> map = new HashMap<String, DossiersStatistics>();
		if (data != null) {
			

			for (int i = 0; i < data.size(); i++) {
				DossierStatisticsBean statisticsBean = (DossierStatisticsBean) data
						.get(i);
				DossiersStatistics temp = new DossiersStatisticsImpl();
				String key = statisticsBean.getMonth()
						+ StringPool.DASH
						+ statisticsBean.getYear()
						+ StringPool.DASH
						+ (Validator.isNotNull(statisticsBean.getGovItemCode()) ? statisticsBean
								.getGovItemCode() : StringPool.BLANK)
						+ StringPool.DASH
						+ (Validator.isNotNull(statisticsBean
								.getDomainItemCode()) ? statisticsBean
								.getDomainItemCode() : StringPool.BLANK)
						+ StringPool.DASH
						+ statisticsBean.getAdministrationLevel();
				
				if(map.containsKey(key)){
					temp = map.get(key);
				}
				
				if(temp.getAdministrationLevel() > statisticsBean.getAdministrationLevel()){
					temp.setAdministrationLevel(statisticsBean.getAdministrationLevel());
				}
				
				
				if(statisticsBean.getDelayingNumber() > 0){
					temp.setAdministrationLevel(statisticsBean.getDelayingNumber());
				}
				
				if(statisticsBean.getOntimeNumber() > 0){
					temp.setAdministrationLevel(statisticsBean.getOntimeNumber());
				}
				
				if(statisticsBean.getOvertimeNumber() > 0){
					temp.setAdministrationLevel(statisticsBean.getOvertimeNumber());
				}
				
				if(statisticsBean.getProcessingNumber() > 0){
					temp.setAdministrationLevel(statisticsBean.getProcessingNumber());
				}
				
				if(statisticsBean.getReceivedNumber() > 0){
					temp.setAdministrationLevel(statisticsBean.getReceivedNumber());
				}
				
				
				System.out.println(statisticsBean.getRemainingNumber());
				
				System.out.println(statisticsBean.getDomainItemCode());
				System.out.println(statisticsBean.getGovItemCode());
			}

		}

		return dossiersStatistics;
	}

}

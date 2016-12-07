package org.opencps.statisticsmgt.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.opencps.statisticsmgt.bean.DossierStatisticsBean;

import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

public class StatisticsUtil {

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

	public static Type getDataType(String typeLabel) {

		return Type.valueOf(typeLabel);
	}

	public static Method getMethod(String columnName, String coulmnDataType)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		String methodName = "set";
		columnName = columnName.substring(0,
				columnName.indexOf(StringPool.SPACE));
		if (columnName.contains("domain.")) {
			String temp = columnName.substring(
					columnName.lastIndexOf(StringPool.PERIOD) + 1,
					columnName.length());
			temp = StringUtil.upperCaseFirstLetter(temp);
			methodName += "Domain" + temp;
		} else if (columnName.contains("gov.")) {
			String temp = columnName.substring(
					columnName.lastIndexOf(StringPool.PERIOD) + 1,
					columnName.length());
			temp = StringUtil.upperCaseFirstLetter(temp);
			methodName += "Gov" + temp;
		} else {
			String temp = columnName.substring(
					columnName.lastIndexOf(StringPool.PERIOD) + 1,
					columnName.length());
			temp = StringUtil.upperCaseFirstLetter(temp);
			methodName += temp;
		}

		Class<?> clazz = getClazz(coulmnDataType);

		Method method = DossierStatisticsBean.class
				.getMethod(methodName, clazz);
		if (clazz != null) {
			method = DossierStatisticsBean.class.getMethod(methodName, clazz);
		} else {
			method = DossierStatisticsBean.class.getMethod(methodName);
		}

		return method;
	}

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
}

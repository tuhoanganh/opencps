/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.opencps.statisticsmgt.service.persistence;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencps.statisticsmgt.bean.DossierStatisticsBean;
import org.opencps.statisticsmgt.model.DossiersStatistics;
import org.opencps.statisticsmgt.util.StatisticsUtil;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * The implementation of the dossiers statistics local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.statisticsmgt.service.DossiersStatisticsLocalService}
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author trungnt
 * @see org.opencps.statisticsmgt.service.base.DossiersStatisticsLocalServiceBaseImpl
 * @see org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil
 */
public class DossiersStatisticsFinderImpl extends
		BasePersistenceImpl<DossiersStatistics> implements
		DossiersStatisticsFinder {
	/*
	 * NOTE FOR DEVELOPERS:
	 * 
	 * Never reference this interface directly. Always use {@link
	 * org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil} to
	 * access the dossiers statistics local service.
	 */

	private static final String SQL_STATISTICS_COLUMN_NAMES = DossiersStatisticsFinder.class
			.getName() + ".statisticsColumnNames";

	private static final String SQL_STATISTICS_COLUMN_DATA_TYPES = DossiersStatisticsFinder.class
			.getName() + ".statisticsColumnDataTypes";

	private static final String SQL_COUNT_COLUMN_NAME = DossiersStatisticsFinder.class
			.getName() + ".countColumnName";

	private static final String SQL_COUNT_COLUMN_DATA_TYPE = DossiersStatisticsFinder.class
			.getName() + ".countColumnType";

	private static final String SQL_STATISTICS_RECEIVED_BY_MONTH = DossiersStatisticsFinder.class
			.getName() + ".statisticsReceivedByMonth";

	private static final String SQL_STATISTICS_RECEIVED_BY_YEAR = DossiersStatisticsFinder.class
			.getName() + ".statisticsReceivedByYear";
	
	/**
	 * @param groupId
	 * @param month
	 * @param year
	 * @return
	 */
	public long countReceivedByMonth(long groupId, int month, int year) {
		Session session = null;
		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SQL_STATISTICS_RECEIVED_BY_MONTH);

			String statisticsColumnNames = CustomSQLUtil
					.get(SQL_COUNT_COLUMN_NAME);
			
			sql = StringUtil.replace(sql, "$column$", statisticsColumnNames);
			
			_log.info(sql);

			String[] columnDataTypes = StringUtil.split(CustomSQLUtil
					.get(SQL_COUNT_COLUMN_DATA_TYPE));

			SQLQuery q = session.createSQLQuery(sql);

			q = StatisticsUtil.bindingProperties(q, columnDataTypes, false);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			qPos.add(year);

			Iterator<Integer> itr = q.iterate();

			if (itr.hasNext()) {
				Integer count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;

		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}
		return 0;
	}

	/**
	 * @param groupId
	 * @param month
	 * @param year
	 * @return
	 */
	public List statisticsReceivedByMonth(long groupId, int month, int year) {
		Session session = null;
		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SQL_STATISTICS_RECEIVED_BY_MONTH);

			String statisticsColumnNames = CustomSQLUtil
					.get(SQL_STATISTICS_COLUMN_NAMES);

			String[] columnNames = StringUtil.split(statisticsColumnNames);

			sql = StringUtil.replace(sql, "$column$", statisticsColumnNames);
			
			_log.info(sql);

			String[] columnDataTypes = StringUtil.split(CustomSQLUtil
					.get(SQL_STATISTICS_COLUMN_DATA_TYPES));

			SQLQuery q = session.createSQLQuery(sql);

			q = StatisticsUtil.bindingProperties(q, columnDataTypes, false);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			qPos.add(month);

			qPos.add(year);

			Iterator<Object[]> itr = (Iterator<Object[]>) QueryUtil.list(q,
					getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS)
					.iterator();

			List<DossierStatisticsBean> statisticsBeans = new ArrayList<DossierStatisticsBean>();

			if (itr.hasNext()) {
				while (itr.hasNext()) {
					DossierStatisticsBean statisticsBean = new DossierStatisticsBean();

					Object[] objects = itr.next();

					if (objects.length == columnDataTypes.length) {
						for (int i = 0; i < objects.length; i++) {
							String columnName = columnNames[i];
							String coulmnDataType = columnDataTypes[i];
							Method method = StatisticsUtil.getMethod(
									columnName, coulmnDataType);
							method.invoke(statisticsBean, objects[i]);
						}
					}

					statisticsBeans.add(statisticsBean);
				}
			}

			return statisticsBeans;
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}
		return null;
	}
	
	/**
	 * @param groupId
	 * @param year
	 * @return
	 */
	public long countReceivedByYear(long groupId, int year) {
		Session session = null;
		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SQL_STATISTICS_RECEIVED_BY_YEAR);

			String statisticsColumnNames = CustomSQLUtil
					.get(SQL_COUNT_COLUMN_NAME);

			sql = StringUtil.replace(sql, "$column$", statisticsColumnNames);
			
			_log.info(sql);

			String[] columnDataTypes = StringUtil.split(CustomSQLUtil
					.get(SQL_COUNT_COLUMN_DATA_TYPE));

			SQLQuery q = session.createSQLQuery(sql);

			q = StatisticsUtil.bindingProperties(q, columnDataTypes, false);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			qPos.add(year);

			Iterator<Integer> itr = q.iterate();

			if (itr.hasNext()) {
				Integer count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;

		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}
		return 0;
	}

	/**
	 * @param groupId
	 * @param year
	 * @return
	 */
	public List statisticsReceivedByYear(long groupId, int year) {
		Session session = null;
		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SQL_STATISTICS_RECEIVED_BY_YEAR);

			String statisticsColumnNames = CustomSQLUtil
					.get(SQL_STATISTICS_COLUMN_NAMES);

			String[] columnNames = StringUtil.split(statisticsColumnNames);

			sql = StringUtil.replace(sql, "$column$", statisticsColumnNames);
			
			_log.info(sql);

			String[] columnDataTypes = StringUtil.split(CustomSQLUtil
					.get(SQL_STATISTICS_COLUMN_DATA_TYPES));

			SQLQuery q = session.createSQLQuery(sql);

			q = StatisticsUtil.bindingProperties(q, columnDataTypes, false);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			qPos.add(year);

			Iterator<Object[]> itr = (Iterator<Object[]>) QueryUtil.list(q,
					getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS)
					.iterator();

			List<DossierStatisticsBean> statisticsBeans = new ArrayList<DossierStatisticsBean>();

			if (itr.hasNext()) {
				while (itr.hasNext()) {
					DossierStatisticsBean statisticsBean = new DossierStatisticsBean();

					Object[] objects = itr.next();

					if (objects.length == columnDataTypes.length) {
						for (int i = 0; i < objects.length; i++) {
							String columnName = columnNames[i];
							String coulmnDataType = columnDataTypes[i];
							Method method = StatisticsUtil.getMethod(
									columnName, coulmnDataType);
							method.invoke(statisticsBean, objects[i]);
						}
					}

					statisticsBeans.add(statisticsBean);
				}
			}

			return statisticsBeans;
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}
		return null;
	}

	private Log _log = LogFactoryUtil.getLog(DossiersStatisticsFinderImpl.class
			.getName());
}
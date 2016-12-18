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

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnmodifiableList;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import org.opencps.statisticsmgt.NoSuchDossiersStatisticsException;
import org.opencps.statisticsmgt.model.DossiersStatistics;
import org.opencps.statisticsmgt.model.impl.DossiersStatisticsImpl;
import org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the dossiers statistics service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author trungnt
 * @see DossiersStatisticsPersistence
 * @see DossiersStatisticsUtil
 * @generated
 */
public class DossiersStatisticsPersistenceImpl extends BasePersistenceImpl<DossiersStatistics>
	implements DossiersStatisticsPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link DossiersStatisticsUtil} to access the dossiers statistics persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = DossiersStatisticsImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_MONTH = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByMonth",
			new String[] {
				Integer.class.getName(), Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_MONTH = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByMonth",
			new String[] { Integer.class.getName(), Integer.class.getName() },
			DossiersStatisticsModelImpl.MONTH_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.YEAR_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_MONTH = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByMonth",
			new String[] { Integer.class.getName(), Integer.class.getName() });

	/**
	 * Returns all the dossiers statisticses where month = &#63; and year = &#63;.
	 *
	 * @param month the month
	 * @param year the year
	 * @return the matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByMonth(int month, int year)
		throws SystemException {
		return findByMonth(month, year, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the dossiers statisticses where month = &#63; and year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param month the month
	 * @param year the year
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @return the range of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByMonth(int month, int year, int start,
		int end) throws SystemException {
		return findByMonth(month, year, start, end, null);
	}

	/**
	 * Returns an ordered range of all the dossiers statisticses where month = &#63; and year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param month the month
	 * @param year the year
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByMonth(int month, int year, int start,
		int end, OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_MONTH;
			finderArgs = new Object[] { month, year };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_MONTH;
			finderArgs = new Object[] { month, year, start, end, orderByComparator };
		}

		List<DossiersStatistics> list = (List<DossiersStatistics>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DossiersStatistics dossiersStatistics : list) {
				if ((month != dossiersStatistics.getMonth()) ||
						(year != dossiersStatistics.getYear())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_MONTH_MONTH_2);

			query.append(_FINDER_COLUMN_MONTH_YEAR_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DossiersStatisticsModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(month);

				qPos.add(year);

				if (!pagination) {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<DossiersStatistics>(list);
				}
				else {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first dossiers statistics in the ordered set where month = &#63; and year = &#63;.
	 *
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByMonth_First(int month, int year,
		OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByMonth_First(month, year,
				orderByComparator);

		if (dossiersStatistics != null) {
			return dossiersStatistics;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("month=");
		msg.append(month);

		msg.append(", year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDossiersStatisticsException(msg.toString());
	}

	/**
	 * Returns the first dossiers statistics in the ordered set where month = &#63; and year = &#63;.
	 *
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByMonth_First(int month, int year,
		OrderByComparator orderByComparator) throws SystemException {
		List<DossiersStatistics> list = findByMonth(month, year, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last dossiers statistics in the ordered set where month = &#63; and year = &#63;.
	 *
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByMonth_Last(int month, int year,
		OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByMonth_Last(month, year,
				orderByComparator);

		if (dossiersStatistics != null) {
			return dossiersStatistics;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("month=");
		msg.append(month);

		msg.append(", year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDossiersStatisticsException(msg.toString());
	}

	/**
	 * Returns the last dossiers statistics in the ordered set where month = &#63; and year = &#63;.
	 *
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByMonth_Last(int month, int year,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByMonth(month, year);

		if (count == 0) {
			return null;
		}

		List<DossiersStatistics> list = findByMonth(month, year, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the dossiers statisticses before and after the current dossiers statistics in the ordered set where month = &#63; and year = &#63;.
	 *
	 * @param dossierStatisticId the primary key of the current dossiers statistics
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a dossiers statistics with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics[] findByMonth_PrevAndNext(
		long dossierStatisticId, int month, int year,
		OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = findByPrimaryKey(dossierStatisticId);

		Session session = null;

		try {
			session = openSession();

			DossiersStatistics[] array = new DossiersStatisticsImpl[3];

			array[0] = getByMonth_PrevAndNext(session, dossiersStatistics,
					month, year, orderByComparator, true);

			array[1] = dossiersStatistics;

			array[2] = getByMonth_PrevAndNext(session, dossiersStatistics,
					month, year, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DossiersStatistics getByMonth_PrevAndNext(Session session,
		DossiersStatistics dossiersStatistics, int month, int year,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

		query.append(_FINDER_COLUMN_MONTH_MONTH_2);

		query.append(_FINDER_COLUMN_MONTH_YEAR_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(DossiersStatisticsModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(month);

		qPos.add(year);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dossiersStatistics);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DossiersStatistics> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the dossiers statisticses where month = &#63; and year = &#63; from the database.
	 *
	 * @param month the month
	 * @param year the year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByMonth(int month, int year) throws SystemException {
		for (DossiersStatistics dossiersStatistics : findByMonth(month, year,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(dossiersStatistics);
		}
	}

	/**
	 * Returns the number of dossiers statisticses where month = &#63; and year = &#63;.
	 *
	 * @param month the month
	 * @param year the year
	 * @return the number of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByMonth(int month, int year) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_MONTH;

		Object[] finderArgs = new Object[] { month, year };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_MONTH_MONTH_2);

			query.append(_FINDER_COLUMN_MONTH_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(month);

				qPos.add(year);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_MONTH_MONTH_2 = "dossiersStatistics.month = ? AND ";
	private static final String _FINDER_COLUMN_MONTH_YEAR_2 = "dossiersStatistics.year = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_YEAR = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByYear",
			new String[] {
				Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_YEAR = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByYear",
			new String[] { Integer.class.getName() },
			DossiersStatisticsModelImpl.YEAR_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_YEAR = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByYear",
			new String[] { Integer.class.getName() });

	/**
	 * Returns all the dossiers statisticses where year = &#63;.
	 *
	 * @param year the year
	 * @return the matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByYear(int year)
		throws SystemException {
		return findByYear(year, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the dossiers statisticses where year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param year the year
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @return the range of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByYear(int year, int start, int end)
		throws SystemException {
		return findByYear(year, start, end, null);
	}

	/**
	 * Returns an ordered range of all the dossiers statisticses where year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param year the year
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByYear(int year, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_YEAR;
			finderArgs = new Object[] { year };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_YEAR;
			finderArgs = new Object[] { year, start, end, orderByComparator };
		}

		List<DossiersStatistics> list = (List<DossiersStatistics>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DossiersStatistics dossiersStatistics : list) {
				if ((year != dossiersStatistics.getYear())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_YEAR_YEAR_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DossiersStatisticsModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(year);

				if (!pagination) {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<DossiersStatistics>(list);
				}
				else {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first dossiers statistics in the ordered set where year = &#63;.
	 *
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByYear_First(int year,
		OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByYear_First(year,
				orderByComparator);

		if (dossiersStatistics != null) {
			return dossiersStatistics;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDossiersStatisticsException(msg.toString());
	}

	/**
	 * Returns the first dossiers statistics in the ordered set where year = &#63;.
	 *
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByYear_First(int year,
		OrderByComparator orderByComparator) throws SystemException {
		List<DossiersStatistics> list = findByYear(year, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last dossiers statistics in the ordered set where year = &#63;.
	 *
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByYear_Last(int year,
		OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByYear_Last(year,
				orderByComparator);

		if (dossiersStatistics != null) {
			return dossiersStatistics;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDossiersStatisticsException(msg.toString());
	}

	/**
	 * Returns the last dossiers statistics in the ordered set where year = &#63;.
	 *
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByYear_Last(int year,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByYear(year);

		if (count == 0) {
			return null;
		}

		List<DossiersStatistics> list = findByYear(year, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the dossiers statisticses before and after the current dossiers statistics in the ordered set where year = &#63;.
	 *
	 * @param dossierStatisticId the primary key of the current dossiers statistics
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a dossiers statistics with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics[] findByYear_PrevAndNext(
		long dossierStatisticId, int year, OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = findByPrimaryKey(dossierStatisticId);

		Session session = null;

		try {
			session = openSession();

			DossiersStatistics[] array = new DossiersStatisticsImpl[3];

			array[0] = getByYear_PrevAndNext(session, dossiersStatistics, year,
					orderByComparator, true);

			array[1] = dossiersStatistics;

			array[2] = getByYear_PrevAndNext(session, dossiersStatistics, year,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DossiersStatistics getByYear_PrevAndNext(Session session,
		DossiersStatistics dossiersStatistics, int year,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

		query.append(_FINDER_COLUMN_YEAR_YEAR_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(DossiersStatisticsModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(year);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dossiersStatistics);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DossiersStatistics> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the dossiers statisticses where year = &#63; from the database.
	 *
	 * @param year the year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByYear(int year) throws SystemException {
		for (DossiersStatistics dossiersStatistics : findByYear(year,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(dossiersStatistics);
		}
	}

	/**
	 * Returns the number of dossiers statisticses where year = &#63;.
	 *
	 * @param year the year
	 * @return the number of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByYear(int year) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_YEAR;

		Object[] finderArgs = new Object[] { year };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_YEAR_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(year);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_YEAR_YEAR_2 = "dossiersStatistics.year = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_G_DC_M_Y = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByG_DC_M_Y",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName(), Integer.class.getName()
			},
			DossiersStatisticsModelImpl.GROUPID_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.DOMAINCODE_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.MONTH_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.YEAR_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_DC_M_Y = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_DC_M_Y",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName(), Integer.class.getName()
			});

	/**
	 * Returns the dossiers statistics where groupId = &#63; and domainCode = &#63; and month = &#63; and year = &#63; or throws a {@link org.opencps.statisticsmgt.NoSuchDossiersStatisticsException} if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param domainCode the domain code
	 * @param month the month
	 * @param year the year
	 * @return the matching dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByG_DC_M_Y(long groupId, String domainCode,
		int month, int year)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByG_DC_M_Y(groupId,
				domainCode, month, year);

		if (dossiersStatistics == null) {
			StringBundler msg = new StringBundler(10);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("groupId=");
			msg.append(groupId);

			msg.append(", domainCode=");
			msg.append(domainCode);

			msg.append(", month=");
			msg.append(month);

			msg.append(", year=");
			msg.append(year);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchDossiersStatisticsException(msg.toString());
		}

		return dossiersStatistics;
	}

	/**
	 * Returns the dossiers statistics where groupId = &#63; and domainCode = &#63; and month = &#63; and year = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param domainCode the domain code
	 * @param month the month
	 * @param year the year
	 * @return the matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByG_DC_M_Y(long groupId, String domainCode,
		int month, int year) throws SystemException {
		return fetchByG_DC_M_Y(groupId, domainCode, month, year, true);
	}

	/**
	 * Returns the dossiers statistics where groupId = &#63; and domainCode = &#63; and month = &#63; and year = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param domainCode the domain code
	 * @param month the month
	 * @param year the year
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByG_DC_M_Y(long groupId, String domainCode,
		int month, int year, boolean retrieveFromCache)
		throws SystemException {
		Object[] finderArgs = new Object[] { groupId, domainCode, month, year };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_G_DC_M_Y,
					finderArgs, this);
		}

		if (result instanceof DossiersStatistics) {
			DossiersStatistics dossiersStatistics = (DossiersStatistics)result;

			if ((groupId != dossiersStatistics.getGroupId()) ||
					!Validator.equals(domainCode,
						dossiersStatistics.getDomainCode()) ||
					(month != dossiersStatistics.getMonth()) ||
					(year != dossiersStatistics.getYear())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(6);

			query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_G_DC_M_Y_GROUPID_2);

			boolean bindDomainCode = false;

			if (domainCode == null) {
				query.append(_FINDER_COLUMN_G_DC_M_Y_DOMAINCODE_1);
			}
			else if (domainCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_DC_M_Y_DOMAINCODE_3);
			}
			else {
				bindDomainCode = true;

				query.append(_FINDER_COLUMN_G_DC_M_Y_DOMAINCODE_2);
			}

			query.append(_FINDER_COLUMN_G_DC_M_Y_MONTH_2);

			query.append(_FINDER_COLUMN_G_DC_M_Y_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindDomainCode) {
					qPos.add(domainCode);
				}

				qPos.add(month);

				qPos.add(year);

				List<DossiersStatistics> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_DC_M_Y,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"DossiersStatisticsPersistenceImpl.fetchByG_DC_M_Y(long, String, int, int, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					DossiersStatistics dossiersStatistics = list.get(0);

					result = dossiersStatistics;

					cacheResult(dossiersStatistics);

					if ((dossiersStatistics.getGroupId() != groupId) ||
							(dossiersStatistics.getDomainCode() == null) ||
							!dossiersStatistics.getDomainCode()
												   .equals(domainCode) ||
							(dossiersStatistics.getMonth() != month) ||
							(dossiersStatistics.getYear() != year)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_DC_M_Y,
							finderArgs, dossiersStatistics);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_DC_M_Y,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (DossiersStatistics)result;
		}
	}

	/**
	 * Removes the dossiers statistics where groupId = &#63; and domainCode = &#63; and month = &#63; and year = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param domainCode the domain code
	 * @param month the month
	 * @param year the year
	 * @return the dossiers statistics that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics removeByG_DC_M_Y(long groupId, String domainCode,
		int month, int year)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = findByG_DC_M_Y(groupId,
				domainCode, month, year);

		return remove(dossiersStatistics);
	}

	/**
	 * Returns the number of dossiers statisticses where groupId = &#63; and domainCode = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param domainCode the domain code
	 * @param month the month
	 * @param year the year
	 * @return the number of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_DC_M_Y(long groupId, String domainCode, int month,
		int year) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_DC_M_Y;

		Object[] finderArgs = new Object[] { groupId, domainCode, month, year };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_COUNT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_G_DC_M_Y_GROUPID_2);

			boolean bindDomainCode = false;

			if (domainCode == null) {
				query.append(_FINDER_COLUMN_G_DC_M_Y_DOMAINCODE_1);
			}
			else if (domainCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_DC_M_Y_DOMAINCODE_3);
			}
			else {
				bindDomainCode = true;

				query.append(_FINDER_COLUMN_G_DC_M_Y_DOMAINCODE_2);
			}

			query.append(_FINDER_COLUMN_G_DC_M_Y_MONTH_2);

			query.append(_FINDER_COLUMN_G_DC_M_Y_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindDomainCode) {
					qPos.add(domainCode);
				}

				qPos.add(month);

				qPos.add(year);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_DC_M_Y_GROUPID_2 = "dossiersStatistics.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_DC_M_Y_DOMAINCODE_1 = "dossiersStatistics.domainCode IS NULL AND ";
	private static final String _FINDER_COLUMN_G_DC_M_Y_DOMAINCODE_2 = "dossiersStatistics.domainCode = ? AND ";
	private static final String _FINDER_COLUMN_G_DC_M_Y_DOMAINCODE_3 = "(dossiersStatistics.domainCode IS NULL OR dossiersStatistics.domainCode = '') AND ";
	private static final String _FINDER_COLUMN_G_DC_M_Y_MONTH_2 = "dossiersStatistics.month = ? AND ";
	private static final String _FINDER_COLUMN_G_DC_M_Y_YEAR_2 = "dossiersStatistics.year = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_GC_DC = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_GC_DC",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_GC_DC =
		new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_GC_DC",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName()
			},
			DossiersStatisticsModelImpl.GROUPID_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.GOVAGENCYCODE_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.DOMAINCODE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_GC_DC = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_GC_DC",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_G_GC_DC = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByG_GC_DC",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName()
			});

	/**
	 * Returns all the dossiers statisticses where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @return the matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByG_GC_DC(long groupId,
		String govAgencyCode, String domainCode) throws SystemException {
		return findByG_GC_DC(groupId, govAgencyCode, domainCode,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the dossiers statisticses where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @return the range of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByG_GC_DC(long groupId,
		String govAgencyCode, String domainCode, int start, int end)
		throws SystemException {
		return findByG_GC_DC(groupId, govAgencyCode, domainCode, start, end,
			null);
	}

	/**
	 * Returns an ordered range of all the dossiers statisticses where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByG_GC_DC(long groupId,
		String govAgencyCode, String domainCode, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_GC_DC;
			finderArgs = new Object[] { groupId, govAgencyCode, domainCode };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_GC_DC;
			finderArgs = new Object[] {
					groupId, govAgencyCode, domainCode,
					
					start, end, orderByComparator
				};
		}

		List<DossiersStatistics> list = (List<DossiersStatistics>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DossiersStatistics dossiersStatistics : list) {
				if ((groupId != dossiersStatistics.getGroupId()) ||
						!Validator.equals(govAgencyCode,
							dossiersStatistics.getGovAgencyCode()) ||
						!Validator.equals(domainCode,
							dossiersStatistics.getDomainCode())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_G_GC_DC_GROUPID_2);

			boolean bindGovAgencyCode = false;

			if (govAgencyCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_1);
			}
			else if (govAgencyCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_3);
			}
			else {
				bindGovAgencyCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_2);
			}

			boolean bindDomainCode = false;

			if (domainCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_1);
			}
			else if (domainCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_3);
			}
			else {
				bindDomainCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DossiersStatisticsModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindGovAgencyCode) {
					qPos.add(govAgencyCode);
				}

				if (bindDomainCode) {
					qPos.add(domainCode);
				}

				if (!pagination) {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<DossiersStatistics>(list);
				}
				else {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first dossiers statistics in the ordered set where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByG_GC_DC_First(long groupId,
		String govAgencyCode, String domainCode,
		OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByG_GC_DC_First(groupId,
				govAgencyCode, domainCode, orderByComparator);

		if (dossiersStatistics != null) {
			return dossiersStatistics;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", govAgencyCode=");
		msg.append(govAgencyCode);

		msg.append(", domainCode=");
		msg.append(domainCode);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDossiersStatisticsException(msg.toString());
	}

	/**
	 * Returns the first dossiers statistics in the ordered set where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByG_GC_DC_First(long groupId,
		String govAgencyCode, String domainCode,
		OrderByComparator orderByComparator) throws SystemException {
		List<DossiersStatistics> list = findByG_GC_DC(groupId, govAgencyCode,
				domainCode, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last dossiers statistics in the ordered set where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByG_GC_DC_Last(long groupId,
		String govAgencyCode, String domainCode,
		OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByG_GC_DC_Last(groupId,
				govAgencyCode, domainCode, orderByComparator);

		if (dossiersStatistics != null) {
			return dossiersStatistics;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", govAgencyCode=");
		msg.append(govAgencyCode);

		msg.append(", domainCode=");
		msg.append(domainCode);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDossiersStatisticsException(msg.toString());
	}

	/**
	 * Returns the last dossiers statistics in the ordered set where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByG_GC_DC_Last(long groupId,
		String govAgencyCode, String domainCode,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByG_GC_DC(groupId, govAgencyCode, domainCode);

		if (count == 0) {
			return null;
		}

		List<DossiersStatistics> list = findByG_GC_DC(groupId, govAgencyCode,
				domainCode, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the dossiers statisticses before and after the current dossiers statistics in the ordered set where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63;.
	 *
	 * @param dossierStatisticId the primary key of the current dossiers statistics
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a dossiers statistics with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics[] findByG_GC_DC_PrevAndNext(
		long dossierStatisticId, long groupId, String govAgencyCode,
		String domainCode, OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = findByPrimaryKey(dossierStatisticId);

		Session session = null;

		try {
			session = openSession();

			DossiersStatistics[] array = new DossiersStatisticsImpl[3];

			array[0] = getByG_GC_DC_PrevAndNext(session, dossiersStatistics,
					groupId, govAgencyCode, domainCode, orderByComparator, true);

			array[1] = dossiersStatistics;

			array[2] = getByG_GC_DC_PrevAndNext(session, dossiersStatistics,
					groupId, govAgencyCode, domainCode, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DossiersStatistics getByG_GC_DC_PrevAndNext(Session session,
		DossiersStatistics dossiersStatistics, long groupId,
		String govAgencyCode, String domainCode,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

		query.append(_FINDER_COLUMN_G_GC_DC_GROUPID_2);

		boolean bindGovAgencyCode = false;

		if (govAgencyCode == null) {
			query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_1);
		}
		else if (govAgencyCode.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_3);
		}
		else {
			bindGovAgencyCode = true;

			query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_2);
		}

		boolean bindDomainCode = false;

		if (domainCode == null) {
			query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_1);
		}
		else if (domainCode.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_3);
		}
		else {
			bindDomainCode = true;

			query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(DossiersStatisticsModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (bindGovAgencyCode) {
			qPos.add(govAgencyCode);
		}

		if (bindDomainCode) {
			qPos.add(domainCode);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dossiersStatistics);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DossiersStatistics> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the dossiers statisticses where groupId = &#63; and govAgencyCode = all &#63; and domainCode = all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param govAgencyCodes the gov agency codes
	 * @param domainCodes the domain codes
	 * @return the matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByG_GC_DC(long groupId,
		String[] govAgencyCodes, String[] domainCodes)
		throws SystemException {
		return findByG_GC_DC(groupId, govAgencyCodes, domainCodes,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the dossiers statisticses where groupId = &#63; and govAgencyCode = all &#63; and domainCode = all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param govAgencyCodes the gov agency codes
	 * @param domainCodes the domain codes
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @return the range of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByG_GC_DC(long groupId,
		String[] govAgencyCodes, String[] domainCodes, int start, int end)
		throws SystemException {
		return findByG_GC_DC(groupId, govAgencyCodes, domainCodes, start, end,
			null);
	}

	/**
	 * Returns an ordered range of all the dossiers statisticses where groupId = &#63; and govAgencyCode = all &#63; and domainCode = all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param govAgencyCodes the gov agency codes
	 * @param domainCodes the domain codes
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByG_GC_DC(long groupId,
		String[] govAgencyCodes, String[] domainCodes, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		if ((govAgencyCodes != null) && (govAgencyCodes.length == 1) &&
				(domainCodes != null) && (domainCodes.length == 1)) {
			return findByG_GC_DC(groupId, govAgencyCodes[0], domainCodes[0],
				start, end, orderByComparator);
		}

		boolean pagination = true;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderArgs = new Object[] {
					groupId, StringUtil.merge(govAgencyCodes),
					StringUtil.merge(domainCodes)
				};
		}
		else {
			finderArgs = new Object[] {
					groupId, StringUtil.merge(govAgencyCodes),
					StringUtil.merge(domainCodes),
					
					start, end, orderByComparator
				};
		}

		List<DossiersStatistics> list = (List<DossiersStatistics>)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_FIND_BY_G_GC_DC,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DossiersStatistics dossiersStatistics : list) {
				if ((groupId != dossiersStatistics.getGroupId()) ||
						!ArrayUtil.contains(govAgencyCodes,
							dossiersStatistics.getGovAgencyCode()) ||
						!ArrayUtil.contains(domainCodes,
							dossiersStatistics.getDomainCode())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_G_GC_DC_GROUPID_5);

			conjunctionable = true;

			if ((govAgencyCodes == null) || (govAgencyCodes.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < govAgencyCodes.length; i++) {
					String govAgencyCode = govAgencyCodes[i];

					if (govAgencyCode == null) {
						query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_4);
					}
					else if (govAgencyCode.equals(StringPool.BLANK)) {
						query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_6);
					}
					else {
						query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_5);
					}

					if ((i + 1) < govAgencyCodes.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if ((domainCodes == null) || (domainCodes.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < domainCodes.length; i++) {
					String domainCode = domainCodes[i];

					if (domainCode == null) {
						query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_4);
					}
					else if (domainCode.equals(StringPool.BLANK)) {
						query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_6);
					}
					else {
						query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_5);
					}

					if ((i + 1) < domainCodes.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DossiersStatisticsModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (govAgencyCodes != null) {
					qPos.add(govAgencyCodes);
				}

				if (domainCodes != null) {
					qPos.add(domainCodes);
				}

				if (!pagination) {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<DossiersStatistics>(list);
				}
				else {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_FIND_BY_G_GC_DC,
					finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_WITH_PAGINATION_FIND_BY_G_GC_DC,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the dossiers statisticses where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByG_GC_DC(long groupId, String govAgencyCode,
		String domainCode) throws SystemException {
		for (DossiersStatistics dossiersStatistics : findByG_GC_DC(groupId,
				govAgencyCode, domainCode, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(dossiersStatistics);
		}
	}

	/**
	 * Returns the number of dossiers statisticses where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @return the number of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_GC_DC(long groupId, String govAgencyCode,
		String domainCode) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_GC_DC;

		Object[] finderArgs = new Object[] { groupId, govAgencyCode, domainCode };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_G_GC_DC_GROUPID_2);

			boolean bindGovAgencyCode = false;

			if (govAgencyCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_1);
			}
			else if (govAgencyCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_3);
			}
			else {
				bindGovAgencyCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_2);
			}

			boolean bindDomainCode = false;

			if (domainCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_1);
			}
			else if (domainCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_3);
			}
			else {
				bindDomainCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindGovAgencyCode) {
					qPos.add(govAgencyCode);
				}

				if (bindDomainCode) {
					qPos.add(domainCode);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of dossiers statisticses where groupId = &#63; and govAgencyCode = all &#63; and domainCode = all &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCodes the gov agency codes
	 * @param domainCodes the domain codes
	 * @return the number of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_GC_DC(long groupId, String[] govAgencyCodes,
		String[] domainCodes) throws SystemException {
		Object[] finderArgs = new Object[] {
				groupId, StringUtil.merge(govAgencyCodes),
				StringUtil.merge(domainCodes)
			};

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_G_GC_DC,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_COUNT_DOSSIERSSTATISTICS_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_G_GC_DC_GROUPID_5);

			conjunctionable = true;

			if ((govAgencyCodes == null) || (govAgencyCodes.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < govAgencyCodes.length; i++) {
					String govAgencyCode = govAgencyCodes[i];

					if (govAgencyCode == null) {
						query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_4);
					}
					else if (govAgencyCode.equals(StringPool.BLANK)) {
						query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_6);
					}
					else {
						query.append(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_5);
					}

					if ((i + 1) < govAgencyCodes.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if ((domainCodes == null) || (domainCodes.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < domainCodes.length; i++) {
					String domainCode = domainCodes[i];

					if (domainCode == null) {
						query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_4);
					}
					else if (domainCode.equals(StringPool.BLANK)) {
						query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_6);
					}
					else {
						query.append(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_5);
					}

					if ((i + 1) < domainCodes.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (govAgencyCodes != null) {
					qPos.add(govAgencyCodes);
				}

				if (domainCodes != null) {
					qPos.add(domainCodes);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_G_GC_DC,
					finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_G_GC_DC,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_GC_DC_GROUPID_2 = "dossiersStatistics.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_GROUPID_5 = "(" +
		removeConjunction(_FINDER_COLUMN_G_GC_DC_GROUPID_2) + ")";
	private static final String _FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_1 = "dossiersStatistics.govAgencyCode IS NULL AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_2 = "dossiersStatistics.govAgencyCode = ? AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_3 = "(dossiersStatistics.govAgencyCode IS NULL OR dossiersStatistics.govAgencyCode = '') AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_4 = "(" +
		removeConjunction(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_1) + ")";
	private static final String _FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_5 = "(" +
		removeConjunction(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_2) + ")";
	private static final String _FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_6 = "(" +
		removeConjunction(_FINDER_COLUMN_G_GC_DC_GOVAGENCYCODE_3) + ")";
	private static final String _FINDER_COLUMN_G_GC_DC_DOMAINCODE_1 = "dossiersStatistics.domainCode IS NULL";
	private static final String _FINDER_COLUMN_G_GC_DC_DOMAINCODE_2 = "dossiersStatistics.domainCode = ?";
	private static final String _FINDER_COLUMN_G_GC_DC_DOMAINCODE_3 = "(dossiersStatistics.domainCode IS NULL OR dossiersStatistics.domainCode = '')";
	private static final String _FINDER_COLUMN_G_GC_DC_DOMAINCODE_4 = "(" +
		removeConjunction(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_1) + ")";
	private static final String _FINDER_COLUMN_G_GC_DC_DOMAINCODE_5 = "(" +
		removeConjunction(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_2) + ")";
	private static final String _FINDER_COLUMN_G_GC_DC_DOMAINCODE_6 = "(" +
		removeConjunction(_FINDER_COLUMN_G_GC_DC_DOMAINCODE_3) + ")";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_GC_DC_Y =
		new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_GC_DC_Y",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName(), Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_GC_DC_Y =
		new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_GC_DC_Y",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName(), Integer.class.getName()
			},
			DossiersStatisticsModelImpl.GROUPID_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.GOVAGENCYCODE_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.DOMAINCODE_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.YEAR_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_GC_DC_Y = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_GC_DC_Y",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName(), Integer.class.getName()
			});

	/**
	 * Returns all the dossiers statisticses where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param year the year
	 * @return the matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByG_GC_DC_Y(long groupId,
		String govAgencyCode, String domainCode, int year)
		throws SystemException {
		return findByG_GC_DC_Y(groupId, govAgencyCode, domainCode, year,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the dossiers statisticses where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param year the year
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @return the range of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByG_GC_DC_Y(long groupId,
		String govAgencyCode, String domainCode, int year, int start, int end)
		throws SystemException {
		return findByG_GC_DC_Y(groupId, govAgencyCode, domainCode, year, start,
			end, null);
	}

	/**
	 * Returns an ordered range of all the dossiers statisticses where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param year the year
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findByG_GC_DC_Y(long groupId,
		String govAgencyCode, String domainCode, int year, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_GC_DC_Y;
			finderArgs = new Object[] { groupId, govAgencyCode, domainCode, year };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_GC_DC_Y;
			finderArgs = new Object[] {
					groupId, govAgencyCode, domainCode, year,
					
					start, end, orderByComparator
				};
		}

		List<DossiersStatistics> list = (List<DossiersStatistics>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DossiersStatistics dossiersStatistics : list) {
				if ((groupId != dossiersStatistics.getGroupId()) ||
						!Validator.equals(govAgencyCode,
							dossiersStatistics.getGovAgencyCode()) ||
						!Validator.equals(domainCode,
							dossiersStatistics.getDomainCode()) ||
						(year != dossiersStatistics.getYear())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(6 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(6);
			}

			query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_G_GC_DC_Y_GROUPID_2);

			boolean bindGovAgencyCode = false;

			if (govAgencyCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_1);
			}
			else if (govAgencyCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_3);
			}
			else {
				bindGovAgencyCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_2);
			}

			boolean bindDomainCode = false;

			if (domainCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_1);
			}
			else if (domainCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_3);
			}
			else {
				bindDomainCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_2);
			}

			query.append(_FINDER_COLUMN_G_GC_DC_Y_YEAR_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DossiersStatisticsModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindGovAgencyCode) {
					qPos.add(govAgencyCode);
				}

				if (bindDomainCode) {
					qPos.add(domainCode);
				}

				qPos.add(year);

				if (!pagination) {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<DossiersStatistics>(list);
				}
				else {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first dossiers statistics in the ordered set where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByG_GC_DC_Y_First(long groupId,
		String govAgencyCode, String domainCode, int year,
		OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByG_GC_DC_Y_First(groupId,
				govAgencyCode, domainCode, year, orderByComparator);

		if (dossiersStatistics != null) {
			return dossiersStatistics;
		}

		StringBundler msg = new StringBundler(10);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", govAgencyCode=");
		msg.append(govAgencyCode);

		msg.append(", domainCode=");
		msg.append(domainCode);

		msg.append(", year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDossiersStatisticsException(msg.toString());
	}

	/**
	 * Returns the first dossiers statistics in the ordered set where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByG_GC_DC_Y_First(long groupId,
		String govAgencyCode, String domainCode, int year,
		OrderByComparator orderByComparator) throws SystemException {
		List<DossiersStatistics> list = findByG_GC_DC_Y(groupId, govAgencyCode,
				domainCode, year, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last dossiers statistics in the ordered set where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByG_GC_DC_Y_Last(long groupId,
		String govAgencyCode, String domainCode, int year,
		OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByG_GC_DC_Y_Last(groupId,
				govAgencyCode, domainCode, year, orderByComparator);

		if (dossiersStatistics != null) {
			return dossiersStatistics;
		}

		StringBundler msg = new StringBundler(10);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", govAgencyCode=");
		msg.append(govAgencyCode);

		msg.append(", domainCode=");
		msg.append(domainCode);

		msg.append(", year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDossiersStatisticsException(msg.toString());
	}

	/**
	 * Returns the last dossiers statistics in the ordered set where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByG_GC_DC_Y_Last(long groupId,
		String govAgencyCode, String domainCode, int year,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByG_GC_DC_Y(groupId, govAgencyCode, domainCode, year);

		if (count == 0) {
			return null;
		}

		List<DossiersStatistics> list = findByG_GC_DC_Y(groupId, govAgencyCode,
				domainCode, year, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the dossiers statisticses before and after the current dossiers statistics in the ordered set where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and year = &#63;.
	 *
	 * @param dossierStatisticId the primary key of the current dossiers statistics
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a dossiers statistics with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics[] findByG_GC_DC_Y_PrevAndNext(
		long dossierStatisticId, long groupId, String govAgencyCode,
		String domainCode, int year, OrderByComparator orderByComparator)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = findByPrimaryKey(dossierStatisticId);

		Session session = null;

		try {
			session = openSession();

			DossiersStatistics[] array = new DossiersStatisticsImpl[3];

			array[0] = getByG_GC_DC_Y_PrevAndNext(session, dossiersStatistics,
					groupId, govAgencyCode, domainCode, year,
					orderByComparator, true);

			array[1] = dossiersStatistics;

			array[2] = getByG_GC_DC_Y_PrevAndNext(session, dossiersStatistics,
					groupId, govAgencyCode, domainCode, year,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DossiersStatistics getByG_GC_DC_Y_PrevAndNext(Session session,
		DossiersStatistics dossiersStatistics, long groupId,
		String govAgencyCode, String domainCode, int year,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

		query.append(_FINDER_COLUMN_G_GC_DC_Y_GROUPID_2);

		boolean bindGovAgencyCode = false;

		if (govAgencyCode == null) {
			query.append(_FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_1);
		}
		else if (govAgencyCode.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_3);
		}
		else {
			bindGovAgencyCode = true;

			query.append(_FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_2);
		}

		boolean bindDomainCode = false;

		if (domainCode == null) {
			query.append(_FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_1);
		}
		else if (domainCode.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_3);
		}
		else {
			bindDomainCode = true;

			query.append(_FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_2);
		}

		query.append(_FINDER_COLUMN_G_GC_DC_Y_YEAR_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(DossiersStatisticsModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (bindGovAgencyCode) {
			qPos.add(govAgencyCode);
		}

		if (bindDomainCode) {
			qPos.add(domainCode);
		}

		qPos.add(year);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dossiersStatistics);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DossiersStatistics> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the dossiers statisticses where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and year = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param year the year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByG_GC_DC_Y(long groupId, String govAgencyCode,
		String domainCode, int year) throws SystemException {
		for (DossiersStatistics dossiersStatistics : findByG_GC_DC_Y(groupId,
				govAgencyCode, domainCode, year, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(dossiersStatistics);
		}
	}

	/**
	 * Returns the number of dossiers statisticses where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param year the year
	 * @return the number of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_GC_DC_Y(long groupId, String govAgencyCode,
		String domainCode, int year) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_GC_DC_Y;

		Object[] finderArgs = new Object[] {
				groupId, govAgencyCode, domainCode, year
			};

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_COUNT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_G_GC_DC_Y_GROUPID_2);

			boolean bindGovAgencyCode = false;

			if (govAgencyCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_1);
			}
			else if (govAgencyCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_3);
			}
			else {
				bindGovAgencyCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_2);
			}

			boolean bindDomainCode = false;

			if (domainCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_1);
			}
			else if (domainCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_3);
			}
			else {
				bindDomainCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_2);
			}

			query.append(_FINDER_COLUMN_G_GC_DC_Y_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindGovAgencyCode) {
					qPos.add(govAgencyCode);
				}

				if (bindDomainCode) {
					qPos.add(domainCode);
				}

				qPos.add(year);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_GC_DC_Y_GROUPID_2 = "dossiersStatistics.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_1 = "dossiersStatistics.govAgencyCode IS NULL AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_2 = "dossiersStatistics.govAgencyCode = ? AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_Y_GOVAGENCYCODE_3 = "(dossiersStatistics.govAgencyCode IS NULL OR dossiersStatistics.govAgencyCode = '') AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_1 = "dossiersStatistics.domainCode IS NULL AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_2 = "dossiersStatistics.domainCode = ? AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_Y_DOMAINCODE_3 = "(dossiersStatistics.domainCode IS NULL OR dossiersStatistics.domainCode = '') AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_Y_YEAR_2 = "dossiersStatistics.year = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByG_GC_DC_M_Y_L",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), Integer.class.getName()
			},
			DossiersStatisticsModelImpl.GROUPID_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.GOVAGENCYCODE_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.DOMAINCODE_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.MONTH_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.YEAR_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.ADMINISTRATIONLEVEL_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_GC_DC_M_Y_L = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_GC_DC_M_Y_L",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), Integer.class.getName()
			});

	/**
	 * Returns the dossiers statistics where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and month = &#63; and year = &#63; and administrationLevel = &#63; or throws a {@link org.opencps.statisticsmgt.NoSuchDossiersStatisticsException} if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param month the month
	 * @param year the year
	 * @param administrationLevel the administration level
	 * @return the matching dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByG_GC_DC_M_Y_L(long groupId,
		String govAgencyCode, String domainCode, int month, int year,
		int administrationLevel)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByG_GC_DC_M_Y_L(groupId,
				govAgencyCode, domainCode, month, year, administrationLevel);

		if (dossiersStatistics == null) {
			StringBundler msg = new StringBundler(14);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("groupId=");
			msg.append(groupId);

			msg.append(", govAgencyCode=");
			msg.append(govAgencyCode);

			msg.append(", domainCode=");
			msg.append(domainCode);

			msg.append(", month=");
			msg.append(month);

			msg.append(", year=");
			msg.append(year);

			msg.append(", administrationLevel=");
			msg.append(administrationLevel);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchDossiersStatisticsException(msg.toString());
		}

		return dossiersStatistics;
	}

	/**
	 * Returns the dossiers statistics where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and month = &#63; and year = &#63; and administrationLevel = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param month the month
	 * @param year the year
	 * @param administrationLevel the administration level
	 * @return the matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByG_GC_DC_M_Y_L(long groupId,
		String govAgencyCode, String domainCode, int month, int year,
		int administrationLevel) throws SystemException {
		return fetchByG_GC_DC_M_Y_L(groupId, govAgencyCode, domainCode, month,
			year, administrationLevel, true);
	}

	/**
	 * Returns the dossiers statistics where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and month = &#63; and year = &#63; and administrationLevel = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param month the month
	 * @param year the year
	 * @param administrationLevel the administration level
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByG_GC_DC_M_Y_L(long groupId,
		String govAgencyCode, String domainCode, int month, int year,
		int administrationLevel, boolean retrieveFromCache)
		throws SystemException {
		Object[] finderArgs = new Object[] {
				groupId, govAgencyCode, domainCode, month, year,
				administrationLevel
			};

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L,
					finderArgs, this);
		}

		if (result instanceof DossiersStatistics) {
			DossiersStatistics dossiersStatistics = (DossiersStatistics)result;

			if ((groupId != dossiersStatistics.getGroupId()) ||
					!Validator.equals(govAgencyCode,
						dossiersStatistics.getGovAgencyCode()) ||
					!Validator.equals(domainCode,
						dossiersStatistics.getDomainCode()) ||
					(month != dossiersStatistics.getMonth()) ||
					(year != dossiersStatistics.getYear()) ||
					(administrationLevel != dossiersStatistics.getAdministrationLevel())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(8);

			query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_GROUPID_2);

			boolean bindGovAgencyCode = false;

			if (govAgencyCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_GOVAGENCYCODE_1);
			}
			else if (govAgencyCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_GOVAGENCYCODE_3);
			}
			else {
				bindGovAgencyCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_GOVAGENCYCODE_2);
			}

			boolean bindDomainCode = false;

			if (domainCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_DOMAINCODE_1);
			}
			else if (domainCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_DOMAINCODE_3);
			}
			else {
				bindDomainCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_DOMAINCODE_2);
			}

			query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_MONTH_2);

			query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_YEAR_2);

			query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_ADMINISTRATIONLEVEL_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindGovAgencyCode) {
					qPos.add(govAgencyCode);
				}

				if (bindDomainCode) {
					qPos.add(domainCode);
				}

				qPos.add(month);

				qPos.add(year);

				qPos.add(administrationLevel);

				List<DossiersStatistics> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"DossiersStatisticsPersistenceImpl.fetchByG_GC_DC_M_Y_L(long, String, String, int, int, int, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					DossiersStatistics dossiersStatistics = list.get(0);

					result = dossiersStatistics;

					cacheResult(dossiersStatistics);

					if ((dossiersStatistics.getGroupId() != groupId) ||
							(dossiersStatistics.getGovAgencyCode() == null) ||
							!dossiersStatistics.getGovAgencyCode()
												   .equals(govAgencyCode) ||
							(dossiersStatistics.getDomainCode() == null) ||
							!dossiersStatistics.getDomainCode()
												   .equals(domainCode) ||
							(dossiersStatistics.getMonth() != month) ||
							(dossiersStatistics.getYear() != year) ||
							(dossiersStatistics.getAdministrationLevel() != administrationLevel)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L,
							finderArgs, dossiersStatistics);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (DossiersStatistics)result;
		}
	}

	/**
	 * Removes the dossiers statistics where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and month = &#63; and year = &#63; and administrationLevel = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param month the month
	 * @param year the year
	 * @param administrationLevel the administration level
	 * @return the dossiers statistics that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics removeByG_GC_DC_M_Y_L(long groupId,
		String govAgencyCode, String domainCode, int month, int year,
		int administrationLevel)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = findByG_GC_DC_M_Y_L(groupId,
				govAgencyCode, domainCode, month, year, administrationLevel);

		return remove(dossiersStatistics);
	}

	/**
	 * Returns the number of dossiers statisticses where groupId = &#63; and govAgencyCode = &#63; and domainCode = &#63; and month = &#63; and year = &#63; and administrationLevel = &#63;.
	 *
	 * @param groupId the group ID
	 * @param govAgencyCode the gov agency code
	 * @param domainCode the domain code
	 * @param month the month
	 * @param year the year
	 * @param administrationLevel the administration level
	 * @return the number of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_GC_DC_M_Y_L(long groupId, String govAgencyCode,
		String domainCode, int month, int year, int administrationLevel)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_GC_DC_M_Y_L;

		Object[] finderArgs = new Object[] {
				groupId, govAgencyCode, domainCode, month, year,
				administrationLevel
			};

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(7);

			query.append(_SQL_COUNT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_GROUPID_2);

			boolean bindGovAgencyCode = false;

			if (govAgencyCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_GOVAGENCYCODE_1);
			}
			else if (govAgencyCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_GOVAGENCYCODE_3);
			}
			else {
				bindGovAgencyCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_GOVAGENCYCODE_2);
			}

			boolean bindDomainCode = false;

			if (domainCode == null) {
				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_DOMAINCODE_1);
			}
			else if (domainCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_DOMAINCODE_3);
			}
			else {
				bindDomainCode = true;

				query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_DOMAINCODE_2);
			}

			query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_MONTH_2);

			query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_YEAR_2);

			query.append(_FINDER_COLUMN_G_GC_DC_M_Y_L_ADMINISTRATIONLEVEL_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindGovAgencyCode) {
					qPos.add(govAgencyCode);
				}

				if (bindDomainCode) {
					qPos.add(domainCode);
				}

				qPos.add(month);

				qPos.add(year);

				qPos.add(administrationLevel);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_GC_DC_M_Y_L_GROUPID_2 = "dossiersStatistics.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_M_Y_L_GOVAGENCYCODE_1 = "dossiersStatistics.govAgencyCode IS NULL AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_M_Y_L_GOVAGENCYCODE_2 = "dossiersStatistics.govAgencyCode = ? AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_M_Y_L_GOVAGENCYCODE_3 = "(dossiersStatistics.govAgencyCode IS NULL OR dossiersStatistics.govAgencyCode = '') AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_M_Y_L_DOMAINCODE_1 = "dossiersStatistics.domainCode IS NULL AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_M_Y_L_DOMAINCODE_2 = "dossiersStatistics.domainCode = ? AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_M_Y_L_DOMAINCODE_3 = "(dossiersStatistics.domainCode IS NULL OR dossiersStatistics.domainCode = '') AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_M_Y_L_MONTH_2 = "dossiersStatistics.month = ? AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_M_Y_L_YEAR_2 = "dossiersStatistics.year = ? AND ";
	private static final String _FINDER_COLUMN_G_GC_DC_M_Y_L_ADMINISTRATIONLEVEL_2 =
		"dossiersStatistics.administrationLevel = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_G_M_Y = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED,
			DossiersStatisticsImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByG_M_Y",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName()
			},
			DossiersStatisticsModelImpl.GROUPID_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.MONTH_COLUMN_BITMASK |
			DossiersStatisticsModelImpl.YEAR_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_M_Y = new FinderPath(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_M_Y",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName()
			});

	/**
	 * Returns the dossiers statistics where groupId = &#63; and month = &#63; and year = &#63; or throws a {@link org.opencps.statisticsmgt.NoSuchDossiersStatisticsException} if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @return the matching dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByG_M_Y(long groupId, int month, int year)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByG_M_Y(groupId, month,
				year);

		if (dossiersStatistics == null) {
			StringBundler msg = new StringBundler(8);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("groupId=");
			msg.append(groupId);

			msg.append(", month=");
			msg.append(month);

			msg.append(", year=");
			msg.append(year);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchDossiersStatisticsException(msg.toString());
		}

		return dossiersStatistics;
	}

	/**
	 * Returns the dossiers statistics where groupId = &#63; and month = &#63; and year = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @return the matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByG_M_Y(long groupId, int month, int year)
		throws SystemException {
		return fetchByG_M_Y(groupId, month, year, true);
	}

	/**
	 * Returns the dossiers statistics where groupId = &#63; and month = &#63; and year = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching dossiers statistics, or <code>null</code> if a matching dossiers statistics could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByG_M_Y(long groupId, int month, int year,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { groupId, month, year };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_G_M_Y,
					finderArgs, this);
		}

		if (result instanceof DossiersStatistics) {
			DossiersStatistics dossiersStatistics = (DossiersStatistics)result;

			if ((groupId != dossiersStatistics.getGroupId()) ||
					(month != dossiersStatistics.getMonth()) ||
					(year != dossiersStatistics.getYear())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_SELECT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_G_M_Y_GROUPID_2);

			query.append(_FINDER_COLUMN_G_M_Y_MONTH_2);

			query.append(_FINDER_COLUMN_G_M_Y_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(month);

				qPos.add(year);

				List<DossiersStatistics> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_M_Y,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"DossiersStatisticsPersistenceImpl.fetchByG_M_Y(long, int, int, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					DossiersStatistics dossiersStatistics = list.get(0);

					result = dossiersStatistics;

					cacheResult(dossiersStatistics);

					if ((dossiersStatistics.getGroupId() != groupId) ||
							(dossiersStatistics.getMonth() != month) ||
							(dossiersStatistics.getYear() != year)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_M_Y,
							finderArgs, dossiersStatistics);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_M_Y,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (DossiersStatistics)result;
		}
	}

	/**
	 * Removes the dossiers statistics where groupId = &#63; and month = &#63; and year = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @return the dossiers statistics that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics removeByG_M_Y(long groupId, int month, int year)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = findByG_M_Y(groupId, month, year);

		return remove(dossiersStatistics);
	}

	/**
	 * Returns the number of dossiers statisticses where groupId = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @return the number of matching dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_M_Y(long groupId, int month, int year)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_M_Y;

		Object[] finderArgs = new Object[] { groupId, month, year };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_DOSSIERSSTATISTICS_WHERE);

			query.append(_FINDER_COLUMN_G_M_Y_GROUPID_2);

			query.append(_FINDER_COLUMN_G_M_Y_MONTH_2);

			query.append(_FINDER_COLUMN_G_M_Y_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(month);

				qPos.add(year);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_M_Y_GROUPID_2 = "dossiersStatistics.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_M_Y_MONTH_2 = "dossiersStatistics.month = ? AND ";
	private static final String _FINDER_COLUMN_G_M_Y_YEAR_2 = "dossiersStatistics.year = ?";

	public DossiersStatisticsPersistenceImpl() {
		setModelClass(DossiersStatistics.class);
	}

	/**
	 * Caches the dossiers statistics in the entity cache if it is enabled.
	 *
	 * @param dossiersStatistics the dossiers statistics
	 */
	@Override
	public void cacheResult(DossiersStatistics dossiersStatistics) {
		EntityCacheUtil.putResult(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsImpl.class, dossiersStatistics.getPrimaryKey(),
			dossiersStatistics);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_DC_M_Y,
			new Object[] {
				dossiersStatistics.getGroupId(),
				dossiersStatistics.getDomainCode(),
				dossiersStatistics.getMonth(), dossiersStatistics.getYear()
			}, dossiersStatistics);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L,
			new Object[] {
				dossiersStatistics.getGroupId(),
				dossiersStatistics.getGovAgencyCode(),
				dossiersStatistics.getDomainCode(),
				dossiersStatistics.getMonth(), dossiersStatistics.getYear(),
				dossiersStatistics.getAdministrationLevel()
			}, dossiersStatistics);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_M_Y,
			new Object[] {
				dossiersStatistics.getGroupId(), dossiersStatistics.getMonth(),
				dossiersStatistics.getYear()
			}, dossiersStatistics);

		dossiersStatistics.resetOriginalValues();
	}

	/**
	 * Caches the dossiers statisticses in the entity cache if it is enabled.
	 *
	 * @param dossiersStatisticses the dossiers statisticses
	 */
	@Override
	public void cacheResult(List<DossiersStatistics> dossiersStatisticses) {
		for (DossiersStatistics dossiersStatistics : dossiersStatisticses) {
			if (EntityCacheUtil.getResult(
						DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
						DossiersStatisticsImpl.class,
						dossiersStatistics.getPrimaryKey()) == null) {
				cacheResult(dossiersStatistics);
			}
			else {
				dossiersStatistics.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all dossiers statisticses.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(DossiersStatisticsImpl.class.getName());
		}

		EntityCacheUtil.clearCache(DossiersStatisticsImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the dossiers statistics.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(DossiersStatistics dossiersStatistics) {
		EntityCacheUtil.removeResult(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsImpl.class, dossiersStatistics.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(dossiersStatistics);
	}

	@Override
	public void clearCache(List<DossiersStatistics> dossiersStatisticses) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (DossiersStatistics dossiersStatistics : dossiersStatisticses) {
			EntityCacheUtil.removeResult(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
				DossiersStatisticsImpl.class, dossiersStatistics.getPrimaryKey());

			clearUniqueFindersCache(dossiersStatistics);
		}
	}

	protected void cacheUniqueFindersCache(
		DossiersStatistics dossiersStatistics) {
		if (dossiersStatistics.isNew()) {
			Object[] args = new Object[] {
					dossiersStatistics.getGroupId(),
					dossiersStatistics.getDomainCode(),
					dossiersStatistics.getMonth(), dossiersStatistics.getYear()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_DC_M_Y, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_DC_M_Y, args,
				dossiersStatistics);

			args = new Object[] {
					dossiersStatistics.getGroupId(),
					dossiersStatistics.getGovAgencyCode(),
					dossiersStatistics.getDomainCode(),
					dossiersStatistics.getMonth(), dossiersStatistics.getYear(),
					dossiersStatistics.getAdministrationLevel()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_GC_DC_M_Y_L, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L, args,
				dossiersStatistics);

			args = new Object[] {
					dossiersStatistics.getGroupId(),
					dossiersStatistics.getMonth(), dossiersStatistics.getYear()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_M_Y, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_M_Y, args,
				dossiersStatistics);
		}
		else {
			DossiersStatisticsModelImpl dossiersStatisticsModelImpl = (DossiersStatisticsModelImpl)dossiersStatistics;

			if ((dossiersStatisticsModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_G_DC_M_Y.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dossiersStatistics.getGroupId(),
						dossiersStatistics.getDomainCode(),
						dossiersStatistics.getMonth(),
						dossiersStatistics.getYear()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_DC_M_Y, args,
					Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_DC_M_Y, args,
					dossiersStatistics);
			}

			if ((dossiersStatisticsModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dossiersStatistics.getGroupId(),
						dossiersStatistics.getGovAgencyCode(),
						dossiersStatistics.getDomainCode(),
						dossiersStatistics.getMonth(),
						dossiersStatistics.getYear(),
						dossiersStatistics.getAdministrationLevel()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_GC_DC_M_Y_L,
					args, Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L,
					args, dossiersStatistics);
			}

			if ((dossiersStatisticsModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_G_M_Y.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dossiersStatistics.getGroupId(),
						dossiersStatistics.getMonth(),
						dossiersStatistics.getYear()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_M_Y, args,
					Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_M_Y, args,
					dossiersStatistics);
			}
		}
	}

	protected void clearUniqueFindersCache(
		DossiersStatistics dossiersStatistics) {
		DossiersStatisticsModelImpl dossiersStatisticsModelImpl = (DossiersStatisticsModelImpl)dossiersStatistics;

		Object[] args = new Object[] {
				dossiersStatistics.getGroupId(),
				dossiersStatistics.getDomainCode(),
				dossiersStatistics.getMonth(), dossiersStatistics.getYear()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_DC_M_Y, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_DC_M_Y, args);

		if ((dossiersStatisticsModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_G_DC_M_Y.getColumnBitmask()) != 0) {
			args = new Object[] {
					dossiersStatisticsModelImpl.getOriginalGroupId(),
					dossiersStatisticsModelImpl.getOriginalDomainCode(),
					dossiersStatisticsModelImpl.getOriginalMonth(),
					dossiersStatisticsModelImpl.getOriginalYear()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_DC_M_Y, args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_DC_M_Y, args);
		}

		args = new Object[] {
				dossiersStatistics.getGroupId(),
				dossiersStatistics.getGovAgencyCode(),
				dossiersStatistics.getDomainCode(),
				dossiersStatistics.getMonth(), dossiersStatistics.getYear(),
				dossiersStatistics.getAdministrationLevel()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_GC_DC_M_Y_L, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L, args);

		if ((dossiersStatisticsModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L.getColumnBitmask()) != 0) {
			args = new Object[] {
					dossiersStatisticsModelImpl.getOriginalGroupId(),
					dossiersStatisticsModelImpl.getOriginalGovAgencyCode(),
					dossiersStatisticsModelImpl.getOriginalDomainCode(),
					dossiersStatisticsModelImpl.getOriginalMonth(),
					dossiersStatisticsModelImpl.getOriginalYear(),
					dossiersStatisticsModelImpl.getOriginalAdministrationLevel()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_GC_DC_M_Y_L,
				args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_GC_DC_M_Y_L,
				args);
		}

		args = new Object[] {
				dossiersStatistics.getGroupId(), dossiersStatistics.getMonth(),
				dossiersStatistics.getYear()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_M_Y, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_M_Y, args);

		if ((dossiersStatisticsModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_G_M_Y.getColumnBitmask()) != 0) {
			args = new Object[] {
					dossiersStatisticsModelImpl.getOriginalGroupId(),
					dossiersStatisticsModelImpl.getOriginalMonth(),
					dossiersStatisticsModelImpl.getOriginalYear()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_M_Y, args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_M_Y, args);
		}
	}

	/**
	 * Creates a new dossiers statistics with the primary key. Does not add the dossiers statistics to the database.
	 *
	 * @param dossierStatisticId the primary key for the new dossiers statistics
	 * @return the new dossiers statistics
	 */
	@Override
	public DossiersStatistics create(long dossierStatisticId) {
		DossiersStatistics dossiersStatistics = new DossiersStatisticsImpl();

		dossiersStatistics.setNew(true);
		dossiersStatistics.setPrimaryKey(dossierStatisticId);

		return dossiersStatistics;
	}

	/**
	 * Removes the dossiers statistics with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param dossierStatisticId the primary key of the dossiers statistics
	 * @return the dossiers statistics that was removed
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a dossiers statistics with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics remove(long dossierStatisticId)
		throws NoSuchDossiersStatisticsException, SystemException {
		return remove((Serializable)dossierStatisticId);
	}

	/**
	 * Removes the dossiers statistics with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the dossiers statistics
	 * @return the dossiers statistics that was removed
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a dossiers statistics with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics remove(Serializable primaryKey)
		throws NoSuchDossiersStatisticsException, SystemException {
		Session session = null;

		try {
			session = openSession();

			DossiersStatistics dossiersStatistics = (DossiersStatistics)session.get(DossiersStatisticsImpl.class,
					primaryKey);

			if (dossiersStatistics == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchDossiersStatisticsException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(dossiersStatistics);
		}
		catch (NoSuchDossiersStatisticsException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected DossiersStatistics removeImpl(
		DossiersStatistics dossiersStatistics) throws SystemException {
		dossiersStatistics = toUnwrappedModel(dossiersStatistics);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(dossiersStatistics)) {
				dossiersStatistics = (DossiersStatistics)session.get(DossiersStatisticsImpl.class,
						dossiersStatistics.getPrimaryKeyObj());
			}

			if (dossiersStatistics != null) {
				session.delete(dossiersStatistics);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (dossiersStatistics != null) {
			clearCache(dossiersStatistics);
		}

		return dossiersStatistics;
	}

	@Override
	public DossiersStatistics updateImpl(
		org.opencps.statisticsmgt.model.DossiersStatistics dossiersStatistics)
		throws SystemException {
		dossiersStatistics = toUnwrappedModel(dossiersStatistics);

		boolean isNew = dossiersStatistics.isNew();

		DossiersStatisticsModelImpl dossiersStatisticsModelImpl = (DossiersStatisticsModelImpl)dossiersStatistics;

		Session session = null;

		try {
			session = openSession();

			if (dossiersStatistics.isNew()) {
				session.save(dossiersStatistics);

				dossiersStatistics.setNew(false);
			}
			else {
				session.merge(dossiersStatistics);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !DossiersStatisticsModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((dossiersStatisticsModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_MONTH.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dossiersStatisticsModelImpl.getOriginalMonth(),
						dossiersStatisticsModelImpl.getOriginalYear()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_MONTH, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_MONTH,
					args);

				args = new Object[] {
						dossiersStatisticsModelImpl.getMonth(),
						dossiersStatisticsModelImpl.getYear()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_MONTH, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_MONTH,
					args);
			}

			if ((dossiersStatisticsModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_YEAR.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dossiersStatisticsModelImpl.getOriginalYear()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_YEAR, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_YEAR,
					args);

				args = new Object[] { dossiersStatisticsModelImpl.getYear() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_YEAR, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_YEAR,
					args);
			}

			if ((dossiersStatisticsModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_GC_DC.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dossiersStatisticsModelImpl.getOriginalGroupId(),
						dossiersStatisticsModelImpl.getOriginalGovAgencyCode(),
						dossiersStatisticsModelImpl.getOriginalDomainCode()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_GC_DC, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_GC_DC,
					args);

				args = new Object[] {
						dossiersStatisticsModelImpl.getGroupId(),
						dossiersStatisticsModelImpl.getGovAgencyCode(),
						dossiersStatisticsModelImpl.getDomainCode()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_GC_DC, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_GC_DC,
					args);
			}

			if ((dossiersStatisticsModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_GC_DC_Y.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dossiersStatisticsModelImpl.getOriginalGroupId(),
						dossiersStatisticsModelImpl.getOriginalGovAgencyCode(),
						dossiersStatisticsModelImpl.getOriginalDomainCode(),
						dossiersStatisticsModelImpl.getOriginalYear()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_GC_DC_Y,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_GC_DC_Y,
					args);

				args = new Object[] {
						dossiersStatisticsModelImpl.getGroupId(),
						dossiersStatisticsModelImpl.getGovAgencyCode(),
						dossiersStatisticsModelImpl.getDomainCode(),
						dossiersStatisticsModelImpl.getYear()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_GC_DC_Y,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_GC_DC_Y,
					args);
			}
		}

		EntityCacheUtil.putResult(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
			DossiersStatisticsImpl.class, dossiersStatistics.getPrimaryKey(),
			dossiersStatistics);

		clearUniqueFindersCache(dossiersStatistics);
		cacheUniqueFindersCache(dossiersStatistics);

		return dossiersStatistics;
	}

	protected DossiersStatistics toUnwrappedModel(
		DossiersStatistics dossiersStatistics) {
		if (dossiersStatistics instanceof DossiersStatisticsImpl) {
			return dossiersStatistics;
		}

		DossiersStatisticsImpl dossiersStatisticsImpl = new DossiersStatisticsImpl();

		dossiersStatisticsImpl.setNew(dossiersStatistics.isNew());
		dossiersStatisticsImpl.setPrimaryKey(dossiersStatistics.getPrimaryKey());

		dossiersStatisticsImpl.setDossierStatisticId(dossiersStatistics.getDossierStatisticId());
		dossiersStatisticsImpl.setCompanyId(dossiersStatistics.getCompanyId());
		dossiersStatisticsImpl.setGroupId(dossiersStatistics.getGroupId());
		dossiersStatisticsImpl.setUserId(dossiersStatistics.getUserId());
		dossiersStatisticsImpl.setCreateDate(dossiersStatistics.getCreateDate());
		dossiersStatisticsImpl.setModifiedDate(dossiersStatistics.getModifiedDate());
		dossiersStatisticsImpl.setRemainingNumber(dossiersStatistics.getRemainingNumber());
		dossiersStatisticsImpl.setReceivedNumber(dossiersStatistics.getReceivedNumber());
		dossiersStatisticsImpl.setOntimeNumber(dossiersStatistics.getOntimeNumber());
		dossiersStatisticsImpl.setOvertimeNumber(dossiersStatistics.getOvertimeNumber());
		dossiersStatisticsImpl.setProcessingNumber(dossiersStatistics.getProcessingNumber());
		dossiersStatisticsImpl.setDelayingNumber(dossiersStatistics.getDelayingNumber());
		dossiersStatisticsImpl.setMonth(dossiersStatistics.getMonth());
		dossiersStatisticsImpl.setYear(dossiersStatistics.getYear());
		dossiersStatisticsImpl.setGovAgencyCode(dossiersStatistics.getGovAgencyCode());
		dossiersStatisticsImpl.setDomainCode(dossiersStatistics.getDomainCode());
		dossiersStatisticsImpl.setAdministrationLevel(dossiersStatistics.getAdministrationLevel());

		return dossiersStatisticsImpl;
	}

	/**
	 * Returns the dossiers statistics with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the dossiers statistics
	 * @return the dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a dossiers statistics with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByPrimaryKey(Serializable primaryKey)
		throws NoSuchDossiersStatisticsException, SystemException {
		DossiersStatistics dossiersStatistics = fetchByPrimaryKey(primaryKey);

		if (dossiersStatistics == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchDossiersStatisticsException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return dossiersStatistics;
	}

	/**
	 * Returns the dossiers statistics with the primary key or throws a {@link org.opencps.statisticsmgt.NoSuchDossiersStatisticsException} if it could not be found.
	 *
	 * @param dossierStatisticId the primary key of the dossiers statistics
	 * @return the dossiers statistics
	 * @throws org.opencps.statisticsmgt.NoSuchDossiersStatisticsException if a dossiers statistics with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics findByPrimaryKey(long dossierStatisticId)
		throws NoSuchDossiersStatisticsException, SystemException {
		return findByPrimaryKey((Serializable)dossierStatisticId);
	}

	/**
	 * Returns the dossiers statistics with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the dossiers statistics
	 * @return the dossiers statistics, or <code>null</code> if a dossiers statistics with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		DossiersStatistics dossiersStatistics = (DossiersStatistics)EntityCacheUtil.getResult(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
				DossiersStatisticsImpl.class, primaryKey);

		if (dossiersStatistics == _nullDossiersStatistics) {
			return null;
		}

		if (dossiersStatistics == null) {
			Session session = null;

			try {
				session = openSession();

				dossiersStatistics = (DossiersStatistics)session.get(DossiersStatisticsImpl.class,
						primaryKey);

				if (dossiersStatistics != null) {
					cacheResult(dossiersStatistics);
				}
				else {
					EntityCacheUtil.putResult(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
						DossiersStatisticsImpl.class, primaryKey,
						_nullDossiersStatistics);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(DossiersStatisticsModelImpl.ENTITY_CACHE_ENABLED,
					DossiersStatisticsImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return dossiersStatistics;
	}

	/**
	 * Returns the dossiers statistics with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param dossierStatisticId the primary key of the dossiers statistics
	 * @return the dossiers statistics, or <code>null</code> if a dossiers statistics with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossiersStatistics fetchByPrimaryKey(long dossierStatisticId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)dossierStatisticId);
	}

	/**
	 * Returns all the dossiers statisticses.
	 *
	 * @return the dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the dossiers statisticses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @return the range of dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the dossiers statisticses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.statisticsmgt.model.impl.DossiersStatisticsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of dossiers statisticses
	 * @param end the upper bound of the range of dossiers statisticses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossiersStatistics> findAll(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<DossiersStatistics> list = (List<DossiersStatistics>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_DOSSIERSSTATISTICS);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_DOSSIERSSTATISTICS;

				if (pagination) {
					sql = sql.concat(DossiersStatisticsModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<DossiersStatistics>(list);
				}
				else {
					list = (List<DossiersStatistics>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the dossiers statisticses from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (DossiersStatistics dossiersStatistics : findAll()) {
			remove(dossiersStatistics);
		}
	}

	/**
	 * Returns the number of dossiers statisticses.
	 *
	 * @return the number of dossiers statisticses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_DOSSIERSSTATISTICS);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the dossiers statistics persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.org.opencps.statisticsmgt.model.DossiersStatistics")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<DossiersStatistics>> listenersList = new ArrayList<ModelListener<DossiersStatistics>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<DossiersStatistics>)InstanceFactory.newInstance(
							getClassLoader(), listenerClassName));
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void destroy() {
		EntityCacheUtil.removeCache(DossiersStatisticsImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_DOSSIERSSTATISTICS = "SELECT dossiersStatistics FROM DossiersStatistics dossiersStatistics";
	private static final String _SQL_SELECT_DOSSIERSSTATISTICS_WHERE = "SELECT dossiersStatistics FROM DossiersStatistics dossiersStatistics WHERE ";
	private static final String _SQL_COUNT_DOSSIERSSTATISTICS = "SELECT COUNT(dossiersStatistics) FROM DossiersStatistics dossiersStatistics";
	private static final String _SQL_COUNT_DOSSIERSSTATISTICS_WHERE = "SELECT COUNT(dossiersStatistics) FROM DossiersStatistics dossiersStatistics WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "dossiersStatistics.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No DossiersStatistics exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No DossiersStatistics exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(DossiersStatisticsPersistenceImpl.class);
	private static DossiersStatistics _nullDossiersStatistics = new DossiersStatisticsImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<DossiersStatistics> toCacheModel() {
				return _nullDossiersStatisticsCacheModel;
			}
		};

	private static CacheModel<DossiersStatistics> _nullDossiersStatisticsCacheModel =
		new CacheModel<DossiersStatistics>() {
			@Override
			public DossiersStatistics toEntityModel() {
				return _nullDossiersStatistics;
			}
		};
}
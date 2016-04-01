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

package org.opencps.accountmgt.service.persistence;

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
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnmodifiableList;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import org.opencps.accountmgt.NoSuchBusinessException;
import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.impl.BusinessImpl;
import org.opencps.accountmgt.model.impl.BusinessModelImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The persistence implementation for the business service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author khoavd
 * @see BusinessPersistence
 * @see BusinessUtil
 * @generated
 */
public class BusinessPersistenceImpl extends BasePersistenceImpl<Business>
	implements BusinessPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link BusinessUtil} to access the business persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = BusinessImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID =
		new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupId",
			new String[] { Long.class.getName() },
			BusinessModelImpl.GROUPID_COLUMN_BITMASK |
			BusinessModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the businesses where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByGroupId(long groupId) throws SystemException {
		return findByGroupId(groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the businesses where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @return the range of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByGroupId(long groupId, int start, int end)
		throws SystemException {
		return findByGroupId(groupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the businesses where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByGroupId(long groupId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID;
			finderArgs = new Object[] { groupId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID;
			finderArgs = new Object[] { groupId, start, end, orderByComparator };
		}

		List<Business> list = (List<Business>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Business business : list) {
				if ((groupId != business.getGroupId())) {
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

			query.append(_SQL_SELECT_BUSINESS_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(BusinessModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (!pagination) {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Business>(list);
				}
				else {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end);
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
	 * Returns the first business in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByGroupId_First(long groupId,
		OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = fetchByGroupId_First(groupId, orderByComparator);

		if (business != null) {
			return business;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBusinessException(msg.toString());
	}

	/**
	 * Returns the first business in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching business, or <code>null</code> if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByGroupId_First(long groupId,
		OrderByComparator orderByComparator) throws SystemException {
		List<Business> list = findByGroupId(groupId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last business in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByGroupId_Last(long groupId,
		OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = fetchByGroupId_Last(groupId, orderByComparator);

		if (business != null) {
			return business;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBusinessException(msg.toString());
	}

	/**
	 * Returns the last business in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching business, or <code>null</code> if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByGroupId_Last(long groupId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByGroupId(groupId);

		if (count == 0) {
			return null;
		}

		List<Business> list = findByGroupId(groupId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the businesses before and after the current business in the ordered set where groupId = &#63;.
	 *
	 * @param businessId the primary key of the current business
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a business with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business[] findByGroupId_PrevAndNext(long businessId, long groupId,
		OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = findByPrimaryKey(businessId);

		Session session = null;

		try {
			session = openSession();

			Business[] array = new BusinessImpl[3];

			array[0] = getByGroupId_PrevAndNext(session, business, groupId,
					orderByComparator, true);

			array[1] = business;

			array[2] = getByGroupId_PrevAndNext(session, business, groupId,
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

	protected Business getByGroupId_PrevAndNext(Session session,
		Business business, long groupId, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_BUSINESS_WHERE);

		query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

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
			query.append(BusinessModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(business);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Business> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the businesses where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByGroupId(long groupId) throws SystemException {
		for (Business business : findByGroupId(groupId, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(business);
		}
	}

	/**
	 * Returns the number of businesses where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByGroupId(long groupId) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_GROUPID;

		Object[] finderArgs = new Object[] { groupId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_BUSINESS_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

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

	private static final String _FINDER_COLUMN_GROUPID_GROUPID_2 = "business.groupId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_S = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_S",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_S",
			new String[] { Long.class.getName(), Integer.class.getName() },
			BusinessModelImpl.GROUPID_COLUMN_BITMASK |
			BusinessModelImpl.ACCOUNTSTATUS_COLUMN_BITMASK |
			BusinessModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_S = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_S",
			new String[] { Long.class.getName(), Integer.class.getName() });

	/**
	 * Returns all the businesses where groupId = &#63; and accountStatus = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @return the matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_S(long groupId, int accountStatus)
		throws SystemException {
		return findByG_S(groupId, accountStatus, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the businesses where groupId = &#63; and accountStatus = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @return the range of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_S(long groupId, int accountStatus, int start,
		int end) throws SystemException {
		return findByG_S(groupId, accountStatus, start, end, null);
	}

	/**
	 * Returns an ordered range of all the businesses where groupId = &#63; and accountStatus = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_S(long groupId, int accountStatus, int start,
		int end, OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S;
			finderArgs = new Object[] { groupId, accountStatus };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_S;
			finderArgs = new Object[] {
					groupId, accountStatus,
					
					start, end, orderByComparator
				};
		}

		List<Business> list = (List<Business>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Business business : list) {
				if ((groupId != business.getGroupId()) ||
						(accountStatus != business.getAccountStatus())) {
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

			query.append(_SQL_SELECT_BUSINESS_WHERE);

			query.append(_FINDER_COLUMN_G_S_GROUPID_2);

			query.append(_FINDER_COLUMN_G_S_ACCOUNTSTATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(BusinessModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(accountStatus);

				if (!pagination) {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Business>(list);
				}
				else {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end);
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
	 * Returns the first business in the ordered set where groupId = &#63; and accountStatus = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByG_S_First(long groupId, int accountStatus,
		OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = fetchByG_S_First(groupId, accountStatus,
				orderByComparator);

		if (business != null) {
			return business;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", accountStatus=");
		msg.append(accountStatus);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBusinessException(msg.toString());
	}

	/**
	 * Returns the first business in the ordered set where groupId = &#63; and accountStatus = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching business, or <code>null</code> if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByG_S_First(long groupId, int accountStatus,
		OrderByComparator orderByComparator) throws SystemException {
		List<Business> list = findByG_S(groupId, accountStatus, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last business in the ordered set where groupId = &#63; and accountStatus = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByG_S_Last(long groupId, int accountStatus,
		OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = fetchByG_S_Last(groupId, accountStatus,
				orderByComparator);

		if (business != null) {
			return business;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", accountStatus=");
		msg.append(accountStatus);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBusinessException(msg.toString());
	}

	/**
	 * Returns the last business in the ordered set where groupId = &#63; and accountStatus = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching business, or <code>null</code> if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByG_S_Last(long groupId, int accountStatus,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByG_S(groupId, accountStatus);

		if (count == 0) {
			return null;
		}

		List<Business> list = findByG_S(groupId, accountStatus, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the businesses before and after the current business in the ordered set where groupId = &#63; and accountStatus = &#63;.
	 *
	 * @param businessId the primary key of the current business
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a business with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business[] findByG_S_PrevAndNext(long businessId, long groupId,
		int accountStatus, OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = findByPrimaryKey(businessId);

		Session session = null;

		try {
			session = openSession();

			Business[] array = new BusinessImpl[3];

			array[0] = getByG_S_PrevAndNext(session, business, groupId,
					accountStatus, orderByComparator, true);

			array[1] = business;

			array[2] = getByG_S_PrevAndNext(session, business, groupId,
					accountStatus, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Business getByG_S_PrevAndNext(Session session, Business business,
		long groupId, int accountStatus, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_BUSINESS_WHERE);

		query.append(_FINDER_COLUMN_G_S_GROUPID_2);

		query.append(_FINDER_COLUMN_G_S_ACCOUNTSTATUS_2);

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
			query.append(BusinessModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		qPos.add(accountStatus);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(business);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Business> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the businesses where groupId = &#63; and accountStatus = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByG_S(long groupId, int accountStatus)
		throws SystemException {
		for (Business business : findByG_S(groupId, accountStatus,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(business);
		}
	}

	/**
	 * Returns the number of businesses where groupId = &#63; and accountStatus = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @return the number of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_S(long groupId, int accountStatus)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_S;

		Object[] finderArgs = new Object[] { groupId, accountStatus };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_BUSINESS_WHERE);

			query.append(_FINDER_COLUMN_G_S_GROUPID_2);

			query.append(_FINDER_COLUMN_G_S_ACCOUNTSTATUS_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(accountStatus);

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

	private static final String _FINDER_COLUMN_G_S_GROUPID_2 = "business.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_S_ACCOUNTSTATUS_2 = "business.accountStatus = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_S_C = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_S_C",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S_C = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_S_C",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				String.class.getName()
			},
			BusinessModelImpl.GROUPID_COLUMN_BITMASK |
			BusinessModelImpl.ACCOUNTSTATUS_COLUMN_BITMASK |
			BusinessModelImpl.CITYCODE_COLUMN_BITMASK |
			BusinessModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_S_C = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_S_C",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				String.class.getName()
			});

	/**
	 * Returns all the businesses where groupId = &#63; and accountStatus = &#63; and cityCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param cityCode the city code
	 * @return the matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_S_C(long groupId, int accountStatus,
		String cityCode) throws SystemException {
		return findByG_S_C(groupId, accountStatus, cityCode, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the businesses where groupId = &#63; and accountStatus = &#63; and cityCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param cityCode the city code
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @return the range of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_S_C(long groupId, int accountStatus,
		String cityCode, int start, int end) throws SystemException {
		return findByG_S_C(groupId, accountStatus, cityCode, start, end, null);
	}

	/**
	 * Returns an ordered range of all the businesses where groupId = &#63; and accountStatus = &#63; and cityCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param cityCode the city code
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_S_C(long groupId, int accountStatus,
		String cityCode, int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S_C;
			finderArgs = new Object[] { groupId, accountStatus, cityCode };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_S_C;
			finderArgs = new Object[] {
					groupId, accountStatus, cityCode,
					
					start, end, orderByComparator
				};
		}

		List<Business> list = (List<Business>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Business business : list) {
				if ((groupId != business.getGroupId()) ||
						(accountStatus != business.getAccountStatus()) ||
						!Validator.equals(cityCode, business.getCityCode())) {
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

			query.append(_SQL_SELECT_BUSINESS_WHERE);

			query.append(_FINDER_COLUMN_G_S_C_GROUPID_2);

			query.append(_FINDER_COLUMN_G_S_C_ACCOUNTSTATUS_2);

			boolean bindCityCode = false;

			if (cityCode == null) {
				query.append(_FINDER_COLUMN_G_S_C_CITYCODE_1);
			}
			else if (cityCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_S_C_CITYCODE_3);
			}
			else {
				bindCityCode = true;

				query.append(_FINDER_COLUMN_G_S_C_CITYCODE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(BusinessModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(accountStatus);

				if (bindCityCode) {
					qPos.add(cityCode);
				}

				if (!pagination) {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Business>(list);
				}
				else {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end);
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
	 * Returns the first business in the ordered set where groupId = &#63; and accountStatus = &#63; and cityCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param cityCode the city code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByG_S_C_First(long groupId, int accountStatus,
		String cityCode, OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = fetchByG_S_C_First(groupId, accountStatus,
				cityCode, orderByComparator);

		if (business != null) {
			return business;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", accountStatus=");
		msg.append(accountStatus);

		msg.append(", cityCode=");
		msg.append(cityCode);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBusinessException(msg.toString());
	}

	/**
	 * Returns the first business in the ordered set where groupId = &#63; and accountStatus = &#63; and cityCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param cityCode the city code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching business, or <code>null</code> if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByG_S_C_First(long groupId, int accountStatus,
		String cityCode, OrderByComparator orderByComparator)
		throws SystemException {
		List<Business> list = findByG_S_C(groupId, accountStatus, cityCode, 0,
				1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last business in the ordered set where groupId = &#63; and accountStatus = &#63; and cityCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param cityCode the city code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByG_S_C_Last(long groupId, int accountStatus,
		String cityCode, OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = fetchByG_S_C_Last(groupId, accountStatus, cityCode,
				orderByComparator);

		if (business != null) {
			return business;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", accountStatus=");
		msg.append(accountStatus);

		msg.append(", cityCode=");
		msg.append(cityCode);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBusinessException(msg.toString());
	}

	/**
	 * Returns the last business in the ordered set where groupId = &#63; and accountStatus = &#63; and cityCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param cityCode the city code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching business, or <code>null</code> if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByG_S_C_Last(long groupId, int accountStatus,
		String cityCode, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByG_S_C(groupId, accountStatus, cityCode);

		if (count == 0) {
			return null;
		}

		List<Business> list = findByG_S_C(groupId, accountStatus, cityCode,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the businesses before and after the current business in the ordered set where groupId = &#63; and accountStatus = &#63; and cityCode = &#63;.
	 *
	 * @param businessId the primary key of the current business
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param cityCode the city code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a business with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business[] findByG_S_C_PrevAndNext(long businessId, long groupId,
		int accountStatus, String cityCode, OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = findByPrimaryKey(businessId);

		Session session = null;

		try {
			session = openSession();

			Business[] array = new BusinessImpl[3];

			array[0] = getByG_S_C_PrevAndNext(session, business, groupId,
					accountStatus, cityCode, orderByComparator, true);

			array[1] = business;

			array[2] = getByG_S_C_PrevAndNext(session, business, groupId,
					accountStatus, cityCode, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Business getByG_S_C_PrevAndNext(Session session,
		Business business, long groupId, int accountStatus, String cityCode,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_BUSINESS_WHERE);

		query.append(_FINDER_COLUMN_G_S_C_GROUPID_2);

		query.append(_FINDER_COLUMN_G_S_C_ACCOUNTSTATUS_2);

		boolean bindCityCode = false;

		if (cityCode == null) {
			query.append(_FINDER_COLUMN_G_S_C_CITYCODE_1);
		}
		else if (cityCode.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_G_S_C_CITYCODE_3);
		}
		else {
			bindCityCode = true;

			query.append(_FINDER_COLUMN_G_S_C_CITYCODE_2);
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
			query.append(BusinessModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		qPos.add(accountStatus);

		if (bindCityCode) {
			qPos.add(cityCode);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(business);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Business> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the businesses where groupId = &#63; and accountStatus = &#63; and cityCode = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param cityCode the city code
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByG_S_C(long groupId, int accountStatus, String cityCode)
		throws SystemException {
		for (Business business : findByG_S_C(groupId, accountStatus, cityCode,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(business);
		}
	}

	/**
	 * Returns the number of businesses where groupId = &#63; and accountStatus = &#63; and cityCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param cityCode the city code
	 * @return the number of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_S_C(long groupId, int accountStatus, String cityCode)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_S_C;

		Object[] finderArgs = new Object[] { groupId, accountStatus, cityCode };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_BUSINESS_WHERE);

			query.append(_FINDER_COLUMN_G_S_C_GROUPID_2);

			query.append(_FINDER_COLUMN_G_S_C_ACCOUNTSTATUS_2);

			boolean bindCityCode = false;

			if (cityCode == null) {
				query.append(_FINDER_COLUMN_G_S_C_CITYCODE_1);
			}
			else if (cityCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_S_C_CITYCODE_3);
			}
			else {
				bindCityCode = true;

				query.append(_FINDER_COLUMN_G_S_C_CITYCODE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(accountStatus);

				if (bindCityCode) {
					qPos.add(cityCode);
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

	private static final String _FINDER_COLUMN_G_S_C_GROUPID_2 = "business.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_S_C_ACCOUNTSTATUS_2 = "business.accountStatus = ? AND ";
	private static final String _FINDER_COLUMN_G_S_C_CITYCODE_1 = "business.cityCode IS NULL";
	private static final String _FINDER_COLUMN_G_S_C_CITYCODE_2 = "business.cityCode = ?";
	private static final String _FINDER_COLUMN_G_S_C_CITYCODE_3 = "(business.cityCode IS NULL OR business.cityCode = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_S_D = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_S_D",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S_D = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_S_D",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				String.class.getName()
			},
			BusinessModelImpl.GROUPID_COLUMN_BITMASK |
			BusinessModelImpl.ACCOUNTSTATUS_COLUMN_BITMASK |
			BusinessModelImpl.DISTRICTCODE_COLUMN_BITMASK |
			BusinessModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_S_D = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_S_D",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				String.class.getName()
			});

	/**
	 * Returns all the businesses where groupId = &#63; and accountStatus = &#63; and districtCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param districtCode the district code
	 * @return the matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_S_D(long groupId, int accountStatus,
		String districtCode) throws SystemException {
		return findByG_S_D(groupId, accountStatus, districtCode,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the businesses where groupId = &#63; and accountStatus = &#63; and districtCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param districtCode the district code
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @return the range of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_S_D(long groupId, int accountStatus,
		String districtCode, int start, int end) throws SystemException {
		return findByG_S_D(groupId, accountStatus, districtCode, start, end,
			null);
	}

	/**
	 * Returns an ordered range of all the businesses where groupId = &#63; and accountStatus = &#63; and districtCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param districtCode the district code
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_S_D(long groupId, int accountStatus,
		String districtCode, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S_D;
			finderArgs = new Object[] { groupId, accountStatus, districtCode };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_S_D;
			finderArgs = new Object[] {
					groupId, accountStatus, districtCode,
					
					start, end, orderByComparator
				};
		}

		List<Business> list = (List<Business>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Business business : list) {
				if ((groupId != business.getGroupId()) ||
						(accountStatus != business.getAccountStatus()) ||
						!Validator.equals(districtCode,
							business.getDistrictCode())) {
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

			query.append(_SQL_SELECT_BUSINESS_WHERE);

			query.append(_FINDER_COLUMN_G_S_D_GROUPID_2);

			query.append(_FINDER_COLUMN_G_S_D_ACCOUNTSTATUS_2);

			boolean bindDistrictCode = false;

			if (districtCode == null) {
				query.append(_FINDER_COLUMN_G_S_D_DISTRICTCODE_1);
			}
			else if (districtCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_S_D_DISTRICTCODE_3);
			}
			else {
				bindDistrictCode = true;

				query.append(_FINDER_COLUMN_G_S_D_DISTRICTCODE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(BusinessModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(accountStatus);

				if (bindDistrictCode) {
					qPos.add(districtCode);
				}

				if (!pagination) {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Business>(list);
				}
				else {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end);
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
	 * Returns the first business in the ordered set where groupId = &#63; and accountStatus = &#63; and districtCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param districtCode the district code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByG_S_D_First(long groupId, int accountStatus,
		String districtCode, OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = fetchByG_S_D_First(groupId, accountStatus,
				districtCode, orderByComparator);

		if (business != null) {
			return business;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", accountStatus=");
		msg.append(accountStatus);

		msg.append(", districtCode=");
		msg.append(districtCode);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBusinessException(msg.toString());
	}

	/**
	 * Returns the first business in the ordered set where groupId = &#63; and accountStatus = &#63; and districtCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param districtCode the district code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching business, or <code>null</code> if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByG_S_D_First(long groupId, int accountStatus,
		String districtCode, OrderByComparator orderByComparator)
		throws SystemException {
		List<Business> list = findByG_S_D(groupId, accountStatus, districtCode,
				0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last business in the ordered set where groupId = &#63; and accountStatus = &#63; and districtCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param districtCode the district code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByG_S_D_Last(long groupId, int accountStatus,
		String districtCode, OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = fetchByG_S_D_Last(groupId, accountStatus,
				districtCode, orderByComparator);

		if (business != null) {
			return business;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", accountStatus=");
		msg.append(accountStatus);

		msg.append(", districtCode=");
		msg.append(districtCode);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBusinessException(msg.toString());
	}

	/**
	 * Returns the last business in the ordered set where groupId = &#63; and accountStatus = &#63; and districtCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param districtCode the district code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching business, or <code>null</code> if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByG_S_D_Last(long groupId, int accountStatus,
		String districtCode, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByG_S_D(groupId, accountStatus, districtCode);

		if (count == 0) {
			return null;
		}

		List<Business> list = findByG_S_D(groupId, accountStatus, districtCode,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the businesses before and after the current business in the ordered set where groupId = &#63; and accountStatus = &#63; and districtCode = &#63;.
	 *
	 * @param businessId the primary key of the current business
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param districtCode the district code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a business with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business[] findByG_S_D_PrevAndNext(long businessId, long groupId,
		int accountStatus, String districtCode,
		OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = findByPrimaryKey(businessId);

		Session session = null;

		try {
			session = openSession();

			Business[] array = new BusinessImpl[3];

			array[0] = getByG_S_D_PrevAndNext(session, business, groupId,
					accountStatus, districtCode, orderByComparator, true);

			array[1] = business;

			array[2] = getByG_S_D_PrevAndNext(session, business, groupId,
					accountStatus, districtCode, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Business getByG_S_D_PrevAndNext(Session session,
		Business business, long groupId, int accountStatus,
		String districtCode, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_BUSINESS_WHERE);

		query.append(_FINDER_COLUMN_G_S_D_GROUPID_2);

		query.append(_FINDER_COLUMN_G_S_D_ACCOUNTSTATUS_2);

		boolean bindDistrictCode = false;

		if (districtCode == null) {
			query.append(_FINDER_COLUMN_G_S_D_DISTRICTCODE_1);
		}
		else if (districtCode.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_G_S_D_DISTRICTCODE_3);
		}
		else {
			bindDistrictCode = true;

			query.append(_FINDER_COLUMN_G_S_D_DISTRICTCODE_2);
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
			query.append(BusinessModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		qPos.add(accountStatus);

		if (bindDistrictCode) {
			qPos.add(districtCode);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(business);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Business> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the businesses where groupId = &#63; and accountStatus = &#63; and districtCode = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param districtCode the district code
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByG_S_D(long groupId, int accountStatus,
		String districtCode) throws SystemException {
		for (Business business : findByG_S_D(groupId, accountStatus,
				districtCode, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(business);
		}
	}

	/**
	 * Returns the number of businesses where groupId = &#63; and accountStatus = &#63; and districtCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param accountStatus the account status
	 * @param districtCode the district code
	 * @return the number of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_S_D(long groupId, int accountStatus, String districtCode)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_S_D;

		Object[] finderArgs = new Object[] { groupId, accountStatus, districtCode };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_BUSINESS_WHERE);

			query.append(_FINDER_COLUMN_G_S_D_GROUPID_2);

			query.append(_FINDER_COLUMN_G_S_D_ACCOUNTSTATUS_2);

			boolean bindDistrictCode = false;

			if (districtCode == null) {
				query.append(_FINDER_COLUMN_G_S_D_DISTRICTCODE_1);
			}
			else if (districtCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_S_D_DISTRICTCODE_3);
			}
			else {
				bindDistrictCode = true;

				query.append(_FINDER_COLUMN_G_S_D_DISTRICTCODE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(accountStatus);

				if (bindDistrictCode) {
					qPos.add(districtCode);
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

	private static final String _FINDER_COLUMN_G_S_D_GROUPID_2 = "business.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_S_D_ACCOUNTSTATUS_2 = "business.accountStatus = ? AND ";
	private static final String _FINDER_COLUMN_G_S_D_DISTRICTCODE_1 = "business.districtCode IS NULL";
	private static final String _FINDER_COLUMN_G_S_D_DISTRICTCODE_2 = "business.districtCode = ?";
	private static final String _FINDER_COLUMN_G_S_D_DISTRICTCODE_3 = "(business.districtCode IS NULL OR business.districtCode = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_T = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_T",
			new String[] {
				Long.class.getName(), String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_T = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, BusinessImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_T",
			new String[] { Long.class.getName(), String.class.getName() },
			BusinessModelImpl.GROUPID_COLUMN_BITMASK |
			BusinessModelImpl.BUSINESSTYPE_COLUMN_BITMASK |
			BusinessModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_T = new FinderPath(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_T",
			new String[] { Long.class.getName(), String.class.getName() });

	/**
	 * Returns all the businesses where groupId = &#63; and businessType = &#63;.
	 *
	 * @param groupId the group ID
	 * @param businessType the business type
	 * @return the matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_T(long groupId, String businessType)
		throws SystemException {
		return findByG_T(groupId, businessType, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the businesses where groupId = &#63; and businessType = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param businessType the business type
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @return the range of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_T(long groupId, String businessType,
		int start, int end) throws SystemException {
		return findByG_T(groupId, businessType, start, end, null);
	}

	/**
	 * Returns an ordered range of all the businesses where groupId = &#63; and businessType = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param businessType the business type
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findByG_T(long groupId, String businessType,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_T;
			finderArgs = new Object[] { groupId, businessType };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_T;
			finderArgs = new Object[] {
					groupId, businessType,
					
					start, end, orderByComparator
				};
		}

		List<Business> list = (List<Business>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Business business : list) {
				if ((groupId != business.getGroupId()) ||
						!Validator.equals(businessType,
							business.getBusinessType())) {
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

			query.append(_SQL_SELECT_BUSINESS_WHERE);

			query.append(_FINDER_COLUMN_G_T_GROUPID_2);

			boolean bindBusinessType = false;

			if (businessType == null) {
				query.append(_FINDER_COLUMN_G_T_BUSINESSTYPE_1);
			}
			else if (businessType.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_T_BUSINESSTYPE_3);
			}
			else {
				bindBusinessType = true;

				query.append(_FINDER_COLUMN_G_T_BUSINESSTYPE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(BusinessModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindBusinessType) {
					qPos.add(businessType);
				}

				if (!pagination) {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Business>(list);
				}
				else {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end);
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
	 * Returns the first business in the ordered set where groupId = &#63; and businessType = &#63;.
	 *
	 * @param groupId the group ID
	 * @param businessType the business type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByG_T_First(long groupId, String businessType,
		OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = fetchByG_T_First(groupId, businessType,
				orderByComparator);

		if (business != null) {
			return business;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", businessType=");
		msg.append(businessType);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBusinessException(msg.toString());
	}

	/**
	 * Returns the first business in the ordered set where groupId = &#63; and businessType = &#63;.
	 *
	 * @param groupId the group ID
	 * @param businessType the business type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching business, or <code>null</code> if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByG_T_First(long groupId, String businessType,
		OrderByComparator orderByComparator) throws SystemException {
		List<Business> list = findByG_T(groupId, businessType, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last business in the ordered set where groupId = &#63; and businessType = &#63;.
	 *
	 * @param groupId the group ID
	 * @param businessType the business type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByG_T_Last(long groupId, String businessType,
		OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = fetchByG_T_Last(groupId, businessType,
				orderByComparator);

		if (business != null) {
			return business;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", businessType=");
		msg.append(businessType);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBusinessException(msg.toString());
	}

	/**
	 * Returns the last business in the ordered set where groupId = &#63; and businessType = &#63;.
	 *
	 * @param groupId the group ID
	 * @param businessType the business type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching business, or <code>null</code> if a matching business could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByG_T_Last(long groupId, String businessType,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByG_T(groupId, businessType);

		if (count == 0) {
			return null;
		}

		List<Business> list = findByG_T(groupId, businessType, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the businesses before and after the current business in the ordered set where groupId = &#63; and businessType = &#63;.
	 *
	 * @param businessId the primary key of the current business
	 * @param groupId the group ID
	 * @param businessType the business type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a business with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business[] findByG_T_PrevAndNext(long businessId, long groupId,
		String businessType, OrderByComparator orderByComparator)
		throws NoSuchBusinessException, SystemException {
		Business business = findByPrimaryKey(businessId);

		Session session = null;

		try {
			session = openSession();

			Business[] array = new BusinessImpl[3];

			array[0] = getByG_T_PrevAndNext(session, business, groupId,
					businessType, orderByComparator, true);

			array[1] = business;

			array[2] = getByG_T_PrevAndNext(session, business, groupId,
					businessType, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Business getByG_T_PrevAndNext(Session session, Business business,
		long groupId, String businessType, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_BUSINESS_WHERE);

		query.append(_FINDER_COLUMN_G_T_GROUPID_2);

		boolean bindBusinessType = false;

		if (businessType == null) {
			query.append(_FINDER_COLUMN_G_T_BUSINESSTYPE_1);
		}
		else if (businessType.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_G_T_BUSINESSTYPE_3);
		}
		else {
			bindBusinessType = true;

			query.append(_FINDER_COLUMN_G_T_BUSINESSTYPE_2);
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
			query.append(BusinessModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (bindBusinessType) {
			qPos.add(businessType);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(business);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Business> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the businesses where groupId = &#63; and businessType = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param businessType the business type
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByG_T(long groupId, String businessType)
		throws SystemException {
		for (Business business : findByG_T(groupId, businessType,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(business);
		}
	}

	/**
	 * Returns the number of businesses where groupId = &#63; and businessType = &#63;.
	 *
	 * @param groupId the group ID
	 * @param businessType the business type
	 * @return the number of matching businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_T(long groupId, String businessType)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_T;

		Object[] finderArgs = new Object[] { groupId, businessType };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_BUSINESS_WHERE);

			query.append(_FINDER_COLUMN_G_T_GROUPID_2);

			boolean bindBusinessType = false;

			if (businessType == null) {
				query.append(_FINDER_COLUMN_G_T_BUSINESSTYPE_1);
			}
			else if (businessType.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_T_BUSINESSTYPE_3);
			}
			else {
				bindBusinessType = true;

				query.append(_FINDER_COLUMN_G_T_BUSINESSTYPE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindBusinessType) {
					qPos.add(businessType);
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

	private static final String _FINDER_COLUMN_G_T_GROUPID_2 = "business.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_T_BUSINESSTYPE_1 = "business.businessType IS NULL";
	private static final String _FINDER_COLUMN_G_T_BUSINESSTYPE_2 = "business.businessType = ?";
	private static final String _FINDER_COLUMN_G_T_BUSINESSTYPE_3 = "(business.businessType IS NULL OR business.businessType = '')";

	public BusinessPersistenceImpl() {
		setModelClass(Business.class);
	}

	/**
	 * Caches the business in the entity cache if it is enabled.
	 *
	 * @param business the business
	 */
	@Override
	public void cacheResult(Business business) {
		EntityCacheUtil.putResult(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessImpl.class, business.getPrimaryKey(), business);

		business.resetOriginalValues();
	}

	/**
	 * Caches the businesses in the entity cache if it is enabled.
	 *
	 * @param businesses the businesses
	 */
	@Override
	public void cacheResult(List<Business> businesses) {
		for (Business business : businesses) {
			if (EntityCacheUtil.getResult(
						BusinessModelImpl.ENTITY_CACHE_ENABLED,
						BusinessImpl.class, business.getPrimaryKey()) == null) {
				cacheResult(business);
			}
			else {
				business.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all businesses.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(BusinessImpl.class.getName());
		}

		EntityCacheUtil.clearCache(BusinessImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the business.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Business business) {
		EntityCacheUtil.removeResult(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessImpl.class, business.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<Business> businesses) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Business business : businesses) {
			EntityCacheUtil.removeResult(BusinessModelImpl.ENTITY_CACHE_ENABLED,
				BusinessImpl.class, business.getPrimaryKey());
		}
	}

	/**
	 * Creates a new business with the primary key. Does not add the business to the database.
	 *
	 * @param businessId the primary key for the new business
	 * @return the new business
	 */
	@Override
	public Business create(long businessId) {
		Business business = new BusinessImpl();

		business.setNew(true);
		business.setPrimaryKey(businessId);

		return business;
	}

	/**
	 * Removes the business with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param businessId the primary key of the business
	 * @return the business that was removed
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a business with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business remove(long businessId)
		throws NoSuchBusinessException, SystemException {
		return remove((Serializable)businessId);
	}

	/**
	 * Removes the business with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the business
	 * @return the business that was removed
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a business with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business remove(Serializable primaryKey)
		throws NoSuchBusinessException, SystemException {
		Session session = null;

		try {
			session = openSession();

			Business business = (Business)session.get(BusinessImpl.class,
					primaryKey);

			if (business == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchBusinessException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(business);
		}
		catch (NoSuchBusinessException nsee) {
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
	protected Business removeImpl(Business business) throws SystemException {
		business = toUnwrappedModel(business);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(business)) {
				business = (Business)session.get(BusinessImpl.class,
						business.getPrimaryKeyObj());
			}

			if (business != null) {
				session.delete(business);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (business != null) {
			clearCache(business);
		}

		return business;
	}

	@Override
	public Business updateImpl(org.opencps.accountmgt.model.Business business)
		throws SystemException {
		business = toUnwrappedModel(business);

		boolean isNew = business.isNew();

		BusinessModelImpl businessModelImpl = (BusinessModelImpl)business;

		Session session = null;

		try {
			session = openSession();

			if (business.isNew()) {
				session.save(business);

				business.setNew(false);
			}
			else {
				session.merge(business);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !BusinessModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((businessModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						businessModelImpl.getOriginalGroupId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);

				args = new Object[] { businessModelImpl.getGroupId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);
			}

			if ((businessModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						businessModelImpl.getOriginalGroupId(),
						businessModelImpl.getOriginalAccountStatus()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_S, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S,
					args);

				args = new Object[] {
						businessModelImpl.getGroupId(),
						businessModelImpl.getAccountStatus()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_S, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S,
					args);
			}

			if ((businessModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S_C.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						businessModelImpl.getOriginalGroupId(),
						businessModelImpl.getOriginalAccountStatus(),
						businessModelImpl.getOriginalCityCode()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_S_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S_C,
					args);

				args = new Object[] {
						businessModelImpl.getGroupId(),
						businessModelImpl.getAccountStatus(),
						businessModelImpl.getCityCode()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_S_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S_C,
					args);
			}

			if ((businessModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S_D.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						businessModelImpl.getOriginalGroupId(),
						businessModelImpl.getOriginalAccountStatus(),
						businessModelImpl.getOriginalDistrictCode()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_S_D, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S_D,
					args);

				args = new Object[] {
						businessModelImpl.getGroupId(),
						businessModelImpl.getAccountStatus(),
						businessModelImpl.getDistrictCode()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_S_D, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_S_D,
					args);
			}

			if ((businessModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_T.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						businessModelImpl.getOriginalGroupId(),
						businessModelImpl.getOriginalBusinessType()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_T, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_T,
					args);

				args = new Object[] {
						businessModelImpl.getGroupId(),
						businessModelImpl.getBusinessType()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_T, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_T,
					args);
			}
		}

		EntityCacheUtil.putResult(BusinessModelImpl.ENTITY_CACHE_ENABLED,
			BusinessImpl.class, business.getPrimaryKey(), business);

		return business;
	}

	protected Business toUnwrappedModel(Business business) {
		if (business instanceof BusinessImpl) {
			return business;
		}

		BusinessImpl businessImpl = new BusinessImpl();

		businessImpl.setNew(business.isNew());
		businessImpl.setPrimaryKey(business.getPrimaryKey());

		businessImpl.setBusinessId(business.getBusinessId());
		businessImpl.setCompanyId(business.getCompanyId());
		businessImpl.setGroupId(business.getGroupId());
		businessImpl.setUserId(business.getUserId());
		businessImpl.setCreateDate(business.getCreateDate());
		businessImpl.setModifiedDate(business.getModifiedDate());
		businessImpl.setUuid(business.getUuid());
		businessImpl.setName(business.getName());
		businessImpl.setEnName(business.getEnName());
		businessImpl.setShortName(business.getShortName());
		businessImpl.setBusinessType(business.getBusinessType());
		businessImpl.setIdNumber(business.getIdNumber());
		businessImpl.setAddress(business.getAddress());
		businessImpl.setCityCode(business.getCityCode());
		businessImpl.setDistrictCode(business.getDistrictCode());
		businessImpl.setWardCode(business.getWardCode());
		businessImpl.setTelNo(business.getTelNo());
		businessImpl.setEmail(business.getEmail());
		businessImpl.setRepresentativeName(business.getRepresentativeName());
		businessImpl.setRepresentativeRole(business.getRepresentativeRole());
		businessImpl.setAttachFile(business.getAttachFile());
		businessImpl.setMappingOrganizationId(business.getMappingOrganizationId());
		businessImpl.setMappingUserId(business.getMappingUserId());
		businessImpl.setAccountStatus(business.getAccountStatus());

		return businessImpl;
	}

	/**
	 * Returns the business with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the business
	 * @return the business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a business with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByPrimaryKey(Serializable primaryKey)
		throws NoSuchBusinessException, SystemException {
		Business business = fetchByPrimaryKey(primaryKey);

		if (business == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchBusinessException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return business;
	}

	/**
	 * Returns the business with the primary key or throws a {@link org.opencps.accountmgt.NoSuchBusinessException} if it could not be found.
	 *
	 * @param businessId the primary key of the business
	 * @return the business
	 * @throws org.opencps.accountmgt.NoSuchBusinessException if a business with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business findByPrimaryKey(long businessId)
		throws NoSuchBusinessException, SystemException {
		return findByPrimaryKey((Serializable)businessId);
	}

	/**
	 * Returns the business with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the business
	 * @return the business, or <code>null</code> if a business with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		Business business = (Business)EntityCacheUtil.getResult(BusinessModelImpl.ENTITY_CACHE_ENABLED,
				BusinessImpl.class, primaryKey);

		if (business == _nullBusiness) {
			return null;
		}

		if (business == null) {
			Session session = null;

			try {
				session = openSession();

				business = (Business)session.get(BusinessImpl.class, primaryKey);

				if (business != null) {
					cacheResult(business);
				}
				else {
					EntityCacheUtil.putResult(BusinessModelImpl.ENTITY_CACHE_ENABLED,
						BusinessImpl.class, primaryKey, _nullBusiness);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(BusinessModelImpl.ENTITY_CACHE_ENABLED,
					BusinessImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return business;
	}

	/**
	 * Returns the business with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param businessId the primary key of the business
	 * @return the business, or <code>null</code> if a business with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Business fetchByPrimaryKey(long businessId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)businessId);
	}

	/**
	 * Returns all the businesses.
	 *
	 * @return the businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the businesses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @return the range of businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findAll(int start, int end) throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the businesses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.accountmgt.model.impl.BusinessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of businesses
	 * @param end the upper bound of the range of businesses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of businesses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Business> findAll(int start, int end,
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

		List<Business> list = (List<Business>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_BUSINESS);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_BUSINESS;

				if (pagination) {
					sql = sql.concat(BusinessModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Business>(list);
				}
				else {
					list = (List<Business>)QueryUtil.list(q, getDialect(),
							start, end);
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
	 * Removes all the businesses from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (Business business : findAll()) {
			remove(business);
		}
	}

	/**
	 * Returns the number of businesses.
	 *
	 * @return the number of businesses
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

				Query q = session.createQuery(_SQL_COUNT_BUSINESS);

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

	@Override
	protected Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	/**
	 * Initializes the business persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.org.opencps.accountmgt.model.Business")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<Business>> listenersList = new ArrayList<ModelListener<Business>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<Business>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(BusinessImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_BUSINESS = "SELECT business FROM Business business";
	private static final String _SQL_SELECT_BUSINESS_WHERE = "SELECT business FROM Business business WHERE ";
	private static final String _SQL_COUNT_BUSINESS = "SELECT COUNT(business) FROM Business business";
	private static final String _SQL_COUNT_BUSINESS_WHERE = "SELECT COUNT(business) FROM Business business WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "business.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Business exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No Business exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(BusinessPersistenceImpl.class);
	private static Set<String> _badColumnNames = SetUtil.fromArray(new String[] {
				"uuid"
			});
	private static Business _nullBusiness = new BusinessImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<Business> toCacheModel() {
				return _nullBusinessCacheModel;
			}
		};

	private static CacheModel<Business> _nullBusinessCacheModel = new CacheModel<Business>() {
			@Override
			public Business toEntityModel() {
				return _nullBusiness;
			}
		};
}
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

package org.opencps.dossiermgt.service.persistence;

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
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
import com.liferay.portal.security.permission.InlineSQLHelperUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import org.opencps.dossiermgt.NoSuchDossierTemplateException;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.model.impl.DossierTemplateImpl;
import org.opencps.dossiermgt.model.impl.DossierTemplateModelImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the dossier template service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author trungnt
 * @see DossierTemplatePersistence
 * @see DossierTemplateUtil
 * @generated
 */
public class DossierTemplatePersistenceImpl extends BasePersistenceImpl<DossierTemplate>
	implements DossierTemplatePersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link DossierTemplateUtil} to access the dossier template persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = DossierTemplateImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
			DossierTemplateModelImpl.FINDER_CACHE_ENABLED,
			DossierTemplateImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
			DossierTemplateModelImpl.FINDER_CACHE_ENABLED,
			DossierTemplateImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
			DossierTemplateModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_TEMPLATENAME =
		new FinderPath(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
			DossierTemplateModelImpl.FINDER_CACHE_ENABLED,
			DossierTemplateImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByTemplateName",
			new String[] {
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TEMPLATENAME =
		new FinderPath(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
			DossierTemplateModelImpl.FINDER_CACHE_ENABLED,
			DossierTemplateImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByTemplateName",
			new String[] { String.class.getName() },
			DossierTemplateModelImpl.TEMPLATENAME_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_TEMPLATENAME = new FinderPath(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
			DossierTemplateModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByTemplateName",
			new String[] { String.class.getName() });

	/**
	 * Returns all the dossier templates where templateName = &#63;.
	 *
	 * @param templateName the template name
	 * @return the matching dossier templates
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierTemplate> findByTemplateName(String templateName)
		throws SystemException {
		return findByTemplateName(templateName, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the dossier templates where templateName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.dossiermgt.model.impl.DossierTemplateModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param templateName the template name
	 * @param start the lower bound of the range of dossier templates
	 * @param end the upper bound of the range of dossier templates (not inclusive)
	 * @return the range of matching dossier templates
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierTemplate> findByTemplateName(String templateName,
		int start, int end) throws SystemException {
		return findByTemplateName(templateName, start, end, null);
	}

	/**
	 * Returns an ordered range of all the dossier templates where templateName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.dossiermgt.model.impl.DossierTemplateModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param templateName the template name
	 * @param start the lower bound of the range of dossier templates
	 * @param end the upper bound of the range of dossier templates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching dossier templates
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierTemplate> findByTemplateName(String templateName,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TEMPLATENAME;
			finderArgs = new Object[] { templateName };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_TEMPLATENAME;
			finderArgs = new Object[] {
					templateName,
					
					start, end, orderByComparator
				};
		}

		List<DossierTemplate> list = (List<DossierTemplate>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DossierTemplate dossierTemplate : list) {
				if (!Validator.equals(templateName,
							dossierTemplate.getTemplateName())) {
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

			query.append(_SQL_SELECT_DOSSIERTEMPLATE_WHERE);

			boolean bindTemplateName = false;

			if (templateName == null) {
				query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_1);
			}
			else if (templateName.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_3);
			}
			else {
				bindTemplateName = true;

				query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DossierTemplateModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindTemplateName) {
					qPos.add(templateName);
				}

				if (!pagination) {
					list = (List<DossierTemplate>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<DossierTemplate>(list);
				}
				else {
					list = (List<DossierTemplate>)QueryUtil.list(q,
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
	 * Returns the first dossier template in the ordered set where templateName = &#63;.
	 *
	 * @param templateName the template name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dossier template
	 * @throws org.opencps.dossiermgt.NoSuchDossierTemplateException if a matching dossier template could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate findByTemplateName_First(String templateName,
		OrderByComparator orderByComparator)
		throws NoSuchDossierTemplateException, SystemException {
		DossierTemplate dossierTemplate = fetchByTemplateName_First(templateName,
				orderByComparator);

		if (dossierTemplate != null) {
			return dossierTemplate;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("templateName=");
		msg.append(templateName);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDossierTemplateException(msg.toString());
	}

	/**
	 * Returns the first dossier template in the ordered set where templateName = &#63;.
	 *
	 * @param templateName the template name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dossier template, or <code>null</code> if a matching dossier template could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate fetchByTemplateName_First(String templateName,
		OrderByComparator orderByComparator) throws SystemException {
		List<DossierTemplate> list = findByTemplateName(templateName, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last dossier template in the ordered set where templateName = &#63;.
	 *
	 * @param templateName the template name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dossier template
	 * @throws org.opencps.dossiermgt.NoSuchDossierTemplateException if a matching dossier template could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate findByTemplateName_Last(String templateName,
		OrderByComparator orderByComparator)
		throws NoSuchDossierTemplateException, SystemException {
		DossierTemplate dossierTemplate = fetchByTemplateName_Last(templateName,
				orderByComparator);

		if (dossierTemplate != null) {
			return dossierTemplate;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("templateName=");
		msg.append(templateName);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDossierTemplateException(msg.toString());
	}

	/**
	 * Returns the last dossier template in the ordered set where templateName = &#63;.
	 *
	 * @param templateName the template name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dossier template, or <code>null</code> if a matching dossier template could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate fetchByTemplateName_Last(String templateName,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByTemplateName(templateName);

		if (count == 0) {
			return null;
		}

		List<DossierTemplate> list = findByTemplateName(templateName,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the dossier templates before and after the current dossier template in the ordered set where templateName = &#63;.
	 *
	 * @param dossierTemplateId the primary key of the current dossier template
	 * @param templateName the template name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next dossier template
	 * @throws org.opencps.dossiermgt.NoSuchDossierTemplateException if a dossier template with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate[] findByTemplateName_PrevAndNext(
		long dossierTemplateId, String templateName,
		OrderByComparator orderByComparator)
		throws NoSuchDossierTemplateException, SystemException {
		DossierTemplate dossierTemplate = findByPrimaryKey(dossierTemplateId);

		Session session = null;

		try {
			session = openSession();

			DossierTemplate[] array = new DossierTemplateImpl[3];

			array[0] = getByTemplateName_PrevAndNext(session, dossierTemplate,
					templateName, orderByComparator, true);

			array[1] = dossierTemplate;

			array[2] = getByTemplateName_PrevAndNext(session, dossierTemplate,
					templateName, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DossierTemplate getByTemplateName_PrevAndNext(Session session,
		DossierTemplate dossierTemplate, String templateName,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DOSSIERTEMPLATE_WHERE);

		boolean bindTemplateName = false;

		if (templateName == null) {
			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_1);
		}
		else if (templateName.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_3);
		}
		else {
			bindTemplateName = true;

			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_2);
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
			query.append(DossierTemplateModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindTemplateName) {
			qPos.add(templateName);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dossierTemplate);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DossierTemplate> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the dossier templates that the user has permission to view where templateName = &#63;.
	 *
	 * @param templateName the template name
	 * @return the matching dossier templates that the user has permission to view
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierTemplate> filterFindByTemplateName(String templateName)
		throws SystemException {
		return filterFindByTemplateName(templateName, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the dossier templates that the user has permission to view where templateName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.dossiermgt.model.impl.DossierTemplateModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param templateName the template name
	 * @param start the lower bound of the range of dossier templates
	 * @param end the upper bound of the range of dossier templates (not inclusive)
	 * @return the range of matching dossier templates that the user has permission to view
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierTemplate> filterFindByTemplateName(String templateName,
		int start, int end) throws SystemException {
		return filterFindByTemplateName(templateName, start, end, null);
	}

	/**
	 * Returns an ordered range of all the dossier templates that the user has permissions to view where templateName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.dossiermgt.model.impl.DossierTemplateModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param templateName the template name
	 * @param start the lower bound of the range of dossier templates
	 * @param end the upper bound of the range of dossier templates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching dossier templates that the user has permission to view
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierTemplate> filterFindByTemplateName(String templateName,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		if (!InlineSQLHelperUtil.isEnabled()) {
			return findByTemplateName(templateName, start, end,
				orderByComparator);
		}

		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(3 +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		if (getDB().isSupportsInlineDistinct()) {
			query.append(_FILTER_SQL_SELECT_DOSSIERTEMPLATE_WHERE);
		}
		else {
			query.append(_FILTER_SQL_SELECT_DOSSIERTEMPLATE_NO_INLINE_DISTINCT_WHERE_1);
		}

		boolean bindTemplateName = false;

		if (templateName == null) {
			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_1);
		}
		else if (templateName.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_3);
		}
		else {
			bindTemplateName = true;

			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_2);
		}

		if (!getDB().isSupportsInlineDistinct()) {
			query.append(_FILTER_SQL_SELECT_DOSSIERTEMPLATE_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator, true);
			}
			else {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_TABLE,
					orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				query.append(DossierTemplateModelImpl.ORDER_BY_JPQL);
			}
			else {
				query.append(DossierTemplateModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(query.toString(),
				DossierTemplate.class.getName(),
				_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery q = session.createSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				q.addEntity(_FILTER_ENTITY_ALIAS, DossierTemplateImpl.class);
			}
			else {
				q.addEntity(_FILTER_ENTITY_TABLE, DossierTemplateImpl.class);
			}

			QueryPos qPos = QueryPos.getInstance(q);

			if (bindTemplateName) {
				qPos.add(templateName);
			}

			return (List<DossierTemplate>)QueryUtil.list(q, getDialect(),
				start, end);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the dossier templates before and after the current dossier template in the ordered set of dossier templates that the user has permission to view where templateName = &#63;.
	 *
	 * @param dossierTemplateId the primary key of the current dossier template
	 * @param templateName the template name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next dossier template
	 * @throws org.opencps.dossiermgt.NoSuchDossierTemplateException if a dossier template with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate[] filterFindByTemplateName_PrevAndNext(
		long dossierTemplateId, String templateName,
		OrderByComparator orderByComparator)
		throws NoSuchDossierTemplateException, SystemException {
		if (!InlineSQLHelperUtil.isEnabled()) {
			return findByTemplateName_PrevAndNext(dossierTemplateId,
				templateName, orderByComparator);
		}

		DossierTemplate dossierTemplate = findByPrimaryKey(dossierTemplateId);

		Session session = null;

		try {
			session = openSession();

			DossierTemplate[] array = new DossierTemplateImpl[3];

			array[0] = filterGetByTemplateName_PrevAndNext(session,
					dossierTemplate, templateName, orderByComparator, true);

			array[1] = dossierTemplate;

			array[2] = filterGetByTemplateName_PrevAndNext(session,
					dossierTemplate, templateName, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DossierTemplate filterGetByTemplateName_PrevAndNext(
		Session session, DossierTemplate dossierTemplate, String templateName,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		if (getDB().isSupportsInlineDistinct()) {
			query.append(_FILTER_SQL_SELECT_DOSSIERTEMPLATE_WHERE);
		}
		else {
			query.append(_FILTER_SQL_SELECT_DOSSIERTEMPLATE_NO_INLINE_DISTINCT_WHERE_1);
		}

		boolean bindTemplateName = false;

		if (templateName == null) {
			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_1);
		}
		else if (templateName.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_3);
		}
		else {
			bindTemplateName = true;

			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_2);
		}

		if (!getDB().isSupportsInlineDistinct()) {
			query.append(_FILTER_SQL_SELECT_DOSSIERTEMPLATE_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					query.append(_ORDER_BY_ENTITY_ALIAS);
				}
				else {
					query.append(_ORDER_BY_ENTITY_TABLE);
				}

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
				if (getDB().isSupportsInlineDistinct()) {
					query.append(_ORDER_BY_ENTITY_ALIAS);
				}
				else {
					query.append(_ORDER_BY_ENTITY_TABLE);
				}

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
			if (getDB().isSupportsInlineDistinct()) {
				query.append(DossierTemplateModelImpl.ORDER_BY_JPQL);
			}
			else {
				query.append(DossierTemplateModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(query.toString(),
				DossierTemplate.class.getName(),
				_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		SQLQuery q = session.createSQLQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			q.addEntity(_FILTER_ENTITY_ALIAS, DossierTemplateImpl.class);
		}
		else {
			q.addEntity(_FILTER_ENTITY_TABLE, DossierTemplateImpl.class);
		}

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindTemplateName) {
			qPos.add(templateName);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dossierTemplate);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DossierTemplate> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the dossier templates where templateName = &#63; from the database.
	 *
	 * @param templateName the template name
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByTemplateName(String templateName)
		throws SystemException {
		for (DossierTemplate dossierTemplate : findByTemplateName(
				templateName, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(dossierTemplate);
		}
	}

	/**
	 * Returns the number of dossier templates where templateName = &#63;.
	 *
	 * @param templateName the template name
	 * @return the number of matching dossier templates
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByTemplateName(String templateName)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_TEMPLATENAME;

		Object[] finderArgs = new Object[] { templateName };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_DOSSIERTEMPLATE_WHERE);

			boolean bindTemplateName = false;

			if (templateName == null) {
				query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_1);
			}
			else if (templateName.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_3);
			}
			else {
				bindTemplateName = true;

				query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindTemplateName) {
					qPos.add(templateName);
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
	 * Returns the number of dossier templates that the user has permission to view where templateName = &#63;.
	 *
	 * @param templateName the template name
	 * @return the number of matching dossier templates that the user has permission to view
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int filterCountByTemplateName(String templateName)
		throws SystemException {
		if (!InlineSQLHelperUtil.isEnabled()) {
			return countByTemplateName(templateName);
		}

		StringBundler query = new StringBundler(2);

		query.append(_FILTER_SQL_COUNT_DOSSIERTEMPLATE_WHERE);

		boolean bindTemplateName = false;

		if (templateName == null) {
			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_1);
		}
		else if (templateName.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_3);
		}
		else {
			bindTemplateName = true;

			query.append(_FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_2);
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(query.toString(),
				DossierTemplate.class.getName(),
				_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME,
				com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			if (bindTemplateName) {
				qPos.add(templateName);
			}

			Long count = (Long)q.uniqueResult();

			return count.intValue();
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_1 = "dossierTemplate.templateName IS NULL";
	private static final String _FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_2 = "dossierTemplate.templateName = ?";
	private static final String _FINDER_COLUMN_TEMPLATENAME_TEMPLATENAME_3 = "(dossierTemplate.templateName IS NULL OR dossierTemplate.templateName = '')";

	public DossierTemplatePersistenceImpl() {
		setModelClass(DossierTemplate.class);
	}

	/**
	 * Caches the dossier template in the entity cache if it is enabled.
	 *
	 * @param dossierTemplate the dossier template
	 */
	@Override
	public void cacheResult(DossierTemplate dossierTemplate) {
		EntityCacheUtil.putResult(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
			DossierTemplateImpl.class, dossierTemplate.getPrimaryKey(),
			dossierTemplate);

		dossierTemplate.resetOriginalValues();
	}

	/**
	 * Caches the dossier templates in the entity cache if it is enabled.
	 *
	 * @param dossierTemplates the dossier templates
	 */
	@Override
	public void cacheResult(List<DossierTemplate> dossierTemplates) {
		for (DossierTemplate dossierTemplate : dossierTemplates) {
			if (EntityCacheUtil.getResult(
						DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
						DossierTemplateImpl.class,
						dossierTemplate.getPrimaryKey()) == null) {
				cacheResult(dossierTemplate);
			}
			else {
				dossierTemplate.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all dossier templates.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(DossierTemplateImpl.class.getName());
		}

		EntityCacheUtil.clearCache(DossierTemplateImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the dossier template.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(DossierTemplate dossierTemplate) {
		EntityCacheUtil.removeResult(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
			DossierTemplateImpl.class, dossierTemplate.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<DossierTemplate> dossierTemplates) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (DossierTemplate dossierTemplate : dossierTemplates) {
			EntityCacheUtil.removeResult(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
				DossierTemplateImpl.class, dossierTemplate.getPrimaryKey());
		}
	}

	/**
	 * Creates a new dossier template with the primary key. Does not add the dossier template to the database.
	 *
	 * @param dossierTemplateId the primary key for the new dossier template
	 * @return the new dossier template
	 */
	@Override
	public DossierTemplate create(long dossierTemplateId) {
		DossierTemplate dossierTemplate = new DossierTemplateImpl();

		dossierTemplate.setNew(true);
		dossierTemplate.setPrimaryKey(dossierTemplateId);

		return dossierTemplate;
	}

	/**
	 * Removes the dossier template with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param dossierTemplateId the primary key of the dossier template
	 * @return the dossier template that was removed
	 * @throws org.opencps.dossiermgt.NoSuchDossierTemplateException if a dossier template with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate remove(long dossierTemplateId)
		throws NoSuchDossierTemplateException, SystemException {
		return remove((Serializable)dossierTemplateId);
	}

	/**
	 * Removes the dossier template with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the dossier template
	 * @return the dossier template that was removed
	 * @throws org.opencps.dossiermgt.NoSuchDossierTemplateException if a dossier template with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate remove(Serializable primaryKey)
		throws NoSuchDossierTemplateException, SystemException {
		Session session = null;

		try {
			session = openSession();

			DossierTemplate dossierTemplate = (DossierTemplate)session.get(DossierTemplateImpl.class,
					primaryKey);

			if (dossierTemplate == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchDossierTemplateException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(dossierTemplate);
		}
		catch (NoSuchDossierTemplateException nsee) {
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
	protected DossierTemplate removeImpl(DossierTemplate dossierTemplate)
		throws SystemException {
		dossierTemplate = toUnwrappedModel(dossierTemplate);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(dossierTemplate)) {
				dossierTemplate = (DossierTemplate)session.get(DossierTemplateImpl.class,
						dossierTemplate.getPrimaryKeyObj());
			}

			if (dossierTemplate != null) {
				session.delete(dossierTemplate);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (dossierTemplate != null) {
			clearCache(dossierTemplate);
		}

		return dossierTemplate;
	}

	@Override
	public DossierTemplate updateImpl(
		org.opencps.dossiermgt.model.DossierTemplate dossierTemplate)
		throws SystemException {
		dossierTemplate = toUnwrappedModel(dossierTemplate);

		boolean isNew = dossierTemplate.isNew();

		DossierTemplateModelImpl dossierTemplateModelImpl = (DossierTemplateModelImpl)dossierTemplate;

		Session session = null;

		try {
			session = openSession();

			if (dossierTemplate.isNew()) {
				session.save(dossierTemplate);

				dossierTemplate.setNew(false);
			}
			else {
				session.merge(dossierTemplate);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !DossierTemplateModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((dossierTemplateModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TEMPLATENAME.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dossierTemplateModelImpl.getOriginalTemplateName()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_TEMPLATENAME,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TEMPLATENAME,
					args);

				args = new Object[] { dossierTemplateModelImpl.getTemplateName() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_TEMPLATENAME,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TEMPLATENAME,
					args);
			}
		}

		EntityCacheUtil.putResult(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
			DossierTemplateImpl.class, dossierTemplate.getPrimaryKey(),
			dossierTemplate);

		return dossierTemplate;
	}

	protected DossierTemplate toUnwrappedModel(DossierTemplate dossierTemplate) {
		if (dossierTemplate instanceof DossierTemplateImpl) {
			return dossierTemplate;
		}

		DossierTemplateImpl dossierTemplateImpl = new DossierTemplateImpl();

		dossierTemplateImpl.setNew(dossierTemplate.isNew());
		dossierTemplateImpl.setPrimaryKey(dossierTemplate.getPrimaryKey());

		dossierTemplateImpl.setDossierTemplateId(dossierTemplate.getDossierTemplateId());
		dossierTemplateImpl.setTemplateName(dossierTemplate.getTemplateName());
		dossierTemplateImpl.setDescription(dossierTemplate.getDescription());
		dossierTemplateImpl.setTemplateNo(dossierTemplate.getTemplateNo());

		return dossierTemplateImpl;
	}

	/**
	 * Returns the dossier template with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the dossier template
	 * @return the dossier template
	 * @throws org.opencps.dossiermgt.NoSuchDossierTemplateException if a dossier template with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate findByPrimaryKey(Serializable primaryKey)
		throws NoSuchDossierTemplateException, SystemException {
		DossierTemplate dossierTemplate = fetchByPrimaryKey(primaryKey);

		if (dossierTemplate == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchDossierTemplateException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return dossierTemplate;
	}

	/**
	 * Returns the dossier template with the primary key or throws a {@link org.opencps.dossiermgt.NoSuchDossierTemplateException} if it could not be found.
	 *
	 * @param dossierTemplateId the primary key of the dossier template
	 * @return the dossier template
	 * @throws org.opencps.dossiermgt.NoSuchDossierTemplateException if a dossier template with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate findByPrimaryKey(long dossierTemplateId)
		throws NoSuchDossierTemplateException, SystemException {
		return findByPrimaryKey((Serializable)dossierTemplateId);
	}

	/**
	 * Returns the dossier template with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the dossier template
	 * @return the dossier template, or <code>null</code> if a dossier template with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		DossierTemplate dossierTemplate = (DossierTemplate)EntityCacheUtil.getResult(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
				DossierTemplateImpl.class, primaryKey);

		if (dossierTemplate == _nullDossierTemplate) {
			return null;
		}

		if (dossierTemplate == null) {
			Session session = null;

			try {
				session = openSession();

				dossierTemplate = (DossierTemplate)session.get(DossierTemplateImpl.class,
						primaryKey);

				if (dossierTemplate != null) {
					cacheResult(dossierTemplate);
				}
				else {
					EntityCacheUtil.putResult(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
						DossierTemplateImpl.class, primaryKey,
						_nullDossierTemplate);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(DossierTemplateModelImpl.ENTITY_CACHE_ENABLED,
					DossierTemplateImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return dossierTemplate;
	}

	/**
	 * Returns the dossier template with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param dossierTemplateId the primary key of the dossier template
	 * @return the dossier template, or <code>null</code> if a dossier template with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierTemplate fetchByPrimaryKey(long dossierTemplateId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)dossierTemplateId);
	}

	/**
	 * Returns all the dossier templates.
	 *
	 * @return the dossier templates
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierTemplate> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the dossier templates.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.dossiermgt.model.impl.DossierTemplateModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of dossier templates
	 * @param end the upper bound of the range of dossier templates (not inclusive)
	 * @return the range of dossier templates
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierTemplate> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the dossier templates.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.dossiermgt.model.impl.DossierTemplateModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of dossier templates
	 * @param end the upper bound of the range of dossier templates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of dossier templates
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierTemplate> findAll(int start, int end,
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

		List<DossierTemplate> list = (List<DossierTemplate>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_DOSSIERTEMPLATE);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_DOSSIERTEMPLATE;

				if (pagination) {
					sql = sql.concat(DossierTemplateModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<DossierTemplate>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<DossierTemplate>(list);
				}
				else {
					list = (List<DossierTemplate>)QueryUtil.list(q,
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
	 * Removes all the dossier templates from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (DossierTemplate dossierTemplate : findAll()) {
			remove(dossierTemplate);
		}
	}

	/**
	 * Returns the number of dossier templates.
	 *
	 * @return the number of dossier templates
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

				Query q = session.createQuery(_SQL_COUNT_DOSSIERTEMPLATE);

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
	 * Initializes the dossier template persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.org.opencps.dossiermgt.model.DossierTemplate")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<DossierTemplate>> listenersList = new ArrayList<ModelListener<DossierTemplate>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<DossierTemplate>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(DossierTemplateImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_DOSSIERTEMPLATE = "SELECT dossierTemplate FROM DossierTemplate dossierTemplate";
	private static final String _SQL_SELECT_DOSSIERTEMPLATE_WHERE = "SELECT dossierTemplate FROM DossierTemplate dossierTemplate WHERE ";
	private static final String _SQL_COUNT_DOSSIERTEMPLATE = "SELECT COUNT(dossierTemplate) FROM DossierTemplate dossierTemplate";
	private static final String _SQL_COUNT_DOSSIERTEMPLATE_WHERE = "SELECT COUNT(dossierTemplate) FROM DossierTemplate dossierTemplate WHERE ";
	private static final String _FILTER_ENTITY_TABLE_FILTER_PK_COLUMN = "dossierTemplate.dossierTemplateId";
	private static final String _FILTER_SQL_SELECT_DOSSIERTEMPLATE_WHERE = "SELECT DISTINCT {dossierTemplate.*} FROM opencps_dossiertemplate dossierTemplate WHERE ";
	private static final String _FILTER_SQL_SELECT_DOSSIERTEMPLATE_NO_INLINE_DISTINCT_WHERE_1 =
		"SELECT {opencps_dossiertemplate.*} FROM (SELECT DISTINCT dossierTemplate.dossierTemplateId FROM opencps_dossiertemplate dossierTemplate WHERE ";
	private static final String _FILTER_SQL_SELECT_DOSSIERTEMPLATE_NO_INLINE_DISTINCT_WHERE_2 =
		") TEMP_TABLE INNER JOIN opencps_dossiertemplate ON TEMP_TABLE.dossierTemplateId = opencps_dossiertemplate.dossierTemplateId";
	private static final String _FILTER_SQL_COUNT_DOSSIERTEMPLATE_WHERE = "SELECT COUNT(DISTINCT dossierTemplate.dossierTemplateId) AS COUNT_VALUE FROM opencps_dossiertemplate dossierTemplate WHERE ";
	private static final String _FILTER_ENTITY_ALIAS = "dossierTemplate";
	private static final String _FILTER_ENTITY_TABLE = "opencps_dossiertemplate";
	private static final String _ORDER_BY_ENTITY_ALIAS = "dossierTemplate.";
	private static final String _ORDER_BY_ENTITY_TABLE = "opencps_dossiertemplate.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No DossierTemplate exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No DossierTemplate exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(DossierTemplatePersistenceImpl.class);
	private static DossierTemplate _nullDossierTemplate = new DossierTemplateImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<DossierTemplate> toCacheModel() {
				return _nullDossierTemplateCacheModel;
			}
		};

	private static CacheModel<DossierTemplate> _nullDossierTemplateCacheModel = new CacheModel<DossierTemplate>() {
			@Override
			public DossierTemplate toEntityModel() {
				return _nullDossierTemplate;
			}
		};
}
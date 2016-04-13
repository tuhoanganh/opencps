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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnmodifiableList;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import org.opencps.dossiermgt.NoSuchDossierPartException;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.impl.DossierPartImpl;
import org.opencps.dossiermgt.model.impl.DossierPartModelImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the dossier part service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author trungnt
 * @see DossierPartPersistence
 * @see DossierPartUtil
 * @generated
 */
public class DossierPartPersistenceImpl extends BasePersistenceImpl<DossierPart>
	implements DossierPartPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link DossierPartUtil} to access the dossier part persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = DossierPartImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(DossierPartModelImpl.ENTITY_CACHE_ENABLED,
			DossierPartModelImpl.FINDER_CACHE_ENABLED, DossierPartImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(DossierPartModelImpl.ENTITY_CACHE_ENABLED,
			DossierPartModelImpl.FINDER_CACHE_ENABLED, DossierPartImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(DossierPartModelImpl.ENTITY_CACHE_ENABLED,
			DossierPartModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	public DossierPartPersistenceImpl() {
		setModelClass(DossierPart.class);
	}

	/**
	 * Caches the dossier part in the entity cache if it is enabled.
	 *
	 * @param dossierPart the dossier part
	 */
	@Override
	public void cacheResult(DossierPart dossierPart) {
		EntityCacheUtil.putResult(DossierPartModelImpl.ENTITY_CACHE_ENABLED,
			DossierPartImpl.class, dossierPart.getPrimaryKey(), dossierPart);

		dossierPart.resetOriginalValues();
	}

	/**
	 * Caches the dossier parts in the entity cache if it is enabled.
	 *
	 * @param dossierParts the dossier parts
	 */
	@Override
	public void cacheResult(List<DossierPart> dossierParts) {
		for (DossierPart dossierPart : dossierParts) {
			if (EntityCacheUtil.getResult(
						DossierPartModelImpl.ENTITY_CACHE_ENABLED,
						DossierPartImpl.class, dossierPart.getPrimaryKey()) == null) {
				cacheResult(dossierPart);
			}
			else {
				dossierPart.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all dossier parts.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(DossierPartImpl.class.getName());
		}

		EntityCacheUtil.clearCache(DossierPartImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the dossier part.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(DossierPart dossierPart) {
		EntityCacheUtil.removeResult(DossierPartModelImpl.ENTITY_CACHE_ENABLED,
			DossierPartImpl.class, dossierPart.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<DossierPart> dossierParts) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (DossierPart dossierPart : dossierParts) {
			EntityCacheUtil.removeResult(DossierPartModelImpl.ENTITY_CACHE_ENABLED,
				DossierPartImpl.class, dossierPart.getPrimaryKey());
		}
	}

	/**
	 * Creates a new dossier part with the primary key. Does not add the dossier part to the database.
	 *
	 * @param dossierpartId the primary key for the new dossier part
	 * @return the new dossier part
	 */
	@Override
	public DossierPart create(long dossierpartId) {
		DossierPart dossierPart = new DossierPartImpl();

		dossierPart.setNew(true);
		dossierPart.setPrimaryKey(dossierpartId);

		return dossierPart;
	}

	/**
	 * Removes the dossier part with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param dossierpartId the primary key of the dossier part
	 * @return the dossier part that was removed
	 * @throws org.opencps.dossiermgt.NoSuchDossierPartException if a dossier part with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierPart remove(long dossierpartId)
		throws NoSuchDossierPartException, SystemException {
		return remove((Serializable)dossierpartId);
	}

	/**
	 * Removes the dossier part with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the dossier part
	 * @return the dossier part that was removed
	 * @throws org.opencps.dossiermgt.NoSuchDossierPartException if a dossier part with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierPart remove(Serializable primaryKey)
		throws NoSuchDossierPartException, SystemException {
		Session session = null;

		try {
			session = openSession();

			DossierPart dossierPart = (DossierPart)session.get(DossierPartImpl.class,
					primaryKey);

			if (dossierPart == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchDossierPartException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(dossierPart);
		}
		catch (NoSuchDossierPartException nsee) {
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
	protected DossierPart removeImpl(DossierPart dossierPart)
		throws SystemException {
		dossierPart = toUnwrappedModel(dossierPart);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(dossierPart)) {
				dossierPart = (DossierPart)session.get(DossierPartImpl.class,
						dossierPart.getPrimaryKeyObj());
			}

			if (dossierPart != null) {
				session.delete(dossierPart);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (dossierPart != null) {
			clearCache(dossierPart);
		}

		return dossierPart;
	}

	@Override
	public DossierPart updateImpl(
		org.opencps.dossiermgt.model.DossierPart dossierPart)
		throws SystemException {
		dossierPart = toUnwrappedModel(dossierPart);

		boolean isNew = dossierPart.isNew();

		Session session = null;

		try {
			session = openSession();

			if (dossierPart.isNew()) {
				session.save(dossierPart);

				dossierPart.setNew(false);
			}
			else {
				session.merge(dossierPart);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		EntityCacheUtil.putResult(DossierPartModelImpl.ENTITY_CACHE_ENABLED,
			DossierPartImpl.class, dossierPart.getPrimaryKey(), dossierPart);

		return dossierPart;
	}

	protected DossierPart toUnwrappedModel(DossierPart dossierPart) {
		if (dossierPart instanceof DossierPartImpl) {
			return dossierPart;
		}

		DossierPartImpl dossierPartImpl = new DossierPartImpl();

		dossierPartImpl.setNew(dossierPart.isNew());
		dossierPartImpl.setPrimaryKey(dossierPart.getPrimaryKey());

		dossierPartImpl.setDossierpartId(dossierPart.getDossierpartId());
		dossierPartImpl.setDossierTemplateId(dossierPart.getDossierTemplateId());
		dossierPartImpl.setPartNo(dossierPart.getPartNo());
		dossierPartImpl.setPartName(dossierPart.getPartName());
		dossierPartImpl.setPartTip(dossierPart.getPartTip());
		dossierPartImpl.setPartType(dossierPart.getPartType());
		dossierPartImpl.setParentId(dossierPart.getParentId());
		dossierPartImpl.setSibling(dossierPart.getSibling());
		dossierPartImpl.setTreeIndex(dossierPart.getTreeIndex());
		dossierPartImpl.setFormScript(dossierPart.getFormScript());
		dossierPartImpl.setSampleData(dossierPart.getSampleData());
		dossierPartImpl.setRequired(dossierPart.isRequired());
		dossierPartImpl.setTemplateFileNo(dossierPart.getTemplateFileNo());

		return dossierPartImpl;
	}

	/**
	 * Returns the dossier part with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the dossier part
	 * @return the dossier part
	 * @throws org.opencps.dossiermgt.NoSuchDossierPartException if a dossier part with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierPart findByPrimaryKey(Serializable primaryKey)
		throws NoSuchDossierPartException, SystemException {
		DossierPart dossierPart = fetchByPrimaryKey(primaryKey);

		if (dossierPart == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchDossierPartException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return dossierPart;
	}

	/**
	 * Returns the dossier part with the primary key or throws a {@link org.opencps.dossiermgt.NoSuchDossierPartException} if it could not be found.
	 *
	 * @param dossierpartId the primary key of the dossier part
	 * @return the dossier part
	 * @throws org.opencps.dossiermgt.NoSuchDossierPartException if a dossier part with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierPart findByPrimaryKey(long dossierpartId)
		throws NoSuchDossierPartException, SystemException {
		return findByPrimaryKey((Serializable)dossierpartId);
	}

	/**
	 * Returns the dossier part with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the dossier part
	 * @return the dossier part, or <code>null</code> if a dossier part with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierPart fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		DossierPart dossierPart = (DossierPart)EntityCacheUtil.getResult(DossierPartModelImpl.ENTITY_CACHE_ENABLED,
				DossierPartImpl.class, primaryKey);

		if (dossierPart == _nullDossierPart) {
			return null;
		}

		if (dossierPart == null) {
			Session session = null;

			try {
				session = openSession();

				dossierPart = (DossierPart)session.get(DossierPartImpl.class,
						primaryKey);

				if (dossierPart != null) {
					cacheResult(dossierPart);
				}
				else {
					EntityCacheUtil.putResult(DossierPartModelImpl.ENTITY_CACHE_ENABLED,
						DossierPartImpl.class, primaryKey, _nullDossierPart);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(DossierPartModelImpl.ENTITY_CACHE_ENABLED,
					DossierPartImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return dossierPart;
	}

	/**
	 * Returns the dossier part with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param dossierpartId the primary key of the dossier part
	 * @return the dossier part, or <code>null</code> if a dossier part with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DossierPart fetchByPrimaryKey(long dossierpartId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)dossierpartId);
	}

	/**
	 * Returns all the dossier parts.
	 *
	 * @return the dossier parts
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierPart> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the dossier parts.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.dossiermgt.model.impl.DossierPartModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of dossier parts
	 * @param end the upper bound of the range of dossier parts (not inclusive)
	 * @return the range of dossier parts
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierPart> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the dossier parts.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.dossiermgt.model.impl.DossierPartModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of dossier parts
	 * @param end the upper bound of the range of dossier parts (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of dossier parts
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DossierPart> findAll(int start, int end,
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

		List<DossierPart> list = (List<DossierPart>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_DOSSIERPART);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_DOSSIERPART;

				if (pagination) {
					sql = sql.concat(DossierPartModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<DossierPart>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<DossierPart>(list);
				}
				else {
					list = (List<DossierPart>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the dossier parts from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (DossierPart dossierPart : findAll()) {
			remove(dossierPart);
		}
	}

	/**
	 * Returns the number of dossier parts.
	 *
	 * @return the number of dossier parts
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

				Query q = session.createQuery(_SQL_COUNT_DOSSIERPART);

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
	 * Initializes the dossier part persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.org.opencps.dossiermgt.model.DossierPart")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<DossierPart>> listenersList = new ArrayList<ModelListener<DossierPart>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<DossierPart>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(DossierPartImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_DOSSIERPART = "SELECT dossierPart FROM DossierPart dossierPart";
	private static final String _SQL_COUNT_DOSSIERPART = "SELECT COUNT(dossierPart) FROM DossierPart dossierPart";
	private static final String _ORDER_BY_ENTITY_ALIAS = "dossierPart.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No DossierPart exists with the primary key ";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(DossierPartPersistenceImpl.class);
	private static DossierPart _nullDossierPart = new DossierPartImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<DossierPart> toCacheModel() {
				return _nullDossierPartCacheModel;
			}
		};

	private static CacheModel<DossierPart> _nullDossierPartCacheModel = new CacheModel<DossierPart>() {
			@Override
			public DossierPart toEntityModel() {
				return _nullDossierPart;
			}
		};
}
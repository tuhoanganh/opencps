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

package org.opencps.servicemgt.service.persistence;

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

import org.opencps.servicemgt.NoSuchTemplateFileException;
import org.opencps.servicemgt.model.TemplateFile;
import org.opencps.servicemgt.model.impl.TemplateFileImpl;
import org.opencps.servicemgt.model.impl.TemplateFileModelImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the template file service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author khoavd
 * @see TemplateFilePersistence
 * @see TemplateFileUtil
 * @generated
 */
public class TemplateFilePersistenceImpl extends BasePersistenceImpl<TemplateFile>
	implements TemplateFilePersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link TemplateFileUtil} to access the template file persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = TemplateFileImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(TemplateFileModelImpl.ENTITY_CACHE_ENABLED,
			TemplateFileModelImpl.FINDER_CACHE_ENABLED, TemplateFileImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(TemplateFileModelImpl.ENTITY_CACHE_ENABLED,
			TemplateFileModelImpl.FINDER_CACHE_ENABLED, TemplateFileImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(TemplateFileModelImpl.ENTITY_CACHE_ENABLED,
			TemplateFileModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	public TemplateFilePersistenceImpl() {
		setModelClass(TemplateFile.class);
	}

	/**
	 * Caches the template file in the entity cache if it is enabled.
	 *
	 * @param templateFile the template file
	 */
	@Override
	public void cacheResult(TemplateFile templateFile) {
		EntityCacheUtil.putResult(TemplateFileModelImpl.ENTITY_CACHE_ENABLED,
			TemplateFileImpl.class, templateFile.getPrimaryKey(), templateFile);

		templateFile.resetOriginalValues();
	}

	/**
	 * Caches the template files in the entity cache if it is enabled.
	 *
	 * @param templateFiles the template files
	 */
	@Override
	public void cacheResult(List<TemplateFile> templateFiles) {
		for (TemplateFile templateFile : templateFiles) {
			if (EntityCacheUtil.getResult(
						TemplateFileModelImpl.ENTITY_CACHE_ENABLED,
						TemplateFileImpl.class, templateFile.getPrimaryKey()) == null) {
				cacheResult(templateFile);
			}
			else {
				templateFile.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all template files.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(TemplateFileImpl.class.getName());
		}

		EntityCacheUtil.clearCache(TemplateFileImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the template file.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(TemplateFile templateFile) {
		EntityCacheUtil.removeResult(TemplateFileModelImpl.ENTITY_CACHE_ENABLED,
			TemplateFileImpl.class, templateFile.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<TemplateFile> templateFiles) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (TemplateFile templateFile : templateFiles) {
			EntityCacheUtil.removeResult(TemplateFileModelImpl.ENTITY_CACHE_ENABLED,
				TemplateFileImpl.class, templateFile.getPrimaryKey());
		}
	}

	/**
	 * Creates a new template file with the primary key. Does not add the template file to the database.
	 *
	 * @param templatefileId the primary key for the new template file
	 * @return the new template file
	 */
	@Override
	public TemplateFile create(long templatefileId) {
		TemplateFile templateFile = new TemplateFileImpl();

		templateFile.setNew(true);
		templateFile.setPrimaryKey(templatefileId);

		return templateFile;
	}

	/**
	 * Removes the template file with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param templatefileId the primary key of the template file
	 * @return the template file that was removed
	 * @throws org.opencps.servicemgt.NoSuchTemplateFileException if a template file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public TemplateFile remove(long templatefileId)
		throws NoSuchTemplateFileException, SystemException {
		return remove((Serializable)templatefileId);
	}

	/**
	 * Removes the template file with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the template file
	 * @return the template file that was removed
	 * @throws org.opencps.servicemgt.NoSuchTemplateFileException if a template file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public TemplateFile remove(Serializable primaryKey)
		throws NoSuchTemplateFileException, SystemException {
		Session session = null;

		try {
			session = openSession();

			TemplateFile templateFile = (TemplateFile)session.get(TemplateFileImpl.class,
					primaryKey);

			if (templateFile == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchTemplateFileException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(templateFile);
		}
		catch (NoSuchTemplateFileException nsee) {
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
	protected TemplateFile removeImpl(TemplateFile templateFile)
		throws SystemException {
		templateFile = toUnwrappedModel(templateFile);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(templateFile)) {
				templateFile = (TemplateFile)session.get(TemplateFileImpl.class,
						templateFile.getPrimaryKeyObj());
			}

			if (templateFile != null) {
				session.delete(templateFile);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (templateFile != null) {
			clearCache(templateFile);
		}

		return templateFile;
	}

	@Override
	public TemplateFile updateImpl(
		org.opencps.servicemgt.model.TemplateFile templateFile)
		throws SystemException {
		templateFile = toUnwrappedModel(templateFile);

		boolean isNew = templateFile.isNew();

		Session session = null;

		try {
			session = openSession();

			if (templateFile.isNew()) {
				session.save(templateFile);

				templateFile.setNew(false);
			}
			else {
				session.merge(templateFile);
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

		EntityCacheUtil.putResult(TemplateFileModelImpl.ENTITY_CACHE_ENABLED,
			TemplateFileImpl.class, templateFile.getPrimaryKey(), templateFile);

		return templateFile;
	}

	protected TemplateFile toUnwrappedModel(TemplateFile templateFile) {
		if (templateFile instanceof TemplateFileImpl) {
			return templateFile;
		}

		TemplateFileImpl templateFileImpl = new TemplateFileImpl();

		templateFileImpl.setNew(templateFile.isNew());
		templateFileImpl.setPrimaryKey(templateFile.getPrimaryKey());

		templateFileImpl.setTemplatefileId(templateFile.getTemplatefileId());
		templateFileImpl.setCompanyId(templateFile.getCompanyId());
		templateFileImpl.setGroupId(templateFile.getGroupId());
		templateFileImpl.setUserId(templateFile.getUserId());
		templateFileImpl.setCreateDate(templateFile.getCreateDate());
		templateFileImpl.setModifiedDate(templateFile.getModifiedDate());
		templateFileImpl.setFileName(templateFile.getFileName());
		templateFileImpl.setFileNo(templateFile.getFileNo());
		templateFileImpl.setFileEntryId(templateFile.getFileEntryId());

		return templateFileImpl;
	}

	/**
	 * Returns the template file with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the template file
	 * @return the template file
	 * @throws org.opencps.servicemgt.NoSuchTemplateFileException if a template file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public TemplateFile findByPrimaryKey(Serializable primaryKey)
		throws NoSuchTemplateFileException, SystemException {
		TemplateFile templateFile = fetchByPrimaryKey(primaryKey);

		if (templateFile == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchTemplateFileException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return templateFile;
	}

	/**
	 * Returns the template file with the primary key or throws a {@link org.opencps.servicemgt.NoSuchTemplateFileException} if it could not be found.
	 *
	 * @param templatefileId the primary key of the template file
	 * @return the template file
	 * @throws org.opencps.servicemgt.NoSuchTemplateFileException if a template file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public TemplateFile findByPrimaryKey(long templatefileId)
		throws NoSuchTemplateFileException, SystemException {
		return findByPrimaryKey((Serializable)templatefileId);
	}

	/**
	 * Returns the template file with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the template file
	 * @return the template file, or <code>null</code> if a template file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public TemplateFile fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		TemplateFile templateFile = (TemplateFile)EntityCacheUtil.getResult(TemplateFileModelImpl.ENTITY_CACHE_ENABLED,
				TemplateFileImpl.class, primaryKey);

		if (templateFile == _nullTemplateFile) {
			return null;
		}

		if (templateFile == null) {
			Session session = null;

			try {
				session = openSession();

				templateFile = (TemplateFile)session.get(TemplateFileImpl.class,
						primaryKey);

				if (templateFile != null) {
					cacheResult(templateFile);
				}
				else {
					EntityCacheUtil.putResult(TemplateFileModelImpl.ENTITY_CACHE_ENABLED,
						TemplateFileImpl.class, primaryKey, _nullTemplateFile);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(TemplateFileModelImpl.ENTITY_CACHE_ENABLED,
					TemplateFileImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return templateFile;
	}

	/**
	 * Returns the template file with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param templatefileId the primary key of the template file
	 * @return the template file, or <code>null</code> if a template file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public TemplateFile fetchByPrimaryKey(long templatefileId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)templatefileId);
	}

	/**
	 * Returns all the template files.
	 *
	 * @return the template files
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<TemplateFile> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the template files.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.servicemgt.model.impl.TemplateFileModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of template files
	 * @param end the upper bound of the range of template files (not inclusive)
	 * @return the range of template files
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<TemplateFile> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the template files.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.servicemgt.model.impl.TemplateFileModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of template files
	 * @param end the upper bound of the range of template files (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of template files
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<TemplateFile> findAll(int start, int end,
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

		List<TemplateFile> list = (List<TemplateFile>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_TEMPLATEFILE);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_TEMPLATEFILE;

				if (pagination) {
					sql = sql.concat(TemplateFileModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<TemplateFile>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<TemplateFile>(list);
				}
				else {
					list = (List<TemplateFile>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the template files from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (TemplateFile templateFile : findAll()) {
			remove(templateFile);
		}
	}

	/**
	 * Returns the number of template files.
	 *
	 * @return the number of template files
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

				Query q = session.createQuery(_SQL_COUNT_TEMPLATEFILE);

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
	 * Initializes the template file persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.org.opencps.servicemgt.model.TemplateFile")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<TemplateFile>> listenersList = new ArrayList<ModelListener<TemplateFile>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<TemplateFile>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(TemplateFileImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_TEMPLATEFILE = "SELECT templateFile FROM TemplateFile templateFile";
	private static final String _SQL_COUNT_TEMPLATEFILE = "SELECT COUNT(templateFile) FROM TemplateFile templateFile";
	private static final String _ORDER_BY_ENTITY_ALIAS = "templateFile.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No TemplateFile exists with the primary key ";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(TemplateFilePersistenceImpl.class);
	private static TemplateFile _nullTemplateFile = new TemplateFileImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<TemplateFile> toCacheModel() {
				return _nullTemplateFileCacheModel;
			}
		};

	private static CacheModel<TemplateFile> _nullTemplateFileCacheModel = new CacheModel<TemplateFile>() {
			@Override
			public TemplateFile toEntityModel() {
				return _nullTemplateFile;
			}
		};
}
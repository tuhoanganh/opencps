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

package org.opencps.paymentmgt.service.persistence;

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

import org.opencps.paymentmgt.NoSuchPaymentFileException;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.model.impl.PaymentFileImpl;
import org.opencps.paymentmgt.model.impl.PaymentFileModelImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The persistence implementation for the Payment file service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author trungdk
 * @see PaymentFilePersistence
 * @see PaymentFileUtil
 * @generated
 */
public class PaymentFilePersistenceImpl extends BasePersistenceImpl<PaymentFile>
	implements PaymentFilePersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link PaymentFileUtil} to access the Payment file persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = PaymentFileImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
			PaymentFileModelImpl.FINDER_CACHE_ENABLED, PaymentFileImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
			PaymentFileModelImpl.FINDER_CACHE_ENABLED, PaymentFileImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
			PaymentFileModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_FETCH_BY_GOODCODE = new FinderPath(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
			PaymentFileModelImpl.FINDER_CACHE_ENABLED, PaymentFileImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByGoodCode",
			new String[] { Long.class.getName(), String.class.getName() },
			PaymentFileModelImpl.GROUPID_COLUMN_BITMASK |
			PaymentFileModelImpl.KEYPAYGOODCODE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_GOODCODE = new FinderPath(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
			PaymentFileModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGoodCode",
			new String[] { Long.class.getName(), String.class.getName() });

	/**
	 * Returns the Payment file where groupId = &#63; and keypayGoodCode = &#63; or throws a {@link org.opencps.paymentmgt.NoSuchPaymentFileException} if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param keypayGoodCode the keypay good code
	 * @return the matching Payment file
	 * @throws org.opencps.paymentmgt.NoSuchPaymentFileException if a matching Payment file could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentFile findByGoodCode(long groupId, String keypayGoodCode)
		throws NoSuchPaymentFileException, SystemException {
		PaymentFile paymentFile = fetchByGoodCode(groupId, keypayGoodCode);

		if (paymentFile == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("groupId=");
			msg.append(groupId);

			msg.append(", keypayGoodCode=");
			msg.append(keypayGoodCode);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchPaymentFileException(msg.toString());
		}

		return paymentFile;
	}

	/**
	 * Returns the Payment file where groupId = &#63; and keypayGoodCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param keypayGoodCode the keypay good code
	 * @return the matching Payment file, or <code>null</code> if a matching Payment file could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentFile fetchByGoodCode(long groupId, String keypayGoodCode)
		throws SystemException {
		return fetchByGoodCode(groupId, keypayGoodCode, true);
	}

	/**
	 * Returns the Payment file where groupId = &#63; and keypayGoodCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param keypayGoodCode the keypay good code
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching Payment file, or <code>null</code> if a matching Payment file could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentFile fetchByGoodCode(long groupId, String keypayGoodCode,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { groupId, keypayGoodCode };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_GOODCODE,
					finderArgs, this);
		}

		if (result instanceof PaymentFile) {
			PaymentFile paymentFile = (PaymentFile)result;

			if ((groupId != paymentFile.getGroupId()) ||
					!Validator.equals(keypayGoodCode,
						paymentFile.getKeypayGoodCode())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_PAYMENTFILE_WHERE);

			query.append(_FINDER_COLUMN_GOODCODE_GROUPID_2);

			boolean bindKeypayGoodCode = false;

			if (keypayGoodCode == null) {
				query.append(_FINDER_COLUMN_GOODCODE_KEYPAYGOODCODE_1);
			}
			else if (keypayGoodCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GOODCODE_KEYPAYGOODCODE_3);
			}
			else {
				bindKeypayGoodCode = true;

				query.append(_FINDER_COLUMN_GOODCODE_KEYPAYGOODCODE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindKeypayGoodCode) {
					qPos.add(keypayGoodCode);
				}

				List<PaymentFile> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_GOODCODE,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"PaymentFilePersistenceImpl.fetchByGoodCode(long, String, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					PaymentFile paymentFile = list.get(0);

					result = paymentFile;

					cacheResult(paymentFile);

					if ((paymentFile.getGroupId() != groupId) ||
							(paymentFile.getKeypayGoodCode() == null) ||
							!paymentFile.getKeypayGoodCode()
											.equals(keypayGoodCode)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_GOODCODE,
							finderArgs, paymentFile);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_GOODCODE,
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
			return (PaymentFile)result;
		}
	}

	/**
	 * Removes the Payment file where groupId = &#63; and keypayGoodCode = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param keypayGoodCode the keypay good code
	 * @return the Payment file that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentFile removeByGoodCode(long groupId, String keypayGoodCode)
		throws NoSuchPaymentFileException, SystemException {
		PaymentFile paymentFile = findByGoodCode(groupId, keypayGoodCode);

		return remove(paymentFile);
	}

	/**
	 * Returns the number of Payment files where groupId = &#63; and keypayGoodCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param keypayGoodCode the keypay good code
	 * @return the number of matching Payment files
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByGoodCode(long groupId, String keypayGoodCode)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_GOODCODE;

		Object[] finderArgs = new Object[] { groupId, keypayGoodCode };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_PAYMENTFILE_WHERE);

			query.append(_FINDER_COLUMN_GOODCODE_GROUPID_2);

			boolean bindKeypayGoodCode = false;

			if (keypayGoodCode == null) {
				query.append(_FINDER_COLUMN_GOODCODE_KEYPAYGOODCODE_1);
			}
			else if (keypayGoodCode.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GOODCODE_KEYPAYGOODCODE_3);
			}
			else {
				bindKeypayGoodCode = true;

				query.append(_FINDER_COLUMN_GOODCODE_KEYPAYGOODCODE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindKeypayGoodCode) {
					qPos.add(keypayGoodCode);
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

	private static final String _FINDER_COLUMN_GOODCODE_GROUPID_2 = "paymentFile.groupId = ? AND ";
	private static final String _FINDER_COLUMN_GOODCODE_KEYPAYGOODCODE_1 = "paymentFile.keypayGoodCode IS NULL";
	private static final String _FINDER_COLUMN_GOODCODE_KEYPAYGOODCODE_2 = "paymentFile.keypayGoodCode = ?";
	private static final String _FINDER_COLUMN_GOODCODE_KEYPAYGOODCODE_3 = "(paymentFile.keypayGoodCode IS NULL OR paymentFile.keypayGoodCode = '')";

	public PaymentFilePersistenceImpl() {
		setModelClass(PaymentFile.class);
	}

	/**
	 * Caches the Payment file in the entity cache if it is enabled.
	 *
	 * @param paymentFile the Payment file
	 */
	@Override
	public void cacheResult(PaymentFile paymentFile) {
		EntityCacheUtil.putResult(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
			PaymentFileImpl.class, paymentFile.getPrimaryKey(), paymentFile);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_GOODCODE,
			new Object[] {
				paymentFile.getGroupId(), paymentFile.getKeypayGoodCode()
			}, paymentFile);

		paymentFile.resetOriginalValues();
	}

	/**
	 * Caches the Payment files in the entity cache if it is enabled.
	 *
	 * @param paymentFiles the Payment files
	 */
	@Override
	public void cacheResult(List<PaymentFile> paymentFiles) {
		for (PaymentFile paymentFile : paymentFiles) {
			if (EntityCacheUtil.getResult(
						PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
						PaymentFileImpl.class, paymentFile.getPrimaryKey()) == null) {
				cacheResult(paymentFile);
			}
			else {
				paymentFile.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all Payment files.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(PaymentFileImpl.class.getName());
		}

		EntityCacheUtil.clearCache(PaymentFileImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the Payment file.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(PaymentFile paymentFile) {
		EntityCacheUtil.removeResult(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
			PaymentFileImpl.class, paymentFile.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(paymentFile);
	}

	@Override
	public void clearCache(List<PaymentFile> paymentFiles) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (PaymentFile paymentFile : paymentFiles) {
			EntityCacheUtil.removeResult(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
				PaymentFileImpl.class, paymentFile.getPrimaryKey());

			clearUniqueFindersCache(paymentFile);
		}
	}

	protected void cacheUniqueFindersCache(PaymentFile paymentFile) {
		if (paymentFile.isNew()) {
			Object[] args = new Object[] {
					paymentFile.getGroupId(), paymentFile.getKeypayGoodCode()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_GOODCODE, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_GOODCODE, args,
				paymentFile);
		}
		else {
			PaymentFileModelImpl paymentFileModelImpl = (PaymentFileModelImpl)paymentFile;

			if ((paymentFileModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_GOODCODE.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						paymentFile.getGroupId(),
						paymentFile.getKeypayGoodCode()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_GOODCODE, args,
					Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_GOODCODE, args,
					paymentFile);
			}
		}
	}

	protected void clearUniqueFindersCache(PaymentFile paymentFile) {
		PaymentFileModelImpl paymentFileModelImpl = (PaymentFileModelImpl)paymentFile;

		Object[] args = new Object[] {
				paymentFile.getGroupId(), paymentFile.getKeypayGoodCode()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GOODCODE, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_GOODCODE, args);

		if ((paymentFileModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_GOODCODE.getColumnBitmask()) != 0) {
			args = new Object[] {
					paymentFileModelImpl.getOriginalGroupId(),
					paymentFileModelImpl.getOriginalKeypayGoodCode()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GOODCODE, args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_GOODCODE, args);
		}
	}

	/**
	 * Creates a new Payment file with the primary key. Does not add the Payment file to the database.
	 *
	 * @param paymentFileId the primary key for the new Payment file
	 * @return the new Payment file
	 */
	@Override
	public PaymentFile create(long paymentFileId) {
		PaymentFile paymentFile = new PaymentFileImpl();

		paymentFile.setNew(true);
		paymentFile.setPrimaryKey(paymentFileId);

		return paymentFile;
	}

	/**
	 * Removes the Payment file with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param paymentFileId the primary key of the Payment file
	 * @return the Payment file that was removed
	 * @throws org.opencps.paymentmgt.NoSuchPaymentFileException if a Payment file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentFile remove(long paymentFileId)
		throws NoSuchPaymentFileException, SystemException {
		return remove((Serializable)paymentFileId);
	}

	/**
	 * Removes the Payment file with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the Payment file
	 * @return the Payment file that was removed
	 * @throws org.opencps.paymentmgt.NoSuchPaymentFileException if a Payment file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentFile remove(Serializable primaryKey)
		throws NoSuchPaymentFileException, SystemException {
		Session session = null;

		try {
			session = openSession();

			PaymentFile paymentFile = (PaymentFile)session.get(PaymentFileImpl.class,
					primaryKey);

			if (paymentFile == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchPaymentFileException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(paymentFile);
		}
		catch (NoSuchPaymentFileException nsee) {
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
	protected PaymentFile removeImpl(PaymentFile paymentFile)
		throws SystemException {
		paymentFile = toUnwrappedModel(paymentFile);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(paymentFile)) {
				paymentFile = (PaymentFile)session.get(PaymentFileImpl.class,
						paymentFile.getPrimaryKeyObj());
			}

			if (paymentFile != null) {
				session.delete(paymentFile);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (paymentFile != null) {
			clearCache(paymentFile);
		}

		return paymentFile;
	}

	@Override
	public PaymentFile updateImpl(
		org.opencps.paymentmgt.model.PaymentFile paymentFile)
		throws SystemException {
		paymentFile = toUnwrappedModel(paymentFile);

		boolean isNew = paymentFile.isNew();

		Session session = null;

		try {
			session = openSession();

			if (paymentFile.isNew()) {
				session.save(paymentFile);

				paymentFile.setNew(false);
			}
			else {
				session.merge(paymentFile);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !PaymentFileModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		EntityCacheUtil.putResult(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
			PaymentFileImpl.class, paymentFile.getPrimaryKey(), paymentFile);

		clearUniqueFindersCache(paymentFile);
		cacheUniqueFindersCache(paymentFile);

		return paymentFile;
	}

	protected PaymentFile toUnwrappedModel(PaymentFile paymentFile) {
		if (paymentFile instanceof PaymentFileImpl) {
			return paymentFile;
		}

		PaymentFileImpl paymentFileImpl = new PaymentFileImpl();

		paymentFileImpl.setNew(paymentFile.isNew());
		paymentFileImpl.setPrimaryKey(paymentFile.getPrimaryKey());

		paymentFileImpl.setPaymentFileId(paymentFile.getPaymentFileId());
		paymentFileImpl.setCompanyId(paymentFile.getCompanyId());
		paymentFileImpl.setGroupId(paymentFile.getGroupId());
		paymentFileImpl.setUserId(paymentFile.getUserId());
		paymentFileImpl.setCreateDate(paymentFile.getCreateDate());
		paymentFileImpl.setModifiedDate(paymentFile.getModifiedDate());
		paymentFileImpl.setUuid(paymentFile.getUuid());
		paymentFileImpl.setDossierId(paymentFile.getDossierId());
		paymentFileImpl.setFileGroupId(paymentFile.getFileGroupId());
		paymentFileImpl.setOwnerUserId(paymentFile.getOwnerUserId());
		paymentFileImpl.setOwnerOrganizationId(paymentFile.getOwnerOrganizationId());
		paymentFileImpl.setGovAgencyOrganizationId(paymentFile.getGovAgencyOrganizationId());
		paymentFileImpl.setPaymentName(paymentFile.getPaymentName());
		paymentFileImpl.setRequestDatetime(paymentFile.getRequestDatetime());
		paymentFileImpl.setAmount(paymentFile.getAmount());
		paymentFileImpl.setRequestNote(paymentFile.getRequestNote());
		paymentFileImpl.setPaymentOptions(paymentFile.getPaymentOptions());
		paymentFileImpl.setKeypayUrl(paymentFile.getKeypayUrl());
		paymentFileImpl.setKeypayTransactionId(paymentFile.getKeypayTransactionId());
		paymentFileImpl.setKeypayGoodCode(paymentFile.getKeypayGoodCode());
		paymentFileImpl.setKeypayMerchantCode(paymentFile.getKeypayMerchantCode());
		paymentFileImpl.setBankInfo(paymentFile.getBankInfo());
		paymentFileImpl.setPlaceInfo(paymentFile.getPlaceInfo());
		paymentFileImpl.setPaymentStatus(paymentFile.getPaymentStatus());
		paymentFileImpl.setPaymentMethod(paymentFile.getPaymentMethod());
		paymentFileImpl.setConfirmDatetime(paymentFile.getConfirmDatetime());
		paymentFileImpl.setConfirmFileEntryId(paymentFile.getConfirmFileEntryId());
		paymentFileImpl.setApproveDatetime(paymentFile.getApproveDatetime());
		paymentFileImpl.setAccountUserName(paymentFile.getAccountUserName());
		paymentFileImpl.setApproveNote(paymentFile.getApproveNote());
		paymentFileImpl.setGovAgencyTaxNo(paymentFile.getGovAgencyTaxNo());
		paymentFileImpl.setInvoiceTemplateNo(paymentFile.getInvoiceTemplateNo());
		paymentFileImpl.setInvoiceIssueNo(paymentFile.getInvoiceIssueNo());
		paymentFileImpl.setInvoiceNo(paymentFile.getInvoiceNo());

		return paymentFileImpl;
	}

	/**
	 * Returns the Payment file with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the Payment file
	 * @return the Payment file
	 * @throws org.opencps.paymentmgt.NoSuchPaymentFileException if a Payment file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentFile findByPrimaryKey(Serializable primaryKey)
		throws NoSuchPaymentFileException, SystemException {
		PaymentFile paymentFile = fetchByPrimaryKey(primaryKey);

		if (paymentFile == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchPaymentFileException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return paymentFile;
	}

	/**
	 * Returns the Payment file with the primary key or throws a {@link org.opencps.paymentmgt.NoSuchPaymentFileException} if it could not be found.
	 *
	 * @param paymentFileId the primary key of the Payment file
	 * @return the Payment file
	 * @throws org.opencps.paymentmgt.NoSuchPaymentFileException if a Payment file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentFile findByPrimaryKey(long paymentFileId)
		throws NoSuchPaymentFileException, SystemException {
		return findByPrimaryKey((Serializable)paymentFileId);
	}

	/**
	 * Returns the Payment file with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the Payment file
	 * @return the Payment file, or <code>null</code> if a Payment file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentFile fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		PaymentFile paymentFile = (PaymentFile)EntityCacheUtil.getResult(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
				PaymentFileImpl.class, primaryKey);

		if (paymentFile == _nullPaymentFile) {
			return null;
		}

		if (paymentFile == null) {
			Session session = null;

			try {
				session = openSession();

				paymentFile = (PaymentFile)session.get(PaymentFileImpl.class,
						primaryKey);

				if (paymentFile != null) {
					cacheResult(paymentFile);
				}
				else {
					EntityCacheUtil.putResult(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
						PaymentFileImpl.class, primaryKey, _nullPaymentFile);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(PaymentFileModelImpl.ENTITY_CACHE_ENABLED,
					PaymentFileImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return paymentFile;
	}

	/**
	 * Returns the Payment file with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param paymentFileId the primary key of the Payment file
	 * @return the Payment file, or <code>null</code> if a Payment file with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentFile fetchByPrimaryKey(long paymentFileId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)paymentFileId);
	}

	/**
	 * Returns all the Payment files.
	 *
	 * @return the Payment files
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<PaymentFile> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the Payment files.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.paymentmgt.model.impl.PaymentFileModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of Payment files
	 * @param end the upper bound of the range of Payment files (not inclusive)
	 * @return the range of Payment files
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<PaymentFile> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the Payment files.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.paymentmgt.model.impl.PaymentFileModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of Payment files
	 * @param end the upper bound of the range of Payment files (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of Payment files
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<PaymentFile> findAll(int start, int end,
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

		List<PaymentFile> list = (List<PaymentFile>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_PAYMENTFILE);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_PAYMENTFILE;

				if (pagination) {
					sql = sql.concat(PaymentFileModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<PaymentFile>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<PaymentFile>(list);
				}
				else {
					list = (List<PaymentFile>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the Payment files from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (PaymentFile paymentFile : findAll()) {
			remove(paymentFile);
		}
	}

	/**
	 * Returns the number of Payment files.
	 *
	 * @return the number of Payment files
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

				Query q = session.createQuery(_SQL_COUNT_PAYMENTFILE);

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
	 * Initializes the Payment file persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.org.opencps.paymentmgt.model.PaymentFile")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<PaymentFile>> listenersList = new ArrayList<ModelListener<PaymentFile>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<PaymentFile>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(PaymentFileImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_PAYMENTFILE = "SELECT paymentFile FROM PaymentFile paymentFile";
	private static final String _SQL_SELECT_PAYMENTFILE_WHERE = "SELECT paymentFile FROM PaymentFile paymentFile WHERE ";
	private static final String _SQL_COUNT_PAYMENTFILE = "SELECT COUNT(paymentFile) FROM PaymentFile paymentFile";
	private static final String _SQL_COUNT_PAYMENTFILE_WHERE = "SELECT COUNT(paymentFile) FROM PaymentFile paymentFile WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "paymentFile.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No PaymentFile exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No PaymentFile exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(PaymentFilePersistenceImpl.class);
	private static Set<String> _badColumnNames = SetUtil.fromArray(new String[] {
				"uuid"
			});
	private static PaymentFile _nullPaymentFile = new PaymentFileImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<PaymentFile> toCacheModel() {
				return _nullPaymentFileCacheModel;
			}
		};

	private static CacheModel<PaymentFile> _nullPaymentFileCacheModel = new CacheModel<PaymentFile>() {
			@Override
			public PaymentFile toEntityModel() {
				return _nullPaymentFile;
			}
		};
}
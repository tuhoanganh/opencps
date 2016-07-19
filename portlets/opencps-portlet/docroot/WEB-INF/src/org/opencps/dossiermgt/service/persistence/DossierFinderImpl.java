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

package org.opencps.dossiermgt.service.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.opencps.dossiermgt.bean.DossierBean;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.impl.DossierImpl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * @author trungnt
 */
public class DossierFinderImpl extends BasePersistenceImpl<Dossier>
	implements DossierFinder {

	public static final String COUNT_DOSSIER = DossierFinder.class
		.getName() + ".countDossier";

	public static final String COUNT_DOSSIER_BY_USER = DossierFinder.class
		.getName() + ".countDossierByUser";

	public static final String COUNT_DOSSIER_BY_KEYWORDDOMAINANDSTATUS =
		DossierFinder.class
			.getName() + ".countDossierByKeywordDomainAndStatus";

	public static final String SEARCH_DOSSIER = DossierFinder.class
		.getName() + ".searchDossier";

	public static final String SEARCH_DOSSIER_BY_USER = DossierFinder.class
		.getName() + ".searchDossierByUser";

	public static final String SEARCH_DOSSIER_BY_KEYWORDDOMAINANDSTATUS =
		DossierFinder.class
			.getName() + ".searchDossierByKeywordDomainAndStatus";

	private Log _log = LogFactoryUtil
		.getLog(DossierFinder.class
			.getName());

	/**
	 * @param groupId
	 * @param keyword
	 * @param dossierStatus
	 * @return
	 */
	public int countDossier(
		long groupId, String keyword, String dossierStatus) {

		String[] keywords = null;

		boolean andOperator = false;

		if (Validator
			.isNotNull(keyword)) {
			keywords = CustomSQLUtil
				.keywords(keyword);
		}
		else {
			andOperator = true;
		}

		return countDossier(groupId, keywords, dossierStatus, andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param dossierStatus
	 * @param andOperator
	 * @return
	 */
	private int countDossier(
		long groupId, String[] keywords, String dossierStatus,
		boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(COUNT_DOSSIER);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_service_config.govAgencyName)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"INNER JOIN opencps_serviceinfo ON opencps_dossier.serviceInfoId = opencps_serviceinfo.serviceInfoId",
						StringPool.BLANK);
				sql = StringUtil
					.replace(sql,
						"INNER JOIN opencps_service_config ON opencps_dossier.serviceConfigId = opencps_service_config.serviceConfigId",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_service_config.govAgencyName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(dossierStatus)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier.dossierStatus = ?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);

			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(dossierStatus)) {
				qPos
					.add(dossierStatus);
			}

			Iterator<Integer> itr = q
				.iterate();

			if (itr
				.hasNext()) {
				Integer count = itr
					.next();

				if (count != null) {
					return count
						.intValue();
				}
			}

			return 0;

		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			closeSession(session);
		}

		return 0;
	}

	/**
	 * @param groupId
	 * @param keyword
	 * @param domainCode
	 * @param dossierStatus
	 * @return
	 */
	public int countDossierByKeywordDomainAndStatus(
		long groupId, String keyword, String domainCode, String dossierStatus) {

		String[] keywords = null;

		boolean andOperator = false;

		if (Validator
			.isNotNull(keyword)) {
			keywords = CustomSQLUtil
				.keywords(keyword);
		}
		else {
			andOperator = true;
		}

		return countDossierByKeywordDomainAndStatus(groupId, keywords,
			domainCode, dossierStatus, andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param domainCode
	 * @param dossierStatus
	 * @param andOperator
	 * @return
	 */
	private int countDossierByKeywordDomainAndStatus(
		long groupId, String[] keywords, String domainCode,
		String dossierStatus, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(COUNT_DOSSIER_BY_KEYWORDDOMAINANDSTATUS);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_service_config.govAgencyName)",
						StringPool.LIKE, true, keywords);
				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.subjectName)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"INNER JOIN opencps_service_config ON opencps_dossier.serviceConfigId = opencps_service_config.serviceConfigId",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_service_config.govAgencyName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (""
				.equals(domainCode)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_serviceinfo.domainCode = ?)",
						StringPool.BLANK);
			}
			else {
			}
			if (Validator
				.isNull(dossierStatus)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier.dossierStatus = ?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);

			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(dossierStatus)) {
				qPos
					.add(dossierStatus);
			}
			if (!""
				.equals(domainCode)) {
				qPos
					.add(domainCode);
			}
			else {

			}

			Iterator<Integer> itr = q
				.iterate();

			if (itr
				.hasNext()) {
				Integer count = itr
					.next();

				if (count != null) {
					return count
						.intValue();
				}
			}

			return 0;

		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			closeSession(session);
		}

		return 0;
	}

	/**
	 * @param groupId
	 * @param userId
	 * @param keyword
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @return
	 */
	public int countDossierByUser(
		long groupId, long userId, String keyword,
		String serviceDomainTreeIndex, String dossierStatus) {

		String[] keywords = null;

		boolean andOperator = false;

		if (Validator
			.isNotNull(keyword)) {
			keywords = CustomSQLUtil
				.keywords(keyword);
		}
		else {
			andOperator = true;
		}

		return countDossierByUser(groupId, userId, keywords,
			serviceDomainTreeIndex, dossierStatus, andOperator);
	}

	/**
	 * @param groupId
	 * @param userId
	 * @param keywords
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @param andOperator
	 * @return
	 */
	private int countDossierByUser(
		long groupId, long userId, String[] keywords,
		String serviceDomainTreeIndex, String dossierStatus,
		boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(COUNT_DOSSIER_BY_USER);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_service_config.govAgencyName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.subjectName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.receptionNo)",
						StringPool.LIKE, true, keywords);
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND ((lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_service_config.govAgencyName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$]))",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(serviceDomainTreeIndex)) {

				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier.serviceDomainIndex LIKE ? OR opencps_dossier.serviceDomainIndex = ?)",
						StringPool.BLANK);
			}
			else {
				if (StringUtil
					.contains(serviceDomainTreeIndex, StringPool.PERIOD)) {
					sql = StringUtil
						.replace(sql,
							"AND (opencps_dossier.serviceDomainIndex LIKE ? OR opencps_dossier.serviceDomainIndex = ?)",
							"AND (opencps_dossier.serviceDomainIndex LIKE ?)");

					serviceDomainTreeIndex = serviceDomainTreeIndex
						.substring(0, serviceDomainTreeIndex
							.indexOf(StringPool.PERIOD) + 1);

				}

			}

			if (Validator
				.isNull(dossierStatus)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier.dossierStatus = ?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);

			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			qPos
				.add(userId);

			if (Validator
				.isNotNull(serviceDomainTreeIndex) && StringUtil
					.contains(serviceDomainTreeIndex, StringPool.PERIOD)) {
				qPos
					.add(serviceDomainTreeIndex + StringPool.PERCENT);

			}
			else if (Validator
				.isNotNull(serviceDomainTreeIndex) && !StringUtil
					.contains(serviceDomainTreeIndex, StringPool.PERIOD)) {
				qPos
					.add(serviceDomainTreeIndex + StringPool.PERIOD +
						StringPool.PERCENT);
				qPos
					.add(serviceDomainTreeIndex);
			}

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(dossierStatus)) {
				qPos
					.add(dossierStatus);
			}

			Iterator<Integer> itr = q
				.iterate();

			if (itr
				.hasNext()) {
				Integer count = itr
					.next();

				if (count != null) {
					return count
						.intValue();
				}
			}

			return 0;

		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			closeSession(session);
		}

		return 0;
	}

	/**
	 * @param groupId
	 * @param keyword
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	public List<Dossier> searchDossier(
		long groupId, String keyword, String dossierStatus, int start, int end,
		OrderByComparator obc) {

		String[] keywords = null;
		boolean andOperator = false;
		if (Validator
			.isNotNull(keyword)) {
			keywords = CustomSQLUtil
				.keywords(keyword);
		}
		else {
			andOperator = true;
		}
		return searchDossier(groupId, keywords, dossierStatus, andOperator,
			start, end, obc);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param dossierStatus
	 * @param andOperator
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	private List<Dossier> searchDossier(
		long groupId, String[] keywords, String dossierStatus,
		boolean andOperator, int start, int end, OrderByComparator obc) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_DOSSIER);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_service_config.govAgencyName)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"INNER JOIN opencps_serviceinfo ON opencps_dossier.serviceInfoId = opencps_serviceinfo.serviceInfoId",
						StringPool.BLANK);
				sql = StringUtil
					.replace(sql,
						"INNER JOIN opencps_service_config ON opencps_dossier.serviceConfigId = opencps_service_config.serviceConfigId",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_service_config.govAgencyName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(dossierStatus)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier.dossierStatus = ?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);

			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addEntity("Dossier", DossierImpl.class);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(dossierStatus)) {
				qPos
					.add(dossierStatus);
			}

			return (List<Dossier>) QueryUtil
				.list(q, getDialect(), start, end);
		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			closeSession(session);
		}

		return null;
	}

	/**
	 * @param groupId
	 * @param keyword
	 * @param domainCode
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	public List<Dossier> searchDossierByKeywordDomainAndStatus(
		long groupId, String keyword, String domainCode, String dossierStatus,
		int start, int end, OrderByComparator obc) {

		boolean andOperator = false;
		String[] keywords = null;
		if (Validator
			.isNotNull(keyword)) {
			keywords = CustomSQLUtil
				.keywords(keyword);
		}
		else {
			andOperator = true;
		}
		return searchDossierByKeywordDomainAndStatus(groupId, keywords,
			domainCode, dossierStatus, start, end, obc, andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param domainCode
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @param obc
	 * @param andOperator
	 * @return
	 */
	private List<Dossier> searchDossierByKeywordDomainAndStatus(
		long groupId, String[] keywords, String domainCode,
		String dossierStatus, int start, int end, OrderByComparator obc,
		boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_DOSSIER_BY_KEYWORDDOMAINANDSTATUS);
			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_service_config.govAgencyName)",
						StringPool.LIKE, true, keywords);
				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.subjectName)",
						StringPool.LIKE, true, keywords);
			}
			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"INNER JOIN opencps_service_config ON opencps_dossier.serviceConfigId = opencps_service_config.serviceConfigId",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_service_config.govAgencyName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}
			if (""
				.equals(domainCode)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_serviceinfo.domainCode = ?)",
						StringPool.BLANK);
			}
			else {
			}
			if (Validator
				.isNull(dossierStatus)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier.dossierStatus = ?)",
						StringPool.BLANK);
			}
			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);
			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addEntity("Dossier", DossierImpl.class);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(dossierStatus)) {
				qPos
					.add(dossierStatus);
			}

			if (!""
				.equals(domainCode)) {
				qPos
					.add(domainCode);
			}
			else {

			}

			List<Dossier> results = (List<Dossier>) QueryUtil
				.list(q, getDialect(), start, end);
			List<Dossier> clones = new ArrayList<Dossier>(results);
			Collections
				.sort(clones, obc);

			return clones;
		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			closeSession(session);
		}

		return null;
	}

	/**
	 * @param groupId
	 * @param userId
	 * @param keyword
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	public List searchDossierByUser(
		long groupId, long userId, String keyword,
		String serviceDomainTreeIndex, String dossierStatus, int start, int end,
		OrderByComparator obc) {

		String[] keywords = null;
		boolean andOperator = false;
		if (Validator
			.isNotNull(keyword)) {
			keywords = CustomSQLUtil
				.keywords(keyword);
		}
		else {
			andOperator = true;
		}
		return searchDossierByUser(groupId, userId, keywords,
			serviceDomainTreeIndex, dossierStatus, andOperator, start, end,
			obc);
	}

	/**
	 * @param groupId
	 * @param userId
	 * @param keywords
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @param andOperator
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	private List<DossierBean> searchDossierByUser(
		long groupId, long userId, String[] keywords,
		String serviceDomainTreeIndex, String dossierStatus,
		boolean andOperator, int start, int end, OrderByComparator obc) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_DOSSIER_BY_USER);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_service_config.govAgencyName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.subjectName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.receptionNo)",
						StringPool.LIKE, true, keywords);
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND ((lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_service_config.govAgencyName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$]))",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(serviceDomainTreeIndex)) {

				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier.serviceDomainIndex LIKE ? OR opencps_dossier.serviceDomainIndex = ?)",
						StringPool.BLANK);
			}
			else {
				if (StringUtil
					.contains(serviceDomainTreeIndex, StringPool.PERIOD)) {
					sql = StringUtil
						.replace(sql,
							"AND (opencps_dossier.serviceDomainIndex LIKE ? OR opencps_dossier.serviceDomainIndex = ?)",
							"AND (opencps_dossier.serviceDomainIndex LIKE ?)");

					serviceDomainTreeIndex = serviceDomainTreeIndex
						.substring(0, serviceDomainTreeIndex
							.indexOf(StringPool.PERIOD) + 1);

				}

			}

			if (Validator
				.isNull(dossierStatus)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier.dossierStatus = ?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);

			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addEntity("Dossier", DossierImpl.class);
			q
				.addScalar("SERVICE_NAME", Type.STRING);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			qPos
				.add(userId);

			if (Validator
				.isNotNull(serviceDomainTreeIndex) && StringUtil
					.contains(serviceDomainTreeIndex, StringPool.PERIOD)) {
				qPos
					.add(serviceDomainTreeIndex + StringPool.PERCENT);

			}
			else if (Validator
				.isNotNull(serviceDomainTreeIndex) && !StringUtil
					.contains(serviceDomainTreeIndex, StringPool.PERIOD)) {
				qPos
					.add(serviceDomainTreeIndex + StringPool.PERIOD +
						StringPool.PERCENT);
				qPos
					.add(serviceDomainTreeIndex);
			}

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(dossierStatus)) {
				qPos
					.add(dossierStatus);
			}

			Iterator<Object[]> itr = (Iterator<Object[]>) QueryUtil
				.list(q, getDialect(), start, end).iterator();

			List<DossierBean> dossierBeans = new ArrayList<DossierBean>();

			if (itr
				.hasNext()) {
				while (itr
					.hasNext()) {
					DossierBean dossierBean = new DossierBean();

					Object[] objects = itr
						.next();

					Dossier dossier = (Dossier) objects[0];

					String serviceName = (String) objects[1];

					dossierBean
						.setDossierId(dossier
							.getDossierId());
					dossierBean
						.setDossier(dossier);
					dossierBean
						.setServiceName(serviceName);

					dossierBeans
						.add(dossierBean);

				}
			}

			return dossierBeans;

		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			closeSession(session);
		}

		return null;
	}
}

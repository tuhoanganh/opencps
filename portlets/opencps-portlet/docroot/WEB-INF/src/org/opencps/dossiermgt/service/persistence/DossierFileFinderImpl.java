
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

import java.util.Iterator;
import java.util.List;

import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.impl.DossierFileImpl;

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

import org.opencps.dossiermgt.service.persistence.DossierFileFinder;
import org.opencps.dossiermgt.util.DossierMgtUtil;

/**
 * @author trungnt
 *
 */
public class DossierFileFinderImpl extends BasePersistenceImpl<DossierFile>
	implements DossierFileFinder {

	/**
	 * @param groupId
	 * @param keyword
	 * @param dossierTemplateId
	 * @param fileEntryId
	 * @param onlyViewFileResult
	 * @return
	 */
	public int countDossierFile(
		long groupId, String keyword, long dossierTemplateId, long fileEntryId,
		boolean onlyViewFileResult) {

		String[] keywords = null;
		int dossierFileType = DossierMgtUtil.DOSSIERFILETYPE_ALL;
		boolean andOperator = false;
		if (Validator
			.isNotNull(keyword)) {
			keywords = CustomSQLUtil
				.keywords(keyword);
		}
		else {
			andOperator = true;
		}
		if (onlyViewFileResult) {
			dossierFileType = DossierMgtUtil.DOSSIERFILETYPE_OUTPUT;
		}
		return countDossierFile(groupId, keywords, dossierTemplateId,
			dossierFileType, fileEntryId, andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param dossierTemplateId
	 * @param dossierFileType
	 * @param fileEntryId
	 * @param andOperator
	 * @return
	 */
	private int countDossierFile(
		long groupId, String[] keywords, long dossierTemplateId,
		int dossierFileType, long fileEntryId, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(COUNT_DOSSIER_FILE_TEMPLATE);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.displayName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.dossierFileNo)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_dossier_file.displayName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier_file.dossierFileNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (dossierTemplateId != 0) {
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossierpart.dossierTemplateId = ?)",
						StringPool.BLANK);
			}

			if (dossierFileType != DossierMgtUtil.DOSSIERFILETYPE_ALL) {
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.dossierFileType = ?)",
						StringPool.BLANK);
			}

			if (fileEntryId < 0) {
				sql = StringUtil
					.replace(sql,
						" AND (opencps_dossier_file.fileEntryId IS NOT NULL AND opencps_dossier_file.fileEntryId = ?)",
						StringPool.BLANK);
			}
			else {

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
			}

			if (fileEntryId > 0) {
				qPos
					.add(fileEntryId);
			}

			if (dossierFileType != DossierMgtUtil.DOSSIERFILETYPE_ALL) {
				qPos
					.add(dossierFileType);
			}

			if (dossierTemplateId > 0) {
				qPos
					.add(dossierTemplateId);
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
	 * @param templateFileNo
	 * @param removed
	 * @return
	 */
	public int countDossierFile(
		long groupId, String keyword, String templateFileNo, int removed) {

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

		return countDossierFile(groupId, keywords, templateFileNo, removed,
			andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param templateFileNo
	 * @param removed
	 * @param andOperator
	 * @return
	 */
	private int countDossierFile(
		long groupId, String[] keywords, String templateFileNo, int removed,
		boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(COUNT_DOSSIER_FILE_BY_G_K_T_R);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.displayName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.dossierFileNo)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"(lower(opencps_dossier_file.displayName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier_file.dossierFileNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(templateFileNo)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier_file.templateFileNo = ?)",
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
				.add(removed);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(templateFileNo)) {
				qPos
					.add(templateFileNo);
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
	 * @param dossierTemplateId
	 * @param fileEntryId
	 * @param onlyViewFileResult
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	public List<DossierFile> searchDossierFile(
		long groupId, String keyword, long dossierTemplateId, long fileEntryId,
		boolean onlyViewFileResult, int start, int end, OrderByComparator obc) {

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
		int dossierFileType = DossierMgtUtil.DOSSIERFILETYPE_ALL;
		if (onlyViewFileResult) {
			dossierFileType = DossierMgtUtil.DOSSIERFILETYPE_OUTPUT;
		}
		return searchDossierFile(groupId, keywords, dossierTemplateId,
			dossierFileType, fileEntryId, start, end, obc, andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param dossierTemplateId
	 * @param dossierFileType
	 * @param fileEntryId
	 * @param start
	 * @param end
	 * @param obc
	 * @param andOperator
	 * @return
	 */
	private List<DossierFile> searchDossierFile(
		long groupId, String[] keywords, long dossierTemplateId,
		int dossierFileType, long fileEntryId, int start, int end,
		OrderByComparator obc, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_DOSSIER_FILE_TEMPLATE);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.displayName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.dossierFileNo)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_dossier_file.displayName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier_file.dossierFileNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (dossierTemplateId != 0) {
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossierpart.dossierTemplateId = ?)",
						StringPool.BLANK);
			}

			if (dossierFileType != DossierMgtUtil.DOSSIERFILETYPE_ALL) {
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.dossierFileType = ?)",
						StringPool.BLANK);
			}

			if (fileEntryId < 0) {
				sql = StringUtil
					.replace(sql,
						" AND (opencps_dossier_file.fileEntryId IS NOT NULL AND opencps_dossier_file.fileEntryId = ?)",
						StringPool.BLANK);
			}
			else {

			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);
			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addEntity("DossierFile", DossierFileImpl.class);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (fileEntryId > 0) {
				qPos
					.add(fileEntryId);
			}

			if (dossierFileType != DossierMgtUtil.DOSSIERFILETYPE_ALL) {
				qPos
					.add(dossierFileType);
			}

			if (dossierTemplateId > 0) {
				qPos
					.add(dossierTemplateId);
			}
			return (List<DossierFile>) QueryUtil
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

	public List<DossierFile> searchDossierFile(
		long groupId, String keyword, String templateFileNo, int removed,
		int start, int end, OrderByComparator obc) {

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

		return searchDossierFile(groupId, keywords, templateFileNo, removed,
			start, end, obc, andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param templateFileNo
	 * @param removed
	 * @param start
	 * @param end
	 * @param obc
	 * @param andOperator
	 * @return
	 */
	private List<DossierFile> searchDossierFile(
		long groupId, String[] keywords, String templateFileNo, int removed,
		int start, int end, OrderByComparator obc, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_DOSSIER_FILE_BY_G_K_T_R);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.displayName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.dossierFileNo)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"(lower(opencps_dossier_file.displayName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier_file.dossierFileNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(templateFileNo)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier_file.templateFileNo = ?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);
			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addEntity("DossierFile", DossierFileImpl.class);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			qPos
				.add(removed);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(templateFileNo)) {
				qPos
					.add(templateFileNo);
			}
			return (List<DossierFile>) QueryUtil
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

	public static final String SEARCH_DOSSIER_FILE_TEMPLATE =
		DossierFileFinder.class
			.getName() + ".searchDossierFile";
	public static final String COUNT_DOSSIER_FILE_TEMPLATE =
		DossierFileFinder.class
			.getName() + ".countDossierFile";

	public static final String SEARCH_DOSSIER_FILE_BY_G_K_T_R =
		DossierFileFinder.class
			.getName() + ".searchDossierFileByG_K_T_R";
	public static final String COUNT_DOSSIER_FILE_BY_G_K_T_R =
		DossierFileFinder.class
			.getName() + ".countDossierFileByG_K_T_R";

	private Log _log = LogFactoryUtil
		.getLog(DossierFileFinder.class
			.getName());
}

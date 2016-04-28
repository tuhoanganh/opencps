/*******************************************************************************
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

package org.opencps.dossiermgt.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.FileGroup;
import org.opencps.dossiermgt.search.DossierFileDisplayTerms;
import org.opencps.dossiermgt.service.base.DossierLocalServiceBaseImpl;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.PortletConstants;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;

/**
 * The implementation of the dossier local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.dossiermgt.service.DossierLocalService} interface. <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author trungnt
 * @see org.opencps.dossiermgt.service.base.DossierLocalServiceBaseImpl
 * @see org.opencps.dossiermgt.service.DossierLocalServiceUtil
 */
public class DossierLocalServiceImpl extends DossierLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.dossiermgt.service.DossierLocalServiceUtil} to access
	 * the dossier local service.
	 */

	public Dossier addDossier(
	    long userId, long ownerOrganizationId, long dossierTemplateId,
	    String templateFileNo, long serviceConfigId, long serviceInfoId,
	    String serviceDomainIndex, long govAgencyOrganizationId,
	    String govAgencyCode, String govAgencyName, int serviceMode,
	    String serviceAdministrationIndex, String cityCode, String cityName,
	    String districtCode, String districtName, String wardName,
	    String wardCode, String subjectName, String subjectId, String address,
	    String contactName, String contactTelNo, String contactEmail,
	    String note, int dossierSource, int dossierStatus,
	    String[] uploadDataSchemas, long parentFolderId,
	    ServiceContext serviceContext)
	    throws SystemException, PortalException {

		long dossierId = counterLocalService
		    .increment(Dossier.class
		        .getName());

		Dossier dossier = dossierPersistence
		    .create(dossierId);

		int count = dossierLocalService
		    .countByGroupId(serviceContext
		        .getScopeGroupId());

		int dossierNo = count + 1;

		Date now = new Date();

		serviceContext
		    .setAddGroupPermissions(true);
		serviceContext
		    .setAddGuestPermissions(true);

		dossier
		    .setUserId(userId);
		dossier
		    .setGroupId(serviceContext
		        .getScopeGroupId());
		dossier
		    .setCompanyId(serviceContext
		        .getCompanyId());
		dossier
		    .setCreateDate(now);
		dossier
		    .setModifiedDate(now);

		dossier
		    .setAddress(address);
		dossier
		    .setCityCode(cityCode);
		dossier
		    .setCityName(cityName);
		dossier
		    .setContactEmail(contactEmail);
		dossier
		    .setContactName(contactName);
		dossier
		    .setContactTelNo(contactTelNo);
		dossier
		    .setCounter(dossierNo);
		dossier
		    .setDistrictCode(districtCode);
		dossier
		    .setDistrictName(districtName);
		dossier
		    .setDossierSource(dossierSource);
		dossier
		    .setDossierStatus(dossierStatus);
		dossier
		    .setDossierTemplateId(dossierTemplateId);
		dossier
		    .setGovAgencyCode(govAgencyCode);
		dossier
		    .setGovAgencyName(govAgencyName);
		dossier
		    .setGovAgencyOrganizationId(govAgencyOrganizationId);
		dossier
		    .setNote(note);
		dossier
		    .setOwnerOrganizationId(ownerOrganizationId);
		dossier
		    .setServiceAdministrationIndex(serviceAdministrationIndex);
		dossier
		    .setServiceConfigId(serviceConfigId);
		dossier
		    .setServiceDomainIndex(serviceDomainIndex);
		dossier
		    .setServiceInfoId(serviceInfoId);
		dossier
		    .setServiceMode(serviceMode);
		dossier
		    .setSubjectId(subjectId);
		dossier
		    .setSubjectName(subjectName);
		dossier
		    .setUuid(PortalUUIDUtil
		        .generate());
		dossier
		    .setWardCode(wardCode);
		dossier
		    .setWardName(wardName);

		dossier = dossierPersistence
		    .update(dossier);

		dossierStatusLocalService
		    .addDossierStatus(
		        userId, dossierId, 0, PortletConstants.DOSSIER_STATUS_NEW,
		        "Create New", "", now,
		        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
		        serviceContext);
		dossierLogLocalService
		    .addDossierLog(
		        userId, dossierId, 0, PortletConstants.DOSSIER_STATUS_NEW,
		        "Create New", "", now, PortletConstants.DOSSIER_LOG_NORMAL,
		        serviceContext);

		DLFolder dossierNoFolder = dlFolderLocalService
		    .addFolder(userId, serviceContext
		        .getScopeGroupId(), serviceContext
		            .getScopeGroupId(),
		        false, parentFolderId, String
		            .valueOf(dossierNo),
		        StringPool.BLANK, false, serviceContext);

		for (int i = 0; i < uploadDataSchemas.length; i++) {
			String schema = uploadDataSchemas[i];

			JSONObject jsonObject = null;

			jsonObject = JSONFactoryUtil
			    .createJSONObject(schema);
			long dossierPartId = jsonObject
			    .getLong(DossierFileDisplayTerms.DOSSIER_PART_ID);
			long fileEntryId = jsonObject
			    .getLong(DossierFileDisplayTerms.FILE_ENTRY_ID);

			int dossierFileOriginal = jsonObject
			    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL);
			int dossierFileType = jsonObject
			    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_TYPE);
			String formData = jsonObject
			    .getString(DossierFileDisplayTerms.FORM_DATA);
			String groupName = jsonObject
			    .getString(DossierFileDisplayTerms.GROUP_NAME);
			String displayName = jsonObject
			    .getString(DossierFileDisplayTerms.DISPLAY_NAME);
			String mimeType = jsonObject
			    .getString(DossierFileDisplayTerms.MIME_TYPE);
			/*
			 * String fileName = jsonObject
			 * .getString(DossierFileDisplayTerms.FILE_NAME);
			 */
			String dossierFileDate = jsonObject
			    .getString(DossierFileDisplayTerms.DOSSIER_FILE_DATE);
			String dossierFileNo = jsonObject
			    .getString(DossierFileDisplayTerms.DOSSIER_FILE_NO);

			FileEntry tempFileEntry = null;

			// tempFileEntry = TempFileUtil.getTempFile(groupId, userId,
			// sourceFileName, tempFolderName);

			FileEntry fileEntry = null;

			if (fileEntryId > 0) {
				tempFileEntry = DLAppServiceUtil
				    .getFileEntry(fileEntryId);

				InputStream inputStream = tempFileEntry
				    .getContentStream();

				long size = tempFileEntry
				    .getSize();

				String sourceFileName = tempFileEntry
				    .getTitle();

				fileEntry = DLAppServiceUtil
				    .addFileEntry(serviceContext
				        .getScopeGroupId(), dossierNoFolder
				            .getFolderId(),
				        sourceFileName, mimeType, displayName, StringPool.BLANK,
				        StringPool.BLANK, inputStream, size, serviceContext);
			}
			if (fileEntry != null) {
				// Add file group
				FileGroup fileGroup = null;

				if (Validator
				    .isNotNull(groupName)) {
					fileGroup = fileGroupLocalService
					    .addFileGroup(
					        userId, dossierId, dossierPartId, groupName,
					        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					        serviceContext);

					dossierStatusLocalService
					    .addDossierStatus(userId, dossierId, fileGroup
					        .getFileGroupId(),
					        PortletConstants.DOSSIER_STATUS_NEW, "Create New",
					        "", now,
					        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					        serviceContext);
					dossierLogLocalService
					    .addDossierLog(userId, dossierId, fileGroup
					        .getFileGroupId(),
					        PortletConstants.DOSSIER_STATUS_NEW, "Create New",
					        "", now, PortletConstants.DOSSIER_LOG_NORMAL,
					        serviceContext);

				}

				Date fileDate = null;

				if (Validator
				    .isNotNull(dossierFileDate)) {
					fileDate = DateTimeUtil
					    .convertStringToDate(dossierFileDate);
				}

				// Add dossier file
				dossierFileLocalService
				    .addDossierFile(
				        userId, dossierId, dossierPartId, templateFileNo,
				        fileGroup != null ? fileGroup
				            .getFileGroupId() : 0L,
				        userId, ownerOrganizationId, displayName, formData,
				        fileEntry
				            .getFileEntryId(),
				        PortletConstants.DOSSIER_FILE_MARK_UNKNOW,
				        dossierFileType, dossierFileNo, fileDate,
				        dossierFileOriginal,
				        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
				        serviceContext);
			}

		}

		long classTypeId = 0;

		assetEntryLocalService
		    .updateEntry(userId, serviceContext
		        .getScopeGroupId(), ServiceInfo.class
		            .getName(),
		        dossier
		            .getDossierId(),
		        dossier
		            .getUuid(),
		        classTypeId, serviceContext
		            .getAssetCategoryIds(),
		        serviceContext
		            .getAssetTagNames(),
		        false, now, null, null, ContentTypes.TEXT_HTML, dossier
		            .getSubjectName(),
		        StringPool.BLANK, StringPool.BLANK, null, null, 0, 0, 0, false);

		return dossier;
	}
	
	public Dossier updateDossier(
	    long dossierId, long userId, long ownerOrganizationId, long dossierTemplateId,
	    String templateFileNo, long serviceConfigId, long serviceInfoId,
	    String serviceDomainIndex, long govAgencyOrganizationId,
	    String govAgencyCode, String govAgencyName, int serviceMode,
	    String serviceAdministrationIndex, String cityCode, String cityName,
	    String districtCode, String districtName, String wardName,
	    String wardCode, String subjectName, String subjectId, String address,
	    String contactName, String contactTelNo, String contactEmail,
	    String note, int dossierSource, int dossierStatus,
	    String[] uploadDataSchemas, long parentFolderId,
	    ServiceContext serviceContext)
	    throws SystemException, PortalException {

		
		Dossier dossier = dossierPersistence.findByPrimaryKey(dossierId);
		
		int count = dossierLocalService
		    .countByGroupId(serviceContext
		        .getScopeGroupId());

		int dossierNo = count + 1;

		Date now = new Date();

		serviceContext
		    .setAddGroupPermissions(true);
		serviceContext
		    .setAddGuestPermissions(true);

		dossier
		    .setUserId(userId);
		dossier
		    .setModifiedDate(now);

		dossier
		    .setAddress(address);
		dossier
		    .setCityCode(cityCode);
		dossier
		    .setCityName(cityName);
		dossier
		    .setContactEmail(contactEmail);
		dossier
		    .setContactName(contactName);
		dossier
		    .setContactTelNo(contactTelNo);
		dossier
		    .setCounter(dossierNo);
		dossier
		    .setDistrictCode(districtCode);
		dossier
		    .setDistrictName(districtName);
		dossier
		    .setDossierSource(dossierSource);
		dossier
		    .setDossierStatus(dossierStatus);
		dossier
		    .setDossierTemplateId(dossierTemplateId);
		dossier
		    .setGovAgencyCode(govAgencyCode);
		dossier
		    .setGovAgencyName(govAgencyName);
		dossier
		    .setGovAgencyOrganizationId(govAgencyOrganizationId);
		dossier
		    .setNote(note);
		dossier
		    .setOwnerOrganizationId(ownerOrganizationId);
		dossier
		    .setServiceAdministrationIndex(serviceAdministrationIndex);
		dossier
		    .setServiceConfigId(serviceConfigId);
		dossier
		    .setServiceDomainIndex(serviceDomainIndex);
		dossier
		    .setServiceInfoId(serviceInfoId);
		dossier
		    .setServiceMode(serviceMode);
		dossier
		    .setSubjectId(subjectId);
		dossier
		    .setSubjectName(subjectName);
		dossier
		    .setUuid(PortalUUIDUtil
		        .generate());
		dossier
		    .setWardCode(wardCode);
		dossier
		    .setWardName(wardName);

		dossier = dossierPersistence
		    .update(dossier);

		dossierStatusLocalService
		    .addDossierStatus(
		        userId, dossierId, 0, PortletConstants.DOSSIER_STATUS_NEW,
		        "Create New", "", now,
		        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
		        serviceContext);
		dossierLogLocalService
		    .addDossierLog(
		        userId, dossierId, 0, PortletConstants.DOSSIER_STATUS_NEW,
		        "Create New", "", now, PortletConstants.DOSSIER_LOG_NORMAL,
		        serviceContext);

		DLFolder dossierNoFolder = dlFolderLocalService
		    .addFolder(userId, serviceContext
		        .getScopeGroupId(), serviceContext
		            .getScopeGroupId(),
		        false, parentFolderId, String
		            .valueOf(dossierNo),
		        StringPool.BLANK, false, serviceContext);

		for (int i = 0; i < uploadDataSchemas.length; i++) {
			String schema = uploadDataSchemas[i];

			JSONObject jsonObject = null;

			jsonObject = JSONFactoryUtil
			    .createJSONObject(schema);
			long dossierPartId = jsonObject
			    .getLong(DossierFileDisplayTerms.DOSSIER_PART_ID);
			long fileEntryId = jsonObject
			    .getLong(DossierFileDisplayTerms.FILE_ENTRY_ID);

			int dossierFileOriginal = jsonObject
			    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL);
			int dossierFileType = jsonObject
			    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_TYPE);
			String formData = jsonObject
			    .getString(DossierFileDisplayTerms.FORM_DATA);
			String groupName = jsonObject
			    .getString(DossierFileDisplayTerms.GROUP_NAME);
			String displayName = jsonObject
			    .getString(DossierFileDisplayTerms.DISPLAY_NAME);
			String mimeType = jsonObject
			    .getString(DossierFileDisplayTerms.MIME_TYPE);
			/*
			 * String fileName = jsonObject
			 * .getString(DossierFileDisplayTerms.FILE_NAME);
			 */
			String dossierFileDate = jsonObject
			    .getString(DossierFileDisplayTerms.DOSSIER_FILE_DATE);
			String dossierFileNo = jsonObject
			    .getString(DossierFileDisplayTerms.DOSSIER_FILE_NO);

			FileEntry tempFileEntry = null;

			// tempFileEntry = TempFileUtil.getTempFile(groupId, userId,
			// sourceFileName, tempFolderName);

			FileEntry fileEntry = null;

			if (fileEntryId > 0) {
				tempFileEntry = DLAppServiceUtil
				    .getFileEntry(fileEntryId);

				InputStream inputStream = tempFileEntry
				    .getContentStream();

				long size = tempFileEntry
				    .getSize();

				String sourceFileName = tempFileEntry
				    .getTitle();

				fileEntry = DLAppServiceUtil
				    .addFileEntry(serviceContext
				        .getScopeGroupId(), dossierNoFolder
				            .getFolderId(),
				        sourceFileName, mimeType, displayName, StringPool.BLANK,
				        StringPool.BLANK, inputStream, size, serviceContext);
			}
			if (fileEntry != null) {
				// Add file group
				FileGroup fileGroup = null;

				if (Validator
				    .isNotNull(groupName)) {
					fileGroup = fileGroupLocalService
					    .addFileGroup(
					        userId, dossierId, dossierPartId, groupName,
					        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					        serviceContext);

					dossierStatusLocalService
					    .addDossierStatus(userId, dossierId, fileGroup
					        .getFileGroupId(),
					        PortletConstants.DOSSIER_STATUS_NEW, "Create New",
					        "", now,
					        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					        serviceContext);
					dossierLogLocalService
					    .addDossierLog(userId, dossierId, fileGroup
					        .getFileGroupId(),
					        PortletConstants.DOSSIER_STATUS_NEW, "Create New",
					        "", now, PortletConstants.DOSSIER_LOG_NORMAL,
					        serviceContext);

				}

				Date fileDate = null;

				if (Validator
				    .isNotNull(dossierFileDate)) {
					fileDate = DateTimeUtil
					    .convertStringToDate(dossierFileDate);
				}

				// Add dossier file
				dossierFileLocalService
				    .addDossierFile(
				        userId, dossierId, dossierPartId, templateFileNo,
				        fileGroup != null ? fileGroup
				            .getFileGroupId() : 0L,
				        userId, ownerOrganizationId, displayName, formData,
				        fileEntry
				            .getFileEntryId(),
				        PortletConstants.DOSSIER_FILE_MARK_UNKNOW,
				        dossierFileType, dossierFileNo, fileDate,
				        dossierFileOriginal,
				        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
				        serviceContext);
			}

		}

		long classTypeId = 0;

		assetEntryLocalService
		    .updateEntry(userId, serviceContext
		        .getScopeGroupId(), ServiceInfo.class
		            .getName(),
		        dossier
		            .getDossierId(),
		        dossier
		            .getUuid(),
		        classTypeId, serviceContext
		            .getAssetCategoryIds(),
		        serviceContext
		            .getAssetTagNames(),
		        false, now, null, null, ContentTypes.TEXT_HTML, dossier
		            .getSubjectName(),
		        StringPool.BLANK, StringPool.BLANK, null, null, 0, 0, 0, false);

		return dossier;
	}

	public int countByGroupId(long groupId)
	    throws SystemException {

		return dossierPersistence
		    .countByGroupId(groupId);
	}

	public int countDossier(long groupId, String keyword, int dossierStatus) {

		return dossierFinder
		    .countDossier(groupId, keyword, dossierStatus);
	}

	public List<Dossier> getDossierByGroupId(long groupId)
	    throws SystemException {

		return dossierPersistence
		    .filterFindByGroupId(groupId);
	}

	public List<Dossier> getDossier(
	    long groupId, String keyword, int dossierStatus, int start, int end,
	    OrderByComparator obc) {

		return dossierFinder
		    .searchDossier(groupId, keyword, dossierStatus, start, end, obc);
	}

}

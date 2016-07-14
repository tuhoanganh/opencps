
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

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.opencps.dossiermgt.NoSuchDossierException;
import org.opencps.dossiermgt.NoSuchDossierStatusException;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierStatus;
import org.opencps.dossiermgt.model.FileGroup;
import org.opencps.dossiermgt.service.base.DossierLocalServiceBaseImpl;
import org.opencps.processmgt.model.WorkflowOutput;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.util.DLFolderUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.model.DLFolder;

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

	/**
	 * @param userId
	 * @param ownerOrganizationId
	 * @param dossierTemplateId
	 * @param templateFileNo
	 * @param serviceConfigId
	 * @param serviceInfoId
	 * @param serviceDomainIndex
	 * @param govAgencyOrganizationId
	 * @param govAgencyCode
	 * @param govAgencyName
	 * @param serviceMode
	 * @param serviceAdministrationIndex
	 * @param cityCode
	 * @param cityName
	 * @param districtCode
	 * @param districtName
	 * @param wardName
	 * @param wardCode
	 * @param subjectName
	 * @param subjectId
	 * @param address
	 * @param contactName
	 * @param contactTelNo
	 * @param contactEmail
	 * @param note
	 * @param dossierSource
	 * @param dossierStatus
	 * @param parentFolderId
	 * @param serviceContext
	 * @return
	 * @throws SystemException
	 * @throws PortalException
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
		String note, int dossierSource, String dossierStatus,
		long parentFolderId, String redirectPaymentURL,
		ServiceContext serviceContext)
		throws SystemException, PortalException {

		long dossierId = counterLocalService
			.increment(Dossier.class
				.getName());

		Dossier dossier = dossierPersistence
			.create(dossierId);

		int count = dossierPersistence
			.countByG_U(serviceContext
				.getScopeGroupId(), userId);

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
			.setOid(PortalUUIDUtil
				.generate());
		dossier
			.setWardCode(wardCode);
		dossier
			.setWardName(wardName);

		dossier
			.setKeypayRedirectUrl(redirectPaymentURL);

		DLFolder folder = null;

		try {
			folder = DLFolderUtil
				.getFolder(serviceContext
					.getScopeGroupId(), parentFolderId, String
						.valueOf(dossierNo));
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		if (folder == null) {
			dlFolderLocalService
				.addFolder(userId, serviceContext
					.getScopeGroupId(), serviceContext
						.getScopeGroupId(),
					false, parentFolderId, String
						.valueOf(dossierNo),
					StringPool.BLANK, false, serviceContext);
		}

		dossier = dossierPersistence
			.update(dossier);

		dossierStatusLocalService
			.addDossierStatus(userId, dossierId, 0,
				PortletConstants.DOSSIER_STATUS_NEW, PortletUtil
					.getActionInfo(PortletConstants.DOSSIER_STATUS_NEW,
						serviceContext
							.getLocale()),
				PortletUtil
					.getMessageInfo(PortletConstants.DOSSIER_STATUS_NEW,
						serviceContext
							.getLocale()),
				now, PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
				serviceContext);
		dossierLogLocalService
			.addDossierLog(userId, dossierId, 0,
				PortletConstants.DOSSIER_STATUS_NEW, PortletUtil
					.getActionInfo(PortletConstants.DOSSIER_STATUS_NEW,
						serviceContext
							.getLocale()),
				PortletUtil
					.getMessageInfo(PortletConstants.DOSSIER_STATUS_NEW,
						serviceContext
							.getLocale()),
				now, PortletConstants.DOSSIER_LOG_NORMAL, serviceContext);

		long classTypeId = 0;

		assetEntryLocalService
			.updateEntry(userId, serviceContext
				.getScopeGroupId(), ServiceInfo.class
					.getName(),
				dossier
					.getDossierId(),
				dossier
					.getOid(),
				classTypeId, serviceContext
					.getAssetCategoryIds(),
				serviceContext
					.getAssetTagNames(),
				false, now, null, null, ContentTypes.TEXT_HTML, dossier
					.getSubjectName(),
				StringPool.BLANK, StringPool.BLANK, null, null, 0, 0, 0, false);

		Indexer indexer = IndexerRegistryUtil
			.nullSafeGetIndexer(Dossier.class);

		indexer
			.reindex(dossier);

		return dossier;
	}
	
	
	public Dossier synDossier(Dossier synDossier, ServiceContext serviceContext)
		throws SystemException, PortalException {

		long dossierId = counterLocalService
			.increment(Dossier.class
				.getName());

		Dossier dossier = dossierPersistence
			.create(dossierId);

		int dossierNo = synDossier.getCounter();

		Date now = new Date();

		serviceContext
			.setAddGroupPermissions(true);
		serviceContext
			.setAddGuestPermissions(true);
/*
		dossier
			.setUserId(userId);*/
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
			.setAddress(synDossier.getAddress());
		dossier
			.setCityCode(synDossier.getCityCode());
		dossier
			.setCityName(synDossier.getCityName());
		dossier
			.setContactEmail(synDossier.getContactEmail());
		dossier
			.setContactName(synDossier.getContactName());
		dossier
			.setContactTelNo(synDossier.getContactTelNo());
		dossier
			.setCounter(dossierNo);
		dossier
			.setDistrictCode(synDossier.getDistrictCode());
		dossier
			.setDistrictName(synDossier.getDistrictName());
		dossier
			.setDossierSource(synDossier.getDossierSource());
		dossier
			.setDossierStatus(synDossier.getDossierStatus());
		dossier
			.setDossierTemplateId(synDossier.getDossierTemplateId());
		dossier
			.setGovAgencyCode(synDossier.getGovAgencyCode());
		dossier
			.setGovAgencyName(synDossier.getGovAgencyName());
		dossier
			.setGovAgencyOrganizationId(synDossier.getGovAgencyOrganizationId());
		dossier
			.setNote(synDossier.getNote());
		dossier
			.setOwnerOrganizationId(synDossier.getOwnerOrganizationId());//?
		dossier
			.setServiceAdministrationIndex(synDossier.getServiceAdministrationIndex());
		dossier
			.setServiceConfigId(synDossier.getServiceConfigId());
		dossier
			.setServiceDomainIndex(synDossier.getServiceDomainIndex());
		dossier
			.setServiceInfoId(synDossier.getServiceInfoId());
		dossier
			.setServiceMode(synDossier.getServiceMode());
		dossier
			.setSubjectId(synDossier.getSubjectId());
		dossier
			.setSubjectName(synDossier.getSubjectName());
		dossier
			.setOid(synDossier.getOid());
		dossier
			.setWardCode(synDossier.getWardCode());
		dossier
			.setWardName(synDossier.getWardName());

		dossier
			.setKeypayRedirectUrl(synDossier.getKeypayRedirectUrl());

		DLFolder folder = null;
/*
		try {
			folder = DLFolderUtil
				.getFolder(serviceContext
					.getScopeGroupId(), parentFolderId, String
						.valueOf(dossierNo));
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		if (folder == null) {
			dlFolderLocalService
				.addFolder(userId, serviceContext
					.getScopeGroupId(), serviceContext
						.getScopeGroupId(),
					false, parentFolderId, String
						.valueOf(dossierNo),
					StringPool.BLANK, false, serviceContext);
		}
*/
		dossier = dossierPersistence
			.update(dossier);

		
		/*dossierLogLocalService
			.addDossierLog(userId, dossierId, 0,
				PortletConstants.DOSSIER_STATUS_NEW, PortletUtil
					.getActionInfo(PortletConstants.DOSSIER_STATUS_NEW,
						serviceContext
							.getLocale()),
				PortletUtil
					.getMessageInfo(PortletConstants.DOSSIER_STATUS_NEW,
						serviceContext
							.getLocale()),
				now, PortletConstants.DOSSIER_LOG_NORMAL, serviceContext);*/

		long classTypeId = 0;

		/*assetEntryLocalService
			.updateEntry(userId, serviceContext
				.getScopeGroupId(), ServiceInfo.class
					.getName(),
				dossier
					.getDossierId(),
				dossier
					.getOid(),
				classTypeId, serviceContext
					.getAssetCategoryIds(),
				serviceContext
					.getAssetTagNames(),
				false, now, null, null, ContentTypes.TEXT_HTML, dossier
					.getSubjectName(),
				StringPool.BLANK, StringPool.BLANK, null, null, 0, 0, 0, false);*/

		Indexer indexer = IndexerRegistryUtil
			.nullSafeGetIndexer(Dossier.class);

		indexer
			.reindex(dossier);

		return dossier;
	}

	/**
	 * @param groupId
	 * @return
	 * @throws SystemException
	 */
	public int countByGroupId(long groupId)
		throws SystemException {

		return dossierPersistence
			.countByGroupId(groupId);
	}

	/**
	 * @param groupId
	 * @param keyword
	 * @param dossierStatus
	 * @return
	 */
	public int countDossier(
		long groupId, String keyword, String dossierStatus) {

		return dossierFinder
			.countDossier(groupId, keyword, dossierStatus);
	};

	/**
	 * @param groupId
	 * @param keyword
	 * @param domainCode
	 * @param dossierStatus
	 * @return
	 */
	public int countDossierByKeywordDomainAndStatus(
		long groupId, String keyword, String domainCode, String dossierStatus) {

		return dossierFinder
			.countDossierByKeywordDomainAndStatus(groupId, keyword, domainCode,
				dossierStatus);
	};

	/**
	 * @param groupId
	 * @param dossierStatus
	 * @return
	 * @throws SystemException
	 */
	public int countDossierByStatus(long groupId, String dossierStatus)
		throws SystemException {

		return dossierPersistence
			.countByG_DS(groupId, dossierStatus);
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

		return dossierFinder
			.countDossierByUser(groupId, userId, keyword,
				serviceDomainTreeIndex, dossierStatus);
	}

	/**
	 * @param dossierId
	 * @param accountFolder
	 * @throws NoSuchDossierException
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void deleteDossierByDossierId(long dossierId, DLFolder accountFolder)
		throws NoSuchDossierException, SystemException, PortalException {

		Dossier dossier = dossierPersistence
			.findByPrimaryKey(dossierId);
		List<FileGroup> fileGroups = fileGroupLocalService
			.getFileGroupByDossierId(dossierId);
		List<DossierFile> dossierFiles = dossierFileLocalService
			.getDossierFileByDossierId(dossierId);

		if (dossierFiles != null) {
			for (DossierFile dossierFile : dossierFiles) {
				dossierFileLocalService
					.deleteDossierFile(dossierFile);
			}
		}

		if (fileGroups != null) {
			for (FileGroup fileGroup : fileGroups) {

				fileGroupLocalService
					.deleteFileGroup(fileGroup);

			}
		}

		Indexer indexer = IndexerRegistryUtil
			.nullSafeGetIndexer(Dossier.class);

		indexer
			.delete(dossier);

		int counter = dossier
			.getCounter();

		DLFolder dlFolder = null;

		try {
			dlFolder = DLFolderUtil
				.getFolder(dossier
					.getGroupId(), accountFolder
						.getFolderId(),
					String
						.valueOf(counter));

			if (dlFolder != null) {
				dlFolderLocalService
					.deleteDLFolder(dlFolder);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		dossierPersistence
			.remove(dossier);
	}

	/**
	 * @param userId
	 * @param dossierId
	 * @throws NoSuchDossierException
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void deleteDossierByDossierId(long userId, long dossierId)
		throws NoSuchDossierException, SystemException, PortalException {

		Date now = new Date();
		Dossier dossier = dossierPersistence
			.findByPrimaryKey(dossierId);
		List<FileGroup> fileGroups = fileGroupLocalService
			.getFileGroupByDossierId(dossierId);
		List<DossierFile> dossierFiles = dossierFileLocalService
			.getDossierFileByDossierId(dossierId);

		if (fileGroups != null) {
			for (FileGroup fileGroup : fileGroups) {
				fileGroup
					.setRemoved(PortletConstants.DOSSIER_FILE_REMOVED);

				fileGroup
					.setModifiedDate(now);
				fileGroup
					.setUserId(userId);
				fileGroupLocalService
					.updateFileGroup(fileGroup);
			}
		}

		if (dossierFiles != null) {
			for (DossierFile dossierFile : dossierFiles) {
				dossierFile
					.setRemoved(PortletConstants.DOSSIER_FILE_REMOVED);
				dossierFile
					.setModifiedDate(now);
				dossierFile
					.setUserId(userId);
				dossierFileLocalService
					.updateDossierFile(dossierFile);
			}
		}

		dossier
			.setModifiedDate(now);
		dossier
			.setUserId(userId);

		Indexer indexer = IndexerRegistryUtil
			.nullSafeGetIndexer(Dossier.class);

		indexer
			.delete(dossier);

		dossierPersistence
			.update(dossier);
	}

	/**
	 * @param dossier
	 * @param userId
	 * @param govAgencyOrganizationId
	 * @param status
	 * @return
	 */
	protected Dossier getDossier(
		Dossier dossier, long userId, long govAgencyOrganizationId,
		String status) {

		dossier
			.setUserId(userId);
		dossier
			.setModifiedDate(new Date());
		if (govAgencyOrganizationId >= 0) {
			dossier
				.setGovAgencyOrganizationId(govAgencyOrganizationId);
		}

		dossier
			.setDossierStatus(status);
		return dossier;
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
	public List<Dossier> getDossier(
		long groupId, String keyword, String dossierStatus, int start, int end,
		OrderByComparator obc) {

		return dossierFinder
			.searchDossier(groupId, keyword, dossierStatus, start, end, obc);
	}

	/**
	 * @param delayStatus
	 * @return
	 * @throws SystemException
	 */
	public List<Dossier> getDossierByDelayStatus(int delayStatus)
		throws SystemException {

		return dossierPersistence
			.findByDelayStatus(delayStatus);
	}

	/**
	 * @param delayStatus
	 * @param dossierStatus
	 * @return
	 * @throws SystemException
	 */
	public List<Dossier> getDossierByDelayStatusAndNotDossierStatus(
		int delayStatus, String dossierStatus)
		throws SystemException {

		return dossierPersistence
			.findByDelayStatusAndNotDossierStatus(delayStatus, dossierStatus);
	}

	/**
	 * @param groupId
	 * @return
	 * @throws SystemException
	 */
	public List<Dossier> getDossierByGroupId(long groupId)
		throws SystemException {

		return dossierPersistence
			.filterFindByGroupId(groupId);
	}

	/**
	 * @param receptionNo
	 * @return
	 * @throws SystemException
	 */
	public Dossier getDossierByReceptionNo(String receptionNo)
		throws SystemException {

		return dossierPersistence
			.fetchByReceptionNo(receptionNo);
	}

	/**
	 * @param groupId
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 * @throws SystemException
	 */
	public List<Dossier> getDossierByStatus(
		long groupId, String dossierStatus, int start, int end,
		OrderByComparator obc)
		throws SystemException {

		return dossierPersistence
			.findByG_DS(groupId, dossierStatus, start, end, obc);
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
	public List getDossierByUser(
		long groupId, long userId, String keyword,
		String serviceDomainTreeIndex, String dossierStatus, int start, int end,
		OrderByComparator obc) {

		return dossierFinder
			.searchDossierByUser(groupId, userId, keyword,
				serviceDomainTreeIndex, dossierStatus, start, end, obc);
	}

	/**
	 * @param dossierStatus
	 * @param userId
	 * @param govAgencyOrganizationId
	 * @param syncStatus
	 * @return
	 */
	protected DossierStatus getDossierStatus(
		DossierStatus dossierStatus, long userId, long govAgencyOrganizationId,
		int syncStatus) {

		dossierStatus
			.setUserId(userId);
		dossierStatus
			.setModifiedDate(new Date());
		dossierStatus
			.setSyncStatus(syncStatus);
		return dossierStatus;
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

		return dossierFinder
			.searchDossierByKeywordDomainAndStatus(groupId, keyword, domainCode,
				dossierStatus, start, end, obc);
	}

	/**
	 * @param dossierId
	 * @param userId
	 * @param ownerOrganizationId
	 * @param dossierTemplateId
	 * @param templateFileNo
	 * @param serviceConfigId
	 * @param serviceInfoId
	 * @param serviceDomainIndex
	 * @param govAgencyOrganizationId
	 * @param govAgencyCode
	 * @param govAgencyName
	 * @param serviceMode
	 * @param serviceAdministrationIndex
	 * @param cityCode
	 * @param cityName
	 * @param districtCode
	 * @param districtName
	 * @param wardName
	 * @param wardCode
	 * @param subjectName
	 * @param subjectId
	 * @param address
	 * @param contactName
	 * @param contactTelNo
	 * @param contactEmail
	 * @param note
	 * @param dossierFolderId
	 * @param serviceContext
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 */
	public Dossier updateDossier(
		long dossierId, long userId, long ownerOrganizationId,
		long dossierTemplateId, String templateFileNo, long serviceConfigId,
		long serviceInfoId, String serviceDomainIndex,
		long govAgencyOrganizationId, String govAgencyCode,
		String govAgencyName, int serviceMode,
		String serviceAdministrationIndex, String cityCode, String cityName,
		String districtCode, String districtName, String wardName,
		String wardCode, String subjectName, String subjectId, String address,
		String contactName, String contactTelNo, String contactEmail,
		String note, long dossierFolderId, ServiceContext serviceContext)
		throws SystemException, PortalException {

		Dossier dossier = dossierPersistence
			.findByPrimaryKey(dossierId);

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
			.setDistrictCode(districtCode);
		dossier
			.setDistrictName(districtName);
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
			.setOid(PortalUUIDUtil
				.generate());
		dossier
			.setWardCode(wardCode);
		dossier
			.setWardName(wardName);

		dossier = dossierPersistence
			.update(dossier);
		Indexer indexer = IndexerRegistryUtil
			.nullSafeGetIndexer(Dossier.class);

		indexer
			.reindex(dossier);
		return dossier;
	}

	/**
	 * @param userId
	 * @param dossierId
	 * @param syncStatus
	 * @throws SystemException
	 * @throws NoSuchDossierStatusException
	 * @throws PortalException
	 */
	public void updateDossierStatus(long userId, long dossierId, int syncStatus)
		throws SystemException, NoSuchDossierStatusException, PortalException {

		Date now = new Date();

		List<DossierFile> dossierFiles = dossierFileLocalService
			.getDossierFileByD_GF(dossierId, 0);

		if (dossierFiles != null) {
			for (DossierFile dossierFile : dossierFiles) {
				dossierFile
					.setSyncStatus(syncStatus);
				dossierFile
					.setModifiedDate(now);
				if (userId != 0) {
					dossierFile
						.setUserId(userId);
				}
				dossierFileLocalService
					.updateDossierFile(dossierFile);
			}
		}

	}

	/**
	 * @param userId
	 * @param dossierId
	 * @param syncStatus
	 * @param worklows
	 * @throws SystemException
	 * @throws NoSuchDossierStatusException
	 * @throws PortalException
	 */
	public void updateDossierStatus(
		long userId, long dossierId, int syncStatus,
		List<WorkflowOutput> worklows)
		throws SystemException, NoSuchDossierStatusException, PortalException {

		Date now = new Date();

		List<DossierFile> dossierFiles = dossierFileLocalService
			.getDossierFileByD_GF(dossierId, 0);

		for (WorkflowOutput output : worklows) {

			DossierFile dossierFile = dossierFileLocalService
				.getDossierFileInUse(dossierId, output
					.getDossierPartId());

			dossierFile
				.setSyncStatus(syncStatus);
			dossierFile
				.setModifiedDate(now);

			if (userId != 0) {
				dossierFile
					.setUserId(userId);
			}

			dossierFileLocalService
				.updateDossierFile(dossierFile);

		}

	}

	/**
	 * @param dossierId
	 * @param fileGroupId
	 * @param dossierStatus
	 * @param receptionNo
	 * @param estimateDatetime
	 * @param receiveDatetime
	 * @param finishDatetime
	 * @param actor
	 * @param requestCommand
	 * @param actionInfo
	 * @param messageInfo
	 * @return
	 */
	public boolean updateDossierStatus(
		long dossierId, long fileGroupId, String dossierStatus,
		String receptionNo, Date estimateDatetime, Date receiveDatetime,
		Date finishDatetime, String actor, String requestCommand,
		String actionInfo, String messageInfo) {

		boolean result = false;
		try {

			Dossier dossier = dossierPersistence
				.findByPrimaryKey(dossierId);
			dossier
				.setReceptionNo(receptionNo);
			dossier
				.setEstimateDatetime(estimateDatetime);
			dossier
				.setReceiveDatetime(receiveDatetime);
			dossier
				.setFinishDatetime(finishDatetime);

			dossier
				.setDossierStatus(dossierStatus);

			int level = 0;
			if (dossier
				.getDossierStatus().equals(
					PortletConstants.DOSSIER_STATUS_ERROR)) {
				level = 2;
			}
			else if (dossier
				.getDossierStatus().equals(
					PortletConstants.DOSSIER_STATUS_WAITING) ||
				dossier
					.getDossierStatus().equals(
						PortletConstants.DOSSIER_STATUS_PAYING)) {
				level = 1;
			}
			dossierLogLocalService
				.addDossierLog(dossier
					.getUserId(), dossier
						.getGroupId(),
					dossier
						.getCompanyId(),
					dossierId, fileGroupId, dossierStatus, actor,
					requestCommand, actionInfo, messageInfo, level);

			dossierPersistence
				.update(dossier);

			result = true;
		}
		catch (Exception e) {
			result = false;
		}

		return result;
	}

	/**
	 * @param userId
	 * @param dossierId
	 * @param govAgencyOrganizationId
	 * @param status
	 * @param syncStatus
	 * @param fileGroupId
	 * @param level
	 * @param locale
	 * @return
	 * @throws SystemException
	 * @throws NoSuchDossierStatusException
	 * @throws PortalException
	 */
	public Dossier updateDossierStatus(
		long userId, long dossierId, long govAgencyOrganizationId,
		String status, int syncStatus, long fileGroupId, int level,
		Locale locale)
		throws SystemException, NoSuchDossierStatusException, PortalException {

		Date now = new Date();

		Dossier dossier = dossierPersistence
			.findByPrimaryKey(dossierId);
		dossier = getDossier(dossier, userId, govAgencyOrganizationId, status);
		/*
		 * DossierStatus dossierStatus = dossierStatusLocalService
		 * .getDossierStatus(dossierId); dossierStatus = getDossierStatus(
		 * dossierStatus, userId, govAgencyOrganizationId, syncStatus);
		 */

		if (fileGroupId > 0) {
			FileGroup fileGroup = fileGroupLocalService
				.getFileGroup(fileGroupId);
			List<DossierFile> dossierFiles = dossierFileLocalService
				.getDossierFileByD_GF(dossierId, fileGroup
					.getFileGroupId());
			if (dossierFiles != null) {
				for (DossierFile dossierFile : dossierFiles) {
					dossierFile
						.setSyncStatus(syncStatus);
					dossierFile
						.setModifiedDate(now);
					dossierFile
						.setUserId(userId);
					dossierFileLocalService
						.updateDossierFile(dossierFile);
				}
			}
			fileGroup
				.setSyncStatus(syncStatus);
			fileGroup
				.setModifiedDate(now);
			fileGroup
				.setUserId(userId);
			fileGroupLocalService
				.updateFileGroup(fileGroup);
		}

		List<DossierFile> dossierFiles = dossierFileLocalService
			.getDossierFileByD_GF(dossierId, 0);
		if (dossierFiles != null) {
			for (DossierFile dossierFile : dossierFiles) {
				dossierFile
					.setSyncStatus(syncStatus);
				dossierFile
					.setModifiedDate(now);
				dossierFile
					.setUserId(userId);
				dossierFileLocalService
					.updateDossierFile(dossierFile);
			}
		}

		dossierLogLocalService
			.addDossierLog(userId, dossier
				.getGroupId(), dossier
					.getCompanyId(),
				dossierId, fileGroupId, status, PortletUtil
					.getActionInfo(status, locale),
				PortletUtil
					.getMessageInfo(status, locale),
				now, level);

		/*
		 * dossierStatusLocalService .updateDossierStatus(dossierStatus);
		 */

		dossierPersistence
			.update(dossier);
		return dossier;
	}

	/**
	 * @param userId
	 * @param groupId
	 * @param companyId
	 * @param dossierId
	 * @param fileGroupId
	 * @param receptionNo
	 * @param estimateDatetime
	 * @param receiveDatetime
	 * @param finishDatetime
	 * @param dossierStatus
	 * @param actionInfo
	 * @param messageInfo
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void updateDossierStatus(
		long userId, long groupId, long companyId, long dossierId,
		long fileGroupId, String receptionNo, Date estimateDatetime,
		Date receiveDatetime, Date finishDatetime, String dossierStatus,
		String actionInfo, String messageInfo)
		throws PortalException, SystemException {

		Dossier dossier = dossierPersistence
			.fetchByPrimaryKey(dossierId);

		dossier
			.setReceptionNo(receptionNo);
		dossier
			.setEstimateDatetime(estimateDatetime);
		dossier
			.setReceiveDatetime(receiveDatetime);
		dossier
			.setFinishDatetime(finishDatetime);
		dossier
			.setDossierStatus(dossierStatus);

		dossierPersistence
			.update(dossier);

		// add DossierLog

		dossierLogLocalService
			.addDossierLog(userId, groupId, companyId, dossierId, fileGroupId,
				PortletConstants.DOSSIER_STATUS_WAITING, actionInfo,
				messageInfo, new Date(), 1);
	}

}

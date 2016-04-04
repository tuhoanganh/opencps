/**
 * OpenCPS is the open source Core Public Services software Copyright (C)
 * 2016-present OpenCPS community This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have received a
 * copy of the GNU Affero General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.accountmgt.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.opencps.accountmgt.NoSuchBusinessException;
import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.BusinessDomain;
import org.opencps.accountmgt.model.impl.BusinessDomainImpl;
import org.opencps.accountmgt.service.base.BusinessLocalServiceBaseImpl;
import org.opencps.util.DLFolderUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.PortletUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.EmailAddress;
import com.liferay.portal.model.Phone;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.model.Website;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserGroupLocalServiceUtil;
import com.liferay.portlet.announcements.model.AnnouncementsDelivery;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.util.PwdGenerator;

/**
 * The implementation of the business local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.accountmgt.service.BusinessLocalService} interface. <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author khoavd
 * @author trungnt
 * @see org.opencps.accountmgt.service.base.BusinessLocalServiceBaseImpl
 * @see org.opencps.accountmgt.service.BusinessLocalServiceUtil
 */
public class BusinessLocalServiceImpl extends BusinessLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.accountmgt.service.BusinessLocalServiceUtil} to access
	 * the business local service.
	 */

	public Business addBusiness(
	    String fullName, String enName, String shortName, String businessType,
	    String idNumber, String address, String cityCode, String districtCode,
	    String wardCode, String cityName, String districtName, String wardName,
	    String telNo, String email, String representativeName,
	    String representativeRole, String[] businessDomainCodes,
	    int birthDateDay, int birthDateMonth, int birthDateYear,
	    long repositoryId, String sourceFileName, String contentType,
	    String title, InputStream inputStream, long size,
	    ServiceContext serviceContext)
	    throws SystemException, PortalException {

		long businessId = counterLocalService
		    .increment(Business.class
		        .getName());

		Business business = businessPersistence
		    .create(businessId);

		Date now = new Date();

		PortletUtil.SplitName spn = PortletUtil
		    .splitName(fullName);

		boolean autoPassword = true;
		boolean autoScreenName = true;
		boolean sendEmail = false;

		long[] groupIds = null;
		long[] organizationIds = null;
		long[] roleIds = null;
		long[] userGroupIds = null;

		String password1 = null;
		String password2 = null;
		String screenName = null;

		UserGroup userGroup = null;
		try {
			userGroup = UserGroupLocalServiceUtil
			    .getUserGroup(serviceContext
			        .getCompanyId(),
			        PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS);
		}
		catch (Exception e) {
			_log
			    .warn(e
			        .getMessage());
		}
		if (userGroup == null) {
			userGroup = UserGroupLocalServiceUtil
			    .addUserGroup(serviceContext
			        .getUserId(), serviceContext
			            .getCompanyId(),
			        PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS,
			        StringPool.BLANK, serviceContext);

			userGroupIds = new long[] {
			    userGroup
			        .getUserGroupId()
			};
		}

		try {
			userGroup = UserGroupLocalServiceUtil
			    .getUserGroup(serviceContext
			        .getCompanyId(),
			        PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS);
		}
		catch (Exception e) {
			_log
			    .warn(e
			        .getMessage());
		}

		password1 = PwdGenerator
		    .getPassword();
		password2 = password1;

		User mappingUser = userService
		    .addUserWithWorkflow(serviceContext
		        .getCompanyId(), autoPassword, password1, password2,
		        autoScreenName, screenName, email, 0L, StringPool.BLANK,
		        LocaleUtil
		            .getDefault(),
		        spn
		            .getFirstName(),
		        spn
		            .getMidName(),
		        spn
		            .getLastName(),
		        0, 0, true, birthDateMonth, birthDateDay, birthDateYear,
		        "Business", groupIds, organizationIds, roleIds, userGroupIds,
		        new ArrayList<Address>(), new ArrayList<EmailAddress>(),
		        new ArrayList<Phone>(), new ArrayList<Website>(),
		        new ArrayList<AnnouncementsDelivery>(), sendEmail,
		        serviceContext);

		int status = WorkflowConstants.STATUS_INACTIVE;

		mappingUser = userService
		    .updateStatus(mappingUser
		        .getUserId(), status);

		String[] folderNames = new String[] {
		    PortletConstants.DestinationRoot.BUSINESS
		        .toString(),
		    cityName, districtName, wardName, String
		        .valueOf(mappingUser
		            .getUserId())
		};

		String destination = PortletUtil
		    .getDestinationFolder(folderNames);

		serviceContext
		    .setAddGroupPermissions(true);
		serviceContext
		    .setAddGuestPermissions(true);
		FileEntry fileEntry = null;
		if (size > 0 && inputStream != null) {
			DLFolder dlFolder = DLFolderUtil
			    .getTargetFolder(mappingUser
			        .getUserId(), serviceContext
			            .getScopeGroupId(),
			        repositoryId, false, 0, destination, StringPool.BLANK,
			        false, serviceContext);

			fileEntry = DLAppServiceUtil
			    .addFileEntry(repositoryId, dlFolder
			        .getFolderId(), sourceFileName, contentType, title,
			        StringPool.BLANK, StringPool.BLANK, inputStream, size,
			        serviceContext);
		}

		/*
		 * Organization org = OrganizationLocalServiceUtil .addOrganization(
		 * userId, 0, fullName + StringPool.OPEN_PARENTHESIS + idNumber +
		 * StringPool.CLOSE_PARENTHESIS,
		 * OrganizationConstants.TYPE_REGULAR_ORGANIZATION, 0, 0,
		 * ListTypeConstants.ORGANIZATION_STATUS_DEFAULT, enName, true,
		 * serviceContext);
		 */

		business
		    .setAccountStatus(PortletConstants.ACCOUNT_STATUS_REGISTERED);
		business
		    .setAddress(address);
		business
		    .setAttachFile(fileEntry != null ? fileEntry
		        .getFileEntryId() : 0);
		business
		    .setBusinessType(businessType);
		business
		    .setCityCode(cityCode);
		business
		    .setCompanyId(serviceContext
		        .getCompanyId());
		business
		    .setCreateDate(now);
		business
		    .setDistrictCode(districtCode);
		business
		    .setEmail(email);
		business
		    .setEnName(enName);
		business
		    .setGroupId(serviceContext
		        .getScopeGroupId());
		business
		    .setIdNumber(idNumber);
		/*
		 * business .setMappingOrganizationId(mappingOrganizationId);
		 */
		business
		    .setMappingUserId(mappingUser
		        .getUserId());
		business
		    .setModifiedDate(now);

		business
		    .setRepresentativeName(representativeName);
		business
		    .setRepresentativeRole(representativeRole);
		business
		    .setShortName(shortName);
		business
		    .setTelNo(telNo);
		business
		    .setUserId(mappingUser
		        .getUserId());
		business
		    .setUuid(serviceContext
		        .getUuid());
		business
		    .setWardCode(wardCode);

		business = businessPersistence
		    .update(business);

		if (businessDomainCodes != null && businessDomainCodes.length > 0) {
			for (int b = 0; b < businessDomainCodes.length; b++) {
				BusinessDomain domain = new BusinessDomainImpl();
				domain
				    .setBusinessId(businessId);
				domain
				    .setBusinessDomainId(businessDomainCodes[b]);
				businessDomainPersistence
				    .update(domain);
			}
		}

		return business;
	}

	public Business getBusiness(String email) throws
	NoSuchBusinessException, SystemException {
		return businessPersistence.findByEmail(email);
	}
	
	private Log _log = LogFactoryUtil
	    .getLog(BusinessLocalServiceImpl.class
	        .getName());
}

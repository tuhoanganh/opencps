
package org.opencps.accountmgt.portlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.search.BusinessDisplayTerms;
import org.opencps.accountmgt.search.CitizenDisplayTerms;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class AccountRegPortlet extends MVCPortlet {

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long citizenId = ParamUtil
		    .getLong(renderRequest, CitizenDisplayTerms.CITIZEN_ID);

		long businessId = ParamUtil
		    .getLong(renderRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);

		try {
			if (citizenId > 0) {
				Citizen citizen = CitizenLocalServiceUtil
				    .fetchCitizen(citizenId);
				renderRequest
				    .setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
			}

			if (businessId > 0) {
				Business business = BusinessLocalServiceUtil
				    .fetchBusiness(businessId);
				renderRequest
				    .setAttribute(WebKeys.BUSINESS_ENTRY, business);
			}
		}

		catch (Exception e) {
			_log
			    .error(e);
		}

		super.render(renderRequest, renderResponse);
	}

	public void updateBusiness(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		UploadPortletRequest uploadPortletRequest = PortalUtil
		    .getUploadPortletRequest(actionRequest);

		long businessId = ParamUtil
		    .getLong(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);

		long cityId = ParamUtil
		    .getLong(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_CITY_ID);
		long districtId = ParamUtil
		    .getLong(
		        uploadPortletRequest,
		        BusinessDisplayTerms.BUSINESS_DISTRICT_ID);
		long wardId = ParamUtil
		    .getLong(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_WARD_ID);

		long repositoryId = 0;

		long size = uploadPortletRequest
		    .getSize(BusinessDisplayTerms.BUSINESS_ATTACHFILE);

		String name = ParamUtil
		    .getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_NAME);
		String enName = ParamUtil
		    .getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_ENNAME);
		String idNumber = ParamUtil
		    .getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_IDNUMBER);
		String shortName = ParamUtil
		    .getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_SHORTNAME);
		String type = ParamUtil
		    .getString(
		        uploadPortletRequest,
		        BusinessDisplayTerms.BUSINESS_BUSINESSTYPE);
		String address = ParamUtil
		    .getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_ADDRESS);
		String email = ParamUtil
		    .getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_EMAIL);
		String telNo = ParamUtil
		    .getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_TELNO);
		String representativeName = ParamUtil
		    .getString(
		        uploadPortletRequest,
		        BusinessDisplayTerms.BUSINESS_REPRESENTATIVENAME);
		String representativeRole = ParamUtil
		    .getString(
		        uploadPortletRequest,
		        BusinessDisplayTerms.BUSINESS_REPRESENTATIVEROLE);

		String contentType = uploadPortletRequest
		    .getContentType(BusinessDisplayTerms.BUSINESS_ATTACHFILE);

		String sourceFileName = uploadPortletRequest
		    .getFileName(BusinessDisplayTerms.BUSINESS_ATTACHFILE);

		String[] domain = ParamUtil
		    .getParameterValues(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_DOMAIN);

		Date defaultBirthDate = DateTimeUtil
		    .convertStringToDate("01/01/1970");

		PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultBirthDate);

		contentType = MimeTypesUtil
		    .getContentType(contentType);

		String title = "Business File";

		DictItem city = null;

		DictItem district = null;

		DictItem ward = null;

		InputStream inputStream = null;

		try {
			ServiceContext serviceContext = ServiceContextFactory
			    .getInstance(uploadPortletRequest);

			inputStream = uploadPortletRequest
			    .getFileAsStream(CitizenDisplayTerms.CITIZEN_ATTACHFILE);

			repositoryId = serviceContext
			    .getScopeGroupId();
			city = DictItemLocalServiceUtil
			    .getDictItem(cityId);
			district = DictItemLocalServiceUtil
			    .getDictItem(districtId);
			ward = DictItemLocalServiceUtil
			    .getDictItem(wardId);

			if (businessId == 0) {

				BusinessLocalServiceUtil
				    .addBusiness(
				        name, enName, shortName, type, idNumber, address, city
				            .getItemCode(),
				        district
				            .getItemCode(),
				        ward
				            .getItemCode(),
				        city
				            .getItemName(serviceContext
				                .getLocale(), true),
				        district
				            .getItemName(serviceContext
				                .getLocale(), true),
				        ward
				            .getItemName(serviceContext
				                .getLocale(), true),
				        telNo, email, representativeName, representativeRole,
				        domain, spd
				            .getDayOfMoth(),
				        spd
				            .getMonth(),
				        spd
				            .getYear(),
				        repositoryId, sourceFileName, contentType, title,
				        inputStream, size, serviceContext);
			}
			else {

			}
		}

		catch (Exception e) {
			_log
			    .error(e);

		}
		finally {

		}

	}

	public void updateCitizen(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		UploadPortletRequest uploadPortletRequest = PortalUtil
		    .getUploadPortletRequest(actionRequest);

		long citizenId = ParamUtil
		    .getLong(uploadPortletRequest, CitizenDisplayTerms.CITIZEN_ID);

		long cityId = ParamUtil
		    .getLong(uploadPortletRequest, CitizenDisplayTerms.CITIZEN_CITY_ID);
		long districtId = ParamUtil
		    .getLong(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_DISTRICT_ID);
		long wardId = ParamUtil
		    .getLong(uploadPortletRequest, CitizenDisplayTerms.CITIZEN_WARD_ID);

		long size = uploadPortletRequest
		    .getSize(CitizenDisplayTerms.CITIZEN_ATTACHFILE);

		long repositoryId = 0;

		String fullName = ParamUtil
		    .getString(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_FULLNAME);
		String personId = ParamUtil
		    .getString(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_PERSONALID);
		String adress = ParamUtil
		    .getString(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_ADDRESS);
		String email = ParamUtil
		    .getString(uploadPortletRequest, CitizenDisplayTerms.CITIZEN_EMAIL);
		String telNo = ParamUtil
		    .getString(uploadPortletRequest, CitizenDisplayTerms.CITIZEN_TELNO);

		String contentType = uploadPortletRequest
		    .getContentType(CitizenDisplayTerms.CITIZEN_ATTACHFILE);

		String sourceFileName = uploadPortletRequest
		    .getFileName(CitizenDisplayTerms.CITIZEN_ATTACHFILE);

		int birthDateDay = ParamUtil
		    .getInteger(
		        uploadPortletRequest, CitizenDisplayTerms.BIRTH_DATE_DAY);
		int birthDateMonth = ParamUtil
		    .getInteger(
		        uploadPortletRequest, CitizenDisplayTerms.BIRTH_DATE_MONTH);
		int birthDateYear = ParamUtil
		    .getInteger(
		        uploadPortletRequest, CitizenDisplayTerms.BIRTH_DATE_YEAR);
		int gender = ParamUtil
		    .getInteger(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_GENDER);

		contentType = MimeTypesUtil
		    .getContentType(contentType);

		String title = "Personal File";

		DictItem city = null;

		DictItem district = null;

		DictItem ward = null;

		InputStream inputStream = null;

		try {
			ServiceContext serviceContext = ServiceContextFactory
			    .getInstance(actionRequest);

			inputStream = uploadPortletRequest
			    .getFileAsStream(CitizenDisplayTerms.CITIZEN_ATTACHFILE);

			repositoryId = serviceContext
			    .getScopeGroupId();

			city = DictItemLocalServiceUtil
			    .getDictItem(cityId);

			district = DictItemLocalServiceUtil
			    .getDictItem(districtId);

			ward = DictItemLocalServiceUtil
			    .getDictItem(wardId);

			if (citizenId == 0) {
				CitizenLocalServiceUtil
				    .addCitizen(
				        fullName, personId, gender, birthDateDay,
				        birthDateMonth, birthDateYear, adress, city
				            .getItemCode(),
				        district
				            .getItemCode(),
				        ward
				            .getItemCode(),
				        city
				            .getItemName(serviceContext
				                .getLocale(), true),
				        district
				            .getItemName(serviceContext
				                .getLocale(), true),
				        ward
				            .getItemName(serviceContext
				                .getLocale(), true),
				        email,

				        telNo, repositoryId, sourceFileName, contentType, title,
				        inputStream, size, serviceContext);
			}
			else {

			}

		}
		catch (Exception e) {
			_log
			    .error(e);
		}
		finally {

		}
	}

	protected void ValidateCitizen(
	    long citizenId, String fullName, String personalId, String address,
	    String email, String telNo, long size, String mimeType) {

	}

	private Log _log = LogFactoryUtil
	    .getLog(AccountRegPortlet.class
	        .getName());
}

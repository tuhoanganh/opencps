
package org.opencps.accountmgt.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.opencps.datamgt.NoSuchDictItemException;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class AccountRegPortlet extends MVCPortlet {

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long citizenId =
		    ParamUtil.getLong(renderRequest, CitizenDisplayTerms.CITIZEN_ID);

		long businessId =
		    ParamUtil.getLong(
		        renderRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);

		try {
			if (citizenId > 0) {
				Citizen citizen =
				    CitizenLocalServiceUtil.fetchCitizen(citizenId);
				renderRequest.setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
			}

			if (businessId > 0) {
				Business business =
				    BusinessLocalServiceUtil.fetchBusiness(businessId);
				renderRequest.setAttribute(WebKeys.BUSINESS_ENTRY, business);
			}
		}

		catch (Exception e) {
			_log.error(e);
		}

		super.render(renderRequest, renderResponse);
	}

	public void updateBusiness(
	    ActionRequest actionRequest, ActionResponse actionResponse)

	    throws FileNotFoundException {

		_log.info("go here");
		
		long dictCollectionId =
		    ParamUtil.getLong(actionRequest, "dictCollectionId");
		long businessId =
		    ParamUtil.getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);
		String name =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_NAME);
		String enName =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_ENNAME);
		String idNumber =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_IDNUMBER);
		String shortName =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_SHORTNAME);
		String type =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_BUSINESSTYPE);
		String address =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_ADDRESS);
		String cityCode =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_CITYCODE);
		String districtCode =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_DISTRICTCODE);
		String wardCode =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_WARDCODE);
		String email =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_EMAIL);
		String telNo =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_TELNO);
		String representativeName =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_REPRESENTATIVENAME);
		String representativeRole =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_REPRESENTATIVEROLE);

		String [] domain =
		    ParamUtil.getParameterValues(
		        actionRequest, "businessDomains", null);
		
		for(int i = 0; i < domain.length ; i++) {
			_log.info(" domain " + domain[i].toString() + " == ");
		}

		UploadPortletRequest uploadPortletRequest =
		    PortalUtil.getUploadPortletRequest(actionRequest);

		String mimeType = uploadPortletRequest.getContentType();
		_log.info("== mimeType " + mimeType + "  telNo" + telNo + " domain " +
		    domain.length);

		File attachFile = (File) uploadPortletRequest.getFile("attachFile");

		InputStream inputStream = new FileInputStream(attachFile);

		Date defaultBirthDate = DateTimeUtil.convertStringToDate("01/01/1970");
		PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultBirthDate);

		ServiceContext serviceContext;
		try {

			if (businessId == 0) {
				_log.info("====update");
				try {
					serviceContext =
					    ServiceContextFactory.getInstance(actionRequest);
					BusinessLocalServiceUtil.addBusiness(
					    name, enName, shortName, type, idNumber, address,
					    cityCode, districtCode, wardCode,
					    getNameOfDataItem(dictCollectionId, cityCode),
					    getNameOfDataItem(dictCollectionId, districtCode),
					    getNameOfDataItem(dictCollectionId, wardCode), telNo,
					    email, representativeName, representativeRole, domain,
					    spd.getDayOfMoth(), spd.getMonth(), spd.getYear(),
					    serviceContext.getScopeGroupId(), attachFile.getName(),
					    mimeType, StringPool.BLANK, inputStream,
					    attachFile.getTotalSpace(), serviceContext);
				}
				catch (NoSuchDictItemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (PortalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else {
				/* BusinessLocalServiceUtil.updateBusiness(business); */
			}
		}
		catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateCitizen(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws PortalException, SystemException, FileNotFoundException {
		
		long dictCollectionId = ParamUtil.getLong(
			actionRequest, "dictCollectionId");
		long citizenId =
		    ParamUtil.getLong(actionRequest, CitizenDisplayTerms.CITIZEN_ID);
		String fullName =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_FULLNAME);
		String personId =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_PERSONALID);
		String adress =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_ADDRESS);
		String cityCode =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_CITYCODE);
		String districtCode =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_DISTRICTCODE);
		String wardCode =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_WARDCODE);
		String email =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_EMAIL);
		String telNo =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_TELNO);
		int birthDateDay =
		    ParamUtil.getInteger(
		        actionRequest, CitizenDisplayTerms.BIRTH_DATE_DAY);
		int birthDateMonth =
		    ParamUtil.getInteger(
		        actionRequest, CitizenDisplayTerms.BIRTH_DATE_MONTH);
		int birthDateYear =
		    ParamUtil.getInteger(
		        actionRequest, CitizenDisplayTerms.BIRTH_DATE_YEAR);
		int gender =
		    ParamUtil.getInteger(
		        actionRequest, CitizenDisplayTerms.CITIZEN_GENDER);
		UploadPortletRequest uploadPortletRequest =
		    PortalUtil.getUploadPortletRequest(actionRequest);

		String mimeType = uploadPortletRequest.getContentType();
		File attachFile = (File) uploadPortletRequest.getFile("attachFile");

		InputStream inputStream = new FileInputStream(attachFile);
		ServiceContext serviceContext =
		    ServiceContextFactory.getInstance(actionRequest);
		if (citizenId == 0) {
			CitizenLocalServiceUtil.addCitizen(
			    fullName, personId, gender, birthDateDay, birthDateMonth,
			    birthDateYear, adress, cityCode, districtCode, wardCode, 
			    getNameOfDataItem(dictCollectionId, cityCode),
			    getNameOfDataItem(dictCollectionId, districtCode),
			    getNameOfDataItem(dictCollectionId, wardCode), email, telNo, 
			    serviceContext.getScopeGroupId(), attachFile.getName(), mimeType, 
			    StringPool.BLANK, inputStream,attachFile.getTotalSpace(),
			    serviceContext);
		}
		else {
			/*CitizenLocalServiceUtil.updateCitizen(citizen)*/
		}
	}

	private String getNameOfDataItem(long dictCollectionId, String itemCode)
	    throws NoSuchDictItemException, SystemException {

		DictItem dictItem =
		    DictItemLocalServiceUtil.getDictItemInuseByItemCode(
		        dictCollectionId, itemCode);
		return dictItem != null ? dictItem.getItemName() : StringPool.BLANK;

	}

	private Log _log = LogFactoryUtil.getLog(AccountRegPortlet.class.getName());
}

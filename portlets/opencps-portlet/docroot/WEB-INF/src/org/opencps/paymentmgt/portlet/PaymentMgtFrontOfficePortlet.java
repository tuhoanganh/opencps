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
package org.opencps.paymentmgt.portlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.opencps.dossiermgt.NoSuchDossierException;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.keypay.model.KeyPay;
import org.opencps.paymentmgt.NoSuchPaymentConfigException;
import org.opencps.paymentmgt.NoSuchPaymentFileException;
import org.opencps.paymentmgt.model.PaymentConfig;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.search.PaymentFileDisplayTerms;
import org.opencps.paymentmgt.service.PaymentConfigLocalServiceUtil;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungdk
 */
public class PaymentMgtFrontOfficePortlet extends MVCPortlet {
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void keypayTransaction(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {
		addProcessActionSuccessMessage = false;
		long paymentFileId = ParamUtil.getLong(actionRequest, PaymentFileDisplayTerms.PAYMENT_FILE_ID, 0L);
		PaymentFile paymentFile = null;
		try {
			paymentFile = PaymentFileLocalServiceUtil.getPaymentFile(paymentFileId);
		}
		catch (NoSuchPaymentFileException e) {
			
		}
        catch (PortalException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
        catch (SystemException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		PaymentConfig paymentConfig = null;
		try {
			if (paymentFile != null)
				paymentConfig = PaymentConfigLocalServiceUtil.getPaymentConfigByGovAgency(PortalUtil.getScopeGroupId(actionRequest), paymentFile.getGovAgencyOrganizationId());
		}
		catch (NoSuchPaymentConfigException e) {
			
		}
        catch (PortalException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
        catch (SystemException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		System.out.println("----REDIRECT KEYPAY----");
		if (paymentConfig != null) {
			Date curDate = new Date();
			boolean updatePaymentFile = false;
	        String merchant_trans_id = String.valueOf(paymentFile.getKeypayTransactionId());
	        if (paymentFile.getKeypayTransactionId() == 0) {
	        	SimpleDateFormat transFormat = new SimpleDateFormat("HHmmss");
	        	paymentFile.setKeypayTransactionId(Integer.parseInt(transFormat.format(curDate)));
	        	updatePaymentFile = true;
	        }
	        String merchant_code = paymentConfig.getKeypayMerchantCode();
	        String good_code = paymentFile.getKeypayGoodCode();
	        if (Validator.isNull(paymentFile.getKeypayGoodCode()) || "".equals(paymentFile.getKeypayGoodCode())) {
	        	Dossier dossier = null;
	        	try {
	        		dossier = DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());
	        		paymentFile.setKeypayGoodCode("GC_" + dossier.getReceptionNo());
	        	}
	        	catch (NoSuchDossierException e) {
	        		
	        	}
                catch (PortalException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
                }
                catch (SystemException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
                }
	        	//paymentFile.setKeypayGoodCode("GC_" + paymentFile.getDossierId());
	        	good_code = paymentFile.getKeypayGoodCode();
	        	updatePaymentFile = true;
	        }
	        String net_cost = String.valueOf((int)paymentFile.getAmount());
	        String ship_fee = "0";
	        String tax = "0";
	        
	        String bank_code = ParamUtil.getString(actionRequest, "bankCode");
	        String service_code = "720";
	        String version = paymentConfig.getKeypayVersion();
	        String command = "pay";
	        String currency_code = "704";
	        String desc_1 = "";
	        String desc_2 = "";
	        String desc_3 = "";
	        String desc_4 = "";
	        String desc_5 = "";
	        String xml_description = "";
	        String current_locale = "vn";
	        String country_code = "+84";
	        String internal_bank = "all_card";
	        String merchant_secure_key = paymentConfig.getKeypaySecureKey();
	        if (updatePaymentFile) {
	        	try {
	        		PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);
	        	}
	        	catch (SystemException e) {
	        		
	        	}
	        }
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String portletName = (String)actionRequest.getAttribute(WebKeys.PORTLET_ID);
			PortletURL redirectURL = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(actionRequest),
				portletName,
				themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE); 
			redirectURL.setParameter("jspPage", templatePath + "frontofficeconfirmkeypay.jsp");	        
	        String return_url = redirectURL.toString();
	        KeyPay keypay = new KeyPay(merchant_trans_id, merchant_code, good_code,
                net_cost, ship_fee, tax, bank_code, service_code, version, command,
                currency_code, desc_1, desc_2, desc_3, desc_4, desc_5, xml_description,
                current_locale, country_code, return_url, internal_bank, merchant_secure_key);
	        keypay.setKeypay_url(paymentConfig.getKeypayDomain());
	        
	        String url_redirect = paymentConfig.getKeypayDomain();
	        String param = "";
	        param += "merchant_code=" + URLEncoder.encode(keypay.getMerchant_code(), "UTF-8") + "&";
	        param += "merchant_secure_key=" + URLEncoder.encode(keypay.getMerchant_secure_key(), "UTF-8") + "&";
	        param += "bank_code=" + URLEncoder.encode(keypay.getBank_code(), "UTF-8") + "&";
	        param += "internal_bank=" + URLEncoder.encode(keypay.getInternal_bank(), "UTF-8") + "&";
	        param += "merchant_trans_id=" + URLEncoder.encode(keypay.getMerchant_trans_id(), "UTF-8") + "&";
	        param += "good_code=" + URLEncoder.encode(keypay.getGood_code(), "UTF-8") + "&";
	        param += "net_cost=" + URLEncoder.encode(keypay.getNet_cost(), "UTF-8") + "&";
	        param += "ship_fee=" + URLEncoder.encode(keypay.getShip_fee(), "UTF-8") + "&";
	        param += "tax=" + URLEncoder.encode(keypay.getTax(), "UTF-8") + "&";
	        param += "return_url=" + URLEncoder.encode(keypay.getReturn_url(), "UTF-8") + "&";
	        param += "version=" + URLEncoder.encode(keypay.getVersion(), "UTF-8") + "&";
	        param += "command=" + URLEncoder.encode(keypay.getCommand(), "UTF-8") + "&";
	        param += "current_locale=" + URLEncoder.encode(keypay.getCurrent_locale(), "UTF-8") + "&";
	        param += "currency_code=" + URLEncoder.encode(keypay.getCurrency_code(), "UTF-8") + "&";
	        param += "service_code=" + URLEncoder.encode(keypay.getService_code(), "UTF-8") + "&";
	        param += "country_code=" + URLEncoder.encode(keypay.getCountry_code(), "UTF-8") + "&";
	        param += "desc_1=" + URLEncoder.encode(keypay.getDesc_1(), "UTF-8") + "&";
	        param += "desc_2=" + URLEncoder.encode(keypay.getDesc_2(), "UTF-8") + "&";
	        param += "desc_3=" + URLEncoder.encode(keypay.getDesc_3(), "UTF-8") + "&";
	        param += "desc_4=" + URLEncoder.encode(keypay.getDesc_4(), "UTF-8") + "&";
	        param += "desc_5=" + URLEncoder.encode(keypay.getDesc_5(), "UTF-8") + "&";
	        param += "xml_description=" + URLEncoder.encode(keypay.getXml_description(), "UTF-8") + "&";
	        
	        url_redirect += param + "secure_hash=" + keypay.getSecure_hash();
	        actionResponse.sendRedirect(url_redirect);
		}
	}
	
	private Log _log = LogFactoryUtil
				    .getLog(PaymentMgtFrontOfficePortlet.class
				        .getName());

}

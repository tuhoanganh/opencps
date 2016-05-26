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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

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
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.sun.research.ws.wadl.Param;

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
				paymentConfig = PaymentConfigLocalServiceUtil.getPaymentConfig(paymentFile.getPaymentFileId());
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
	        String merchant_trans_id = String.valueOf(paymentFile.getKeypayTransactionId());
	        String merchant_code = paymentConfig.getKeypayMerchantCode();
	        String good_code = paymentFile.getKeypayGoodCode();
	        String net_cost = String.valueOf(paymentFile.getAmount());
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
	        String internal_bank = "";
	        String merchant_secure_key = "all_card";
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
	        actionResponse.sendRedirect(url_redirect);
		}
	}
	
	private Log _log = LogFactoryUtil
				    .getLog(PaymentMgtFrontOfficePortlet.class
				        .getName());

}

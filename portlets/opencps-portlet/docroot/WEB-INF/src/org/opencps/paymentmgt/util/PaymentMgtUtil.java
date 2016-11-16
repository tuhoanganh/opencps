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

package org.opencps.paymentmgt.util;

import java.util.ArrayList;
import java.util.List;

import org.opencps.backend.message.UserActionMsg;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.keypay.model.KeyPay;
import org.opencps.paymentmgt.model.PaymentConfig;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.model.PaymentGateConfig;
import org.opencps.paymentmgt.model.impl.PaymentConfigImpl;
import org.opencps.paymentmgt.service.PaymentConfigLocalServiceUtil;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.util.WebKeys;
import org.opencps.vtcpay.model.VTCPay;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author trungdk
 */
public class PaymentMgtUtil {

	public static final int PAYMENT_STATUS_REQUESTED = 0;
	public static final int PAYMENT_STATUS_CONFIRMED = 1;
	public static final int PAYMENT_STATUS_APPROVED = 2;
	public static final int PAYMENT_STATUS_REJECTED = 3;

	public static final int PAYMENT_METHOD_CASH = 1;
	public static final int PAYMENT_METHOD_KEYPAY = 2;
	public static final int PAYMENT_METHOD_BANK = 3;

	public static final int PAYMENT_STATUS_KEYPAY_OK = 0;
	public static final int PAYMENT_STATUS_KEYPAY_PENDING = 1;

	/**
	 * @param ownerUserId
	 * @param ownerOrgId
	 * @return
	 */
	public static String getOwnerPayment(long ownerUserId, long ownerOrgId) {

		String ownerName = StringPool.BLANK;

		if (ownerUserId != 0) {
			try {
				User user = UserLocalServiceUtil.fetchUser(ownerUserId);
				ownerName = user.getFullName();
			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		if (ownerOrgId != 0) {
			try {
				Organization org = OrganizationLocalServiceUtil.fetchOrganization(ownerOrgId);
				ownerName = org.getName();
			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		return ownerName;
	}

	/**
	 * @param paymentFileId
	 * @return
	 */
	public static String getPaymentMethod(long paymentFileId) {

		String paymentMethodName = StringPool.BLANK;

		PaymentFile paymentFile = null;

		try {
			paymentFile = PaymentFileLocalServiceUtil.fetchPaymentFile(paymentFileId);

			int paymentMethod = paymentFile.getPaymentMethod();

			if (paymentFile.getPaymentStatus() == 1 || paymentFile.getPaymentStatus() == 2) {

				switch (paymentMethod) {
				case 1:
					paymentMethodName = "payment-method-cash";
					break;
				case 2:
					paymentMethodName = "payment-method-keypay";
					break;
				case 4:
					paymentMethodName = "payment-method-bank";
					break;
				default:
					break;
				}

			}
		}
		catch (Exception e) {
			_log.error(e);
		}

		return paymentMethodName;
	}

	/**
	 * Get dossierStatus by PaymentFile
	 * 
	 * @param paymentFile
	 * @return String (DossierStatus)
	 */
	public static String getDossierStatus(PaymentFile paymentFile) {

		String dossierStatus = StringPool.BLANK;

		try {
			Dossier dossier = DossierLocalServiceUtil.fetchDossier(paymentFile.getDossierId());

			dossierStatus = dossier.getDossierStatus();

		}
		catch (Exception e) {
			_log.error(e);
		}

		return dossierStatus;

	}

	public static PaymentConfig validatePaymentConfig(long groupId, long govAgencyOrganizationId) {

		PaymentConfig paymentConfigValid = null;
		
		_log.info("=====groupId:"+groupId);
		_log.info("=====govAgencyOrganizationId:"+govAgencyOrganizationId);

		List<PaymentConfig> paymentConfigList = new ArrayList<PaymentConfig>();

		try {
			paymentConfigList =
				PaymentConfigLocalServiceUtil.getPaymentConfigListByGovAgencyAndStatus(
					groupId, govAgencyOrganizationId, true);
		}
		catch (SystemException e) {
			_log.error(e);
		}

		if (paymentConfigList.size() == 1) {
			paymentConfigValid = paymentConfigList.get(0);
		}

		return paymentConfigValid;

	}

	public static String runVTCGateData(VTCPay vtcPay, VTCPay vtcPayData) {

		PaymentGateConfig paymentGateConfig = null;
		Dossier dossier = null;
		PaymentFile paymentFile = null;

		boolean isVerify = VTCPay.validateSign(vtcPay, vtcPayData);
		
		_log.info("=====vtcPay.getStatus():"+vtcPay.getStatus());

		if (isVerify) {

			if (vtcPayData.getStatus().equals("1")) {
				try {
					paymentFile =
						PaymentFileLocalServiceUtil.getByTransactionId(Long.parseLong(vtcPayData.getReference_number()));

					dossier = DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());

				}
				catch (NumberFormatException | PortalException | SystemException e) {
					// TODO Auto-generated catch block
					_log.info(e);
				}
				
				if(Validator.isNotNull(paymentFile) && paymentFile.getPaymentGateStatusCode().equals("1")){

					UserActionMsg actionMsg = new UserActionMsg();
	
					actionMsg.setAction(WebKeys.ACTION_PAY_VALUE);
	
					actionMsg.setPaymentFileId(paymentFile.getPaymentFileId());
	
					actionMsg.setDossierId(paymentFile.getDossierId());
	
					actionMsg.setCompanyId(dossier.getCompanyId());
	
					actionMsg.setGovAgencyCode(dossier.getGovAgencyCode());
	
					Message message = new Message();
	
					message.put("msgToEngine", actionMsg);
	
					MessageBusUtil.sendMessage("opencps/frontoffice/out/destination", message);
	
					paymentFile.setPaymentStatus(PaymentMgtUtil.PAYMENT_STATUS_APPROVED);
					paymentFile.setPaymentMethod(WebKeys.PAYMENT_METHOD_VTCPAY);
				}
			}
			else {
				paymentFile.setPaymentGateStatusCode(vtcPayData.getStatus());
			}

		}
		else {
			paymentFile.setPaymentGateStatusCode("-100");
		}

		JSONObject jsonData = JSONFactoryUtil.createJSONObject();

		jsonData.put("data", vtcPay.getData());
		jsonData.put("signature", vtcPay.getSignature());

		paymentFile.setPaymentGateResponseData(jsonData.toString());

		try {
			PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);
		}
		catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static String runKeyPayGateData(KeyPay keyPay) {

		return null;
	}

	public static Log _log = LogFactoryUtil.getLog(PaymentMgtUtil.class);
}

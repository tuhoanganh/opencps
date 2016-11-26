/**
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.backend.scheduler;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLogLocalServiceUtil;
import org.opencps.dossiermgt.util.ActorBean;
import org.opencps.notificationmgt.utils.NotificationUtils;
import org.opencps.paymentmgt.model.PaymentConfig;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.service.PaymentConfigLocalServiceUtil;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.paymentmgt.util.PaymentMgtUtil;
import org.opencps.paymentmgt.util.VTCPayEventKeys;
import org.opencps.paymentmgt.vtcpay.model.VTCPay;
import org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransSoapProxy;
import org.opencps.util.PortletConstants;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author nhanhoang
 */
public class CheckPaymentStatus implements MessageListener {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay
	 * .portal.kernel.messaging.Message)
	 */
	@Override
	public void receive(Message message)
		throws MessageListenerException {

		int[] paymentGateStatus = VTCPayEventKeys.NEED_CHECK_STATUS;
		int[] recheckStatus = VTCPayEventKeys.RECHECK_STATUS;
		List<PaymentFile> paymentFileList = new ArrayList<PaymentFile>();

		try {
			paymentFileList =
				PaymentFileLocalServiceUtil.getPaymentFileByParam(
					new int[] {}, paymentGateStatus,recheckStatus, true);
		}
		catch (PortalException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		Dossier dossier = null;

		if (paymentFileList.size() > 0) {

			PaymentConfig paymentConfig = null;

			for (PaymentFile paymentFile : paymentFileList) {

				try {
					paymentConfig =
						PaymentConfigLocalServiceUtil.getPaymentConfig(paymentFile.getPaymentConfig());
				}
				catch (PortalException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (Validator.isNotNull(paymentConfig)) {

					VTCPay vtcPay =
						new VTCPay(
							paymentConfig.getKeypayMerchantCode(),
							String.valueOf(paymentFile.getKeypayTransactionId()),
							paymentConfig.getBankInfo(), paymentConfig.getKeypaySecureKey());

					int website_id = 0;
					String order_code = StringPool.BLANK;
					String receiver_acc = StringPool.BLANK;
					String sign = StringPool.BLANK;

					website_id = Integer.valueOf(vtcPay.getWebsite_id());
					order_code = vtcPay.getOrder_code();
					receiver_acc = vtcPay.getReceiver_acc();
					sign = VTCPay.getSecureHashCodeCheckRequest(vtcPay);

					String dataResult = StringPool.BLANK;

					WSCheckTransSoapProxy checkTransSoapProxy = new WSCheckTransSoapProxy();

					try {
						dataResult =
							checkTransSoapProxy.checkPartnerTransation(
								website_id, order_code, receiver_acc, sign);
					}
					catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (dataResult.trim().length() > 0) {
						VTCPay vtcPayResult = VTCPay.getSecureHashCodeCheckResponse(dataResult);

						if (vtcPayResult.getOrder_code().trim().length() > 0) {

							long transactionId = 0;
							transactionId = Long.parseLong(vtcPayResult.getOrder_code());

							if (paymentFile.getKeypayTransactionId() == transactionId) {

								if (vtcPayResult.getResponsecode().equals(VTCPayEventKeys.SUCCESS)) {
									
									//kiem tra neu trang thai thanh cong thi cap nhat paymentfile,log
									//gui notice

									try {
										dossier =
											DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());
									}
									catch (PortalException | SystemException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									paymentFile.setPaymentStatus(PaymentMgtUtil.PAYMENT_STATUS_APPROVED);
									paymentFile.setPaymentMethod(WebKeys.PAYMENT_METHOD_VTCPAY);
									paymentFile.setPaymentGateStatusCode(Integer.valueOf(VTCPayEventKeys.SUCCESS));

									JSONObject jsonObject = null;
									try {
										jsonObject =
											JSONFactoryUtil.createJSONObject(paymentFile.getPaymentGateResponseData());
									}
									catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

									jsonObject.put("status", VTCPayEventKeys.SUCCESS);

									paymentFile.setPaymentGateResponseData(jsonObject.toString());
									
									if(Validator.isNotNull(dossier)){
										
										ActorBean actorBean = new ActorBean(1, dossier.getUserId());
	
										try {
											DossierLogLocalServiceUtil.addDossierLog(
												dossier.getUserId(), dossier.getGroupId(),
												dossier.getCompanyId(), dossier.getDossierId(),
												paymentFile.getFileGroupId(),
												PortletConstants.DOSSIER_STATUS_NEW,
												PortletConstants.DOSSIER_ACTION_CONFIRM_PAYMENT,
												PortletConstants.DOSSIER_ACTION_CONFIRM_PAYMENT,
												new Date(), 1, actorBean.getActor(),
												actorBean.getActorId(), actorBean.getActorName());
										}
										catch (SystemException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
	
										NotificationUtils.sendNotificationToAccountant(
											dossier, paymentFile);
									}

								}
								else {

								}
								//truong hop khong tra ve thanh cong
								//thi van luu lieu check , bao gom ca loi
								JSONObject jsonData = JSONFactoryUtil.createJSONObject();
								jsonData.put("reponsecode", vtcPayResult.getResponsecode());
								jsonData.put("order_code", vtcPayResult.getOrder_code());
								jsonData.put("amount", vtcPayResult.getAmount());

								paymentFile.setPaymentGateCheckCode(Integer.valueOf(vtcPayResult.getResponsecode()));
								paymentFile.setPaymentGateCheckResponseData(jsonData.toString());

								try {
									PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);
								}
								catch (SystemException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}

					}

				}

			}
		}

	}
	private Log _log = LogFactoryUtil.getLog(CheckPaymentStatus.class);

}

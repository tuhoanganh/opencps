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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.opencps.paymentmgt.model.PaymentConfig;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.service.PaymentConfigLocalServiceUtil;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.vtcpay.model.VTCPay;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
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
		
		_log.info("*****checkPaymentStatus*****");

		int[] paymentStatus = new int[] {};
		String[] paymentGateStatus = new String[] {
			"7,-21,-23,-99"
		};
		List<PaymentFile> paymentFileList = new ArrayList<PaymentFile>();

		try {
			paymentFileList =
				PaymentFileLocalServiceUtil.getPaymentFileByParam(
					paymentStatus, paymentGateStatus, true);
		}
		catch (PortalException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		_log.info("paymentFileList.size():"+paymentFileList.size());

		SchedulerUtils schedulerUtil = new SchedulerUtils();

		if (paymentFileList.size() > 0) {

			PaymentConfig paymentConfig = null;

			for (PaymentFile paymentFile : paymentFileList) {

				StringBuffer requestURL = new StringBuffer();
				StringBuffer params = new StringBuffer();

				try {
					paymentConfig =
						PaymentConfigLocalServiceUtil.getPaymentConfig(paymentFile.getPaymentConfig());
				}
				catch (PortalException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (Validator.isNotNull(paymentConfig)) {

					requestURL.append(paymentConfig.getCheckUrl().toString());

					VTCPay vtcPay =
						new VTCPay(
							paymentConfig.getKeypayMerchantCode(), paymentFile.getKeypayGoodCode(),
							paymentConfig.getBankInfo(), StringPool.BLANK);

					params.append("?");
					try {
						params.append("website_id=").append(
							URLEncoder.encode(vtcPay.getWebsite_id(), CHAR_SET));
						params.append("&order_code=").append(
							URLEncoder.encode(vtcPay.getOrder_code(), CHAR_SET));
						params.append("&receiver_acc=").append(
							URLEncoder.encode(vtcPay.getReceiver_acc(), CHAR_SET));
						params.append("&sign=").append(
							URLEncoder.encode(
								VTCPay.getSecureHashCodeRequest1(paymentConfig, vtcPay), CHAR_SET));
					}
					catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					requestURL.append(params.toString());
					
					_log.info("requestURL.toString():"+requestURL.toString());

					try {

						URL obj = new URL(requestURL.toString());
						HttpURLConnection con = (HttpURLConnection) obj.openConnection();

						con.setRequestMethod("POST");
						con.setRequestProperty("User-Agent", USER_AGENT);
						con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
						con.setDoOutput(true);

						DataOutputStream wr = new DataOutputStream(con.getOutputStream());
						wr.flush();
						wr.close();

						int responseCode = 0;
						responseCode = con.getResponseCode();
						System.out.println("Response Code : " + responseCode);

						BufferedReader in =
							new BufferedReader(new InputStreamReader(con.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();

						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();

						System.out.println("response.toString():" + response.toString());

					}
					catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		}

	}

	private Log _log = LogFactoryUtil.getLog(CheckPaymentStatus.class);
	private final String USER_AGENT = "Mozilla/5.0";
	private final String CHAR_SET = "UTF-8";

}

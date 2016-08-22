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

package org.opencps.paymentmgt.scheduler;

import java.util.List;

import org.opencps.keypay.model.KeyPay;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.paymentmgt.util.PaymentMgtUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author trungnt
 */
public class PaymentResponseStatus implements MessageListener {

	@Override
	public void receive(Message message)
		throws MessageListenerException {

		try {

			_log.info("***************************PaymentResponseStatus");
			//get all payment file pendding
			List<PaymentFile> listPaymentFiles = PaymentFileLocalServiceUtil.getPaymentFileByPaymentResponseStatus(PaymentMgtUtil.PAYMENT_STATUS_KEYPAY_PENDING);
			KeyPay keyPay = new KeyPay();
			String result = StringPool.BLANK;
			for (PaymentFile paymentFile : listPaymentFiles) {
				result = keyPay.queryBillStatusRESTful_JSON(paymentFile.getKeypayTransactionId(), 
						paymentFile.getKeypayGoodCode(), 
						trans_id, 
						merchant_code, 
						merchant_secure_key);
			}

		}
		catch (Exception e) {
			_log
				.error(e);
		}
	}

	private Log _log = LogFactoryUtil
		.getLog(PaymentResponseStatus.class);
}

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

package org.opencps.payment.servlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opencps.keypay.model.KeyPay;
import org.opencps.paymentmgt.util.PaymentMgtUtil;
import org.opencps.vtcpay.model.VTCPay;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.util.PortalUtil;

/**
 * author nhanhoang
 */

public class PaymentGateServlet extends HttpServlet {

	private static Log _log = LogFactoryUtil.getLog(PaymentGateServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		_log.info("=====doGet():"+request.getPathInfo());
		String paymentGateName = request.getParameter("paymentGateName");

		paymentGateName = "VTCPAY";
		
		if (paymentGateName.equals("VTCPAY")) {

			VTCPay vtcPay = new VTCPay(request);
			_log.info("=====vtcPay:" + vtcPay);

			response = PaymentMgtUtil.runVTCGateData(request, response, vtcPay);

		}
		else if (paymentGateName.equals("KEYPAY")) {

			// KeyPay keyPay = new KeyPay(request);

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		String paymentGateName = request.getParameter("paymentGateName");

		paymentGateName = "VTCPAY";

		_log.info("=====doPost():" + request);
		_log.info("=====paymentGateName:" + paymentGateName);

		if (paymentGateName.equals("VTCPAY")) {

			VTCPay vtcPay = new VTCPay(request);
			_log.info("=====vtcPay:" + vtcPay);

			PaymentMgtUtil.runVTCGateData(request, response, vtcPay);

		}
		else if (paymentGateName.equals("KEYPAY")) {

			KeyPay keyPay = new KeyPay(request);

		}

	}

}

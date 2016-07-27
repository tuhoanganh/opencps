/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.opencps.api.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.opencps.api.service.ApiServiceServiceUtil;

import java.rmi.RemoteException;

/**
 * Provides the SOAP utility for the
 * {@link org.opencps.api.service.ApiServiceServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a {@link java.util.List}, that
 * is translated to an array of {@link org.opencps.api.model.ApiServiceSoap}.
 * If the method in the service utility returns a
 * {@link org.opencps.api.model.ApiService}, that is translated to a
 * {@link org.opencps.api.model.ApiServiceSoap}. Methods that SOAP cannot
 * safely wire are skipped.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/api/axis. Set the
 * property <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author trungdk
 * @see ApiServiceServiceHttp
 * @see org.opencps.api.model.ApiServiceSoap
 * @see org.opencps.api.service.ApiServiceServiceUtil
 * @generated
 */
public class ApiServiceServiceSoap {
	public static java.lang.String searchDossierByUserAssignProcessOrder(
		java.lang.String username) throws RemoteException {
		try {
			com.liferay.portal.kernel.json.JSONObject returnValue = ApiServiceServiceUtil.searchDossierByUserAssignProcessOrder(username);

			return returnValue.toString();
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String searchDossierByProcessStepAndUser(
		java.lang.String processno, java.lang.String stepno,
		java.lang.String username) throws RemoteException {
		try {
			com.liferay.portal.kernel.json.JSONObject returnValue = ApiServiceServiceUtil.searchDossierByProcessStepAndUser(processno,
					stepno, username);

			return returnValue.toString();
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String getByoid(java.lang.String oid)
		throws RemoteException {
		try {
			com.liferay.portal.kernel.json.JSONObject returnValue = ApiServiceServiceUtil.getByoid(oid);

			return returnValue.toString();
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String addDossierFile(java.lang.String oid,
		java.lang.String dossierfile) throws RemoteException {
		try {
			com.liferay.portal.kernel.json.JSONObject returnValue = ApiServiceServiceUtil.addDossierFile(oid,
					dossierfile);

			return returnValue.toString();
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String nextStep(java.lang.String oid,
		java.lang.String actioncode, java.lang.String username)
		throws RemoteException {
		try {
			com.liferay.portal.kernel.json.JSONObject returnValue = ApiServiceServiceUtil.nextStep(oid,
					actioncode, username);

			return returnValue.toString();
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String nextStep(java.lang.String oid,
		java.lang.String actioncode, java.lang.String actionnote,
		java.lang.String username) throws RemoteException {
		try {
			com.liferay.portal.kernel.json.JSONObject returnValue = ApiServiceServiceUtil.nextStep(oid,
					actioncode, actionnote, username);

			return returnValue.toString();
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String searchDossierByDS_RD_SN_U(
		java.lang.String dossierstatus, java.lang.String serviceno,
		java.lang.String fromdate, java.lang.String todate,
		java.lang.String username) throws RemoteException {
		try {
			com.liferay.portal.kernel.json.JSONObject returnValue = ApiServiceServiceUtil.searchDossierByDS_RD_SN_U(dossierstatus,
					serviceno, fromdate, todate, username);

			return returnValue.toString();
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(ApiServiceServiceSoap.class);
}
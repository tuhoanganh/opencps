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

package org.opencps.api.service.base;

import org.opencps.api.service.ApiServiceServiceUtil;

import java.util.Arrays;

/**
 * @author trungdk
 * @generated
 */
public class ApiServiceServiceClpInvoker {
	public ApiServiceServiceClpInvoker() {
		_methodName26 = "getBeanIdentifier";

		_methodParameterTypes26 = new String[] {  };

		_methodName27 = "setBeanIdentifier";

		_methodParameterTypes27 = new String[] { "java.lang.String" };

		_methodName32 = "searchDossierByUserAssignProcessOrder";

		_methodParameterTypes32 = new String[] { "java.lang.String" };

		_methodName33 = "searchDossierByProcessStepAndUser";

		_methodParameterTypes33 = new String[] {
				"java.lang.String", "java.lang.String", "java.lang.String"
			};

		_methodName34 = "getByoid";

		_methodParameterTypes34 = new String[] { "java.lang.String" };

		_methodName35 = "addDossierFile";

		_methodParameterTypes35 = new String[] {
				"java.lang.String", "java.lang.String"
			};

		_methodName36 = "nextStep";

		_methodParameterTypes36 = new String[] {
				"java.lang.String", "java.lang.String", "java.lang.String"
			};

		_methodName37 = "nextStep";

		_methodParameterTypes37 = new String[] {
				"java.lang.String", "java.lang.String", "java.lang.String",
				"java.lang.String"
			};

		_methodName38 = "searchDossierByDS_RD_SN_U";

		_methodParameterTypes38 = new String[] {
				"java.lang.String", "java.lang.String", "java.lang.String",
				"java.lang.String", "java.lang.String"
			};
	}

	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		if (_methodName26.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes26, parameterTypes)) {
			return ApiServiceServiceUtil.getBeanIdentifier();
		}

		if (_methodName27.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes27, parameterTypes)) {
			ApiServiceServiceUtil.setBeanIdentifier((java.lang.String)arguments[0]);

			return null;
		}

		if (_methodName32.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes32, parameterTypes)) {
			return ApiServiceServiceUtil.searchDossierByUserAssignProcessOrder((java.lang.String)arguments[0]);
		}

		if (_methodName33.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes33, parameterTypes)) {
			return ApiServiceServiceUtil.searchDossierByProcessStepAndUser((java.lang.String)arguments[0],
				(java.lang.String)arguments[1], (java.lang.String)arguments[2]);
		}

		if (_methodName34.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes34, parameterTypes)) {
			return ApiServiceServiceUtil.getByoid((java.lang.String)arguments[0]);
		}

		if (_methodName35.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes35, parameterTypes)) {
			return ApiServiceServiceUtil.addDossierFile((java.lang.String)arguments[0],
				(java.lang.String)arguments[1]);
		}

		if (_methodName36.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes36, parameterTypes)) {
			return ApiServiceServiceUtil.nextStep((java.lang.String)arguments[0],
				(java.lang.String)arguments[1], (java.lang.String)arguments[2]);
		}

		if (_methodName37.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes37, parameterTypes)) {
			return ApiServiceServiceUtil.nextStep((java.lang.String)arguments[0],
				(java.lang.String)arguments[1], (java.lang.String)arguments[2],
				(java.lang.String)arguments[3]);
		}

		if (_methodName38.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes38, parameterTypes)) {
			return ApiServiceServiceUtil.searchDossierByDS_RD_SN_U((java.lang.String)arguments[0],
				(java.lang.String)arguments[1], (java.lang.String)arguments[2],
				(java.lang.String)arguments[3], (java.lang.String)arguments[4]);
		}

		throw new UnsupportedOperationException();
	}

	private String _methodName26;
	private String[] _methodParameterTypes26;
	private String _methodName27;
	private String[] _methodParameterTypes27;
	private String _methodName32;
	private String[] _methodParameterTypes32;
	private String _methodName33;
	private String[] _methodParameterTypes33;
	private String _methodName34;
	private String[] _methodParameterTypes34;
	private String _methodName35;
	private String[] _methodParameterTypes35;
	private String _methodName36;
	private String[] _methodParameterTypes36;
	private String _methodName37;
	private String[] _methodParameterTypes37;
	private String _methodName38;
	private String[] _methodParameterTypes38;
}
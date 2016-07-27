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

package org.opencps.api.service.impl;

import org.opencps.api.service.base.ApiServiceLocalServiceBaseImpl;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.opencps.api.model.ApiService;
import org.opencps.api.service.base.ApiServiceLocalServiceBaseImpl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the api service local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.api.service.ApiServiceLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author trungdk
 * @see org.opencps.api.service.base.ApiServiceLocalServiceBaseImpl
 * @see org.opencps.api.service.ApiServiceLocalServiceUtil
 */
public class ApiServiceLocalServiceImpl extends ApiServiceLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.opencps.api.service.ApiServiceLocalServiceUtil} to access the api service local service.
	 */
	
	public ApiService addApiService(long userId,
			String apiCode,
			String ipAddress,
			String httpAgent,
			String params,
			String status,
			ServiceContext serviceContext) throws SystemException {

		long apiLogId = CounterLocalServiceUtil
				.increment(ApiService.class.getName());
		ApiService apiService = apiServicePersistence
				.create(apiLogId);
		Date curDate = new Date();
		apiService.setCompanyId(serviceContext.getCompanyId());
		apiService.setGroupId(serviceContext.getScopeGroupId());
		apiService.setUserId(userId);
		apiService.setCreateDate(curDate);
		apiService.setModifiedDate(curDate);
		apiService.setApiCode(apiCode);
		apiService.setIpAddress(ipAddress);
		apiService.setHttpAgent(httpAgent);
		apiService.setParams(params);
		apiService.setStatus(status);
		
		return apiServicePersistence.update(apiService);
}	
}
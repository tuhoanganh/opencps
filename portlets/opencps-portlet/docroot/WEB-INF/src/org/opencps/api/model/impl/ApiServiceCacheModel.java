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

package org.opencps.api.model.impl;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import org.opencps.api.model.ApiService;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing ApiService in entity cache.
 *
 * @author trungdk
 * @see ApiService
 * @generated
 */
public class ApiServiceCacheModel implements CacheModel<ApiService>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(25);

		sb.append("{apiLogId=");
		sb.append(apiLogId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", oid=");
		sb.append(oid);
		sb.append(", apiCode=");
		sb.append(apiCode);
		sb.append(", ipAddress=");
		sb.append(ipAddress);
		sb.append(", httpAgent=");
		sb.append(httpAgent);
		sb.append(", params=");
		sb.append(params);
		sb.append(", status=");
		sb.append(status);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public ApiService toEntityModel() {
		ApiServiceImpl apiServiceImpl = new ApiServiceImpl();

		apiServiceImpl.setApiLogId(apiLogId);
		apiServiceImpl.setCompanyId(companyId);
		apiServiceImpl.setGroupId(groupId);
		apiServiceImpl.setUserId(userId);

		if (createDate == Long.MIN_VALUE) {
			apiServiceImpl.setCreateDate(null);
		}
		else {
			apiServiceImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			apiServiceImpl.setModifiedDate(null);
		}
		else {
			apiServiceImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (oid == null) {
			apiServiceImpl.setOid(StringPool.BLANK);
		}
		else {
			apiServiceImpl.setOid(oid);
		}

		if (apiCode == null) {
			apiServiceImpl.setApiCode(StringPool.BLANK);
		}
		else {
			apiServiceImpl.setApiCode(apiCode);
		}

		if (ipAddress == null) {
			apiServiceImpl.setIpAddress(StringPool.BLANK);
		}
		else {
			apiServiceImpl.setIpAddress(ipAddress);
		}

		if (httpAgent == null) {
			apiServiceImpl.setHttpAgent(StringPool.BLANK);
		}
		else {
			apiServiceImpl.setHttpAgent(httpAgent);
		}

		if (params == null) {
			apiServiceImpl.setParams(StringPool.BLANK);
		}
		else {
			apiServiceImpl.setParams(params);
		}

		if (status == null) {
			apiServiceImpl.setStatus(StringPool.BLANK);
		}
		else {
			apiServiceImpl.setStatus(status);
		}

		apiServiceImpl.resetOriginalValues();

		return apiServiceImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		apiLogId = objectInput.readLong();
		companyId = objectInput.readLong();
		groupId = objectInput.readLong();
		userId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		oid = objectInput.readUTF();
		apiCode = objectInput.readUTF();
		ipAddress = objectInput.readUTF();
		httpAgent = objectInput.readUTF();
		params = objectInput.readUTF();
		status = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(apiLogId);
		objectOutput.writeLong(companyId);
		objectOutput.writeLong(groupId);
		objectOutput.writeLong(userId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (oid == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(oid);
		}

		if (apiCode == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(apiCode);
		}

		if (ipAddress == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(ipAddress);
		}

		if (httpAgent == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(httpAgent);
		}

		if (params == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(params);
		}

		if (status == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(status);
		}
	}

	public long apiLogId;
	public long companyId;
	public long groupId;
	public long userId;
	public long createDate;
	public long modifiedDate;
	public String oid;
	public String apiCode;
	public String ipAddress;
	public String httpAgent;
	public String params;
	public String status;
}
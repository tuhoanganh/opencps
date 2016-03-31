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
package org.opencps.accountmgt.search;

import java.util.Date;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;


public class BussinessSearchTerm extends BussinessDisplayTerms{

	public BussinessSearchTerm(PortletRequest request) {

		super(request);
		
		businessType = ParamUtil.getString(request, BUSSINESS_BUSINESSTYPE);
		shortName = ParamUtil.getString(request, BUSSINESS_SHORTNAME);
		enName = ParamUtil.getString(request, BUSSINESS_ENNAME);
		name = ParamUtil.getString(request, BUSSINESS_NAME);
		telNo = ParamUtil.getString(request, BUSSINESS_TELNO);
		email = ParamUtil.getString(request, BUSSINESS_EMAIL);
		representativeName = ParamUtil.getString(request,
			BUSSINESS_REPRESENTATIVENAME);
		representativeRole = ParamUtil.getString(request,
			BUSSINESS_REPRESENTATIVEROLE);
		address = ParamUtil.getString(request, BUSSINESS_ADDRESS);
		cityCode = ParamUtil.getString(request, BUSSINESS_CITYCODE);
		districtCode = ParamUtil.getString(request, BUSSINESS_DISTRICTCODE);
		wardCode = ParamUtil.getString(request, BUSSINESS_WARDCODE);
		uuid = ParamUtil.getString(request, BUSSINESS_UUID);
		
		createdDate = ParamUtil.getDate(request, BUSSINESS_CREATEDDATE , 
			DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		modifiedDate = ParamUtil.getDate(request, BUSSINESS_MODIFIEDDATE , 
			DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		
		businessId = ParamUtil.getLong(request, BUSSINESS_BUSINESSID);
		userId = ParamUtil.getLong(request, BUSSINESS_USERID);
		groupId = setGroupId(request);
		attachFile = ParamUtil.getLong(request, BUSSINESS_ATTACHFILE);
		mappingUserId = ParamUtil.getLong(request, BUSSINESS_MAPPINGUSERID);
		mappingOrganizationId = ParamUtil.getLong(request,
			BUSSINESS_MAPPINGORGANIZATIONID);
		
		accountStatus = ParamUtil.getInteger(request, BUSSINESS_ACCOUNTSTATUS);
	}
	
	public long setGroupId(PortletRequest portletRequest) {

		groupId = ParamUtil.getLong(portletRequest, BUSSINESS_GROUPID);

		if (groupId != 0) {
			return groupId;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);

		return themeDisplay.getScopeGroupId();
	}
	
	public String getFullName() {
	
		return fullName;
	}
	
	public void setFullName(String fullName) {
	
		this.fullName = fullName;
	}
	
	public String getPersonalId() {
	
		return personalId;
	}
	
	public void setPersonalId(String personalId) {
	
		this.personalId = personalId;
	}
	
	public String getAddress() {
	
		return address;
	}
	
	public void setAddress(String address) {
	
		this.address = address;
	}
	
	public String getCityCode() {
	
		return cityCode;
	}
	
	public void setCityCode(String cityCode) {
	
		this.cityCode = cityCode;
	}
	
	public String getDistrictCode() {
	
		return districtCode;
	}
	
	public void setDistrictCode(String districtCode) {
	
		this.districtCode = districtCode;
	}
	
	public String getWardCode() {
	
		return wardCode;
	}
	
	public void setWardCode(String wardCode) {
	
		this.wardCode = wardCode;
	}
	
	public String getTelNo() {
	
		return telNo;
	}
	
	public void setTelNo(String telNo) {
	
		this.telNo = telNo;
	}
	
	public String getEmail() {
	
		return email;
	}
	
	public void setEmail(String email) {
	
		this.email = email;
	}
	
	public String getUuid() {
	
		return uuid;
	}
	
	public void setUuid(String uuid) {
	
		this.uuid = uuid;
	}
	
	public Date getCreatedDate() {
	
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
	
		this.createdDate = createdDate;
	}
	
	public Date getModifiedDate() {
	
		return modifiedDate;
	}
	
	public void setModifiedDate(Date modifiedDate) {
	
		this.modifiedDate = modifiedDate;
	}
	
	public Date getBirthDate() {
	
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
	
		this.birthDate = birthDate;
	}
	
	public long getAttachFile() {
	
		return attachFile;
	}
	
	public void setAttachFile(long attachFile) {
	
		this.attachFile = attachFile;
	}
	
	public long getMappingUserId() {
	
		return mappingUserId;
	}
	
	public void setMappingUserId(long mappingUserId) {
	
		this.mappingUserId = mappingUserId;
	}
	
	public long getUserId() {
	
		return userId;
	}
	
	public void setUserId(long userId) {
	
		this.userId = userId;
	}
	
	public int getAccountStatus() {
	
		return accountStatus;
	}
	
	public void setAccountStatus(int accountStatus) {
	
		this.accountStatus = accountStatus;
	}
	
	public int getGender() {
	
		return gender;
	}
	
	public void setGender(int gender) {
	
		this.gender = gender;
	}

	protected String fullName;
	protected String personalId;
	protected String address;
	protected String cityCode;
	protected String districtCode;
	protected String wardCode;
	protected String telNo;
	protected String email;
	protected String uuid;
	protected Date createdDate;
	protected Date modifiedDate;
	protected Date birthDate;
	protected long attachFile;
	protected long mappingUserId;
	protected long userId;
	protected long groupId;
	protected int accountStatus; 
	protected int gender;

}

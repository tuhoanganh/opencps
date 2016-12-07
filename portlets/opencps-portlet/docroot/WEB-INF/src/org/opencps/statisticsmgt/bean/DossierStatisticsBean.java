package org.opencps.statisticsmgt.bean;

import java.util.Date;

public class DossierStatisticsBean {
	
			
	public long getProcessOrderId() {
		return _processOrderId;
	}
	public void setProcessOrderId(long processOrderId) {
		this._processOrderId = processOrderId;
	}
	public long getGroupId() {
		return _groupId;
	}
	public void setGroupId(long groupId) {
		this._groupId = groupId;
	}
	public long getProcessStepId() {
		return _processStepId;
	}
	public void setProcessStepId(long processStepId) {
		this._processStepId = processStepId;
	}
	public long getProcessWorkflowId() {
		return _processWorkflowId;
	}
	public void setProcessWorkflowId(long processWorkflowId) {
		this._processWorkflowId = processWorkflowId;
	}
	public String getGovAgencyCode() {
		return _govAgencyCode;
	}
	public void setGovAgencyCode(String govAgencyCode) {
		this._govAgencyCode = govAgencyCode;
	}
	public String getGovAgencyName() {
		return _govAgencyName;
	}
	public void setGovAgencyName(String govAgencyName) {
		this._govAgencyName = govAgencyName;
	}
	public long getDossierId() {
		return _dossierId;
	}
	public void setDossierId(long dossierId) {
		this._dossierId = dossierId;
	}
	public long getServiceInfoId() {
		return _serviceInfoId;
	}
	public void setServiceInfoId(long serviceInfoId) {
		this._serviceInfoId = serviceInfoId;
	}
	public long getServiceConfigId() {
		return _serviceConfigId;
	}
	public void setServiceConfigId(long serviceConfigId) {
		this._serviceConfigId = serviceConfigId;
	}
	public long getSubjectId() {
		return _subjectId;
	}
	public void setSubjectId(long subjectId) {
		this._subjectId = subjectId;
	}
	public Date getCreateDate() {
		return _createDate;
	}
	public void setCreateDate(Date createDate) {
		this._createDate = createDate;
	}
	public Date getModifiedDate() {
		return _modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this._modifiedDate = modifiedDate;
	}
	public Date getReceiveDatetime() {
		return _receiveDatetime;
	}
	public void setReceiveDatetime(Date receiveDatetime) {
		this._receiveDatetime = receiveDatetime;
	}
	public Date getSubmitDatetime() {
		return _submitDatetime;
	}
	public void setSubmitDatetime(Date submitDatetime) {
		this._submitDatetime = submitDatetime;
	}
	public Date getEstimateDatetime() {
		return _estimateDatetime;
	}
	public void setEstimateDatetime(Date estimateDatetime) {
		this._estimateDatetime = estimateDatetime;
	}
	public Date getFinishDatetime() {
		return _finishDatetime;
	}
	public void setFinishDatetime(Date finishDatetime) {
		this._finishDatetime = finishDatetime;
	}
	public int getDelayStatus() {
		return _delayStatus;
	}
	public void setDelayStatus(int delayStatus) {
		this._delayStatus = delayStatus;
	}
	public String getDossierStatus() {
		return _dossierStatus;
	}
	public void setDossierStatus(String dossierStatus) {
		this._dossierStatus = dossierStatus;
	}
	public String getSubjectName() {
		return _subjectName;
	}
	public void setSubjectName(String subjectName) {
		this._subjectName = subjectName;
	}
	public long getGovDictItemId() {
		return _govDictItemId;
	}
	public void setGovDictItemId(long govDictItemId) {
		this._govDictItemId = govDictItemId;
	}
	public String getGovItemCode() {
		return _govItemCode;
	}
	public void setGovItemCode(String govItemCode) {
		this._govItemCode = govItemCode;
	}
	public String getGovItemName() {
		return _govItemName;
	}
	public void setGovItemName(String govItemName) {
		this._govItemName = govItemName;
	}
	public String getGovTreeIndex() {
		return _govTreeIndex;
	}
	public void setGovTreeIndex(String govTreeIndex) {
		this._govTreeIndex = govTreeIndex;
	}
	public String getGovCollectionCode() {
		return _govCollectionCode;
	}
	public void setGovCollectionCode(String govCollectionCode) {
		this._govCollectionCode = govCollectionCode;
	}
	public long getDomainDictItemId() {
		return _domainDictItemId;
	}
	public void setDomainDictItemId(long domainDictItemId) {
		this._domainDictItemId = domainDictItemId;
	}
	public String getDomainItemCode() {
		return _domainItemCode;
	}
	public void setDomainItemCode(String domainItemCode) {
		this._domainItemCode = domainItemCode;
	}
	public String getDomainItemName() {
		return _domainItemName;
	}
	public void setDomainItemName(String domainItemName) {
		this._domainItemName = domainItemName;
	}
	public String getDomainTreeIndex() {
		return _domainTreeIndex;
	}
	public void setDomainTreeIndex(String domainTreeIndex) {
		this._domainTreeIndex = domainTreeIndex;
	}
	public String getDomainCollectionCode() {
		return _domainCollectionCode;
	}
	public void setDomainCollectionCode(String domainCollectionCode) {
		this._domainCollectionCode = domainCollectionCode;
	}
	private long _processOrderId;
	private long _groupId;
	private long _processStepId;
	private long _processWorkflowId;
	private String _govAgencyCode;
	private String _govAgencyName;
	private long _dossierId;
	private long _serviceInfoId;
	private long _serviceConfigId;
	private long _subjectId;
	private Date _createDate;
	private Date _modifiedDate;
	private Date _receiveDatetime;
	private Date _submitDatetime;
	private Date _estimateDatetime;
	private Date _finishDatetime;
	private int _delayStatus;
	private String _dossierStatus;
	private String _subjectName;
	private long _govDictItemId;
	private String _govItemCode;
	private String _govItemName;
	private String _govTreeIndex;
	private String _govCollectionCode;
	private long _domainDictItemId;
	private String _domainItemCode;
	private String _domainItemName;
	private String _domainTreeIndex;
	private String _domainCollectionCode;
}

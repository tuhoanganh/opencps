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
package org.opencps.processmgt.portlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.accountmgt.NoSuchAccountException;
import org.opencps.accountmgt.NoSuchAccountFolderException;
import org.opencps.accountmgt.NoSuchAccountOwnOrgIdException;
import org.opencps.accountmgt.NoSuchAccountOwnUserIdException;
import org.opencps.accountmgt.NoSuchAccountTypeException;
import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.backend.message.SendToEngineMsg;
import org.opencps.dossiermgt.DuplicateFileGroupException;
import org.opencps.dossiermgt.EmptyFileGroupException;
import org.opencps.dossiermgt.NoSuchDossierException;
import org.opencps.dossiermgt.NoSuchDossierFileException;
import org.opencps.dossiermgt.NoSuchDossierPartException;
import org.opencps.dossiermgt.PermissionDossierException;
import org.opencps.dossiermgt.bean.AccountBean;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.search.DossierDisplayTerms;
import org.opencps.dossiermgt.search.DossierFileDisplayTerms;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil;
import org.opencps.dossiermgt.service.FileGroupLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.model.ServiceProcess;
import org.opencps.processmgt.search.ProcessOrderDisplayTerms;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
import org.opencps.processmgt.service.ServiceProcessLocalServiceUtil;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.util.AccountUtil;
import org.opencps.util.DLFolderUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.DuplicateFileException;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 */

public class ProcessOrderPortlet extends MVCPortlet {

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void addAttachmentFile(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		boolean updated = false;

		UploadPortletRequest uploadPortletRequest = PortalUtil
			.getUploadPortletRequest(actionRequest);

		Dossier dossier = null;
		DossierFile dossierFile = null;
		DossierPart dossierPart = null;

		long dossierId = ParamUtil
			.getLong(uploadPortletRequest, DossierDisplayTerms.DOSSIER_ID);

		long dossierFileId = ParamUtil
			.getLong(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);

		long dossierPartId = ParamUtil
			.getLong(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_PART_ID);

		long fileGroupId = ParamUtil
			.getLong(uploadPortletRequest, DossierDisplayTerms.FILE_GROUP_ID);

		long size = uploadPortletRequest
			.getSize(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

		int dossierFileType = ParamUtil
			.getInteger(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_TYPE);

		int dossierFileOriginal = ParamUtil
			.getInteger(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL);

		String displayName = ParamUtil
			.getString(uploadPortletRequest,
				DossierFileDisplayTerms.DISPLAY_NAME);

		String dossierFileNo = ParamUtil
			.getString(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_NO);

		String dossierFileDate = ParamUtil
			.getString(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_DATE);

		String sourceFileName = uploadPortletRequest
			.getFileName(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

		sourceFileName = sourceFileName
			.concat(PortletConstants.TEMP_RANDOM_SUFFIX).concat(StringUtil
				.randomString());

		String redirectURL = ParamUtil
			.getString(uploadPortletRequest, "redirectURL");

		InputStream inputStream = null;

		Date fileDate = DateTimeUtil
			.convertStringToDate(dossierFileDate);

		try {
			dossier = DossierLocalServiceUtil
				.getDossier(dossierId);

			ServiceContext serviceContext = ServiceContextFactory
				.getInstance(uploadPortletRequest);
			serviceContext
				.setAddGroupPermissions(true);
			serviceContext
				.setAddGuestPermissions(true);

			AccountBean accountBean = AccountUtil
				.getAccountBean(dossier
					.getUserId(), serviceContext
						.getScopeGroupId(),
					serviceContext);

			validateAddAttachDossierFile(dossierId, dossierPartId,
				dossierFileId, displayName, size, sourceFileName, inputStream,
				accountBean);
			inputStream = uploadPortletRequest
				.getFileAsStream(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			if (dossierFileId > 0) {
				dossierFile = DossierFileLocalServiceUtil
					.getDossierFile(dossierFileId);
			}

			dossierPart = DossierPartLocalServiceUtil
				.getDossierPart(dossierPartId);

			String contentType = uploadPortletRequest
				.getContentType(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			DLFolder dossierFolder = DLFolderUtil
				.getDossierFolder(serviceContext
					.getScopeGroupId(), dossier
						.getUserId(),
					dossier
						.getCounter(),
					serviceContext);

			DossierFileLocalServiceUtil
				.addDossierFile(serviceContext
					.getUserId(), dossierId, dossierPartId, dossierPart
						.getTemplateFileNo(),
					StringPool.BLANK, fileGroupId, 0, accountBean
						.getOwnerUserId(),
					accountBean
						.getOwnerOrganizationId(),
					displayName, StringPool.BLANK,
					dossierFile != null ? dossierFile
						.getFileEntryId() : 0,
					PortletConstants.DOSSIER_FILE_MARK_UNKNOW, dossierFileType,
					dossierFileNo, fileDate, dossierFileOriginal,
					PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					dossierFolder
						.getFolderId(),
					sourceFileName, contentType, displayName, StringPool.BLANK,
					StringPool.BLANK, inputStream, size, serviceContext);

			updated = true;

		}
		catch (Exception e) {
			updated = false;
			if (e instanceof DuplicateFileException) {
				SessionErrors
					.add(actionRequest, DuplicateFileException.class);
			}
			else if (e instanceof NoSuchDossierException) {
				SessionErrors
					.add(actionRequest, NoSuchDossierException.class);
			}
			else if (e instanceof NoSuchDossierPartException) {
				SessionErrors
					.add(actionRequest, NoSuchDossierPartException.class);
			}
			else if (e instanceof NoSuchAccountException) {
				SessionErrors
					.add(actionRequest, NoSuchAccountException.class);
			}
			else if (e instanceof NoSuchAccountTypeException) {
				SessionErrors
					.add(actionRequest, NoSuchAccountTypeException.class);
			}
			else if (e instanceof NoSuchAccountFolderException) {
				SessionErrors
					.add(actionRequest, NoSuchAccountFolderException.class);
			}
			else if (e instanceof NoSuchAccountOwnUserIdException) {
				SessionErrors
					.add(actionRequest, NoSuchAccountOwnUserIdException.class);
			}
			else if (e instanceof NoSuchAccountOwnOrgIdException) {
				SessionErrors
					.add(actionRequest, NoSuchAccountOwnOrgIdException.class);
			}
			else if (e instanceof PermissionDossierException) {
				SessionErrors
					.add(actionRequest, PermissionDossierException.class);
			}
			else if (e instanceof FileSizeException) {
				SessionErrors
					.add(actionRequest, FileSizeException.class);
			}
			else {
				SessionErrors
					.add(actionRequest, "upload-error");

			}
			_log
				.error(e);
		}
		finally {
			if (updated) {
				if (Validator
					.isNotNull(redirectURL)) {
					actionResponse
						.sendRedirect(redirectURL);
				}
			}
			else {
				actionResponse
					.setRenderParameter("redirectURL", redirectURL);
				actionResponse
					.setRenderParameter("content", "upload-file");
				actionResponse
					.setRenderParameter("jspPage",
						"/html/portlets/processmgt/processorder/modal_dialog.jsp");

			}

		}
	}

	public void deleteAttachmentFile(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		long dossierFileId = ParamUtil
			.getLong(actionRequest, DossierFileDisplayTerms.DOSSIER_FILE_ID);
		DossierFile dossierFile = null;

		JSONObject jsonObject = null;

		try {
			if (dossierFileId > 0) {
				jsonObject = JSONFactoryUtil
					.createJSONObject();
				dossierFile = DossierFileLocalServiceUtil
					.getDossierFile(dossierFileId);
				long fileEntryId = dossierFile
					.getFileEntryId();
				DossierFileLocalServiceUtil
					.deleteDossierFile(dossierFileId, fileEntryId);
				jsonObject
					.put("deleted", Boolean.TRUE);
			}

		}
		catch (Exception e) {
			jsonObject
				.put("deleted", Boolean.FALSE);
			_log
				.error(e);
		}
		finally {
			PortletUtil
				.writeJSON(actionRequest, actionResponse, jsonObject);
		}

	}

	@Override
	public void render(
		RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException, IOException {

		long processOrderId = ParamUtil
			.getLong(renderRequest, ProcessOrderDisplayTerms.PROCESS_ORDER_ID);

		long dossierFileId = ParamUtil
			.getLong(renderRequest, DossierFileDisplayTerms.DOSSIER_FILE_ID);

		long dossierPartId = ParamUtil
			.getLong(renderRequest, DossierFileDisplayTerms.DOSSIER_PART_ID);

		if (processOrderId > 0) {
			try {
				ProcessOrder processOrder = ProcessOrderLocalServiceUtil
					.getProcessOrder(processOrderId);
				ProcessStep processStep = ProcessStepLocalServiceUtil
					.getProcessStep(processOrder
						.getProcessStepId());
				Dossier dossier = DossierLocalServiceUtil
					.getDossier(processOrder
						.getDossierId());
				ServiceProcess serviceProcess = ServiceProcessLocalServiceUtil
					.getServiceProcess(processOrder
						.getServiceProcessId());
				ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil
					.getServiceInfo(processOrder
						.getServiceInfoId());
				ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil
					.getServiceConfig(dossier
						.getServiceConfigId());

				DossierTemplate dossierTemplate =
					DossierTemplateLocalServiceUtil
						.getDossierTemplate(dossier
							.getDossierTemplateId());

				ProcessWorkflow processWorkflow =
					ProcessWorkflowLocalServiceUtil
						.getProcessWorkflow(processOrder
							.getProcessWorkflowId());

				renderRequest
					.setAttribute(WebKeys.PROCESS_ORDER_ENTRY, processOrder);
				renderRequest
					.setAttribute(WebKeys.PROCESS_STEP_ENTRY, processStep);
				renderRequest
					.setAttribute(WebKeys.DOSSIER_ENTRY, dossier);
				renderRequest
					.setAttribute(WebKeys.SERVICE_PROCESS_ENTRY,
						serviceProcess);
				renderRequest
					.setAttribute(WebKeys.SERVICE_INFO_ENTRY, serviceInfo);
				renderRequest
					.setAttribute(WebKeys.SERVICE_CONFIG_ENTRY, serviceConfig);

				renderRequest
					.setAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY,
						dossierTemplate);

				renderRequest
					.setAttribute(WebKeys.PROCESS_WORKFLOW_ENTRY,
						processWorkflow);

				ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest
					.getAttribute(WebKeys.THEME_DISPLAY);

				String accountType = StringPool.BLANK;

				if (themeDisplay
					.isSignedIn()) {

					List<UserGroup> userGroups = new ArrayList<UserGroup>();

					User user = themeDisplay
						.getUser();
					userGroups = user
						.getUserGroups();

					if (!userGroups
						.isEmpty()) {
						for (UserGroup userGroup : userGroups) {
							if (userGroup
								.getName().equals(
									PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN) ||
								userGroup
									.getName().equals(
										PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {
								accountType = userGroup
									.getName();
								break;
							}

						}
					}

					renderRequest
						.setAttribute(WebKeys.ACCOUNT_TYPE, accountType);

					if (accountType
						.equals(
							PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
						Citizen citizen = CitizenLocalServiceUtil
							.getCitizen(user
								.getUserId());
						renderRequest
							.setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
					}
					else if (accountType
						.equals(
							PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {

						Business business = BusinessLocalServiceUtil
							.getBusiness(user
								.getUserId());
						renderRequest
							.setAttribute(WebKeys.BUSINESS_ENTRY, business);
					}

				}

				if (dossierFileId > 0) {
					DossierFile dossierFile = DossierFileLocalServiceUtil
						.getDossierFile(dossierFileId);

					renderRequest
						.setAttribute(WebKeys.DOSSIER_FILE_ENTRY, dossierFile);
				}

				if (dossierPartId > 0) {
					DossierPart dossierPart = DossierPartLocalServiceUtil
						.getDossierPart(dossierPartId);
					renderRequest
						.setAttribute(WebKeys.DOSSIER_PART_ENTRY, dossierPart);
				}

			}

			catch (Exception e) {
				_log
					.error(e
						.getCause());
			}

		}
		super.render(renderRequest, renderResponse);
	}

	public void assignToUser(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		long assignToUserId = ParamUtil
			.getLong(actionRequest, ProcessOrderDisplayTerms.ASSIGN_TO_USER_ID);

		String paymentValue = ParamUtil
			.getString(actionRequest, ProcessOrderDisplayTerms.PAYMENTVALUE);

		String estimateDatetime = ParamUtil
			.getString(actionRequest,
				ProcessOrderDisplayTerms.ESTIMATE_DATETIME);

		/*
		 * String redirectURL = ParamUtil .getString(actionRequest,
		 * "redirectURL");
		 */

		long dossierId = ParamUtil
			.getLong(actionRequest, ProcessOrderDisplayTerms.DOSSIER_ID);

		long groupId = ParamUtil
			.getLong(actionRequest, ProcessOrderDisplayTerms.GROUP_ID);

		long companyId = ParamUtil
			.getLong(actionRequest, ProcessOrderDisplayTerms.COMPANY_ID);

		long fileGroupId = ParamUtil
			.getLong(actionRequest, ProcessOrderDisplayTerms.FILE_GROUP_ID);
		long processOrderId = ParamUtil
			.getLong(actionRequest, ProcessOrderDisplayTerms.PROCESS_ORDER_ID);
		long actionUserId = ParamUtil
			.getLong(actionRequest, ProcessOrderDisplayTerms.ACTION_USER_ID);
		long processWorkflowId = ParamUtil
			.getLong(actionRequest,
				ProcessOrderDisplayTerms.PROCESS_WORKFLOW_ID);
		long serviceProcessId = ParamUtil
			.getLong(actionRequest,
				ProcessOrderDisplayTerms.SERVICE_PROCESS_ID);
		long processStepId = ParamUtil
			.getLong(actionRequest, ProcessOrderDisplayTerms.PROCESS_STEP_ID);

		String actionNote = ParamUtil
			.getString(actionRequest, ProcessOrderDisplayTerms.ACTION_NOTE);
		String event = ParamUtil
			.getString(actionRequest, ProcessOrderDisplayTerms.EVENT);

		boolean signature = ParamUtil
			.getBoolean(actionRequest, ProcessOrderDisplayTerms.SIGNATURE);

		Date deadline = null;
		if (Validator
			.isNotNull(estimateDatetime)) {
			deadline = DateTimeUtil
				.convertStringToDate(estimateDatetime);
		}

		Dossier dossier = null;

		try {
			dossier = DossierLocalServiceUtil
				.getDossier(dossierId);
		}
		catch (Exception e) {
			_log
				.error(e);
		}

		Message message = new Message();
		message
			.put(ProcessOrderDisplayTerms.EVENT, event);
		message
			.put(ProcessOrderDisplayTerms.ACTION_NOTE, actionNote);
		message
			.put(ProcessOrderDisplayTerms.PROCESS_STEP_ID, processStepId);
		message
			.put(ProcessOrderDisplayTerms.ASSIGN_TO_USER_ID, assignToUserId);
		message
			.put(ProcessOrderDisplayTerms.SERVICE_PROCESS_ID, serviceProcessId);
		message
			.put(ProcessOrderDisplayTerms.PAYMENTVALUE, paymentValue);

		message
			.put(ProcessOrderDisplayTerms.PROCESS_WORKFLOW_ID,
				processWorkflowId);

		message
			.put(ProcessOrderDisplayTerms.ACTION_USER_ID, actionUserId);

		message
			.put(ProcessOrderDisplayTerms.PROCESS_ORDER_ID, processOrderId);
		message
			.put(ProcessOrderDisplayTerms.FILE_GROUP_ID, fileGroupId);
		message
			.put(ProcessOrderDisplayTerms.DOSSIER_ID, dossierId);
		message
			.put(ProcessOrderDisplayTerms.ESTIMATE_DATETIME, deadline);

		message
			.put(ProcessOrderDisplayTerms.SIGNATURE, signature);

		message
			.put(ProcessOrderDisplayTerms.GROUP_ID, groupId);

		message
			.put(ProcessOrderDisplayTerms.COMPANY_ID, companyId);

		SendToEngineMsg sendToEngineMsg = new SendToEngineMsg();

		// sendToEngineMsg.setAction(WebKeys.ACTION);
		sendToEngineMsg
			.setActionNote(actionNote);
		sendToEngineMsg
			.setActionUserId(actionUserId);
		sendToEngineMsg
			.setDossierId(dossierId);
		sendToEngineMsg
			.setEstimateDatetime(deadline);
		sendToEngineMsg
			.setFileGroupId(fileGroupId);
		sendToEngineMsg
			.setPaymentValue(GetterUtil
				.getDouble(paymentValue));
		sendToEngineMsg
			.setProcessOrderId(processOrderId);
		sendToEngineMsg
			.setProcessWorkflowId(processWorkflowId);
		sendToEngineMsg
			.setReceptionNo(Validator
				.isNotNull(dossier
					.getReceptionNo()) ? dossier
						.getReceptionNo() : StringPool.BLANK);
		sendToEngineMsg
			.setSignature(signature ? 1 : 0);
		message
			.put("msgToEngine", sendToEngineMsg);
		MessageBusUtil
			.sendMessage("opencps/backoffice/engine/destination", message);

		actionResponse
			.setRenderParameter("jspPage",
				"/html/portlets/processmgt/processorder/assign_to_user.jsp");
	}

	/**
	 * @param accountBean
	 * @throws NoSuchAccountTypeException
	 * @throws NoSuchAccountException
	 * @throws NoSuchAccountFolderException
	 * @throws NoSuchAccountOwnUserIdException
	 * @throws NoSuchAccountOwnOrgIdException
	 */
	private void validateAccount(AccountBean accountBean)
		throws NoSuchAccountTypeException, NoSuchAccountException,
		NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
		NoSuchAccountOwnOrgIdException {

		if (accountBean == null) {
			throw new NoSuchAccountException();
		}
		else if (Validator
			.isNull(accountBean
				.getAccountType())) {
			throw new NoSuchAccountTypeException();
		}
		else if (accountBean
			.getAccountFolder() == null) {
			throw new NoSuchAccountFolderException();
		}

		else if (accountBean
			.isCitizen() && accountBean
				.getOwnerUserId() == 0) {
			throw new NoSuchAccountOwnUserIdException();
		}

		else if (accountBean
			.isBusiness() && accountBean
				.getOwnerOrganizationId() == 0) {
			throw new NoSuchAccountOwnOrgIdException();
		}
	}

	/**
	 * @param dossierId
	 * @param dossierPartId
	 * @param accountBean
	 * @throws NoSuchAccountTypeException
	 * @throws NoSuchAccountException
	 * @throws NoSuchAccountFolderException
	 * @throws NoSuchAccountOwnUserIdException
	 * @throws NoSuchAccountOwnOrgIdException
	 * @throws NoSuchDossierException
	 * @throws NoSuchDossierPartException
	 * @throws PermissionDossierException
	 */
	private void validateDynamicFormData(
		long dossierId, long dossierPartId, AccountBean accountBean)
		throws NoSuchAccountTypeException, NoSuchAccountException,
		NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
		NoSuchAccountOwnOrgIdException, NoSuchDossierException,
		NoSuchDossierPartException, PermissionDossierException {

		validateAccount(accountBean);
		if (dossierId <= 0) {
			throw new NoSuchDossierException();
		}

		if (dossierPartId < 0) {
			throw new NoSuchDossierPartException();
		}

		Dossier dossier = null;

		try {
			dossier = DossierLocalServiceUtil
				.getDossier(dossierId);
		}
		catch (Exception e) {
			throw new NoSuchDossierPartException();
		}

		if (dossier
			.getUserId() != accountBean
				.getOwnerUserId()) {
			throw new PermissionDossierException();
		}

		try {
			DossierPartLocalServiceUtil
				.getDossierPart(dossierPartId);
		}
		catch (Exception e) {
			throw new NoSuchDossierPartException();
		}
	}

	/**
	 * @param dossierFileId
	 * @param accountBean
	 * @throws NoSuchAccountTypeException
	 * @throws NoSuchAccountException
	 * @throws NoSuchAccountFolderException
	 * @throws NoSuchAccountOwnUserIdException
	 * @throws NoSuchAccountOwnOrgIdException
	 * @throws NoSuchDossierFileException
	 */
	private void validateCreateDynamicForm(
		long dossierFileId, AccountBean accountBean)
		throws NoSuchAccountTypeException, NoSuchAccountException,
		NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
		NoSuchAccountOwnOrgIdException, NoSuchDossierFileException {

		validateAccount(accountBean);

		if (dossierFileId < 0) {
			throw new NoSuchDossierFileException();
		}

		DossierFile dossierFile = null;

		try {
			dossierFile = DossierFileLocalServiceUtil
				.getDossierFile(dossierFileId);
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		if (dossierFile == null) {
			throw new NoSuchDossierFileException();
		}
	}

	/**
	 * @param dossierId
	 * @param partName
	 * @throws NoSuchDossierException
	 * @throws EmptyFileGroupException
	 * @throws DuplicateFileGroupException
	 */
	private void valiadateFileGroup(long dossierId, String partName)
		throws NoSuchDossierException, EmptyFileGroupException,
		DuplicateFileGroupException {

		if (dossierId <= 0) {
			throw new NoSuchDossierException();
		}
		else if (Validator
			.isNull(partName
				.trim())) {
			throw new EmptyFileGroupException();
		}

		int count = 0;

		try {
			count = FileGroupLocalServiceUtil
				.countByD_DN(dossierId, partName
					.trim());
		}
		catch (Exception e) {
		}

		if (count > 0) {
			throw new DuplicateFileGroupException();
		}
	}

	/**
	 * @param dossierId
	 * @param dossierPartId
	 * @param dossierFileId
	 * @param displayName
	 * @param size
	 * @param sourceFileName
	 * @param inputStream
	 * @param accountBean
	 * @throws NoSuchDossierException
	 * @throws NoSuchDossierPartException
	 * @throws NoSuchAccountException
	 * @throws NoSuchAccountTypeException
	 * @throws NoSuchAccountFolderException
	 * @throws NoSuchAccountOwnUserIdException
	 * @throws NoSuchAccountOwnOrgIdException
	 * @throws PermissionDossierException
	 * @throws FileSizeException
	 */
	private void validateAddAttachDossierFile(
		long dossierId, long dossierPartId, long dossierFileId,
		String displayName, long size, String sourceFileName,
		InputStream inputStream, AccountBean accountBean)
		throws NoSuchDossierException, NoSuchDossierPartException,
		NoSuchAccountException, NoSuchAccountTypeException,
		NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
		NoSuchAccountOwnOrgIdException, PermissionDossierException,
		FileSizeException {

		validateAccount(accountBean);

		if (dossierId <= 0) {
			throw new NoSuchDossierException();
		}

		if (dossierPartId < 0) {
			throw new NoSuchDossierPartException();
		}

		Dossier dossier = null;

		try {
			dossier = DossierLocalServiceUtil
				.getDossier(dossierId);
		}
		catch (Exception e) {
			throw new NoSuchDossierPartException();
		}

		if (accountBean
			.isBusiness()) {
			if (dossier
				.getOwnerOrganizationId() != accountBean
					.getOwnerOrganizationId()) {
				throw new PermissionDossierException();
			}

		}
		else if (accountBean
			.isCitizen()) {
			if (dossier
				.getUserId() != accountBean
					.getOwnerUserId()) {
				throw new PermissionDossierException();
			}

		}

		try {
			DossierPartLocalServiceUtil
				.getDossierPart(dossierPartId);
		}
		catch (Exception e) {
			throw new NoSuchDossierPartException();
		}

		if (size == 0) {
			throw new FileSizeException();
		}
		else if (size > 300000000) {
			throw new FileSizeException();
		}
	}

	private Log _log = LogFactoryUtil
		.getLog(ProcessOrderPortlet.class
			.getName());
}

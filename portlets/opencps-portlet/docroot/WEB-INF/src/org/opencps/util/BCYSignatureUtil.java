package org.opencps.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.cert.Certificate;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.opencps.processmgt.util.ReportUtils;

import vgca.svrsigner.ServerSigner;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.util.portlet.PortletProps;

public class BCYSignatureUtil extends SignatureUtil {

	public static Log _log = LogFactoryUtil.getLog(BCYSignatureUtil.class);

	/**
	 * @param resourceRequest
	 * @param resourceResponse
	 * @param fileEntry
	 * @param symbolType
	 * @param dossierFileId
	 * @param dossierPartId
	 * @param index
	 * @param indexSize
	 * @throws IOException
	 */
	public static void getComputerHash(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse, FileEntry fileEntry,
			int symbolType, long dossierFileId, long dossierPartId, int index,
			long indexSize) throws IOException {

		// String result = StringPool.BLANK;

		long userId = PortalUtil.getUserId(resourceRequest);

		byte[] inHash = null;

		String userName = StringPool.BLANK;
		String fieldName = StringPool.BLANK;

		JSONObject jsonFeed = JSONFactoryUtil.createJSONObject();

		JSONArray hashComputers = JSONFactoryUtil.getJSONFactory()
				.createJSONArray();
		JSONArray signFieldNames = JSONFactoryUtil.getJSONFactory()
				.createJSONArray();
		JSONArray filePaths = JSONFactoryUtil.getJSONFactory()
				.createJSONArray();
		JSONArray messages = JSONFactoryUtil.getJSONFactory().createJSONArray();
		JSONArray fileNames = JSONFactoryUtil.getJSONFactory()
				.createJSONArray();
		JSONArray dossierFileIds = JSONFactoryUtil.getJSONFactory()
				.createJSONArray();
		JSONArray dossierPartIds = JSONFactoryUtil.getJSONFactory()
				.createJSONArray();
		JSONArray indexs = JSONFactoryUtil.getJSONFactory().createJSONArray();
		JSONArray indexSizes = JSONFactoryUtil.getJSONFactory()
				.createJSONArray();
		String filePath = StringPool.BLANK;

		File file = null;
		try {
			// String realExportPathTmp = request.getContextPath();
			User user = UserLocalServiceUtil.fetchUser(userId);
			if (user != null) {
				userName = user.getScreenName();
			}

			String realPath = ReportUtils
					.getTemplateReportFilePath(resourceRequest);
			String realExportPath = realPath + "resources/";

			String imageBase64 = StringPool.BLANK;
			String cer = realExportPath;
			String cerPath = cer + userName + ".cer";
			String signImagePath = StringPool.BLANK;
			String imgsrc = realExportPath;
			if (symbolType == 1) {
				signImagePath = imgsrc + userName + ".png";
			} else {
				signImagePath = imgsrc + userName + "_condau.png";
			}

			imageBase64 = ImageUtil
					.getSignatureImageBase64ByPath(signImagePath);

			BufferedImage bufferedImage = ImageUtil
					.getImageByPath(signImagePath);

			// tinh toa do chu ky
			String realExportDir = PortletProps
					.get("opencps.file.system.temp.dir");
			filePath = PDFUtil.saveAsPdf(realExportDir,
					fileEntry.getFileEntryId());
			file = new File(filePath);

			ExtractTextLocations textLocation = new ExtractTextLocations(
					filePath);

			_log.info("*********************************"
					+ textLocation.getAnchorX() + "-"
					+ textLocation.getAnchorY()
					+ "********************************");

			_log.info("*********************************"
					+ textLocation.getPageLLX() + "-"
					+ textLocation.getPageURX() + "-"
					+ textLocation.getPageLLY() + "-"
					+ textLocation.getPageURY()
					+ "*******************************");

			// doc file cer tren server
			Certificate cert = CertUtil.getCertificateByPath(cerPath);
			
			ServerSigner signer = BCYSignatureUtil.getServerSigner(filePath,
					cert, imageBase64);

			// tinh kich thuoc cua anh

			int signatureImageWidth = (bufferedImage != null && bufferedImage
					.getWidth() > 0) ? bufferedImage.getWidth() : 80;

			int signatureImageHeight = (bufferedImage != null && bufferedImage
					.getHeight() > 0) ? bufferedImage.getHeight() : 80;
			float llx = textLocation.getAnchorX();

			float urx = llx + signatureImageWidth / 3;

			float lly = textLocation.getPageURY() - textLocation.getAnchorY()
					- signatureImageHeight / 3;

			float ury = lly + signatureImageHeight / 3;

			// inHash = signer.computeHash(new Rectangle(llx + 65, lly - 55, urx
			// + 114, ury-20), 1);

			// signer.setSignatureAppearance(PdfSignatureAppearance.RenderingMode.GRAPHIC);
			inHash = signer.computeHash(new Rectangle(llx, lly, urx, ury), 1);

			fieldName = signer.getSignatureName();
			
			signFieldNames.put(fieldName);
			
			hashComputers.put(Base64.encode(inHash));
			
			filePaths.put(filePath);
			
			fileNames.put(fileEntry.getTitle());
			
			dossierFileIds.put(dossierFileId);
			
			dossierPartIds.put(dossierPartId);
			
			indexs.put(index);
			
			indexSizes.put(indexSize);
			
			_log.info("**************inHash: " + inHash
					+ "-----------fieldName: " + fieldName
					+ "----------filePath: " + filePath);
			messages.put("success");
		} catch (Exception e) {
			messages.put("Error signature filePath: " + filePath);
			hashComputers.put(StringPool.BLANK);
			signFieldNames.put(StringPool.BLANK);
			filePaths.put(filePath);
			_log.error(e);
		} finally {
			if (Validator.isNotNull(file) && file.exists()) {
				//file.delete();
			}
		}

		jsonFeed.put("hashComputers", hashComputers);
		jsonFeed.put("signFieldNames", signFieldNames);
		jsonFeed.put("filePaths", filePaths);
		jsonFeed.put("msg", messages);
		jsonFeed.put("fileNames", fileNames);
		jsonFeed.put("dossierFileIds", dossierFileIds);
		jsonFeed.put("dossierPartIds", dossierPartIds);
		jsonFeed.put("indexs", indexs);
		jsonFeed.put("indexSizes", indexSizes);
		PrintWriter out = resourceResponse.getWriter();
		out.print(jsonFeed.toString());

	}

	/**
	 * @param fullPath
	 * @param cert
	 * @param imageBase64
	 * @return
	 */
	public static ServerSigner getServerSigner(String fullPath,
			Certificate cert, String imageBase64) {
		ServerSigner signer = new ServerSigner(fullPath, cert);
		signer.setSignatureGraphic(imageBase64);
		signer.setSignatureAppearance(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION);
		return signer;
	}

	/**
	 * @param fileEntry
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static String getDLFileAbsPath(FileEntry fileEntry)
			throws PortalException, SystemException {
		return PortletProps.get("opencps.file.system.dir")
				+ "/document_library/" + fileEntry.getCompanyId() + "/"
				+ fileEntry.getFolderId() + "/"
				+ ((DLFileEntry) fileEntry.getModel()).getName() + "/"
				+ fileEntry.getVersion();
	}

}

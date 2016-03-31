/**
 * OpenCPS is the open source Core Public Services software Copyright (C)
 * 2016-present OpenCPS community This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have received a
 * copy of the GNU Affero General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.util;

import java.io.InputStream;

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;

/**
 * @author trungnt
 */
public class DLFileEntryUtil {

	public static FileEntry addFileEntry(
	    long repositoryId, long folderId, String sourceFileName,
	    String contentType, String title, String description, String changeLog,
	    InputStream inputStream, long size, ServiceContext serviceContext) {

		FileEntry fileEntry = null;

		try {
			fileEntry = DLAppServiceUtil
			    .addFileEntry(
			        repositoryId, folderId, sourceFileName, contentType, title,
			        description, changeLog, inputStream, size, serviceContext);
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		return fileEntry;
	}
}

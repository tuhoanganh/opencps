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

package org.opencps.datamgt.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.model.DictVersion;
import org.opencps.datamgt.service.DictCollectionLocalServiceUtil;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.datamgt.service.DictVersionLocalServiceUtil;
import org.opencps.datamgt.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 *
 */
public class DataMamagementPortlet extends MVCPortlet {
	@Override
	public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {

		long dictCollectionId = ParamUtil.getLong(request, "dictCollectionId");
		long dictVersionId = ParamUtil.getLong(request, "dictVersionId");
		long dictItemId = ParamUtil.getLong(request, "dictItemId");

		try {
			if (dictCollectionId > 0) {
				DictCollection dictCollection = DictCollectionLocalServiceUtil.getDictCollection(dictCollectionId);
				request.setAttribute(WebKeys.DICT_COLLECTION_ENTRY, dictCollection);
			}

			if (dictVersionId > 0) {
				DictVersion dictVersion = DictVersionLocalServiceUtil.getDictVersion(dictVersionId);
				request.setAttribute(WebKeys.DICT_VERSION_ENTRY, dictVersion);
			}

			if (dictItemId > 0) {
				DictItem dictItem = DictItemLocalServiceUtil.getDictItem(dictItemId);
				request.setAttribute(WebKeys.DICT_ITEM_ENTRY, dictItem);
			}
		} catch (Exception e) {
			_log.error(e);
		}

		super.render(request, response);
	}

	private Log _log = LogFactoryUtil.getLog(DataMamagementPortlet.class.getName());
}

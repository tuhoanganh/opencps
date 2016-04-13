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

package org.opencps.dossiermgt.model.impl;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import org.opencps.dossiermgt.model.DossierTemplate;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The cache model class for representing DossierTemplate in entity cache.
 *
 * @author trungnt
 * @see DossierTemplate
 * @generated
 */
public class DossierTemplateCacheModel implements CacheModel<DossierTemplate>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(9);

		sb.append("{dossierTemplateId=");
		sb.append(dossierTemplateId);
		sb.append(", templateName=");
		sb.append(templateName);
		sb.append(", description=");
		sb.append(description);
		sb.append(", templateNo=");
		sb.append(templateNo);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public DossierTemplate toEntityModel() {
		DossierTemplateImpl dossierTemplateImpl = new DossierTemplateImpl();

		dossierTemplateImpl.setDossierTemplateId(dossierTemplateId);

		if (templateName == null) {
			dossierTemplateImpl.setTemplateName(StringPool.BLANK);
		}
		else {
			dossierTemplateImpl.setTemplateName(templateName);
		}

		if (description == null) {
			dossierTemplateImpl.setDescription(StringPool.BLANK);
		}
		else {
			dossierTemplateImpl.setDescription(description);
		}

		if (templateNo == null) {
			dossierTemplateImpl.setTemplateNo(StringPool.BLANK);
		}
		else {
			dossierTemplateImpl.setTemplateNo(templateNo);
		}

		dossierTemplateImpl.resetOriginalValues();

		return dossierTemplateImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		dossierTemplateId = objectInput.readLong();
		templateName = objectInput.readUTF();
		description = objectInput.readUTF();
		templateNo = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(dossierTemplateId);

		if (templateName == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(templateName);
		}

		if (description == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(description);
		}

		if (templateNo == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(templateNo);
		}
	}

	public long dossierTemplateId;
	public String templateName;
	public String description;
	public String templateNo;
}
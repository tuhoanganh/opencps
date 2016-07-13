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

package org.opencps.jms.message;

import java.io.InputStream;
import java.io.Serializable;

import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.FileGroup;

import com.liferay.portal.kernel.repository.model.FileEntry;

/**
 * @author trungnt
 */
public class DossierFileMsgBody implements Serializable {

	public DossierFile getDossierFile() {

		return _dossierFile;
	}

	public void setDossierFile(DossierFile dossierFile) {

		this._dossierFile = dossierFile;
	}

	public FileGroup getFileGroup() {

		return _fileGroup;
	}

	public void setFileGroup(FileGroup fileGroup) {

		this._fileGroup = fileGroup;
	}

	public DossierPart getDossierPart() {

		return _dossierPart;
	}

	public void setDossierPart(DossierPart dossierPart) {

		this._dossierPart = dossierPart;
	}

	public FileEntry getFileEntry() {

		return _fileEntry;
	}

	public void setFileEntry(FileEntry fileEntry) {

		this._fileEntry = fileEntry;
	}
	

	public byte[] getBytes() {
	
		return _bytes;
	}

	public void setBytes(byte[] bytes) {
	
		this._bytes = bytes;
	}



	private DossierFile _dossierFile;
	
	private FileGroup _fileGroup;
	
	private DossierPart _dossierPart;
	
	private FileEntry _fileEntry;
	
	private byte[] _bytes;
}

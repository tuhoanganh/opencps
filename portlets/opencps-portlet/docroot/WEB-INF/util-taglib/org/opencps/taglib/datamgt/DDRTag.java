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

package org.opencps.taglib.datamgt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.taglib.util.IncludeTag;

/**
 * @author trungnt
 */
public class DDRTag extends IncludeTag {

	@Override
	protected void cleanUp() {
		_dictCollectionCode = null;
		_initDictItemId = 0;
		_depthLevel = 0;
		_cssClass = null;
		_labelItems = null;
		_selectedItems = null;
		_renderMode = null;
		_name = null;
		_cssClass = null;
	}

	@Override
	protected String getPage() {
		System.out.println(pageContext.getPage());

		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {

		request.setAttribute("opencps-datamgt:ddr:dictCollectionCode", _dictCollectionCode);
		request.setAttribute("opencps-datamgt:ddr:initDictItemId", _initDictItemId);
		request.setAttribute("opencps-datamgt:ddr:depthLevel", _depthLevel);
		request.setAttribute("opencps-datamgt:ddr:labelItems", _labelItems);
		request.setAttribute("opencps-datamgt:ddr:selectedItems", _selectedItems);
		request.setAttribute("opencps-datamgt:ddr:renderMode", _renderMode);
		request.setAttribute("opencps-datamgt:ddr:name", _name);
		request.setAttribute("opencps-datamgt:ddr:cssClass", _cssClass);
	}

	public String getDictCollectionCode() {
		return _dictCollectionCode;
	}

	public void setDictCollectionCode(String _dictCollectionCode) {
		this._dictCollectionCode = _dictCollectionCode;
	}

	public long getInitDictItemId() {
		return _initDictItemId;
	}

	public void setInitDictItemId(long _initDictItemId) {
		this._initDictItemId = _initDictItemId;
	}

	public int getDepthLevel() {
		return _depthLevel;
	}

	public void setDepthLevel(int _depthLevel) {
		this._depthLevel = _depthLevel;
	}

	public List<String> getLabelItems() {
		return _labelItems;
	}

	public void setLabelItems(List<String> _labelItems) {
		this._labelItems = _labelItems;
	}

	public List<Long> getSelectedItems() {
		return _selectedItems;
	}

	public void setSelectedItems(List<Long> _selectedItems) {
		this._selectedItems = _selectedItems;
	}

	public String get_renderMode() {
		return _renderMode;
	}

	public void setRenderMode(String _renderMode) {
		this._renderMode = _renderMode;
	}

	public String getName() {
		return _name;
	}

	public void setName(String _name) {
		this._name = _name;
	}

	public String getCssClass() {
		return _cssClass;
	}

	public void setCssClass(String _cssClass) {
		this._cssClass = _cssClass;
	}

	private String _dictCollectionCode;
	private long _initDictItemId;
	private int _depthLevel;
	private List<String> _labelItems;
	private List<Long> _selectedItems;
	private String _renderMode;
	private String _name;
	private String _cssClass;

	private static final String _PAGE = "/html/taglib/datamgt/ddr/page.jsp";
}
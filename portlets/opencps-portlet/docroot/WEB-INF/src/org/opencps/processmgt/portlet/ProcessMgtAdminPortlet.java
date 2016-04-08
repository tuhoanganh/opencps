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

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.util.bridges.mvc.MVCPortlet;


/**
 * @author khoavd
 *
 */
public class ProcessMgtAdminPortlet extends MVCPortlet{
	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#render(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	public void render(RenderRequest arg0, RenderResponse arg1)
	    throws PortletException, IOException {
	
	    // TODO Auto-generated method stub
	    super.render(arg0, arg1);
	}

}

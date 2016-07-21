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

import org.opencps.jms.SyncServiceContext;
import org.opencps.jms.context.JMSContext;
import org.opencps.jms.context.JMSLocalContext;
import org.opencps.jms.message.body.SyncFromBackOfficeMsgBody;

/**
 * @author trungnt
 */
public class SyncFromBackOfficeMessage {

	public SyncFromBackOfficeMessage(JMSContext context) {

		this.setContext(context);
	}

	public SyncFromBackOfficeMessage(JMSLocalContext context) {

		this.setLocalContext(context);
	}

	public JMSContext getContext() {

		return _context;
	}

	public void setContext(JMSContext context) {

		this._context = context;
	}

	public JMSLocalContext getLocalContext() {

		return _localContext;
	}

	public void setLocalContext(JMSLocalContext localContext) {

		this._localContext = localContext;
	}

	public SyncFromBackOfficeMsgBody getSyncFromBackOfficeMsgBody() {

		return _syncFromBackOfficeMsgBody;
	}

	public void setSyncFromBackOfficeMsgBody(
		SyncFromBackOfficeMsgBody syncFromBackOfficeMsgBody) {

		this._syncFromBackOfficeMsgBody = syncFromBackOfficeMsgBody;
	}

	public SyncServiceContext getServiceContext() {

		return _serviceContext;
	}

	public void setServiceContext(SyncServiceContext serviceContext) {

		this._serviceContext = serviceContext;
	}

	private JMSContext _context;

	private JMSLocalContext _localContext;

	private SyncFromBackOfficeMsgBody _syncFromBackOfficeMsgBody;

	private SyncServiceContext _serviceContext;
}

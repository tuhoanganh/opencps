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
package org.opencps.dossiermgt.util;


import org.opencps.dossiermgt.comparator.DossierTemplateNameComparator;
import org.opencps.dossiermgt.comparator.DossierTemplateNoComparator;
import org.opencps.dossiermgt.search.DossierTemplateDisplayTerms;

import com.liferay.portal.kernel.util.OrderByComparator;


public class DossierMgtUtil {
	
	public static final String[] _DOSSIER_CATEGORY_NAMES = {
		"dossier-info"
	};
	public static OrderByComparator getDossierTemplateOrderByComparator(
		String orderByCol, String orderByType) {
		
		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}
		
		OrderByComparator orderByComparator = null;
		
		if (orderByCol.equals(DossierTemplateDisplayTerms
			.DOSSIERTEMPLATE_TEMPLATENO)) {
			orderByComparator = new DossierTemplateNoComparator(orderByAsc);
		}
		else if (orderByCol.equals(DossierTemplateDisplayTerms
			.DOSSIERTEMPLATE_TEMPLATENAME)) {
			orderByComparator = new DossierTemplateNameComparator(orderByAsc);
		}
		
		return orderByComparator;
	}
}

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
package org.opencps.dossiermgt.search;

import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.BooleanQueryFactoryUtil;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.TermQueryFactoryUtil;

/**
 * @author trungdk
 */
public class DossierSearchUtil {

	public static BooleanQuery buildSearchQuery(String keywords, SearchContext searchContext) {
		BooleanQuery query = BooleanQueryFactoryUtil.create(searchContext);
		TermQuery termReceptionNo = TermQueryFactoryUtil.create(searchContext, DossierDisplayTerms.RECEPTION_NO, keywords);		
		TermQuery termCityName = TermQueryFactoryUtil.create(searchContext, DossierDisplayTerms.CITY_NAME, keywords);		
		TermQuery termExternalRefNo = TermQueryFactoryUtil.create(searchContext, DossierDisplayTerms.EXTERNALREF_NO, keywords);		
		TermQuery termAddress = TermQueryFactoryUtil.create(searchContext, DossierDisplayTerms.ADDRESS, keywords);		
		TermQuery termDossierId = TermQueryFactoryUtil.create(searchContext, DossierDisplayTerms.DOSSIER_ID, keywords);
		
		try {
			query.add(termReceptionNo, BooleanClauseOccur.SHOULD);
			query.add(termCityName, BooleanClauseOccur.SHOULD);
			query.add(termExternalRefNo, BooleanClauseOccur.SHOULD);	
			query.add(termAddress, BooleanClauseOccur.SHOULD);
			query.add(termDossierId, BooleanClauseOccur.SHOULD);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return query;
	}
}

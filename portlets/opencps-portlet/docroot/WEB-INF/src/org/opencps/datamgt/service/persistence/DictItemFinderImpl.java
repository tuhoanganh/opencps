package org.opencps.datamgt.service.persistence;

import java.util.List;

import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.model.impl.DictItemImpl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class DictItemFinderImpl extends BasePersistenceImpl<DictItem> implements
	DictItemFinder{
	
	public static final String GET_DICTITEMS_BY_COLLECTION_CODE = DictItemFinder.class
			.getName() + ".getDictItemsByCollectionCode";
	
	public List<DictItem> getDictItemsByCollectionCode(String dictCollectionCode, long groupId)
			throws SystemException{
		
		Session session = null;
		
		try{
			session = openSession();
			
			String sql = CustomSQLUtil
					.get(GET_DICTITEMS_BY_COLLECTION_CODE);
			
			System.out.println("============== sql: "+sql);
			
			SQLQuery queryObject = session.createSQLQuery(sql);
			queryObject.setCacheable(false);
			queryObject.addEntity("DictItem", DictItemImpl.class);
			QueryPos qPos = QueryPos.getInstance(queryObject);
			qPos.add(dictCollectionCode);
			qPos.add(groupId);
			
			return (List<DictItem>) queryObject.list();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeSession(session);
		}
			return null;
	}
			
}
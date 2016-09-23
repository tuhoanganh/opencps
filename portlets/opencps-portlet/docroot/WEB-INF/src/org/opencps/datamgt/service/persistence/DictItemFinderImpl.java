package org.opencps.datamgt.service.persistence;

import java.util.List;

import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.model.impl.DictItemImpl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class DictItemFinderImpl extends BasePersistenceImpl<DictItem> implements
	DictItemFinder{
	
	public static final String FIND_DICTITEMS_BY_G_DC_S = DictItemFinder.class
			.getName() + ".findDictItemsByG_DC_S";
	
	private Log _log = LogFactoryUtil.getLog(DictItemFinder.class.getName());
	
	public List<DictItem> findDictItemsByG_DC_S(long groupId, String dictCollectionCode , int issueStatus){
		
		return _findDictItemsByG_DC_S(groupId, dictCollectionCode, issueStatus);
	}
	
	private List<DictItem> _findDictItemsByG_DC_S(long groupId, String dictCollectionCode, int issueStatus){
		
		Session session = null;
		
		try{
			session = openSession();
			
			String sql = CustomSQLUtil
					.get(FIND_DICTITEMS_BY_G_DC_S);
						
			if(Validator.isNull(dictCollectionCode)){
				sql = StringUtil.replace(sql, "AND (opencps_dictcollection.collectionCode = ?)", StringPool.BLANK);
			}
			
			if(issueStatus < 0 && issueStatus > 2){
				sql = StringUtil.replace(sql, "AND (opencps_dictitem.issueStatus = ?)", StringPool.BLANK);
			}
			
			SQLQuery queryObject = session.createSQLQuery(sql);
			queryObject.setCacheable(false);
			queryObject.addEntity("DictItem", DictItemImpl.class);
			
			QueryPos qPos = QueryPos.getInstance(queryObject);
			qPos.add(groupId);
			
			if(Validator.isNotNull(dictCollectionCode)){
				qPos.add(dictCollectionCode);
			}
			
			if(!(issueStatus < 0 && issueStatus > 2)){
				qPos.add(issueStatus);
			}
			
			return (List<DictItem>) queryObject.list();
			
		}catch (Exception e) {
			_log.error(e);
			
		} finally {
			
			closeSession(session);
		}
		
		return null;
	}
			
}
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

package org.opencps.usermgt.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.opencps.usermgt.NoSuchJobPosException;
import org.opencps.usermgt.NoSuchWorkingUnitException;
import org.opencps.usermgt.model.JobPos;
import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.service.base.JobPosLocalServiceBaseImpl;
import org.opencps.util.PortletPropsValues;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.RoleServiceUtil;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the job pos local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.usermgt.service.JobPosLocalService} interface. <p> This is
 * a local service. Methods of this service will not have security checks based
 * on the propagated JAAS credentials because this service can only be accessed
 * from within the same VM. </p>
 *
 * @author khoavd
 * @see org.opencps.usermgt.service.base.JobPosLocalServiceBaseImpl
 * @see org.opencps.usermgt.service.JobPosLocalServiceUtil
 */
public class JobPosLocalServiceImpl extends JobPosLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.usermgt.service.JobPosLocalServiceUtil} to access the
	 * job pos local service.
	 */
	public JobPos addJobPos(
		long userId, ServiceContext serviceContext, String title,
		String description, long workingUnitId, int leader)
		throws SystemException, PortalException {

		long jobPosId = CounterLocalServiceUtil
			.increment(JobPos.class
				.getName());

		JobPos jobPos = jobPosPersistence
			.create(jobPosId);
		WorkingUnit workingUnit = workingUnitPersistence
						.findByPrimaryKey(workingUnitId);

		Date currentDate = new Date();
		String roleName = "";
		
		roleName = title + " " + workingUnit.getName();
 
		Map<Locale, String> titleMap = new HashMap<Locale, String>();
		titleMap
			.put(Locale.US, title);

		Map<Locale, String> descriptionMap = new HashMap<Locale, String>();
		descriptionMap
			.put(Locale.US, description);
		
		long directWorkingUnitId = getDirectWorkingUnitId(
			workingUnitId).getWorkingunitId();
		Role role = RoleLocalServiceUtil
			.addRole(userId, Role.class
				.getName(), serviceContext
					.getCompanyId(),
				roleName, titleMap, descriptionMap, RoleConstants.TYPE_REGULAR,
				null, serviceContext);

		jobPos
			.setUserId(userId);
		jobPos
			.setGroupId(serviceContext
				.getScopeGroupId());
		jobPos
			.setCompanyId(serviceContext
				.getCompanyId());
		jobPos
			.setCreateDate(currentDate);
		jobPos
			.setModifiedDate(currentDate);
		jobPos
			.setTitle(title);
		jobPos
			.setDescription(description);
		jobPos
			.setWorkingUnitId(workingUnitId);
		jobPos
			.setDirectWorkingUnitId(directWorkingUnitId);
		jobPos
			.setLeader(leader);
		jobPos
			.setMappingRoleId(role
				.getRoleId());

		return jobPosPersistence
			.update(jobPos);
	}

	public JobPos updateJobPos(
		long jobPosId, long userId, ServiceContext serviceContext, String title,
		String description, long workingUnitId, int leader)
		throws SystemException, PortalException {

		JobPos jobPos = jobPosPersistence
			.findByPrimaryKey(jobPosId);

		Role role = RoleServiceUtil
			.getRole(jobPos
				.getMappingRoleId());
		WorkingUnit workingUnit = workingUnitPersistence
						.findByPrimaryKey(workingUnitId);
		
		long directWorkingUnitId = getDirectWorkingUnitId(
			workingUnitId).getWorkingunitId();
		Date currentDate = new Date();
		String roleName = "";
		
		roleName = title + " " + workingUnit.getName();
		


		jobPos
			.setUserId(userId);
		jobPos
			.setGroupId(serviceContext
				.getUserId());
		jobPos
			.setCompanyId(serviceContext
				.getCompanyId());
		jobPos
			.setCreateDate(currentDate);
		jobPos
			.setModifiedDate(currentDate);
		jobPos
			.setTitle(title);
		jobPos
			.setDescription(description);
		jobPos
			.setWorkingUnitId(workingUnitId);
		jobPos
			.setDirectWorkingUnitId(directWorkingUnitId);
		jobPos
			.setLeader(leader);

		role
			.setName(roleName);
		RoleLocalServiceUtil
			.updateRole(role);

		return jobPosPersistence
			.update(jobPos);
	}

	public void deletejobPos(long jobPosId)
		throws SystemException, NoSuchJobPosException {
		JobPos jobPos = jobPosPersistence.findByPrimaryKey(jobPosId);
		
		try {
			RoleLocalServiceUtil.deleteRole(jobPos.getMappingRoleId());
			jobPosPersistence
			.remove(jobPosId);
		}
		catch (PortalException e) {
			e.printStackTrace();
		}
		
	}

	public int countAll()
		throws SystemException {

		return jobPosPersistence
			.countAll();
	}

	public List<JobPos> getJobPoss(int start, int end, OrderByComparator odc)
		throws SystemException {

		return jobPosPersistence
			.findAll(start, end, odc);
	}

	public List<JobPos> getJobPoss(long workingUnitId)
		throws SystemException {

		return jobPosPersistence
			.findByWorkingUnitId(workingUnitId);
	}
	
	public List<JobPos> getJobPoss(long workingUnitId, OrderByComparator odc)
					throws SystemException {

					return jobPosPersistence
						.findByWorkingUnitId(workingUnitId);
				}

	public List<JobPos> getJobPoss(long groupId, long workingUnitId)
		throws SystemException {

		return jobPosPersistence
			.findByG_W(groupId, workingUnitId);
	}

	public List<JobPos> getJobPoss(
		long groupId, long workingUnitId, long directWorkingUnitId)
		throws SystemException {

		return jobPosPersistence
			.findByG_W_D(groupId, workingUnitId, directWorkingUnitId);
	}

	public void mapMultipleWorkingUnitToOneJobPos(
		long jobPosId, long[] workingUnitId)
		throws SystemException {

		jobPosPersistence
			.addWorkingUnits(jobPosId, workingUnitId);
	}

	public void mapMultipleEmployeeToOneJobPos(long jobPosId, long[] employeeId)
		throws SystemException {

		jobPosPersistence
			.addEmployees(jobPosId, employeeId);
	}
	
	private WorkingUnit getDirectWorkingUnitId(long parentWorkingUnitId) 
					throws NoSuchWorkingUnitException, SystemException {
		WorkingUnit workingUnit = workingUnitPersistence
						.findByPrimaryKey(parentWorkingUnitId);
		if(workingUnit.getIsEmployer() == true) {
			return workingUnit;
		} else {
			return getDirectWorkingUnitId(workingUnit.getParentWorkingUnitId());
		}
	}
	
	private Log _log = LogFactoryUtil.getLog(JobPosLocalServiceImpl.class);
}

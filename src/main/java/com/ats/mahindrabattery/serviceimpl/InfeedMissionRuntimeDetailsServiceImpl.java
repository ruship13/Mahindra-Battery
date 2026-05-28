package com.ats.mahindrabattery.serviceimpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ats.mahindrabattery.entity.AuditTrailDetailsEntity;
import com.ats.mahindrabattery.entity.InfeedMissionRuntimeDetailsEntity;
import com.ats.mahindrabattery.entity.MasterPalletInformationEntity;
import com.ats.mahindrabattery.entity.MasterPositionDetailsEntity;
import com.ats.mahindrabattery.repository.AuditTrailDetailsRepository;
import com.ats.mahindrabattery.repository.InfeedMissionRuntimeDetailsRepository;
import com.ats.mahindrabattery.repository.MasterPalletInformationRepository;
import com.ats.mahindrabattery.repository.MasterPositionDetailsRepository;
import com.ats.mahindrabattery.service.InfeedMissionRuntimeDetailsService;

@Service
public class InfeedMissionRuntimeDetailsServiceImpl implements InfeedMissionRuntimeDetailsService {

	MasterPositionDetailsEntity masterPositionDetailsEntity = new MasterPositionDetailsEntity();

	MasterPalletInformationEntity masterPalletInformationEntity = new MasterPalletInformationEntity();

	@Autowired
	MasterPalletInformationRepository masterPalletInformationRepositoryInstance;

	@Autowired
	private MasterPositionDetailsRepository masterPositionDetailsRepositoryInstance;

	@Autowired
	private InfeedMissionRuntimeDetailsRepository infeedMissionRuntimeDetailsRepository;

	@Autowired
	private AuditTrailDetailsRepository auditTrailDetailsRepository;

	public List<InfeedMissionRuntimeDetailsEntity> getAllInfeedMissionRuntimeDetails() {
		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentDateTime = dateFormat.format(date);
//			System.out.println("currentDateTime::"+currentDateTime);
//			System.out.println("curretdate::"+infeedMissionRuntimeDetailsRepository.getAllInfeedMissionRuntimeDetailsBetweenDates(
//					currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59"));
			return infeedMissionRuntimeDetailsRepository.getAllInfeedMissionRuntimeDetailsBetweenDates(
					currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	public List<InfeedMissionRuntimeDetailsEntity> findByCreatedDateTime(String cDatetime) {
		try {
			List<InfeedMissionRuntimeDetailsEntity> findBycDateTimeInfeedMissionRuntimeDetails = infeedMissionRuntimeDetailsRepository
					.findByCreatedDatetime(cDatetime);
			return findBycDateTimeInfeedMissionRuntimeDetails;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List<InfeedMissionRuntimeDetailsEntity> findAllInfeedMissionRuntimeDetailsByDate(String startDate,
			String endDate) {
		try {
			List<InfeedMissionRuntimeDetailsEntity> findAllInfeedMissionRuntimeDetailsBetweenDates = infeedMissionRuntimeDetailsRepository
					.getAllInfeedMissionRuntimeDetailsBetweenDates(startDate, endDate);
			return findAllInfeedMissionRuntimeDetailsBetweenDates;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List<InfeedMissionRuntimeDetailsEntity> getByDate() {
		try {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			String currentDateTime = dateFormat.format(date);
			// System.out.println("currentDateTime::"+currentDateTime);
			return infeedMissionRuntimeDetailsRepository.findInfeedfeedMissionRuntimeDetailsBetweenDates(
					currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List<InfeedMissionRuntimeDetailsEntity> findByInfeedMissionId(int id) {
		try {
			List<InfeedMissionRuntimeDetailsEntity> findByInfeedMissionId = infeedMissionRuntimeDetailsRepository
					.findByinfeedMissionId(id);
			return findByInfeedMissionId;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List<InfeedMissionRuntimeDetailsEntity> fetchInfeedMissionRuntimeDetails() {
		try {
			return infeedMissionRuntimeDetailsRepository.findByInfeedMissionStatus("READY", "IN_PROGRESS");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void updateInfeedMissionRuntimeDetails(InfeedMissionRuntimeDetailsEntity infeedMissionRuntimeDetailsEntity) {

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDate = ft.format(dNow);
		System.out.println("currentDate :: " + currentDate);
		int positionId = infeedMissionRuntimeDetailsEntity.getPositionId();
		MasterPositionDetailsEntity findByPositionId = masterPositionDetailsRepositoryInstance
				.findByPositionId(positionId);
		MasterPalletInformationEntity findByPalletInformationId = masterPalletInformationRepositoryInstance
				.findByPalletInformationId(infeedMissionRuntimeDetailsEntity.getPalletInformationId());

		InfeedMissionRuntimeDetailsEntity findByPalletInformationId2 = infeedMissionRuntimeDetailsRepository
				.findByPalletInformationId(infeedMissionRuntimeDetailsEntity.getPalletInformationId());

		String infeedMissionStatus = infeedMissionRuntimeDetailsEntity.getInfeedMissionStatus();
		if (infeedMissionStatus.equals("ABORT")) {
			findByPositionId.setIsManualDispatch(0);
			findByPositionId.setPositionIsAllocated(0);
			findByPositionId.setCDateTime(currentDate);
			masterPositionDetailsRepositoryInstance.save(findByPositionId);
			findByPalletInformationId.setIsInfeedMissionGenerated(0);
			findByPalletInformationId.setCdatetime(currentDate);

			AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String name = authentication.getName();
			System.out.println(" name :: " + name);
			auditTrailDetailsEntity.setOperatorActions(
					"Infeed mission aborted for position " + findByPalletInformationId2.getPositionName()
							+ " having pallet code " + findByPalletInformationId2.getPalletCode() + " by " + name);
			auditTrailDetailsEntity.setField("Infeed mission aborted");
//			auditTrailDetailsEntity.setAfterValue(0);
//			auditTrailDetailsEntity.setBeforeValue(0);
			auditTrailDetailsEntity.setReason("Infeed mission aborted");

			auditTrailDetailsEntity.setUsername(name);
			auditTrailDetailsEntity.setDatetimeC(currentDate);
			auditTrailDetailsRepository.save(auditTrailDetailsEntity);

			masterPalletInformationRepositoryInstance.save(findByPalletInformationId);
		} else if (infeedMissionStatus.equals("COMPLETED")) {
			Date dNow1 = new Date();
			SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate1 = ft1.format(dNow1);
			System.out.println("currentDate1 :: " + currentDate1);
			findByPositionId.setIsManualDispatch(0);
			findByPositionId.setPositionIsAllocated(1);
			findByPositionId.setCDateTime(currentDate1);
			masterPositionDetailsRepositoryInstance.save(findByPositionId);
			findByPalletInformationId.setIsInfeedMissionGenerated(0);
			findByPalletInformationId.setCdatetime(currentDate1);
			masterPalletInformationRepositoryInstance.save(findByPalletInformationId);
			findByPalletInformationId2.setInfeedMissionEndDateTime(currentDate1);
			System.out.println("End date :: " + findByPalletInformationId2.getInfeedMissionEndDateTime());
			infeedMissionRuntimeDetailsRepository.save(findByPalletInformationId2);
			System.out.println("findByPalletInformationId2 ::" + findByPalletInformationId2);
			
			AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String name = authentication.getName();
			System.out.println(" name :: " + name);
			auditTrailDetailsEntity.setOperatorActions(
					"Infeed mission completed for position " + findByPalletInformationId2.getPositionName()
							+ " having pallet code " + findByPalletInformationId2.getPalletCode() + " by " + name);
			auditTrailDetailsEntity.setField("Infeed mission completed");
//			auditTrailDetailsEntity.setAfterValue(0);
//			auditTrailDetailsEntity.setBeforeValue(0);
			auditTrailDetailsEntity.setReason("Infeed mission completed");

			auditTrailDetailsEntity.setUsername(name);
			auditTrailDetailsEntity.setDatetimeC(currentDate);
			auditTrailDetailsRepository.save(auditTrailDetailsEntity);
		}
		System.out.println("masterPositionDetailsEntity" + findByPositionId);
		infeedMissionRuntimeDetailsRepository.save(infeedMissionRuntimeDetailsEntity);

	}

	public List<InfeedMissionRuntimeDetailsEntity> findByAllFilters(String floorName, String areaName,
			String infeedMissionStatus, String infeedMissionStartCdatetime, String infeedMissionEndCdatetime) {
		List<String> filterList = new ArrayList<String>();
		List<InfeedMissionRuntimeDetailsEntity> list = new ArrayList<InfeedMissionRuntimeDetailsEntity>();

		if (!floorName.equals("NA")) {
			filterList.add("floorName");
			// System.out.println("floorName::"+floorName);
		}
		if (!areaName.equals("NA")) {
			filterList.add("areaName");
			// System.out.println("areaName::"+areaName);
		}
		if (!infeedMissionStatus.equals("NA")) {
			filterList.add("infeedMissionStatus");
			// System.out.println("infeedMissionStatus::"+infeedMissionStatus);
		}
		Predicate<InfeedMissionRuntimeDetailsEntity> floorNamePred = data -> data.getFloorName().equals(floorName);
		Predicate<InfeedMissionRuntimeDetailsEntity> areaNamePred = data -> data.getAreaName().equals(areaName);
		Predicate<InfeedMissionRuntimeDetailsEntity> infeedMissionStatusPred = data -> data.getInfeedMissionStatus()
				.equals(infeedMissionStatus);

		if (!(infeedMissionStartCdatetime.equals("NA")) && !(infeedMissionEndCdatetime.equals("NA"))) {
			// Removinf "T" from dataetime format
			// System.out.println("startDateTime
			// ::"+infeedMissionStartCdatetime.toString().replace("T", " "));
			String startDateTime = infeedMissionStartCdatetime.toString().replace("T", " ");
			// System.out.println("endDateTime::"+
			// infeedMissionEndCdatetime.toString().replace("T", " "));
			String endDateTime = infeedMissionEndCdatetime.toString().replace("T", " ");

			list = infeedMissionRuntimeDetailsRepository.findInfeedfeedMissionRuntimeDetailsBetweenDates(startDateTime,
					endDateTime);
//			System.out.println("list from infeed::"+list.size());
//			System.out.println("list data::"+list.get(0));

		} else {
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
			String date = ft.format(dNow);
			// System.out.println("list from infeed ::"+list);
			list = infeedMissionRuntimeDetailsRepository
					.findInfeedfeedMissionRuntimeDetailsBetweenDates(date + " " + "00:00:00", date + " " + "23:59:59");
		}

		if (filterList.size() != 0) {

			for (int i = 0; i < filterList.size(); i++) {

				if (filterList.get(i).equals("floorName")) {
					list = list.stream().filter(floorNamePred).collect((Collectors.toList()));
					// System.out.println("floor list::"+list.size());
				} else if (filterList.get(i).equals("areaName")) {
					list = list.stream().filter(areaNamePred).collect((Collectors.toList()));
					// System.out.println("area list::"+list.size());
				} else if (filterList.get(i).equals("infeedMissionStatus")) {
					list = list.stream().filter(infeedMissionStatusPred).collect((Collectors.toList()));
					// System.out.println("status list::"+list.size());
				}
			}
		}

		if (filterList.size() == 0 && list.size() == 0) {
			list = null;
		}
		return list;
	}
}

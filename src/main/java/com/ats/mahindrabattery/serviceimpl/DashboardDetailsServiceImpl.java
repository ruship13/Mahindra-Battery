package com.ats.mahindrabattery.serviceimpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ats.mahindrabattery.entity.BufferDetailsEntity;
import com.ats.mahindrabattery.entity.CurrentPalletStockDetailsEntity;
import com.ats.mahindrabattery.entity.DashboardDetailsEntity;
import com.ats.mahindrabattery.entity.EquipmentAlarmHistoryEntity;
import com.ats.mahindrabattery.entity.GenerateManualRetrievalOrderEntity;
import com.ats.mahindrabattery.entity.InfeedMissionRuntimeDetailsEntity;
import com.ats.mahindrabattery.entity.MasterPositionDetailsEntity;
import com.ats.mahindrabattery.entity.OutfeedMissionRuntimeDetailsEntity;
import com.ats.mahindrabattery.repository.BufferDetailsRepository;
import com.ats.mahindrabattery.repository.CurrentPalletStockDetailsRepository;
import com.ats.mahindrabattery.repository.DashboardDetailsRepository;
import com.ats.mahindrabattery.repository.EquipmentAlarmHistoryRepository;
import com.ats.mahindrabattery.repository.GenerateManualRetrievalOrderRepository;
import com.ats.mahindrabattery.repository.InfeedMissionRuntimeDetailsRepository;
import com.ats.mahindrabattery.repository.MasterPositionDetailsRepository;
import com.ats.mahindrabattery.repository.OutfeedMissionruntimeDetailsRepository;
import com.ats.mahindrabattery.service.DashboardDetailsService;

@Service
public class DashboardDetailsServiceImpl implements DashboardDetailsService {
	  
	DashboardDetailsEntity dashboardEntity = new DashboardDetailsEntity();

	@Autowired
	private CurrentPalletStockDetailsRepository currentPalletStockDetailsRepository;

	@Autowired
	private EquipmentAlarmHistoryRepository equipmentAlarmHistoryRepository;

	@Autowired
	private InfeedMissionRuntimeDetailsRepository infeedMissionRuntimeDetailsRepository;

	@Autowired
	private OutfeedMissionruntimeDetailsRepository outfeedMissionRuntimeDetailsRepository;

	@Autowired
	private GenerateManualRetrievalOrderRepository generateManualRetrievalOrderRepository;

	@Autowired
	private CurrentPalletStockDetailsServiceImpl CurrentPalletStockDetailsServiceInstance;


	@Autowired
	private DashboardDetailsRepository dashboardDetailsRepository;

	@Autowired
	private MasterPositionDetailsRepository masterPositionDetailsRepository;

	@Autowired
	private BufferDetailsRepository bufferDetailsRepository;

	private int dashboardId;
	private int totalAlarmCount;
	private int bevInfeedCount;
	private int s230InfeedCount;
	private int bevOutfeedCount;
	private int s230OutfeedCount;
//	private int totalCurrentStockCount;
	private int currentokMaterialCount;
	private int currentNokMaterialCount;
	private int totalOrders;
	private int executedOrders;
	private int remainingOrders;
	private String percentageOrders = "";
	private float percentageOrders1;
	private String formattedPercentage;
	private int totalCurrentStock;
	private int totalNOKCount;
	private int totalBEVCount;
	private int totalOKCount;
	private int totalOKBEVCount;
	private int totalNOKBEVCount;
	private int totalS230NOKCount;
	private int totalS230OKCount;
	private int totalInfeedCount;
	private int totalOutfeedCount;

	private int s230Count;
	private int bevCount;
	private int bevEmptyPalletCount;
	private int s230EmptyPalletCount;

	private int bevCurrentStockCount;
	private int s230CurrentStockCount;

	private int bevBufferCount;
	private int s230BufferCount;
	private int area1Count;
	private int area2Count;

	public Page<CurrentPalletStockDetailsEntity> findAll(Pageable pageable) {
		return currentPalletStockDetailsRepository.findAll(pageable);
	}

	// CurrentStock All Detail by CdateTime
	public List<CurrentPalletStockDetailsEntity> getByDate() {
		try {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			String currentDateTime = dateFormat.format(date);

			// Create an instance of the repository
			CurrentPalletStockDetailsRepository repository = currentPalletStockDetailsRepository;

			// Call the instance method on the repository
			return repository.findCurrentStockDetailsBetweenDates(currentDateTime + " " + "00:00:00",
					currentDateTime + " " + "23:59:59");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// Infeed details by currentdate for BEV Material
	public List<InfeedMissionRuntimeDetailsEntity> getBevInfeedDetailsByDate() {
		try {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			String currentDateTime = dateFormat.format(date);
			// System.out.println("currentDateTime::"+currentDateTime);
			return infeedMissionRuntimeDetailsRepository
					.findByInfeedMissionEndDateTimeBetweenAndInfeedMissionIsDeletedAndProductNameAndInfeedMissionStatusAndPalletStatusIdNot(
							currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59", 0, "BEV",
							"COMPLETED", 3);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// Infeed details by currentdate for BEV Material
	public List<InfeedMissionRuntimeDetailsEntity> getS230InfeedDetailsByDate() {
		try {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			String currentDateTime = dateFormat.format(date);
			// System.out.println("currentDateTime::"+currentDateTime);
			return infeedMissionRuntimeDetailsRepository
					.findByInfeedMissionEndDateTimeBetweenAndInfeedMissionIsDeletedAndProductNameAndInfeedMissionStatusAndPalletStatusIdNot(
							currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59", 0, "S230",
							"COMPLETED", 3);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}



	public int getInfeedDetailsByDate() {
		try {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			String currentDateTime = dateFormat.format(date);
			// System.out.println("currentDateTime::"+currentDateTime);
			List<InfeedMissionRuntimeDetailsEntity> findBycreatedDatetimeBetweenAndInfeedMissionIsDeleted = infeedMissionRuntimeDetailsRepository
					.findByInfeedMissionIsDeletedAndPalletStatusIdNotAndInfeedMissionStatus(0, 3, "COMPLETED");
			totalInfeedCount = findBycreatedDatetimeBetweenAndInfeedMissionIsDeleted.size();
			dashboardEntity.setTotalInfeedCount(totalInfeedCount);
//						 System.out.println("totalInfeedCount::"+dashboardEntity.getTotalInfeedCount());
			return dashboardEntity.getTotalInfeedCount();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (Integer) null;

	}


	// Outfeed details by currentdate for BEV Material
	public List<OutfeedMissionRuntimeDetailsEntity> getBevOuteedDetailsByDate() {
		try {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			String currentDateTime = dateFormat.format(date);
			// System.out.println("currentDateTime::"+currentDateTime);
			return outfeedMissionRuntimeDetailsRepository
					.findByOutfeedMissionEndDateTimeBetweenAndOutfeedMissionIsDeletedAndProductNameAndOutfeedMissionStatusAndPalletStatusIdNot(
							currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59", 0, "BEV",
							"COMPLETED", 3);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// Outfeed details by currentdate for S230 Material
	public List<OutfeedMissionRuntimeDetailsEntity> getS230OuteedDetailsByDate() {
		try {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			String currentDateTime = dateFormat.format(date);
			// System.out.println("currentDateTime::"+currentDateTime);
			return outfeedMissionRuntimeDetailsRepository
					.findByOutfeedMissionEndDateTimeBetweenAndOutfeedMissionIsDeletedAndProductNameAndOutfeedMissionStatusAndPalletStatusIdNot(
							currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59", 0, "S230",
							"COMPLETED", 3);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}



	public int getOutfeedDetailsByDate() {
		try {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			String currentDateTime = dateFormat.format(date);
			// System.out.println("currentDateTime::"+currentDateTime);
			List<OutfeedMissionRuntimeDetailsEntity> findBycreatedDatetimeBetweenAndOutfeedMissionIsDeleted = outfeedMissionRuntimeDetailsRepository
					.findByOutfeedMissionIsDeletedAndOutfeedMissionStatusAndPalletStatusIdNot(0, "COMPLETED", 3);
			totalOutfeedCount = findBycreatedDatetimeBetweenAndOutfeedMissionIsDeleted.size();
			dashboardEntity.setTotalOutfeedCount(totalOutfeedCount);
			// System.out.println("totalOutfeedCount::"+dashboardEntity.getTotalOutfeedCount());
			return dashboardEntity.getTotalOutfeedCount();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (Integer) null;

	}

	@SuppressWarnings("null")
	public int findbevOutfeedDetailsByCurrentDate() {
		try {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String createdDatetime = simpleDateFormat.format(date);
			List<OutfeedMissionRuntimeDetailsEntity> findBycreatedDatetimeBetweenAndOutfeedMissionIsDeletedAndProductVariantName = outfeedMissionRuntimeDetailsRepository
					.findByOutfeedMissionEndDateTimeBetweenAndOutfeedMissionIsDeletedAndProductNameAndOutfeedMissionStatusAndPalletStatusIdNot(
							createdDatetime + " " + "00:00:00", createdDatetime + " " + "23:59:59", 0, "BEV",
							"COMPLETED", 3);
			bevOutfeedCount = findBycreatedDatetimeBetweenAndOutfeedMissionIsDeletedAndProductVariantName.size();

			dashboardEntity.setBevOutfeedCount(bevOutfeedCount);
			return dashboardEntity.getBevOutfeedCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	@SuppressWarnings("null")
	public int finds230OutfeedDetailsByCurrentDate() {
		try {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String createdDatetime = simpleDateFormat.format(date);
			List<OutfeedMissionRuntimeDetailsEntity> findBycreatedDatetimeBetweenAndOutfeedMissionIsDeletedAndProductVariantName = outfeedMissionRuntimeDetailsRepository
					.findByOutfeedMissionEndDateTimeBetweenAndOutfeedMissionIsDeletedAndProductNameAndOutfeedMissionStatusAndPalletStatusIdNot(
							createdDatetime + " " + "00:00:00", createdDatetime + " " + "23:59:59", 0, "S230",
							"COMPLETED", 3);
			s230OutfeedCount = findBycreatedDatetimeBetweenAndOutfeedMissionIsDeletedAndProductVariantName.size();

			dashboardEntity.setS230OutfeedCount(s230OutfeedCount);
			return dashboardEntity.getS230OutfeedCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	@SuppressWarnings("null")
	public int finds230InfeedDetailsByCurrentDate() {
		try {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String createdDatetime = simpleDateFormat.format(date);
			List<InfeedMissionRuntimeDetailsEntity> findBycreatedDatetimeBetweenAndInfeedMissionIsDeletedAndProductVariantName = infeedMissionRuntimeDetailsRepository
					.findByInfeedMissionEndDateTimeBetweenAndInfeedMissionIsDeletedAndProductNameAndInfeedMissionStatusAndPalletStatusIdNot(
							createdDatetime + " " + "00:00:00", createdDatetime + " " + "23:59:59", 0, "S230",
							"COMPLETED", 3);

			s230InfeedCount = findBycreatedDatetimeBetweenAndInfeedMissionIsDeletedAndProductVariantName.size();
			dashboardEntity.setS230InfeedCount(s230InfeedCount);
			return dashboardEntity.getS230InfeedCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	@SuppressWarnings("null")
	public int findBEVInfeedDetailsByCurrentDate() {
		try {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String createdDatetime = simpleDateFormat.format(date);
			List<InfeedMissionRuntimeDetailsEntity> findBycreatedDatetimeBetweenAndInfeedMissionIsDeletedAndProductVariantName = infeedMissionRuntimeDetailsRepository
					.findByInfeedMissionEndDateTimeBetweenAndInfeedMissionIsDeletedAndProductNameAndInfeedMissionStatusAndPalletStatusIdNot(
							createdDatetime + " " + "00:00:00", createdDatetime + " " + "23:59:59", 0, "BEV",
							"COMPLETED", 3);

			bevInfeedCount = findBycreatedDatetimeBetweenAndInfeedMissionIsDeletedAndProductVariantName.size();
			dashboardEntity.setBevInfeedCount(bevInfeedCount);
			return dashboardEntity.getBevInfeedCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	@SuppressWarnings("null")
	public int findTotalCurrentStockDetails() {
		try {
			List<CurrentPalletStockDetailsEntity> findByPalletCodeNot = currentPalletStockDetailsRepository
					.findByPalletCodeNot("NA");
			totalCurrentStock = findByPalletCodeNot.size();
			dashboardEntity.setTotalCurrentStockCount(totalCurrentStock);
			return dashboardEntity.getTotalCurrentStockCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	// CurrentStock Ok material Status details by currentdate for ok Material
	public List<CurrentPalletStockDetailsEntity> getOkMaterialCurrentStockDetailsByDate() {
		try {

			return currentPalletStockDetailsRepository.findByQualityStatus("OK");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Page<CurrentPalletStockDetailsEntity> getOkMaterialCurrentStockDetailsPage(Pageable pageable) {
		try {

			Page<CurrentPalletStockDetailsEntity> findAll = currentPalletStockDetailsRepository.findAll(pageable);
			List<CurrentPalletStockDetailsEntity> collect = findAll.stream()
					.filter(e -> e.getQualityStatus().equals("OK")).collect(Collectors.toList());
			return (Page<CurrentPalletStockDetailsEntity>) collect;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		return null;
	}

	// CurrentStock NOk material Status details by currentdate for Nok Material
	public List<CurrentPalletStockDetailsEntity> getNOkMaterialCurrentStockDetailsByDate() {
		try {


			return currentPalletStockDetailsRepository.findByQualityStatus("NOK");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// CurrentStock NOk BEV material count
	@SuppressWarnings("null")
	public int getNOkBEVMaterialCurrentStockDetails() {
		try {
			List<CurrentPalletStockDetailsEntity> findByQualityStatus = currentPalletStockDetailsRepository
					.findByQualityStatusAndProductNameAndPalletStatusnameAndPalletCodeNot("NOK", "BEV", "FULL", "NA");

			totalNOKBEVCount = findByQualityStatus.size();
			dashboardEntity.setTotalNOKBEVCount(totalNOKBEVCount);
			return dashboardEntity.getTotalNOKBEVCount();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (Integer) null;
	}

	// CurrentStock Ok BEV material count
	@SuppressWarnings("null")
	public int getOkBEVMaterialCurrentStockDetails() {
		try {
			List<CurrentPalletStockDetailsEntity> findByQualityStatus = currentPalletStockDetailsRepository
					.findByQualityStatusAndProductNameAndPalletStatusnameAndPalletCodeNot("OK", "BEV", "FULL", "NA");

			totalOKBEVCount = findByQualityStatus.size();
			
			dashboardEntity.setTotalOKBEVCount(totalOKBEVCount);
			return dashboardEntity.getTotalOKBEVCount();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (Integer) null;
	}

	// CurrentStock NOk S230 material count
	@SuppressWarnings("null")
	public int getNOkS230MaterialCurrentStockDetails() {
		try {
			List<CurrentPalletStockDetailsEntity> findByQualityStatus = currentPalletStockDetailsRepository
					.findByQualityStatusAndProductNameAndPalletStatusnameAndPalletCodeNot("NOK", "S230", "FULL", "NA");

			totalS230NOKCount = findByQualityStatus.size();
			dashboardEntity.setTotalS230NOKCount(totalS230NOKCount);
			return dashboardEntity.getTotalS230NOKCount();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (Integer) null;
	}

	// CurrentStock NOk S230 material count
	@SuppressWarnings("null")
	public int getOkS230MaterialCurrentStockDetails() {
		try {
			List<CurrentPalletStockDetailsEntity> findByQualityStatus = currentPalletStockDetailsRepository
					.findByQualityStatusAndProductNameAndPalletStatusnameAndPalletCodeNot("OK", "S230", "FULL", "NA");

			totalS230OKCount = findByQualityStatus.size();
			
			dashboardEntity.setTotalS230OKCount(totalS230OKCount);
			return dashboardEntity.getTotalS230OKCount();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (Integer) null;
	}



	@SuppressWarnings("null")
	public int findNokCurrentStockByCurrentDate() {
		try {

			List<CurrentPalletStockDetailsEntity> findByLoadDatetimeBetweenAndQualityStatus = currentPalletStockDetailsRepository
					.findByQualityStatusAndPalletCodeNotAndPalletStatusIdNot("NOK", "NA", 3);
			currentNokMaterialCount = findByLoadDatetimeBetweenAndQualityStatus.size();
			dashboardEntity.setCurrentNokMaterialCount(currentNokMaterialCount);
			return dashboardEntity.getCurrentNokMaterialCount();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	@SuppressWarnings("null")
	public int findOkCurrentStockByCurrentDate() {
		try {

			List<CurrentPalletStockDetailsEntity> findByLoadDatetimeBetweenAndQualityStatus = currentPalletStockDetailsRepository
					.findByQualityStatusAndPalletCodeNotAndPalletStatusIdNot("OK", "NA", 3);
			currentokMaterialCount = findByLoadDatetimeBetweenAndQualityStatus.size();
			dashboardEntity.setCurrentokMaterialCount(currentokMaterialCount);
			return dashboardEntity.getCurrentokMaterialCount();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	@SuppressWarnings("null")
	public int findEquipmentAlarmHistoryByCDatetime() {
		try {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = simpleDateFormat.format(date);

			List<EquipmentAlarmHistoryEntity> findByEquipmentAlarmOccurredDatetimeBetween = equipmentAlarmHistoryRepository
					.findByEquipmentAlarmOccurredDatetimeBetween(currentDate + " " + "00:00:00",
							currentDate + " " + "23:59:59");
			totalAlarmCount = findByEquipmentAlarmOccurredDatetimeBetween.size();
			dashboardEntity.setTotalAlarmCount(totalAlarmCount);
			return dashboardEntity.getTotalAlarmCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}


	@SuppressWarnings("null")
	public int findBEVCurrentStockDetails() {
		try {

			List<CurrentPalletStockDetailsEntity> findByProductName = currentPalletStockDetailsRepository
					.findByProductNameAndPalletStatusIdNot("BEV", 3);

			bevCurrentStockCount = findByProductName.size();
			dashboardEntity.setBevCurrentStockCount(bevCurrentStockCount);
			return dashboardEntity.getBevCurrentStockCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	@SuppressWarnings("null")
	public int findS230CurrentStockDetails() {
		try {

			List<CurrentPalletStockDetailsEntity> findByProductName = currentPalletStockDetailsRepository
					.findByProductNameAndPalletStatusIdNot("S230", 3);

			s230CurrentStockCount = findByProductName.size();
			dashboardEntity.setS230CurrentStockCount(s230CurrentStockCount);
			return dashboardEntity.getS230CurrentStockCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}



	@SuppressWarnings("null")
	public int findBevEmptyPalletCountCurrentStockDetails() {
		try {
			List<CurrentPalletStockDetailsEntity> findByProductName = currentPalletStockDetailsRepository
					.findByProductVariantCodeAndQuantityAndProductName("NA", 0, "BEV");

			bevEmptyPalletCount = (int) findByProductName.parallelStream().filter(e -> e.getPalletStatusId() == 3)
					.filter(e -> {
						MasterPositionDetailsEntity positionDetails = masterPositionDetailsRepository
								.findById(e.getPositionId()).orElse(null);
						return positionDetails != null && positionDetails.getEmptyPalletPosition() == 0
								&& positionDetails.getIsMaterialLoaded() == 0;
					}).count();

			dashboardEntity.setBevEmptyPalletCount(bevEmptyPalletCount);
			return dashboardEntity.getBevEmptyPalletCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	@SuppressWarnings("null")
	public int findS230EmptyPalletCountCurrentStockDetails() {
		try {
			List<CurrentPalletStockDetailsEntity> findByProductName = currentPalletStockDetailsRepository
					.findByProductVariantCodeAndQuantityAndProductName("NA", 0, "S230");

			s230EmptyPalletCount = (int) findByProductName.parallelStream().filter(e -> e.getPalletStatusId() == 3)
					.filter(e -> {
						MasterPositionDetailsEntity positionDetails = masterPositionDetailsRepository
								.findById(e.getPositionId()).orElse(null);
						return positionDetails != null && positionDetails.getEmptyPalletPosition() == 0
								&& positionDetails.getIsMaterialLoaded() == 0;
					}).count();

			dashboardEntity.setS230EmptyPalletCount(s230EmptyPalletCount);
			return dashboardEntity.getS230EmptyPalletCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}


	public List<String> fetchFormattedDates() throws ParseException {
		List<DashboardDetailsEntity> list = new ArrayList<>(
				dashboardDetailsRepository.findTop30ByOrderByDashboardIdDesc());

		List<String> datetimeCon = new ArrayList<>();

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("dd MMM");

		for (int i = 0; i < list.size(); i++) {
			Date date = format1.parse(list.get(i).getCDateTime().substring(0, 10));
			list.get(i).setCDateTime(format2.format(date));
			datetimeCon.add(format2.format(date));
		}

		Collections.reverse(datetimeCon);
		return datetimeCon;
	}

	public List<DashboardDetailsEntity> fetchProductionTrendDetails() {
		try {
			List<DashboardDetailsEntity> list = new ArrayList<>(
					dashboardDetailsRepository.findTop30ByOrderByDashboardIdDesc());

			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format2 = new SimpleDateFormat("dd MMM");

			for (int i = 0; i < list.size(); i++) {
				Date date = format1.parse(list.get(i).getCDateTime().substring(0, 10));
				list.get(i).setCDateTime(format2.format(date));
			}

			Collections.reverse(list);

			return list;
		} catch (Exception ex) {

			ex.printStackTrace();
			return null;

		}

	}


		
	public Page<CurrentPalletStockDetailsEntity> findByQualityStatus(String qualityStatus, Pageable pageable) {
		Page<CurrentPalletStockDetailsEntity> findByQualityStatus = currentPalletStockDetailsRepository
				.findByQualityStatusAndPalletCodeNot(qualityStatus, "NA", pageable);
		return findByQualityStatus;
	}

	public Page<CurrentPalletStockDetailsEntity> getByQualityStatus(String qualityStatus, Pageable pageable) {
		Page<CurrentPalletStockDetailsEntity> getByQualityStatus = currentPalletStockDetailsRepository
				.getByQualityStatus(qualityStatus, pageable);
		return getByQualityStatus;
	}

	@SuppressWarnings("null")
	public int findTotalOrderDetailsByCurrentDate() {
		try {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String createdDatetime = simpleDateFormat.format(date);

			List<GenerateManualRetrievalOrderEntity> orders = generateManualRetrievalOrderRepository
					.findByCreatedDatetimeBetweenAndIsOrderDeletedAndIsOrderCancelledAndDispatchStatus(
							createdDatetime + " " + "00:00:00", createdDatetime + " " + "23:59:59", 0, 0, "COMPLETED");
			List<GenerateManualRetrievalOrderEntity> findByDispatchStatusIn = generateManualRetrievalOrderRepository
					.findByIsOrderDeletedAndDispatchStatusIn(0, Arrays.asList("READY", "IN_PROGRESS"));

			totalOrders = orders.size() + findByDispatchStatusIn.size();

			dashboardEntity.setTotalOrders(totalOrders);

			return dashboardEntity.getTotalOrders();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	@SuppressWarnings("null")
	public int findExecutedOrder() {
		try {


			findTotalOrderDetailsByCurrentDate();
			openOrders();

			executedOrders = (dashboardEntity.getTotalOrders()) - (dashboardEntity.getRemainingOrders());
			dashboardEntity.setExecutedOrders(executedOrders);
			System.out.println("executedOrders::" + dashboardEntity.getExecutedOrders());
			return dashboardEntity.getExecutedOrders();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	public int openOrders() {
		try {

			List<GenerateManualRetrievalOrderEntity> orders = generateManualRetrievalOrderRepository
					.findByIsOrderDeletedAndDispatchStatusIn(0, Arrays.asList("READY", "IN_PROGRESS"));
			remainingOrders = orders.size();

			dashboardEntity.setRemainingOrders(remainingOrders);

			return dashboardEntity.getRemainingOrders();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}


	public String percentageOrders() {
		try {
			findTotalOrderDetailsByCurrentDate();
			findExecutedOrder();

			if (dashboardEntity.getTotalOrders() == 0) {

				DecimalFormat decimalFormatter = new DecimalFormat("0.0000");
				formattedPercentage = decimalFormatter.format(0.0000);

				dashboardEntity.setPercentageOrders(formattedPercentage);
				System.out.println("percentorder::" + dashboardEntity.getPercentageOrders());

				return dashboardEntity.getPercentageOrders();
			}

			float percentageOrders1 = (dashboardEntity.getExecutedOrders() * 100.0f) / dashboardEntity.getTotalOrders();

			// Format to 3 decimal places
			DecimalFormat decimalFormatter = new DecimalFormat("0.0000");
			formattedPercentage = decimalFormatter.format(percentageOrders1);

			dashboardEntity.setPercentageOrders(formattedPercentage);
			System.out.println("percentorder::" + dashboardEntity.getPercentageOrders());

			return dashboardEntity.getPercentageOrders();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	@SuppressWarnings("null")
	public int findTotalBEVBufferCount() {
		try {

			List<BufferDetailsEntity> bevbuffercount = bufferDetailsRepository
					.findByProductNameAndBufferIsDeleted("BEV", 0);

			bevBufferCount = bevbuffercount.size();

			dashboardEntity.setBevBufferCount(bevBufferCount);

			return dashboardEntity.getBevBufferCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	@SuppressWarnings("null")
	public int findTotalS230BufferCount() {
		try {

			List<BufferDetailsEntity> s230buffercount = bufferDetailsRepository
					.findByProductNameAndBufferIsDeleted("S230", 0);

			s230BufferCount = s230buffercount.size();

			dashboardEntity.setS230BufferCount(s230BufferCount);

			return dashboardEntity.getS230BufferCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	public List<InfeedMissionRuntimeDetailsEntity> findByTotalInfeedCount() {
		Date date = new Date();
		String strDateFormat = "yyyy-MM-dd";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String currentDateTime = dateFormat.format(date);

		List<InfeedMissionRuntimeDetailsEntity> getBycreatedDatetimeBetweenAndInfeedMissionIsDeleted = infeedMissionRuntimeDetailsRepository
				.findByInfeedMissionIsDeletedAndPalletStatusIdNotAndInfeedMissionStatus(0, 3, "COMPLETED");
		return getBycreatedDatetimeBetweenAndInfeedMissionIsDeleted;
	}

	public List<OutfeedMissionRuntimeDetailsEntity> findByTotalOutfeedCount() {
		Date date = new Date();
		String strDateFormat = "yyyy-MM-dd";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String currentDateTime = dateFormat.format(date);

		List<OutfeedMissionRuntimeDetailsEntity> getBycreatedDatetimeBetweenAndOutfeedMissionIsDeleted = outfeedMissionRuntimeDetailsRepository
				.getByOutfeedMissionIsDeletedAndPalletStatusIdNotAndOutfeedMissionStatus(0, 3, "COMPLETED");
		return getBycreatedDatetimeBetweenAndOutfeedMissionIsDeleted;
	}

	public int findtotalInfeedCount() {
		int totalInfeedBEVCountDashboard = 0;
		int totalInfeedS230CountDashboard = 0;
		int totalInfeedCountDashboard = 0;
		List<DashboardDetailsEntity> findAll = dashboardDetailsRepository.findAll();
		for (int i = 0; i < findAll.size(); i++) {
			int totalInfeedCount2 = findAll.get(i).getBevInfeedCount();
			totalInfeedBEVCountDashboard = totalInfeedBEVCountDashboard + totalInfeedCount2;
		}
		for (int j = 0; j < findAll.size(); j++) {
			int totalInfeedCount2 = findAll.get(j).getS230InfeedCount();
			totalInfeedS230CountDashboard = totalInfeedS230CountDashboard + totalInfeedCount2;
		}
		totalInfeedCountDashboard = totalInfeedBEVCountDashboard + totalInfeedS230CountDashboard;
		return totalInfeedCountDashboard;
	}

	public int findtotalOutfeedCount() {
		int totalBEVOutfeedCountDashboard = 0;
		int totalS230OutfeedCountDashboard = 0;
		int totalOutfeedCountDashboard = 0;
		List<DashboardDetailsEntity> findAll = dashboardDetailsRepository.findAll();
		for (int i = 0; i < findAll.size(); i++) {
			int totalBEVOutfeedCount2 = findAll.get(i).getBevOutfeedCount();
			totalBEVOutfeedCountDashboard = totalBEVOutfeedCountDashboard + totalBEVOutfeedCount2;
		}
		for (int i = 0; i < findAll.size(); i++) {
			int totalS230OutfeedCount2 = findAll.get(i).getS230OutfeedCount();
			totalS230OutfeedCountDashboard = totalS230OutfeedCountDashboard + totalS230OutfeedCount2;
		}
		totalOutfeedCountDashboard = totalBEVOutfeedCountDashboard + totalS230OutfeedCountDashboard;
		return totalOutfeedCountDashboard;
	}

	public int Area1Count() {
		List<CurrentPalletStockDetailsEntity> findByAreaNameAndPalletCodeNot = currentPalletStockDetailsRepository
				.findByAreaNameAndPalletCodeNotAndPalletStatusIdNot("Area-1", "NA", 3);

		area1Count = findByAreaNameAndPalletCodeNot.size();
		dashboardEntity.setArea1Count(area1Count);
		return dashboardEntity.getArea1Count();

	}

	public int Area2Count() {
		List<CurrentPalletStockDetailsEntity> findByAreaNameAndPalletCodeNot = currentPalletStockDetailsRepository
				.findByAreaNameAndPalletCodeNotAndPalletStatusIdNot("Area-2", "NA", 3);

		area2Count = findByAreaNameAndPalletCodeNot.size();
		dashboardEntity.setArea2Count(area2Count);
		return dashboardEntity.getArea2Count();

	}


	

	
//	public DashboardDetailsEntity findAllDashboard2() {
//	    DashboardDetailsEntity findTopByOrderByDashboardIdDesc = dashboardDetailsRepository
//	            .findTopByOrderByDashboardIdDesc();
//	    ExecutorService executor = null;
//
//	    try {
//	        System.out.println("2");
//	        Date date = new Date();
//	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//	        String currentDate = simpleDateFormat.format(date);
//
//	        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	        String createdDatetime = simpleDateFormat1.format(date);
//
//	        System.out.println(createdDatetime);
//
//	        int taskCount = 26;
//	        CountDownLatch latch = new CountDownLatch(taskCount);
//
//	        // Use a cached thread pool to allow for efficient thread management
//	        executor = Executors.newCachedThreadPool();  // Reclaims threads more efficiently
//
//	        // Submit tasks to executor
//	        executor.submit(() -> { findEquipmentAlarmHistoryByCDatetime(); latch.countDown(); });
//	        executor.submit(() -> { findBEVInfeedDetailsByCurrentDate(); latch.countDown(); });
//	        executor.submit(() -> { finds230InfeedDetailsByCurrentDate(); latch.countDown(); });
//	        executor.submit(() -> { findbevOutfeedDetailsByCurrentDate(); latch.countDown(); });
//	        executor.submit(() -> { finds230OutfeedDetailsByCurrentDate(); latch.countDown(); });
//	        executor.submit(() -> { findOkCurrentStockByCurrentDate(); latch.countDown(); });
//	        executor.submit(() -> { findNokCurrentStockByCurrentDate(); latch.countDown(); });
//	        executor.submit(() -> { findTotalOrderDetailsByCurrentDate(); latch.countDown(); });
//	        executor.submit(() -> { openOrders(); latch.countDown(); });
//	        executor.submit(() -> { findExecutedOrder(); latch.countDown(); });
//	        executor.submit(() -> { percentageOrders(); latch.countDown(); });
//	        executor.submit(() -> { getNOkBEVMaterialCurrentStockDetails(); latch.countDown(); });
//	        executor.submit(() -> { getNOkS230MaterialCurrentStockDetails(); latch.countDown(); });
//	        executor.submit(() -> { getOkBEVMaterialCurrentStockDetails(); latch.countDown(); });
//	        executor.submit(() -> { getOkS230MaterialCurrentStockDetails(); latch.countDown(); });
//	        executor.submit(() -> { findTotalCurrentStockDetails(); latch.countDown(); });
//	        executor.submit(() -> { getInfeedDetailsByDate(); latch.countDown(); });
//	        executor.submit(() -> { getOutfeedDetailsByDate(); latch.countDown(); });
//	        executor.submit(() -> { findBevEmptyPalletCountCurrentStockDetails(); latch.countDown(); });
//	        executor.submit(() -> { findS230EmptyPalletCountCurrentStockDetails(); latch.countDown(); });
//	        executor.submit(() -> { findS230CurrentStockDetails(); latch.countDown(); });
//	        executor.submit(() -> { findBEVCurrentStockDetails(); latch.countDown(); });
//	        executor.submit(() -> { findTotalBEVBufferCount(); latch.countDown(); });
//	        executor.submit(() -> { findTotalS230BufferCount(); latch.countDown(); });
//	        executor.submit(() -> { Area1Count(); latch.countDown(); });
//	        executor.submit(() -> { Area2Count(); latch.countDown(); });
//
//	        // Wait for all threads to finish with a timeout
//	        if (!latch.await(60, TimeUnit.SECONDS)) {
//	            System.out.println("Timeout while waiting for tasks to complete.");
//	        }
//
//	        // Now retrieve the computed value safely
//	        String percentageOrders2 = dashboardEntity.getPercentageOrders();
//	        System.out.println("percent::" + percentageOrders2);
//
//	        List<DashboardDetailsEntity> findBycDateTimeBetween = dashboardDetailsRepository
//	                .findBycDateTimeContaining(currentDate);
//
//	        if (findBycDateTimeBetween.isEmpty()) {
//	            // If no records for the current date, create and save a new entry
//	            DashboardDetailsEntity dashboardDetailsEntity = new DashboardDetailsEntity(
//	                    findTopByOrderByDashboardIdDesc.getDashboardId() + 1, totalAlarmCount, bevInfeedCount,
//	                    s230InfeedCount, bevOutfeedCount, s230OutfeedCount, currentokMaterialCount,
//	                    currentNokMaterialCount, totalOrders, executedOrders, remainingOrders, percentageOrders2,
//	                    totalCurrentStock, totalInfeedCount, totalOutfeedCount, totalOKBEVCount, totalNOKBEVCount,
//	                    totalS230OKCount, totalS230NOKCount, createdDatetime, bevCurrentStockCount,
//	                    s230CurrentStockCount, bevEmptyPalletCount, s230EmptyPalletCount, bevBufferCount,
//	                    s230BufferCount, area1Count, area2Count);
//
//	            dashboardDetailsRepository.save(dashboardDetailsEntity);
//	        } else {
//	            // If there is already a record, check if the current date matches
//	            String cDateTime = findTopByOrderByDashboardIdDesc.getCDateTime();
//	            if (cDateTime.substring(0, 10).equalsIgnoreCase(currentDate)) {
//	                // Update the existing record with the new values
//	                findTopByOrderByDashboardIdDesc.setTotalAlarmCount(totalAlarmCount);
//	                findTopByOrderByDashboardIdDesc.setBevInfeedCount(bevInfeedCount);
//	                findTopByOrderByDashboardIdDesc.setS230InfeedCount(s230InfeedCount);
//	                findTopByOrderByDashboardIdDesc.setBevOutfeedCount(bevOutfeedCount);
//	                findTopByOrderByDashboardIdDesc.setS230OutfeedCount(s230OutfeedCount);
//	                findTopByOrderByDashboardIdDesc.setCurrentokMaterialCount(currentokMaterialCount);
//	                findTopByOrderByDashboardIdDesc.setCurrentNokMaterialCount(currentNokMaterialCount);
//	                findTopByOrderByDashboardIdDesc.setTotalOrders(totalOrders);
//	                findTopByOrderByDashboardIdDesc.setExecutedOrders(executedOrders);
//	                findTopByOrderByDashboardIdDesc.setRemainingOrders(remainingOrders);
//	                findTopByOrderByDashboardIdDesc.setPercentageOrders(percentageOrders2);
//	                findTopByOrderByDashboardIdDesc.setTotalCurrentStockCount(totalCurrentStock);
//	                findTopByOrderByDashboardIdDesc.setTotalInfeedCount(totalInfeedCount);
//	                findTopByOrderByDashboardIdDesc.setTotalOutfeedCount(totalOutfeedCount);
//	                findTopByOrderByDashboardIdDesc.setTotalOKBEVCount(totalOKBEVCount);
//	                findTopByOrderByDashboardIdDesc.setTotalNOKBEVCount(totalNOKBEVCount);
//	                findTopByOrderByDashboardIdDesc.setTotalS230OKCount(totalS230OKCount);
//	                findTopByOrderByDashboardIdDesc.setTotalS230NOKCount(totalS230NOKCount);
//	                findTopByOrderByDashboardIdDesc.setCDateTime(createdDatetime);
//	                findTopByOrderByDashboardIdDesc.setBevCurrentStockCount(bevCurrentStockCount);
//	                findTopByOrderByDashboardIdDesc.setS230CurrentStockCount(s230CurrentStockCount);
//	                findTopByOrderByDashboardIdDesc.setBevEmptyPalletCount(bevEmptyPalletCount);
//	                findTopByOrderByDashboardIdDesc.setS230EmptyPalletCount(s230EmptyPalletCount);
//	                findTopByOrderByDashboardIdDesc.setBevBufferCount(bevBufferCount);
//	                findTopByOrderByDashboardIdDesc.setS230BufferCount(s230BufferCount);
//	                findTopByOrderByDashboardIdDesc.setArea1Count(area1Count);
//	                findTopByOrderByDashboardIdDesc.setArea2Count(area2Count);
//
//	                dashboardDetailsRepository.save(findTopByOrderByDashboardIdDesc);
//	            } else {
//	                // If the current date does not match, create a new entry
//	                DashboardDetailsEntity dashboardDetailsEntity = new DashboardDetailsEntity(
//	                        findTopByOrderByDashboardIdDesc.getDashboardId() + 1, totalAlarmCount, bevInfeedCount,
//	                        s230InfeedCount, bevOutfeedCount, s230OutfeedCount, currentokMaterialCount,
//	                        currentNokMaterialCount, totalOrders, executedOrders, remainingOrders, percentageOrders2,
//	                        totalCurrentStock, totalInfeedCount, totalOutfeedCount, totalOKBEVCount, totalNOKBEVCount,
//	                        totalS230OKCount, totalS230NOKCount, createdDatetime, bevCurrentStockCount,
//	                        s230CurrentStockCount, bevEmptyPalletCount, s230EmptyPalletCount, bevBufferCount,
//	                        s230BufferCount, area1Count, area2Count);
//
//	                dashboardDetailsRepository.save(dashboardDetailsEntity);
//	            }
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    } finally {
//	        if (executor != null) {
//	            // Ensure thread pool is shut down properly to avoid memory leaks
//	            executor.shutdown();
//	            try {
//	                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
//	                    System.err.println("Executor did not terminate in time.");
//	                    executor.shutdownNow();
//	                }
//	            } catch (InterruptedException e) {
//	                Thread.currentThread().interrupt();
//	                executor.shutdownNow();
//	            }
//	        }
//	    }
//
//	    return findTopByOrderByDashboardIdDesc;
//	}



	    private static final Logger logger = LoggerFactory.getLogger(DashboardDetailsServiceImpl.class);
		@Scheduled(cron = "0 1 0 * * *")
	    public DashboardDetailsEntity findAllDashboard() {
	        DashboardDetailsEntity latestDashboard = dashboardDetailsRepository.findTopByOrderByDashboardIdDesc();
	        ExecutorService executor = null;

	        try {
	            logger.info("Starting findAllDashboard2 at {}", new Date());

	            Date now = new Date();
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            String currentDate = dateFormat.format(now);

	            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            String createdDateTime = dateTimeFormat.format(now);
	            logger.info("Current timestamp: {}", createdDateTime);

	            int totalTasks = 26;
	            CountDownLatch latch = new CountDownLatch(totalTasks);
	            executor = Executors.newCachedThreadPool();

	            // Submit all your tasks here
	            executor.submit(() -> { findEquipmentAlarmHistoryByCDatetime(); latch.countDown(); });
	            executor.submit(() -> { findBEVInfeedDetailsByCurrentDate(); latch.countDown(); });
	            executor.submit(() -> { finds230InfeedDetailsByCurrentDate(); latch.countDown(); });
	            executor.submit(() -> { findbevOutfeedDetailsByCurrentDate(); latch.countDown(); });
	            executor.submit(() -> { finds230OutfeedDetailsByCurrentDate(); latch.countDown(); });
	            executor.submit(() -> { findOkCurrentStockByCurrentDate(); latch.countDown(); });
	            executor.submit(() -> { findNokCurrentStockByCurrentDate(); latch.countDown(); });
	            executor.submit(() -> { findTotalOrderDetailsByCurrentDate(); latch.countDown(); });
	            executor.submit(() -> { openOrders(); latch.countDown(); });
	            executor.submit(() -> { findExecutedOrder(); latch.countDown(); });
	            executor.submit(() -> { percentageOrders(); latch.countDown(); });
	            executor.submit(() -> { getNOkBEVMaterialCurrentStockDetails(); latch.countDown(); });
	            executor.submit(() -> { getNOkS230MaterialCurrentStockDetails(); latch.countDown(); });
	            executor.submit(() -> { getOkBEVMaterialCurrentStockDetails(); latch.countDown(); });
	            executor.submit(() -> { getOkS230MaterialCurrentStockDetails(); latch.countDown(); });
	            executor.submit(() -> { findTotalCurrentStockDetails(); latch.countDown(); });
	            executor.submit(() -> { getInfeedDetailsByDate(); latch.countDown(); });
	            executor.submit(() -> { getOutfeedDetailsByDate(); latch.countDown(); });
	            executor.submit(() -> { findBevEmptyPalletCountCurrentStockDetails(); latch.countDown(); });
	            executor.submit(() -> { findS230EmptyPalletCountCurrentStockDetails(); latch.countDown(); });
	            executor.submit(() -> { findS230CurrentStockDetails(); latch.countDown(); });
	            executor.submit(() -> { findBEVCurrentStockDetails(); latch.countDown(); });
	            executor.submit(() -> { findTotalBEVBufferCount(); latch.countDown(); });
	            executor.submit(() -> { findTotalS230BufferCount(); latch.countDown(); });
	            executor.submit(() -> { Area1Count(); latch.countDown(); });
	            executor.submit(() -> { Area2Count(); latch.countDown(); });

	            if (!latch.await(60, TimeUnit.SECONDS)) {
	                logger.warn("Timeout waiting for tasks to finish");
	            }

	            String percentageOrdersResult = dashboardEntity.getPercentageOrders();
	            logger.info("Percentage Orders calculated: {}", percentageOrdersResult);

	            List<DashboardDetailsEntity> todayRecords = dashboardDetailsRepository.findBycDateTimeContaining(currentDate);

	            if (todayRecords.isEmpty()) {
	                // Insert new record for today
	                DashboardDetailsEntity newEntity = new DashboardDetailsEntity(
	                    latestDashboard.getDashboardId() + 1,
	                    totalAlarmCount, bevInfeedCount, s230InfeedCount, bevOutfeedCount, s230OutfeedCount,
	                    currentokMaterialCount, currentNokMaterialCount, totalOrders, executedOrders, remainingOrders,
	                    percentageOrdersResult, totalCurrentStock, totalInfeedCount, totalOutfeedCount,
	                    totalOKBEVCount, totalNOKBEVCount, totalS230OKCount, totalS230NOKCount,
	                    createdDateTime, bevCurrentStockCount, s230CurrentStockCount,
	                    bevEmptyPalletCount, s230EmptyPalletCount, bevBufferCount, s230BufferCount,
	                    area1Count, area2Count
	                );
	                dashboardDetailsRepository.save(newEntity);
	                logger.info("Inserted new dashboard record for date {}", currentDate);

	            } else {
	                // Update existing record if date matches
	                String existingDate = latestDashboard.getCDateTime().substring(0, 10);
	                if (existingDate.equals(currentDate)) {
	                    latestDashboard.setTotalAlarmCount(totalAlarmCount);
	                    latestDashboard.setBevInfeedCount(bevInfeedCount);
	                    latestDashboard.setS230InfeedCount(s230InfeedCount);
	                    latestDashboard.setBevOutfeedCount(bevOutfeedCount);
	                    latestDashboard.setS230OutfeedCount(s230OutfeedCount);
	                    latestDashboard.setCurrentokMaterialCount(currentokMaterialCount);
	                    latestDashboard.setCurrentNokMaterialCount(currentNokMaterialCount);
	                    latestDashboard.setTotalOrders(totalOrders);
	                    latestDashboard.setExecutedOrders(executedOrders);
	                    latestDashboard.setRemainingOrders(remainingOrders);
	                    latestDashboard.setPercentageOrders(percentageOrdersResult);
	                    latestDashboard.setTotalCurrentStockCount(totalCurrentStock);
	                    latestDashboard.setTotalInfeedCount(totalInfeedCount);
	                    latestDashboard.setTotalOutfeedCount(totalOutfeedCount);
	                    latestDashboard.setTotalOKBEVCount(totalOKBEVCount);
	                    latestDashboard.setTotalNOKBEVCount(totalNOKBEVCount);
	                    latestDashboard.setTotalS230OKCount(totalS230OKCount);
	                    latestDashboard.setTotalS230NOKCount(totalS230NOKCount);
	                    latestDashboard.setCDateTime(createdDateTime);
	                    latestDashboard.setBevCurrentStockCount(bevCurrentStockCount);
	                    latestDashboard.setS230CurrentStockCount(s230CurrentStockCount);
	                    latestDashboard.setBevEmptyPalletCount(bevEmptyPalletCount);
	                    latestDashboard.setS230EmptyPalletCount(s230EmptyPalletCount);
	                    latestDashboard.setBevBufferCount(bevBufferCount);
	                    latestDashboard.setS230BufferCount(s230BufferCount);
	                    latestDashboard.setArea1Count(area1Count);
	                    latestDashboard.setArea2Count(area2Count);

	                    dashboardDetailsRepository.save(latestDashboard);
	                    logger.info("Updated existing dashboard record for date {}", currentDate);

	                } else {
	                    // Date changed - insert new record
	                    DashboardDetailsEntity newEntity = new DashboardDetailsEntity(
	                        latestDashboard.getDashboardId() + 1,
	                        totalAlarmCount, bevInfeedCount, s230InfeedCount, bevOutfeedCount, s230OutfeedCount,
	                        currentokMaterialCount, currentNokMaterialCount, totalOrders, executedOrders, remainingOrders,
	                        percentageOrdersResult, totalCurrentStock, totalInfeedCount, totalOutfeedCount,
	                        totalOKBEVCount, totalNOKBEVCount, totalS230OKCount, totalS230NOKCount,
	                        createdDateTime, bevCurrentStockCount, s230CurrentStockCount,
	                        bevEmptyPalletCount, s230EmptyPalletCount, bevBufferCount, s230BufferCount,
	                        area1Count, area2Count
	                    );
	                    dashboardDetailsRepository.save(newEntity);
	                    logger.info("Inserted new dashboard record for a new date {}", currentDate);
	                }
	            }

	        } catch (Exception e) {
	            logger.error("Error in findAllDashboard2: ", e);
	        } finally {
	            if (executor != null) {
	                logger.info("Shutting down executor service...");
	                executor.shutdown();
	                try {
	                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
	                        logger.warn("Executor did not terminate on time, forcing shutdown");
	                        executor.shutdownNow();
	                    } else {
	                        logger.info("Executor terminated successfully");
	                    }
	                } catch (InterruptedException e) {
	                    logger.error("Interrupted while waiting for executor termination", e);
	                    Thread.currentThread().interrupt();
	                    executor.shutdownNow();
	                }
	            }
	        }
	        return latestDashboard;
	    }


	@SuppressWarnings("null")
	public int findEquipmentAlarmHistoryByPreviousDate() {
		try {
			// Get the previous day's date using LocalDate
			LocalDate previousDay = LocalDate.now().minusDays(1);

			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String previousDate = previousDay.format(dateTimeFormatter);

			// Construct the date range for the previous day
			List<EquipmentAlarmHistoryEntity> findByEquipmentAlarmOccurredDatetimeBetween = equipmentAlarmHistoryRepository
					.findByEquipmentAlarmOccurredDatetimeBetween(previousDate + " " + "00:00:00",
							previousDate + " " + "23:59:59");

			totalAlarmCount = findByEquipmentAlarmOccurredDatetimeBetween.size();
			dashboardEntity.setTotalAlarmCount(totalAlarmCount);
			return dashboardEntity.getTotalAlarmCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null; // Return null if an exception occurs
	}

	@SuppressWarnings("null")
	public int findBEVInfeedDetailsByPreviousDate() {
		try {
			// Get the previous day's date using LocalDate
			LocalDate previousDay = LocalDate.now().minusDays(1);

			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String previousDate = previousDay.format(dateTimeFormatter);

			// Use the calculated previous day in the query
			List<InfeedMissionRuntimeDetailsEntity> findBycreatedDatetimeBetweenAndInfeedMissionIsDeletedAndProductNameAndInfeedMissionStatusAndPalletStatusIdNot = infeedMissionRuntimeDetailsRepository
					.findByInfeedMissionEndDateTimeBetweenAndInfeedMissionIsDeletedAndProductNameAndInfeedMissionStatusAndPalletStatusIdNot(
							previousDate + " " + "00:00:00", previousDate + " " + "23:59:59", 0, "BEV", "COMPLETED", 3);

			bevInfeedCount = findBycreatedDatetimeBetweenAndInfeedMissionIsDeletedAndProductNameAndInfeedMissionStatusAndPalletStatusIdNot
					.size();
			dashboardEntity.setBevInfeedCount(bevInfeedCount);
			return dashboardEntity.getBevInfeedCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null; // Return null if an exception occurs
	}

	@SuppressWarnings("null")
	public int findS230InfeedDetailsByPreviousDate() {
		try {
			// Get the previous day's date using LocalDate
			LocalDate previousDay = LocalDate.now().minusDays(1);

			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String previousDate = previousDay.format(dateTimeFormatter);

			// Use the calculated previous day in the query
			List<InfeedMissionRuntimeDetailsEntity> findBycreatedDatetimeBetweenAndInfeedMissionIsDeletedAndProductNameAndInfeedMissionStatusAndPalletStatusIdNot = infeedMissionRuntimeDetailsRepository
					.findByInfeedMissionEndDateTimeBetweenAndInfeedMissionIsDeletedAndProductNameAndInfeedMissionStatusAndPalletStatusIdNot(
							previousDate + " " + "00:00:00", previousDate + " " + "23:59:59", 0, "S230", "COMPLETED",
							3);

			s230InfeedCount = findBycreatedDatetimeBetweenAndInfeedMissionIsDeletedAndProductNameAndInfeedMissionStatusAndPalletStatusIdNot
					.size();
			dashboardEntity.setS230InfeedCount(s230InfeedCount);
			return dashboardEntity.getS230InfeedCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null; // Return null if an exception occurs
	}

	@SuppressWarnings("null")
	public int findbevOutfeedDetailsByPreviousDate() {
		try {
			// Get the previous day's date using LocalDate
			LocalDate previousDay = LocalDate.now().minusDays(1);

			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String previousDate = previousDay.format(dateTimeFormatter);

			// Use the previous day in the query
			List<OutfeedMissionRuntimeDetailsEntity> findBycreatedDatetimeBetweenAndOutfeedMissionIsDeletedAndProductNameAndOutfeedMissionStatusAndPalletStatusIdNot = outfeedMissionRuntimeDetailsRepository
					.findByOutfeedMissionEndDateTimeBetweenAndOutfeedMissionIsDeletedAndProductNameAndOutfeedMissionStatusAndPalletStatusIdNot(
							previousDate + " " + "00:00:00", previousDate + " " + "23:59:59", 0, "BEV", "COMPLETED", 3);

			bevOutfeedCount = findBycreatedDatetimeBetweenAndOutfeedMissionIsDeletedAndProductNameAndOutfeedMissionStatusAndPalletStatusIdNot
					.size();
			dashboardEntity.setBevOutfeedCount(bevOutfeedCount);
			return dashboardEntity.getBevOutfeedCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null; // Return null if an exception occurs
	}

	@SuppressWarnings("null")
	public int finds230OutfeedDetailsByPreviousDate() {
		try {
			// Get the previous day's date using LocalDate
			LocalDate previousDay = LocalDate.now().minusDays(1);

			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String previousDate = previousDay.format(dateTimeFormatter);

			// Use the previous day in the query
			List<OutfeedMissionRuntimeDetailsEntity> findBycreatedDatetimeBetweenAndOutfeedMissionIsDeletedAndProductNameAndOutfeedMissionStatusAndPalletStatusIdNot = outfeedMissionRuntimeDetailsRepository
					.findByOutfeedMissionEndDateTimeBetweenAndOutfeedMissionIsDeletedAndProductNameAndOutfeedMissionStatusAndPalletStatusIdNot(
							previousDate + " " + "00:00:00", previousDate + " " + "23:59:59", 0, "S230", "COMPLETED",
							3);

			s230OutfeedCount = findBycreatedDatetimeBetweenAndOutfeedMissionIsDeletedAndProductNameAndOutfeedMissionStatusAndPalletStatusIdNot
					.size();
			dashboardEntity.setS230OutfeedCount(s230OutfeedCount);
			return dashboardEntity.getS230OutfeedCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null; // Return null if an exception occurs
	}


	
	
	
	@Scheduled(cron = "0 5 0 * * *")
	public DashboardDetailsEntity findAllDashboardByPreviousDate() {
	    List<DashboardDetailsEntity> top2Records = dashboardDetailsRepository.findTop2ByOrderByDashboardIdDesc();
	    ExecutorService executorService = Executors.newFixedThreadPool(10);  // Thread pool with 10 threads
	    try {
	        String secondLastRecordDate = "";
	        int taskCount = 26;
	        CountDownLatch latch = new CountDownLatch(taskCount);

	        // List of tasks to run in parallel
	        Runnable[] tasks = new Runnable[] {
	            () -> { finds230OutfeedDetailsByPreviousDate(); latch.countDown(); },
	            () -> { findbevOutfeedDetailsByPreviousDate(); latch.countDown(); },
	            () -> { findS230InfeedDetailsByPreviousDate(); latch.countDown(); },
	            () -> { findBEVInfeedDetailsByPreviousDate(); latch.countDown(); },
	            () -> { findEquipmentAlarmHistoryByPreviousDate(); latch.countDown(); },
	            () -> { percentageOrders(); latch.countDown(); },
	            () -> { findNokCurrentStockByCurrentDate(); latch.countDown(); },
	            () -> { findTotalOrderDetailsByCurrentDate(); latch.countDown(); },
	            () -> { openOrders(); latch.countDown(); },
	            () -> { findExecutedOrder(); latch.countDown(); },
	            () -> { percentageOrders(); latch.countDown(); },
	            () -> { getNOkBEVMaterialCurrentStockDetails(); latch.countDown(); },
	            () -> { getNOkS230MaterialCurrentStockDetails(); latch.countDown(); },
	            () -> { getOkBEVMaterialCurrentStockDetails(); latch.countDown(); },
	            () -> { getOkS230MaterialCurrentStockDetails(); latch.countDown(); },
	            () -> { findTotalCurrentStockDetails(); latch.countDown(); },
	            () -> { getInfeedDetailsByDate(); latch.countDown(); },
	            () -> { getOutfeedDetailsByDate(); latch.countDown(); },
	            () -> { findBevEmptyPalletCountCurrentStockDetails(); latch.countDown(); },
	            () -> { findS230EmptyPalletCountCurrentStockDetails(); latch.countDown(); },
	            () -> { findS230CurrentStockDetails(); latch.countDown(); },
	            () -> { findBEVCurrentStockDetails(); latch.countDown(); },
	            () -> { findTotalBEVBufferCount(); latch.countDown(); },
	            () -> { findTotalS230BufferCount(); latch.countDown(); },
	            () -> { Area1Count(); latch.countDown(); },
	            () -> { Area2Count(); latch.countDown(); }
	        };

	        // Submit tasks to the executor
	        for (Runnable task : tasks) {
	            executorService.submit(task);
	        }

	        // Wait for all threads to finish
	        latch.await();

	        String percentageOrders2 = dashboardEntity.getPercentageOrders();
	        LocalDate previousDay = LocalDate.now().minusDays(1);
	        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        String previousDate = previousDay.format(dateTimeFormatter);

	        // Formatting current time
	        Date date = new Date();
	        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
	        String previousDateTime = simpleDateFormat1.format(date);

	        if (top2Records != null && top2Records.size() > 1) {
	            // Get second last record's cDateTime
	            secondLastRecordDate = top2Records.get(1).getCDateTime();
	            String str = secondLastRecordDate.substring(0, 10);

	            // Update the second record with new values
	            DashboardDetailsEntity secondRecord = top2Records.get(1);
	            secondRecord.setTotalAlarmCount(dashboardEntity.getTotalAlarmCount());
	            secondRecord.setBevInfeedCount(dashboardEntity.getBevInfeedCount());
	            secondRecord.setS230InfeedCount(dashboardEntity.getS230InfeedCount());
	            secondRecord.setBevOutfeedCount(dashboardEntity.getBevOutfeedCount());
	            secondRecord.setS230OutfeedCount(dashboardEntity.getS230OutfeedCount());
	            secondRecord.setCurrentokMaterialCount(dashboardEntity.getCurrentokMaterialCount());
	            secondRecord.setCurrentNokMaterialCount(dashboardEntity.getCurrentNokMaterialCount());
	            secondRecord.setTotalOrders(dashboardEntity.getTotalOrders());
	            secondRecord.setExecutedOrders(dashboardEntity.getExecutedOrders());
	            secondRecord.setRemainingOrders(dashboardEntity.getRemainingOrders());
	            secondRecord.setPercentageOrders(percentageOrders2);
	            secondRecord.setTotalCurrentStockCount(dashboardEntity.getTotalCurrentStockCount());
	            secondRecord.setTotalInfeedCount(dashboardEntity.getTotalInfeedCount());
	            secondRecord.setTotalOutfeedCount(dashboardEntity.getTotalOutfeedCount());
	            secondRecord.setTotalOKBEVCount(dashboardEntity.getTotalOKBEVCount());
	            secondRecord.setTotalNOKBEVCount(dashboardEntity.getTotalNOKBEVCount());
	            secondRecord.setTotalS230OKCount(dashboardEntity.getTotalS230OKCount());
	            secondRecord.setTotalS230NOKCount(dashboardEntity.getTotalS230NOKCount());
	            secondRecord.setCDateTime(str + " " + previousDateTime);
	            secondRecord.setBevCurrentStockCount(dashboardEntity.getBevCurrentStockCount());
	            secondRecord.setS230CurrentStockCount(dashboardEntity.getS230CurrentStockCount());
	            secondRecord.setBevEmptyPalletCount(dashboardEntity.getBevEmptyPalletCount());
	            secondRecord.setS230EmptyPalletCount(dashboardEntity.getS230EmptyPalletCount());
	            secondRecord.setArea1Count(dashboardEntity.getArea1Count());
	            secondRecord.setArea2Count(dashboardEntity.getArea2Count());
	            secondRecord.setBevBufferCount(dashboardEntity.getBevBufferCount());
	            secondRecord.setS230BufferCount(dashboardEntity.getS230BufferCount());

	            // Save the updated second record
	            dashboardDetailsRepository.save(secondRecord);
	        }

	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // Ensure the executor is shut down properly to avoid memory leaks
	        executorService.shutdown();
	        try {
	            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
	                System.err.println("Executor did not terminate in time.");
	                executorService.shutdownNow();
	            }
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            executorService.shutdownNow();
	        }
	    }
	    return top2Records != null && top2Records.size() > 1 ? top2Records.get(1) : null;
	}

	
	
	@SuppressWarnings("null")
	public int findTotalOrderDetailsByPreviousDate() {
		try {
			LocalDate previousDay = LocalDate.now().minusDays(1);

			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String previousDate = previousDay.format(dateTimeFormatter);
			System.out.println(previousDate);
//			Date date = new Date();
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			String createdDatetime = simpleDateFormat.format(date);

			List<GenerateManualRetrievalOrderEntity> orders = generateManualRetrievalOrderRepository
					.findByCreatedDatetimeBetweenAndIsOrderDeletedAndIsOrderCancelledAndDispatchStatus(
							previousDate + " " + "00:00:00", previousDate + " " + "23:59:59", 0, 0, "COMPLETED");
			List<GenerateManualRetrievalOrderEntity> findByDispatchStatusIn = generateManualRetrievalOrderRepository
					.findByIsOrderDeletedAndDispatchStatusIn(0, Arrays.asList("READY", "IN_PROGRESS"));

			totalOrders = orders.size() + findByDispatchStatusIn.size();

			dashboardEntity.setTotalOrders(totalOrders);

			return dashboardEntity.getTotalOrders();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

}

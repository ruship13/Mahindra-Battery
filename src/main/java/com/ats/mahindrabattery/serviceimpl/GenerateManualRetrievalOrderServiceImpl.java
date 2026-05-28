package com.ats.mahindrabattery.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ats.mahindrabattery.entity.AuditTrailDetailsEntity;
import com.ats.mahindrabattery.entity.BufferDetailsEntity;
import com.ats.mahindrabattery.entity.CurrentPalletStockDetailsEntity;
import com.ats.mahindrabattery.entity.GenerateManualRetrievalOrderEntity;
import com.ats.mahindrabattery.entity.InfeedMissionRuntimeDetailsEntity;
import com.ats.mahindrabattery.entity.MasterProductVariantDetailsEntity;
import com.ats.mahindrabattery.exception.ResourceNotFoundException;
import com.ats.mahindrabattery.repository.AuditTrailDetailsRepository;
import com.ats.mahindrabattery.repository.BufferDetailsRepository;
import com.ats.mahindrabattery.repository.CurrentPalletStockDetailsRepository;
import com.ats.mahindrabattery.repository.GenerateManualRetrievalOrderRepository;
import com.ats.mahindrabattery.response.ResponseHandler;
import com.ats.mahindrabattery.service.GenerateManualRetrievalOrderService;

@Service
public class GenerateManualRetrievalOrderServiceImpl implements GenerateManualRetrievalOrderService {

	@Autowired
	GenerateManualRetrievalOrderRepository generateManualRetrievalOrderRepository;

	@Autowired
	private CurrentPalletStockDetailsRepository currentPalletStockDetailsRepository;

	@Autowired
	private BufferDetailsRepository bufferDetailsRepository;

	@Autowired
	private CurrentPalletStockDetailsServiceImpl CurrentPalletStockDetailsServiceInstance;

	@Autowired
	private AuditTrailDetailsRepository auditTrailDetailsRepository;

//	public ResponseEntity<Object> createMannualRetrivalDetails(
//			GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity) {
//		try {
//
//			Date dNow = new Date();
//			SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
//			String date = ft.format(dNow);
//			generateManualRetrievalOrderEntity.setCreatedDatetime(date);
//			generateManualRetrievalOrderEntity.setAcutualQuantity(0);
//
//			generateManualRetrievalOrderEntity.setIsDispatchStart(0);
//
//			generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//			return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.OK, null);
//
//		} catch (Exception ex) {
//			return ResponseHandler.generateResponse("Error occurred at server side", HttpStatus.INTERNAL_SERVER_ERROR,
//					null);
//
//		}
//
//	}

//	public ResponseEntity<Object> createMannualRetrivalDetails(
//			GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity) {
//		int totalquantity = 0;
//		int totalBufferQuantity = 0;
//		try {
//			String productVariantCode = generateManualRetrievalOrderEntity.getProductVariantCode();
//			List<CurrentPalletStockDetailsEntity> findByproductVariantCode = CurrentPalletStockDetailsServiceInstance
//					.findByproductVariantCode(productVariantCode);
//			for (int i = 0; i < findByproductVariantCode.size(); i++) {
//				int quantity = findByproductVariantCode.get(i).getQuantity();
//				totalquantity = totalquantity + quantity;
//			}
//			//System.out.println("totalquantity::" + totalquantity);
//			if (generateManualRetrievalOrderEntity.getPlannedQuantity() < totalquantity) {
//				Date dNow = new Date();
//				SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
//				String date = ft.format(dNow);
//				generateManualRetrievalOrderEntity.setCreatedDatetime(date);
//				generateManualRetrievalOrderEntity.setAcutualQuantity(0);
//				generateManualRetrievalOrderEntity.setIsDispatchStart(0);
//				generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//				System.out.println("1");
//				return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.OK,
//						generateManualRetrievalOrderEntity);
//			} else {
//				List<BufferDetailsEntity> findByproductVariantCode2 = bufferDetailsRepository
//						.findByproductVariantCodeAndBufferIsDeleted(productVariantCode, 0);
//				for (int j = 0; j < findByproductVariantCode2.size(); j++) {
//					int quantity = findByproductVariantCode2.get(j).getQuantity();
//					totalBufferQuantity = totalBufferQuantity + quantity;
//				}
//				//System.out.println("totalBufferQuantity::" + totalBufferQuantity);
//				if (generateManualRetrievalOrderEntity.getPlannedQuantity() < (totalquantity + totalBufferQuantity)) {
//					Date dNow = new Date();
//					SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
//					String date = ft.format(dNow);
//					generateManualRetrievalOrderEntity.setCreatedDatetime(date);
//					generateManualRetrievalOrderEntity.setAcutualQuantity(0);
//					generateManualRetrievalOrderEntity.setIsDispatchStart(0);
//					generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//					System.out.println("2");
//					return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.ACCEPTED,
//							generateManualRetrievalOrderEntity);
//
//				} else {
//					Date dNow = new Date();
//					SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
//					String date = ft.format(dNow);
//					generateManualRetrievalOrderEntity.setCreatedDatetime(date);
//					generateManualRetrievalOrderEntity.setAcutualQuantity(0);
//					generateManualRetrievalOrderEntity.setIsDispatchStart(0);
//					generateManualRetrievalOrderEntity.setPlannedQuantity(totalquantity + totalBufferQuantity);
//					generateManualRetrievalOrderEntity.setBalanceQuantity(totalquantity + totalBufferQuantity);
//					generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//					System.out.println("Do you want to dispatch"+ (totalquantity + totalBufferQuantity));
//					
//					return ResponseHandler.generateResponse(
//							"Insufficient Quantity available Quantity is :  " + (totalquantity + totalBufferQuantity) + " Do you want to dispatch",
//							HttpStatus.CREATED, generateManualRetrievalOrderEntity);
//				}
//			}
//
//		} catch (Exception ex) {
//			return ResponseHandler.generateResponse("Error occurred at server side", HttpStatus.INTERNAL_SERVER_ERROR,
//					null);
//
//		}
//
//	}

	public ResponseEntity<Object> createMannualRetrivalDetails(
			GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity) {
		
		int totalquantity = 0;
		int totalBufferQuantity = 0;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = authentication.getName();

		
		List <GenerateManualRetrievalOrderEntity> findyByUserName = generateManualRetrievalOrderRepository.findByUserName(name);
		int userId = findyByUserName.get(0).getUserId();
		try {
			String productVariantCode = generateManualRetrievalOrderEntity.getProductVariantCode();
			List<CurrentPalletStockDetailsEntity> findByproductVariantCode = currentPalletStockDetailsRepository
					.findByproductVariantCode(productVariantCode);
			for (int i = 0; i < findByproductVariantCode.size(); i++) {
				int quantity = findByproductVariantCode.get(i).getQuantity();
				totalquantity = totalquantity + quantity;
			}
			List<BufferDetailsEntity> findByproductVariantCode2 = bufferDetailsRepository
					.findByproductVariantCodeAndBufferIsDeleted(productVariantCode, 0);

			for (int j = 0; j < findByproductVariantCode2.size(); j++) {
				int quantity = findByproductVariantCode2.get(j).getQuantity();
				totalBufferQuantity = totalBufferQuantity + quantity;
			}
			// System.out.println("totalquantity::" + totalquantity);
			if (generateManualRetrievalOrderEntity.getPlannedQuantity() < totalquantity) {
				Date dNow = new Date();
				SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
				String date = ft.format(dNow);
				generateManualRetrievalOrderEntity.setCreatedDatetime(date);
				generateManualRetrievalOrderEntity.setAcutualQuantity(0);
				generateManualRetrievalOrderEntity.setIsDispatchStart(0);
				generateManualRetrievalOrderEntity.setIsOrderCancelled(0);
				generateManualRetrievalOrderEntity.setIsOrderDeleted(0);
			generateManualRetrievalOrderEntity.setUserName(name);
			generateManualRetrievalOrderEntity.setUserId(userId);

				generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
				AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
				auditTrailDetailsEntity.setOperatorActions(
						"Retrival order generated of " + generateManualRetrievalOrderEntity.getProductVariantCode()
								+ " and Quantity is  " + generateManualRetrievalOrderEntity.getPlannedQuantity());
				auditTrailDetailsEntity.setField("Dipsatch");
				auditTrailDetailsEntity.setAfterValue(0);
				auditTrailDetailsEntity.setBeforeValue(0);
				auditTrailDetailsEntity.setReason("Bulk Dispatch");
				
				System.out.println(" name :: " + name);
				auditTrailDetailsEntity.setUsername(name);
				auditTrailDetailsEntity.setDatetimeC(date);
				auditTrailDetailsRepository.save(auditTrailDetailsEntity);
				System.out.println("1");

			} else if (generateManualRetrievalOrderEntity
					.getPlannedQuantity() < (totalquantity + totalBufferQuantity)) {
				Date dNow = new Date();
				SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
				String date = ft.format(dNow);
				generateManualRetrievalOrderEntity.setCreatedDatetime(date);
				generateManualRetrievalOrderEntity.setAcutualQuantity(0);
				generateManualRetrievalOrderEntity.setIsDispatchStart(0);
				generateManualRetrievalOrderEntity.setIsOrderCancelled(0);
				generateManualRetrievalOrderEntity.setIsOrderDeleted(0);

				generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
				AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
				auditTrailDetailsEntity.setOperatorActions(
						"Retrival order generated of " + generateManualRetrievalOrderEntity.getProductVariantCode()
								+ "and Quantity is  " + generateManualRetrievalOrderEntity.getPlannedQuantity());
				auditTrailDetailsEntity.setField("Dispatch");
				auditTrailDetailsEntity.setAfterValue(0);
				auditTrailDetailsEntity.setBeforeValue(0);
				auditTrailDetailsEntity.setReason("Bulk Dispatch");
//				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//				String name = authentication.getName();
				System.out.println(" name :: " + name);
				auditTrailDetailsEntity.setUsername(name);
				auditTrailDetailsEntity.setDatetimeC(date);
				auditTrailDetailsRepository.save(auditTrailDetailsEntity);
				System.out.println("2");
				return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.ACCEPTED,
						generateManualRetrievalOrderEntity);
			} else if ((totalquantity + totalBufferQuantity) == 0) {
				System.out.println("totalquantity + totalBufferQuantity :: " + (totalquantity + totalBufferQuantity));

				return ResponseHandler.generateResponse("Mannual dispatch failed due to Quantity available is Zero",
						HttpStatus.INTERNAL_SERVER_ERROR, generateManualRetrievalOrderEntity);
			}

			else {
				System.out.println("***");
				return ResponseHandler
						.generateResponse(
								"Insufficient Quantity available. Quantity is  :    "
										+ (totalquantity + totalBufferQuantity) + ".  Do you want to dispatch?",
								HttpStatus.ALREADY_REPORTED, null);
			}

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Mannual dispatch failed", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
		return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.OK,
				generateManualRetrievalOrderEntity);

	}

	public ResponseEntity<Object> createMannualRetrivalDetails1(
			GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity) {
		int totalquantity = 0;
		int totalBufferQuantity = 0;
		try {
			String productVariantCode = generateManualRetrievalOrderEntity.getProductVariantCode();
			List<CurrentPalletStockDetailsEntity> findByproductVariantCode = currentPalletStockDetailsRepository
					.findByproductVariantCode(productVariantCode);
			for (int i = 0; i < findByproductVariantCode.size(); i++) {
				int quantity = findByproductVariantCode.get(i).getQuantity();
				totalquantity = totalquantity + quantity;
			}
			List<BufferDetailsEntity> findByproductVariantCode2 = bufferDetailsRepository
					.findByproductVariantCodeAndBufferIsDeleted(productVariantCode, 0);

			for (int j = 0; j < findByproductVariantCode2.size(); j++) {
				int quantity = findByproductVariantCode2.get(j).getQuantity();
				totalBufferQuantity = totalBufferQuantity + quantity;
			}
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
			String date = ft.format(dNow);
			generateManualRetrievalOrderEntity.setCreatedDatetime(date);
			generateManualRetrievalOrderEntity.setAcutualQuantity(0);
			generateManualRetrievalOrderEntity.setIsDispatchStart(0);
			generateManualRetrievalOrderEntity.setIsOrderCancelled(0);
			generateManualRetrievalOrderEntity.setIsOrderDeleted(0);
			generateManualRetrievalOrderEntity.setPlannedQuantity(totalquantity + totalBufferQuantity);
			generateManualRetrievalOrderEntity.setBalanceQuantity(totalquantity + totalBufferQuantity);
			generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
			AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
			auditTrailDetailsEntity.setOperatorActions(
					"Retrival order generated of " + generateManualRetrievalOrderEntity.getProductVariantCode()
							+ "and Quantity is  " + generateManualRetrievalOrderEntity.getPlannedQuantity());
			auditTrailDetailsEntity.setField("Dispatch");
			auditTrailDetailsEntity.setAfterValue(0);
			auditTrailDetailsEntity.setBeforeValue(0);
			auditTrailDetailsEntity.setReason("Bulk Dispatch");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String name = authentication.getName();
			System.out.println(" name :: " + name);
			auditTrailDetailsEntity.setUsername(name);
			auditTrailDetailsEntity.setDatetimeC(date);
			auditTrailDetailsRepository.save(auditTrailDetailsEntity);
			System.out.println("Do you want to dispatch" + (totalquantity + totalBufferQuantity));

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Mannual dispatch failed ", HttpStatus.BAD_REQUEST, null);
		}
		return ResponseHandler
				.generateResponse(
						"Insufficient Quantity available. Quantity is  :  " + (totalquantity + totalBufferQuantity)
								+ ".  Do you want to dispatch?",
						HttpStatus.CREATED, generateManualRetrievalOrderEntity);
	}

//	public List<GenerateManualRetrievalOrderEntity> getAllInfeedMissionRuntimeDetails() {
//		try {
//			Date date = new Date();
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			String currentDateTime = dateFormat.format(date);
//		System.out.println("currentDateTime::"+currentDateTime);
//	
//		return generateManualRetrievalOrderRepository.findBycDateTimeBetween(
//			currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59");
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return null;
//
//
//	}
	public List<GenerateManualRetrievalOrderEntity> getAllMannualRetrivalDetails() {
		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentDateTime = dateFormat.format(date);
//			System.out.println("currentDateTime::"+currentDateTime);
//			System.out.println("curretdate::"+infeedMissionRuntimeDetailsRepository.getAllInfeedMissionRuntimeDetailsBetweenDates(
//					currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59"));
//		 List<GenerateManualRetrievalOrderEntity> allGenerateManualRetrievalOrderBetweenDates = generateManualRetrievalOrderRepository.getAllGenerateManualRetrievalOrderBetweenDates(
//					currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59",0);

			List<GenerateManualRetrievalOrderEntity> findBycreatedDatetimeBetweenAndIsOrderDeleted = generateManualRetrievalOrderRepository
					.findBycreatedDatetimeBetweenAndIsOrderDeleted(currentDateTime + " " + "00:00:00",
							currentDateTime + " " + "23:59:59", 0);
			System.out.println(
					"findBycreatedDatetimeBetweenAndIsOrderDeleted::" + findBycreatedDatetimeBetweenAndIsOrderDeleted);
			return findBycreatedDatetimeBetweenAndIsOrderDeleted;
//			if (findBycreatedDatetimeBetween.size() > 0) {
//				List<GenerateManualRetrievalOrderEntity> findByIsOrderDeleted = generateManualRetrievalOrderRepository
//						.findByIsOrderDeleted(0);
//				System.out.println("findByIsOrderDeleted" + findByIsOrderDeleted);
//				return findByIsOrderDeleted;
//			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

//	public List<GenerateManualRetrievalOrderEntity> findByCreatedDateTime(String cDatetime) {
//		try {
//			List<GenerateManualRetrievalOrderEntity> findBycDateTimeInfeedMissionRuntimeDetails = generateManualRetrievalOrderRepository
//					.findByCreatedDatetime(cDatetime);
//			return findBycDateTimeInfeedMissionRuntimeDetails;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return null;
//	}

	public List<GenerateManualRetrievalOrderEntity> findAllMannualDispatchOrdersByDate(String startDate,
			String endDate) {
		try {
			List<GenerateManualRetrievalOrderEntity> findAllInfeedMissionRuntimeDetailsBetweenDates = generateManualRetrievalOrderRepository
					.findGenerateManualRetrievalOrderBetweenDates(startDate, endDate);
			return findAllInfeedMissionRuntimeDetailsBetweenDates;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

//	public List<GenerateManualRetrievalOrderEntity> getByDate() {
//		try {
//			Date date = new Date();
//			String strDateFormat = "yyyy-MM-dd";
//			DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
//			String currentDateTime = dateFormat.format(date);
//			//System.out.println("currentDateTime::"+currentDateTime);
//			return generateManualRetrievalOrderRepository.getAllInfeedMissionRuntimeDetailsBetweenDates(
//					currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59");
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return null;
//	}

	public List<GenerateManualRetrievalOrderEntity> findByMannualDispatchNumber(String dispatchOrderNumber) {
		try {
			List<GenerateManualRetrievalOrderEntity> findByMannualDispatchNumber = generateManualRetrievalOrderRepository
					.findBydispatchOrderNumber(dispatchOrderNumber);
			return findByMannualDispatchNumber;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List<GenerateManualRetrievalOrderEntity> findByAllFilters(String cDatetimeStart, String cDatetimeEnd,
			String dispatchOrderNumber, String productVariantCode, String shiftName) {
		List<String> filterList = new ArrayList<>();
		List<GenerateManualRetrievalOrderEntity> list = new ArrayList<>();

		if (!dispatchOrderNumber.equals("NA")) {
			filterList.add("dispatchOrderNumber");
			System.out.println("dispatchOrderNumber::" + dispatchOrderNumber);
		}
		if (!productVariantCode.equals("NA")) {
			filterList.add("productVariantCode");
			System.out.println("productVariantCode::" + productVariantCode);
		}
		if (!shiftName.equals("NA")) {
			filterList.add("shiftName");
			System.out.println("shiftName::" + shiftName);
		}
		Predicate<GenerateManualRetrievalOrderEntity> dispatchOrderNumberPred = data -> data.getDispatchOrderNumber()
				.equals(dispatchOrderNumber);
		Predicate<GenerateManualRetrievalOrderEntity> productVariantCodePred = data -> data.getProductVariantCode()
				.equals(productVariantCode);
		Predicate<GenerateManualRetrievalOrderEntity> shiftNamePred = data -> data.getShiftName().equals(shiftName);

		if (!(cDatetimeStart.equals("NA")) && !(cDatetimeEnd.equals("NA"))) {
			String startDateTime = cDatetimeStart.toString().replace("T", " ");
			String endDateTime = cDatetimeEnd.toString().replace("T", " ");
			System.out.println("startDateTime= " + startDateTime);
			System.out.println("endDateTime= " + endDateTime);
			list = generateManualRetrievalOrderRepository.findBycreatedDatetimeBetween(startDateTime, endDateTime);
			System.out.println("list1:" + list);

		} else {
			Date dateNow = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(dateNow);
			list = generateManualRetrievalOrderRepository.findBycreatedDatetimeBetween(date + " " + "00:00:00",
					date + " " + "23:59:59");
			// System.out.println("list2:" + list);
		}

		if (filterList.size() != 0) {

			for (int i = 0; i < filterList.size(); i++) {

				if (filterList.get(i).equals("dispatchOrderNumber")) {
					list = list.stream().filter(dispatchOrderNumberPred).collect((Collectors.toList()));
					// System.out.println("floor list::"+list.size());
				} else if (filterList.get(i).equals("productVariantCode")) {
					list = list.stream().filter(productVariantCodePred).collect((Collectors.toList()));
					// System.out.println("area list::"+list.size());
				} else if (filterList.get(i).equals("shiftName")) {
					list = list.stream().filter(shiftNamePred).collect((Collectors.toList()));
					// System.out.println("status list::"+list.size());
				}
			}
		}

		if (filterList.size() == 0 && list.size() == 0) {
			list = null;
		}
		return list;

	}

	public void updateDispatchStart(GenerateManualRetrievalOrderEntity data) {

		GenerateManualRetrievalOrderEntity entityToUpdate = generateManualRetrievalOrderRepository
				.findById(data.getDispatchHistoryId()).orElse(null);

		if (entityToUpdate != null) {

			entityToUpdate.setIsDispatchStart(data.getIsDispatchStart());

			generateManualRetrievalOrderRepository.save(entityToUpdate);
		} else {
			System.out.println("Data not updated");
		}
	}

	public void updateDispatchCanceled(GenerateManualRetrievalOrderEntity data) {

		GenerateManualRetrievalOrderEntity entityToUpdate = generateManualRetrievalOrderRepository
				.findById(data.getDispatchHistoryId())
				.orElseThrow(() -> new ResourceNotFoundException("MasterUserDetailsEntity", "Id",
						data.getDispatchHistoryId()));

//		if (entityToUpdate != null) {

		entityToUpdate.setIsOrderCancelled(data.getIsOrderCancelled());

		generateManualRetrievalOrderRepository.save(entityToUpdate);
//		} else {
//			System.out.println("Data not updated");
//		}
	}

	public List<GenerateManualRetrievalOrderEntity> getAllDispatchOrdernumberDetails() {
		try {
			List<GenerateManualRetrievalOrderEntity> findByIsDispatchStarted = generateManualRetrievalOrderRepository
//					.findAll();
					.findByIsOrderDeleted(0);

			return findByIsDispatchStarted;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public ResponseEntity<Object> createMannualRetrivalDetailsBySerialNumber(
			List<GenerateManualRetrievalOrderEntity> generateManualRetrievalOrderEntity) {
		try {
			for (int i = 0; i < generateManualRetrievalOrderEntity.size(); i++) {
				List<GenerateManualRetrievalOrderEntity> list = generateManualRetrievalOrderRepository
						.findBySerialNumber(generateManualRetrievalOrderEntity.get(i).getSerialNumber());

//			if (list.size() == 0) {
				Date dNow = new Date();
				SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
				String date = ft.format(dNow);
				generateManualRetrievalOrderEntity.get(i).setCreatedDatetime(date);
				generateManualRetrievalOrderEntity.get(i).setAcutualQuantity(0);
//				generateManualRetrievalOrderEntity.setBalanceQuantity(0);
				generateManualRetrievalOrderEntity.get(i).setIsDispatchStart(0);

				generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity.get(i));

				AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
				auditTrailDetailsEntity.setOperatorActions("NA");
				auditTrailDetailsEntity.setField("Retrival");
				auditTrailDetailsEntity.setAfterValue(0);
				auditTrailDetailsEntity.setBeforeValue(0);
				auditTrailDetailsEntity.setReason("mannual Retrival");
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				String name = authentication.getName();
				System.out.println(" name :: " + name);
				auditTrailDetailsEntity.setUsername(name);
				auditTrailDetailsEntity.setDatetimeC(date);
				auditTrailDetailsRepository.save(auditTrailDetailsEntity);

//				return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.OK,
//						generateManualRetrievalOrderEntity.get(i));
//			} else {
//				return ResponseHandler.generateResponse("Mannual dispatch already done already exists",
//						HttpStatus.ALREADY_REPORTED, null);
//			}
			}
			return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.OK,
					generateManualRetrievalOrderEntity);

		} catch (Exception ex) {
			return ResponseHandler.generateResponse("Error occurred at server side", HttpStatus.INTERNAL_SERVER_ERROR,
					null);

		}

	}

	public void updateOrderCancelledStatus(int dispatchHistoryId, int isOrderCancelled) {
		GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity = generateManualRetrievalOrderRepository
				.findById(dispatchHistoryId).orElse(null);
		if (generateManualRetrievalOrderEntity != null) {
			generateManualRetrievalOrderEntity.setIsOrderCancelled(1);
			generateManualRetrievalOrderEntity.setDispatchStatus("Cancelled");
			generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
		}
	}

//	public void updateOrderIsDeleted(int dispatchHistoryId, int isOrderDeleted) {
//		GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity = generateManualRetrievalOrderRepository
//				.findById(dispatchHistoryId).orElse(null);
//		if (generateManualRetrievalOrderEntity != null) {
//			generateManualRetrievalOrderEntity.setIsOrderDeleted(1);
////	    	generateManualRetrievalOrderEntity.setDispatchStatus("Cancelled");
//			generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//		}
//	}
	
	
	public void updateOrderIsDeleted(int dispatchHistoryId) {
	GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity = generateManualRetrievalOrderRepository
			.findById(dispatchHistoryId).orElse(null);
	if (generateManualRetrievalOrderEntity != null) {
		generateManualRetrievalOrderEntity.setIsOrderDeleted(1);
//    	generateManualRetrievalOrderEntity.setDispatchStatus("Cancelled");
		generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
	
	}
}
}

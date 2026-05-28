package com.ats.mahindrabattery.serviceimpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.Cacheable;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ats.mahindrabattery.entity.AuditTrailDetailsEntity;
import com.ats.mahindrabattery.entity.BufferDetailsEntity;
import com.ats.mahindrabattery.entity.CurrentPalletStockDetailsEntity;
import com.ats.mahindrabattery.entity.CurrentStockDetails;
import com.ats.mahindrabattery.entity.DashboardDetailsEntity;
import com.ats.mahindrabattery.entity.GenerateManualRetrievalOrderEntity;
import com.ats.mahindrabattery.entity.InfeedMissionRuntimeDetailsEntity;
import com.ats.mahindrabattery.entity.MasterPalletInformationEntity;
import com.ats.mahindrabattery.entity.MasterPositionDetailsEntity;
import com.ats.mahindrabattery.entity.MasterProductVariantDetailsEntity;
import com.ats.mahindrabattery.entity.MasterUserDetailsEntity;
import com.ats.mahindrabattery.exception.ResourceNotFoundException;
import com.ats.mahindrabattery.repository.AuditTrailDetailsRepository;
import com.ats.mahindrabattery.repository.BufferDetailsRepository;
import com.ats.mahindrabattery.repository.CurrentPalletStockDetailsRepository;
import com.ats.mahindrabattery.repository.GenerateManualRetrievalOrderRepository;
import com.ats.mahindrabattery.repository.MasterPalletInformationRepository;
import com.ats.mahindrabattery.repository.MasterPositionDetailsRepository;
import com.ats.mahindrabattery.repository.MasterProductVariantDetailsRepository;
import com.ats.mahindrabattery.repository.MasterUserDetailsRepository;
import com.ats.mahindrabattery.response.ResponseHandler;
import com.ats.mahindrabattery.service.CurrentPalletStockDetailsService;

@Service
//@CacheConfig(cacheNames = {"allCurrentPalletStockDetailsCache","CurrentPalletStockDetailsCache","loadDatetimeCache"})
//@CacheConfig(cacheNames = {"allCurrentPalletStockDetailsCache"})
public class CurrentPalletStockDetailsServiceImpl implements CurrentPalletStockDetailsService {

	private int bevCurrentStockCount;
	private int s230CurrentStockCount;

	CurrentStockDetails currentStockDetails = new CurrentStockDetails();

	// MasterPositionDetailsEntity masterPositionDetailsEntity=new
	// MasterPositionDetailsEntity();

	@Autowired
	private MasterProductVariantDetailsRepository masterProductVariantDetailsRepository;

	@Autowired
	private CurrentPalletStockDetailsRepository currentPalletStockDetailsRepository;

	@Autowired
	private MasterPalletInformationRepository masterPalletInformationDetailsRepositoryInstance;

	@Autowired
	MasterPalletInformationServiceImpl masterPalletInformationServiceImpl;

	@Autowired
	GenerateManualRetrievalOrderRepository generateManualRetrievalOrderRepository;

	MasterProductVariantDetailsEntity masterProductVariantDetailsEntity = new MasterProductVariantDetailsEntity();

	@Autowired
	private MasterProductVariantDetailsRepository masterProductVariantDetailsRepositoryInstance;

	@Autowired
	private MasterPositionDetailsRepository masterPositionDetailsRepository;

	@Autowired
	private BufferDetailsRepository bufferDetailsRepository;

	@Autowired
	private MasterUserDetailsRepository masterUserDetailsRepository;

	@Autowired
	private AuditTrailDetailsRepository auditTrailDetailsRepository;

	public Page<CurrentPalletStockDetailsEntity> findAll(Pageable pageable) {
		return currentPalletStockDetailsRepository.findAll(pageable);
	}

//	public Page<CurrentPalletStockDetailsEntity> findAllByPalletCodeNotNA(Pageable pageable) {
//		
//		 Page<CurrentPalletStockDetailsEntity> findByPalletCodeNotOrderByAgeingDaysDesc = currentPalletStockDetailsRepository.findByPalletCodeNotOrderByAgeingDaysDesc(pageable, "NA");
//		 System.out.println("findByPalletCodeNotOrderByAgeingDaysDesc :: "+findByPalletCodeNotOrderByAgeingDaysDesc.getContent());
//		 return findByPalletCodeNotOrderByAgeingDaysDesc;
//	}

//	public Page<CurrentPalletStockDetailsEntity> findAllByPalletCodeNotNA(Pageable pageable) {
//		
//		 Page<CurrentPalletStockDetailsEntity> findByPalletCodeNotOrderByAgeingDaysDesc = currentPalletStockDetailsRepository.getByPalletCodeNotOrderByAgeingDaysDesc(pageable, "NA");
//		 System.out.println("findByPalletCodeNotOrderByAgeingDaysDesc :: "+findByPalletCodeNotOrderByAgeingDaysDesc.getContent());
//		 return findByPalletCodeNotOrderByAgeingDaysDesc;
//	}
	public Page<CurrentPalletStockDetailsEntity> findAllByPalletCodeNotNA(Pageable pageable) {
		return currentPalletStockDetailsRepository.getByPalletCodeNotOrderByAgeingDaysDesc(pageable, "NA");
	}

	public CurrentPalletStockDetailsEntity addOrUpdateMasterPalletInformation(
			CurrentPalletStockDetailsEntity currentPalletStockDetailsEntity) {
		try {
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			String currentDate = ft.format(dNow);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String name = authentication.getName();
			System.out.println("authentication:" + name);

			List<MasterProductVariantDetailsEntity> findByproductVariantCode = masterProductVariantDetailsRepositoryInstance
					.findByproductVariantCode(currentPalletStockDetailsEntity.getProductVariantCode());
			if (findByproductVariantCode.size() > 0) {
				int productVariantId = findByproductVariantCode.get(0).getProductVariantId();
				String productVariantname = findByproductVariantCode.get(0).getProductVariantname();
				int productId = findByproductVariantCode.get(0).getProductId();
				String productName = findByproductVariantCode.get(0).getProductName();

				List<CurrentPalletStockDetailsEntity> findByPositionId2 = currentPalletStockDetailsRepository
						.findByPositionId(currentPalletStockDetailsEntity.getPositionId());
				int palletInformationId = findByPositionId2.get(0).getPalletInformationId();

				List<MasterUserDetailsEntity> findByuserName = masterUserDetailsRepository.findByuserName(name);
				int userId = findByuserName.get(0).getUserId();

				// Update fields in CurrentPalletStockDetailsEntity
				currentPalletStockDetailsRepository.updateCurrentPalletInforamtion(
						currentPalletStockDetailsEntity.getPositionId(), palletInformationId,
						currentPalletStockDetailsEntity.getPalletCode(), 0, productId, productName, productVariantId,
						productVariantname, currentPalletStockDetailsEntity.getProductVariantCode(),
						currentPalletStockDetailsEntity.getQuantity(),

						1, "FULL", 0, currentPalletStockDetailsEntity.getBatchNumber(), "NA", "NA", "OK", 1, 0,
						currentDate, userId, name);

				System.out.println("Before updating MasterPositionDetailsEntity..."
						+ currentPalletStockDetailsEntity.getPositionId());

				List<CurrentPalletStockDetailsEntity> findBypalletInformationId = currentPalletStockDetailsRepository
						.findBypalletInformationId(currentPalletStockDetailsEntity.getPalletInformationId());
				int serialNumber = findBypalletInformationId.get(0).getSerialNumber();

				// Update fields in MasterPalletInformationEntity
				masterPalletInformationDetailsRepositoryInstance.updateMasterPalletInformation(palletInformationId,
						currentPalletStockDetailsEntity.getPalletCode(),
						// currentPalletStockDetailsEntity.getWmsTransferMissionOrderId(),
						serialNumber, productId, productName, productVariantId, productVariantname,
						currentPalletStockDetailsEntity.getProductVariantCode(),
						currentPalletStockDetailsEntity.getQuantity(), 1, "FULL", "OK", 1, 0, 0, 0, currentDate, 0);
				// List<CurrentPalletStockDetailsEntity> findBypalletInformationId =
				// currentPalletStockDetailsRepository.findBypalletInformationId(currentPalletStockDetailsEntity.getPalletInformationId());

				System.out.println("Before updating MasterPositionDetailsEntity..."
						+ currentPalletStockDetailsEntity.getPositionId());

				MasterPositionDetailsEntity findByPositionId = masterPositionDetailsRepository
						.findByPositionId(currentPalletStockDetailsEntity.getPositionId());
				findByPositionId.setIsManualDispatch(0);
				findByPositionId.setPositionIsAllocated(1);
				findByPositionId.setEmptyPalletPosition(0);
				findByPositionId.setUserId(userId);
				findByPositionId.setUserName(name);
				MasterPositionDetailsEntity masterPositionDetailsEntity = masterPositionDetailsRepository
						.save(findByPositionId);

				AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
				auditTrailDetailsEntity.setOperatorActions(
						"Data added by " + name + " for position  " + findByPositionId.getPositionName()
								+ " having pallet code " + currentPalletStockDetailsEntity.getPalletCode());
				auditTrailDetailsEntity.setField("Data added");
				auditTrailDetailsEntity.setAfterValue(0);
				auditTrailDetailsEntity.setBeforeValue(0);
				auditTrailDetailsEntity.setReason("Data added");
//				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//				String name = authentication.getName();
				System.out.println(" name :: " + name);
				auditTrailDetailsEntity.setUsername(name);
				auditTrailDetailsEntity.setDatetimeC(currentDate);
				auditTrailDetailsRepository.save(auditTrailDetailsEntity);

				System.out.println("masterPositionDetailsEntity" + masterPositionDetailsEntity);

				// System.out.println("After updating
				// MasterPositionDetailsEntity..."+currentPalletStockDetailsEntity.getPositionId());

				System.out.println("DATA UPDATED SUCCESSFULLY....");

				CurrentPalletStockDetailsEntity findByPalletInformationId2 = currentPalletStockDetailsRepository
						.findByPalletInformationId(findBypalletInformationId.get(0).getPalletInformationId());

				return findByPalletInformationId2;
			} else {
				System.out.println("Entity with PalletInformationId not found.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

//	public CurrentPalletStockDetailsEntity addCurrentStockDetails(
//			CurrentPalletStockDetailsEntity currentPalletStockDetailsEntity) {
//		Date dNow = new Date();
//		SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
//		String currentDate = ft.format(dNow);
//
//		List<CurrentPalletStockDetailsEntity> findByPalletCodeOrderByPalletInformationIdDesc = currentPalletStockDetailsRepository
//				.findByPalletCodeOrderByPalletInformationIdDesc(currentPalletStockDetailsEntity.getPalletCode());
//		int palletInformationId = findByPalletCodeOrderByPalletInformationIdDesc.get(0).getPalletInformationId();
//
//		 List<MasterProductVariantDetailsEntity> findByProductVariantCode = masterProductVariantDetailsRepository.findByProductVariantCode(currentPalletStockDetailsEntity.getProductVariantCode());
//		int productId = findByProductVariantCode.get(0).getProductId();
//		int productVariantId = findByProductVariantCode.get(0).getProductVariantId();
//		String productName = findByProductVariantCode.get(0).getProductName();
//		//String productVariantCode = findbyProductVariantname.getProductVariantCode();
//		String productVariantname = findByProductVariantCode.get(0).getProductVariantname();
//		
//		currentPalletStockDetailsRepository.updateCurrentPalletInforamtion(palletInformationId,
//				currentPalletStockDetailsEntity.getPalletCode(), "NA", productId, productName,productVariantId,
//				productVariantname, currentPalletStockDetailsEntity.getProductVariantCode(),
//				currentPalletStockDetailsEntity.getQuantity(), 0, "NA", 0,
//				currentPalletStockDetailsEntity.getBatchNumber(), "NA",
//				"NA", "NA", 0, 0, currentDate);
//		
////		List<CurrentPalletStockDetailsEntity> findByPositionId = currentPalletStockDetailsRepository.findByPositionId(currentPalletStockDetailsEntity.getPositionId());
////		findByPositionId.get(0).getPositionId();
//		
//		MasterPositionDetailsEntity findByPositionId = masterPositionDetailsRepository.findByPositionId(currentPalletStockDetailsEntity.getPositionId());
//		findByPositionId.setEmptyPalletPosition(0);
//		findByPositionId.setPositionIsAllocated(1);
//		masterPositionDetailsRepository.save(findByPositionId);
//		return currentPalletStockDetailsEntity;
//		
//	}

//	@Cacheable(value="allCurrentPalletStockDetailsCache")

	public List<CurrentPalletStockDetailsEntity> findAllCurrentPalletStockDetails() {
		try {
			List<CurrentPalletStockDetailsEntity> currentPalletStockDetails = currentPalletStockDetailsRepository
					.findAll();

			return currentPalletStockDetails;

		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return null;
	}

//	@Cacheable(key = "#currentPalletStockDetailsId")
	public CurrentPalletStockDetailsEntity findbycurrentPalletStockDetailsId(int currentPalletStockDetailsId) {
		try {
			CurrentPalletStockDetailsEntity findByCurrentPalletStockDetailsId = currentPalletStockDetailsRepository
					.findByCurrentPalletStockDetailsId(currentPalletStockDetailsId);

			return findByCurrentPalletStockDetailsId;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

//	@Cacheable(key = "#quantity")
	public List<CurrentPalletStockDetailsEntity> findByQuantity(int quantity) {
		try {
			List<CurrentPalletStockDetailsEntity> findByQuantity = currentPalletStockDetailsRepository
					.findByQuantity(quantity);

			return findByQuantity;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

//	@Cacheable(key = "#positionName")
	public List<CurrentPalletStockDetailsEntity> findAllByPositionName(String positionName) {
		try {

			return currentPalletStockDetailsRepository.findAllByPositionName(positionName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

//	@Cacheable(key = "#palletCode")
	public List<CurrentPalletStockDetailsEntity> findBypalletCode(String palletCode) {
		try {

			return currentPalletStockDetailsRepository.findByPalletCode(palletCode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// @Cacheable(key = "#productName")
	public List<CurrentPalletStockDetailsEntity> findByproductName(String productName) {
		try {
			// System.out.println("called by findByproductName() from DB");
			List<CurrentPalletStockDetailsEntity> currentPalletStockDetailsByProductName = currentPalletStockDetailsRepository
					.findByproductName(productName);
			return currentPalletStockDetailsByProductName;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List<CurrentPalletStockDetailsEntity> findByproductVariantCode(String productVariantCode) {
		try {
			List<CurrentPalletStockDetailsEntity> currentPalletStockDetailsByProductVariantCode = currentPalletStockDetailsRepository
					.findByproductVariantCode(productVariantCode);
			return currentPalletStockDetailsByProductVariantCode;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

//	 public void deleteByProductVariantCodeAndPalletCode(String productVariantCode,
//				String palletCode) {
//	 System.out.println("productVariantCode :: " + productVariantCode + " " + palletCode);
//	 CurrentPalletStockDetailsEntity findByProductVariantCodeAndPalletCode = currentPalletStockDetailsRepository.findByProductVariantCodeAndPalletCode(productVariantCode,palletCode);
//	  currentPalletStockDetailsRepository.delete(findByProductVariantCodeAndPalletCode);
//	
//}
	public CurrentPalletStockDetailsEntity deleteByProductVariantCodeAndPalletCodeAndCurrentPalletStockDetailsId(
			String productVariantCode, String palletCode, int currentPalletStockDetailsId) {
		// System.out.println("productVariantCode :: " + currentPalletStockDetailsId);
		try {
			return deleteByProductVariantCodeAndPalletCodeAndCurrentPalletStockDetailsId(productVariantCode, palletCode,
					currentPalletStockDetailsId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

//	@Cacheable(key = "#areaName")
	public List<CurrentPalletStockDetailsEntity> findByAreaName(String areaName) {
		try {

			return currentPalletStockDetailsRepository.findByAreaName(areaName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

//	@Cacheable(key = "#positionId")
	public List<CurrentPalletStockDetailsEntity> findByPositionId(int positionId) {
		try {
//			System.out.println("called by findByPositionId() from DB");
			return currentPalletStockDetailsRepository.findByPositionId(positionId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public CurrentPalletStockDetailsEntity updateQuantityAndPalletCodeAndproductVariantCodeByPositionId(
			CurrentPalletStockDetailsEntity currentPalletStockDetailsEntity, int quantity, int positionId,
			String palletCode, String productVariantCode) {
//		System.out.println("productVariantCode :: " + productVariantCode + ":: positionId" + positionId
//				+ " ::  palletCode" + palletCode + " :: quantity" + quantity);
		try {
			CurrentPalletStockDetailsEntity findByQuantityAndPositionIdAndPalletCodeAndProductVariantCode = currentPalletStockDetailsRepository
					.findByQuantityAndPositionIdAndPalletCodeAndProductVariantCode(quantity, positionId, palletCode,
							productVariantCode);
			findByQuantityAndPositionIdAndPalletCodeAndProductVariantCode
					.setQuantity(currentPalletStockDetailsEntity.getQuantity());
			// System.out.println("Quantity:" +
			// currentPalletStockDetailsEntity.getQuantity());
//			findByQuantityAndPositionIdAndPalletCodeAndProductVariantCode
//					.setUom(currentPalletStockDetailsEntity.getUom());
			findByQuantityAndPositionIdAndPalletCodeAndProductVariantCode
					.setPalletCode(currentPalletStockDetailsEntity.getPalletCode());
			findByQuantityAndPositionIdAndPalletCodeAndProductVariantCode
					.setProductVariantCode(currentPalletStockDetailsEntity.getProductVariantCode());
			findByQuantityAndPositionIdAndPalletCodeAndProductVariantCode
					.setPositionId(currentPalletStockDetailsEntity.getPositionId());

			return currentPalletStockDetailsRepository
					.save(findByQuantityAndPositionIdAndPalletCodeAndProductVariantCode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	public CurrentPalletStockDetailsEntity deleteByCurrentPalletStockDetailsId(int currentPalletStockDetailsId) {
		// System.out.println("currentPalletStockDetailsId :: " +
		// currentPalletStockDetailsId);
		try {
			CurrentPalletStockDetailsEntity findByCurrentPalletStockDetailsId = currentPalletStockDetailsRepository
					.findByCurrentPalletStockDetailsId(currentPalletStockDetailsId);
			currentPalletStockDetailsRepository.delete(findByCurrentPalletStockDetailsId);
			return findByCurrentPalletStockDetailsId;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

//	@Cacheable(value = "loadDatetimeCache")
	public List<CurrentPalletStockDetailsEntity> findByLoadDateTime() {
		try {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			String currentDateTime = dateFormat.format(date);
			// System.out.println("Current date= " + currentDateTime);

			return currentPalletStockDetailsRepository.getAllCurrentPalletStockDetailsBetweenDates(
					currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public CurrentPalletStockDetailsEntity updateCurrentPalletStockDetails(int currentPalletStockDetailsId,
			CurrentPalletStockDetailsEntity currentPalletStockDetailsEntity) {
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDate = ft.format(dNow);
		System.out.println("currentDate :: " + currentDate);
		CurrentPalletStockDetailsEntity currentStockDetails = currentPalletStockDetailsRepository
				.findById(currentPalletStockDetailsId)
				.orElseThrow(() -> new ResourceNotFoundException("CurrentPalletStockDetailsEntity", "Id",
						currentPalletStockDetailsId));

		currentStockDetails.setQualityStatus(currentPalletStockDetailsEntity.getQualityStatus());

		System.out.println("Quality approved status=" + currentStockDetails.getQualityStatus());
		
		AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = authentication.getName();
		System.out.println(" name :: " + name);
		if(currentStockDetails.getQualityStatus().equals("NOK")) {
			auditTrailDetailsEntity.setOperatorActions(
					"Quality Status changed from OK to NOK for  " + currentStockDetails.getPositionName()
							+ " having pallet code " + currentStockDetails.getPalletCode() + " by " + name);
			auditTrailDetailsEntity.setField("Quality Status Changed");
//			auditTrailDetailsEntity.setAfterValue(0);
//			auditTrailDetailsEntity.setBeforeValue(0);
			auditTrailDetailsEntity.setReason("Quality Status Changed");

			auditTrailDetailsEntity.setUsername(name);
			auditTrailDetailsEntity.setDatetimeC(currentDate);
			auditTrailDetailsRepository.save(auditTrailDetailsEntity);
		}else{
			auditTrailDetailsEntity.setOperatorActions(
					"Quality Status changed from NOK to OK for  " + currentStockDetails.getPositionName()
							+ " having pallet code " + currentStockDetails.getPalletCode() + " by " + name);
			auditTrailDetailsEntity.setField("Quality Status Changed");
//			auditTrailDetailsEntity.setAfterValue(0);
//			auditTrailDetailsEntity.setBeforeValue(0);
			auditTrailDetailsEntity.setReason("Quality Status Changed");

			auditTrailDetailsEntity.setUsername(name);
			auditTrailDetailsEntity.setDatetimeC(currentDate);
			auditTrailDetailsRepository.save(auditTrailDetailsEntity);
		}
	
		currentPalletStockDetailsRepository.save(currentStockDetails);
		return currentStockDetails;
	}

	public List<CurrentPalletStockDetailsEntity> findByAllFilters(String startDate, String endDate,
			String productVariantCode, String floorName, String areaName, String productName) {
		List<String> filterList = new ArrayList<>();
		List<CurrentPalletStockDetailsEntity> list = new ArrayList<>();

		if (!productVariantCode.equals("NA")) {
			filterList.add("productVariantCode");
			// System.out.println("productVariantCode= " + productVariantCode);
		}
		if (!areaName.equals("NA")) {
			filterList.add("areaName");
			// System.out.println("Area= " + areaName);
		}
		if (!floorName.equals("NA")) {
			filterList.add("floorName");
			// System.out.println("Floor= " + floorName);
		}
		if (!productName.equals("NA")) {
			filterList.add("productName");
			// System.out.println("productVariantName= " + productVariantName);
		}

		Predicate<CurrentPalletStockDetailsEntity> predProductVariantCode = data -> (data.getProductVariantCode()
				.equals(productVariantCode));
		// System.out.println("predicate product variant code=" + productVariantCode);
		Predicate<CurrentPalletStockDetailsEntity> predArea = data -> (data.getAreaName().equals(areaName));
		// System.out.println("predicate area=" + predArea);
		Predicate<CurrentPalletStockDetailsEntity> predFloor = data -> (data.getFloorName().equals(floorName));
		// System.out.println("Predicate floor=" + floorName);
		Predicate<CurrentPalletStockDetailsEntity> predProductName = data -> (data.getProductName()
				.equals(productName));
		// System.out.println("Predicate productVariantName=" + productVariantName);

		if (!(startDate.equals("NA")) && !(endDate.equals("NA"))) {
			String startDateTime = startDate.toString().replace("T", " ");
			String endDateTime = endDate.toString().replace("T", " ");
//			System.out.println("startDateTime " + startDateTime);
//			System.out.println("endDateTime " + endDateTime);
			list = currentPalletStockDetailsRepository.findByLoadDatetimeBetween(startDateTime, endDateTime);
			System.out.println("List length=" + list.size());
		} else {
			Date dateNow = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
			String date = dateFormat.format(dateNow);
//			System.out.println("Date= " + date);
//			System.out.println("datelist ::"+list);
			list = currentPalletStockDetailsRepository.findByLoadDatetimeBetween(date + " " + "00:00:00",
					date + " " + "23:59:59");
		}
//		System.out.println("List length 2=" + list.size());
//		System.out.println("filterList.size() =" + filterList.size());
		if (filterList.size() != 0) {
			// System.out.println("filter list size=" + filterList.size());
			for (int i = 0; i < filterList.size(); i++) {
				// System.out.println("filterlist content: " + filterList.get(i));
				if (filterList.get(i).equals("productVariantCode")) {
					list = list.stream().filter(predProductVariantCode).collect((Collectors.toList()));
					// System.out.println("productVariantCode list size= " + list.size());
				} else if (filterList.get(i).equals("areaName")) {
					list = list.stream().filter(predArea).collect((Collectors.toList()));
					// System.out.println("area list size= " + list.size());
				} else if (filterList.get(i).equals("floorName")) {
					list = list.stream().filter(predFloor).collect((Collectors.toList()));
					// System.out.println("floor list size= " + list.size());
				} else if (filterList.get(i).equals("productName")) {
					list = list.stream().filter(predProductName).collect((Collectors.toList()));
					// System.out.println("product variant name list size= " + list.size());
				}

			}
		}
//		String startDateTime = startDate.toString().replace("T", " ");
//		String endDateTime = endDate.toString().replace("T", " ");
//		System.out.println("startDateTime " + startDateTime);
//		System.out.println("endDateTime " + endDateTime);
//		list = currentPalletStockDetailsRepository.findByLoadDateTimeBetween(startDateTime,endDateTime);
//		System.out.println("list list size= " + list.size());
		if (filterList.size() == 0 || list.size() == 0) {
			String startDateTime = startDate.toString().replace("T", " ");
			String endDateTime = endDate.toString().replace("T", " ");
//			System.out.println("startDateTime " + startDateTime);
//			System.out.println("endDateTime " + endDateTime);
			list = currentPalletStockDetailsRepository.findByLoadDatetimeBetween(startDateTime, endDateTime);
//			list = currentPalletStockDetailsRepository.findByLoadDateTimeBetween(startDateTime,endDateTime);
		}
		// System.out.println("List length end=" + list.size());
		return list;

	}

	public List<CurrentPalletStockDetailsEntity> findbyloaddatetime() {
		try {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			String currentDateTime = dateFormat.format(date);
			// System.out.println("Current date= " + currentDateTime);
			return currentPalletStockDetailsRepository.getAllCurrentPalletStockDetailsBetweenDates(
					currentDateTime + " " + "00:00:00", currentDateTime + " " + "23:59:59");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public ResponseEntity<Object> addCurrentPalletStockDetails(
			CurrentPalletStockDetailsEntity currentPalletStockDetailsEntity) {
		List<MasterProductVariantDetailsEntity> productVirantlist = null;
		List<MasterPalletInformationEntity> palletInformationList = null;
		try {
			palletInformationList = masterPalletInformationDetailsRepositoryInstance
					.findByPalletCode(currentPalletStockDetailsEntity.getPalletCode());

//			System.out.println(
//					"addCurrentPalletStockDetails :: pallet info data available :: " + palletInformationList.size());
			currentPalletStockDetailsEntity
					.setPalletInformationId(palletInformationList.get(0).getPalletInformationId());

			List<CurrentPalletStockDetailsEntity> currentpalletList = currentPalletStockDetailsRepository
					.findByPalletCodeAndProductVariantCode(currentPalletStockDetailsEntity.getPalletCode(), "NA");

			if (currentpalletList.size() > 0) {
				currentPalletStockDetailsEntity
						.setCurrentPalletStockDetailsId(currentpalletList.get(0).getCurrentPalletStockDetailsId());
			}

			productVirantlist = masterProductVariantDetailsRepositoryInstance
					.findByProductVariantCode(currentPalletStockDetailsEntity.getProductVariantCode());

//			System.out.println(
//					"addCurrentPalletStockDetails :: product varient data available :: " + productVirantlist.size());

			if (productVirantlist.size() > 0) {
				currentPalletStockDetailsEntity.setProductVariantName(productVirantlist.get(0).getProductVariantname());
				currentPalletStockDetailsEntity.setProductVariantId(productVirantlist.get(0).getProductVariantId());
				currentPalletStockDetailsEntity.setProductId(productVirantlist.get(0).getProductId());
				currentPalletStockDetailsEntity.setProductName(productVirantlist.get(0).getProductName());

				currentPalletStockDetailsEntity.setAreaName("NA");
				currentPalletStockDetailsEntity.setFloorName("NA");
				currentPalletStockDetailsEntity.setRackName("NA");
				currentPalletStockDetailsEntity.setRackSide("NA");
				currentPalletStockDetailsEntity.setPositionName("NA");

				Date dNow = new Date();
				SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
				String date = ft.format(dNow);
				currentPalletStockDetailsEntity.setLoadDatetime(date);
				if (currentpalletList.size() > 0) {
					currentPalletStockDetailsRepository.updateNApalletData(
							currentPalletStockDetailsEntity.getProductVariantId(),
							currentPalletStockDetailsEntity.getProductVariantCode(),
							currentPalletStockDetailsEntity.getProductVariantName(),
							currentPalletStockDetailsEntity.getQuantity(),
							currentPalletStockDetailsEntity.getLoadDatetime(),
							currentPalletStockDetailsEntity.getUserId(), currentPalletStockDetailsEntity.getUserName(),
							currentPalletStockDetailsEntity.getCurrentPalletStockDetailsId());
//					List<MasterPositionDetailsEntity> positionInstance = masterPositionDetailsRepository
//							.findByPositionName(currentPalletStockDetailsEntity.getPositionName());
//					// updating isMaterial loaded to 1 when there is material on the pallet or
//					// position
//					System.out.println("update pallet::");
//					masterPositionDetailsRepository.updateisMaterialLoadedBypositionId(1,
//							positionInstance.get(0).getPositionId());

				} else {
					// System.out.println("save
					// data::"+currentPalletStockDetailsRepository.save(currentPalletStockDetailsEntity));
					currentPalletStockDetailsRepository.save(currentPalletStockDetailsEntity);

//					List<MasterPositionDetailsEntity> positionInstance = masterPositionDetailsRepository
//							.findByPositionName(currentPalletStockDetailsEntity.getPositionName());
					// updating isMaterial loaded to 1 when there is material on the pallet or
					// position
//					masterPositionDetailsRepository.updateisMaterialLoadedBypositionId(1,
//							positionInstance.get(0).getPositionId());
					return ResponseHandler.generateResponse("Material loaded sucessfully", HttpStatus.OK, null);
				}
			} else {
				return ResponseHandler.generateResponse("Material is not available in master material details",
						HttpStatus.BAD_REQUEST, null);
			}
		} catch (Exception ex) {
			// System.out.println("error "+ex.getMessage());
			return ResponseHandler.generateResponse("Error occurred at server side", HttpStatus.INTERNAL_SERVER_ERROR,
					null);
		}
		return null;

	}

	public ResponseEntity<List<CurrentPalletStockDetailsEntity>> unloadingOperationDetails(String palletCode,
			String prodVariantCode, int quantity, String binLocation, int userId, String userName) {
//		System.out.println("CurrentPalletStockDetailsController :: unloadingOperationDetails " + palletCode + " "
//				+ prodVariantCode + "" + qty);
		List<CurrentPalletStockDetailsEntity> palletProdVariantDetailsList = null;
		// System.out.println("quantity" + qty);
		if (quantity <= 0) {
			return new ResponseEntity<List<CurrentPalletStockDetailsEntity>>(palletProdVariantDetailsList,
					HttpStatus.BAD_REQUEST);
		}
		try {
			palletProdVariantDetailsList = currentPalletStockDetailsRepository
					.findByPalletCodeAndProductVariantCode(palletCode, prodVariantCode);
			if (palletProdVariantDetailsList.size() > 0) {
				int totalQtyOnPallet = 0;
				int remainingQty = quantity;
				for (int i = 0; i < palletProdVariantDetailsList.size(); i++) {
					totalQtyOnPallet += palletProdVariantDetailsList.get(i).getQuantity();
				}
				// System.out.println("totalQtyOnPallet :: " + totalQtyOnPallet);
				if (quantity > totalQtyOnPallet) {
					return new ResponseEntity<List<CurrentPalletStockDetailsEntity>>(palletProdVariantDetailsList,
							HttpStatus.ALREADY_REPORTED);
				} else if (quantity <= totalQtyOnPallet) {

					for (int i = 0; i < palletProdVariantDetailsList.size(); i++) {

						if (remainingQty - palletProdVariantDetailsList.get(i).getQuantity() >= 0) {
							// System.out.println("remainingQuantity ::" + remainingQty);
							remainingQty = remainingQty - palletProdVariantDetailsList.get(i).getQuantity();
							currentPalletStockDetailsRepository.deleteByCurrentPalletStockDetailsId(
									palletProdVariantDetailsList.get(i).getCurrentPalletStockDetailsId());
//							System.out.println("FIRST IF  delete this CURRENT_PALLET_STOCK_DETAILS_ID :: "
//									+ palletProdVariantDetailsList.get(i).getCurrentPalletStockDetailsId());
							totalQtyOnPallet = totalQtyOnPallet - palletProdVariantDetailsList.get(i).getQuantity();
//							System.out.println("FIRST IF  orderRemainingQty after operation ::" + remainingQty);
//							System.out.println("FIRST IF  totalQtyOnPallet after operation ::" + totalQtyOnPallet);
						} else if (remainingQty - palletProdVariantDetailsList.get(i).getQuantity() <= 0) {
							totalQtyOnPallet = totalQtyOnPallet - remainingQty;
							if (palletProdVariantDetailsList.get(i).getQuantity() - remainingQty >= 0) {
								remainingQty = palletProdVariantDetailsList.get(i).getQuantity() - remainingQty;
								currentPalletStockDetailsRepository.updateQuantityByCurrentPalletStockDetailsId(
										palletProdVariantDetailsList.get(i).getCurrentPalletStockDetailsId(),
										remainingQty, userId, userName);
//								System.out.println("SECOND IF  update CURRENT_PALLET_STOCK_DETAILS_ID :: "
//										+ palletProdVariantDetailsList.get(i).getCurrentPalletStockDetailsId()
//										+ " orderRemainingQty:: " + remainingQty);

								// System.out.println("SECOND IF break");
								break;

							} else {

								remainingQty = remainingQty - palletProdVariantDetailsList.get(i).getQuantity();
								currentPalletStockDetailsRepository.deleteByCurrentPalletStockDetailsId(
										palletProdVariantDetailsList.get(i).getCurrentPalletStockDetailsId());
								// System.out.println("SECOND else delete this CURRENT_PALLET_STOCK_DETAILS_ID
								// :: "
								// + palletProdVariantDetailsList.get(i).getCurrentPalletStockDetailsId());

//								System.out.println("SECOND else  orderRemainingQty after operation ::" + remainingQty);
//								System.out
//										.println("SECOND else  totalQtyOnPallet after operation ::" + totalQtyOnPallet);
								if (remainingQty <= 0) {
									// System.out.println("SECOND else break");
									break;
								}
							}

						} else if (remainingQty <= totalQtyOnPallet) {
							remainingQty = palletProdVariantDetailsList.get(i).getQuantity() - remainingQty;
							totalQtyOnPallet = totalQtyOnPallet - palletProdVariantDetailsList.get(i).getQuantity();
//							System.out.println("ELSE IF orderRemainingQty after operation ::" + remainingQty);
//							System.out.println("ELSE IF totalQtyOnPallet after operation ::" + totalQtyOnPallet);
							currentPalletStockDetailsRepository.updateQuantityByCurrentPalletStockDetailsId(
									palletProdVariantDetailsList.get(i).getCurrentPalletStockDetailsId(), remainingQty,
									userId, userName);

//							System.out.println(
//									"ELSE IF update totalQtyOnPallet  against CURRENT_PALLET_STOCK_DETAILS_ID :: "
//											+ totalQtyOnPallet + " :: "
//											+ palletProdVariantDetailsList.get(i).getCurrentPalletStockDetailsId());
						}
						if (remainingQty <= 0) {
							// System.out.println("ELSE IF break");
							break;
						}

					}
					// System.out.println(" OUTSIDE orderRemainingQty :: " + remainingQty);

				}

			}

			List<CurrentPalletStockDetailsEntity> list = currentPalletStockDetailsRepository
					.findByPalletCode(palletCode);
			// System.out.println("bin location on submit button: " + binLocation);
			if (list.size() == 0) {
				// System.out.println("Empty pallet data added on unload button");
				List<MasterPalletInformationEntity> palletInfoList = masterPalletInformationDetailsRepositoryInstance
						.findByPalletCode(palletCode);
				List<MasterPositionDetailsEntity> positionInstance = masterPositionDetailsRepository
						.findByPositionName(binLocation);

				Date dNow = new Date();
				SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
				String date = ft.format(dNow);
				CurrentPalletStockDetailsEntity instance = new CurrentPalletStockDetailsEntity();
//				CurrentPalletStockDetailsEntity instance = new CurrentPalletStockDetailsEntity(0,
//						palletInfoList.get(0).getPalletInformationId(), palletCode, 0, "NA", "NA", binLocation, date, 0,
//						date, 0, date, date, date, 0, date, 0, userId, 0, userId, "NA", date,
//						positionInstance.get(0).getPositionId(), null, userId, date, userId, date, date, userId, userId, date);
				currentPalletStockDetailsRepository.save(instance);
//		CurrentPalletStockDetailsEntity instance = new CurrentPalletStockDetailsEntity(0, palletInfoList.get(0).getPalletInformationId(), palletCode, 0, "NA", "NA", binLocation, date, 0, date, 0, prodVariantCode, date, date, qty, date, qty, userId, palletCode, userId, date, date, userId, prodVariantCode, userId, userName, userId, binLocation, date, qty, userId, userName);
//				currentPalletStockDetailsRepository.save(instance);
				// updating isMaterial loaded to 0 when there is no material on the pallet or
				// position
				// masterPositionDetailsRepository.updateisMaterialLoadedBypositionId(0,
				// positionInstance.get(0).getPositionId());

			} else {
//				List<MasterPositionDetailsEntity> positionInstance = masterPositionDetailsRepository
//						.findByPositionName(binLocation);
				// updating isMaterial loaded to 1 when there is material on the pallet or
				// position
				// masterPositionDetailsRepository.updateisMaterialLoadedBypositionId(1,
				// positionInstance.get(0).getPositionId());

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public List<CurrentPalletStockDetailsEntity> findByPalletCode(String palletCode) {

		return null;
	}

	public void updateCurrentStockDetails(CurrentPalletStockDetailsEntity currentPalletStockDetailsEntity) {

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
		String date = ft.format(dNow);
		AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = authentication.getName();
		auditTrailDetailsEntity
				.setOperatorActions("Position updated " + currentPalletStockDetailsEntity.getPositionName() + " by "
						+ name + " having pallet code " + currentPalletStockDetailsEntity.getPalletCode());
		auditTrailDetailsEntity.setField("Data updated from 2D Layout");
//		auditTrailDetailsEntity.setAfterValue(0);
//		auditTrailDetailsEntity.setBeforeValue(0);
		auditTrailDetailsEntity.setReason("Updation");
//		System.out.println(" name :: " + name);
		auditTrailDetailsEntity.setUsername(name);
		auditTrailDetailsEntity.setDatetimeC(date);
		auditTrailDetailsRepository.save(auditTrailDetailsEntity);
		currentPalletStockDetailsRepository.save(currentPalletStockDetailsEntity);
	}

	public ResponseEntity<Object> deletCurrentStockDetailsByPalletCode(int positionId) {
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
		String date = ft.format(dNow);
		CurrentPalletStockDetailsEntity currentStockDetailsEntityInsrt = new CurrentPalletStockDetailsEntity();
		List<CurrentPalletStockDetailsEntity> findByPositionId = currentPalletStockDetailsRepository
				.findByPositionId(positionId);
		if (findByPositionId != null) {
			for (int i = 0; i < findByPositionId.size(); i++) {
				// findByPositionId.get(i).setCurrentPalletStockDetailsId(0);
				findByPositionId.get(i).setPalletCode("NA");
				findByPositionId.get(i).setPalletInformationId(0);
				findByPositionId.get(i).setSerialNumber(0);
				findByPositionId.get(i).setProductVariantId(0);
				findByPositionId.get(i).setProductVariantCode("NA");
				findByPositionId.get(i).setProductVariantName("NA");
				findByPositionId.get(i).setProductId(0);
				findByPositionId.get(i).setProductName("NA");
				findByPositionId.get(i).setPalletStatusId(0);
				findByPositionId.get(i).setPalletStatusname("EMPTY");
				findByPositionId.get(i).setAgeingDays(0);
				findByPositionId.get(i).setQuantity(0);
				findByPositionId.get(i).setQualityStatus("NA");
				findByPositionId.get(i).setBatchNumber("NA");
				findByPositionId.get(i).setModelNumber("NA");
				findByPositionId.get(i).setLocation("NA");
				// findByPositionId.get(i).setPositionName("NA");
				findByPositionId.get(i).setLoadDatetime(date);
				findByPositionId.get(i).setExpiryDate("9999-12-31 00:00:00.0000000");
				findByPositionId.get(i).setUserId(0);
				findByPositionId.get(i).setUserName("NA");
				currentPalletStockDetailsRepository.save(findByPositionId.get(i));
			}
			// return new
			// ResponseEntity<List<CurrentPalletStockDetailsEntity>>(findByPositionId,HttpStatus.OK);
			return ResponseHandler.generateResponse("Material deleted sucessfully", HttpStatus.OK, null);

		}
		return ResponseHandler.generateResponse("error occured", HttpStatus.BAD_REQUEST, null);
	}

	@SuppressWarnings("null")
	public int findBEVCurrentStockDetails() {
		try {

			List<CurrentPalletStockDetailsEntity> findByProductName = currentPalletStockDetailsRepository
					.findByProductName("BEV");
			bevCurrentStockCount = (int) findByProductName.stream().filter(e -> e.getPalletStatusId() != 3).count();
			currentStockDetails.setBevCurrentStockCount(bevCurrentStockCount);
			return currentStockDetails.getBevCurrentStockCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	@SuppressWarnings("null")
	public int findS230CurrentStockDetails() {
		try {

			List<CurrentPalletStockDetailsEntity> findByProductName = currentPalletStockDetailsRepository
					.findByProductName("S230");
			s230CurrentStockCount = (int) findByProductName.stream().filter(e -> e.getPalletStatusId() != 3).count();
			currentStockDetails.setS230CurrentStockCount(s230CurrentStockCount);
			return currentStockDetails.getS230CurrentStockCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

//	@Override
//	public List<CurrentPalletStockDetailsEntity> dispatchNokCurrentStock(String dispatchOrderNumber,
//			String startSerialNumber, String endSerialNumber) {
//		List<CurrentPalletStockDetailsEntity> bySerialNumberAndQualityStatus = currentPalletStockDetailsRepository
//				.getBySerialNumberAndQualityStatus(startSerialNumber, endSerialNumber);
//		List<CurrentPalletStockDetailsEntity> collect = bySerialNumberAndQualityStatus.stream()
//				.filter(e -> e.getQualityStatus().equalsIgnoreCase("NOK")).collect(Collectors.toList());
//		System.out.println(collect);
//		return collect;
//	}

//	@Override
//	public List<CurrentPalletStockDetailsEntity> findByBEV(Pageable pageable) {
//		List<CurrentPalletStockDetailsEntity> findByProductVariantNameAndPalletStatusIdNot = currentPalletStockDetailsRepository
//				.findByProductNameAndPalletStatusIdNotOrderByAgeingDaysDesc(pageable,"BEV", 3);
//		return findByProductVariantNameAndPalletStatusIdNot;
//	}

	@Override
	public Page<CurrentPalletStockDetailsEntity> findByBEV(Pageable pageable) {
		return currentPalletStockDetailsRepository.findByProductNameAndPalletStatusIdNotOrderByAgeingDaysDesc(pageable,
				"BEV", 3);
	}

	@Override
	public Page<CurrentPalletStockDetailsEntity> findByS230(Pageable pageable) {
		return currentPalletStockDetailsRepository.findByProductNameAndPalletStatusIdNotOrderByAgeingDaysDesc(pageable,
				"S230", 3);
	}

	@Override
	public List<CurrentPalletStockDetailsEntity> findByOkAndBev(Pageable pageable) {
		List<CurrentPalletStockDetailsEntity> findByProductNameAndQualityStatusAndPalletStatusIdNot = currentPalletStockDetailsRepository
				.findByProductNameAndQualityStatusAndPalletStatusIdNot(pageable, "BEV", "OK", 3);
		return findByProductNameAndQualityStatusAndPalletStatusIdNot;
	}

	@Override
	public List<CurrentPalletStockDetailsEntity> findByOkAndS230(Pageable pageable) {
		List<CurrentPalletStockDetailsEntity> findByProductNameAndQualityStatusAndPalletStatusIdNot = currentPalletStockDetailsRepository
				.findByProductNameAndQualityStatusAndPalletStatusIdNot(pageable, "S230", "OK", 3);
		return findByProductNameAndQualityStatusAndPalletStatusIdNot;
	}

	@Override
	public List<CurrentPalletStockDetailsEntity> findByNOkAndBEV(Pageable pageable) {
		List<CurrentPalletStockDetailsEntity> findByProductNameAndQualityStatusAndPalletStatusIdNot = currentPalletStockDetailsRepository
				.findByProductNameAndQualityStatusAndPalletStatusIdNot(pageable, "BEV", "NOK", 3);
		return findByProductNameAndQualityStatusAndPalletStatusIdNot;
	}

	@Override
	public List<CurrentPalletStockDetailsEntity> findByNOkAndS230(Pageable pageable) {
		List<CurrentPalletStockDetailsEntity> findByProductNameAndQualityStatusAndPalletStatusIdNot = currentPalletStockDetailsRepository
				.findByProductNameAndQualityStatusAndPalletStatusIdNot(pageable, "S230", "NOK", 3);
		return findByProductNameAndQualityStatusAndPalletStatusIdNot;
	}

	public Page<CurrentPalletStockDetailsEntity> findByQualityStatus(String qualityStatus, Pageable pageable) {
		Page<CurrentPalletStockDetailsEntity> findByQualityStatus = currentPalletStockDetailsRepository
				.findByQualityStatusAndProductNameOrderByAgeingDaysDesc(qualityStatus, "BEV", pageable);
		return findByQualityStatus;
	}

	public Page<CurrentPalletStockDetailsEntity> findByQualityStatusS230(String qualityStatus, Pageable pageable) {
		Page<CurrentPalletStockDetailsEntity> findByQualityStatus = currentPalletStockDetailsRepository
				.findByQualityStatusAndProductNameOrderByAgeingDaysDesc(qualityStatus, "S230", pageable);
		return findByQualityStatus;
	}

	// @Override
//	public List<CurrentPalletStockDetailsEntity> findByserialNumberBetween2(int serialNumber1, int serialNumber2,
//			String dispatchOrderNumber, int shiftId, String shiftName) {
//		List<CurrentPalletStockDetailsEntity> findByserialNumberBetween = currentPalletStockDetailsRepository
//				.findByserialNumberBetween(serialNumber1, serialNumber2);
//
////				List<GenerateManualRetrievalOrderEntity> list = generateManualRetrievalOrderRepository
////						.findBySerialNumber(generateManualRetrievalOrderEntity.get(i).getSerialNumber());
//		GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity = new GenerateManualRetrievalOrderEntity();
////			if (list.size() == 0) {
//		Date dNow = new Date();
//		SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
//		String date = ft.format(dNow);
//		generateManualRetrievalOrderEntity.setCreatedDatetime(date);
//		generateManualRetrievalOrderEntity.setAcutualQuantity(0);
////				generateManualRetrievalOrderEntity.setBalanceQuantity(0);
//		generateManualRetrievalOrderEntity.setIsDispatchStart(0);
//		generateManualRetrievalOrderEntity.setProductVariantCode("NA");
//		generateManualRetrievalOrderEntity.setProductId(0);
//		generateManualRetrievalOrderEntity.setProductName("NA");
//		generateManualRetrievalOrderEntity.setProductVariantId(0);
//		generateManualRetrievalOrderEntity.setProductVariantName("NA");
//		generateManualRetrievalOrderEntity.setSerialNumber(0);
//		generateManualRetrievalOrderEntity.setShiftId(shiftId);
//		generateManualRetrievalOrderEntity.setShiftName(shiftName);
//		generateManualRetrievalOrderEntity.setDispatchOrderNumber(dispatchOrderNumber);
//		generateManualRetrievalOrderEntity.setDispatchStatus("READY");
//		generateManualRetrievalOrderEntity.setPlannedQuantity(findByserialNumberBetween.size());
//		generateManualRetrievalOrderEntity.setBalanceQuantity(findByserialNumberBetween.size());
//		generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//		System.out.println("generateManualRetrievalOrderEntity::" + generateManualRetrievalOrderEntity);
//
//		List<GenerateManualRetrievalOrderEntity> findAll = generateManualRetrievalOrderRepository
//				.findByserialNumberBetween(serialNumber1, serialNumber2);
//		System.out.println("findAll::" + findAll);
//		return findByserialNumberBetween;
//
//	}

//	@Override
//	public ResponseEntity<Object> findByserialNumberBetween(int serialNumber1, int serialNumber2,
//			String dispatchOrderNumber, int shiftId, String shiftName) {
//		GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity = new GenerateManualRetrievalOrderEntity();
//		try {
//
//			List<CurrentPalletStockDetailsEntity> findByserialNumberBetween = currentPalletStockDetailsRepository
//					.findByserialNumberBetween(serialNumber1, serialNumber2);
////			int plannedQuantity = findByserialNumberBetween.size();
//			int totalquantity = 0;
//			int totalBufferQuantity = 0;
//			int plannedQuantity=serialNumber2-serialNumber1+1;
//			for (int i = 0; i < findByserialNumberBetween.size(); i++) {
//				int quantity = findByserialNumberBetween.get(i).getQuantity();
//				totalquantity = totalquantity + quantity;
//			}
//			List<BufferDetailsEntity> findByproductVariantCode2 = bufferDetailsRepository
//					.findBySerialNumberBetweenAndBufferIsDeleted(serialNumber1, serialNumber2, 0);
//
//			for (int j = 0; j < findByproductVariantCode2.size(); j++) {
//				int quantity = findByproductVariantCode2.get(j).getQuantity();
//				totalBufferQuantity = totalBufferQuantity + quantity;
//			}
//
//			if (plannedQuantity < totalquantity) {
//				Date dNow = new Date();
//				SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
//				String date = ft.format(dNow);
//				generateManualRetrievalOrderEntity.setCreatedDatetime(date);
//				generateManualRetrievalOrderEntity.setAcutualQuantity(0);
//				generateManualRetrievalOrderEntity.setIsDispatchStart(0);
//				generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//				System.out.println("1");
//
//			} else if (plannedQuantity < (totalquantity + totalBufferQuantity)) {
//				Date dNow = new Date();
//				SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
//				String date = ft.format(dNow);
//				generateManualRetrievalOrderEntity.setCreatedDatetime(date);
//				generateManualRetrievalOrderEntity.setAcutualQuantity(0);
//				generateManualRetrievalOrderEntity.setIsDispatchStart(0);
//				generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//				System.out.println("2");
//				return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.ACCEPTED,
//						generateManualRetrievalOrderEntity);
//			} else if ((totalquantity + totalBufferQuantity) == 0) {
//				System.out.println("totalquantity + totalBufferQuantity :: " + (totalquantity + totalBufferQuantity));
//
//				return ResponseHandler.generateResponse("Mannual dispatch failed due to Quantity available is Zero",
//						HttpStatus.INTERNAL_SERVER_ERROR, generateManualRetrievalOrderEntity);
//			}
//
//			else {
//				System.out.println("***");
//				return ResponseHandler
//						.generateResponse(
//								"Insufficient Quantity available Quantity is :  "
//										+ (totalquantity + totalBufferQuantity) + "Do you want to dispatch",
//								HttpStatus.ALREADY_REPORTED, null);
//			}
//
//		} catch (Exception e) {
//			return ResponseHandler.generateResponse("Mannual dispatch failed", HttpStatus.INTERNAL_SERVER_ERROR, null);
//		}
//
//		return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.OK,
//				generateManualRetrievalOrderEntity);
//	}
//
//	public ResponseEntity<Object> findByserialNumberBetween1(int serialNumber1, int serialNumber2,
//			String dispatchOrderNumber, int shiftId, String shiftName) {
//		int totalquantity = 0;
//		int totalBufferQuantity = 0;
//		GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity = new GenerateManualRetrievalOrderEntity();
//		try {
//
//			List<CurrentPalletStockDetailsEntity> findByserialNumberBetween = currentPalletStockDetailsRepository
//					.findByserialNumberBetween(serialNumber1, serialNumber2);
//			for (int i = 0; i < findByserialNumberBetween.size(); i++) {
//				int quantity = findByserialNumberBetween.get(i).getQuantity();
//				totalquantity = totalquantity + quantity;
//			}
//			List<BufferDetailsEntity> findByproductVariantCode2 = bufferDetailsRepository
//					.findBySerialNumberBetweenAndBufferIsDeleted(serialNumber1, serialNumber2, 0);
//
//			for (int j = 0; j < findByproductVariantCode2.size(); j++) {
//				int quantity = findByproductVariantCode2.get(j).getQuantity();
//				totalBufferQuantity = totalBufferQuantity + quantity;
//			}
//			Date dNow = new Date();
//			SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
//			String date = ft.format(dNow);
//			generateManualRetrievalOrderEntity.setCreatedDatetime(date);
//			generateManualRetrievalOrderEntity.setAcutualQuantity(0);
//			generateManualRetrievalOrderEntity.setIsDispatchStart(0);
//			generateManualRetrievalOrderEntity.setPlannedQuantity(totalquantity + totalBufferQuantity);
//			generateManualRetrievalOrderEntity.setBalanceQuantity(totalquantity + totalBufferQuantity);
//			generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//			System.out.println("Do you want to dispatch" + (totalquantity + totalBufferQuantity));
//
//		} catch (Exception e) {
//			return ResponseHandler.generateResponse("Mannual dispatch failed ", HttpStatus.BAD_REQUEST, null);
//		}
//		return ResponseHandler.generateResponse("Insufficient Quantity available Quantity  :  "
//				+ (totalquantity + totalBufferQuantity) + " Do you want to dispatch", HttpStatus.CREATED,
//				generateManualRetrievalOrderEntity);
//	}

//	@Override
//	public ResponseEntity<Object> findByserialNumberBetween(
//			CurrentPalletStockDetailsEntity currentPalletStockDetailsEntity) {
//		GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity = new GenerateManualRetrievalOrderEntity();
//		try {
//			int serialNumber1 = currentPalletStockDetailsEntity.getSerialNumber();
//			int serialNumber2 = currentPalletStockDetailsEntity.getSerialNumber();
//			List<CurrentPalletStockDetailsEntity> findByserialNumberBetween = currentPalletStockDetailsRepository
//					.findByserialNumberBetween(serialNumber1, serialNumber2);
////			int plannedQuantity = findByserialNumberBetween.size();
//			int totalquantity = 0;
//			int totalBufferQuantity = 0;
//			int plannedQuantity = serialNumber2 - serialNumber1 + 1;
//			for (int i = 0; i < findByserialNumberBetween.size(); i++) {
//				int quantity = findByserialNumberBetween.get(i).getQuantity();
//				totalquantity = totalquantity + quantity;
//			}
//			List<BufferDetailsEntity> findByproductVariantCode2 = bufferDetailsRepository
//					.findBySerialNumberBetweenAndBufferIsDeleted(serialNumber1, serialNumber2, 0);
//
//			for (int j = 0; j < findByproductVariantCode2.size(); j++) {
//				int quantity = findByproductVariantCode2.get(j).getQuantity();
//				totalBufferQuantity = totalBufferQuantity + quantity;
//			}
//
//			if (plannedQuantity < totalquantity) {
//				Date dNow = new Date();
//				SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
//				String date = ft.format(dNow);
//				generateManualRetrievalOrderEntity.setCreatedDatetime(date);
//				generateManualRetrievalOrderEntity.setAcutualQuantity(0);
//				generateManualRetrievalOrderEntity.setIsDispatchStart(0);
//				generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//				System.out.println("1");
//
//			} else if (plannedQuantity < (totalquantity + totalBufferQuantity)) {
//				Date dNow = new Date();
//				SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
//				String date = ft.format(dNow);
//				generateManualRetrievalOrderEntity.setCreatedDatetime(date);
//				generateManualRetrievalOrderEntity.setAcutualQuantity(0);
//				generateManualRetrievalOrderEntity.setIsDispatchStart(0);
//				generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//				System.out.println("2");
//				return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.ACCEPTED,
//						generateManualRetrievalOrderEntity);
//			} else if ((totalquantity + totalBufferQuantity) == 0) {
//				System.out.println("totalquantity + totalBufferQuantity :: " + (totalquantity + totalBufferQuantity));
//
//				return ResponseHandler.generateResponse("Mannual dispatch failed due to Quantity available is Zero",
//						HttpStatus.INTERNAL_SERVER_ERROR, generateManualRetrievalOrderEntity);
//			}
//
//			else {
//				System.out.println("***");
//				return ResponseHandler
//						.generateResponse(
//								"Insufficient Quantity available Quantity is :  "
//										+ (totalquantity + totalBufferQuantity) + "Do you want to dispatch",
//								HttpStatus.ALREADY_REPORTED, null);
//			}
//
//		} catch (Exception e) {
//			return ResponseHandler.generateResponse("Mannual dispatch failed", HttpStatus.INTERNAL_SERVER_ERROR, null);
//		}
//
//		return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.OK,
//				generateManualRetrievalOrderEntity);
//	}

//	public ResponseEntity<Object> findByserialNumberBetween1(
//			CurrentPalletStockDetailsEntity currentPalletStockDetailsEntity) {
//		int totalquantity = 0;
//		int totalBufferQuantity = 0;
//		int serialNumber1 = currentPalletStockDetailsEntity.getSerialNumber();
//		int serialNumber2 = currentPalletStockDetailsEntity.getSerialNumber();
//		GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity = new GenerateManualRetrievalOrderEntity();
//		try {
//
//			List<CurrentPalletStockDetailsEntity> findByserialNumberBetween = currentPalletStockDetailsRepository
//					.findByserialNumberBetween(serialNumber1, serialNumber2);
//			for (int i = 0; i < findByserialNumberBetween.size(); i++) {
//				int quantity = findByserialNumberBetween.get(i).getQuantity();
//				totalquantity = totalquantity + quantity;
//			}
//			List<BufferDetailsEntity> findByproductVariantCode2 = bufferDetailsRepository
//					.findBySerialNumberBetweenAndBufferIsDeleted(serialNumber1, serialNumber2, 0);
//
//			for (int j = 0; j < findByproductVariantCode2.size(); j++) {
//				int quantity = findByproductVariantCode2.get(j).getQuantity();
//				totalBufferQuantity = totalBufferQuantity + quantity;
//			}
//			Date dNow = new Date();
//			SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
//			String date = ft.format(dNow);
//			generateManualRetrievalOrderEntity.setCreatedDatetime(date);
//			generateManualRetrievalOrderEntity.setAcutualQuantity(0);
//			generateManualRetrievalOrderEntity.setIsDispatchStart(0);
//			generateManualRetrievalOrderEntity.setPlannedQuantity(totalquantity + totalBufferQuantity);
//			generateManualRetrievalOrderEntity.setBalanceQuantity(totalquantity + totalBufferQuantity);
//			generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
//			System.out.println("Do you want to dispatch" + (totalquantity + totalBufferQuantity));
//
//		} catch (Exception e) {
//			return ResponseHandler.generateResponse("Mannual dispatch failed ", HttpStatus.BAD_REQUEST, null);
//		}
//		return ResponseHandler.generateResponse("Insufficient Quantity available Quantity  :  "
//				+ (totalquantity + totalBufferQuantity) + " Do you want to dispatch", HttpStatus.CREATED,
//				generateManualRetrievalOrderEntity);
//	}

	public ResponseEntity<Object> findByserialNumberBetween(int serialNumber1, int serialNumber2,
			String dispatchOrderNumber, int shiftId, String shiftName) {
		List<CurrentPalletStockDetailsEntity> findByserialNumberBetween = currentPalletStockDetailsRepository
				.findByserialNumberBetween(serialNumber1, serialNumber2);
		GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity = new GenerateManualRetrievalOrderEntity();

		int plannedQuantity = serialNumber2 - serialNumber1 + 1;
		int totalquantity = 0;
		int totalBufferQuantity = 0;
		for (int i = 0; i < findByserialNumberBetween.size(); i++) {
			int quantity = findByserialNumberBetween.get(i).getQuantity();
			totalquantity = totalquantity + quantity;
		}
		// int plannedQuantity=serialNumber2-serialNumber1+1;
		List<BufferDetailsEntity> findByproductVariantCode2 = bufferDetailsRepository
				.findBySerialNumberBetweenAndBufferIsDeleted(serialNumber1, serialNumber2, 0);

		for (int j = 0; j < findByproductVariantCode2.size(); j++) {
			int quantity = findByproductVariantCode2.get(j).getQuantity();
			totalBufferQuantity = totalBufferQuantity + quantity;
		}

		if (plannedQuantity <= totalquantity) {
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
			String date = ft.format(dNow);
			generateManualRetrievalOrderEntity.setCreatedDatetime(date);
			generateManualRetrievalOrderEntity.setPlannedQuantity(totalquantity + totalBufferQuantity);
			generateManualRetrievalOrderEntity.setBalanceQuantity(totalquantity + totalBufferQuantity);
			generateManualRetrievalOrderEntity.setAcutualQuantity(0);
			generateManualRetrievalOrderEntity.setIsDispatchStart(0);
			generateManualRetrievalOrderEntity.setDispatchOrderNumber(dispatchOrderNumber);
			generateManualRetrievalOrderEntity.setProductVariantCode("NA");
			generateManualRetrievalOrderEntity.setShiftName(shiftName);
			generateManualRetrievalOrderEntity.setProductName("NA");
			generateManualRetrievalOrderEntity.setProductVariantName("NA");
			generateManualRetrievalOrderEntity.setDispatchStatus("READY");
			generateManualRetrievalOrderEntity.setShiftId(shiftId);
			generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
			AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
			auditTrailDetailsEntity.setOperatorActions("Retrival order generated of Dispatch Number  :  "
					+ generateManualRetrievalOrderEntity.getDispatchOrderNumber() + " and Quantity is  "
					+ generateManualRetrievalOrderEntity.getPlannedQuantity());
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
			System.out.println("1");

		} else if (plannedQuantity <= (totalquantity + totalBufferQuantity)) {
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
			String date = ft.format(dNow);
			generateManualRetrievalOrderEntity.setCreatedDatetime(date);
			generateManualRetrievalOrderEntity.setPlannedQuantity(totalquantity + totalBufferQuantity);
			generateManualRetrievalOrderEntity.setBalanceQuantity(totalquantity + totalBufferQuantity);
			generateManualRetrievalOrderEntity.setAcutualQuantity(0);
			generateManualRetrievalOrderEntity.setIsDispatchStart(0);
			generateManualRetrievalOrderEntity.setDispatchOrderNumber(dispatchOrderNumber);
			generateManualRetrievalOrderEntity.setProductVariantCode("NA");
			generateManualRetrievalOrderEntity.setShiftName(shiftName);
			generateManualRetrievalOrderEntity.setProductName("NA");
			generateManualRetrievalOrderEntity.setProductVariantName("NA");
			generateManualRetrievalOrderEntity.setDispatchStatus("READY");
//			generateManualRetrievalOrderEntity.setIsOrderCancelled(1);
			generateManualRetrievalOrderEntity.setShiftId(shiftId);

			generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
			AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
			auditTrailDetailsEntity.setOperatorActions("Retrival order generated of Dispatch Number  :  "
					+ generateManualRetrievalOrderEntity.getDispatchOrderNumber() + " and Quantity is  "
					+ generateManualRetrievalOrderEntity.getPlannedQuantity());

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
			return ResponseHandler.generateResponse("Insufficient Quantity available Quantity is :  "
					+ (totalquantity + totalBufferQuantity) + ". Do you want to dispatch ?",
					HttpStatus.ALREADY_REPORTED, null);
		}

		return ResponseHandler.generateResponse("Mannual dispatch sucessfully done", HttpStatus.OK,
				generateManualRetrievalOrderEntity);

	}

	public ResponseEntity<Object> findByserialNumberBetween1(int serialNumber1, int serialNumber2,
			String dispatchOrderNumber, int shiftId, String shiftName) {
		int totalquantity = 0;
		int totalBufferQuantity = 0;
//		int serialNumber1 = currentPalletStockDetailsEntity.getSerialNumber();
//		int serialNumber2 = currentPalletStockDetailsEntity.getSerialNumber();
		GenerateManualRetrievalOrderEntity generateManualRetrievalOrderEntity = new GenerateManualRetrievalOrderEntity();
		try {

			List<CurrentPalletStockDetailsEntity> findByserialNumberBetween = currentPalletStockDetailsRepository
					.findByserialNumberBetween(serialNumber1, serialNumber2);
			for (int i = 0; i < findByserialNumberBetween.size(); i++) {
				int quantity = findByserialNumberBetween.get(i).getQuantity();
				totalquantity = totalquantity + quantity;
			}
			List<BufferDetailsEntity> findByproductVariantCode2 = bufferDetailsRepository
					.findBySerialNumberBetweenAndBufferIsDeleted(serialNumber1, serialNumber2, 0);

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
			generateManualRetrievalOrderEntity.setPlannedQuantity(totalquantity + totalBufferQuantity);
			generateManualRetrievalOrderEntity.setBalanceQuantity(totalquantity + totalBufferQuantity);
			generateManualRetrievalOrderEntity.setDispatchOrderNumber(dispatchOrderNumber);
			generateManualRetrievalOrderEntity.setProductVariantCode("NA");
			generateManualRetrievalOrderEntity.setShiftName(shiftName);
			generateManualRetrievalOrderEntity.setProductName("NA");
			generateManualRetrievalOrderEntity.setProductVariantName("NA");
			generateManualRetrievalOrderEntity.setDispatchStatus("READY");
//			generateManualRetrievalOrderEntity.setIsOrderCancelled(1);
			generateManualRetrievalOrderEntity.setShiftId(shiftId);
			generateManualRetrievalOrderRepository.save(generateManualRetrievalOrderEntity);
			AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
			auditTrailDetailsEntity.setOperatorActions("Retrival order generated of Dispatch Number  : "
					+ generateManualRetrievalOrderEntity.getDispatchOrderNumber() + " and Quantity is "
					+ generateManualRetrievalOrderEntity.getPlannedQuantity());
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
		return ResponseHandler.generateResponse("Insufficient Quantity available Quantity  :  "
				+ (totalquantity + totalBufferQuantity) + " Do you want to dispatch ?", HttpStatus.CREATED,
				generateManualRetrievalOrderEntity);
	}
}

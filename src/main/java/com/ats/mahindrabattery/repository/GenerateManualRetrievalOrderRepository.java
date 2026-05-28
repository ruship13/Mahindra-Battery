package com.ats.mahindrabattery.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ats.mahindrabattery.entity.GenerateManualRetrievalOrderEntity;
import com.ats.mahindrabattery.entity.InfeedMissionRuntimeDetailsEntity;
import com.ats.mahindrabattery.entity.MasterProductVariantDetailsEntity;

@Transactional
public interface GenerateManualRetrievalOrderRepository
		extends JpaRepository<GenerateManualRetrievalOrderEntity, Integer> {

//	@Query(value = "select * from ats_wms_material_dispatch_schedule_history where cDateTime BETWEEN :startDate AND :endDate, AND IS_ORDER_DELETED :0", nativeQuery = true)
//	public List<GenerateManualRetrievalOrderEntity> getAllGenerateManualRetrievalOrderBetweenDatesAndIsOrderDeleted(String startDate,
//			String endDate,int isOrderDeleted);
//	
//	
	
	

	@Query(value = "select * from ats_wms_material_dispatch_schedule_history where CDATETIME BETWEEN :startDate AND :endDate ", nativeQuery = true)
	public List<GenerateManualRetrievalOrderEntity> findCDateTimeBetweenDates(String startDate,
			String endDate);
	
	public List<GenerateManualRetrievalOrderEntity> findBycreatedDatetimeBetween(String startDate, String endDate);
	
	public List<GenerateManualRetrievalOrderEntity> findBycreatedDatetimeBetweenAndIsOrderDeleted(String startDate, String endDate, int isOrderDeleted);

	@Query(value = "select * FROM ats_wms_material_dispatch_schedule_history where cDateTime BETWEEN :startDate AND :endDate", nativeQuery = true)
	public List<GenerateManualRetrievalOrderEntity> findGenerateManualRetrievalOrderBetweenDates(String startDate,
			String endDate);
	public List<GenerateManualRetrievalOrderEntity> findByIsDispatchStart(int isDispatchStart);
	public List<GenerateManualRetrievalOrderEntity> findBydispatchOrderNumber(String dispatchOrderNumber);

	public List<GenerateManualRetrievalOrderEntity> findByProductVariantCode(String productVariantCode);

	public List<GenerateManualRetrievalOrderEntity> findBySerialNumber(int serialNumber);

	public List<GenerateManualRetrievalOrderEntity> findByserialNumberBetween(int serialNumber1, int serialNumber2);

	public List<GenerateManualRetrievalOrderEntity> findByIsOrderDeleted(int i);

	

	public List <GenerateManualRetrievalOrderEntity> findByUserName(String name);
	
	
	



}

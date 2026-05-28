package com.ats.mahindrabattery.service;

import java.util.List;

import com.ats.mahindrabattery.entity.GenerateManualRetrievalOrderEntity;

public interface GenerateManualRetrievalOrderService {

	public List<GenerateManualRetrievalOrderEntity> getAllMannualRetrivalDetails();
	
	public List<GenerateManualRetrievalOrderEntity> findAllMannualDispatchOrdersByDate(String startDate,
			String endDate);
	
	public List<GenerateManualRetrievalOrderEntity> findByMannualDispatchNumber(String dispatchOrderNumber);

	
	
}

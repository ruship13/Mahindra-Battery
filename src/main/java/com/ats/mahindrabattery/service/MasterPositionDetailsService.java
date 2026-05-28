package com.ats.mahindrabattery.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.ats.mahindrabattery.entity.MasterPositionDetailsEntity;
import com.ats.mahindrabattery.entity.MasterRackPositionDetails;

public interface MasterPositionDetailsService{

	public List<MasterPositionDetailsEntity> findAll();
	
	public MasterPositionDetailsEntity findByPositionNameAndPositionIsAllocatedAndEmptyPalletPositionAndPositionIsActive(
			String positionName, int positionIsAllocated, int emptyPalletPosition, int positionIsActive);
	
	public List<MasterRackPositionDetails> findByAreaIdAndFloorId(int areaId, int floorId);
	
	public List<MasterPositionDetailsEntity> findByAreaId(int areaId);
	
	public ResponseEntity<MasterPositionDetailsEntity> updatIsManualDispatchInMasterPositionDetails(int positionId);
	
	public MasterPositionDetailsEntity updateUnlockSelectedPositionIsActive(
			MasterPositionDetailsEntity masterPositionDetailsEntity, @PathVariable int positionId);
	
	public MasterPositionDetailsEntity updateLockSelectedPositionIsActive(
			MasterPositionDetailsEntity masterPositionDetailsEntity, int positionId) ;
	
	public MasterPositionDetailsEntity UpdatePositionIsEmpty(MasterPositionDetailsEntity masterPositionDetailsEntity,
			int positionId);
	
	public void updatePositionIsAllocated(MasterPositionDetailsEntity masterPositionDetailsEntity, int positionId);
	
	public List<MasterPositionDetailsEntity> findByPositionName(String positionName);
	
	
}

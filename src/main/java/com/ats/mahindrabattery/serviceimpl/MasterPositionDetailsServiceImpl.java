package com.ats.mahindrabattery.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ats.mahindrabattery.entity.AuditTrailDetailsEntity;
import com.ats.mahindrabattery.entity.CurrentPalletStockDetailsEntity;
import com.ats.mahindrabattery.entity.MasterPositionDetailsEntity;
import com.ats.mahindrabattery.entity.MasterRackDetailsEntity;
import com.ats.mahindrabattery.entity.MasterRackPositionDetails;
import com.ats.mahindrabattery.repository.AuditTrailDetailsRepository;
import com.ats.mahindrabattery.repository.CurrentPalletStockDetailsRepository;
import com.ats.mahindrabattery.repository.MasterPositionDetailsRepository;
import com.ats.mahindrabattery.repository.MasterRackDetailsRepository;
import com.ats.mahindrabattery.service.MasterPositionDetailsService;


@Service

public class MasterPositionDetailsServiceImpl implements MasterPositionDetailsService{

	@Autowired
	private MasterPositionDetailsRepository masterPositionDetailsRepositoryInstance;

	@Autowired
	private MasterRackDetailsRepository masterRackDetailsRepositoryInstance;
	
	@Autowired
	private CurrentPalletStockDetailsRepository currentPalletStockDetailsRepository;
	
	@Autowired
	private AuditTrailDetailsRepository auditTrailDetailsRepository;

	public List<MasterPositionDetailsEntity> findAll() {
		return masterPositionDetailsRepositoryInstance.findAll();
	}
//	public List<MasterPositionDetailsEntity>findByPositionName(String positionName){
//		return masterPositionDetailsRepositoryInstance.findByPositionName(positionName);
//	}

	public MasterPositionDetailsEntity findByPositionNameAndPositionIsAllocatedAndEmptyPalletPositionAndPositionIsActive(
			String positionName, int positionIsAllocated, int emptyPalletPosition, int positionIsActive) {
		try {
			return masterPositionDetailsRepositoryInstance
					.findByPositionNameAndPositionIsAllocatedAndEmptyPalletPositionAndPositionIsActive(positionName,
							positionIsAllocated, emptyPalletPosition, positionIsActive);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}
	
	public List<MasterRackPositionDetails> findByAreaIdAndFloorId(int areaId, int floorId) {
		//System.out.println("in 123");
		//System.out.println("Area Id: " + areaId + "Floor Id : " + floorId);
		List<MasterRackPositionDetails> list = new ArrayList<MasterRackPositionDetails>();
		List<MasterRackDetailsEntity> rackList = null;
		List<MasterPositionDetailsEntity> positionList = null;
		//List<CurrentPalletStockDetailsEntity>currentPalletList=null;
		try {
			positionList = masterPositionDetailsRepositoryInstance.findByAreaIdAndFloorIdOrderByPositionId(areaId, floorId);
		//System.out.println("in positionList ::"+positionList);
			rackList = masterRackDetailsRepositoryInstance.findByAreaIdAndFloorIdOrderByRackId(areaId, floorId);
			//System.out.println("rackList ::"+rackList.size());
			for (int i = 0; i < rackList.size(); i++) {
				MasterRackPositionDetails obj = new MasterRackPositionDetails();
				obj.setRackId(rackList.get(i).getRackId());
				int rackId = rackList.get(i).getRackId();

				List<MasterPositionDetailsEntity> list1 = null;
				list1 = positionList.stream().filter(data -> data.getRackId() == (rackId))
						.sorted(Comparator.comparing(MasterPositionDetailsEntity::getPositionId).reversed())
						.collect(Collectors.toList());
				//System.out.println("in list1 ::"+list1.size());
				for(int p=0;p<list1.size();p++) {
					//find by position id
					List<CurrentPalletStockDetailsEntity>currentPalletList=null;
					currentPalletList=currentPalletStockDetailsRepository.findByPositionId(list1.get(p).getPositionId());
					
					// check list size >0
					
					if(currentPalletList.size()>0) {
						//check if materialcode!=na, if true
						//System.out.println("in currentpallet::"+currentPalletList.get(0).getProductVariantCode());
						if(!currentPalletList.get(0).getProductVariantCode().equals("NA")) {
							if(currentPalletList.get(0).getProductName().equals("BEV")) {
							list1.get(p).setIsMaterialLoaded(1);
							list1.get(p).setProductName(currentPalletList.get(0).getProductName());
							System.out.println("list1 productvariant name::"+list1.get(p).getProductName());
							}
							else if (currentPalletList.get(0).getProductName().equals("S230")){
								list1.get(p).setIsMaterialLoaded(1);
								list1.get(p).setProductName(currentPalletList.get(0).getProductName());
								System.out.println("list1 productvariant name::"+list1.get(p).getProductName());
							}
							
						}
						
						else {
							
							//System.out.println("in else::"+currentPalletList.get(0).getProductVariantCode());
							list1.get(p).setIsMaterialLoaded(0);
							
						}
						
						
					}
					
					
				}
				

				obj.setPosition(list1);
				list.add(obj);
				

			}
			//System.out.println(list);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//System.out.println(list);
		return list;
//		return null;
	}

//
//	public List<MasterRackPositionDetails> findByAreaIdAndFloorId(int areaId, int floorId) {
//		//System.out.println("in 123");
//		//System.out.println("Area Id: " + areaId + "Floor Id : " + floorId);
//		List<MasterRackPositionDetails> list = new ArrayList<MasterRackPositionDetails>();
//		List<MasterRackDetailsEntity> rackList = null;
//		List<MasterPositionDetailsEntity> positionList = null;
//		//List<CurrentPalletStockDetailsEntity>currentPalletList=null;
//		try {
//			positionList = masterPositionDetailsRepositoryInstance.findByAreaIdAndFloorIdOrderByPositionId(areaId, floorId);
//		//System.out.println("in positionList ::"+positionList);
//			rackList = masterRackDetailsRepositoryInstance.findByAreaIdAndFloorIdOrderByRackId(areaId, floorId);
//			//System.out.println("rackList ::"+rackList.size());
//			for (int i = 0; i < rackList.size(); i++) {
//				MasterRackPositionDetails obj = new MasterRackPositionDetails();
//				obj.setRackId(rackList.get(i).getRackId());
//				int rackId = rackList.get(i).getRackId();
//
//				List<MasterPositionDetailsEntity> list1 = null;
//				list1 = positionList.stream().filter(data -> data.getRackId() == (rackId))
//						.sorted(Comparator.comparing(MasterPositionDetailsEntity::getPositionId).reversed())
//						.collect(Collectors.toList());
//				//System.out.println("in list1 ::"+list1.size());
//				for(int p=0;p<list1.size();p++) {
//					//find by position id
//					List<CurrentPalletStockDetailsEntity>currentPalletList=null;
//					currentPalletList=currentPalletStockDetailsRepository.findByPositionId(list1.get(p).getPositionId());
//					
//					// check list size >0
//					
//					if(currentPalletList.size()>0) {
//						//check if materialcode!=na, if true
//						//System.out.println("in currentpallet::"+currentPalletList.get(0).getProductVariantCode());
//						if(!currentPalletList.get(0).getProductVariantCode().equals("NA")) {
//							
//							list1.get(p).setIsMaterialLoaded(1);
//							
//						}
//						
//						else {
//							
//							//System.out.println("in else::"+currentPalletList.get(0).getProductVariantCode());
//							list1.get(p).setIsMaterialLoaded(0);
//							
//						}
//						
//						
//					}
//					
//					
//				}
//				
//
//				obj.setPosition(list1);
//				list.add(obj);
//				
//
//			}
//			//System.out.println(list);
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		//System.out.println(list);
//		return list;
////		return null;
//	}


//	public List<MasterRackPositionDetails> findByAreaIdAndFloorId1(int areaId, int floorId) {
//		
//	}

	public List<MasterPositionDetailsEntity> findByAreaId(int areaId) {
		try {
			return masterPositionDetailsRepositoryInstance.findByAreaId(areaId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public ResponseEntity<MasterPositionDetailsEntity> updatIsManualDispatchInMasterPositionDetails(int positionId) {
		MasterPositionDetailsEntity masterPositionDetailsEntity1 = new MasterPositionDetailsEntity();
		try {
			masterPositionDetailsEntity1 = masterPositionDetailsRepositoryInstance.findByPositionId(positionId);

			if (masterPositionDetailsEntity1 != null) {
				masterPositionDetailsEntity1.setIsManualDispatch(1);
				return new ResponseEntity<MasterPositionDetailsEntity>(
						masterPositionDetailsRepositoryInstance.save(masterPositionDetailsEntity1), HttpStatus.OK);
			} else {
				return new ResponseEntity<MasterPositionDetailsEntity>(new MasterPositionDetailsEntity(),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	public MasterPositionDetailsEntity updateUnlockSelectedPositionIsActive(
			MasterPositionDetailsEntity masterPositionDetailsEntity, @PathVariable int positionId) {
		try {
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
			String date = ft.format(dNow);
			masterPositionDetailsEntity.setCDateTime(date);
			masterPositionDetailsRepositoryInstance.findById(positionId).ifPresent(positionData -> {
				positionData.setPositionIsActive(1);
				//System.out.println("positionData" + positionData.toString());
				MasterPositionDetailsEntity save1 = masterPositionDetailsRepositoryInstance.save(positionData);
			});
			return masterPositionDetailsEntity;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return masterPositionDetailsEntity;

	}

	public MasterPositionDetailsEntity updateLockSelectedPositionIsActive(
			MasterPositionDetailsEntity masterPositionDetailsEntity, int positionId) {
		try {
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
			String date = ft.format(dNow);
			masterPositionDetailsEntity.setCDateTime(date);

			masterPositionDetailsRepositoryInstance.findById(positionId).ifPresent(positionData -> {
				positionData.setPositionIsActive(0);

				masterPositionDetailsRepositoryInstance.save(positionData);
			});
			return masterPositionDetailsEntity;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return masterPositionDetailsEntity;
	}

	public MasterPositionDetailsEntity UpdatePositionIsEmpty(MasterPositionDetailsEntity masterPositionDetailsEntity,
			int positionId) {
		
		try {
			masterPositionDetailsRepositoryInstance.findById(positionId).ifPresent(positionData -> {
				   positionData.setPositionIsAllocated(0);
				   positionData.setEmptyPalletPosition(1);
				masterPositionDetailsRepositoryInstance.save(positionData);
			});
			System.out.println("positionId");
			return masterPositionDetailsEntity;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return masterPositionDetailsEntity;

	}

	
	
	public void updatePositionIsAllocated(MasterPositionDetailsEntity masterPositionDetailsEntity, int positionId) {
		masterPositionDetailsRepositoryInstance.findById(positionId).ifPresent(positionDetails -> {
			positionDetails.setPositionIsAllocated(0);
			positionDetails.setEmptyPalletPosition(1);
			positionDetails.setIsManualDispatch(0);
			masterPositionDetailsRepositoryInstance.save(positionDetails);

		});
		
		Date dNow = new Date();
		SimpleDateFormat sdateformat = new SimpleDateFormat("dd MMM yyyy" + " " + "HH:mm:ss");
		String date = sdateformat.format(dNow);
		
		MasterPositionDetailsEntity masterPositionDetailsEntity2 = masterPositionDetailsRepositoryInstance.findById(positionId).get();
		
		
		AuditTrailDetailsEntity auditTrailDetailsEntity = new AuditTrailDetailsEntity();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = authentication.getName();
		System.out.println(" name :: " + name);
		auditTrailDetailsEntity.setOperatorActions(
				"Position free allocated by  " + name
						+ " for position  " + masterPositionDetailsEntity2.getPositionName() );
		auditTrailDetailsEntity.setField("Free allocation");
//		auditTrailDetailsEntity.setAfterValue(0);
//		auditTrailDetailsEntity.setBeforeValue(0);
		auditTrailDetailsEntity.setReason("Free allocation");
		
		auditTrailDetailsEntity.setUsername(name);
		auditTrailDetailsEntity.setDatetimeC(date);
		auditTrailDetailsRepository.save(auditTrailDetailsEntity);

	}

	public List<MasterPositionDetailsEntity> findByPositionName(String positionName) {
		// TODO Auto-generated method stub
		 List<MasterPositionDetailsEntity> data=masterPositionDetailsRepositoryInstance.findByPositionName(positionName);;
	return data;
//		return null;
	}


}

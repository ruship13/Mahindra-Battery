package com.ats.mahindrabattery.service;

import java.util.List;

import com.ats.mahindrabattery.entity.AccessMatrixEntity;

public interface AccessMatrixService {

	
	AccessMatrixEntity updateAccessMatrixDetails(int accessmMatrixId);

	List<AccessMatrixEntity> getAllAccessMatrixDetails();
	
	  
	    public void updateAccessMatrix(AccessMatrixEntity accessMatrix) ;

	//public List<AccessMatrixEntity> findAll();

}

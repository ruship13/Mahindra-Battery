package com.ats.mahindrabattery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ats.mahindrabattery.entity.BufferDetailsEntity;

public interface BufferDetailsRepository extends JpaRepository<BufferDetailsEntity, Integer>{

//	public List<BufferDetailsEntity> findByproductVariantCode(String productVariantCode);

	public List<BufferDetailsEntity> findByproductVariantCodeAndBufferIsDeleted(String productVariantCode, int i);

	public List<BufferDetailsEntity> findBySerialNumberBetweenAndBufferIsDeleted(int serialNumber1, int serialNumber2,
			int i);

}

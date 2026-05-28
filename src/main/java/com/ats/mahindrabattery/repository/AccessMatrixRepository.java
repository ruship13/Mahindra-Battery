package com.ats.mahindrabattery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ats.mahindrabattery.entity.AccessMatrixEntity;

public interface AccessMatrixRepository extends JpaRepository<AccessMatrixEntity, Integer> {

	
}

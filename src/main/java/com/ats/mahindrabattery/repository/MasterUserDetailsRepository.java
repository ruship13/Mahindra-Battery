package com.ats.mahindrabattery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ats.mahindrabattery.entity.MasterUserDetailsEntity;

public interface MasterUserDetailsRepository extends JpaRepository<MasterUserDetailsEntity, Integer> {

	List<MasterUserDetailsEntity> findByuserName(String userName);

	List<MasterUserDetailsEntity> findByuserIsDeleted(int i);

	MasterUserDetailsEntity findByUserIdAndUserName(int userId, String userName);

	MasterUserDetailsEntity findByUserId(int userId);

	MasterUserDetailsEntity findByEmailId(String emailId);

	List<MasterUserDetailsEntity> findByuserNameAndUserIsDeleted(String userName, int i);

	@Query(value = "select * from ats_wms_master_user_details where EMAIL_ID =:emailId", nativeQuery = true)
	List<MasterUserDetailsEntity> getByEmailId(String emailId);

	List<MasterUserDetailsEntity> findByUserNameAndUserIdNot(String userName, int id);

	List<MasterUserDetailsEntity> findByEmailIdAndUserIdNot(String emailId, int id);
	
}

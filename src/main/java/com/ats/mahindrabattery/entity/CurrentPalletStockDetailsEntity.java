package com.ats.mahindrabattery.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ats_wms_current_stock_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentPalletStockDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CURRENT_STOCK_DETAILS_ID")
	private int currentPalletStockDetailsId;

	@Column(name = "PALLET_INFORMATION_ID")
	private int palletInformationId;

	@Column(name = "PALLET_CODE")
	private String palletCode;

	@Column(name = "POSITION_ID")
	private int positionId;

	@Column(name = "POSITION_NAME")
	private String positionName;

	@Column(name = "SERIAL_NUMBER")
	private int serialNumber;

	@Column(name = "PRODUCT_ID")
	private int productId;

	@Column(name = "PRODUCT_NAME")
	private String productName;

	@Column(name = "PRODUCT_VARIANT_ID")
	private int productVariantId;

	@Column(name = "PRODUCT_VARIANT_CODE")
	private String productVariantCode;

	@Column(name = "PRODUCT_VARIANT_NAME")
	private String productVariantName;

	@Column(name = "BATCH_NUMBER")
	private String batchNumber;

	@Column(name = "MODEL_NUMBER")
	private String modelNumber;

	@Column(name = "PALLET_STATUS_ID")
	private int palletStatusId;

	@Column(name = "PALLET_STATUS_NAME")
	private String palletStatusname;

	@Column(name = "AGEING_DAYS")
	private int ageingDays;

	@Column(name = "QUANTITY")
	private int quantity;

	@Column(name = "QUALITY_STATUS")
	private String qualityStatus;

	@Column(name = "EXPIRY_DATE")
	private String expiryDate;

	@Column(name = "LOAD_DATETIME")
	private String loadDatetime;

	@Column(name = "AREA_ID")
	private int areaId;

	@Column(name = "AREA_Name")
	private String areaName;

	@Column(name = "FLOOR_ID")
	private int floorId;

	@Column(name = "FLOOR_NAME")
	private String floorName;

	@Column(name = "RACK_ID")
	private int rackId;

	@Column(name = "RACK_NAME")
	private String rackName;

	@Column(name = "RACK_SIDE")
	private String rackSide;

	@Column(name = "RACK_COLUMN")
	private String rackColumn;

	@Column(name = "POSITION_NUMBER_IN_RACK")
	private int positionNumberInRack;

	@Column(name = "LOCATION")
	private String location;

	@Column(name = "IS_OUTFEED_MISSION_GENERATED")
	private int isOutfeedMissionGenerated;

	@Column(name = "IS_INFEED_MISSION_GENERATED")
	private int isInfeedMissionGenerated;

	@Column(name = "USER_ID")
	private int userId;

	@Column(name = "USER_NAME")
	private String userName;

}

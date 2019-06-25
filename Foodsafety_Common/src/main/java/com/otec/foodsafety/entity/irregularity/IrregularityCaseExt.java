package com.otec.foodsafety.entity.irregularity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class IrregularityCaseExt implements Serializable {

private static final long serialVersionUID = 1L;

     private IrregularityCase irregularityCase;

     private Long  productPTypeId;

	private IrregularityCaseClose caseClose;
	private IrregularityPunish punish;

	public IrregularityCase getIrregularityCase() {
		return irregularityCase;
	}

	public void setIrregularityCase(IrregularityCase irregularityCase) {
		this.irregularityCase = irregularityCase;
	}

	public IrregularityCaseClose getCaseClose() {
		return caseClose;
	}

	public void setCaseClose(IrregularityCaseClose caseClose) {
		this.caseClose = caseClose;
	}

	public IrregularityPunish getPunish() {
		return punish;
	}

	public void setPunish(IrregularityPunish punish) {
		this.punish = punish;
	}

	public Long getProductPTypeId() {
		return productPTypeId;
	}

	public void setProductPTypeId(Long productPTypeId) {
		this.productPTypeId = productPTypeId;
	}
}
package com.otec.foodsafety.entity.enterprise;

import java.io.Serializable;

public class EnterpriseMaterialExt extends EnterpriseMaterial implements Serializable {
	// 变更编号
	private Integer changeId;
	// 审批状态 1.待审核；2.审核通过；3.审核不通过
	private String verifyStatus;
	// 变更的操作类型 1.新增；2.修改；3.删除
	private String changeType;
	// 变更 原因
	private String changeReason;

	public Integer getChangeId() {
		return changeId;
	}

	public void setChangeId(Integer changeId) {
		this.changeId = changeId;
	}

	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getChangeReason() {
		return changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}

}

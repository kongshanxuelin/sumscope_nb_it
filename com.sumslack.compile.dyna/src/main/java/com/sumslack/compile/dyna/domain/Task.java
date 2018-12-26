package com.sumslack.compile.dyna.domain;

import com.sumslack.compile.dyna.schedule.SharedSpace;

public class Task {
	private Long id;
	private String name;
	private String description;
	private String scheduleStr;
	private String code;
	private String codeType;
	private String version;
	private String runStatus;
	// 任务状态 0,停止 1運行
	private Integer status;
	private CodeInfo codeInfo = new CodeInfo();
	// 2:cron
	private Integer type=2;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getScheduleStr() {
		return scheduleStr;
	}
	public void setScheduleStr(String scheduleStr) {
		this.scheduleStr = scheduleStr;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getRunStatus() {
		return runStatus;
	}
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public CodeInfo codeInfo() {
		return codeInfo;
	}
	
	public void setMessage(String message) {
		SharedSpace.setTaskMessage(this.getId(), message) ;
	}
	
	public void updateError() {
		SharedSpace.updateError(this.getId());
	}

	public void updateSuccess() {
		SharedSpace.updateSuccess(this.getId());
	}

	public String getMessage() {
		return SharedSpace.getTaskMessage(this.getId());
	}
	
}

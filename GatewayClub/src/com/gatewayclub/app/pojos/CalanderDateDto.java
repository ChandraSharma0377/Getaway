package com.gatewayclub.app.pojos;

public class CalanderDateDto {

	private String fromDate;
	private String toDate;
	private String adminApproval;
	private String pbId;
	private String mobile;
	private String PbNoOfAdult;
	private String PbNoOfChildren;

	public CalanderDateDto(String fromDate, String toDate, String adminApproval,String pbId ,String mobile,String PbNoOfAdult,String PbNoOfChildren) {
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.adminApproval = adminApproval;
		this.pbId=pbId;
		this.mobile=mobile;
		this.PbNoOfAdult=PbNoOfAdult;
		this.PbNoOfChildren=PbNoOfChildren;
	}

	public String getFromDate() {
		return fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public String getAdminApproval() {
		return adminApproval;
	}

	public String getPbId() {
		return pbId;
	}

	public String getMobile() {
		return mobile;
	}

	public String getPbNoOfAdult() {
		return PbNoOfAdult;
	}

	public String getPbNoOfChildren() {
		return PbNoOfChildren;
	}

	@Override
	public String toString() {
		return "CalanderDateDto{" +
				"fromDate='" + fromDate + '\'' +
				", toDate='" + toDate + '\'' +
				", adminApproval='" + adminApproval + '\'' +
				", pbId='" + pbId + '\'' +
				", mobile='" + mobile + '\'' +
				", PbNoOfAdult='" + PbNoOfAdult + '\'' +
				", PbNoOfChildren='" + PbNoOfChildren + '\'' +
				'}';
	}
}

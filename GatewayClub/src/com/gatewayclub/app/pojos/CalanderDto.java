package com.gatewayclub.app.pojos;

import java.util.Date;

public class CalanderDto {

	private Date date;
	private CalanderDateDto calanderDateDto;

	public CalanderDto(Date date, CalanderDateDto calanderDateDto) {

		this.date = date;
		this.calanderDateDto = calanderDateDto;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public CalanderDateDto getCalanderDateDto() {
		return calanderDateDto;
	}

	public void setCalanderDateDto(CalanderDateDto calanderDateDto) {
		this.calanderDateDto = calanderDateDto;
	}

	@Override
	public String toString() {
		return "CalanderDto [date=" + date + ", calanderDateDto=" + calanderDateDto + "]";
	}

}

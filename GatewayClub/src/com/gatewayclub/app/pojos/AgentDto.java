package com.gatewayclub.app.pojos;

public class AgentDto {

	private String agentID;
	private String agentName;

	public AgentDto(String agentID, String agentName) {
		super();
		this.agentID = agentID;
		this.agentName = agentName;
	}

	public String getAgentID() {
		return agentID;
	}

	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	@Override
	public String toString() {
		return "AgentDto [agentID=" + agentID + ", agentName=" + agentName + "]";
	}

}

package com.luv2code.springboot.cruddemo.entity;

public class UserAccountWithAction extends UserAccount{

	//actionType=null, overvide the whole userModel on PUT
	//actionType=balance, just update teh balance 
	private String actionType;

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Override
	public String toString() {
		return "UserActionWithAction [actionType=" + actionType + "]";
	}
	
	
	
	
}

package com.qq.bean;

import java.io.Serializable;

/**
 * friendʵ��bean
 *
 * @author jzyqd_
 */
public class Friend implements Serializable {
	/**qq�û��˺�*/
    private String userAccount;
    /**qq�����˺�*/
    private String friendAccount;
    
	public String getFriendAccount() {
		return friendAccount;
	}
	public void setFriendAccount(String friendAccount) {
		this.friendAccount = friendAccount;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
}

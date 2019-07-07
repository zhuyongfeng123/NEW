package org.fkjava.weixin.library.domain;

import java.util.List;

import org.fkjava.commons.domain.User;

public class DebitList {
	private User user;
	private List<DebitItem> items;
	public List<DebitItem> getItems() {
		return items;
	}

	public void setItems(List<DebitItem> items) {
		this.items = items;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

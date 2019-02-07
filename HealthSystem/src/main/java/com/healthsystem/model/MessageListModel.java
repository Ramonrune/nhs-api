package com.healthsystem.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageListModel<E> extends MessageModel{

	private List<?> list = new ArrayList<>();



	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	
}

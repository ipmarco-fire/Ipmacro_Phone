package com.ipmacro.model;

import java.util.ArrayList;
import java.util.List;

public class ChannelType {
	private int id;
	private List<Channel> channelList;
	private String name;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Channel> getChannelList() {
		return channelList;
	}
	
	public ChannelType(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public void setChannelList(List<Channel> channelList) {
		this.channelList = channelList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addChild(Channel channel) {
		if(this.channelList== null){
			this.channelList = new ArrayList<Channel>();
		}
		this.channelList.add(channel);
	}
	
	
}

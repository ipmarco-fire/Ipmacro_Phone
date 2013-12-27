package com.ipmacro.model;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class Channel {
	private int id;
	private int isFree;
	private boolean isP2p;
	private String logo;
	private int mode;
	private String name;
	private String playUrl;
	private int sort;
	private int typeId;
	
	private String fileId;
	private Short port;
	private int peerid;
	private String ip;
	private String key;
	private String typeIds;
	private boolean state;
	
	
	
	public boolean getState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getTypeIds() {
		return typeIds;
	}
	public void setTypeIds(String typeIds) {
		this.typeIds = typeIds;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIsFree() {
		return isFree;
	}
	public Short getPort() {
		return port;
	}
	public void setPort(Short port) {
		this.port = port;
	}
	public int getPeerid() {
		return peerid;
	}
	public void setPeerid(int peerid) {
		this.peerid = peerid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setP2p(boolean isP2p) {
		this.isP2p = isP2p;
	}
	public void setIsFree(int isFree) {
		this.isFree = isFree;
	}
	public boolean getIsP2p() {
		return isP2p;
	}
	public void setIsP2p(boolean isP2p) {
		this.isP2p = isP2p;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
}

package com.example.project_book_selling_system.vo;

import java.util.List;
import java.util.Map;

public class Request {

	private String ISBN;
	private String book;
	private String auther;
	private int price;
	private Integer inventory;
	private Integer sales;
	private String keyValue;
	private List<String> keyValues;
	private Map<String, Integer> buyList;
	private boolean isCustomer;

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public String getAuther() {
		return auther;
	}

	public void setAuther(String auther) {
		this.auther = auther;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public Integer getSales() {
		return sales;
	}

	public void setSales(Integer sales) {
		this.sales = sales;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public List<String> getKeyValues() {
		return keyValues;
	}

	public void setKeyValues(List<String> keyValues) {
		this.keyValues = keyValues;
	}

	public Map<String, Integer> getBuyList() {
		return buyList;
	}

	public void setBuyList(Map<String, Integer> buyList) {
		this.buyList = buyList;
	}

	public boolean isCustomer() {
		return isCustomer;
	}

	public void setCustomer(boolean isCustomer) {
		this.isCustomer = isCustomer;
	}

}

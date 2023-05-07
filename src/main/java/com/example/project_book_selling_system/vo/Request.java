package com.example.project_book_selling_system.vo;

import java.util.List;
import java.util.Map;

import com.example.project_book_selling_system.entity.Book;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) // 不顯示值為null的key 和 value
public class Request {

	@JsonProperty("book_entity")
	private Book bookEntity;
	private String isbn;
	private String book;
	private String auther;
	private int price;
	private Integer inventory;
	private Integer sales;
	@JsonProperty("key_value")
	private String keyValue;
	@JsonProperty("key_values")
	private List<String> keyValues;
	@JsonProperty("buy_list")
	private Map<String, Integer> buyList;
	private boolean isCustomer;

	public Book getBookEntity() {
		return bookEntity;
	}

	public void setBookEntity(Book bookEntity) {
		this.bookEntity = bookEntity;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
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

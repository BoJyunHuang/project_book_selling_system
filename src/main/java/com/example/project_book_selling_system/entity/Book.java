package com.example.project_book_selling_system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "book")
@JsonInclude(JsonInclude.Include.NON_NULL) // 不顯示值為null的key 和 value
public class Book {

	@Id
	@Column(name = "ISBN")
	private String ISBN;

	@Column(name = "book")
	private String book;

	@Column(name = "auther")
	private String auther;

	@Column(name = "price")
	private int price;

	@Column(name = "inventory")
	private Integer inventory;

	@Column(name = "sales")
	private Integer sales = 0;

	@Column(name = "key_value")
	private String keyValue;

	public Book() {
		super();
	}

	public Book(String iSBN, String book, String auther, int price, Integer inventory, Integer sales, String keyValue) {
		super();
		ISBN = iSBN;
		this.book = book;
		this.auther = auther;
		this.price = price;
		this.inventory = inventory;
		this.sales = sales;
		this.keyValue = keyValue;
	}

	public Book(String iSBN, String book, String auther, int price, Integer inventory, Integer sales) {
		super();
		ISBN = iSBN;
		this.book = book;
		this.auther = auther;
		this.price = price;
		this.inventory = inventory;
		this.sales = sales;
	}

	public Book(String ISBN, String book, String auther, int price, Integer inventory, String keyValue) {
		super();
		this.ISBN = ISBN;
		this.book = book;
		this.auther = auther;
		this.price = price;
		this.inventory = inventory;
		this.keyValue = keyValue;
	}
	
	public Book(String iSBN, String book, String auther, int price, Integer inventory) {
		super();
		ISBN = iSBN;
		this.book = book;
		this.auther = auther;
		this.price = price;
		this.inventory = inventory;
	}
	
	public Book(String iSBN, String book, String auther, int price) {
		super();
		ISBN = iSBN;
		this.book = book;
		this.auther = auther;
		this.price = price;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
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

}

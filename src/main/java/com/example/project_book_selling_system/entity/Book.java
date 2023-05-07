package com.example.project_book_selling_system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "book")
@JsonInclude(JsonInclude.Include.NON_NULL) // 不顯示值為null的key 和 value
public class Book {

	@Id
	@Column(name = "isbn")
	private String isbn;

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
	@JsonProperty("key_value")
	private String keyValue;

	public Book() {
		super();
	}

	public Book(String isbn, String book, String auther, int price, Integer inventory, Integer sales, String keyValue) {
		super();
		this.isbn = isbn;
		this.book = book;
		this.auther = auther;
		this.price = price;
		this.inventory = inventory;
		this.sales = sales;
		this.keyValue = keyValue;
	}

	public Book(String isbn, String book, String auther, int price, Integer inventory, Integer sales) {
		super();
		this.isbn = isbn;
		this.book = book;
		this.auther = auther;
		this.price = price;
		this.inventory = inventory;
		this.sales = sales;
	}

	public Book(String isbn, String book, String auther, int price, Integer inventory, String keyValue) {
		super();
		this.isbn = isbn;
		this.book = book;
		this.auther = auther;
		this.price = price;
		this.inventory = inventory;
		this.keyValue = keyValue;
	}
	
	public Book(String isbn, String book, String auther, int price, Integer inventory) {
		super();
		this.isbn = isbn;
		this.book = book;
		this.auther = auther;
		this.price = price;
		this.inventory = inventory;
	}
	
	public Book(String isbn, String book, String auther, int price) {
		super();
		this.isbn = isbn;
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

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
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

package com.example.project_book_selling_system.vo;

import java.util.List;
import java.util.Map;

import com.example.project_book_selling_system.entity.Book;

public class Response {

	private String message;
	private Book book;
	private List<Book> bookList;
	private Map<String, Integer> buyList;
	private int total;

	public Response() {
		super();
	}

	public Response(String message) {
		super();
		this.message = message;
	}

	public Response(Book book, String message) {
		super();
		this.book = book;
		this.message = message;
	}

	public Response(List<Book> bookList, String message) {
		super();
		this.bookList = bookList;
		this.message = message;
	}

	public Response(Map<String, Integer> buyList, List<Book> bookList, int total, String message) {
		super();
		this.buyList = buyList;
		this.bookList = bookList;
		this.total = total;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public List<Book> getBookList() {
		return bookList;
	}

	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}

	public Map<String, Integer> getBuyList() {
		return buyList;
	}

	public void setBuyList(Map<String, Integer> buyList) {
		this.buyList = buyList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}

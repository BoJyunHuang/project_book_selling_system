package com.example.project_book_selling_system.service.ifs;

import java.util.Map;

import com.example.project_book_selling_system.vo.Request;
import com.example.project_book_selling_system.vo.Response;

public interface BookService {

	// 1.新增書籍
	public Response addBook(String ISBN, String book, String auther, int price, Integer inventory, String keyValue);

	// 2.書籍分類搜尋，只顯示書名、ISBN、作者、價格、庫存量
	public Response searchKeyValue(String keyValue);

	/*
	 * 3.書籍搜尋，透過書名或ISBN或作者。
	 * 消費者:只顯示書名、ISBN、作者、價格。 
	 * 書籍商:顯示書名、ISBN、作者、價格、銷售量、庫存量。
	 */
	public Response searchBook(boolean isCustomer, Request request);

	/*
	 * 4.更新書籍資料 
	 * 庫存量(進貨):顯示書名、ISBN、作者、價格、庫存量 
	 * 價格:顯示書名、ISBN、作者、價格、庫存量
	 * 分類:顯示書名、ISBN、作者、價格、分類
	 */
	public Response renewBookSaleInfo(Request request);

	// 5.書籍銷售，消費者購買(可買多本): 只顯示書名、ISBN、作者、價格、購買數量，購買總價格
	public Response bookSelling(Map<String, Integer> buyList);

	// 6.暢銷書排行榜(依照銷售量前5，排序) ，只顯示書名、ISBN、作者、價格
	public Response bestSellingInfo();

}

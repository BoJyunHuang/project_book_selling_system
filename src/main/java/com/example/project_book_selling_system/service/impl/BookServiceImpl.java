package com.example.project_book_selling_system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.project_book_selling_system.constants.RtnCode;
import com.example.project_book_selling_system.entity.Book;
import com.example.project_book_selling_system.repository.BookDao;
import com.example.project_book_selling_system.service.ifs.BookService;
import com.example.project_book_selling_system.vo.Request;
import com.example.project_book_selling_system.vo.Response;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookDao bookDao;

	@Override
	public Response addBook(String ISBN, String book, String auther, int price, Integer inventory, String keyValue) {
		// 防止輸入為空
		if (!StringUtils.hasText(ISBN) || !StringUtils.hasText(book) || !StringUtils.hasText(auther)
				|| !StringUtils.hasText(keyValue)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 輸入價格與庫存錯誤
		if (price < 0 || inventory == null || inventory < 0) {
			return new Response(RtnCode.INCORRECT.getMessage());
		}
		// 新增物件
		Integer sales = 0;
		int res = bookDao.insertBook(ISBN, book, auther, price, inventory, sales, keyValue);
		// 物件已存在
		if (res == 0) {
			return new Response(RtnCode.ALREADY_EXISTED.getMessage());
		}
		return new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response searchKeyValue(String keyValue) {
		// 防止輸入為空
		if (!StringUtils.hasText(keyValue)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 尋找書籍
		List<Book> res = bookDao.findByKeyValue(keyValue);
		// 物件不存在
		if (CollectionUtils.isEmpty(res)) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		return new Response(res, RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response searchBook(boolean isCustomer, Request request) {
		// 防止空輸入
		if (request == null) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		Book resBook = new Book();
		List<Book> res = new ArrayList<>();
		// 顧客狀況
		if (isCustomer) {
			// 用ISBN
			if (StringUtils.hasText(request.getISBN())) {
				resBook = bookDao.CustomerfindByISBN(request.getISBN());
				if (resBook == null) {
					return new Response(RtnCode.NOT_FOUND.getMessage());
				}
				return new Response(res, RtnCode.SUCCESS.getMessage());
			}
			// 用書名
			if (StringUtils.hasText(request.getBook())) {
				res = bookDao.CustomerfindByBook(request.getBook());
				if (CollectionUtils.isEmpty(res)) {
					return new Response(RtnCode.NOT_FOUND.getMessage());
				}
				return new Response(res, RtnCode.SUCCESS.getMessage());
			}
			// 用作者
			if (StringUtils.hasText(request.getAuther())) {
				res = bookDao.CustomerfindByAuther(request.getAuther());
				if (CollectionUtils.isEmpty(res)) {
					return new Response(RtnCode.NOT_FOUND.getMessage());
				}
				return new Response(res, RtnCode.SUCCESS.getMessage());
			}
		}
		// 書商狀況
		// 用ISBN
		if (StringUtils.hasText(request.getISBN())) {
			resBook = bookDao.SellerfindByISBN(request.getISBN());
			if (resBook == null) {
				return new Response(RtnCode.NOT_FOUND.getMessage());
			}
			return new Response(res, RtnCode.SUCCESS.getMessage());
		}
		// 用書名
		if (StringUtils.hasText(request.getBook())) {
			res = bookDao.SellerfindByBook(request.getBook());
			if (CollectionUtils.isEmpty(res)) {
				return new Response(RtnCode.NOT_FOUND.getMessage());
			}
			return new Response(res, RtnCode.SUCCESS.getMessage());
		}
		// 用作者
		if (StringUtils.hasText(request.getAuther())) {
			res = bookDao.SellerfindByAuther(request.getAuther());
			if (CollectionUtils.isEmpty(res)) {
				return new Response(RtnCode.NOT_FOUND.getMessage());
			}
			return new Response(res, RtnCode.SUCCESS.getMessage());
		}
		return new Response(RtnCode.INCORRECT.getMessage());
	}

	@Override
	public Response renewBookSaleInfo(Request request) {
		// 防止空輸入
		if (request == null || !StringUtils.hasText(request.getISBN())) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 確認存在
		Optional<Book> res = bookDao.findById(request.getISBN());
		if (res.isEmpty()) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// 修改價格
		if (request.getPrice() > 0) {
			if (res.get().getPrice() == request.getPrice()) {
				return new Response(RtnCode.ALREADY_EXISTED.getMessage());
			}
			res.get().setPrice(request.getPrice());
			bookDao.save(res.get());
			res.get().setKeyValue(null);
			return new Response(res.get(), RtnCode.SUCCESS.getMessage());
		}
		// 修改庫存(進貨)
		if (request.getInventory() != null && request.getInventory() > 0) {
			res.get().setPrice(res.get().getPrice() + request.getPrice());
			bookDao.save(res.get());
			res.get().setKeyValue(null);
			return new Response(res.get(), RtnCode.SUCCESS.getMessage());
		}
		// 修改分類(新增分類)
		if (StringUtils.hasText(request.getKeyValue())) {
			String newKeyValue = res.get().getKeyValue() + ", " + request.getKeyValue();
			res.get().setKeyValue(newKeyValue);
			bookDao.save(res.get());
			res.get().setInventory(null);
			return new Response(res.get(), RtnCode.SUCCESS.getMessage());
		}
		// 確認無修改
		return new Response(RtnCode.INCORRECT.getMessage());

	}

	@Override
	public Response bookSelling(Map<String, Integer> buyList) {
		// 防止輸入為0
		if (CollectionUtils.isEmpty(buyList)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 查詢欲買書籍
		List<String> ISBNs = new ArrayList<>();
		for (Entry<String, Integer> b : buyList.entrySet()) {
			ISBNs.add(b.getKey());
		}
		// 確認欲買書籍存在
		List<Book> res = bookDao.findAllById(ISBNs);
		if (CollectionUtils.isEmpty(res) || res.size() != ISBNs.size()) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// 計算價錢
		int total = 0;
		for (Book r : res) {
			total = r.getPrice() * buyList.get(r.getISBN()); // 計算價錢
			r.setInventory(r.getInventory() - buyList.get(r.getISBN())); // 扣除庫存
			r.setSales(buyList.get(r.getISBN())); // 新增銷售量
		}
		// 儲存資訊
		bookDao.saveAll(res);
		for (Book r:res) {
			r.setInventory(null);
			r.setSales(null);
			r.setKeyValue(null);
		}
		return new Response(buyList, res, total, RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response bestSellingInfo() {
		List<Book> res = bookDao.BestSellTop5();
		for (Book r:res) {
			r.setInventory(null);
			r.setSales(null);
			r.setKeyValue(null);
		}
		return new Response(res, RtnCode.SUCCESS.getMessage());
	}

}

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
		// �����J����
		if (!StringUtils.hasText(ISBN) || !StringUtils.hasText(book) || !StringUtils.hasText(auther)
				|| !StringUtils.hasText(keyValue)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// ��J����P�w�s���~
		if (price < 0 || inventory == null || inventory < 0) {
			return new Response(RtnCode.INCORRECT.getMessage());
		}
		// �s�W����
		Integer sales = 0;
		int res = bookDao.insertBook(ISBN, book, auther, price, inventory, sales, keyValue);
		// ����w�s�b
		if (res == 0) {
			return new Response(RtnCode.ALREADY_EXISTED.getMessage());
		}
		return new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response searchKeyValue(String keyValue) {
		// �����J����
		if (!StringUtils.hasText(keyValue)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// �M����y
		List<Book> res = bookDao.findByKeyValue(keyValue);
		// ���󤣦s�b
		if (CollectionUtils.isEmpty(res)) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		return new Response(res, RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response searchBook(boolean isCustomer, Request request) {
		// ����ſ�J
		if (request == null) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		Book resBook = new Book();
		List<Book> res = new ArrayList<>();
		// �U�Ȫ��p
		if (isCustomer) {
			// ��ISBN
			if (StringUtils.hasText(request.getISBN())) {
				resBook = bookDao.CustomerfindByISBN(request.getISBN());
				if (resBook == null) {
					return new Response(RtnCode.NOT_FOUND.getMessage());
				}
				return new Response(res, RtnCode.SUCCESS.getMessage());
			}
			// �ήѦW
			if (StringUtils.hasText(request.getBook())) {
				res = bookDao.CustomerfindByBook(request.getBook());
				if (CollectionUtils.isEmpty(res)) {
					return new Response(RtnCode.NOT_FOUND.getMessage());
				}
				return new Response(res, RtnCode.SUCCESS.getMessage());
			}
			// �Χ@��
			if (StringUtils.hasText(request.getAuther())) {
				res = bookDao.CustomerfindByAuther(request.getAuther());
				if (CollectionUtils.isEmpty(res)) {
					return new Response(RtnCode.NOT_FOUND.getMessage());
				}
				return new Response(res, RtnCode.SUCCESS.getMessage());
			}
		}
		// �ѰӪ��p
		// ��ISBN
		if (StringUtils.hasText(request.getISBN())) {
			resBook = bookDao.SellerfindByISBN(request.getISBN());
			if (resBook == null) {
				return new Response(RtnCode.NOT_FOUND.getMessage());
			}
			return new Response(res, RtnCode.SUCCESS.getMessage());
		}
		// �ήѦW
		if (StringUtils.hasText(request.getBook())) {
			res = bookDao.SellerfindByBook(request.getBook());
			if (CollectionUtils.isEmpty(res)) {
				return new Response(RtnCode.NOT_FOUND.getMessage());
			}
			return new Response(res, RtnCode.SUCCESS.getMessage());
		}
		// �Χ@��
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
		// ����ſ�J
		if (request == null || !StringUtils.hasText(request.getISBN())) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// �T�{�s�b
		Optional<Book> res = bookDao.findById(request.getISBN());
		if (res.isEmpty()) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// �ק����
		if (request.getPrice() > 0) {
			if (res.get().getPrice() == request.getPrice()) {
				return new Response(RtnCode.ALREADY_EXISTED.getMessage());
			}
			res.get().setPrice(request.getPrice());
			bookDao.save(res.get());
			res.get().setKeyValue(null);
			return new Response(res.get(), RtnCode.SUCCESS.getMessage());
		}
		// �ק�w�s(�i�f)
		if (request.getInventory() != null && request.getInventory() > 0) {
			res.get().setPrice(res.get().getPrice() + request.getPrice());
			bookDao.save(res.get());
			res.get().setKeyValue(null);
			return new Response(res.get(), RtnCode.SUCCESS.getMessage());
		}
		// �ק����(�s�W����)
		if (StringUtils.hasText(request.getKeyValue())) {
			String newKeyValue = res.get().getKeyValue() + ", " + request.getKeyValue();
			res.get().setKeyValue(newKeyValue);
			bookDao.save(res.get());
			res.get().setInventory(null);
			return new Response(res.get(), RtnCode.SUCCESS.getMessage());
		}
		// �T�{�L�ק�
		return new Response(RtnCode.INCORRECT.getMessage());

	}

	@Override
	public Response bookSelling(Map<String, Integer> buyList) {
		// �����J��0
		if (CollectionUtils.isEmpty(buyList)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// �d�߱��R���y
		List<String> ISBNs = new ArrayList<>();
		for (Entry<String, Integer> b : buyList.entrySet()) {
			ISBNs.add(b.getKey());
		}
		// �T�{���R���y�s�b
		List<Book> res = bookDao.findAllById(ISBNs);
		if (CollectionUtils.isEmpty(res) || res.size() != ISBNs.size()) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// �p�����
		int total = 0;
		for (Book r : res) {
			total = r.getPrice() * buyList.get(r.getISBN()); // �p�����
			r.setInventory(r.getInventory() - buyList.get(r.getISBN())); // �����w�s
			r.setSales(buyList.get(r.getISBN())); // �s�W�P��q
		}
		// �x�s��T
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

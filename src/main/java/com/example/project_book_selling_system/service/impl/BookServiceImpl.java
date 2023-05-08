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
	public Response addBook(String isbn, String book, String auther, int price, Integer inventory, String keyValue) {
		// 0.�����J���ũο��~
		if (!StringUtils.hasText(isbn) || !StringUtils.hasText(book) || !StringUtils.hasText(auther)
				|| !StringUtils.hasText(keyValue) || price < 0 || inventory == null || inventory < 0) {
			return new Response(RtnCode.INCORRECT.getMessage());
		}
		// 1.�s�W����
		return bookDao.insertBook(isbn, book, auther, price, inventory, 0, keyValue) == 0
				? new Response(RtnCode.ALREADY_EXISTED.getMessage())
				: new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response searchKeyValue(List<String> keyValues) {
		// 0.���ſ�J
		if (CollectionUtils.isEmpty(keyValues)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 1.��z�r��
		String totalKeyValues = "";
		for (int i = 0; i < keyValues.size(); i++) {
			totalKeyValues += i < keyValues.size() - 1 ? keyValues.get(i) + "|" : keyValues.get(i);
		}
		// 2.�M����y
		List<Book> res = bookDao.findByKeyValue(totalKeyValues);
		return CollectionUtils.isEmpty(res) ? new Response(RtnCode.NOT_FOUND.getMessage())
				: new Response(revealInfo(res, true, false, false), RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response searchBook(boolean isCustomer, String isbn, String book, String auther) {
		// 1.��ISBN�ήѦW�Χ@��
		List<Book> res = bookDao.searchBy(StringUtils.hasText(isbn) ? isbn : null,
				StringUtils.hasText(book) ? book : null, StringUtils.hasText(auther) ? auther : null);
		// 2.�̾ڨ����^��
		return CollectionUtils.isEmpty(res) ? new Response(RtnCode.NOT_FOUND.getMessage())
				: isCustomer ? new Response(revealInfo(res, false, false, false), RtnCode.SUCCESS.getMessage())
						: new Response(revealInfo(res, true, true, false), RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response renewBookSaleInfo(Book book) {
		// 0.����ſ�J
		if (book == null || !StringUtils.hasText(book.getIsbn())) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 1.�T�{�s�b
		Optional<Book> res = bookDao.findById(book.getIsbn());
		if (res.isEmpty()) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// 2-1.�ק����
		if (book.getPrice() > 0) {
			if (res.get().getPrice() == book.getPrice()) {
				return new Response(RtnCode.REPEAT.getMessage());
			}
			res.get().setPrice(book.getPrice());
			return new Response(hideInfo(bookDao.save(res.get()), false, true, true), RtnCode.SUCCESS.getMessage());
		}
		// 2-2.�ק�w�s(�i�f)
		if (book.getInventory() != null && book.getInventory() > 0) {
			res.get().setInventory(res.get().getInventory() + book.getInventory());
			return new Response(hideInfo(bookDao.save(res.get()), false, true, true), RtnCode.SUCCESS.getMessage());
		}
		// 2-3.�ק����(�s�W����)
		if (StringUtils.hasText(book.getKeyValue())) {
			res.get().setKeyValue(res.get().getKeyValue() + ", " + book.getKeyValue());
			return new Response(hideInfo(bookDao.save(res.get()), true, true, false), RtnCode.SUCCESS.getMessage());
		}
		// 3.�T�{�L�ק�
		return new Response(RtnCode.INCORRECT.getMessage());
	}

	@Override
	public Response bookSelling(Map<String, Integer> buyList) {
		// 0.�����J��0
		if (CollectionUtils.isEmpty(buyList)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 1.�L�o�Ŧr���A��X���R���yISBN (lambda��k)
		List<String> isbns = new ArrayList<>();
		buyList.entrySet().stream()
				.filter(b -> b.getKey() != null && !b.getKey().isEmpty() && b.getValue() != null && b.getValue() > 0)
				.forEach(b -> {
					isbns.add(b.getKey());
				});
		// 2.�T�{���R���y�s�b
		List<Book> res = bookDao.findAllById(isbns);
		if (CollectionUtils.isEmpty(res) || res.size() != isbns.size()) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// 3.�p�����
		int total = 0;
		for (Book r : res) {
			total += r.getPrice() * buyList.get(r.getIsbn()); // �p�����
			r.setInventory(r.getInventory() - buyList.get(r.getIsbn())); // �����w�s
			r.setSales(r.getSales() + buyList.get(r.getIsbn())); // �s�W�P��q
		}
		// 4.�x�s��T
		return new Response(buyList, revealInfo(bookDao.saveAll(res), false, false, false), total,
				RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response bestSellingInfo() {
		return new Response(revealInfo(bookDao.BestSellTop5(), false, false, false), RtnCode.SUCCESS.getMessage());
	}

	// �p����k:������>�w�s�B�P��B����
	private List<Book> revealInfo(List<Book> bookList, boolean isInventory, boolean isSales, boolean isKeyValue) {
		for (Book b : bookList) {
			b.setInventory(isInventory ? b.getInventory() : null);
			b.setSales(isSales ? b.getSales() : null);
			b.setKeyValue(isKeyValue ? b.getKeyValue() : null);
		}
		return bookList;
	}

	// �p����k:�������>�w�s�B�P��B����
	private Book hideInfo(Book book, boolean isInventory, boolean isSales, boolean isKeyValue) {
		book.setInventory(isInventory ? null : book.getInventory());
		book.setSales(isSales ? null : book.getSales());
		book.setKeyValue(isKeyValue ? null : book.getKeyValue());
		return book;
	}

}

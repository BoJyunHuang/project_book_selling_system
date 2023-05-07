package com.example.project_book_selling_system.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project_book_selling_system.service.ifs.BookService;
import com.example.project_book_selling_system.vo.Request;
import com.example.project_book_selling_system.vo.Response;

@RestController
public class Controller {

	@Autowired
	private BookService bookService;

	@PostMapping(value = "add_book")
	public Response addBook(@RequestBody Request request) {
		return bookService.addBook(request.getISBN(), request.getBook(), request.getAuther(), request.getPrice(),
				request.getInventory(), request.getKeyValue());
	}

	@GetMapping(value = "search_key_value")
	public Response searchKeyValue(@RequestBody Request request) {
		return bookService.searchKeyValue(request.getKeyValues());
	}

	@GetMapping(value = "search_book")
	public Response searchBook(@RequestBody Request request) {
		return bookService.searchBook(request.isCustomer(), request.getISBN(), request.getBook(), request.getAuther());
	}

	@GetMapping(value = "renew_book_sale_info")
	public Response renewBookSaleInfo(@RequestBody Request request) {
		return bookService.renewBookSaleInfo(request);
	}

	@GetMapping(value = "book_selling")
	public Response bookSelling(@RequestBody Request request) {
		return bookService.bookSelling(request.getBuyList());
	}

	@GetMapping(value = "best_selling_info")
	public Response bestSellingInfo() {
		return bookService.bestSellingInfo();
	}

}

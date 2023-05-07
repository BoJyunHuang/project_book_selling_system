package com.example.project_book_selling_system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.project_book_selling_system.constants.RtnCode;
import com.example.project_book_selling_system.entity.Book;
import com.example.project_book_selling_system.repository.BookDao;
import com.example.project_book_selling_system.service.ifs.BookService;
import com.example.project_book_selling_system.vo.Request;
import com.example.project_book_selling_system.vo.Response;

@SpringBootTest(classes = ProjectBookSellingSystemApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookDaoAndServiceTests {

	@Autowired
	private BookDao bDao;

	@Autowired
	private BookService bSer;

	@BeforeEach
	private void BeforeEach() {
		// 建立測試資料
		Book b1 = new Book("ISBN001", "Book1", "auther1", 1000, 200, "class1");
		Book b2 = new Book("ISBN002", "Book2", "auther2", 1000, 150, 50, "class1, class2");
		Book b3 = new Book("ISBN003", "Book3", "auther2", 800, 50, 250, "class3");
		Book b4 = new Book("ISBN004", "Book1", "auther3", 1200, 250, 250, "class4");
		Book b5 = new Book("ISBN005", "Book4", "auther4", 500, 450, 850, "class5");
		Book b6 = new Book("ISBN006", "Book5", "auther5", 200, 1250, 5250, "class2");
		Book b7 = new Book("ISBN007", "Book6", "auther6", 5200, 30, 10, "class1");
		List<Book> bList = new ArrayList<>(Arrays.asList(b1, b2, b3, b4, b5, b6, b7));
		bDao.saveAll(bList);
	}

	@AfterAll
	private void AfterAll() {
		// 刪除測試資料
		List<String> bList = new ArrayList<>(
				Arrays.asList("ISBN001", "ISBN002", "ISBN003", "ISBN004", "ISBN005", "ISBN006", "ISBN007"));
		bDao.deleteAllById(bList);
	}

	@Test
	void insertBookTest() {
		// 物件已存在
		int res1 = bDao.insertBook("ISBN002", "Book2", "auther2", 1000, 150, 50, "class1, class2");
		Assert.isTrue(res1 == 0, RtnCode.TEST1_ERROR.getMessage());
		// 儲存成功
		int res2 = bDao.insertBook("ISBN010", "Book10", "auther1", 1000, 150, 50, "class1, class2");
		Assert.isTrue(res2 == 1, RtnCode.TEST2_ERROR.getMessage());
		bDao.deleteById("ISBN010");
	}

	@Test
	void findByKeyValueTest() {
		// 查詢class1
		List<Book> res1 = bDao.findByKeyValue("class1|class4|class5");
		Assert.isTrue(res1.size() == 5, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	void searchByTest() {
		// 查詢空白
		List<Book> res1 = bDao.searchBy("ISBNXXX", "", null);
		Assert.isTrue(res1.isEmpty(), RtnCode.TEST1_ERROR.getMessage());
		// 查詢ISBN002及Book1
		List<Book> res2 = bDao.searchBy("ISBN002", "Book1", "");
		Assert.isTrue(res2.size() == 3, RtnCode.TEST2_ERROR.getMessage());
		// 多查詢authew3
		List<Book> res3 = bDao.searchBy("ISBN002", "Book1", "auther3");
		Assert.isTrue(res3.size() == 3, RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	void BestSellTop5Test() {
		// 查詢authew3
		List<Book> res1 = bDao.BestSellTop5();
		Assert.isTrue(res1.size() == 5, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	void addBookTest() {
		// 狀況:輸入空白
		Response res1 = bSer.addBook(null, "BookTest", "Me", 500, 200, "TestClass");
		Assert.isTrue(res1.getMessage().equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:輸入價格錯誤
		Response res2 = bSer.addBook("ISBNTest", "BookTest", "Me", -500, 200, "TestClass");
		Assert.isTrue(res2.getMessage().equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:輸入物件已存在-ISBN001
		Response res3 = bSer.addBook("ISBN001", "BookTest", "Me", 500, 200, "TestClass");
		Assert.isTrue(res3.getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// 狀況:成功
		Response res4 = bSer.addBook("ISBNTest", "BookTest", "Me", 500, 200, "TestClass");
		Assert.isTrue(res4.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST4_ERROR.getMessage());
		bDao.deleteById("ISBNTest");
	}

	@Test
	void searchKeyValueTest() {
		// 狀況:輸入空白
		Response res1 = bSer.searchKeyValue(null);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:無此分類
		List<String> keyValues1 = new ArrayList<>(Arrays.asList("classX"));
		Response res2 = bSer.searchKeyValue(keyValues1);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功
		List<String> keyValues2 = new ArrayList<>(Arrays.asList("class1", "class2"));
		Response res3 = bSer.searchKeyValue(keyValues2);
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	void searchBookCustmerTest() {
		// 狀況:輸入空
		Response res1 = bSer.searchBook(true, null, null, null);
		Assert.isTrue(res1.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:ISBN不存在
		Response res2 = bSer.searchBook(true, "ISBNTest", null, null);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:書名不存在
		Response res3 = bSer.searchBook(true, null, "BookTest", null);
		Assert.isTrue(res3.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// 狀況:作者不存在
		Response res4 = bSer.searchBook(true, null, null, "AutherTest");
		Assert.isTrue(res4.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST4_ERROR.getMessage());
		// 狀況:書名查找成功
		Response res5 = bSer.searchBook(true, null, "Book1", null);
		Assert.isTrue(res5.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST5_ERROR.getMessage());
	}

	@Test
	void searchBookSellerTest() {
		// 狀況:輸入內容空白
		Response res1 = bSer.searchBook(false, "","","");
		Assert.isTrue(res1.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:ISBN不存在
		Response res2 = bSer.searchBook(false, "ISBNTest","","");
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:書名不存在
		Response res3 = bSer.searchBook(false, "","BookTest","");
		Assert.isTrue(res3.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// 狀況:作者不存在
		Response res4 = bSer.searchBook(false, "","","AutherTest");
		Assert.isTrue(res4.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST4_ERROR.getMessage());
		// 狀況:作者查找成功
		Response res5 = bSer.searchBook(false, "","","auther2");
		Assert.isTrue(res5.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST5_ERROR.getMessage());
	}

	@Test
	void renewBookSaleInfo1Test() {
		// 狀況:輸入內容空白
		Request request = new Request();
		Response res1 = bSer.renewBookSaleInfo(request);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:ISBN不存在
		request.setISBN("ISBNTest");
		Response res2 = bSer.renewBookSaleInfo(request);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:無修改任何資訊
		request.setISBN("ISBN001");
		Response res3 = bSer.renewBookSaleInfo(request);
		Assert.isTrue(res3.getMessage().equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	void renewBookSaleInfo2Test() {
		// 狀況:輸入價錢相同
		Request request = new Request();
		request.setISBN("ISBN001");
		request.setPrice(1000);
		Response res1 = bSer.renewBookSaleInfo(request);
		Assert.isTrue(res1.getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:改價成功
		request.setPrice(1500);
		Response res2 = bSer.renewBookSaleInfo(request);
		Assert.isTrue(res2.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:無修改任何資訊
		request.setPrice(0);
		Response res3 = bSer.renewBookSaleInfo(request);
		Assert.isTrue(res3.getMessage().equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}
	
	@Test
	void bookSellingTest() {
		Map<String, Integer> buyList = new HashMap<>();
		// 狀況:輸入空
		Response res1 = bSer.bookSelling(buyList);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:部分書籍不存在
		buyList.put(null, null);
		buyList.put("ISBN002", 5);
		buyList.put("ISBN006", 120);
		buyList.put("ISBN008", 10);
		Response res2 = bSer.bookSelling(buyList);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:購買成功
		buyList.remove("ISBN008");
		Response res3 = bSer.bookSelling(buyList);
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}
}

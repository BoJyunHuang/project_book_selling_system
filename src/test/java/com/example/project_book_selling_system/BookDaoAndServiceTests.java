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
		bDao.saveAll(new ArrayList<>(Arrays.asList(new Book("ISBN001", "Book1", "auther1", 1000, 200, "class1"),
				new Book("ISBN002", "Book2", "auther2", 1000, 150, 50, "class1, class2"),
				new Book("ISBN003", "Book3", "auther2", 800, 50, 250, "class3"),
				new Book("ISBN004", "Book1", "auther3", 1200, 250, 250, "class4"),
				new Book("ISBN005", "Book4", "auther4", 500, 450, 850, "class5"),
				new Book("ISBN006", "Book5", "auther5", 200, 1250, 5250, "class2"),
				new Book("ISBN007", "Book6", "auther6", 5200, 30, 10, "class1"))));
	}

	@AfterAll
	private void AfterAll() {
		// 刪除測試資料
		bDao.deleteAllById(new ArrayList<>(
				Arrays.asList("ISBN001", "ISBN002", "ISBN003", "ISBN004", "ISBN005", "ISBN006", "ISBN007")));
	}

	@Test
	void insertBookTest() {
		// 物件已存在(null會出錯，""會儲存)
		Assert.isTrue(bDao.insertBook("ISBN002", "Book2", "auther2", 1000, 150, 50, "class1, class2") == 0,
				RtnCode.TEST1_ERROR.getMessage());
		// 儲存成功
		Assert.isTrue(bDao.insertBook("ISBN010", "Book10", "auther1", 1000, 150, 50, "class1, class2") == 1,
				RtnCode.TEST2_ERROR.getMessage());
		bDao.deleteById("ISBN010");
	}

	@Test
	void findByKeyValueTest() {
		// 查詢class
		Assert.isTrue(bDao.findByKeyValue("class1|class4|class5").size() == 5, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	void searchByTest() {
		// 查詢空白(不能""，會導致搜尋到全部，要注意)
		Assert.isTrue(bDao.searchBy("ISBNXXX", null, null).isEmpty(), RtnCode.TEST1_ERROR.getMessage());
		// 查詢ISBN002及Book1
		Assert.isTrue(bDao.searchBy("ISBN002", "Book1", "").size() == 3, RtnCode.TEST2_ERROR.getMessage());
		// 多查詢authew3
		Assert.isTrue(bDao.searchBy("ISBN002", "Book1", "auther3").size() == 3, RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	void BestSellTop5Test() {
		// 查詢銷售榜
		Assert.isTrue(bDao.BestSellTop5().size() == 5, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	void addBookTest() {
		// 狀況:輸入空白
		Assert.isTrue(bSer.addBook(null, "BookTest", "Me", 500, 200, "TestClass").getMessage()
				.equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:輸入價格錯誤
		Assert.isTrue(bSer.addBook("ISBNTest", "BookTest", "Me", -500, 200, "TestClass").getMessage()
				.equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:輸入物件已存在-ISBN001
		Assert.isTrue(bSer.addBook("ISBN001", "BookTest", "Me", 500, 200, "TestClass").getMessage()
				.equals(RtnCode.ALREADY_EXISTED.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// 狀況:成功
		Assert.isTrue(bSer.addBook("ISBNTest", "BookTest", "Me", 500, 200, "TestClass").getMessage()
				.equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST4_ERROR.getMessage());
		bDao.deleteById("ISBNTest");
	}

	@Test
	void searchKeyValueTest() {
		// 狀況:輸入空白
		Assert.isTrue(bSer.searchKeyValue(null).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:無此分類
		Assert.isTrue(bSer.searchKeyValue(new ArrayList<>(Arrays.asList("classX"))).getMessage()
				.equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功
		Assert.isTrue(bSer.searchKeyValue(new ArrayList<>(Arrays.asList("class1", "class2"))).getMessage()
				.equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	void searchBookTest() {
		// 狀況:輸入空
		Assert.isTrue(bSer.searchBook(true, null, null, null).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:ISBN不存在
		Assert.isTrue(bSer.searchBook(true, "ISBNTest", null, null).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 狀況:書名不存在
		Assert.isTrue(bSer.searchBook(true, null, "BookTest", null).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 狀況:作者不存在
		Assert.isTrue(bSer.searchBook(true, null, null, "AuthTest").getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
		// 狀況:書名查找成功
		Assert.isTrue(bSer.searchBook(true, null, "Book1", null).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST5_ERROR.getMessage());
	}

	@Test
	void renewBookSaleInfoTest() {
		// 狀況:輸入內容空白
		Assert.isTrue(bSer.renewBookSaleInfo(new Book()).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:ISBN不存在
		Assert.isTrue(bSer.renewBookSaleInfo(new Book("ISBNTest", null, null, 0)).getMessage()
				.equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:無修改任何資訊
		Assert.isTrue(bSer.renewBookSaleInfo(new Book("ISBN001", null, null, 0)).getMessage()
				.equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:輸入價錢相同
		Assert.isTrue(bSer.renewBookSaleInfo(new Book("ISBN001", null, null, 1000)).getMessage()
				.equals(RtnCode.REPEAT.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:改價成功
		Assert.isTrue(bSer.renewBookSaleInfo(new Book("ISBN001", null, null, 1500)).getMessage()
				.equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:無修改任何資訊
		Assert.isTrue(bSer.renewBookSaleInfo(new Book("ISBN001", null, null, 0)).getMessage()
				.equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

	@SuppressWarnings("serial")
	@Test
	void bookSellingTest() {
		// 狀況:輸入空
		Assert.isTrue(bSer.bookSelling(new HashMap<>()).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:部分書籍不存在
		Assert.isTrue(bSer.bookSelling(new HashMap<>() {
			{
				put(null, null);
				put("ISBN002", 5);
				put("ISBN006", 120);
				put("ISBN008", 10);
			}
		}).getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:購買成功
		Assert.isTrue(bSer.bookSelling(new HashMap<>() {
			{
				put(null, null);
				put("ISBN002", 5);
				put("ISBN006", 120);
			}
		}).getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}
}

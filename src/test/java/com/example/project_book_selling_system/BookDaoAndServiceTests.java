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
		// �إߴ��ո��
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
		// �R�����ո��
		bDao.deleteAllById(new ArrayList<>(
				Arrays.asList("ISBN001", "ISBN002", "ISBN003", "ISBN004", "ISBN005", "ISBN006", "ISBN007")));
	}

	@Test
	void insertBookTest() {
		// ����w�s�b(null�|�X���A""�|�x�s)
		Assert.isTrue(bDao.insertBook("ISBN002", "Book2", "auther2", 1000, 150, 50, "class1, class2") == 0,
				RtnCode.TEST1_ERROR.getMessage());
		// �x�s���\
		Assert.isTrue(bDao.insertBook("ISBN010", "Book10", "auther1", 1000, 150, 50, "class1, class2") == 1,
				RtnCode.TEST2_ERROR.getMessage());
		bDao.deleteById("ISBN010");
	}

	@Test
	void findByKeyValueTest() {
		// �d��class
		Assert.isTrue(bDao.findByKeyValue("class1|class4|class5").size() == 5, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	void searchByTest() {
		// �d�ߪť�(����""�A�|�ɭP�j�M������A�n�`�N)
		Assert.isTrue(bDao.searchBy("ISBNXXX", null, null).isEmpty(), RtnCode.TEST1_ERROR.getMessage());
		// �d��ISBN002��Book1
		Assert.isTrue(bDao.searchBy("ISBN002", "Book1", "").size() == 3, RtnCode.TEST2_ERROR.getMessage());
		// �h�d��authew3
		Assert.isTrue(bDao.searchBy("ISBN002", "Book1", "auther3").size() == 3, RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	void BestSellTop5Test() {
		// �d�߾P��]
		Assert.isTrue(bDao.BestSellTop5().size() == 5, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	void addBookTest() {
		// ���p:��J�ť�
		Assert.isTrue(bSer.addBook(null, "BookTest", "Me", 500, 200, "TestClass").getMessage()
				.equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// ���p:��J������~
		Assert.isTrue(bSer.addBook("ISBNTest", "BookTest", "Me", -500, 200, "TestClass").getMessage()
				.equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:��J����w�s�b-ISBN001
		Assert.isTrue(bSer.addBook("ISBN001", "BookTest", "Me", 500, 200, "TestClass").getMessage()
				.equals(RtnCode.ALREADY_EXISTED.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// ���p:���\
		Assert.isTrue(bSer.addBook("ISBNTest", "BookTest", "Me", 500, 200, "TestClass").getMessage()
				.equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST4_ERROR.getMessage());
		bDao.deleteById("ISBNTest");
	}

	@Test
	void searchKeyValueTest() {
		// ���p:��J�ť�
		Assert.isTrue(bSer.searchKeyValue(null).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:�L������
		Assert.isTrue(bSer.searchKeyValue(new ArrayList<>(Arrays.asList("classX"))).getMessage()
				.equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:���\
		Assert.isTrue(bSer.searchKeyValue(new ArrayList<>(Arrays.asList("class1", "class2"))).getMessage()
				.equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	void searchBookTest() {
		// ���p:��J��
		Assert.isTrue(bSer.searchBook(true, null, null, null).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:ISBN���s�b
		Assert.isTrue(bSer.searchBook(true, "ISBNTest", null, null).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// ���p:�ѦW���s�b
		Assert.isTrue(bSer.searchBook(true, null, "BookTest", null).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// ���p:�@�̤��s�b
		Assert.isTrue(bSer.searchBook(true, null, null, "AuthTest").getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
		// ���p:�ѦW�d�䦨�\
		Assert.isTrue(bSer.searchBook(true, null, "Book1", null).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST5_ERROR.getMessage());
	}

	@Test
	void renewBookSaleInfoTest() {
		// ���p:��J���e�ť�
		Assert.isTrue(bSer.renewBookSaleInfo(new Book()).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:ISBN���s�b
		Assert.isTrue(bSer.renewBookSaleInfo(new Book("ISBNTest", null, null, 0)).getMessage()
				.equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:�L�ק�����T
		Assert.isTrue(bSer.renewBookSaleInfo(new Book("ISBN001", null, null, 0)).getMessage()
				.equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:��J�����ۦP
		Assert.isTrue(bSer.renewBookSaleInfo(new Book("ISBN001", null, null, 1000)).getMessage()
				.equals(RtnCode.REPEAT.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// ���p:������\
		Assert.isTrue(bSer.renewBookSaleInfo(new Book("ISBN001", null, null, 1500)).getMessage()
				.equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:�L�ק�����T
		Assert.isTrue(bSer.renewBookSaleInfo(new Book("ISBN001", null, null, 0)).getMessage()
				.equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

	@SuppressWarnings("serial")
	@Test
	void bookSellingTest() {
		// ���p:��J��
		Assert.isTrue(bSer.bookSelling(new HashMap<>()).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:�������y���s�b
		Assert.isTrue(bSer.bookSelling(new HashMap<>() {
			{
				put(null, null);
				put("ISBN002", 5);
				put("ISBN006", 120);
				put("ISBN008", 10);
			}
		}).getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:�ʶR���\
		Assert.isTrue(bSer.bookSelling(new HashMap<>() {
			{
				put(null, null);
				put("ISBN002", 5);
				put("ISBN006", 120);
			}
		}).getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}
}

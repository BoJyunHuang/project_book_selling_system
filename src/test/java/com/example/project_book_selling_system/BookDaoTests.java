package com.example.project_book_selling_system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.project_book_selling_system.entity.Book;
import com.example.project_book_selling_system.repository.BookDao;

@SpringBootTest(classes = ProjectBookSellingSystemApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookDaoTests {

	@Autowired
	private BookDao bDao;
	
	@BeforeEach
	private void BeforeEach() {
		Book b1 = new Book("ISBN001","Book1","auther1",1000,200,"class1");
		Book b2 = new Book("ISBN002","Book2","auther2",1000,150,50,"class1, class2");
		List<Book> bList = new ArrayList<>(Arrays.asList(b1, b2));
		bDao.saveAll(bList);
	}
	
	@AfterAll
	private void AfterAll() {
		List<String> bList = new ArrayList<>(Arrays.asList("ISBN001", "ISBN002"));
		bDao.deleteAllById(bList);
	}
	
	@Test
	void insertBookTest() {
		// 物件已存在
		
	}

}

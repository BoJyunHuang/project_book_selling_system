package com.example.project_book_selling_system.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project_book_selling_system.entity.Book;

@Repository
public interface BookDao extends JpaRepository<Book, String> {

	// 新增書籍(Insert)
	@Transactional
	@Modifying
	@Query(value = "insert into book (isbn, book, auther, price, inventory, sales, key_value) "
			+ "select :isbn, :book, :auther, :price, :inventory, :sales, :keyValue "
			+ "where not exists (select 1 from book where isbn = :isbn)", nativeQuery = true)
	public int insertBook(@Param("isbn") String isbn, @Param("book") String book, @Param("auther") String auther,
			@Param("price") int price, @Param("inventory") int inventory, @Param("sales") int sales,
			@Param("keyValue") String keyValue);

	// 透過分類尋找書籍
	@Query(value = "select * from book b where b.key_value regexp :inputKeyValue", nativeQuery = true)
	public List<Book> findByKeyValue(@Param("inputKeyValue") String keyValue);

	// 透過條件尋找書籍
	@Query(value = "select * from book b where b.isbn = :isbn or b.book = :Book or b.auther = :Auther", nativeQuery = true)
	public List<Book> searchBy(@Param("isbn") String isbn, @Param("Book") String book,
			@Param("Auther") String auther);

	// 最佳銷售前五名
	@Query(value = "select * from book b order by b.sales desc limit 5", nativeQuery = true)
	public List<Book> BestSellTop5();

}

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

	// �s�W���y(Insert)
	@Transactional
	@Modifying
	@Query(value = "insert into book (ISBN, book, auther, price, inventory, sales, key_value) select "
			+ ":inputISBN, :inputBook, :inputAuther, :inputPrice, :inputInventory, :inputSales, :inputKeyValue "
			+ "where not exists (select 1 from book where ISBN = :inputISBN)", nativeQuery = true)
	public int insertBook(
			@Param("inputISBN") String ISBN, 
			@Param("inputBook") String book,
			@Param("inputAuther") String auther, 
			@Param("inputPrice") int price, 
			@Param("inputInventory") int inventory,
			@Param("inputSales") int sales, 
			@Param("inputKeyValue") String keyValue);

	// �z�L�����M����y
	@Query("select new Book (b.ISBN, b.book, b.auther, b.price, b.inventory) from Book b "
			+ "where b.keyValue like concat('%' , :inputkeyValue , '%')")
	public List<Book> findByKeyValue(@Param("inputKeyValue") String keyValue);
	
	// �U�ȳz�LISBN�M����y
	@Query("select new Book (b.ISBN, b.book, b.auther, b.price) from Book b "
			+ "where b.ISBN = :inputISBN")
	public Book CustomerfindByISBN(@Param("inputISBN") String ISBN);
	
	// �U�ȳz�L�ѦW�M����y
	@Query("select new Book (b.ISBN, b.book, b.auther, b.price) from Book b "
			+ "where b.book = :inputBook")
	public List<Book> CustomerfindByBook(@Param("inputBook") String book);
	
	// �U�ȳz�L�@�̴M����y
	@Query("select new Book (b.ISBN, b.book, b.auther, b.price) from Book b "
			+ "where b.auther = :inputAuther")
	public List<Book> CustomerfindByAuther(@Param("inputAuther") String auther);
	
	// �Ѱӳz�LISBN�M����y
	@Query("select new Book (b.ISBN, b.book, b.auther, b.price, b.inventory, b.sales) from Book b "
			+ "where b.ISBN = :inputISBN")
	public Book SellerfindByISBN(@Param("inputISBN") String ISBN);
	
	// �Ѱӳz�L�ѦW�M����y
	@Query("select new Book (b.ISBN, b.book, b.auther, b.price, b.inventory, b.sales) from Book b "
			+ "where b.book = :inputBook")
	public List<Book> SellerfindByBook(@Param("inputBook") String book);
	
	// �Ѱӳz�L�@�̴M����y
	@Query("select new Book (b.ISBN, b.book, b.auther, b.price, b.inventory, b.sales) from Book b "
			+ "where b.auther = :inputAuther")
	public List<Book> SellerfindByAuther(@Param("inputAuther") String auther);
	
	// �Ѱӳz�L�@�̴M����y
	@Query(value = "select * from book b order by b.sales desc limit 5", nativeQuery = true)
	public List<Book> BestSellTop5();
	
}

package com.example.project_book_selling_system.service.ifs;

import java.util.List;
import java.util.Map;

import com.example.project_book_selling_system.entity.Book;
import com.example.project_book_selling_system.vo.Response;

public interface BookService {

	// 1.�s�W���y
	public Response addBook(String isbn, String book, String auther, int price, Integer inventory, String keyValue);

	// 2.���y�����j�M�A�u��ܮѦW�Bisbn�B�@�̡B����B�w�s�q
	public Response searchKeyValue(List<String> keyValueList);

	/*
	 * 3.���y�j�M�A�z�L�ѦW��ISBN�Χ@�̡C
	 * ���O��:�u��ܮѦW�Bisbn�B�@�̡B����C 
	 * ���y��:��ܮѦW�Bisbn�B�@�̡B����B�P��q�B�w�s�q�C
	 */
	public Response searchBook(boolean isCustomer, String isbn, String book, String auther);

	/*
	 * 4.��s���y��� 
	 * �w�s�q(�i�f):��ܮѦW�Bisbn�B�@�̡B����B�w�s�q 
	 * ����:��ܮѦW�Bisbn�B�@�̡B����B�w�s�q
	 * ����:��ܮѦW�Bisbn�B�@�̡B����B����
	 */
	public Response renewBookSaleInfo(Book book);

	// 5.���y�P��A���O���ʶR(�i�R�h��): �u��ܮѦW�Bisbn�B�@�̡B����B�ʶR�ƶq�A�ʶR�`����
	public Response bookSelling(Map<String, Integer> buyList);

	// 6.�Z�P�ѱƦ�](�̷ӾP��q�e5�A�Ƨ�) �A�u��ܮѦW�Bisbn�B�@�̡B����
	public Response bestSellingInfo();

}

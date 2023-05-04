package com.example.project_book_selling_system.service.ifs;

import java.util.Map;

import com.example.project_book_selling_system.vo.Request;
import com.example.project_book_selling_system.vo.Response;

public interface BookService {

	// 1.�s�W���y
	public Response addBook(String ISBN, String book, String auther, int price, Integer inventory, String keyValue);

	// 2.���y�����j�M�A�u��ܮѦW�BISBN�B�@�̡B����B�w�s�q
	public Response searchKeyValue(String keyValue);

	/*
	 * 3.���y�j�M�A�z�L�ѦW��ISBN�Χ@�̡C
	 * ���O��:�u��ܮѦW�BISBN�B�@�̡B����C 
	 * ���y��:��ܮѦW�BISBN�B�@�̡B����B�P��q�B�w�s�q�C
	 */
	public Response searchBook(boolean isCustomer, Request request);

	/*
	 * 4.��s���y��� 
	 * �w�s�q(�i�f):��ܮѦW�BISBN�B�@�̡B����B�w�s�q 
	 * ����:��ܮѦW�BISBN�B�@�̡B����B�w�s�q
	 * ����:��ܮѦW�BISBN�B�@�̡B����B����
	 */
	public Response renewBookSaleInfo(Request request);

	// 5.���y�P��A���O���ʶR(�i�R�h��): �u��ܮѦW�BISBN�B�@�̡B����B�ʶR�ƶq�A�ʶR�`����
	public Response bookSelling(Map<String, Integer> buyList);

	// 6.�Z�P�ѱƦ�](�̷ӾP��q�e5�A�Ƨ�) �A�u��ܮѦW�BISBN�B�@�̡B����
	public Response bestSellingInfo();

}

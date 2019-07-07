package org.fkjava.weixin.library.service.impl;

import java.util.LinkedList;

import org.fkjava.weixin.library.dao.BookRepository;
import org.fkjava.weixin.library.domain.Book;
import org.fkjava.weixin.library.domain.DebitItem;
import org.fkjava.weixin.library.domain.DebitList;
import org.fkjava.weixin.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LibraryServiceImpl implements LibraryService {
	@Autowired
	private BookRepository bookRepository;
	@Override
	public Page<Book> search(String keyword, int pageNumber) {
	Pageable pageable = PageRequest.of(pageNumber, 3);
	Page<Book> page;
	if (StringUtils.isEmpty(keyword)) {
	page = this.bookRepository.findAll(pageable);
	} else {
	page = this.bookRepository.findByNameContaining(keyword, pageable);}
	return page;}
	@Override
	public void add(DebitList list, String bookId) {
	if (list.getItems() == null) {
	list.setItems(new LinkedList<>());}
	Book book = this.bookRepository.findById(bookId).get();
	boolean exists = false;
	for (DebitItem b : list.getItems()) {
	if (b.getBook().getId().equals(bookId)) {
	exists = true;}}
	if (!exists) {
	DebitItem item = new DebitItem();
	item.setBook(book);
	list.getItems().add(item);}}
	@Override
	public void remove(DebitList list, String id) {
	list.getItems().stream()//
	.filter(item -> item.getBook().getId().equals(id))//
	.findFirst()//
	.ifPresent(item -> list.getItems().remove(item));}}

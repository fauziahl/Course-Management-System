package com.juaracoding.TA.service;


import com.juaracoding.TA.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    void saveAll(List<Book> books);
}
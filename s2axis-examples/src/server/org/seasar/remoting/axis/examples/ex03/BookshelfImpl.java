/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.remoting.axis.examples.ex03;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author koichik
 */
public class BookshelfImpl implements Bookshelf {
    private Map books = new HashMap();

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public Book getBook(String isbn) {
        return (Book) books.get(isbn);
    }

    public Book[] getBooks() {
        Collection values = books.values();
        return (Book[]) values.toArray(new Book[values.size()]);
    }
}

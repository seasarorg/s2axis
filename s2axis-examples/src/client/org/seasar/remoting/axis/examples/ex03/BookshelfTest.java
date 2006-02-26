/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 */
public class BookshelfTest extends S2TestCase {
    public BookshelfTest(String name) {
        super(name);
    }

    public void setUp() {
        include("BookshelfTest.dicon");
    }

    public void test() throws Exception {
        Bookshelf bookshelf = (Bookshelf) getComponent(Bookshelf.class);

        assertEquals(0, bookshelf.getBooks().length);

        bookshelf.addBook(new Book("Building Web Services with Java", "Steve Graham, et al.",
                "ISBN0-672-32641-8"));
        bookshelf.addBook(new Book("Designing Web Services with the J2EE 1.4 Platform",
                "Inderjeet Singh, et al.", "ISBN0-321-20521-9"));

        Book book = bookshelf.getBook("ISBN0-672-32641-8");
        assertEquals("Building Web Services with Java", book.getTitle());
        assertEquals("Steve Graham, et al.", book.getAuthor());
        assertEquals("ISBN0-672-32641-8", book.getIsbn());

        book = bookshelf.getBook("ISBN0-321-20521-9");
        assertEquals("Designing Web Services with the J2EE 1.4 Platform", book.getTitle());
        assertEquals("Inderjeet Singh, et al.", book.getAuthor());
        assertEquals("ISBN0-321-20521-9", book.getIsbn());
    }
}

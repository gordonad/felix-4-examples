package com.packtpub.felix.bookshelf.inventory.impl.mock;

import com.packtpub.felix.bookshelf.inventory.api.MutableBook;

public class MutableBookImpl implements MutableBook {
    String isbn;

    String author;
    String title;
    String group;

    int grade;

    transient String _toString = null;

    public MutableBookImpl(String isbn) {
        setISBN(isbn);
    }

    @Override
	public void setISBN(String isbn) {
        this.isbn = isbn;
        _toString = null;
    }

    @Override
	public String getISBN() {
        return this.isbn;
    }

    @Override
	public void setAuthor(String author) {
        this.author = author;
        _toString = null;
    }

    @Override
	public String getAuthor() {
        return this.author;
    }

    @Override
	public void setTitle(String title) {
        this.title = title;
        _toString = null;
    }


    @Override
	public String getTitle() {
        return this.title;
    }

    @Override
	public void setGroup(String group) {
        this.group = group;
        _toString = null;
    }

    @Override
	public String getGroup() {
        return this.group;
    }

    @Override
	public void setGrade(int grade) {
        this.grade = grade;
        _toString = null;
    }

    @Override
	public int getGrade() {
        return this.grade;
    }

    @Override
	public String toString() {
        if (_toString == null) {
            StringBuffer buf = new StringBuffer();
            buf.append(getGroup()).append(": ");
            buf.append(getTitle()).append(" from ").append(getAuthor());
            buf.append(" (").append(getISBN()).append(") ");
            buf.append(" [").append(getGrade()).append("/10]");
            this._toString = buf.toString();
        }
        return _toString;
    }
}

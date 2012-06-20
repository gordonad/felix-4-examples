package com.packtpub.felix.bookshelf.service.impl;

import com.packtpub.felix.bookshelf.inventory.api.*;
import com.packtpub.felix.bookshelf.inventory.api.BookInventory.SearchCriteria;
import com.packtpub.felix.bookshelf.service.api.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.service.api.BookshelfService;
import com.packtpub.felix.bookshelf.service.api.InvalidCredentialsException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BookshelfServiceImpl implements BookshelfService {
    String session;

    BundleContext context;

    public BookshelfServiceImpl(BundleContext context) {
        this.context = context;
    }

    private BookInventory lookupBookInventory() {
        ServiceReference ref = this.context.getServiceReference(BookInventory.class.getName());
        if (ref == null) {
            throw new BookInventoryNotRegisteredRuntimeException(BookInventory.class.getName());
        }
        return (BookInventory) this.context.getService(ref);
    }

    @Override
	public String login(String username, char[] password) throws InvalidCredentialsException {
        if ("admin".equals(username) && Arrays.equals(password, "admin".toCharArray())) {
            this.session = Long.toString(System.currentTimeMillis());
            return this.session;
        }
        throw new InvalidCredentialsException(username);
    }

    protected void checkSession(String session) {
        if (this.session == null || !this.session.equals(session)) {
            throw new SessionNotValidRuntimeException(session);
        }
    }

    @Override
	public void logout(String session) {
        checkSession(session);
        this.session = null;
    }

    @Override
	public Book getBook(String session, String isbn) throws BookNotFoundException {
        checkSession(session);
        BookInventory inv = lookupBookInventory();
        return inv.loadBook(isbn);
    }

    public MutableBook getBookForEdit(String session, String isbn) throws BookNotFoundException {
        checkSession(session);
        BookInventory inv = lookupBookInventory();
        return inv.loadBookForEdit(isbn);
    }

    @Override
	public void addBook(String session, String isbn, String title, String author, String group,
                        int grade) throws BookAlreadyExistsException, InvalidBookException {
        checkSession(session);

        BookInventory inv = lookupBookInventory();

        MutableBook book = inv.createBook(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setGroup(group);
        book.setGrade(grade);

        inv.storeBook(book);
    }

    @Override
	public void modifyBookGroup(String session, String isbn, String group)
            throws BookNotFoundException, InvalidBookException {
        checkSession(session);

        BookInventory inv = lookupBookInventory();

        MutableBook book = inv.loadBookForEdit(isbn);
        book.setGroup(group);

        inv.storeBook(book);
    }

    @Override
	public void modifyBookGrade(String session, String isbn, int grade)
            throws BookNotFoundException, InvalidBookException {
        checkSession(session);

        BookInventory inv = lookupBookInventory();

        MutableBook book = inv.loadBookForEdit(isbn);
        book.setGrade(grade);

        inv.storeBook(book);
    }

    @Override
	public Set<String> getGroups(String session) {
        checkSession(session);
        BookInventory inv = lookupBookInventory();
        return inv.getGroups();
    }

    @Override
	public void removeBook(String session, String isbn) throws BookNotFoundException {
        checkSession(session);
        BookInventory inv = lookupBookInventory();
        inv.removeBook(isbn);
    }

    @Override
	public Set<String> searchBooksByAuthor(String session, String authorLike) {
        checkSession(session);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> crits = new HashMap<SearchCriteria, String>();
        crits.put(SearchCriteria.AUTHOR_LIKE, authorLike);
        return inv.searchBooks(crits);
    }

    @Override
	public Set<String> searchBooksByGroup(String session, String groupLike) {
        checkSession(session);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> crits = new HashMap<SearchCriteria, String>();
        crits.put(SearchCriteria.GROUP_LIKE, groupLike);
        return inv.searchBooks(crits);
    }

    @Override
	public Set<String> searchBooksByTitle(String session, String titleLike) {
        checkSession(session);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> crits = new HashMap<SearchCriteria, String>();
        crits.put(SearchCriteria.TITLE_LIKE, titleLike);
        return inv.searchBooks(crits);
    }

    @Override
	public Set<String> searchBooksByGrade(String session, int gradeLower, int gradeUpper) {
        checkSession(session);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> crits = new HashMap<SearchCriteria, String>();
        crits.put(SearchCriteria.GRADE_LT, Integer.toString(gradeLower));
        crits.put(SearchCriteria.GRADE_GT, Integer.toString(gradeUpper));
        return inv.searchBooks(crits);
    }

}

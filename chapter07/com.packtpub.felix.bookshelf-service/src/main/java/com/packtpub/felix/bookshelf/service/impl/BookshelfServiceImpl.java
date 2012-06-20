package com.packtpub.felix.bookshelf.service.impl;

import com.packtpub.felix.bookshelf.inventory.api.*;
import com.packtpub.felix.bookshelf.inventory.api.BookInventory.SearchCriteria;
import com.packtpub.felix.bookshelf.service.api.BookshelfService;
import com.packtpub.felix.bookshelf.service.api.InvalidCredentialsException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BookshelfServiceImpl implements BookshelfService {
    private String sessionId;

    private BundleContext context;

    public BookshelfServiceImpl(BundleContext context) {
        this.context = context;
    }

    @Override
	public String login(String username, char[] password) throws InvalidCredentialsException {
        if ("admin".equals(username) && Arrays.equals(password, "admin".toCharArray())) {
            this.sessionId = Long.toString(System.currentTimeMillis());
            return this.sessionId;
        }
        throw new InvalidCredentialsException(username);
    }

    @Override
	public void logout(String sessionId) {
        checkSession(sessionId);
        this.sessionId = null;
    }

    @Override
	public boolean sessionIsValid(String sessionId) {
        return this.sessionId != null && this.sessionId.equals(sessionId);
    }

    protected void checkSession(String sessionId) {
        if (!sessionIsValid(sessionId)) {
            throw new SessionNotValidRuntimeException(sessionId);
        }
    }

    private BookInventory lookupBookInventory() {
        ServiceReference ref = this.context.getServiceReference(BookInventory.class.getName());
        if (ref == null) {
            throw new BookInventoryNotRegisteredRuntimeException(BookInventory.class.getName());
        }
        return (BookInventory) this.context.getService(ref);
    }

    @Override
	public Book getBook(String sessionId, String isbn) throws BookNotFoundException {
        checkSession(sessionId);
        BookInventory inventory = lookupBookInventory();
        return inventory.loadBook(isbn);
    }

    public MutableBook getBookForEdit(String sessionId, String isbn) throws BookNotFoundException {
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        return inv.loadBookForEdit(isbn);
    }

    @Override
	public void addBook(String sessionId, String isbn, String title, String author, String category,
                        int rating) throws BookAlreadyExistsException, InvalidBookException {
        checkSession(sessionId);

        BookInventory inv = lookupBookInventory();

        MutableBook book = inv.createBook(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setRating(rating);

        inv.storeBook(book);
    }

    @Override
	public void modifyBookCategory(String sessionId, String isbn, String category)
            throws BookNotFoundException, InvalidBookException {
        checkSession(sessionId);

        BookInventory inv = lookupBookInventory();

        MutableBook book = inv.loadBookForEdit(isbn);
        book.setCategory(category);

        inv.storeBook(book);
    }

    @Override
	public void modifyBookRating(String sessionId, String isbn, int rating)
            throws BookNotFoundException, InvalidBookException {
        checkSession(sessionId);

        BookInventory inv = lookupBookInventory();

        MutableBook book = inv.loadBookForEdit(isbn);
        book.setRating(rating);

        inv.storeBook(book);
    }

    @Override
	public Set<String> getCategories(String sessionId) {
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        return inv.getCategories();
    }

    @Override
	public void removeBook(String sessionId, String isbn) throws BookNotFoundException {
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        inv.removeBook(isbn);
    }

    @Override
	public Set<String> searchBooksByAuthor(String sessionId, String authorLike) {
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
        criteria.put(SearchCriteria.AUTHOR_LIKE, authorLike);
        return inv.searchBooks(criteria);
    }

    @Override
	public Set<String> searchBooksByCategory(String sessionId, String groupLike) {
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
        criteria.put(SearchCriteria.CATEGORY_LIKE, groupLike);
        return inv.searchBooks(criteria);
    }

    @Override
	public Set<String> searchBooksByTitle(String sessionId, String titleLike) {
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
        criteria.put(SearchCriteria.TITLE_LIKE, titleLike);
        return inv.searchBooks(criteria);
    }

    @Override
	public Set<String> searchBooksByRating(String sessionId, int gradeLower, int gradeUpper) {
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
        criteria.put(SearchCriteria.RATING_LT, Integer.toString(gradeLower));
        criteria.put(SearchCriteria.RATING_GT, Integer.toString(gradeUpper));
        return inv.searchBooks(criteria);
    }

}

package com.packtpub.felix.bookshelf.service.tui;

import com.packtpub.felix.bookshelf.service.api.BookshelfService;
import com.packtpub.felix.bookshelf.service.api.InvalidCredentialsException;
import org.apache.commons.exec.CommandLine;
import org.osgi.framework.BundleContext;

import java.io.PrintStream;

public class AddBookCommand extends AbstractBookshelfCommand {
    public AddBookCommand(BundleContext context) {
        super(context);
    }

    @Override
	public String getName() {
        return "book-add";
    }

    @Override
	public String getShortDescription() {
        return "Add a book to the bookshelf";
    }

    @Override
	public String getUsage() {
        return "book-add <user> <pass> <isbn> <title> <author> <category> <rating>";
    }

    @Override
	public void execute(String command, PrintStream out, PrintStream err) {
        CommandLine line = CommandLine.parse(command);
        int len = line.getArguments().length;
        if (len != 7) {
            throw new InvalidCommandAttributesRuntimeException("Got "
                    + len
                    + " arguments, expecting 7");
        }

        String username = line.getArguments()[0];
        String password = line.getArguments()[1];
        String isbn = line.getArguments()[2];
        String title = trimQuotes(line.getArguments()[3]);
        String author = trimQuotes(line.getArguments()[4]);
        String category = trimQuotes(line.getArguments()[5]);
        int rating;
        try {
            rating = Integer.parseInt(line.getArguments()[6]);
        } catch (NumberFormatException e) {
            throw new InvalidCommandAttributesRuntimeException(
                    "Expecting integer rating, got '" + line.getArguments()[6] + "'");
        }

        BookshelfService service = lookupService();

        String session;
        try {
            session = service.login(username, password.toCharArray());
        } catch (InvalidCredentialsException e) {
            err.println("Invalid credentials");
            return;
        }

        try {
            service.addBook(session, isbn, title, author, category, rating);
        } catch (Exception e) {
            err.println(e.getMessage());
            return;
        }
        out.println("Done");
    }
}

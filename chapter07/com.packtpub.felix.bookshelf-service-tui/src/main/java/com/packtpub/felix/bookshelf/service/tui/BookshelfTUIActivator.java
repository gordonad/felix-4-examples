package com.packtpub.felix.bookshelf.service.tui;

import org.apache.felix.shell.Command;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.LinkedList;
import java.util.List;

public class BookshelfTUIActivator implements BundleActivator {
    List<ServiceRegistration> commands = new LinkedList<ServiceRegistration>();

    @Override
	public void start(BundleContext context) throws Exception {
        registerCommand(context, new SearchBooksCommand(context));
        registerCommand(context, new AddBookCommand(context));
    }

    private void registerCommand(BundleContext context, Command command) {
        ServiceRegistration reg = context.registerService(Command.class.getName(), command, null);
        this.commands.add(reg);
    }

    @Override
	public void stop(BundleContext context) throws Exception {
        for (ServiceRegistration reg : this.commands) {
            context.ungetService(reg.getReference());
        }
    }
}

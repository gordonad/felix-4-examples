package com.packtpub.felix.bookshelf.inventory.impl.mock;

import com.packtpub.felix.bookshelf.inventory.api.BookInventory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class BookInventoryMockImplActivator implements BundleActivator {
    ServiceRegistration reg = null;

    @Override
	public void start(BundleContext context) throws Exception {
        System.out.println("\nStarting Book Inventory Mock Impl");

        this.reg = context.registerService(BookInventory.class.getName(),
                new BookInventoryMockImpl(), null);
    }

    @Override
	public void stop(BundleContext context) throws Exception {
        if (this.reg != null) {
            System.out.println("\nStoping Book Inventory Mock Impl");
            context.ungetService(reg.getReference());
            this.reg = null;
        }
    }
}

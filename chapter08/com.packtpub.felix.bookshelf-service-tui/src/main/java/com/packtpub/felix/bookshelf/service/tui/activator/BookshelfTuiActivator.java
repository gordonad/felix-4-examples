package com.packtpub.felix.bookshelf.service.tui.activator;

import com.packtpub.felix.bookshelf.service.tui.BookshelfServiceProxy;
import com.packtpub.felix.bookshelf.service.tui.BookshelfServiceProxyImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Hashtable;


public class BookshelfTuiActivator implements BundleActivator {

    @Override
    public void start(BundleContext bc) {
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope", BookshelfServiceProxy.SCOPE);
        props.put("osgi.command.function", BookshelfServiceProxy.FUNCTIONS);
        bc.registerService(
                BookshelfServiceProxy.class.getName(), new BookshelfServiceProxyImpl(bc), props);
    }

    @Override
    public void stop(BundleContext bc) {
    }
}

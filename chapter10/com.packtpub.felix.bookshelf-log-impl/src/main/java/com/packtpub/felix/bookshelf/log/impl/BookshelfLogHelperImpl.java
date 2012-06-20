package com.packtpub.felix.bookshelf.log.impl;

import com.packtpub.felix.bookshelf.log.api.BookshelfLogHelper;
import org.osgi.service.log.LogService;

import java.text.MessageFormat;

public class BookshelfLogHelperImpl implements BookshelfLogHelper {
    LogService log;

    @Override
	public void debug(String pattern, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_DEBUG, message);
    }

    @Override
	public void debug(String pattern, Throwable throwable, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_DEBUG, message);
    }

    @Override
	public void error(String pattern, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_ERROR, message);
    }

    @Override
	public void error(String pattern, Throwable throwable, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_ERROR, message, throwable);
    }

    @Override
	public void info(String pattern, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_INFO, message);
    }

    @Override
	public void warn(String pattern, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_WARNING, message);
    }

    @Override
	public void warn(String pattern, Throwable throwable, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_WARNING, message, throwable);
    }

}

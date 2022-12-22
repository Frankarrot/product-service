package com.imp.productservice.support;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseExtension implements AfterEachCallback {
    @Override
    public void afterEach(final ExtensionContext context) throws Exception {
        DatabaseCleaner databaseCleaner = (DatabaseCleaner) SpringExtension
                .getApplicationContext(context).getBean("databaseCleaner");

        databaseCleaner.execute();
    }
}

package com.imp.productservice.support;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import jakarta.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {
    private static final String SET_REFERENTIAL_INTEGRITY_FALSE = "SET REFERENTIAL_INTEGRITY FALSE";
    private static final String SET_REFERENTIAL_INTEGRITY_TRUE = "SET REFERENTIAL_INTEGRITY TRUE";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE %s";
    private static final String ID_RESTART_COMMAND = "ALTER TABLE %s ALTER COLUMN %s RESTART WITH 1";

    @PersistenceContext
    private EntityManager entityManager;
    private final Map<String, String> tableNames = new HashMap<>();

    @PostConstruct
    public void init() {
        final var entityTypes = entityManager.getMetamodel().getEntities();

        for (EntityType<?> entityType : entityTypes) {
            final var tableName = entityType.getJavaType().getAnnotation(Table.class).name();
            final var fields = entityType.getJavaType().getDeclaredFields();
            final var idColumn = extractIdColumn(fields);
            tableNames.put(tableName, idColumn);
        }
    }

    private String extractIdColumn(final Field[] fields) {
        return Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Id.class))
                .map(Field::getName)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No Id Column Found"));
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery(SET_REFERENTIAL_INTEGRITY_FALSE).executeUpdate();

        for (String tableName : tableNames.keySet()) {
            entityManager.createNativeQuery(String.format(TRUNCATE_TABLE, tableName)).executeUpdate();
            entityManager.createNativeQuery(String.format(ID_RESTART_COMMAND, tableName, tableNames.get(tableName)))
                    .executeUpdate();
        }

        entityManager.createNativeQuery(SET_REFERENTIAL_INTEGRITY_TRUE).executeUpdate();
    }
}

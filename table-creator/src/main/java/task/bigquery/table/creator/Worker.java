package task.bigquery.table.creator;

import com.google.cloud.bigquery.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class Worker {
    private final BigQuery bigquery;
    private static final TableDefinition tableDefinition;

    static {
        Field hitId = Field.newBuilder("hitId", LegacySQLTypeName.STRING)
                .setMode(Field.Mode.NULLABLE).build();
        Field userId = Field.newBuilder("userId", LegacySQLTypeName.STRING)
                .setMode(Field.Mode.NULLABLE).build();
        Schema schema = Schema.of(hitId, userId);
        tableDefinition = StandardTableDefinition.of(schema);
    }

    void createTables(@NonNull String datasetId, @NonNull Set<String> tableNames) {
        if (datasetId.isEmpty()) {
            throw new IllegalArgumentException("Dataset should not be empty");
        }

        tableNames.forEach(name -> createTable(datasetId, name));
    }

    private void createTable(String datasetId, String tableName) {
        log.info("Start creating table `{}`", tableName);
        TableId tableId = TableId.of(datasetId, tableName);

        TableInfo tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build();
        Table table = bigquery.create(tableInfo);
        log.info("table `{}` has been successfully created, link: {}", tableName, table.getSelfLink());
    }
}
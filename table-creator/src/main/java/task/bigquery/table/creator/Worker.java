package task.bigquery.table.creator;

import com.google.cloud.bigquery.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;

@Slf4j
class Worker {
    private final BigQuery bigquery;

    Worker(BigQuery bigquery) {
        this.bigquery = bigquery;
    }

    void createTables(@NonNull String datasetId, @NonNull Set<String> tableNames) {
        if (datasetId.isEmpty()) {
            throw new IllegalArgumentException("Dataset should not be empty");
        }

        tableNames.forEach((name) -> createTable(datasetId, name));
    }

    private void createTable(String datasetId, String tableName) {
        log.info("Start creating table `" + tableName + "`");
        TableId tableId = TableId.of(datasetId, tableName);
        Field hitId = Field.newBuilder("hitId", LegacySQLTypeName.STRING)
                .setMode(Field.Mode.NULLABLE).build();
        Field userId = Field.newBuilder("userId", LegacySQLTypeName.STRING)
                .setMode(Field.Mode.NULLABLE).build();
        Schema schema = Schema.of(hitId, userId);
        TableDefinition tableDefinition = StandardTableDefinition.of(schema);
        TableInfo tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build();
        Table table = bigquery.create(tableInfo);
        log.info("table `" + tableName + "` has been successfully created, link: " + table.getSelfLink());
    }
}
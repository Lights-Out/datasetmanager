package task.bigquery.table.updater.service;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableInfo;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@AllArgsConstructor
public class BigQueryServiceImpl implements BigQueryService {

    private final BigQuery bigquery;

    @Override
    public Iterable<Table> findTablesByDataset(@NonNull String datasetId) {
        return bigquery.listTables(datasetId).iterateAll();
    }

    /**
     * Since expiration date is stored as timestamp, for consistency use UTC when converting from local
     * @param table TableInfo
     * @param dateTime LocalDateTime
     * @return Table
     */
    @Override
    public Table updateExpirationDate(@NonNull TableInfo table, @NonNull LocalDateTime dateTime) {
        TableInfo tableInfo = table.toBuilder()
                .setExpirationTime(dateTime.toInstant(ZoneOffset.UTC).toEpochMilli())
                .build();
        return bigquery.update(tableInfo);
    }
}
package task.bigquery.table.updater.service;

import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableInfo;
import lombok.NonNull;

import java.time.LocalDateTime;

public interface BigQueryService {
    Iterable<Table> findTablesByDataset(@NonNull String datasetId);
    Table updateExpirationDate(@NonNull TableInfo table, @NonNull LocalDateTime dateTime);
}
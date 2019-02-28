package task.bigquery.table.updater;

import com.google.cloud.bigquery.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import task.bigquery.table.updater.service.BigQueryService;
import task.bigquery.table.updater.service.DateTimeService;
import java.time.LocalDateTime;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class Worker {
    private static final String TABLE_PATTERN = "tmp.+";
    private final BigQueryService bigquery;
    private final DateTimeService dateTimeService;

    void updateTablesExpirationDate(@NonNull String datasetId) {

        if (datasetId.isEmpty()) {
            throw new IllegalArgumentException("Dataset should not be empty");
        }
        log.info("Start updating tables for dataset " + datasetId);

        LocalDateTime time = dateTimeService.localNow().plusWeeks(2);

        StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                bigquery.findTablesByDataset(datasetId).iterator(), Spliterator.DISTINCT), false)
                .filter(table -> table.getTableId().getTable().matches(TABLE_PATTERN))
                .forEach(table -> updateExpirationDate(table, time));

        log.info("Updating tables for dataset `{}` is finished", datasetId);
    }

    private void updateExpirationDate(Table table, LocalDateTime time) {
        bigquery.updateExpirationDate(table, time);
        log.info("Expiration date has been successfully updated for table `{}` to {}",
                table.getTableId().getTable(),
                time);
    }
}
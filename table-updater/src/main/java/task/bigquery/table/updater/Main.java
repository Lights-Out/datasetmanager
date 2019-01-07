package task.bigquery.table.updater;

import lombok.extern.slf4j.Slf4j;
import task.bigquery.client.BigQueryClient;
import task.bigquery.table.updater.service.BigQueryServiceImpl;
import task.bigquery.table.updater.service.DateTimeServiceImpl;

@Slf4j
public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Insufficient arguments count");
        }

        String projectId = args[0];
        String datasetId = args[1];

        try {
            new Worker(
                    new BigQueryServiceImpl(BigQueryClient.getService(projectId)),
                    new DateTimeServiceImpl()
            ).updateTablesExpirationDate(datasetId);
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }
}
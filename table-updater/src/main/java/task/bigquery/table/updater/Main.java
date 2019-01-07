package task.bigquery.table.updater;

import task.bigquery.client.BigQueryClient;
import task.bigquery.table.updater.service.BigQueryServiceImpl;
import task.bigquery.table.updater.service.DateTimeServiceImpl;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Insufficient arguments count");
        }

        String projectId = args[0];
        String datasetId = args[1];

        new Worker(
                new BigQueryServiceImpl(BigQueryClient.getService(projectId)),
                new DateTimeServiceImpl()
        ).updateTablesExpirationDate(datasetId);
    }
}
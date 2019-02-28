package task.bigquery.table.creator;

import lombok.extern.slf4j.Slf4j;
import task.bigquery.client.BigQueryClient;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class Main {

    public static void main(String[] args) {
        if (args.length < 4) {
            throw new IllegalArgumentException("Insufficient arguments count");
        }
        String projectId = args[0];
        String datasetId = args[1];
        String prefix = args[2];
        int count = Integer.parseInt(args[3]);

        Set<String> tableNames = IntStream.range(1, count + 1)
                .mapToObj(number -> prefix + String.valueOf(number))
                .collect(Collectors.toSet());

        try {
            new Worker(BigQueryClient.getService(projectId)).createTables(datasetId, tableNames);
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

}
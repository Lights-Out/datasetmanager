package task.bigquery.client;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.BigQueryOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class BigQueryClient {

    private static final String CREDENTIALS_ENVIRONMENT_VARIABLE = "GOOGLE_APPLICATION_CREDENTIALS";

    private BigQueryClient() {
    }

    /**
     * @param projectId String
     * @return BigQuery
     * @throws IllegalStateException if credentials environment variable is not set.
     * @throws BigQueryException if the credentials file does not exist
     * or credential cannot be created from the file stream.
     */
    public static BigQuery getService(String projectId) {
        String pathname = System.getenv(CREDENTIALS_ENVIRONMENT_VARIABLE);

        if (pathname == null) {
            throw new IllegalStateException("Cannot create BigQuery service. " +
                    "Credentials environment variable should be set");
        }

        GoogleCredentials credentials;

        try (FileInputStream serviceAccountStream = new FileInputStream(pathname)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
        } catch (IOException e) {
            throw new BigQueryException(e);
        }

        return BigQueryOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();
    }
}


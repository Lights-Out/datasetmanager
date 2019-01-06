package task.bigquery.table.creator;

import com.google.cloud.bigquery.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class WorkerTest {

    private static final String TEST_DATASET_ID = "testdataset0";
    private BigQuery bigQuery;
    private Worker worker;

    @Before
    public void setUp() {
        bigQuery = mock(BigQuery.class);
        given(bigQuery.create(any(TableInfo.class))).willReturn(mock(Table.class));
        worker = new Worker(bigQuery);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfDatasetIsEmpty() {
        worker.createTables("", new HashSet<>());
    }

    @Test
    public void shouldCreateTableForEachNameInSet() {
        HashSet<String> tableNames = new HashSet<>(Arrays.asList("t1", "t2", "t3"));
        worker.createTables(TEST_DATASET_ID, tableNames);
        verify(bigQuery, times(tableNames.size())).create(any(TableInfo.class));
    }
}
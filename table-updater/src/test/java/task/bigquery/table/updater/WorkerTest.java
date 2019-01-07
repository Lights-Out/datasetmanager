package task.bigquery.table.updater;

import com.google.cloud.bigquery.*;
import org.junit.Before;
import org.junit.Test;
import task.bigquery.table.updater.service.BigQueryService;
import task.bigquery.table.updater.service.DateTimeService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class WorkerTest {

    private static final String TEST_DATASET_ID = "testdataset0";
    private static final String MATCHING_TABLE_NAME = "tmp";
    private static final String NON_MATCHING_TABLE_NAME = "other";

    private final DateTimeService dateTimeService = mock(DateTimeService.class);
    private final BigQueryService bigQueryService = mock(BigQueryService.class);

    @Before
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        given(dateTimeService.localNow()).willReturn(now);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotProcessIfDatasetIsEmpty() {
        Worker worker = new Worker(bigQueryService, dateTimeService);
        worker.updateTablesExpirationDate("");
    }

    @Test
    public void shouldFilterTablesByPattern() {
        int matchingTablesCount = 4;
        Iterable<Table> infos = makeTablesListWithMatchingCount(matchingTablesCount);

        given(bigQueryService.findTablesByDataset(TEST_DATASET_ID)).willReturn(infos);

        Worker worker = new Worker(bigQueryService, dateTimeService);
        worker.updateTablesExpirationDate(TEST_DATASET_ID);

        verify(bigQueryService, times(matchingTablesCount)).updateExpirationDate(
                any(Table.class),
                any(LocalDateTime.class));
    }

    @Test
    public void shouldUpdateExpirationTimeByTwoWeeks() {

        List<Table> tables = new ArrayList<>();
        Table table = mock(Table.class);
        given(table.getTableId()).willReturn(TableId.of(TEST_DATASET_ID, "tmp0"));
        tables.add(table);

        given(bigQueryService.findTablesByDataset(TEST_DATASET_ID)).willReturn(tables);

        Worker worker = new Worker(bigQueryService, dateTimeService);
        worker.updateTablesExpirationDate(TEST_DATASET_ID);

        LocalDateTime expectedTime = dateTimeService.localNow().plusWeeks(2);
        verify(bigQueryService).updateExpirationDate(
                any(Table.class),
                eq(expectedTime));
    }

    private List<Table> makeTablesListWithMatchingCount(int count) {

        List<Table> tables = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            tables.add(makeTables(MATCHING_TABLE_NAME + i));
        }

        for (int i = 0; i < 5; i++) {
            tables.add(makeTables(NON_MATCHING_TABLE_NAME + i));
        }

        return tables;
    }

    private Table makeTables(String tableId) {
        Table table = mock(Table.class);
        given(table.getTableId()).willReturn(TableId.of(TEST_DATASET_ID, tableId));
        return table;
    }
}
package task.bigquery.table.updater.service;

import java.time.LocalDateTime;

public class DateTimeServiceImpl implements DateTimeService {
    public LocalDateTime localNow() {
        return LocalDateTime.now();
    }
}

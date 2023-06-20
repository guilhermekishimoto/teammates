package teammates.logic.external;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.math3.random.RandomDataGenerator;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonParseException;

import teammates.common.datatransfer.FeedbackSessionLogEntry;
import teammates.common.datatransfer.QueryLogsResults;
import teammates.common.datatransfer.logs.ExceptionLogDetails;
import teammates.common.datatransfer.logs.GeneralLogEntry;
import teammates.common.datatransfer.logs.LogDetails;
import teammates.common.datatransfer.logs.LogEvent;
import teammates.common.datatransfer.logs.QueryLogsParams;
import teammates.common.datatransfer.logs.RequestLogDetails;
import teammates.common.datatransfer.logs.RequestLogUser;
import teammates.common.datatransfer.logs.SourceLocation;
import teammates.common.datatransfer.logs.LogSeverity;
import teammates.common.util.FileHelper;
import teammates.common.util.JsonUtils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;



public class LocalLoggingServiceTest {
    private LocalLoggingService localLoggingService;

    @Before
    public void setUp() {
        localLoggingService = new LocalLoggingService();
    }

    @Test
    public void testIsEventBasedFilterSatisfied_AllNull() {
        // CT1: Todos os parâmetros são null
        QueryLogsParams queryLogsParams = new QueryLogsParams();
        GeneralLogEntry logEntry = new GeneralLogEntry();
        boolean result = localLoggingService.isEventBasedFilterSatisfied(logEntry, queryLogsParams);
        assertTrue(result);
    }

    @Test
    public void testIsEventBasedFilterSatisfied_AllNullExceptGoogleId() {
        // CT2: Todos os parâmetros são null exceto googleId
        QueryLogsParams queryLogsParams = new QueryLogsParams();
        queryLogsParams.setUserInfoParams(new RequestLogUser("googleId", null, null));
        GeneralLogEntry logEntry = new GeneralLogEntry();
        boolean result = localLoggingService.isEventBasedFilterSatisfied(logEntry, queryLogsParams);
        assertFalse(result);
    }

    @Test
    public void testIsEventBasedFilterSatisfied_AllNullExceptGoogleIdAndLogDetails_DifferentLogEvent() {
        // CT3: Todos os parâmetros são null exceto googleId e LogDetails. O objeto event é diferente do objeto logEvent.
        QueryLogsParams queryLogsParams = new QueryLogsParams();
        queryLogsParams.setLogEvent(LogEvent.EXCEPTION_LOG.name());
        queryLogsParams.setUserInfoParams(new RequestLogUser("googleId", null, null));

        RequestLogDetails requestLogDetails = new RequestLogDetails("actionClass", null, "uri", 200, 12, null, null, null);
        requestLogDetails.setEvent(LogEvent.REQUEST_LOG);

        GeneralLogEntry logEntry = new GeneralLogEntry();
        logEntry.setDetails(requestLogDetails);

        boolean result = localLoggingService.isEventBasedFilterSatisfied(logEntry, queryLogsParams);
        assertFalse(result);
    }

}
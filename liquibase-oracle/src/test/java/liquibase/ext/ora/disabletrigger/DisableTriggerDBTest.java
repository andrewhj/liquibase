package liquibase.ext.ora.disabletrigger;

import liquibase.ext.ora.testing.BaseTestCase;
import org.junit.Before;
import org.junit.Test;

public class DisableTriggerDBTest extends BaseTestCase {

    @Test
    public void placeholder() {

    }

//    private IDataSet loadedDataSet;
//    private final String TABLE_NAME = "USER_TRIGGERS";
//
//    @Before
//    public void setUp() throws Exception {
//        changeLogFile = "liquibase/ext/ora/disabletrigger/changelog.test.xml";
//        connectToDB();
//        cleanDB();
//    }
//
//    protected IDatabaseConnection getConnection() throws Exception {
//        return new DatabaseConnection(connection);
//    }
//
//    protected IDataSet getDataSet() throws Exception {
//        loadedDataSet = new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream(
//                "liquibase/ext/ora/disabletrigger/input.xml"));
//        return loadedDataSet;
//    }
//
//    @Test
//    public void testCompare() throws Exception {
//        if (connection == null) {
//            return;
//        }
//        QueryDataSet actualDataSet = new QueryDataSet(getConnection());
//
//        liquiBase.update((String) null);
//        actualDataSet.addTable("USER_TRIGGERS", "SELECT STATUS from " + TABLE_NAME
//                + " WHERE TRIGGER_NAME = 'RENAMEDZUIOLTRIGGER'");
//        loadedDataSet = getDataSet();
//
//        Assertion.assertEquals(loadedDataSet, actualDataSet);
//    }
}
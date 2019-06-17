package visitor;

import factory.Factory;
import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.naming.directory.InvalidAttributeValueException;
import java.io.InputStream;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InspectorTest {

    @Mock
    private Appender mockAppender;
    @Captor
    private ArgumentCaptor captorLoggingEvent;

    @Before
    public void setup() {
        LogManager.getRootLogger().addAppender(mockAppender);
    }

    @After
    public void teardown() {
        LogManager.getRootLogger().removeAppender(mockAppender);
    }

    @Test
    public void visitFactory_atCorrectTime(){
        //given
        Inspector inspector = new Inspector(300);
        Factory factory = Factory.getInstance();
        InputStream configStream = Factory.class.getResourceAsStream("/config1.txt");
        JSONObject json = new JSONObject(new JSONTokener(configStream));
        try {
            factory.setUp(json);
        } catch (InvalidAttributeValueException e) {
            System.err.println("Error in configuration");
        }


    }


}
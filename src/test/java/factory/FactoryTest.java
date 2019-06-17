package factory;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import javax.naming.directory.InvalidAttributeValueException;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class FactoryTest {


    @Test
    public void setUpTest(){
        //given
        InputStream configStream = Factory.class.getResourceAsStream("/config1.txt");
        JSONObject json = new JSONObject(new JSONTokener(configStream));
        Factory factory = Factory.getInstance();
        try {
            factory.setUp(json);
        } catch (InvalidAttributeValueException e) {
            System.err.println("Error in configuration");
        }
    }

}
package factory;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Test;
import worker.state.Ready;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Stack;

import static org.junit.Assert.*;

public class LineTest {

    private Factory factory;

    @Before
    public void setUp() throws Exception {
        String config = "/config1.txt";
        InputStream configStream = Factory.class.getResourceAsStream(config);
        if (configStream == null)
            throw new FileNotFoundException("resource not found: " + config);
        JSONObject json = new JSONObject(new JSONTokener(configStream));

        factory = Factory.getInstance();
        factory.setUp(json);
    }

    @Test
    public void workOnProduct_OrdinaryCase(){
//        arrange
        Line line = factory.getLines().get(0);

        Product product = new Product(ProductType.R2D2);
        int ticksInFirstPhase = product.getPhasesTicks()[0];
        Stack<Product> products = new Stack<>();
        products.push(product);
        line.setProductsToMake(products);
//        act
        line.tick();
//        assert
        assertNotNull(line.getCurrentWorker());
        assertTrue(line.getProductsToMake().empty());
        assertEquals(ticksInFirstPhase-1, product.getPhasesTicks()[0]);
        assertTrue(!product.isDone());
        assertNotNull(line.getCurrentWorker());
    }

    @Test
    public void workTick_LineIsDoneWhenFinishesAllProducts() {
//        arrange
        Line line = factory.getLines().get(1);

        Product product = new Product(ProductType.R2D2);
        Stack<Product> products = new Stack<>();
        products.push(product);
        line.setProductsToMake(products);
//        act
        while (!product.isDone()) {
            line.tick();
        }
        line.tick();
        //        assert
        assertTrue(line.isDone());
    }
}

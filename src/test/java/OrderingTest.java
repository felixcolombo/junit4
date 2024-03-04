import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderingTest {

    public static int count = 0;

    @Test
    public void t1_primeiro() {
        count = 1;
    }

    @Test
    public void t2_segundo() {
        Assert.assertEquals(1, count);
    }
}

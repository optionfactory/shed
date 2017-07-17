
import java.util.Arrays;
import java.util.List;
import javax.persistence.Temporal;
import org.junit.Assert;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fdegrassi
 */
public class ApplesServiceTest {
    
    @Test
    public void testA() {
        ApplesService sut = new ApplesService(
                new CannedApplesRepository(Arrays.asList(new Apple(30), new Apple(70))));
        
        Assert.assertEquals(50, (long) sut.calculateAverageWeight());
        
    }

    static class CannedApplesRepository implements ApplesRepository {

        private final List<Apple> apples;

        public CannedApplesRepository(List<Apple> apples) {
            this.apples = apples;
        }

        @Override
        public List<Apple> findAllApples() {
            return apples;
        }
    }
}

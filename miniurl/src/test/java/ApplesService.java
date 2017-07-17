
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fdegrassi
 */
public class ApplesService {
    private final ApplesRepository repo;

    public ApplesService(ApplesRepository repo) {
        this.repo = repo;
    }
 
    public final static int MAX_WEIGHT = 10;
    
    public Integer calculateAverageWeight() {
        final List<Apple> apples = repo.findAllApples(); // ...
        int total = 0;
        int count = 0;
        for (Apple apple : apples) {
            count++;
            total+=apple.weight;
        }
        int average = total / count;
        
        // OptionalDouble average = apples.stream().mapToDouble(a -> a.weight).average();
        return average;
    }
}

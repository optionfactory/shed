package net.optionfactory.minispring.minify;

import javax.persistence.Entity;
import javax.persistence.Id;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class MinifiedUrl {

    @Id
    public String handle;
    @NotEmpty
    public String target;

    public static MinifiedUrl of(String handle, String target) {
        final MinifiedUrl instance = new MinifiedUrl();
        instance.handle = handle;
        instance.target = target;
        return instance;
    }

}

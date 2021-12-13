package net.optionfactory.minispring.minify;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import java.net.URL;

@Entity
public class MinifiedUrl {

    @Id
    public String handle;
    @NotNull
    public URL target;

    public static MinifiedUrl of(String handle, URL target) {
        final MinifiedUrl instance = new MinifiedUrl();
        instance.handle = handle;
        instance.target = target;
        return instance;
    }

}

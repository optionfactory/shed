package net.optionfactory.minispring.blacklist;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Entity
public class BlackListItem {

    @Id
    @NotNull
    @Length(min=5, max=255)
    public String domain;
    @NotNull
    public Instant since;

    public BlackListItem(String domain, Instant since) {
        this.domain = domain;
        this.since = since;
    }

}

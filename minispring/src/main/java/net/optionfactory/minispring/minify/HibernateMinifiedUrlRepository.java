package net.optionfactory.minispring.minify;

import java.util.Optional;
import org.hibernate.SessionFactory;

public class HibernateMinifiedUrlRepository implements MinifiedUrlRepository {

    private final SessionFactory hibernate;

    public HibernateMinifiedUrlRepository(SessionFactory hibernate) {
        this.hibernate = hibernate;
    }

    @Override
    public void add(MinifiedUrl url) {
        hibernate.getCurrentSession().persist(url);
    }

    @Override
    public Optional<MinifiedUrl> find(String handle) {
        return Optional.ofNullable(hibernate.getCurrentSession().find(MinifiedUrl.class, handle));
    }

}

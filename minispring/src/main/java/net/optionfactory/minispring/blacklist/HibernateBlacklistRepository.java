package net.optionfactory.minispring.blacklist;

import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.SessionFactory;

public class HibernateBlacklistRepository implements BlacklistRepository {

    private final SessionFactory hibernate;

    public HibernateBlacklistRepository(SessionFactory hibernate) {
        this.hibernate = hibernate;
    }

    @Override
    public void add(BlackListItem item) {
        hibernate.getCurrentSession().persist(item);
    }

    @Override
    public Optional<BlackListItem> find(String domain) {
        return Optional.ofNullable(hibernate.getCurrentSession().get(BlackListItem.class, domain));
    }

    @Override
    public List<BlackListItem> findAll() {
        final CriteriaQuery<BlackListItem> q = hibernate.getCurrentSession()
                .getCriteriaBuilder()
                .createQuery(BlackListItem.class);
        q.select(q.from(BlackListItem.class));
        return hibernate.getCurrentSession().createQuery(q).getResultList();
    }

    @Override
    public void remove(BlackListItem item) {
        hibernate.getCurrentSession().remove(item);
    }

}

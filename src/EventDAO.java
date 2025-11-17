import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class EventDAO {
    public void saveEvent(Event event) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(event);
        tx.commit();
        session.close();
    }

    public List<Event> getAllEvents() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Event> events = session.createQuery("from Event", Event.class).list();
        session.close();
        return events;
    }

    public Event getEventById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Event event = session.get(Event.class, id);
        session.close();
        return event;
    }

    public void deleteEvent(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Event event = session.get(Event.class, id);
        if (event != null) {
            session.delete(event);
        }
        tx.commit();
        session.close();
    }
}
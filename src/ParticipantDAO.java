import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class ParticipantDAO {
    public void saveParticipant(Participant participant) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(participant);
        tx.commit();
        session.close();
    }

    public List<Participant> getParticipantsByEvent(int eventId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Participant> participants = session.createQuery(
                        "from Participant where event.id = :eventId", Participant.class)
                .setParameter("eventId", eventId)
                .list();
        session.close();
        return participants;
    }

    public void deleteParticipant(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Participant participant = session.get(Participant.class, id);
        if (participant != null) {
            session.delete(participant);
        }
        tx.commit();
        session.close();
    }
}
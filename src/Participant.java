import javax.persistence.*;

@Entity
@Table(name = "participant")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private boolean rsvp;
    private boolean cancelled = false;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isRsvp() { return rsvp; }
    public void setRsvp(boolean rsvp) { this.rsvp = rsvp; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
}
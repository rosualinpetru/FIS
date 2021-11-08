package ro.go.redhomeserver.tom.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import ro.go.redhomeserver.tom.enums.RequestStatus;
import ro.go.redhomeserver.tom.enums.RequestType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "HolidayRequest")
@Table(name = "holiday_request")
public class HolidayRequest implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Enumerated(EnumType.ORDINAL)
    private RequestType type;

    @Enumerated(EnumType.ORDINAL)
    private RequestStatus status;

    private String description;
    private Date start;
    private Date end;

    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @OneToOne(mappedBy = "request", orphanRemoval = true)
    private UploadedFile uploadedFile;

    @ManyToOne
    @JoinColumn(name = "FK_requester")
    private Account requester;

    @ManyToOne
    @JoinColumn(name = "FK_delegate")
    private Account delegate;

    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Feedback> requestFeedback = new HashSet<>();

    public HolidayRequest(RequestType type, RequestStatus status, String description, Date start, Date end, Account requester, Account delegate) {
        this.type = type;
        this.status = status;
        this.description = description;
        this.start = start;
        this.end = end;
        this.requester = requester;
        this.delegate = delegate;
    }

    public int getWorkingDays() {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);

        int workDays = 0;

        if (startCal.getTimeInMillis() != endCal.getTimeInMillis()) {
            do {
                if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    ++workDays;
                }
                startCal.add(Calendar.DAY_OF_MONTH, 1);
            } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());
        }
        return workDays;
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        HolidayRequest that = (HolidayRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    @Generated
    public int hashCode() {
        return getClass().hashCode();
    }
}

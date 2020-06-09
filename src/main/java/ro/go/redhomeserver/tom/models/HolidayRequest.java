package ro.go.redhomeserver.tom.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.go.redhomeserver.tom.dtos.RequestStatus;
import ro.go.redhomeserver.tom.dtos.RequestType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "HolidayRequest")
@Table(name = "holiday_request")
public class HolidayRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.ORDINAL)
    private RequestType type;

    @Enumerated(EnumType.ORDINAL)
    private RequestStatus status;

    private String description;
    private Date start;
    private Date end;

    @ManyToOne
    @JoinColumn(name = "FK_requester")
    private Account requester;

    @ManyToOne
    @JoinColumn(name = "FK_delegate")
    private Account delegate;

    public HolidayRequest(RequestType type, RequestStatus status, String description, Date start, Date end, Account requester, Account delegate) {
        this.type = type;
        this.status = status;
        this.description = description;
        this.start = start;
        this.end = end;
        this.requester = requester;
        this.delegate = delegate;
    }

}

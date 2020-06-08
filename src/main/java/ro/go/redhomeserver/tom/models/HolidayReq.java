package ro.go.redhomeserver.tom.models;

import lombok.*;
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
@Table(name = "holiday_req")
public class HolidayReq implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.ORDINAL)
    private RequestType type;

    @Enumerated(EnumType.ORDINAL)
    private RequestStatus status;

    private String description;

    private Date start;
    private Date end;

    @ManyToOne
    @JoinColumn(name = "FK_ID_req")
    private Account accountReq;

    @ManyToOne
    @JoinColumn(name = "FK_ID_tl")
    private Account accountTl;

    public HolidayReq(RequestType type, RequestStatus status, String description, Date start, Date end, Account accountReq, Account accountTl) {
        this.type = type;
        this.status = status;
        this.description = description;
        this.start = start;
        this.end = end;
        this.accountReq = accountReq;
        this.accountTl = accountTl;
    }

}

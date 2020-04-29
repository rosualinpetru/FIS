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
@ToString
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
    private Account account_req;

    @ManyToOne
    @JoinColumn(name = "FK_ID_tl")
    private Account account_tl;
}

package ro.go.redhomeserver.tom.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity(name = "HolidayRequest")
@Table(name = "holiday_req")
public class HolidayReq {
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

    public HolidayReq() {

    }

    public HolidayReq(RequestType type, RequestStatus status, String description, Date start, Date end, Account account_req, Account account_tl) {
        this.type = type;
        this.status = status;
        this.description = description;
        this.start = start;
        this.end = end;
        this.account_req = account_req;
        this.account_tl = account_tl;
    }

    public int getId() {
        return id;
    }

    public RequestType getType() {
        return type;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Account getAccount_req() {
        return account_req;
    }

    public Account getAccount_tl() {
        return account_tl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HolidayReq that = (HolidayReq) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getStatus(), getDescription(), getStart(), getEnd(), getAccount_req(), getAccount_tl());
    }

    @Override
    public String toString() {
        return "HolidayReq{" +
                "id=" + id +
                ", type=" + type +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", account_req=" + account_req +
                ", account_tl=" + account_tl +
                '}';
    }
}

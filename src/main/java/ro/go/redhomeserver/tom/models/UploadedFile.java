package ro.go.redhomeserver.tom.models;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "UploadedFile")
@Table(name = "uploaded_file")
public class UploadedFile implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String fileName;
    private String fileType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_request")
    private HolidayRequest request;

    @Lob
    private byte[] data;

    public UploadedFile(String fileName, String fileType, byte[] data, HolidayRequest request) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.request = request;
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UploadedFile that = (UploadedFile) o;
        return Objects.equals(id, that.id);
    }

    @Override
    @Generated
    public int hashCode() {
        return getClass().hashCode();
    }
}

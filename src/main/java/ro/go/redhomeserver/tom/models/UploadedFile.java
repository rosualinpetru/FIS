package ro.go.redhomeserver.tom.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
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
}

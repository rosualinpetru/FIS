package ro.go.redhomeserver.tom.models;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


public class UploadedFileTest {
    @Test
    void checkConstructorAndGetters() {
        byte[] data = new byte[]{};
        HolidayRequest holidayRequest = new HolidayRequest();
        UploadedFile uploadedFile = new UploadedFile("test", "test", data, holidayRequest);
        assertThat(uploadedFile.getFileName().equals("test")).isTrue();
        assertThat(uploadedFile.getFileType().equals("test")).isTrue();
        assertThat(Arrays.equals(uploadedFile.getData(), data)).isTrue();
        assertThat(uploadedFile.getRequest().equals(holidayRequest)).isTrue();
        assertThat(uploadedFile.getId()).isNull();
    }

    @Test
    void checkNoArgsConstructor() {
        UploadedFile uploadedFile = new UploadedFile();
        assertThat(uploadedFile.getFileName()).isNull();
        assertThat(uploadedFile.getFileType()).isNull();
        assertThat(uploadedFile.getData()).isNull();
        assertThat(uploadedFile.getRequest()).isNull();
        assertThat(uploadedFile.getId()).isNull();
    }

    @Test
    void checkSetter() {
        byte[] data = new byte[]{};
        HolidayRequest holidayRequest = new HolidayRequest();
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName("test");
        uploadedFile.setFileType("test");
        uploadedFile.setData(data);
        uploadedFile.setRequest(holidayRequest);
        uploadedFile.setId("1");
        assertThat(uploadedFile.getFileName().equals("test")).isTrue();
        assertThat(uploadedFile.getFileType().equals("test")).isTrue();
        assertThat(Arrays.equals(uploadedFile.getData(), data)).isTrue();
        assertThat(uploadedFile.getRequest().equals(holidayRequest)).isTrue();
        assertThat(uploadedFile.getId().equals("1")).isTrue();
    }

    @Test
    void checkEquals() {
        byte[] data = new byte[]{};
        HolidayRequest holidayRequest = new HolidayRequest();
        UploadedFile uploadedFile1 = new UploadedFile("test", "test", data, holidayRequest);
        UploadedFile uploadedFile2 = new UploadedFile("test", "test", data, holidayRequest);
        UploadedFile uploadedFile3 = new UploadedFile("test", "test1", data, holidayRequest);
        assertThat(uploadedFile1.equals(uploadedFile2)).isTrue();
        assertThat(uploadedFile1.equals(uploadedFile3)).isFalse();
        assertThat(uploadedFile1.equals(new Object())).isFalse();
        assertThat(uploadedFile1.hashCode() == uploadedFile2.hashCode()).isTrue();
    }
}

package ro.go.redhomeserver.tom.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.HolidayRequest;
import ro.go.redhomeserver.tom.models.UploadedFile;
import ro.go.redhomeserver.tom.repositories.HolidayRequestRepository;
import ro.go.redhomeserver.tom.repositories.UploadedFileRepository;

import java.io.File;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UploadedFileServiceTest {
    @Mock
    private UploadedFileRepository uploadedFileRepository;
    @Mock
    private HolidayRequestRepository holidayRequestRepository;

    @InjectMocks
    private UploadedFileService uploadedFileService;

    //getFileByRequestId
    @Test
    void getFileByRequestId_Null_RequestNotFound() {
        assertThat(uploadedFileService.getFileByRequestId(anyString())).isNull();
    }

    @Test
    void getFileByRequestId_Null_RequestFoundButFileNotSet() {
        when(holidayRequestRepository.findById(anyString())).thenReturn(java.util.Optional.of(new HolidayRequest()));
        assertThat(uploadedFileService.getFileByRequestId(anyString())).isNull();
    }

    @Test
    void getFileByRequestId_AFile_RequestFoundButFileNotSet() {
        HolidayRequest hr = new HolidayRequest();
        UploadedFile f = new UploadedFile();
        hr.setUploadedFile(f);
        when(holidayRequestRepository.findById(anyString())).thenReturn(java.util.Optional.of(hr));
        assertThat(uploadedFileService.getFileByRequestId(anyString())).isNotNull();
        assertThat(uploadedFileService.getFileByRequestId(anyString()).equals(f)).isNotNull();
    }

    //storeFile
    @Test
    void storeFileShouldReturnTheUploadedFile() {
        HolidayRequest hr = new HolidayRequest();
        Account requester = new Account();
        requester.setUsername("arosu");
        hr.setRequester(requester);
        UploadedFile f = null;
        when(uploadedFileRepository.save(any(UploadedFile.class))).then(invocation -> invocation.getArguments()[0]);
        try {
            f = uploadedFileService.storeFile(new MultipartFile() {
                @Override
                public String getName() {
                    return null;
                }

                @Override
                public String getOriginalFilename() {
                    return null;
                }

                @Override
                public String getContentType() {
                    return null;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public long getSize() {
                    return 0;
                }

                @Override
                public byte[] getBytes() {
                    return new byte[0];
                }

                @Override
                public InputStream getInputStream() {
                    return null;
                }

                @Override
                public void transferTo(File dest) throws IllegalStateException {

                }
            }, hr);
        } catch (Exception e) {
            fail("Exception interfered!");
        }

        verify(uploadedFileRepository, times(1)).save(any(UploadedFile.class));
        assertThat(f).isNotNull();
        assertThat(f.getFileName().contains("arosu")).isTrue();
    }
}

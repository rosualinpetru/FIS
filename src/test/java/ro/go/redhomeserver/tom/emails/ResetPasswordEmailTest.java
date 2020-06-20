package ro.go.redhomeserver.tom.emails;

import org.junit.jupiter.api.Test;
import ro.go.redhomeserver.tom.models.Account;

import static org.assertj.core.api.Assertions.assertThat;

public class ResetPasswordEmailTest {
     @Test

    void checkCredentialAndConstructor(){
         Account account = new Account();
         ResetPasswordEmail resetPasswordEmail =new ResetPasswordEmail(account,"test");
         assertThat(resetPasswordEmail.getTo().equals(account)).isTrue();
     }

}

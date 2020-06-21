package ro.go.redhomeserver.tom.dtos;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

public class CalendarEventTest {
     @Test
    void checkConstructorsAndGetters (){

         Date date1 = new Date();
         Date date2 = new Date();

         CalendarEvent calendarEvent = new CalendarEvent("id","test",date1,date2,"test");
         assertThat(calendarEvent.getColor().equals("test")).isTrue();
         assertThat(calendarEvent.getEnd().equals(date1)).isTrue();
         assertThat(calendarEvent.getStart().equals(date2)).isTrue();
         assertThat(calendarEvent.getId().equals("id")).isTrue();
         assertThat(calendarEvent.getTitle().equals("test")).isTrue();

     }
    @Test
     void checkSetters(){


         Date date1 = new Date();
         Date date2 = new Date();
         CalendarEvent calendarEvent= new CalendarEvent("id","test",new Date(),new Date(),"test");
         calendarEvent.setColor("test2");
         calendarEvent.setId("id1");
         calendarEvent.setTitle("title2");
         calendarEvent.setStart(date1);
         calendarEvent.setEnd(date2);
         assertThat(calendarEvent.getColor().equals("test2")).isTrue();
         assertThat(calendarEvent.getEnd().equals(date1)).isTrue();
         assertThat(calendarEvent.getStart().equals(date2)).isTrue();
         assertThat(calendarEvent.getId().equals("id1")).isTrue();
         assertThat(calendarEvent.getTitle().equals("title2")).isTrue();
     }

}

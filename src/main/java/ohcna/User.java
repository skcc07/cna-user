package ohcna;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="User_table")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String userId;
    private String userName;
    private String createDtm;

    @PostPersist
    public void onPostPersist(){
        UserCanceled userCanceled = new UserCanceled();
        BeanUtils.copyProperties(this, userCanceled);
        userCanceled.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        ohcna.external.Booking booking = new ohcna.external.Booking();
        // mappings goes here
        UserApplication.applicationContext.getBean(ohcna.external.BookingService.class)
            .bookingCancel(booking);


    }

    @PrePersist
    public void onPrePersist(){
        UserCreated userCreated = new UserCreated();
        BeanUtils.copyProperties(this, userCreated);
        userCreated.publishAfterCommit();


        UserChanged userChanged = new UserChanged();
        BeanUtils.copyProperties(this, userChanged);
        userChanged.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getCreateDtm() {
        return createDtm;
    }

    public void setCreateDtm(String createDtm) {
        this.createDtm = createDtm;
    }




}

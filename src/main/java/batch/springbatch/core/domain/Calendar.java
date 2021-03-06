package batch.springbatch.core.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@DynamicUpdate // entity의 일부 컬럼의 값이 변경되었을때 그 컬럼 값만 변경가능하도록 하는 것
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "calendar")
public class Calendar extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    LocalDate date;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    DayType dayType;

    public Calendar( LocalDate now, DayType holiday, LocalDateTime now1, LocalDateTime now2, String create_Id, String update_Id) {
        super.setCreatedAt(now1);
        super.setCreateId(create_Id);
        super.setUpdatedAt(now2);
        super.setUpdateId(update_Id);

        this.date = now;
        this.dayType = holiday;
    }

}

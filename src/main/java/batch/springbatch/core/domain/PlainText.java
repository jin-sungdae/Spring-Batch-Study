package batch.springbatch.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DynamicUpdate // entity의 일부 컬럼의 값이 변경되었을때 그 컬럼 값만 변경가능하도록 하는 것
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "plain_text")
public class PlainText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String text;
}

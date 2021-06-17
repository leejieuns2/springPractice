package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ", initialValue = 1, allocationSize = 1)
public class Member {

    // Annotation에서 필요한 매핑 처리를 해줌.

    // @GeneratedValue 사용시 필요한 전략(Strategy) 설정 가능
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(name = "name", nullable = false)
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // LocalDate, LocalDateTime 사용 시 어노테이션 생략 가능
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;

    // 메모리에서만 쓰고 싶은 경우 @Transient Annotation 사용
    @Transient
    private String temp;

    public Member() { }
}

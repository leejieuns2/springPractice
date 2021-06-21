package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    // 일대다 양방향 매핑은 공식적으로 존재하지 않으나,
    // 읽기 전용 필드를 속성을 통해 추가 정의하여 양방향처럼 사용이 가능하다.
    // 되도록이면 다대일 양방향 매핑을 사용하는 것이 좋다.
    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    private Team team;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    /*
    // 연결테이블읊 생성해 다대다 관계 매핑 가능하지만 권장하지 않음.
    // 편리해보이지만 대체로 실무에서 연결테이블이 단순 연결 역할만 하는 경우는 거의 없기 때문에
    // 추가적인 컬럼이 필요할 경우, 연결 테이블 용 엔티티 객체를 따로 만들어 일대다, 다대일 매핑을 진행해주는 것을 권장함.
    @ManyToMany
    @JoinTable(name="MEMBER_PRODUCT")
    private List<Product> products = new ArrayList<>();
     */

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}

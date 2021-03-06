# 자바 ORM 표준 JPA 프로그래밍 강의 내용 정리 (Section 5-8)

## 05. 연관관계 매핑 기초

### 연관관계가 필요한 이유

- 객체를 테이블에 맞추어 모델링할 경우, 참조 대신에 외래 키를 그대로 사용해 참조하는 테이블에서 외래 키 식별자를 직접 다루게 된다.   
  이러한 방법은 객체 지향적이지 않다.

- 테이블은 외래 키로 조인을 사용해 연관된 테이블을 찾고, 객체는 참조를 사용해 연관된 객체를 찾는다는 큰 차이점이 있다.

- 이러한 차이점이 존재하기 때문에, 객체를 테이블에 맞추어 데이터 중심적으로 모델링을 할 경우, 협력 관계를 만들 수 없다.

> 객체의 참조와 테이블의 외래 키를 매핑하는 연관관계 매핑을 사용하여 객체 내에서 데이터의 조인관계를 표현하면 객체 지향적인 모델링이 가능하다.


<br>

### 단방향 연관관계

```java

@Entity
public class Member {

    @Id
    @GenerateValue
    private Long id;

    @Column(name = "USERNAME")
    private String name;
    private int age;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;
}
```

- 객체의 참조와 테이블의 외래키를 매핑

```java
    //팀 저장
    Team team = new Team();
    team.setName("TeamA");
    em.persist(team);

    //회원 저장    
    Member member=new Member(); member.setName("member1");
    member.setTeam(team); //단방향 연관관계 설정, 참조 저장 
    em.persist(member);

    //조회
    Member findMember=em.find(Member.class,member.getId());

    //참조를 사용해서 연관관계 조회
    Team findTeam=findMember.getTeam();

    // 새로운 팀
    Team teamB=new Team();
    teamB.setName("TeamB");
    em.persist(teamB);
        
    // 회원1에 새로운 팀B 설정
    member.setTeam(teamB);
```

<br>

### 양방향 연관관계와 연관관계의 주인

```java
@Entity
public class Member {

    // Member Entity는 단방향과 동일하게 설정해줌.
    @Id
    @GenerateValue
    private Long id;

    @Column(name = "USERNAME")
    private String name;
    private int age;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;
}

@Entity
public class Team {

    @Id
    @GenerateValue
    private Long id;

    private String name;

    // Team Entity는 컬렉션(ArrayList)로 추가함.
    @OneToMany(mappedBy = "team")
    List<Member> members = new ArrayList<Member>();
}

```

<br>

### 객체와 테이블이 관계를 맺는 것의 차이

1. 객체 연관관계 : 2개

- 회원 -> 팀 연관관계 1개 / 팀 -> 회원 연관관계 1개
- 단방향 관계 2개로 구성되어야 양방향 참조 가능 (결국 객체의 양방향 관계는 서로 다른 단방향 관계 2개로 구성되어 있다는 의미)

2. 테이블 연관관계 : 1개

- 회원 <-> 팀의 연관관계 1개
- 테이블은 외래 키 하나로 두 테이블의 양방향 연관관계 관리 가능

<br>

### 연관관계의 주인 (Owner)

- 양방향 매핑 시 객체의 두 관계중 하나를 연관관계의 주인으로 지정한다.
- 연관관계의 주인만이 외래 키를 관리할 수 있다 (외래키의 등록 및 수정 가능)
- 주인이 아닌 객체는 외래 키에 영향을 주지 않으며, 단순 조회. 즉 읽기만 가능하다.
- 주인이 아닐 경우 mappedBy 속성으로 주인을 지정해주어야 한다. (주인 객체는 mappedBy 속성 사용 불가)
- 연관관계의 주인은 외래 키를 가지고 있는 객체이다.
- 순수 객체 상태를 고려해 항상 양쪽에 값을 설정해주어야 하며, 이 때 무한 루프를 조심해야 한다.

<br>

## 06. 다양한 연관관계 매핑

### 연관관계 매핑시 고려사항

- 다중성 (다대일, 일대다, 일대일, 다대다)
- 단방향, 양방향
    - 테이블 : 외래 키 하나로 양쪽 조인 가능하므로 사실상 방향이라는 개념이 없음
    - 객체 : 참조용 필드가 있는 쪽으로만 참조 가능. 한쪽만 참조하면 단방향, 양쪽이 서로 참조하면 양방향
- 연관관계의 주인

<br>

### 1. 다대일 [N:1] `@ManyToOne`

- 가장 많이 사용하는 연관관계
- 양뱡항 설계시 양쪽을 서로 참조하도록 개발
  <-> 일대다 `@OneToMany`

<br>

### 2. 일대다 [1:N] `@OneToMany`

- 일대다 단방향은 일대다에서 일(1)이 연관관계의 주인
- 테이블 일대다 관계는 항샹 다(N)쪽에 외래 키가 있음.
- 객체와 테이블의 차이로 인해 반대편 테이블의 외래 키를 관리하는 특이한 구조
- `@JoinColumn`을 꼭 사용해야 함. 그렇지 않으면 조인 테이블 방식을 사용함 (중간에 테이블을 하나 추가함)

> 일대다 단방향 매핑의 단점

- 엔티티가 관리하는 외래 키가 다른 테이블에 있음
- 연관관계 관리를 위해 추가로 UPDATE SQL 실행
- 일대다 단방향 매핑보다는 다대일 양방향 매핑을 사용하는 것이 좋음 !

- 일대다 양방향은 공식적으로 존재하지 않으나, 읽기 전용 필드를 사용해 양방향처럼 사용할 수 있음 (권장하지 않음)
  `@JoinColumn(insertable=false, updatable=false)`

<br>

### 3. 일대일 [1:1] `@OneToOne`

- 주 테이블이나 대상 테이블 중에 외래 키 선택 가능
- 외래 키에 데이터베이스 유니크(Unique) 제약조건 추가
- 다대일 `@ManyToOne` 단방향 매핑과 유사함.
- 양방향 설정 시 다대일 양방향과 같은 방법으로 적용 (외래 키:연관관계의 주인 <-> 반대편은 MappedBy 적용)
- 대상 테이블에 적용하는 외래 키 일대일 단방향 관계는 JPA에서 지원되지 않으며, 양방향만 지원함.

> 주 테이블에 외래키

- 주 객체가 대상 객체의 참조를 가지는 것 처럼 주 테이블에 외래 키를 두고 대상 테이블을 찾음
- 객체지향 개발자 선호 • JPA 매핑 편리
- 장점: 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인 가능
- 단점: 값이 없으면 외래 키에 null 허용

> 대상 테이블에 외래키

- 대상 테이블에 외래 키가 존재
- 전통적인 데이터베이스 개발자 선호
- 장점: 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경할 때 테이블 구조 유지
- 단점: 프록시 기능의 한계로 지연 로딩으로 설정해도 항상 즉시 로딩됨(프록시는 뒤에서 설명)

<br>

### 4. 다대다 [N:M] `@ManyToMany`

- 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없음
- 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야함
- 객체는 컬렉션을 사용해서 객체 2개로 다대다 관계 가능
- 다대다 매핑 : 단방향, 양방향 가능
- `@JoinTable` 로 연결 테이블 지정

> 다대다 매핑의 한계

- 편리해 보이지만 실무에서 사용X
- 연결 테이블이 단순히 연결만 하고 끝나지 않고 추가 데이터를 필요로 할 가능성이 높음.

> 다대다 매핑의 한계 극복 방법

- 연결 테이블용 엔티티 추가(연결 테이블을 엔티티로 승격)
- `@ManyToMany` -> `@OneToMany`, `@ManyToOne`

<br>

## 07. 고급 매핑

### 상속관계 매핑

- 관계형 데이터베이스는 상속 관계X
- 슈퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사
- 상속관계 매핑: 객체의 상속과 구조와 DB의 슈퍼타입 서브타입 관계를 매핑
- 슈퍼타입 서브타입 논리 모델을 실제 물리 모델로 구현하는 방법
    1. 각각 테이블로 변환 > 조인 전략
    2. 통합 테이블로 변환 -> 단일 테이블 전략
    3. 서브타입 테이블로 변환 -> 구현 클래스마다 테이블 전략

### 상속관계 매핑 주요 어노테이션

- `@Inheritance(strategy=InheritanceType.XXX)`
    1. JOINED: 조인 전략
    2. SINGLE_TABLE: 단일 테이블 전략
    3. TABLE_PER_CLASS: 구현 클래스마다 테이블 전략
- `@DiscriminatorColumn(name=“DTYPE”)`
- `@DiscriminatorValue(“XXX”)`

<br>

### 상속관계 매핑 1. 조인 전략 (JOINED)

1. 장점

- 테이블 정규화
- 외래 키 참조 무결성 제약조건 활용가능
- 저장공간 효율화

2. 단점

- 조회시 조인을 많이 사용, 성능 저하
- 조회 쿼리가 복잡함
- 데이터 저장시 INSERT SQL 2번 호출

<br>

### 상속관계 매핑 2. 단일 테이블 전략 (SINGLE_TABLE)

1. 장점

- 조인이 필요 없으므로 일반적으로 조회 성능이 빠름
- 조회 쿼리가 단순함

2. 단점

- 자식 엔티티가 매핑한 컬럼은 모두 null 허용
- 단일 테이블에 모든 것을 저장하므로 테이블이 커질수있다.
- 상황에 따라서 조회 성능이 오히려 느려질 수 있다.

<br>

### 상속관계 매핑 3. 구현 클래스마다 테이블 전략 (TABLE_PER_CLASS) 추천X

1. 장점

- 서브 타입을 명확하게 구분해서 처리할 때 효과적
- not null 제약조건 사용 가능

2. 단점

- 여러 자식 테이블을 함께 조회할 때 성능이 느림(UNION SQL 필요)
- 자식 테이블을 통합해서 쿼리하기 어려움

<br>

### `@MappedSuperClass`

- 공통 매핑 정보가 필요할 때 사용 (id, name)
- 상속관계 매핑X
- 엔티티X, 테이블과 매핑X
- 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공 조회, 검색 불가(em.find(BaseEntity) 불가)
- 직접 생성해서 사용할 일이 없으므로 추상 클래스 권장
- 테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할
- 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통 으로 적용하는 정보를 모을 때 사용
- 참고: @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속 가능

## 프록시와 연관관계 관리

1. 프록시

- em.find() vs em.getReference()
- em.find(): 데이터베이스를 통해서 실제 엔티티 객체 조회
- em.getReference(): 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회

> 프록시 특징

- 실제 클래스를 상속 받아서 만들어졌기 때문에 실제 클래스와 겉 모양이 같다.
- 이론상으로 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 된다.
- 프록시 객체는 실제 객체의 참조(target)를 보관하지만, 호출 시에는 실제 객체의 메소드를 호출한다.
- 프록시 객체는 처음 사용할 때 한 번만 초기화
- 프록시 객체를 초기화 할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님, 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근 가능
- 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야함 (== 비교 실패, 대신 instance of 사용)
- 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티 반환
- 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면문제 발생
  (하이버네이트는 org.hibernate.LazyInitializationException 예외를 터트림)

> 프록시 확인

- 프록시 인스턴스의 초기화 여부 확인 : `PersistenceUnitUtil.isLoaded(Object entity)`
- 프록시 클래스 확인 방법 : `entity.getClass().getName()` 출력(..javasist.. or HibernateProxy…)
- 프록시 강제 초기화 : `org.hibernate.Hibernate.initialize(entity);`
- 참고: JPA 표준은 강제 초기화 없음 -> 강제 호출: member.getName()

2. 즉시 로딩과 지연 로딩

- 즉시 로딩 :

```java
@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name="USERNAME")
    private String name;
    
    @ManyToOne(fetch = FetchType.EAGER) //** 
    @JoinColumn(name = "TEAM_ID")
    private Team team;
    ..
}
```

- 지연 로딩 : 실제 객체를 사용하는 시점에서 초기화(DB 조회)를 진행하는 로딩 방식

```java
@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name="USERNAME")
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY) //** 
    @JoinColumn(name = "TEAM_ID")
    private Team team;
    ..
}
```

3. 지연 로딩 활용

4. 영속성 전이 : CASCADE

5. 고아 객체

6. 영속성 전이 + 고아 객체, 생명 주기

```java
```
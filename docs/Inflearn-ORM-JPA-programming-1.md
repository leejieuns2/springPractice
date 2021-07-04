# 자바 ORM 표준 JPA 프로그래밍 강의 내용 정리 (Section 1-4)

## 01. JPA 소개

### 1. ORM (Object-Relational Mapping)
- 객체 관계 매핑
- 객체는 객체대로 설계, 관계형 DB는 관계형 DB에 맞게 설계 진행
- ORM 프레임워크가 중간에서 객체와 관계형 DB를 매핑해준다.
- 대중적인 언어에는 대부분 ORM 기술이 존재할 정도로 매우 중요하다.

<br>

## 02. JPA 시작

### 1. JPA 구동 방식
1) Persistence -> META-INF/persistence.xml 을 통해 설정 정보 조회
2) EntityManagerFactory 생성
3) EntityManagerFactory를 이용해 EntityManager 생성
4) `@Entity` Annotation 과 `@Id` Annotataion을 정의해 JPA가 관리할 객체와 객체의 PK 매핑해줌.

### 💡 주의사항
- EntityManagerFactory는 하나만 새성해서 애플리케이션 전체에서 공유됨.
- EntityManager는 Thread간의 공유가 불가능하기 때문에 사용 후 close 해주어야 함.
- JPA의 모든 데이터 변경은 트랜잭션 안에서 실행되기 때문에 CRUD 실행 전 트랜잭션 생성해주어야 함.

  ```java
      EntityTransaction tx = entityManager.getTransaction();
      tx.begin();
      
      try {
        // write code
        tx.commit();
      } catch (Exception e) {
        tx.rollback();
      } finally {
        entityManager.close();
      }
  ```

### 2. JPQL
- JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어를 제공한다.
- SQL과 문법이 유사하며, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN을 지원한다.
- JPQL은 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 SQL로, 엔티티 객체를 대상으로 쿼리한다
- 특정 데이터베이스 SQL에 의존하지 않는다는 특징이 있다.

<br>

## 03. 영속성 관리

### 1. 영속성 컨텍스트
- 엔티티를 영구 저장하는 환경
- 눈에 보이지 않는 논리적인 개념
- EntityManager를 통해서 영속성 컨텍스트에 접근 가능.
- 영속성 컨텍스트의 이점
    - 1차 캐시
    - 동일성(identity) 보장
    - 트랜잭션을 지원하는 쓰기 지연 (transactional write-behind)
    - 변경 감지(Dirty Checking)
    - 지연 로딩(Lazy Loading)

### 2. Entity의 생명주기
1. 비영속 (new Transient)
    - 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
2. 영속 (managed)
    - 영속성 컨텍스트에 관리되는 상태
3. 준영속 (detached)
    - 영속성 컨텍스트에 저장되었다가 분리된 상태
4. 삭제 (Removed)
    - 삭제된 상태

### 3. 플러시
- 영속성 컨텍스트의 변경내용을 데이터베이스에 반영
- 변경 감지 -> 수정된 엔티티 쓰기 지연 SQL 저장소에 등록 -> 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송(등록, 수정, 삭제 쿼리)
- 플러시는 영속성 컨텍스트를 비우지는 않으며, 변경 내용을 데이터베이스에 동기화하는 것.
- 트랜잭션 작업 단위가 중요하기 때문에 커밋 직전에만 동기화 진행하면 됨.

<br>

## 04. 엔티티 매핑

### 1. `@Entity`
- `@Entity`가 붙은 클래스는 JPA가 관리하기 때문에 JPA를 사용해서 테이블과 매핑하려면 꼭 추가해야한다.

> 사용 시 주의사항
- 기본 생성자 필수 (파라미터가 없는 public or protected 생성자 필요)
- final class, enum, interface, inner class 사용 불가능.
- 저장할 필드에 final 사용 불가능.

- 속성 종류
    1) name : JPA에서 사용할 엔티티 이름 지정. default value는 class name. 가급적 기본값 사용 권장.


### 2. `@Table`
- 엔티티와 매핑할 테이블 지정
- 속성 : name, catalog, schema, uniqueConstraints


### 3. 데이터베이스 스키마 자동생성 (hibernate.hbm2ddl.auto)
- DDL을 애플리케이션 실행 시점에 자동으로 생성해줌.
- 특정 데이터베이스에 종속되지 않고 설정한 Dialect에 맞춰 적절한 DDL 생성
- 이렇게 생성된 DDL은 개발 장비에서만 사용됨.
- 옵션 종류 및 설명
    1) `<property name="hibernate.hbm2ddl.auto" value="create" />`   
       : 기존 테이블 삭제 후 다시 생성 ( DROP + CREATE )
    2) `<property name="hibernate.hbm2ddl.auto" value="create-drop" />`   
       : CREATE와 같으나 종료 시점에 테이블 DROP
    3) `<property name="hibernate.hbm2ddl.auto" value="update" />`   
       : 변경분만 반영 (운영 DB에는 사용하면 절대 안됨.)
    4) `<property name="hibernate.hbm2ddl.auto" value="validate" />`   
       : 엔티티와 테이블이 정상 매핑되었는지 확인함.
    5) `<property name="hibernate.hbm2ddl.auto" value="none" />`   
       : 사용하지 않음.

  ### 💡 주의사항
    - 운영 장비에는 절대 create, create-drop, update 사용 금지.
    - 개발 초기 단계에는 create, update 권장.
    - 테스트 서버에는 update, validate 권장.
    - 스테이징과 운영 서버에는 validate, none 권장.


### 4. 필드와 컬럼 매핑
1. `@Column` : 컬럼 매핑
2. `@Temporal` : 날짜 타입 매핑
    - JAVA 1.8 이상에서는 LocalDate, LocalDateTime을 제공하므로 생략 가능함.
3. `@Enumerated` : enum 타입 매핑
    - 사용 시 value 속성을 EnumType.STRING 으로 설정할 것을 권장함. (default value ="EnumType=ORDINAL")
4. `@Lob` : BLOB, CLOB 매핑
5. `@Transient` : 특정 필드를 컬럼에 매핑하지 않음 (매핑 무시)
    - 주로 메모리상에서만 임시로 어떤 값을 보관하고 싶을 때 사용함.


### 5. 기본 키 매핑
1. `@Id` : Primary Key 설정
2. `@GeneratedValue` : 자동 생성시 사용. 특정 데이터베이스에 맞게 자동적으로 매핑해줌.
    - strategy 속성 : 어떤 데이터베이스를 사용하는지 설정해줌. (IDENTITY, AUTO, SEQUENCE, TABLE)

   ### 권장하는 식별자 전략
    - 기본 키 제약 조건 준수 (Not null, unique, immutable)
    - Long형 컬럼 + 대체키 + 키 생성 전략 사용을 권장.

<br>

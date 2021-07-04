# 스프링 부트의 개념과 활용 강의 내용 정리

## 3-1. 자동 설정 이해 (Auto Configuration)

- `@SpringBootApplication` (기본 Application Annotation)
    - 위의 어노테이션은 사실 세 단계로 나누어 읽힌다.
        1. `@SpringBootConfiguration`
        2. `@ComponentScan`
        3. `@EnableAutoConfiguration`

- `@ComponentScan` 설정 시 같은 경로 내 모든 어노테이션을 읽어 자동으로 Bean 설정을 수행한다.
    - `@Component`, `@Configuration`, `@Repository`, `@Service`, `@Controller`, `@RestController`

- `@EnableAutoConfiguration`

    - spring.factories(org.springframework.boot.autoconfigure.EnableAutoConfiguration)안에 수많은 자동 설정들이 적용이 되어 있음.

    - 조건에 따라 Auto Configuration이 적용되어 수많은 Bean이 생성됨.

## 4-1. Type-Safe Property

- `@ConfigurationProperties` Annotation을 사용해 Properties Class 정의 가능.

- 여러 프로퍼티를 묶어서 읽어올 수 있으며, 빈으로 등록해 다른 빈에 주입할 수 있음.   
  (`@EnableConfigurationProperties`, `@Component`, `@Bean` Annotation 활용하기)


## 4-2. Profile

- `@Configuration`, `@Component` Annotation과 함께 `@Profile` Annotation을 같이 사용.

- 어떤 프로파일을 활성화 할 것인가 ? `spring.profiles.active`

- 어떤 프로파일을 추가할 것인가 ? (상속) `spring.profiles.include`

  > 💡 Spring Framework가 update 되면서, active와 include를 같이 사용할 수 없게 됨.

- Profile 용 Property 네이밍 ? application-((profile name)).properties


## 4-3. Thymeleaf

- 스프링 부트가 자동 설정을 지원하는 템플릿 엔진 : FreeMarker, Groovy, Thymeleaf, Mustache

- JSP를 권장하지 않는 이유
    1. JAR 패키징 불가능. JSP를 사용할 경우 WAR 패키징 해야 함.
    2. 서블릿 엔진 중 Undertow는 jsp를 아예 지원하지 않음.


## 4-4. Spring HATEOAS (Hypermedia As The Engine Of Application State)

- 서버 : 현재 리소스와 연관된 링크 정보를 클라이언트에게 제공한다.

- 클라이언트 : 연관된 링크 정보를 바탕으로 리소스에 접근한다.

- 연관된 링크 정보 : Relation + Hypertext Reference

- `spring-boot-starter-hateoas` dependency 추가해서 사용 가능.

- 고급 REST API 구축 시 사용됨.


## 4-5. ORM (Object-Relational Mapping)

- 객체와 릴레이션을 맵핑할 때 발생하는 개념적 불일치를 해결하는 프레임워크


## 4-6. JPA (Java Persistence API)

- ORM을 위한 자바(IEE) 표준

- Repository Bean 자동 생성

- Query Method 자동 생성

- @EnableJpaRepositories (스프링 부트에서 자동 생성)


## 4-7. JPA를 사용한 데이터베이스 초기화

> `application.properties` 에서 다음과 같은 문장을 설정해주어야 자동으로 스키마를 생성할 수 있음.

1. `spring.jpa.hibernate.ddl-auto=create` : 어플리케이션 실행시 스키마 생성. 데이터 유지 불가능.

2. `spring.jpa.hibernate.ddl-auto=update` : 추가된 스키마만 변경. (기존 데이터 유지 가능)

3. `spring.jpa.hibernate.ddl-auto=create-drop` : 처음에 만들고, 애플리케이션 종료시 스키마 지움. 데이터 유지 불가능.

4. `spring.jpa.hibernate.validate` : Mapping이 정상적으로 동작하는지 검증하는 역할을 수행함.

+) `spring.jpa.generate-ddl=true` 도 같이 설정해야 함.

+) `spring.jpa.show-sql=true` 설정 시 실행할 때 SQL 생성 여부나 정보를 확인할 수 있음.


## 4-8. Database Migration

- Flyway, Liquibase를 사용해 주로 프로젝트 내에서 Migration을 진행함.

- `org.flywaydb:flyway-core` dependency 추가하기

- directory PATH : /resource/db/migration

- application.properties에서 `spring.flyway.locations`로 classpath 변경 가능.

- migration file naming : V숫자__이름.sql
    1) V는 꼭 대문자로, 숫자와 이름 사이에 __ 언더바 두개 필수
    2) 숫자는 순차적으로, 이름은 가능한 서술적으로 네이밍 하기.


## 4-9. Spring Security

- 스프링에서 자체적으로 제공해주는 보안 관련 기능

- 웹 시큐리티, 메소드 시큐리티, 다양한 인증 방법 지원.

- Dependency 추가
  ```xml
      <!-- Security -->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-security</artifactId>
      </dependency>
  ```   

- dependency 추가 후 localhost 진입 시 기본 사용자 로그인 페이지로 자동 redirect됨. (/login)
> 기본 사용자 생성
1. Username: user
2. Password: 애플리케이션을 실행할 때 마다 랜덤 값 생성 (콘솔에 출력 됨.)


## 4-10. Spring REST Client

1. RestTemplate
- Blocking I/O 기반의 Synchronous API (순차적 실행)

- RestTemplateAutoConfiguration

- 기본으로 java.net.HttpURLConnection 사용.

- 커스터마이징 가능. 커스터마이징 시에는 RestTempleteCustomizer 객체 생성 후 빈 재정의

  ```java
  @Bean
  public RestTempleteCustomizer restTempleteCustomizer() {
      return restTemplete -> restTemplete.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }
  ```   

- 프로젝트에 spring-web 모듈이 있다면 RestTemplateBuilder를 빈으로 등록해 줍니다.

> [참고자료](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#rest-client-access)

2. WebClient

- Non-Blocking I/O 기반의 Asynchronous API

- WebClientAutoConfiguration

- 기본으로 Reactor Netty의 HTTP 클라이언트 사용.

- 프로젝트에 spring-webflux 모듈이 있다면 WebClient.Builder를 빈으로 등록해줍니다.

- 커스터마이징 가능. 커스터마이징 시에는 RestTempleteCustomizer 객체 생성 후 빈 재정의

  ```java
  @Bean
  public WebClientCustomizer webClientCustomizer() {
      return webClientBuilder -> webClientBuilder.baseUrl("http://localhost:8080");
  }
  ```   

> [참고자료](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-client)


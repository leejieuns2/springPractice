package main;

import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain6 {

    public static void main(String[] args) {

        // persistence.xml에 정의한 persistence-unitname을 인스턴스로 전달.
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");

        // EntityManager는 Thread간의 공유 절대 X (쓰고 Close 해주어야 함.)
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행됨.
        EntityTransaction tx = entityManager.getTransaction();

        // 트랜잭션 시작
        tx.begin();

        // 예외처리 수행
        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            entityManager.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            entityManager.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(0);
            member1.setTeam(teamA);
            entityManager.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(0);
            member2.setTeam(teamA);
            entityManager.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(0);
            member3.setTeam(teamB);
            entityManager.persist(member3);

            // flush 자동 호출되므로 필요 없음.
//            entityManager.flush();
            
            // 벌크 연산
            // 벌크 연산을 먼저 실행하고 영속성 컨텍스트 초기화 진행
            int resultCount = entityManager.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount = " + resultCount);

            // 벌크 연산에 적용된 부분은 DB에 적용됐을 뿐, 객체에는 적용되지 않았기 때문에 영속성 컨텍스트를 초기화해주어야 한다.
            entityManager.clear();

            // 만약 초기화를 진행하지 않고 아래의 문장을 실행했을 경우 age가 0으로 출력된다.
            Member findMember = entityManager.find(Member.class, member1.getId());
            System.out.println("findMember.getAge() = " + findMember.getAge());
            // 트랜잭션 커밋
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}

package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain4 {

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

            Team team = new Team();
            team.setName("TeamA");

            entityManager.persist(team);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(team);
            entityManager.persist(member1);

            entityManager.flush();
            entityManager.clear();

            Member m = entityManager.find(Member.class, member1.getId());

            System.out.println("m = " + m.getTeam().getClass());

            System.out.println("===================");
            m.getTeam().getName(); // 이 때 프록시 초기화
            System.out.println("===================");

            // 트랜잭션 커밋
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }
}


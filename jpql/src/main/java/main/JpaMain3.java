package main;

import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain3 {

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
            team.setName("teamA");
            entityManager.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자");
            member1.setAge(10);
            member1.setTeam(team);
            entityManager.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);

            entityManager.persist(member2);

            entityManager.flush();
            entityManager.clear();

            String query = "select " + "case when m.age <= 10 then '학생요금' "
                                         + " when m.age >= 60 then '경로요금' else '일반요금' end " + "from Member m";

            List<String> result = entityManager.createQuery(query, String.class).getResultList();

            for(String s : result) {
                System.out.println("s = " + s);
            }

            String query2 = "select coalesce(m.username, '이름 없는 회원') as username " + "from Member m ";

            List<String> result2 = entityManager.createQuery(query2, String.class).getResultList();

            for(String s : result2) {
                System.out.println("s = " + s);
            }

            String query3 = "select nullif(m.username, '관리자') as username " + "from Member m ";

            List<String> result3 = entityManager.createQuery(query3, String.class).getResultList();

            for(String s : result3) {
                System.out.println("s = " + s);
            }

            String query4 = "select group_concat(m.username) From Member m";

            List<String> result4 = entityManager.createQuery(query4, String.class).getResultList();

            for(String s : result4) {
                System.out.println("s = " + s);
            }


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

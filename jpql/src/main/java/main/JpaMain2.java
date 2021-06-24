package main;

import jpql.Member;
import jpql.MemberType;
import jpql.Team;

import javax.persistence.*;
import java.util.List;

public class JpaMain2 {

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

            for(int i = 0; i < 100; i++) {
                Team team = new Team();
                team.setName("teamA");
                entityManager.persist(team);

                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                member.setTeam(team);

                entityManager.persist(member);
            }

            entityManager.flush();
            entityManager.clear();

            // 페이징 api 예시
            List<Member> result = entityManager.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result. size = " + result.size());

            for (Member member1 : result) {
                System.out.println("member1 = " + member1.toString());
            }

            String query = "select m from Member m left outer join m.team t";
            String query2 = "select m from Member m, Team t where m.username = t.name";
            List<Member> rslt3 = entityManager.createQuery(query2, Member.class).getResultList();

            String query3 = "select m.username, 'Hello', TRUE from Member m "
                    + "where m.type = :userType";

            List<Object[]> result3 = entityManager.createQuery(query3)
                    .setParameter("userType", MemberType.ADMIN)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            for(Object[] objects : result3) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
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

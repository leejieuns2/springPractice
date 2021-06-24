package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

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

            /* DB Insert
            Member member = new Member();

            member.setId(1L);
            member.setName("HelloA");

            member.setId(2L);
            member.setName("HelloB"); // 비영속 상태 (new transient)

            // Insert할 때만 persist method 호출. update시에는 필요 없음.
            entityManager.persist(member); // 영속 상태 (managed)

            // 회원 엔티티를 영속성 컨텍스트에서 분리, 특정 엔티티(member)만 준영속 상태
            entityManager.detach(member);

            // 객체를 삭제한 상태 (삭제)
            entityManager.remove(member);
            */

            /* DB select
            Member findMember = entityManager.find(Member.class, 1L);
            System.out.println("findMember id = " + findMember.getId());
            System.out.println("findMember name = " + findMember.getName());
             */

            /* DB update
            findMember.setName("HelloJPA");
             */

            // 자바 객체를 대상으로 쿼리를 작성하기 때문에 일반적인 query랑 약간 다름.
            entityManager.createQuery("select m from Member as m", Member.class).getResultList();

            // 트랜잭션 커밋
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}

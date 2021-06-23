package jpql;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);

            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

            // 타입이 명확할 경우 TypedQuery 사용
            TypedQuery<Member> query = entityManager.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = entityManager.createQuery("select m.username from Member m", String.class);

            // 타입이 명확하지 않을 경우는 Query 사용
            Query query3 = entityManager.createQuery("select m.username, m.age from Member m");

            List<Member> result = entityManager.createQuery("select m from Member m", Member.class).getResultList();

            Member findMember = result.get(0);
            findMember.setAge(20);

            // getSingleResult는 결과가 하나가 아니면 에러 출력됨. 사용 시 주의.
            // Spring Data JPA -> Exception 처리 없이 사용하거나 try-catch 사용해서 에러 처리 해줌.
            Member rslt = query.getSingleResult();
            System.out.println("result = " + rslt.getUsername());

            List<MemberDTO> result2 = entityManager.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class).getResultList();

            MemberDTO memberDTO = result2.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());

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

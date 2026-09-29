package hello.servlet.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();
    // new MemberRepository 를 안쓰는 이유
    // 싱글톤이기 때문에 못씀 -> 스프링 사용하면 싱글톤 쓸 필요X

    // Test 끝날때마다 afterEach() 실행하여 깔끔하게 초기화 (실행 순서 보장X)
    @AfterEach
    void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void save(){
        //given  이런게 주어졌을 때
        Member member = new Member("hello", 20);

        //when 이런걸 실행했을 때
        Member savedMember = memberRepository.save(member);

        //then 결과가 이거여야만 해
        Member findMember = memberRepository.findById(savedMember.getId());
        assertThat(findMember).isEqualTo(savedMember);
    }


    @Test
    void findAll(){
        // given  이런게 주어졌을 때
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member1", 30);

        memberRepository.save(member1);
        memberRepository.save(member2);


        //when 이런걸 실행했을 때
        List<Member> result = memberRepository.findAll();

        //then 결과가 이거여야만 해
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(member1,member2);
    }
}

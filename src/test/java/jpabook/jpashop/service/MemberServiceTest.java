package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // 스프링이 실제 작동하는거를 확인하기 위해
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberService memberService; //테스트 케이스므로 필드주입
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;
    @Test
    @Rollback(false) // jpa에 의해 insert문이 나가기전에 rollback 하지만 //false 하면 실제 insert가 나가는 것을 확인가능
    @DisplayName("회원가입")
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");
        //when
        Long savedID = memberService.join(member);
        //then
        assertEquals(member,memberRepository.findOne(savedID));
    }
    @Test
    @DisplayName("회원 중복 테스트")
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        try{
            memberService.join(member2); //예외 발생
        }catch(IllegalStateException e){
            return;
        }
        //then
        fail("예외가 발생해야 한다."); //fail -> 코드가 잘못된것 즉 테스트가 이상하다.
    }

}
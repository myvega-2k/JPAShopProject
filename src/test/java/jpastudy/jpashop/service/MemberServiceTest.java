package jpastudy.jpashop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jpastudy.jpashop.domain.Member;
import jpastudy.jpashop.repository.MemberRepository;

@SpringBootTest
@Transactional
public class MemberServiceTest {
	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;

	@Test
	public void 회원가입() throws Exception {
		//Given
		Member member = new Member();
		member.setName("boot");
		//When
		Long saveId = memberService.join(member);
		//Then
		assertEquals(member, memberRepository.findOne(saveId));
	}

//	@Test
//	public void 중복_회원_예외() throws Exception {
//		//Given
//		Member member1 = new Member();
//		member1.setName("boot");
//		Member member2 = new Member();
//		member2.setName("boot");
//		IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> {
//			//When
//			memberService.join(member1);
//			memberService.join(member2); // 예외가 발생해야 한다.
//		});
//		//Then
//		assertEquals("이미 존재하는 회원입니다. ", exception.getMessage());                                                                                                                                                                                                                                                                          
//	}
}

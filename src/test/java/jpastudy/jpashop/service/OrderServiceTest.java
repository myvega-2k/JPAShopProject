package jpastudy.jpashop.service;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jpastudy.jpashop.domain.Address;
import jpastudy.jpashop.domain.Member;
import jpastudy.jpashop.domain.Order;
import jpastudy.jpashop.domain.OrderStatus;
import jpastudy.jpashop.domain.item.Book;
import jpastudy.jpashop.domain.item.Item;
import jpastudy.jpashop.exception.NotEnoughStockException;
import jpastudy.jpashop.repository.OrderRepository;

@SpringBootTest
@Transactional
public class OrderServiceTest {
	@PersistenceContext
	EntityManager em;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderRepository orderRepository;

	@Test
	public void 상품주문() throws Exception {
		// Given
		Member member = createMember("회원1", new Address("서울", "성내로", "80"));
		Item item = createBook("스프링 부트", 10000, 10); // 이름, 가격, 재고
		int orderCount = 2;
		// When
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
		// Then
		Order getOrder = orderRepository.findOne(orderId);
		assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
		assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
		assertEquals("주문 가격은 가격 * 수량이다.", 10000 * 2, getOrder.getTotalPrice());
		assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, item.getStockQuantity());
	}

	private Member createMember(String name, Address address) {
		Member member = new Member();
		member.setName(name);
		member.setAddress(address);
		em.persist(member);
		return member;
	}

	private Book createBook(String name, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setStockQuantity(stockQuantity);
		book.setPrice(price);
		em.persist(book);
		return book;
	}

//	@Test
//	public void 상품주문_재고수량초과() throws Exception {
//		// Given
//		Member member = createMember("회원1", new Address("서울", "성내로", "80"));
//		Item item = createBook("스프링 부트", 10000, 10); // 이름, 가격, 재고
//		int orderCount = 11; // 재고보다 많은 수량
//		NotEnoughStockException exception = Assertions.assertThrows(NotEnoughStockException.class, () -> {
//			// When
//			orderService.order(member.getId(), item.getId(), orderCount);
//		});
//		// Then
//		Assertions.assertEquals("need more stock", exception.getMessage());
//	}

	@Test
	public void 주문취소() {
		// Given
		Member member = createMember("회원1", new Address("서울", "성내로", "180"));
		Item item = createBook("스프링 부트", 10000, 10); // 이름, 가격, 재고
		int orderCount = 2;
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
		// When
		orderService.cancelOrder(orderId);
		// Then
		Order getOrder = orderRepository.findOne(orderId);
		assertEquals("주문 취소시 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getStatus());
		assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
	}

}

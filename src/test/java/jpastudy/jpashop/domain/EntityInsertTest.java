package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class EntityInsertTest {
    @Autowired
    EntityManager em;

    @Test
    @Rollback(value = false)
    public void insert() throws Exception {
        //주문 생성
        Order order = new Order();

        //회원 생성
        Member member = new Member();
        member.setName("부트");
        Address address = new Address("서울", "11번지", "05401");
        member.setAddress(address);
        em.persist(member);
        //주문에 회원정보 저장
        order.setMember(member);

        //배송 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);
        //주문에 배송정보 저장
        order.setDelivery(delivery);

        //상품생성
        Book book = new Book();
        book.setName("자바책");
        book.setPrice(1000);
        book.setStockQuantity(20);
        book.setAuthor("김저자");
        book.setIsbn("1234ab");
        em.persist(book);

        //주문상품 생성
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderPrice(1000);
        orderItem.setCount(5);
        
        //주문상품에 상품을 저장
        orderItem.setItem(book);
        //주문에 주문상품을 저장
        order.addOrderItem(orderItem);

        //주문에 주문날짜 저장
        order.setOrderDate(LocalDateTime.now());
        //주문에 주문상태 저장
        order.setStatus(OrderStatus.ORDER);

        //주문을 저장
        em.persist(order);
        
    }
}
package jpastudy.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jpastudy.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item; // 주문 상품

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order; // 주문

	private int orderPrice; // 주문 가격

	private int count; // 주문 수량

	// ==생성 메서드==//
	public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setOrderPrice(orderPrice);
		orderItem.setCount(count);
		item.removeStock(count);
		return orderItem;
	}

	// ==비즈니스 로직 : 주문 취소 ==//
	public void cancel() {
		getItem().addStock(count);
	}

	// ==비즈니스 로직 : 주문상품 전체 가격 조회 ==//
	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}

}

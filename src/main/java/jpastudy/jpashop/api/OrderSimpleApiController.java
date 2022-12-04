package jpastudy.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpastudy.jpashop.domain.Address;
import jpastudy.jpashop.domain.Order;
import jpastudy.jpashop.domain.OrderStatus;
import jpastudy.jpashop.domain.dto.OrderSearch;
import jpastudy.jpashop.repository.OrderRepository;
import jpastudy.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpastudy.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * xToOne(ManyToOne, OneToOne) 관계 최적화 Order, Order -> Member , Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;

	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		List<Order> all = orderRepository.findAll(new OrderSearch());
		for (Order order : all) {
			order.getMember().getName(); // Lazy 강제 초기화
			order.getDelivery().getAddress(); // Lazy 강제 초기화
		}
		return all;
	}

	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> ordersV2() {
		List<Order> orders = orderRepository.findAll(new OrderSearch());
		List<SimpleOrderDto> result = orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
		return result;
	}

	/**
	 * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용함) fetch join으로 쿼리 1번 호출
	 */
	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDto> ordersV3() {
		List<Order> orders = orderRepository.findAllWithMemberDelivery();
		List<SimpleOrderDto> result = orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
		return result;
	}

	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4() {
		return orderSimpleQueryRepository.findOrderDtos();
	}

	@Data
	static class SimpleOrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;

		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName(); // Lazy 강제 초기화
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress(); // Lazy 강제 초기화
		}
	}

}

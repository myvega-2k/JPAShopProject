package jpastudy.jpashop.repository.order.query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
private final EntityManager em;

	/**
	* 1:N 관계(컬렉션)를 제외한 Order, Member, Delivery를 한번에 조회
	*/
	private List<OrderQueryDto> findOrders() {
	return em.createQuery(
		"select new jpastudy.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate,o.status, d.address)" +
		" from Order o" +
		" join o.member m" +
		" join o.delivery d", OrderQueryDto.class)
		.getResultList();
	} //findOrders

	private List<OrderItemQueryDto> findOrderItems(Long orderId) {
		return em.createQuery(
		"select new jpastudy.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name,oi.orderPrice, oi.count)" +
		" from OrderItem oi" +
		" join oi.item i" +
		" where oi.order.id = : orderId", OrderItemQueryDto.class)
		.setParameter("orderId", orderId)
		.getResultList();
		} //findOrderItems

	public List<OrderQueryDto> findOrderQueryDtos() {
		//루트 조회(toOne 코드를 모두 한번에 조회)
		List<OrderQueryDto> result = findOrders();
		//루프를 돌면서 컬렉션 추가(추가 쿼리 실행)
		result.forEach(o -> {
		List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
		o.setOrderItems(orderItems);
		});
		return result;
	} //findOrderQueryDtos
	
	//OrderId 목록
    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(o -> o.getOrderId())
                .collect(toList());
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpastudy.jpashop.repository.order.query.OrderItemQueryDto("+
                        "oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        return orderItems.stream().collect(groupingBy(orderItem -> orderItem.getOrderId()));
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        //toOne 코드를 모두 한번에 조회
        List<OrderQueryDto> result = findOrders();
        //orderItem 컬렉션을 MAP 한방에 조회
        Map<Long, List<OrderItemQueryDto>> orderItemMap =
                findOrderItemMap(toOrderIds(result));
        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행X)
        result.forEach(order -> order.setOrderItems(orderItemMap.get(order.getOrderId())));
        return result;
    }
	
}

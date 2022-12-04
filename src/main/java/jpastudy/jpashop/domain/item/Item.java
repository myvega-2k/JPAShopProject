package jpastudy.jpashop.domain.item;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import jpastudy.jpashop.domain.Category;
import jpastudy.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {
	@Id
	@GeneratedValue
	@Column(name = "item_id")
	private Long id;

	private String name;

	private int price;

	private int stockQuantity;

	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<Category>();

	// ==비즈니스 로직==//
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}

	public void removeStock(int quantity) {
		int restStock = this.stockQuantity - quantity;
		if (restStock < 0) {
			throw new NotEnoughStockException("need more stock");
		}
		this.stockQuantity = restStock;
	}

}

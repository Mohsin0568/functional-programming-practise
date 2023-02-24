/**
 * 
 */
package com.systa.practise.functional.programming;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.systa.practise.functional.programming.model.Order;
import com.systa.practise.functional.programming.model.Product;
import com.systa.practise.functional.programming.repository.OrderRepo;
import com.systa.practise.functional.programming.repository.ProductRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mohsin
 *
 */

@Slf4j
@DataJpaTest
public class StreamAPITestTwo {
	
	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private ProductRepo productRepo;
	
	@Test
	void test1() {
		
		List<Product> result = 
		productRepo.findAll()
			.stream()
			.filter(product -> product.getCategory().equalsIgnoreCase("Books"))
			.filter(product -> product.getPrice() > 100)
			.collect(Collectors.toList());
		
		result.stream().forEach(product -> log.info(product.toString()));
	}
	
	@Test
	void test2() {
		
		List<Order> result = orderRepo.findAll()
			.stream()
			.filter(order -> order.getProducts()
						.stream()
						.anyMatch(product -> product.getCategory().equalsIgnoreCase("Baby")))
			.collect(Collectors.toList());
		
	}
	
	@Test
	void test3() {
		List<Product> result = productRepo.findAll()
			.stream()
			.filter(product -> product.getCategory().equalsIgnoreCase("Toys"))
			.map(product -> product.withPrice(product.getPrice()*0.9))
			.collect(Collectors.toList());
	}
	
	@Test
	void test4() {
		List<Product> result = orderRepo.findAll()
			.stream()
			.filter(order -> order.getCustomer().getTier() == 2)
			.filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 02, 01)))
			.filter(order -> order.getOrderDate().isBefore(LocalDate.of(2021, 02, 28)))
			.flatMap(order -> order.getProducts().stream())
			.distinct()
			.collect(Collectors.toList());
	}
	
	@Test
	void test5() {
		Optional<Product> cheapestProduct = productRepo.findAll()
			.stream()
//			.filter(order -> order.getCategory().equalsIgnoreCase("Books"))
			.min(Comparator.comparing(product -> product.getPrice()));
		
		log.info(cheapestProduct.get().toString());
			
	}
	
	@Test
	void test6() {
		
		orderRepo.findAll()
			.stream()
			.sorted(Comparator.comparing(Order :: getOrderDate).reversed())
			.limit(3);
		
	}
	
	@Test
	void test7() {
		
		orderRepo.findAll()
			.stream()
			.filter(order -> order.getDeliveryDate().isEqual(LocalDate.of(2021, 03, 15)))
			.peek(null)
			.map(order -> order.getProducts().stream())
			.distinct()
			.collect(Collectors.toList());
		
	}
	
	@Test
	void test8() {
		
		orderRepo.findAll()
			.stream()
			.filter(order -> order.getDeliveryDate().getMonthValue() == 2 && order.getDeliveryDate().getYear() == 2021)
			.flatMap(order -> order.getProducts().stream())
			.mapToDouble(product -> product.getPrice())
			.sum();
	}
	
	@Test
	void test9() {
		
		orderRepo.findAll()
			.stream()
			.filter(order -> order.getDeliveryDate().isEqual(LocalDate.of(2021, 03, 14)))
			.flatMap(order -> order.getProducts().stream())
			.mapToDouble(product -> product.getPrice())
			.average();
		
	}
	
	@Test
	void test10() {
		
		productRepo.findAll()
		.stream()
		.filter(order -> order.getCategory().equalsIgnoreCase("Books"))
		.mapToDouble(product -> product.getPrice())
		.summaryStatistics();
		
	}
	
	@Test
	void test11() {
		
		Map<Object, Integer> ordersWithProductCount = orderRepo.findAll()
			.stream()
			.collect(Collectors.toMap(order -> order.getId(), order -> order.getProducts().size()));
		
	}
	
	@Test
	void test12() {
		
		orderRepo.findAll()
			.stream()
			.collect(Collectors.groupingBy(order -> order.getCustomer()));
		
	}
	
	@Test
	void test13() {
		
		orderRepo.findAll()
			.stream()
			.collect(Collectors.toMap(Function.identity(),
					order -> order.getProducts().stream().mapToDouble(product -> product.getPrice()).sum()));
		
	}
	
	@Test
	void test14() {
		
		productRepo.findAll()
			.stream()
			.collect(Collectors.groupingBy(Product :: getCategory, 
						Collectors.mapping(product -> product.getName(), Collectors.toList())));
		
	}
	
	@Test
	void test15() {
		
		productRepo.findAll()
			.stream()
			.collect(Collectors.groupingBy(
						Product :: getCategory,
						Collectors.maxBy(Comparator.comparing(Product :: getPrice))
					));
		
	}

}

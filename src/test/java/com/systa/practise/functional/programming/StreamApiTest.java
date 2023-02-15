/**
 * 
 */
package com.systa.practise.functional.programming;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.systa.practise.functional.programming.model.Customer;
import com.systa.practise.functional.programming.model.Order;
import com.systa.practise.functional.programming.model.Product;
import com.systa.practise.functional.programming.repository.CustomerRepo;
import com.systa.practise.functional.programming.repository.OrderRepo;
import com.systa.practise.functional.programming.repository.ProductRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mohsin
 *
 */

@Slf4j
@DataJpaTest
public class StreamApiTest {
	
	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private ProductRepo productRepo;
	
	@Test
	void excercise1() {
		
		List<Product> filteredProducts = productRepo.findAll()
			.stream()
			.filter(product -> product.getCategory().equalsIgnoreCase("Books"))
			.filter(product -> (product.getPrice() > 100d))
			.collect(Collectors.toList());
		
		filteredProducts.stream().forEach(product -> log.info(product.toString()));
		
	}
	
	@Test
	void excercise2() {
		
		List<Order> result  = orderRepo.findAll().stream().filter(order -> {
			return order.getProducts().stream().anyMatch(product -> product.getCategory().equalsIgnoreCase("Baby"));
		}).collect(Collectors.toList());
		
		result.stream().forEach(order -> {
			log.info("--- {}", order.toString());
			order.getProducts().stream().forEach(product -> log.info(product.toString()));
		});
	}
	
	@Test
	void excercise3() {
		List<Product> result = productRepo.findAll()
			.stream()
			.filter(product -> product.getCategory().equalsIgnoreCase("Toys"))
			.map(product -> product.withPrice(product.getPrice() * 0.9))
			.collect(Collectors.toList());
		
		result.stream().forEach(product -> log.info(product.toString()));			
	}
	
	@Test
	void excercise4() {
		List<Product> products = orderRepo.findAll()
			.stream()
			.filter(order -> order.getCustomer().getTier()==2)
			.filter(order -> order.getOrderDate().compareTo(LocalDate.of(2021, 2, 1))>=0)
			.filter(order -> order.getOrderDate().compareTo(LocalDate.of(2021, 4, 1))<=0)
			.flatMap(order -> order.getProducts().stream())
			.distinct()
			.collect(Collectors.toList());
		
		products.stream().forEach(product -> log.info(product.toString()));
			
	}
	
	@Test
	void excercise5() {
		
		Optional<Product> result = productRepo.findAll()
			.stream()
			.filter(product -> product.getCategory().equalsIgnoreCase("Books"))
			.min(Comparator.comparing(Product :: getPrice));
		
		// or we can do like below also 
		
		Optional<Product> result1 = productRepo.findAll()
				.stream()
				.filter(product -> product.getCategory().equalsIgnoreCase("Books"))
				.sorted(Comparator.comparing(Product :: getPrice))
				.findFirst();			
	}
	
	@Test
	void excercise6() {
		
		List<Order> result = orderRepo.findAll()
			.stream()
			.sorted(Comparator.comparing(Order :: getOrderDate).reversed())
			.limit(3)
			.collect(Collectors.toList());
		
		assertEquals(3, result.size());
		result.stream().forEach(order -> log.info(order.toString()));
	}
	
	@Test
	void excercise7() {
		
		List<Product> result = orderRepo.findAll()
			.stream()
			.filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021, 03, 15)))
			.peek(order -> log.info(order.toString()))
			.flatMap(order -> order.getProducts().stream())
			.distinct()
			.collect(Collectors.toList());
		
		result.stream().forEach(product -> log.info(product.toString()));
		
	}
	
	@Test
	void excercise8() {
		double totalOrderValue = orderRepo.findAll()
			.stream()
			.filter(order -> order.getOrderDate().getMonthValue() == 2)
			.filter(order -> order.getOrderDate().getYear() == 2021)
			.mapToDouble(order -> {
				return order.getProducts().stream().mapToDouble(product -> product.getPrice()).sum();
			}).sum();
		
		assertEquals(11995.36, totalOrderValue);
			
	}
	
	@Test
	void excercise9() {
		double average = orderRepo.findAll()
				.stream()
				.filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021, 03, 15)))
				.flatMap(order -> order.getProducts().stream())
				.mapToDouble(product -> product.getPrice())
				.average().getAsDouble();
		
		assertEquals(352.89, average);
				
	}
	
	@Test
	void excercise10() {
		DoubleSummaryStatistics statistics = productRepo.findAll()
			.stream()
			.mapToDouble(product -> product.getPrice())
			.summaryStatistics();
		
		assertEquals(451.3333333333333d, statistics.getAverage());
		assertEquals(13540.0d, statistics.getSum());
		assertEquals(988.49, statistics.getMax());
		assertEquals(12.66, statistics.getMin());
		assertEquals(30, statistics.getCount());
	}
	
	@Test
	void excercise11() {
		
		Map<Object, Integer> result = orderRepo.findAll()
			.stream()
			.collect(Collectors.toMap(order -> order.getId(), order -> order.getProducts().size()));
		
		log.info(result.toString());
		
	}
	
	@Test
	void excercise12() {
		
		Map<Customer, List<Order>> result = orderRepo.findAll()
			.stream()
			.collect(Collectors.groupingBy(Order :: getCustomer));
		
		log.info(result.toString());
	}
	
	@Test
	void excercise13() {
		
		Map<Object, Double> result = orderRepo.findAll()
			.stream()
			.collect(Collectors.toMap(order -> order.getId(), 
						order -> order.getProducts()
								.stream()
								.mapToDouble(product -> product.getPrice())
								.sum()));
		
		log.info(result.toString());
		
	}
	
	@Test
	void excercise14() {
		
		Map<String, List<String>> result = productRepo.findAll()
			.stream()
			.collect(Collectors.groupingBy(
						Product :: getCategory,
						Collectors.mapping(product -> product.getName(), Collectors.toList())
						)
					);
		
		log.info(result.toString());
		
	}
	
	@Test
	void excercise15() {
		
		Map<String, Optional<Product>> result = productRepo.findAll()
			.stream()
			.collect(Collectors.groupingBy(
						Product :: getCategory,
						Collectors.maxBy(Comparator.comparing(Product :: getPrice))
						
					));
		
	}

}

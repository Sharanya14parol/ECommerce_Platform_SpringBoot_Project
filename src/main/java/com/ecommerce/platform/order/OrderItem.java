package com.ecommerce.platform.order;

import com.ecommerce.platform.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderItem {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	 @ManyToOne
	 @JoinColumn(name = "order_id")
	 @JsonIgnore
	 private PurchaseOrder order;
	 
	 @ManyToOne
	 @JoinColumn(name = "product_id")
	 private Product product;

	 private int quantity;
	 private double price;
	
	 public int getId() {
		return id;
	}
	 public void setId(int id) {
		 this.id = id;
	 }
	 public PurchaseOrder getOrder() {
		 return order;
	 }
	 public void setOrder(PurchaseOrder order) {
		 this.order = order;
	 }
	 public Product getProduct() {
		 return product;
	 }
	 public void setProduct(Product product) {
		 this.product = product;
	 }
	 public int getQuantity() {
		 return quantity;
	 }
	 public void setQuantity(int quantity) {
		 this.quantity = quantity;
	 }
	 public double getPrice() {
		 return price;
	 }
	 public void setPrice(double price) {
		 this.price = price;
	 }
	 
	 
	 @Override
		public String toString() {
			return "OrderItem [id=" + id + ", order=" + order + ", product=" + product + ", quantity=" + quantity
					+ ", price=" + price + "]";
		}
		 public OrderItem() {
			super();
		}
}

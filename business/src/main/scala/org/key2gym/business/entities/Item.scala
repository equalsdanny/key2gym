/*
 * Copyright 2012 Danylo Vashchilenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.key2gym.business.entities

import java.io.Serializable
import java.math.BigDecimal
import java.util.List
import javax.persistence._
import scala.collection.JavaConversions._
import org.key2gym.business.api.ValidationException
import org.key2gym.business.resources.ResourcesManager
import org.key2gym.persistence._

/**
 *
 * @author Danylo Vashchilenko
 */
@Entity
@Table(name = "item_itm")
@NamedQueries(Array(
    new NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i"),
    new NamedQuery(name = "Item.findPure", query = "SELECT i FROM Item i WHERE i.id NOT IN (SELECT s.item.id FROM ItemSubscription s)"),
    new NamedQuery(name = "Item.findAvailable", query = "SELECT i FROM Item i WHERE i.quantity IS NULL OR i.quantity > 0"),
    new NamedQuery(name = "Item.findPureAvailable", query = "SELECT i FROM Item i WHERE (i.quantity is NULL OR i.quantity > 0) AND i.id NOT IN (SELECT s.item.id FROM ItemSubscription s)"),
    new NamedQuery(name = "Item.findById", query = "SELECT i FROM Item i WHERE i.id = :id"),
    new NamedQuery(name = "Item.findByBarcode", query = "SELECT i FROM Item i WHERE i.barcode = :barcode"),
    new NamedQuery(name = "Item.findByQuantity", query = "SELECT i FROM Item i WHERE i.quantity = :quantity"),
    new NamedQuery(name = "Item.findByPrice", query = "SELECT i FROM Item i WHERE i.price = :price")))
@SequenceGenerator(name="id_itm_seq", allocationSize = 1)
class Item extends Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="id_itm_seq")
  @Basic(optional = false)
  @Column(name = "id_itm")
  protected var id: java.lang.Integer = _
  
  @Basic(optional = false)
  @Lob
  @Column(name = "title")
  protected var title: String = _
  
  @Basic(optional = true)
  @Column(name = "barcode")
  protected var barcode: java.lang.Long = _
  
  @Basic(optional = true)
  @Column(name = "quantity")
  protected var quantity: java.lang.Integer = _
  
  @Basic(optional = false)
  @Column(name = "price")
  protected var price: BigDecimal = _
  
  @OneToMany(mappedBy="item")
  protected var orderLines: List[OrderLine] = _
  
  @JoinColumn(name="id_itm", referencedColumnName="iditm_its")
  @OneToOne(mappedBy="item", cascade=Array(CascadeType.REMOVE))
  protected var itemSubscription: ItemSubscription = _
  
  def getId(): Int = this.id
  
  def getTitle(): String = this.title
  def setTitle(title: String) = this.title = title
  
  def getPrice(): BigDecimal = this.price
  def setPrice(price: BigDecimal) = this.price = price
  
  def getItemSubscription(): ItemSubscription = this.itemSubscription
  def setItemSubscription(itemSubscription: ItemSubscription) = this.itemSubscription = itemSubscription
  
  def getBarcode(): java.lang.Long = this.barcode

  /** Sets the barcode.
    *
    * @param quantity the new barocode
    * @throws ValidationException if the barcode is not valid
    */  
  def setBarcode(barcode: java.lang.Long) {
    // Barcode can not be negative
    if (barcode != null && barcode < 0) {
      val message = ResourcesManager.getString("Invalid.Property.CanNotBeNegative.withPropertyName", ResourcesManager.getString("Property.Barcode"));
      throw new ValidationException(message);
    }
    
    this.barcode = barcode
  }
  
  def getQuantity(): java.lang.Integer = this.quantity
  
  /** Sets the quantity.
    *
    * @param quantity the new quantity
    * @throws ValidationException if the quantity is not valid
    */
  def setQuantity(quantity: java.lang.Integer) {
    // Quantity has to be within range [0; 255]
    if(quantity != null) {
      if (quantity < 0) {
	val message = ResourcesManager.getString("Invalid.Property.CanNotBeNegative.withPropertyName", ResourcesManager.getString("Property.Quantity"));
	throw new ValidationException(message);
      } else if (quantity > 255) {
	val message = ResourcesManager.getString("Invalid.Property.OverLimit.withPropertyName", ResourcesManager.getString("Property.Quantity"));
	  throw new ValidationException(message);
      }
    }
    
    this.quantity = quantity
  }
  
  def getOrderLines(): List[OrderLine] = this.orderLines
}

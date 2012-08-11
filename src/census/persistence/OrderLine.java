/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package census.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

/**
 *
 * @author Danylo Vashchilenko
 */
@Entity
@Table(name = "order_line_orl")
@NamedQueries({
    @NamedQuery(name="OrderLine.findByOrderAndItemAndDiscount", query="SELECT ol FROM OrderLine ol WHERE ol.orderEntity = :order AND ol.item = :item AND ol.discount = :discount"),
    @NamedQuery(name="OrderLine.findByOrderAndItemAndNoDiscount", query="SELECT ol FROM OrderLine ol WHERE ol.orderEntity = :order AND ol.item = :item AND ol.discount IS NULL")
})

public class OrderLine implements Serializable {
    
    @Id
    @Column(name = "id_orl")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private short id;
            
    @JoinColumn(name="idord_orl", referencedColumnName="id_ord")//, insertable=false, updatable=false)
    @ManyToOne
    private OrderEntity orderEntity;
    
    @JoinColumn(name="iditm_orl", referencedColumnName="id_itm")//, insertable=false, updatable=false)
    @ManyToOne
    private Item item;
    
    @JoinColumn(name = "iddsc_orl", referencedColumnName = "id_dsc")
    @ManyToOne
    private Discount discount;
    
    @Column(name="quantity")
    private short quantity;

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public OrderEntity getOrder() {
        return orderEntity;
    }

    public void setOrder(OrderEntity order) {
        this.orderEntity = order;
    }

    public short getQuantity() {
        return quantity;
    }

    public void setQuantity(short quantity) {
        this.quantity = quantity;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
    
    public BigDecimal getTotal() {
        BigDecimal total = item.getPrice().multiply(new BigDecimal(quantity));
        if(discount != null) {
            total = total.divide(new BigDecimal(100));
            total = total.multiply(new BigDecimal(100-discount.getPercent()));
        }
        return total;
    }
}

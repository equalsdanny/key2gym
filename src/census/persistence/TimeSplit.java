/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package census.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author daniel
 */
@Entity
@Table(name = "time_split_tsp")
@NamedQueries({
    @NamedQuery(name = "TimeSplit.findAll", query = "SELECT t FROM TimeSplit t ORDER BY t.endTime ASC"),
    @NamedQuery(name = "TimeSplit.findById", query = "SELECT t FROM TimeSplit t WHERE t.id = :id")})
public class TimeSplit implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tsp")
    private Short id;
    
    @Basic(optional = false)
    @Column(name = "end_time")
    @Temporal(TemporalType.TIME)
    private Date endTime;
    
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    
    @OneToMany(mappedBy = "timeSplit")
    private List<ItemSubscription> itemSubscriptionList;

    public TimeSplit() {
    }

    public TimeSplit(Short id) {
        this.id = id;
    }

    public TimeSplit(Short id, Date endTime, String title) {
        this.id = id;
        this.endTime = endTime;
        this.title = title;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Date getTime() {
        return endTime;
    }

    public void setTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<ItemSubscription> getItemSubscriptionList() {
        return itemSubscriptionList;
    }

    public void setItemSubscriptionList(List<ItemSubscription> itemSubscriptionList) {
        this.itemSubscriptionList = itemSubscriptionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimeSplit)) {
            return false;
        }
        TimeSplit other = (TimeSplit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "census.persistence.TimeRange[ id=" + id + " ]";
    }
}
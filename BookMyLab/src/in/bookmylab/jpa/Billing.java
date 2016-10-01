/**
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2016 Balwinder Sodhi
 * You may obtain a copy of the License at: https://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package in.bookmylab.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import in.bookmylab.JsonDeserializeOnly;

/**
 *
 * @author Balwinder Sodhi
 */
@Entity
@Table(name="billing", catalog = "autolab", schema = "")
@NamedQueries({
    @NamedQuery(name = "Billing.findAll", query = "SELECT b FROM Billing b"),
    @NamedQuery(name = "Billing.getBillById", query = "SELECT b FROM Billing b WHERE b.billId = :billId"),
    @NamedQuery(name = "Billing.getBillsForUser", query = "SELECT b FROM Billing b WHERE b.user.userId = :userId")})
public class Billing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bill_id")
    public Integer billId;

    @Basic(optional = false)
    @Column(name = "charge_type")
    public String chargeType;

    @Basic(optional = false)
    public String status;

    @Basic(optional = false)
    @Column(name = "amount")
    public Float amount;

    @Basic(optional = false)
    @Column(name = "bill_date")
    @Temporal(TemporalType.DATE)
    public Date billDate;

    @JoinColumn(name = "booking_id", referencedColumnName = "booking_id")
    @ManyToOne(optional = false)
    @JsonDeserializeOnly
    public ResourceBooking booking;

    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    @JsonDeserializeOnly
    public User user;

//    @JoinColumn(name = "resource_price", referencedColumnName = "pricing_id")
//    @OneToOne(optional = false, cascade = CascadeType.ALL)
//    public ResourcePricing price;

    public Billing() {
    }

    public Billing(Integer billId) {
        this.billId = billId;
    }

    public Billing(Integer billId, String chargeType, String status, Date billDate) {
        this.billId = billId;
        this.chargeType = chargeType;
        this.status = status;
        this.billDate = billDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billId != null ? billId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Billing)) {
            return false;
        }
        Billing other = (Billing) object;
        if ((this.billId == null && other.billId != null) || (this.billId != null && !this.billId.equals(other.billId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.bookmylab.jpa.Billing[ billId=" + billId + " ]";
    }
    
}

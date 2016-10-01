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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Balwinder Sodhi
 */
@Entity
@Table(name = "resource_pricing", catalog = "autolab", schema = "")
@NamedQueries({
    @NamedQuery(name = "ResourcePricing.findAllCurrent", query = "SELECT r FROM ResourcePricing r WHERE r.isCurrent=true")})
public class ResourcePricing implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pricing_id")
    public Integer pricingId;
    
    @JoinColumn(name = "resource_id", referencedColumnName = "resource_id")
    @OneToOne(optional = false)
    public ResourceType resourceType;
    
    @Basic(optional = false)
    @Column(name = "per_sample_price")
    public long perSamplePrice;
    
    @Basic(optional = false)
    @Column(name = "per_result_price")
    public long perResultPrice;
    
    @Basic(optional = false)
    @Column(name = "is_current")
    public Boolean isCurrent;

    @Basic(optional = false)
    @Column(name = "valid_from")
    @Temporal(TemporalType.DATE)
    public Date validFrom;

    @Basic(optional = true)
    @Column(name = "valid_till")
    @Temporal(TemporalType.DATE)
    public Date validTill;

    public String remarks;

    public ResourcePricing() {
    }

    public ResourcePricing(Integer pricingId) {
        this.pricingId = pricingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pricingId != null ? pricingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResourcePricing)) {
            return false;
        }
        ResourcePricing other = (ResourcePricing) object;
        if ((this.pricingId == null && other.pricingId != null) || (this.pricingId != null && !this.pricingId.equals(other.pricingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.bookmylab.jpa.ResourcePricing[ princingId=" + pricingId + " ]";
    }
    
}

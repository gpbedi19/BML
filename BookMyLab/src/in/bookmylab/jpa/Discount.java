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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Balwinder Sodhi
 */
@Entity
@Table(name="discount", catalog = "autolab", schema = "")
@NamedQueries({
    @NamedQuery(name = "Discount.findAll", query = "SELECT d FROM Discount d")})
public class Discount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "discount_id")
    public Integer discountId;
    
    @Basic(optional = false)
    public long rate;
    
    @Basic(optional = false)
    @Column(name = "discount_code")
    public String discountCode;
    
    @Basic(optional = false)
    public boolean active;
    
    public String description;

    public Discount() {
    }

    public Discount(Integer discountId) {
        this.discountId = discountId;
    }

    public Discount(Integer discountId, long rate, String discountCode, boolean active) {
        this.discountId = discountId;
        this.rate = rate;
        this.discountCode = discountCode;
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (discountId != null ? discountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Discount)) {
            return false;
        }
        Discount other = (Discount) object;
        if ((this.discountId == null && other.discountId != null) || (this.discountId != null && !this.discountId.equals(other.discountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.bookmylab.jpa.Discount[ discountId=" + discountId + " ]";
    }
    
}

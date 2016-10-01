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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Balwinder Sodhi
 */
@Entity
@Table(name = "spm_lab_booking", catalog = "autolab", schema = "")
@NamedQueries({
    @NamedQuery(name = "SpmLabBooking.findAll", query = "SELECT s FROM SpmLabBooking s"),
    @NamedQuery(name = "SpmLabBooking.findById", query = "SELECT u FROM SpmLabBooking u WHERE u.spmId = :spmId"),
    @NamedQuery(name = "SpmLabBooking.findByBookingId", query = "SELECT u FROM SpmLabBooking u WHERE u.booking.bookingId = :bookingId"),
    @NamedQuery(name = "SpmLabBooking.findByUser", query = "SELECT u FROM SpmLabBooking u WHERE u.booking.user.userId = :userId")})
public class SpmLabBooking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "spm_id")
    public Integer spmId;

    @Basic(optional = false)
    @Lob
    public String material;

    @Basic(optional = false)
    @Lob
    @Column(name = "prep_method")
    public String prepMethod;

    @Basic(optional = false)
    @Column(name = "material_type")
    public String materialType;

    @Basic(optional = false)
    public boolean toxic;

    @Basic(optional = false)
    public boolean conducting;

    @Lob
    @Column(name = "other_requirements")
    public String otherRequirements;

    @JoinColumn(name = "booking_id", referencedColumnName = "booking_id")
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    public ResourceBooking booking;

    public SpmLabBooking() {
    }

    public SpmLabBooking(Integer spmId) {
        this.spmId = spmId;
    }

    public SpmLabBooking(Integer spmId, String material, String prepMethod, String materialType, boolean toxic, boolean conducting) {
        this.spmId = spmId;
        this.material = material;
        this.prepMethod = prepMethod;
        this.materialType = materialType;
        this.toxic = toxic;
        this.conducting = conducting;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (spmId != null ? spmId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SpmLabBooking)) {
            return false;
        }
        SpmLabBooking other = (SpmLabBooking) object;
        if ((this.spmId == null && other.spmId != null) || (this.spmId != null && !this.spmId.equals(other.spmId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.bookmylab.jpa.SpmLabBooking[ spmId=" + spmId + " ]";
    }
    
}

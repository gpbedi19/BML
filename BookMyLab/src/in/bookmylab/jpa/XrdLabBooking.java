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
@Table(name = "xrd_lab_booking", catalog = "autolab", schema = "")
@NamedQueries({
    @NamedQuery(name = "XrdLabBooking.findAll", query = "SELECT s FROM XrdLabBooking s"),
    @NamedQuery(name = "XrdLabBooking.findById", query = "SELECT u FROM XrdLabBooking u WHERE u.xrdId = :xrdId"),
    @NamedQuery(name = "XrdLabBooking.findByBookingId", query = "SELECT u FROM XrdLabBooking u WHERE u.booking.bookingId = :bookingId"),
    @NamedQuery(name = "XrdLabBooking.findByUser", query = "SELECT u FROM XrdLabBooking u WHERE u.booking.user.userId = :userId")})
public class XrdLabBooking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "xrd_id")
    public Integer xrdId;

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
    @Column(name = "scan_angle")
    public Integer scanAngle;

    @Basic(optional = false)
    @Column(name = "texture")
    public boolean texture;

    @Basic(optional = false)
    @Column(name = "residual_stress")
    public boolean residualStress;

    @Basic(optional = false)
    @Column(name = "saxs")
    public boolean saxs;


    
    @Basic(optional = false)
    public boolean toxic;
    

    @Lob
    @Column(name = "other_requirements")
    public String otherRequirements;

    @JoinColumn(name = "booking_id", referencedColumnName = "booking_id")
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    public ResourceBooking booking;

    public XrdLabBooking() {
    }

    public XrdLabBooking(Integer xrdId) {
        this.xrdId = xrdId;
    }

    public XrdLabBooking(Integer xrdId, String material, String prepMethod, String materialType, Integer scanAngle, boolean texture, boolean residualStress, boolean saxs, boolean toxic) {
        this.xrdId = xrdId;
        this.material = material;
        this.prepMethod = prepMethod;
        this.materialType = materialType;
        this.scanAngle = scanAngle;
        this.texture = texture;
        this.residualStress = residualStress;
        this.saxs = saxs;
        
        this.toxic = toxic;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xrdId != null ? xrdId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XrdLabBooking)) {
            return false;
        }
        XrdLabBooking other = (XrdLabBooking) object;
        if ((this.xrdId == null && other.xrdId != null) || (this.xrdId != null && !this.xrdId.equals(other.xrdId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.bookmylab.jpa.XrdLabBooking[ xrdId=" + xrdId + " ]";
    }
    
}

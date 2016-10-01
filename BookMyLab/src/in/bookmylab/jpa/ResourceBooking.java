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

import in.bookmylab.JsonDeserializeOnly;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Balwinder Sodhi
 */
@Entity
@Table(name = "resource_booking", catalog = "autolab", schema = "")
@NamedQueries({
    @NamedQuery(name = "ResourceBooking.findAll", query = "SELECT r FROM ResourceBooking r"),
    @NamedQuery(name = "ResourceBooking.findById", query = "SELECT r FROM ResourceBooking r WHERE r.bookingId = :bookingId"),
    @NamedQuery(name = "ResourceBooking.findUnfinished", query = "SELECT r FROM ResourceBooking r WHERE r.status != 'FINISHED'"),
    @NamedQuery(name = "ResourceBooking.findSlotsByDate", query = "SELECT r FROM ResourceBooking r WHERE r.bookingDate = :bookingDate AND r.status != 'REJECTED'"),
    @NamedQuery(name = "ResourceBooking.findUnfinishedForUser", query = "SELECT r FROM ResourceBooking r WHERE r.status != 'FINISHED' AND r.user.userId = :userId")
})
public class ResourceBooking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "booking_id")
    public Integer bookingId;

    @Basic(optional = false)
    @Column(name = "booking_date")
    @Temporal(TemporalType.DATE)
    public Date bookingDate;

    @Basic(optional = false)
    @Column(name = "start_time")
    public String startTime;

    @Basic(optional = false)
    @Column(name = "end_time")
    public String endTime;

    @Basic(optional = false)
    @Column(name = "lab")
    public String lab;

    @Basic(optional = false)
    @Column(name = "status", insertable=false)
    public String status;

    @Basic(optional = true)
    @Column(name = "status_notes")
    public String statusNotes;

    @Basic(optional = false)
    @Column(name = "created_on", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date createdOn;

    @Basic(optional = false)
    @Column(name = "instrument_type")
    public String instrumentType;

    @Basic(optional = false)
    @Column(name = "instrument_no")
    public String instrumentNo;

    @Basic(optional = false)
    @Column(name = "instrument_date")
    @Temporal(TemporalType.DATE)
    public Date instrumentDate;

    @Basic(optional = false)
    @Column(name = "instrument_amt")
    public float instrumentAmt;

    //    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
//    @ManyToOne(optional = false)
//    public BookingCategory category;

    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
//    @JsonDeserializeOnly
    public User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    @JsonDeserializeOnly
    public List<Billing> billingList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    public List<AnalysisMode> analysisModes;

    public ResourceBooking() {
    }

    public ResourceBooking(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public ResourceBooking(Integer bookingId, Date bookingDate, String startTime, String endTime, String status, Date createdOn) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdOn = createdOn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookingId != null ? bookingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResourceBooking)) {
            return false;
        }
        ResourceBooking other = (ResourceBooking) object;
        if ((this.bookingId == null && other.bookingId != null) || (this.bookingId != null && !this.bookingId.equals(other.bookingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.bookmylab.jpa.ResourceBooking[ bookingId=" + bookingId + " ]";
    }
    
}

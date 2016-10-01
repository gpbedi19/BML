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
 * @author auser
 */
@Entity
@Table(name = "booking_category", catalog = "autolab", schema = "")
@NamedQueries({
    @NamedQuery(name = "BookingCategory.findAll", query = "SELECT b FROM BookingCategory b")})
public class BookingCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "category_id")
    public Integer categoryId;
    
    @Basic(optional = false)
    public String category;
    
    @Basic(optional = false)
    @Column(name = "opens_at")
    public Short opensAt;
    
    @Basic(optional = false)
    @Column(name = "closes_at")
    public Short closesAt;
    
    @Basic(optional = false)
    @Column(name = "week_day")
    public short weekDay;
    
    public String description;
    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
//    @JsonDeserializeOnly
//    public List<ResourceBooking> resourceBookingList;

    public BookingCategory() {
    }

    public BookingCategory(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public BookingCategory(Integer categoryId, String category, Short opensAt, Short closesAt, short weekDay) {
        this.categoryId = categoryId;
        this.category = category;
        this.opensAt = opensAt;
        this.closesAt = closesAt;
        this.weekDay = weekDay;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookingCategory)) {
            return false;
        }
        BookingCategory other = (BookingCategory) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.bookmylab.jpa.BookingCategory[ categoryId=" + categoryId + " ]";
    }
    
}

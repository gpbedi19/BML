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

/**
 *
 * @author Balwinder Sodhi
 */
@Entity
@Table(name = "user_profile")
@NamedQueries({
    @NamedQuery(name = "UserProfile.findAll", query = "SELECT u FROM UserProfile u"),
    @NamedQuery(name = "UserProfile.findByUser", query = "SELECT u FROM UserProfile u WHERE u.user = :user"),
    @NamedQuery(name = "UserProfile.findByProfileId", query = "SELECT u FROM UserProfile u WHERE u.profileId = :profileId"),
    @NamedQuery(name = "UserProfile.findByOrgType", query = "SELECT u FROM UserProfile u WHERE u.orgType = :orgType"),
    @NamedQuery(name = "UserProfile.findByOrg", query = "SELECT u FROM UserProfile u WHERE u.org = :org"),
    @NamedQuery(name = "UserProfile.findByDept", query = "SELECT u FROM UserProfile u WHERE u.dept = :dept"),
    @NamedQuery(name = "UserProfile.findByDesignation", query = "SELECT u FROM UserProfile u WHERE u.designation = :designation"),
    @NamedQuery(name = "UserProfile.findByOfficePhone", query = "SELECT u FROM UserProfile u WHERE u.officePhone = :officePhone"),
    @NamedQuery(name = "UserProfile.findByAddress", query = "SELECT u FROM UserProfile u WHERE u.address = :address"),
    @NamedQuery(name = "UserProfile.findByCity", query = "SELECT u FROM UserProfile u WHERE u.city = :city"),
    @NamedQuery(name = "UserProfile.findByState", query = "SELECT u FROM UserProfile u WHERE u.state = :state"),
    @NamedQuery(name = "UserProfile.findByPostalCode", query = "SELECT u FROM UserProfile u WHERE u.postalCode = :postalCode"),
    @NamedQuery(name = "UserProfile.findByWebPage", query = "SELECT u FROM UserProfile u WHERE u.webPage = :webPage")})
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "profile_id")
    public Integer profileId;
    
    @Basic(optional = false)
    @Column(name = "org_type")
    public String orgType;
    
    @Basic(optional = false)
    public String org;
    
    @Basic(optional = false)
    public String dept;
    
    @Basic(optional = false)
    public String designation;
    
    @Basic(optional = false)
    @Column(name = "office_phone")
    public String officePhone;
    
    @Basic(optional = false)
    public String address;
    
    @Basic(optional = false)
    public String city;
    
    @Basic(optional = false)
    public String state;
    
    @Basic(optional = false)
    @Column(name = "postal_code")
    public String postalCode;
    
    @Column(name = "web_page")
    public String webPage;
    
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @OneToOne(optional = false)
    @JsonDeserializeOnly
    public User user;

    public UserProfile() {
    }

    public UserProfile(Integer profileId) {
        this.profileId = profileId;
    }

    public UserProfile(Integer profileId, String orgType, String org, String dept, String designation, String officePhone, String address, String city, String state, String postalCode) {
        this.profileId = profileId;
        this.orgType = orgType;
        this.org = org;
        this.dept = dept;
        this.designation = designation;
        this.officePhone = officePhone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (profileId != null ? profileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserProfile)) {
            return false;
        }
        UserProfile other = (UserProfile) object;
        if ((this.profileId == null && other.profileId != null) || (this.profileId != null && !this.profileId.equals(other.profileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.bookmylab.jpa.UserProfile[ profileId=" + profileId + " ]";
    }
    
}

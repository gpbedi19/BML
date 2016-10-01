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
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import in.bookmylab.Constants.RoleName;
import in.bookmylab.JsonDeserializeOnly;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

/**
 *
 * @author Balwinder Sodhi
 */
@Entity
@Table(catalog = "autolab", schema = "", name = "user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findUnverified", query = "SELECT u FROM User u WHERE u.verified = false"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email=:email"),
    @NamedQuery(name = "User.authenticate", query = "SELECT COUNT(u.userId) FROM User u WHERE u.email=:email AND u.password=:password"),
    @NamedQuery(name = "User.setPassword", query = "UPDATE User u SET u.password=:password WHERE u.userId=:userId")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    public Integer userId;

    @Basic(optional = false)
    @Column(updatable = false, name = "password")
    @JsonDeserializeOnly
    public String password;

    @Basic(optional = false)
    @Column(name = "first_name")
    public String firstName;

    @Column(name = "middle_name")
    public String middleName;

    @Basic(optional = false)
    @Column(name = "last_name")
    public String lastName;

    @Basic(optional = false)
    public Character gender;

    @Basic(optional = false)
    public String email;

    public boolean verified;

    @Column(name = "failed_logins")
    public short failedLogins;

    @Column(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    public Date lastLogin;

    @Basic(optional = false)
    @Column(name = "version_no")
    @Version
    public int versionNo;
    
    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "user", optional = true)
    public UserProfile profile;

    @JoinTable(name = "user_role", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "user_id")}, inverseJoinColumns = {
        @JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    @ManyToMany
    public List<Role> roleList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
//    public List<ResourceBooking> resourceBookingList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
//    public List<Billing> billingList;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(Integer userId, String password, String firstName, String lastName, Character gender, String email, int versionNo) {
        this.userId = userId;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.versionNo = versionNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.bookmylab.jpa.User[ userId=" + userId + " ]";
    }
    
    public boolean hasRole(RoleName roleName) {
        boolean has = false;
        for (Role role : roleList) {
            if (role.role.equalsIgnoreCase(roleName.name())) {
                has = true;
                break;
            }
        }
        return has;
    }
    
}
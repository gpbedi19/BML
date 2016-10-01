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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Balwinder Sodhi
 */
@Entity
@Table(catalog = "autolab", schema = "", name = "static_data")
@NamedQueries({
    @NamedQuery(name = "StaticData.findAll", query = "SELECT d FROM StaticData d"),
    @NamedQuery(name = "StaticData.findByKey", query = "SELECT d FROM StaticData d WHERE d.dataKey=:dataKey")})
public class StaticData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "data_id")
    public Long dataId;

    @Basic(optional = false)
    @Column(name = "data_key")
    public String dataKey;

    @Basic(optional = false)
    @Column(name = "is_array")
    public Boolean isArray;

    public String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "staticData")
    public List<StaticDataValue> dataValues;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dataId != null ? dataId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the dataId fields are not set
        if (!(object instanceof StaticData)) {
            return false;
        }
        StaticData other = (StaticData) object;
        if ((this.dataId == null && other.dataId != null) || (this.dataId != null && !this.dataId.equals(other.dataId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.bookmylab.jpa.StaticData[ id=" + dataId + " ]";
    }
    
}

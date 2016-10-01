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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import in.bookmylab.JsonDeserializeOnly;

/**
 *
 * @author Balwinder Sodhi
 */
@Entity
@Table(catalog = "autolab", schema = "", name = "static_data_value")
@NamedQueries({
    @NamedQuery(name = "StaticDataValue.findAll", query = "SELECT d FROM StaticDataValue d")})
public class StaticDataValue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "value_id")
    public Long valueId;

    @JoinColumn(name = "data_id", referencedColumnName = "data_id")
    @ManyToOne(optional = false)
    @JsonDeserializeOnly
    public StaticData staticData;
    
    @Column(name = "data_value")
    public String dataValue;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (valueId != null ? valueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StaticDataValue)) {
            return false;
        }
        StaticDataValue other = (StaticDataValue) object;
        if ((this.valueId == null && other.valueId != null) || (this.valueId != null && !this.valueId.equals(other.valueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.bookmylab.jpa.StaticDataValue[ id=" + valueId + " ]";
    }
    
}

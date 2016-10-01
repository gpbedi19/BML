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

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ResourceType
 * @author Balwinder Sodhi
 */
@Entity
@Table(name = "resource_type", catalog = "autolab", schema = "")
@NamedQueries({
    @NamedQuery(name = "ResourceType.findById", query = "SELECT r FROM ResourceType r WHERE r.resourceId = :resourceId"),
    @NamedQuery(name = "ResourceType.findAllActive", query = "SELECT r FROM ResourceType r WHERE r.active = true"),
    @NamedQuery(name = "ResourceType.findByCode", query = "SELECT r FROM ResourceType r WHERE r.code = :code")
})
public class ResourceType implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "resource_id")
	public Integer resourceId;

	@Basic(optional = false)
    @Column(name = "code")
	public String code;
	
	@Basic(optional = true)
    @Column(name = "description")
	public String description;

	@Basic(optional = false)
    @Column(name = "active")
	public Boolean active;

	public ResourceType() {
		super();
	}
   
}

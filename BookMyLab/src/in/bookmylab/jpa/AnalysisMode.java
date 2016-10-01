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

import javax.persistence.*;

/**
 * Entity implementation class for Entity: AnalysisMode
 * @author Balwinder Sodhi
 */
@Entity
@Table(name = "analysis_mode", catalog = "autolab", schema = "")
public class AnalysisMode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "analysis_id")
	public Integer analysisId;

    @JoinColumn(name = "resource_id", referencedColumnName = "resource_id")
    @OneToOne(optional = false)
    public ResourceType resourceType;
	
	@Basic(optional = false)
    @Column(name = "results")
	public Integer results;

	@Basic(optional = false)
    @Column(name = "samples")
	public Integer samples;

	@Basic(optional = false)
    @Column(name = "charges")
	public Float charges;

	@Basic(optional = true)
    @Column(name = "remarks")
	public String remarks;

	@JoinColumn(name = "booking_id", referencedColumnName = "booking_id")
    @ManyToOne(optional = false)
    @JsonDeserializeOnly
    public ResourceBooking booking;

	@Transient
	public boolean deleted;

	public AnalysisMode() {
		super();
	}   
   
}

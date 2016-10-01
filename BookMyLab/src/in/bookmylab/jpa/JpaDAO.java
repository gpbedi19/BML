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

import in.bookmylab.AppException;
import in.bookmylab.Constants;
import in.bookmylab.Constants.BookingStatus;
import in.bookmylab.Constants.RoleName;
import in.bookmylab.Utils;
import in.bookmylab.controllers.Bookings.BookingSearchInput;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Balwinder Sodhi
 */
public class JpaDAO {

    private EntityManagerFactory emf = null;

    private JpaDAO() {
        emf = Persistence.createEntityManagerFactory("AutoLABPU");
    }

    public static JpaDAO getInstance() {
        return JpaDAOHolder.INSTANCE;
    }

    public User saveUser(User user, boolean savePassword) {
        EntityManager em = emf.createEntityManager();
        try {
            user.password = Utils.hashPassword(user.password);
            em.getTransaction().begin();
            if (user.userId == null) {
            	grantDefaultRoleToUser(em, user);
                em.persist(user);
            } else {
            	user = em.merge(user);
                if (savePassword) {
                    Query q = em.createNamedQuery("User.setPassword");
                    q.setParameter("password", user.password);
                    q.setParameter("userId", user.userId);
                    q.executeUpdate();
                }
            }
            em.getTransaction().commit();
        } finally {
            em.clear();
            em.close();
        }
        return user;
    }

    /**
	 * @param em
	 * @param user
	 */
	private void grantDefaultRoleToUser(EntityManager em, User user) {
		Query q = em.createNamedQuery("Role.findByRole");
		q.setParameter("role", RoleName.USER.name());
		Role r = (Role) q.getSingleResult();
		if (user.roleList == null) user.roleList = new ArrayList<>();
		user.roleList.add(r);
	}

	public void saveProfile(UserProfile profile) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (profile.profileId == null) {
                em.persist(profile);
            } else {
                em.merge(profile);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public UserProfile getProfile(User user) {
        EntityManager em = emf.createEntityManager();
        UserProfile p = null;
        try {
            List<UserProfile> res = em.createNamedQuery("UserProfile.findByUser",
                UserProfile.class).setParameter("user", user).getResultList();
            if (!res.isEmpty()) {
                p = res.get(0);
            }
        } finally {
            em.close();
        }
        return p;
    }

    public List<StaticData> getStaticData() {
        EntityManager em = emf.createEntityManager();
        List<StaticData> data = null;
        try {
            data = em.createNamedQuery("StaticData.findAll",
                StaticData.class).getResultList();
        } finally {
            em.clear();
            em.close();
        }
        return data;
    }

    public HashMap<String, Object> getDashboardData(boolean isManager, int userId) {
        EntityManager em = emf.createEntityManager();
        HashMap<String, Object> data = new HashMap<>();
        try {
            if (isManager) {

                List<User> l = em.createNamedQuery("User.findUnverified",
                    User.class).getResultList();
                data.put(Constants.JK_UNVERIFIED_USERS, l);

                List<ResourceBooking> l2 = em.createNamedQuery(
                    "ResourceBooking.findUnfinished", ResourceBooking.class).getResultList();
                data.put(Constants.JK_PENDING_BOOKINGS, l2);

            } else {
                List<ResourceBooking> l2 = em.createNamedQuery(
                    "ResourceBooking.findUnfinishedForUser", ResourceBooking.class)
                    .setParameter("userId", userId).getResultList();
                data.put(Constants.JK_PENDING_BOOKINGS, l2);
                
                // TODO: Add other lab bookings

            }
        } finally {
            em.close();
        }
        return data;
    }

    private static class JpaDAOHolder {

        private static final JpaDAO INSTANCE = new JpaDAO();
    }

    public User getUserByLogin(String email) {
        User u = null;
        EntityManager em = emf.createEntityManager();
        try {
            Query q = em.createNamedQuery("User.findByEmail");
            List<User> l = q.setParameter("email", email).getResultList();
            if (l.size() == 1) {
                u = l.get(0);
            }
        } finally {
            em.clear();
            em.close();
        }
        return u;
    }

    public List<ResourceBooking> getSlotsByDate(Date dt) {
    	List<ResourceBooking> list = null;
        EntityManager em = emf.createEntityManager();
        try {
            Query q = em.createNamedQuery("ResourceBooking.findSlotsByDate");
            list = q.setParameter("bookingDate", dt).getResultList();
        } finally {
            em.clear();
            em.close();
        }
        return list;
    }

    public boolean authenticate(String email, String hashedPassword) {
        Long c;
        EntityManager em = emf.createEntityManager();
        try {
            Query q = em.createNamedQuery("User.authenticate");
            c = (Long) q.setParameter("email", email).setParameter("password", hashedPassword).getSingleResult();
        } finally {
            em.close();
        }
        return c == 1;
    }
    ///////////////////////////////////////////////////////////////////////////////////
	/**
	 * @param spmId 
	 * @return
	 */
	public SpmLabBooking getSpmBooking(Long spmId) {
        EntityManager em = emf.createEntityManager();
        SpmLabBooking spm=null;
        try {
        	Query q = em.createNamedQuery("SpmLabBooking.findById");
        	spm = (SpmLabBooking) q.setParameter("spmId", spmId).getSingleResult();
        } finally {
            em.close();
        }
		return spm;
	}

	/**
	 * @param spmId 
	 * @return
	 */
	public SpmLabBooking getSpmByBookingId(Long bookingId) {
        EntityManager em = emf.createEntityManager();
        SpmLabBooking spm=null;
        try {
        	Query q = em.createNamedQuery("SpmLabBooking.findByBookingId");
        	spm = (SpmLabBooking) q.setParameter("bookingId", bookingId).getSingleResult();
        } finally {
            em.close();
        }
		return spm;
	}

	public void saveSpmBooking(SpmLabBooking spm) throws AppException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (spm.spmId == null) {
                em.persist(spm);
                fixAnalysisModesAndResourceType(spm.booking, em, false);
            } else {
            	// Validate status change, if any
            	Query q = em.createNamedQuery("ResourceBooking.findById");
        		ResourceBooking rb = (ResourceBooking) q.setParameter("bookingId",
        				spm.booking.bookingId).getSingleResult();
        		if (!statusChangeValid(rb.status, spm.booking.status)) {
        			throw new AppException(String.format(
        					"Status cannot be changed from %s to %s.",
        					rb.status, spm.booking.status));
        		}

        		fixAnalysisModesAndResourceType(spm.booking, em, true);
                em.merge(spm);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
	////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @param xrdId 
	 * @return
	 */
	public XrdLabBooking getXrdBooking(Long xrdId) {
        EntityManager em = emf.createEntityManager();
        XrdLabBooking xrd=null;
        try {
        	Query q = em.createNamedQuery("XrdLabBooking.findById");
        	xrd = (XrdLabBooking) q.setParameter("xrdId", xrdId).getSingleResult();
        } finally {
            em.close();
        }
		return xrd;
	}

	/**
	 * @param xrdId 
	 * @return
	 */
	public XrdLabBooking getXrdByBookingId(Long bookingId) {
        EntityManager em = emf.createEntityManager();
        XrdLabBooking xrd=null;
        try {
        	Query q = em.createNamedQuery("XrdLabBooking.findByBookingId");
        	xrd = (XrdLabBooking) q.setParameter("bookingId", bookingId).getSingleResult();
        } finally {
            em.close();
        }
		return xrd;
	}

	public void saveXrdBooking(XrdLabBooking xrd) throws AppException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (xrd.xrdId == null) {
                em.persist(xrd);
                fixAnalysisModesAndResourceType(xrd.booking, em, false);
            } else {
            	// Validate status change, if any
            	Query q = em.createNamedQuery("ResourceBooking.findById");
        		ResourceBooking rb = (ResourceBooking) q.setParameter("bookingId",
        				xrd.booking.bookingId).getSingleResult();
        		if (!statusChangeValid(rb.status, xrd.booking.status)) {
        			throw new AppException(String.format(
        					"Status cannot be changed from %s to %s.",
        					rb.status, xrd.booking.status));
        		}

        		fixAnalysisModesAndResourceType(xrd.booking, em, true);
                em.merge(xrd);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
	///////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @param oldStatus
	 * @param newStatus
	 * @return
	 */
	private boolean statusChangeValid(String oldStatus, String newStatus) {
		if (oldStatus.equalsIgnoreCase(newStatus)) {
			return true;
		}
		String combo = oldStatus+newStatus;
		boolean invalid = combo.startsWith(BookingStatus.FINISHED.name()) ||
				combo.startsWith(BookingStatus.REJECTED.name());
		boolean valid = combo.endsWith(BookingStatus.FINISHED.name()) ||
				combo.endsWith(BookingStatus.REJECTED.name()) ||
				combo.endsWith(BookingStatus.SCHEDULED.name());
		return valid && !invalid;
	}

	/**
	 * @param xrd
	 * @param em
	 */
	private void fixAnalysisModesAndResourceType(ResourceBooking booking,
			EntityManager em, boolean isUpdate) {
		// Analysis modes deletion
		if (booking.analysisModes != null) {
			for (Iterator<AnalysisMode> itr = booking.analysisModes.iterator(); itr.hasNext();) {
				AnalysisMode a = itr.next();
				if (a.deleted) {
					if (isUpdate) {
						em.remove(em.merge(a));
					}
					itr.remove(); // Remove from collection
				} else {
					// Attach resource type
					Query q = em.createNamedQuery("ResourceType.findByCode");
			    	ResourceType rt = (ResourceType) q.setParameter("code", 
			    			StringUtils.upperCase(a.resourceType.code)).getSingleResult();
			    	a.resourceType = rt;
			    	if (isUpdate) {
			    		em.merge(a);
			    	}
				}
			}
		}
	}

	public List<ResourceBooking> searchResourceBooking(BookingSearchInput si) {
		EntityManager em = emf.createEntityManager();
        try {
        	CriteriaBuilder cb = em.getCriteriaBuilder();
        	CriteriaQuery<ResourceBooking> cq = cb.createQuery(ResourceBooking.class);
        	Root<ResourceBooking> s = cq.from(ResourceBooking.class);
        	List<Predicate> pred = new ArrayList<Predicate>();
        	if (si.userId != null) {
        		pred.add(cb.equal(s.<User>get("user").<Integer>get("userId"), si.userId));
        	}
        	if (si.bookingDateFrom != null) {
        		pred.add(cb.greaterThanOrEqualTo(s.<Date>get("bookingDate"), si.bookingDateFrom));
        	}
        	if (si.bookingDateTo != null) {
        		pred.add(cb.lessThanOrEqualTo(s.<Date>get("bookingDate"), si.bookingDateTo));
        	}
        	if (!StringUtils.isEmpty(si.lab)) {
        		pred.add(cb.equal(s.<String>get("lab"), si.lab));
        	}
        	if (!StringUtils.isEmpty(si.status)) {
        		pred.add(cb.equal(s.<String>get("status"), si.status));
        	}
        	cq.select(s).where(pred.toArray(new Predicate[]{}));
        	return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
	}

	/**
	 * @return
	 */
	public List<ResourceType> getResourceTypes() {
        EntityManager em = emf.createEntityManager();
        try {
        	Query q = em.createNamedQuery("ResourceType.findAllActive");
        	return q.getResultList();
        } finally {
            em.close();
        }
	}

	/**
	 * @return
	 */
	public List<ResourcePricing> getResourcePrices() {
        EntityManager em = emf.createEntityManager();
        try {
        	Query q = em.createNamedQuery("ResourcePricing.findAllCurrent");
        	return q.getResultList();
        } finally {
            em.close();
        }
	}

	/**
	 * @param parseInt
	 * @return
	 */
	public ResourceBooking getBookingById(int bookingId) {
        EntityManager em = emf.createEntityManager();
        try {
        	Query q = em.createNamedQuery("ResourceBooking.findById");
        	q.setParameter("bookingId", bookingId);
        	return (ResourceBooking) q.getSingleResult();
        } finally {
            em.close();
        }
	}
}

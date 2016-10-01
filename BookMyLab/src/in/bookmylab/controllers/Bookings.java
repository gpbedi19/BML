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
package in.bookmylab.controllers;

import in.bookmylab.AppException;
import in.bookmylab.Constants;
import in.bookmylab.Constants.ResourceType;
import in.bookmylab.Constants.RoleName;
import in.bookmylab.Utils;
import in.bookmylab.jpa.AnalysisMode;
import in.bookmylab.jpa.JpaDAO;
import in.bookmylab.jpa.ResourceBooking;
import in.bookmylab.jpa.SpmLabBooking;
import in.bookmylab.jpa.User;
import in.bookmylab.jpa.UserProfile;
import in.bookmylab.jpa.XrdLabBooking;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.javamvc.core.annotations.*;

/**
 *
 * @author Balwinder Sodhi
 */
public class Bookings extends BaseController {

	@Action
    @Authorize
    public void getBooking() throws IOException {
        try {
        	String bid = getRequestParameter("bid");
        	String lab = getRequestParameter("lab");
        	Object booking = null;
        	if (!StringUtils.isEmpty(bid) && !StringUtils.isEmpty(lab)) {        		
            	Long bookingId = Long.parseLong(bid);
            	if (ResourceType.sem.name().equalsIgnoreCase(lab)) {
            		// TODO
            	} else if (ResourceType.spm.name().equalsIgnoreCase(lab)) {
            		booking = JpaDAO.getInstance().getSpmByBookingId(bookingId);
            		if (!canCurrentUserView(((SpmLabBooking)booking).booking)) {
            			throw new AppException("Cannot access the requested booking!");
            		}
            	} else if (ResourceType.xrd.name().equalsIgnoreCase(lab)) {
            		booking = JpaDAO.getInstance().getXrdByBookingId(bookingId);
            		if (!canCurrentUserView(((XrdLabBooking)booking).booking)) {
            			throw new AppException("Cannot access the requested booking!");
            		}
            	} else if (ResourceType.other.name().equalsIgnoreCase(lab)) {
            		// TODO
            	} else {
            		throw new IllegalArgumentException("Invalid resource type: "+lab);
            	}
        	}
        	sendJsonResponse(Status.OK, booking);
        } catch (Exception e) {
            handleActionError(e);
        }
    }

	/**
	 * @param booking
	 * @return
	 */
	private boolean canCurrentUserView(ResourceBooking booking) {
		boolean canView = false;
		User u = getLoggedInUser();
		if(u.hasRole(RoleName.USER)){
			canView = u.userId == booking.user.userId;
		} else {
			canView = true;
		}
		return canView;
	}

	@Action
    @Authorize
    public void saveBooking() throws IOException {
        try {
            if (isJsonRequest()) {
            	String json = getJsonData();
            	Map booking = getJsonAsObject(Map.class, json);
            	String lab = (String) ((Map)booking.get("booking")).get("lab");
            	if (ResourceType.spm.name().equalsIgnoreCase(lab)) {
            		SpmLabBooking spm = getJsonAsObject(SpmLabBooking.class, json);
            		updateBookingOwner(spm.booking);
            		updateAnalysisModes(spm.booking);
                    JpaDAO.getInstance().saveSpmBooking(spm);
                    sendJsonResponse(Status.OK, spm);
            	} else if (ResourceType.sem.name().equalsIgnoreCase(lab)) {
            		
            	} else if (ResourceType.xrd.name().equalsIgnoreCase(lab)) {
            		XrdLabBooking xrd = getJsonAsObject(XrdLabBooking.class, json);
            		updateBookingOwner(xrd.booking);
            		updateAnalysisModes(xrd.booking);
                    JpaDAO.getInstance().saveXrdBooking(xrd);
                    sendJsonResponse(Status.OK, xrd);
            	} else if (ResourceType.other.name().equalsIgnoreCase(lab)) {
            		
            	} else {
            		// ERROR
            		sendJsonResponse(Status.ERROR, "Unsupported lab type: "+lab);
            	}
                
            } else {
                sendJsonResponse(Status.ERROR, "Expected request in JSON format.");
            }
        } catch (Exception e) {
            handleActionError(e);
        }
    }

	/**
	 * @param booking
	 */
	private void updateBookingOwner(ResourceBooking booking) {
		if (booking.bookingId == null || booking.bookingId == 0) {
			booking.user = getLoggedInUser();
		} else {
			logger.fine("Updating existing booking; user will not be changed.");
		}
	}

	/**
	 * @param spm
	 */
	private void updateAnalysisModes(ResourceBooking booking) {
		if (booking.analysisModes != null) {
			for (Iterator iterator = booking.analysisModes.iterator(); iterator.hasNext();) {
				AnalysisMode a = (AnalysisMode) iterator.next();
				if (a.deleted && (a.analysisId == null || a.analysisId < 1)) {
					iterator.remove();
				} else {
					a.booking = booking;
				}				
			}
		}
	}

	@Action
    @Authorize
    public void searchBooking() throws IOException {
        try {
            if (isJsonRequest()) {
                BookingSearchInput si = getJsonRequestAsObject(BookingSearchInput.class);
                if (isCurrentUserInRole(RoleName.USER)) {
                	si.userId = ((User)getLoggedInUser()).userId;
                }
                List<ResourceBooking> list = JpaDAO.getInstance().searchResourceBooking(si);
                if (list == null) list = new ArrayList<ResourceBooking>();
                sendJsonResponse(Status.OK, list);
            } else {
                sendJsonResponse(Status.ERROR, "Expected request in JSON format.");
            }
        } catch (Exception e) {
            handleActionError(e);
        }
    }

    @Action
	@Authorize
	public void getSlotsForDate() throws IOException {
        String dt = getRequestParameter("date");
        if (!StringUtils.isEmpty(dt)) {
        	try {
				Date d = Utils.parseDate(dt, Constants.DATE_MEDIUM);
				List<ResourceBooking> list = JpaDAO.getInstance().getSlotsByDate(d);
				sendJsonResponse(Status.OK, list);
			} catch (ParseException e) {
				logger.severe("Unparsable input date: "+e.getMessage());
				sendJsonResponse(Status.ERROR, e.getMessage());
			}
        } else {
        	sendJsonResponse(Status.ERROR, "Expected a valid input date.");
        }
    }
	
    @Action
	@Authorize
	public void getInvoice() throws IOException {
        String bid = getRequestParameter("bid");
        if (!StringUtils.isEmpty(bid)) {
        	ResourceBooking booking = JpaDAO.getInstance().getBookingById(Integer.parseInt(bid));
			Invoice inv = new Invoice();
			inv.analysisModes = booking.analysisModes;
			inv.invoiceDate = booking.bookingDate;
			inv.invoiceNo = booking.lab+"-"+booking.bookingId;
			inv.paymentMode = booking.instrumentType;
			inv.billedTo = String.format("%s %s %s", booking.user.firstName, 
					booking.user.middleName, booking.user.lastName);
			UserProfile p = booking.user.profile;
			inv.billingAddress = String.format("%s %s %s %s", p.address, p.city, p.state, 
					p.postalCode);
			inv.amount = 0;
			for (AnalysisMode am : booking.analysisModes) {
				inv.amount += am.charges;
			}
			sendJsonResponse(Status.OK, inv);
        } else {
        	sendJsonResponse(Status.ERROR, "Expected a valid bill id.");
        }
    }

    public static class BookingSearchInput implements Serializable {
		public Date bookingDateFrom;
		public Date bookingDateTo;
		public String status;
		public String lab;
		public Integer userId;
	}
    
    public static class Invoice implements Serializable {
    	public Date invoiceDate;
    	public List<AnalysisMode> analysisModes;
    	public float amount;
    	public String invoiceNo;
    	public String billedTo;
    	public String billingAddress;
    	public String paymentMode;
    }
}

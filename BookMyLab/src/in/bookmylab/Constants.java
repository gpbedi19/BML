/**
 * Copyright 2015 Balwinder Sodhi
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package in.bookmylab;

/**
 *
 * @author Balwinder Sodhi
 */
public interface Constants {

	public enum ResourceType{
		sem, spm, xrd, other
	};

	public enum RoleName{
		USER, ADMIN, MANAGER
	};

	public enum BookingStatus{
		PENDING, SCHEDULED, REJECTED, FINISHED
	};

	// Keys for session attributes
    String SK_USER = "user";
    String SK_AUTH_CTX = "auth.context";

    String DATE_DDMMYYYY = "dd/MM/yyyy";
    String DATETIME = "MMM d,yyyy HH:mm:ss";
    String DATE_MEDIUM = "MMM d,yyyy";

    String RE_PHONE_NO = "^\\+(?:[0-9] ?){6,14}[0-9]$";
    String SIGN_KEY_FILE = "WEB-INF/cfg/sign.key";

    String CFG_ANALYTICS_ENABLED = "cfg.analytics.enabled";
    String CFG_ANALYTICS_PG_SIZE = "cfg.analytics.page.size";
    String CFG_NOREPLY_EMAIL = "cfg.noreply.email";
    String CFG_CALLBACK_ERROR_EMAIL = "cfg.error.email.text";
    
    // JSON keys
    String JK_UNVERIFIED_USERS = "UnverifiedUsers";
    String JK_PENDING_BOOKINGS = "PendingBookings";

}

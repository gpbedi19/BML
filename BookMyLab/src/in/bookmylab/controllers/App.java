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
package in.bookmylab.controllers;

import in.bookmylab.AuthContextImpl;
import in.bookmylab.Constants;
import in.bookmylab.Utils;
import in.bookmylab.Constants.RoleName;
import in.bookmylab.jpa.JpaDAO;
import in.bookmylab.jpa.ResourcePricing;
import in.bookmylab.jpa.ResourceType;
import in.bookmylab.jpa.StaticData;
import in.bookmylab.jpa.User;
import in.bookmylab.jpa.UserProfile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.javamvc.core.annotations.Action;
import org.javamvc.core.annotations.Authorize;

/**
 *
 * @author Balwinder Sodhi
 */
public class App extends BaseController {

    @Action
    public void login() throws IOException {
        try {
            if (isJsonRequest()) {
                User user = getJsonRequestAsObject(User.class);
                String hashed = Utils.hashPassword(user.password);
                User u2 = JpaDAO.getInstance().getUserByLogin(user.email);
                boolean success = u2 != null && u2.password.equals(hashed);
                
                if (success) {
                	u2.lastLogin = new Date();
                	u2.password = null;
                }
            	if (u2 != null) {
                	if (!success) u2.failedLogins++;
                	u2 = JpaDAO.getInstance().saveUser(u2, false);
                }
                
            	if (success) {
                    setSessionAttribute(Constants.SK_USER, u2);
                    setSessionAttribute(Constants.SK_AUTH_CTX, new AuthContextImpl(u2));
                    sendJsonResponse(Status.OK, u2);            		
            	} else {
            		sendJsonResponse(Status.ERROR, "Authentication failed.");
            	}

            } else {
                sendJsonResponse(Status.ERROR, "Expected request in JSON format.");
            }

        } catch (Exception e) {
            handleActionError(e);
        }
    }

    @Action
    public void logout() throws IOException {
        invalidateSession();
        sendJsonResponse(Status.OK, "Logged out");
    }

    @Action
    public void register() throws IOException {
        try {
            if (isJsonRequest()) {
                User user = getJsonRequestAsObject(User.class);
                user = JpaDAO.getInstance().saveUser(user, true);
                user.password = null;
                setSessionAttribute(Constants.SK_USER, user);
                setSessionAttribute(Constants.SK_AUTH_CTX, new AuthContextImpl(user));

                sendJsonResponse(Status.OK, user);
            } else {
                sendJsonResponse(Status.ERROR, "Expected request in JSON format.");
            }
        } catch (Exception e) {
            handleActionError(e);
        }
    }

    @Action
    @Authorize(roles={"MANAGER", "ADMIN"})
    public void markUserVerified() throws IOException {
        try {
        	String email = getRequestParameter("email");
        	if (!StringUtils.isEmpty(email)) {
        		User u = JpaDAO.getInstance().getUserByLogin(email);
        		u.verified = true;
        		JpaDAO.getInstance().saveUser(u, false);
        		sendJsonResponse(Status.OK, "Marked verifired.");
        	} else {
        		sendJsonResponse(Status.ERROR, "A valid email is expected.");
        	}
        } catch (Exception e) {
            handleActionError(e);
        }
    }

    @Action
    @Authorize
    public void getProfile() throws IOException {
        try {
            User u = getLoggedInUser();
            UserProfile profile = JpaDAO.getInstance().getProfile(u);
            if (profile == null) profile = new UserProfile();
            sendJsonResponse(Status.OK, profile);
        } catch (Exception e) {
            handleActionError(e);
        }
    }

    @Action
    @Authorize
    public void saveProfile() throws IOException {
        try {
            if (isJsonRequest()) {
                UserProfile profile = getJsonRequestAsObject(UserProfile.class);
                User u = getLoggedInUser();
                profile.user = u;
                JpaDAO.getInstance().saveProfile(profile);
                sendJsonResponse(Status.OK, profile);
            } else {
                sendJsonResponse(Status.ERROR, "Expected request in JSON format.");
            }
        } catch (Exception e) {
            handleActionError(e);
        }
    }

    @Action
    @Authorize
    public void saveUser() throws IOException {
        try {

            if (isJsonRequest()) {
                User user = getJsonRequestAsObject(User.class);
                User inSession = getLoggedInUser();
                if (inSession.userId != user.userId) {
                    sendJsonResponse(Status.ERROR, "Illegal attempt to change user details! Will be reported.");
                } else {
                    user = JpaDAO.getInstance().saveUser(user, false);
                    user.password = null;
                    setSessionAttribute(Constants.SK_USER, user);
                    sendJsonResponse(Status.OK, user);
                }
            } else {
                sendJsonResponse(Status.ERROR, "Expected request in JSON format.");
            }
        } catch (Exception e) {
            handleActionError(e);
        }
    }

    @Action
    @Authorize
    public void getSessionDetails() throws IOException {
        User u = getLoggedInUser();
        sendJsonResponse(Status.OK, u);
    }

    @Action
    @Authorize
    public void getDashboard() throws IOException {
        User u = getLoggedInUser();
        HashMap<String, Object> dash = new HashMap<>();
        dash = JpaDAO.getInstance().getDashboardData(u.hasRole(RoleName.MANAGER), u.userId);
        sendJsonResponse(Status.OK, dash);
    }

    @Action
    public void isLoggedIn() throws IOException {
        User u = getLoggedInUser();
        sendJsonResponse(Status.OK, u != null);
    }

    @Action
    public void getStaticData() throws IOException {
        List<StaticData> sd = JpaDAO.getInstance().getStaticData();
        sendJsonResponse(Status.OK, sd);
    }

    @Action
    public void getResourceTypes() throws IOException {
        List<ResourceType> sd = JpaDAO.getInstance().getResourceTypes();
        sendJsonResponse(Status.OK, sd);
    }

    @Action
    public void getResourcePrices() throws IOException {
        List<ResourcePricing> sd = JpaDAO.getInstance().getResourcePrices();
        sendJsonResponse(Status.OK, sd);
    }

    @Action
    @Authorize(roles = {"admin", "editor"})
    public void rbacTest() throws IOException {
        sendJsonResponse(Status.OK, "This is a test of RBAC.");
    }
}

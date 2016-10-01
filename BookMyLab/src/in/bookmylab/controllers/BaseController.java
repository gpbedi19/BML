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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.javamvc.core.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.bookmylab.AnnotationExclusionStrategy;
import in.bookmylab.AppException;
import in.bookmylab.Constants;
import in.bookmylab.Constants.RoleName;
import in.bookmylab.jpa.ClientInfo;
import in.bookmylab.jpa.User;

import java.util.logging.Level;

/**
 *
 * @author Balwinder Sodhi
 */
public abstract class BaseController extends Controller {

    public BaseController() {
        super();
    }

    protected void handleActionError(Exception e) throws IOException {
    	if (e instanceof AppException) {
    		sendJsonResponse(Status.ERROR, e.getLocalizedMessage());
    	} else {
    		sendJsonResponse(Status.ERROR, "Error occurred while processing your request. Please retry later or contact the administrator.");
    	}
        logger.log(Level.SEVERE, "Error occured. ", e);
    }

    public enum EmailType {

        ConfirmEmail, PasswordReset, NewPassword
    }

    public enum Status {

        OK, ERROR
    }

    protected Gson gson = new GsonBuilder().addSerializationExclusionStrategy(
        new AnnotationExclusionStrategy()).setDateFormat(Constants.DATE_MEDIUM).create();
    protected final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void trace() {
        try {
            logger.info("Serving " + request.getRequestURI() + "?"
                + request.getQueryString() + ". UserId: " + getLoggedInUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFromMemCache(String name) {
        cache.remove(name);
    }

    public void putInMemCache(String name, Object value) {
        cache.put(name, value);
    }

    public Object getFromMemCache(String name) {
        return cache.get(name);
    }

    protected String getLoggedInUserName() {
        if (getSessionAttribute(Constants.SK_USER) != null) {
            return ((User) getSessionAttribute(Constants.SK_USER)).email;
        } else {
            return "GUEST";
        }
    }

    protected Integer getLoggedInUserId() {
        if (getSessionAttribute(Constants.SK_USER) != null) {
            return ((User) getSessionAttribute(Constants.SK_USER)).userId;
        } else {
            return 0;
        }
    }

    protected User getLoggedInUser() {
    	return getSessionAttribute(Constants.SK_USER);
    }

    protected String getClientIP() {
        return request.getRemoteAddr();
    }

    public ClientInfo getClientInfo() {
        return new ClientInfo(getLoggedInUserName(), getClientIP());
    }

    public ClientInfo getClientInfo(String userId) {
        return new ClientInfo(userId, getClientIP());
    }

    protected <T> T getSessionAttribute(String key) {
        return (T) request.getSession().getAttribute(key);
    }

    protected void setSessionAttribute(String key, Object value) {
        request.getSession().setAttribute(key, value);
    }

    protected void removeSessionAttribute(String key) {
        request.getSession().removeAttribute(key);
    }

    protected void invalidateSession() {
        request.getSession().invalidate();
    }

    protected String getRequestParameter(String name) {
        return request.getParameter(name);
    }

    protected void sendObjectAsJson(Object obj) throws IOException {
        Json(gson.toJson(obj));
    }

    public void sendJsonResponse(Status s, Object payload) throws IOException {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("Status", s);
        obj.put("Payload", payload);
        Json(gson.toJson(obj)); //Normal JSON requests
    }

    public void sendJsonpResponse(Status s, Object payload) throws IOException {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("Status", s);
        obj.put("Payload", payload);
        // This is to take care of JSONP AJAX requests.
        String cb = getRequestParameter("callback");
        if (!StringUtils.isBlank(cb)) {
            StringBuilder jsonp = new StringBuilder();
            jsonp.append(cb).append("(").append(gson.toJson(obj)).append(")");
            JsonScript(jsonp.toString());
            logger.info("Sending JSONP response.");
        } else {
            Json(gson.toJson(obj)); //Normal JSON requests
        }

    }

    protected <T> T getJsonRequestAsObject(Type type) throws IOException {
        String jsonPayload = getJsonData();
        return (T) gson.fromJson(jsonPayload, type);
    }

    protected <T> T getJsonAsObject(Type type, String json) throws IOException {
        return (T) gson.fromJson(json, type);
    }

    protected void setJsonDateFormat(String fmt) {
        gson = new GsonBuilder().setExclusionStrategies(
            new AnnotationExclusionStrategy()).setDateFormat(fmt).create();
    }

    public HashMap objToMap(Object obj) {
        HashMap map = gson.fromJson(gson.toJson(obj), HashMap.class);
        return map;
    }

    protected void logAnalytics(String appId, String login, String receiver, String action) {
        if (!"true".equalsIgnoreCase(getConfigValue(Constants.CFG_ANALYTICS_ENABLED))) {
            return;
        }
        // TODO: Save the object
    }

	/**
	 * @param role
	 * @return
	 */
	protected boolean isCurrentUserInRole(RoleName role) {
		User u = (User) getSessionAttribute(Constants.SK_USER);
		return u != null && u.hasRole(role);
	}
}

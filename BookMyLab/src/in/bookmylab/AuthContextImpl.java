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

import in.bookmylab.jpa.Role;
import in.bookmylab.jpa.User;
import java.util.ArrayList;
import java.util.List;
import org.javamvc.core.AuthContext;

/**
 *
 * @author Balwinder Sodhi
 */
public class AuthContextImpl implements AuthContext {

    private final User currentUser;

    public AuthContextImpl(User currentUser) {
        this.currentUser = currentUser;
    }
    
    @Override
    public String getLoginName() {
        return currentUser.email;
    }

    @Override
    public List<String> getRoles() {
        ArrayList<String> roles = new ArrayList<>();
        for(Role r : currentUser.roleList) {
            roles.add(r.role);
        }
        return roles;
    }

    @Override
    public boolean hasRole(String roleName) {
        boolean has = false;
        for(Role r : currentUser.roleList) {
            if (roleName.equalsIgnoreCase(r.role)) {
                has = true;
                break;
            }
        }
        return has;
    }

    @Override
    public boolean isAuthenticated() {
        return currentUser != null && currentUser.userId > 0;
    }
    
}

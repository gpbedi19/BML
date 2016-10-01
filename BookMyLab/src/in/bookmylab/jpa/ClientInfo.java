/*
Copyright 2015 Balwinder Sodhi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package in.bookmylab.jpa;

/**
 *
 * @author Balwinder Sodhi
 */
public class ClientInfo {
    private String appUser;
    private String clientIp;

    public ClientInfo(String appUser, String clientIp) {
        this.appUser = appUser;
        this.clientIp = clientIp;
    }

    public String getAppUser() {
        return appUser;
    }

    public String getClientIp() {
        return clientIp;
    }
    
}

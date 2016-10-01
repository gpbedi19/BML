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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Balwinder Sodhi
 */
public class Utils {
    
    private static final String salt = "88yt5%432aSdAsdP3#1";
    private static Logger log = Logger.getLogger(Utils.class.getName());
    
    public static String hashPassword(String password) {
        String hashed=null;
        try {
            if (!StringUtils.isBlank(password)) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest((salt + password).getBytes(StandardCharsets.ISO_8859_1));
                //hashed = Base64.getEncoder().encodeToString(hash);
                hashed = Base64.encodeBase64String(hash);
            }
        } catch (NoSuchAlgorithmException ex) {
            log.log(Level.SEVERE, "Could not hash string.", ex);
        }
        return hashed;
    }
    
    public static Date parseDate(String strDate, String format) throws ParseException {
    	SimpleDateFormat df = new SimpleDateFormat(format);
    	return df.parse(strDate);
    }
    
}

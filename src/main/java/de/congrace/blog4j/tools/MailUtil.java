/*
 *  Copyright 2010 frank asseg.
 *  frank.asseg@congrace.de
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package de.congrace.blog4j.tools;

import java.nio.charset.Charset;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
public abstract class MailUtil {

    public static String scrambleEmail(String email) {
        StringBuilder encoded = new StringBuilder(email.length() * 6);
        //reverse the input
        email = new StringBuffer(email).reverse().toString();
        byte[] bytes = email.getBytes(Charset.forName("UTF-8"));
        for (int i = 0; i < bytes.length; i++) {
            String hex = byteToHex(bytes[i]);
            if (hex.equals("40"))
                hex = "7b;53;43;52;41;4d;42;4c;45;7d"; // the @ symbol
            if (hex.equals("2e"))
                hex = "5b;53;43;52;41;4d;42;4c;45;5d"; // the . symbol
            encoded.append(hex + ";");
        }
        return encoded.toString();
    }

    public static String byteToHex(byte b) {
        char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
        return new String(array);
    }
}

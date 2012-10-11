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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
public abstract class Blog4jCodeUtil {

    private static final Pattern pattern = Pattern.compile("\\[(\\/){0,1}(b|i|ol|ul|li|code|a href=\\\".*?\\\"( target=\\\".*?\\\"){0,1}|a)\\]", Pattern.DOTALL);
    private static final Pattern patternNonBreakingTagsOpen = Pattern.compile("\\[(ol|ul|li|code)\\]");
    private static final Pattern patternNonBreakingTagsClose = Pattern.compile("\\[\\/(ol|ul|li|code)\\]");

    public static String translate(String input) {
        input = replaceLineBreaks(input);
        Matcher m = pattern.matcher(input);
        while (m.find()) {
            String tag = m.group();
            tag = "<" + tag.substring(1, tag.length() - 1) + ">";
            input = input.substring(0, m.start()) + tag + input.substring(m.end());
        }
        return new String(input.replaceAll("\\<code\\>", "<pre class=\"prettyprint\">").replaceAll("\\<\\/code\\>", "</pre>"));
    }

    public static String stripTags(String input) {
        return input.replaceAll(pattern.toString(), "");
    }

    public static String replaceLineBreaks(String input) {
        String lineSeparator = System.getProperty("line.separator");
        String[] lines = input.split(lineSeparator);
        StringBuilder builder = new StringBuilder(input.length());
        int nonBreakingTagDepth = 0;
        for (String line : lines) {
            Matcher m = patternNonBreakingTagsOpen.matcher(line);
            while (m.find()) {
                nonBreakingTagDepth++;
            }
            m = patternNonBreakingTagsClose.matcher(line);
            while (m.find()) {
                nonBreakingTagDepth--;
            }
            if (nonBreakingTagDepth == 0) {
                builder.append(line + "<br/>");
            } else {
                builder.append(line + lineSeparator);
            }
        }
        return builder.toString();
    }
}

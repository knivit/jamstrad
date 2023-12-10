package com.tsoft.jamstrad.util;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public final class StringUtils {

    public static boolean isBlank(String str) {
        return (str == null) || str.isBlank();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String emptyForNull(String str) {
        return str == null ? "" : str;
    }

    public static String spaces(int width) {
        return repeat(' ', width);
    }

    public static String repeat(char c, int times) {
        if (times == 0) {
            return "";
        } else if (times == 1) {
            return String.valueOf(c);
        } else {
            StringBuilder sb = new StringBuilder(times);

            for(int i = 0; i < times; ++i) {
                sb.append(c);
            }

            return sb.toString();
        }
    }

    public static String truncate(String str, int width) {
        int n = str.length();
        return n <= width ? str : str.substring(0, width - 2) + "..";
    }

    public static String fitWidth(String str, int width) {
        return fitWidthLeftAlign(str, width);
    }

    public static String fitWidthLeftAlign(String str, int width) {
        int n = str.length();
        if (n == width) {
            return str;
        } else {
            return n > width ? str.substring(0, width - 2) + ".." : str + spaces(width - n);
        }
    }

    public static String fitWidthRightAlign(String str, int width) {
        int n = str.length();
        if (n == width) {
            return str;
        } else {
            return n > width ? ".." + str.substring(n - width, n) : spaces(width - n) + str;
        }
    }

    public static String fitWidthCenterAlign(String str, int width) {
        int n = str.length();
        if (n == width) {
            return str;
        } else if (n > width) {
            return str.substring(0, width - 2) + "..";
        } else {
            int spacesBefore = (width - n) / 2;
            int spacesAfter = width - n - spacesBefore;
            return spaces(spacesBefore) + str + spaces(spacesAfter);
        }
    }

    public static String leftPad(String str, int width, char padding) {
        int n = str.length();
        return n >= width ? str : repeat(padding, width - n) + str;
    }

    public static String rightPad(String str, int width, char padding) {
        int n = str.length();
        return n >= width ? str : str + repeat(padding, width - n);
    }

    public static List<String> splitOnNewlinesAndWrap(String str, int width) {
        List<String> wrappedLines = new Vector();
        List<String> lines = splitOnNewlines(str);
        Iterator var5 = lines.iterator();

        while(var5.hasNext()) {
            String line = (String)var5.next();
            Iterator var7 = wrap(line, width).iterator();

            while(var7.hasNext()) {
                String wrappedLine = (String)var7.next();
                wrappedLines.add(wrappedLine);
            }
        }

        return wrappedLines;
    }

    public static List<String> splitOnNewlines(String str) {
        List<String> lines = new Vector();
        if (!isEmpty(str)) {
            StringTokenizer st = new StringTokenizer(str, "\r\n");

            while(st.hasMoreTokens()) {
                lines.add(st.nextToken());
            }
        }

        return lines;
    }

    public static List<String> wrap(String str, int width) {
        List<String> lines = new Vector();
        if (!isEmpty(str)) {
            StringBuilder line = new StringBuilder(width);
            int lastWsIndex = -1;
            int i = 0;

            while(true) {
                char c;
                boolean ws;
                do {
                    if (i >= str.length()) {
                        if (line.length() > 0) {
                            lines.add(line.toString().trim());
                        }

                        return lines;
                    }

                    c = str.charAt(i++);
                    ws = Character.isWhitespace(c);
                    if (line.length() == width) {
                        int wrapFromIndex = width;
                        if (lastWsIndex > 0 && !ws) {
                            wrapFromIndex = lastWsIndex + 1;
                        }

                        lines.add(line.substring(0, wrapFromIndex).trim());
                        line.delete(0, wrapFromIndex);
                        lastWsIndex = -1;
                    }
                } while(line.length() <= 0 && ws);

                if (ws) {
                    lastWsIndex = line.length();
                }

                line.append(c);
            }
        } else {
            return lines;
        }
    }

    public static int toInt(String str, int defaultValue) {
        int result = defaultValue;
        if (!isEmpty(str)) {
            try {
                result = Integer.parseInt(str);
            } catch (NumberFormatException var4) {
            }
        }

        return result;
    }

    public static boolean containsIgnoringCase(String str, String substring) {
        if (str != null && substring != null) {
            if (substring.isEmpty()) {
                return true;
            } else {
                return substring.length() > str.length() ? false : str.toLowerCase().contains(substring.toLowerCase());
            }
        } else {
            return false;
        }
    }
}

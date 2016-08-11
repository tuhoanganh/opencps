package org.opencps.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "wewewewe\nwe\nwe\nwe\nwe\n";
			System.out.println("JsonUtils.main()"+quoteHTML(str));
	}

	public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "{}";
        }

        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        String       t;

        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
            case '\\':
            	sb.append("\\");
            	sb.append("\\");
                break;
            default:
                if (c < ' ') {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
	public static String quoteHTML(String string) {
		
		if (string == null || string.length() == 0) {
            return "{}";
        }

        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        String       t;

        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
            case '\\':
            	sb.append("REPLACEKEY");
                break;
            default:
                if (c < ' ') {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
		String result = sb.toString().replaceAll("REPLACEKEYn", "<br />");
        return result;
    }
}

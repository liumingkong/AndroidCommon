package com.black.common.json;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;

public class JsonBuilder {

    public static final String UTF_8 = "UTF-8";
    //endregion

    private StringBuilder sb;
    private boolean flip = false;

    public JsonBuilder() {
        sb = new StringBuilder();
        sb.append("{");
    }

    public JsonBuilder(int initCapacity) {
        sb = new StringBuilder(initCapacity);
        sb.append("{");
    }

    public JsonBuilder(String content, boolean flip) {
        sb = new StringBuilder(content);
        this.flip = flip;
    }

    public JsonBuilder(JsonBuilder json) {
        sb = new StringBuilder(json.sb);
        flip = json.flip;
    }

    public JsonBuilder append(String name, String value) {
        if (name == null || value == null)
            return this;

        if (sb.length() > 1)
            sb.append(",");
        sb.append("\"").append(name).append("\":\"").append(toJsonStr(value)).append("\"");
        return this;
    }

    public JsonBuilder appendRaw(String name, String value) {
        if (name == null || value == null)
            return this;

        if (sb.length() > 1)
            sb.append(",");
        sb.append("\"").append(name).append("\":").append(toJsonStr(value));
        return this;
    }

    public JsonBuilder append(String name, boolean value) {
        if (sb.length() > 1)
            sb.append(",");
        sb.append("\"").append(name).append("\":").append(value);
        return this;
    }

    public JsonBuilder append(String name, long value) {
        if (sb.length() > 1)
            sb.append(",");
        sb.append("\"").append(name).append("\":").append(value);
        return this;
    }

    public JsonBuilder append(String name, double value) {
        if (sb.length() > 1)
            sb.append(",");
        sb.append("\"").append(name).append("\":").append(value);
        return this;
    }

    public JsonBuilder append(String name, int value) {
        if (sb.length() > 1)
            sb.append(",");
        sb.append("\"").append(name).append("\":").append(value);
        return this;
    }

    public JsonBuilder append(String name, JsonBuilder value) {
        return appendJsonValue(name, value == null ? null : value.toString());
    }

    public JsonBuilder appendJsonValue(String name, String jsonValue) {
        if (name == null || jsonValue == null)
            return this;

        if (sb.length() > 1)
            sb.append(",");
        sb.append("\"").append(name).append("\":").append(jsonValue);
        return this;
    }

    public JsonBuilder flip() {
        sb.append('}');
        flip = true;
        return this;
    }

    public JsonBuilder reset() {
        sb.setLength(0);
        sb.append("{");
        return this;
    }


    public JsonBuilder appendJsonArr(String name, List<JsonBuilder> jsonArr) {
        if (name == null)
            return this;

        if (sb.length() > 1)
            sb.append(",");
        sb.append("\"").append(name).append("\":");
        if (jsonArr != null) {
            sb.append("[");
            int i = 0;
            for (JsonBuilder s : jsonArr) {
                if (i++ > 0) {
                    sb.append(",");
                }
                sb.append(s.toString());
            }
            sb.append("]");
        } else {
            sb.append("[]");
        }

        return this;
    }

    public JsonBuilder appendLongSet(String name, HashSet<Long> longSet) {
        if (name == null)
            return this;

        if (sb.length() > 1)
            sb.append(",");
        sb.append("\"").append(name).append("\":");
        if (longSet != null) {
            sb.append("[");
            int i = 0;
            for (long s : longSet) {
                if (i++ > 0) {
                    sb.append(",");
                }
                sb.append(s);
            }
            sb.append("]");
        } else {
            sb.append("[]");
        }
        return this;
    }

    public JsonBuilder appendStringList(String name, List<String> stringList) {
        if (name == null)
            return this;

        if (sb.length() > 1)
            sb.append(",");
        sb.append("\"").append(name).append("\":");
        if (stringList != null) {
            sb.append("[\"");
            int i = 0;
            for (String s : stringList) {
                if (i++ > 0) {
                    sb.append("\",\"");
                }
                sb.append(s);
            }
            sb.append("\"]");
        } else {
            sb.append("[]");
        }
        return this;
    }

    public static String toJsonStr(String value) {
        if (value == null)
            return null;
        boolean valid = true;
        int length = value.length();
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c < 32 || c == '"' || c == '\\' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b') {
                valid = false;
                break;
            }
        }
        if (valid)
            return value;
        int size = value.length();
        StringBuilder buf = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"':
                    buf.append("\\\"");
                    break;
                case '\\':
                    buf.append("\\\\");
                    break;
                case '\n':
                    buf.append("\\n");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\t':
                    buf.append("\\t");
                    break;
                case '\f':
                    buf.append("\\f");
                    break;
                case '\b':
                    buf.append("\\b");
                    break;

                default:
                    if (c < 32) {
                        buf.append("\\u00");
                        String str = Integer.toHexString(c);
                        if (str.length() == 1)
                            buf.append('0');
                        buf.append(str);
                    } else {
                        buf.append(c);
                    }
            }
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        if (!flip)
            flip();
        return sb.toString();
    }

    public JsonBuilder appendEncodedString(String name, String value) throws UnsupportedEncodingException {
        if (name == null || value == null)
            return this;

        if (sb.length() > 1)
            sb.append(",");

        String strEncodedValue = URLEncoder.encode(toJsonStr(value), UTF_8);
        sb.append("\"").append(name).append("\":\"").append(strEncodedValue).append("\"");
        return this;
    }
}
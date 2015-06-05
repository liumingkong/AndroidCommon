package com.black.common.json;

import android.util.Log;

import com.black.common.utils.Utils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.NumericNode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class JsonWrapper {

    //region 编码常量
    public static final String UTF_8 = "UTF-8";
    //endregion

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        /* to enable standard indentation ("pretty-printing") */
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        /* to allow coercion of JSON empty String ("") to null Object value */
        OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        /* to write java.util.Date, Calendar as number (timestamp) */
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        // to prevent exception when encountering unknown property:
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // to allow C/C++ style comments in JSON (non-standard, disabled by default)
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        // to allow (non-standard) unquoted field names in JSON:
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // to allow use of apostrophes (single quotes), non standard
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    private JsonNode root;

    public JsonWrapper(String json) {
        /* TODO which approach should be chosen?*/
        if (!Utils.isEmptyString(json)) {
            try {
                root = OBJECT_MAPPER.readTree(json);
                //            JsonParser jp = new JsonFactory().disable(
                //                    JsonParser.Feature.STRICT_DUPLICATE_DETECTION).enable(
                //                    JsonParser.Feature.ALLOW_COMMENTS).createParser(json);
                //            this.root = jp.readValueAsTree();
            } catch (Throwable t) {
                throw new IllegalArgumentException(t);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public JsonWrapper(JsonNode root) {
        this.root = root;
    }

    public String get(String name) {
        JsonNode node = getJsonNode(name);
        return (node == null ? null : node.asText());
    }

    public int getInt(String name) {
        return getInt(name, 0);
    }

    public int getInt(String name, int defaultValue) {
        JsonNode node = getJsonNode(name);
        if (node == null) return defaultValue;
        else if (node instanceof NumericNode) return node.intValue();
        else return str2int(node.textValue(), defaultValue);
    }

    public double getDouble(String name) {
        return getDouble(name, 0.0);
    }

    public double getDouble(String name, double defaultValue) {
        JsonNode node = getJsonNode(name);
        if (node == null) return defaultValue;
        else if (node instanceof NumericNode) return node.doubleValue();
        else return str2double(node.textValue(), defaultValue);
    }

    public long getLong(String name) {
        return getLong(name, 0L);
    }

    public long getLong(String name, long defaultValue) {
        JsonNode node = getJsonNode(name);
        if (node == null) return defaultValue;
        else if (node instanceof NumericNode) return node.longValue();
        else return str2long(node.textValue(), defaultValue);
    }

    public float getFloat(String name) {
        return getFloat(name, 0F);
    }

    public float getFloat(String name, float defaultValue) {
        JsonNode node = getJsonNode(name);
        if (node == null) return defaultValue;
        else if (node instanceof NumericNode) return node.floatValue();
        else return str2float(node.textValue(), defaultValue);
    }

    public JsonWrapper getNode(String name) {
        return new JsonWrapper(getJsonNode(name));
    }

    public List<JsonNode> getList(String name) {
        JsonNode node = getJsonNode(name);
        List<JsonNode> result = new ArrayList<JsonNode>();
        if (null == node) {
            return result;
        }
        Iterator<JsonNode> iter = node.elements();
        while (iter.hasNext()) {
            JsonNode next = iter.next();
            if (next != null) {
                result.add(next);
            }
        }
        return result;
    }

    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        JsonNode node = getJsonNode(name);
        if (node == null) return defaultValue;
        else if (node instanceof BooleanNode) return node.booleanValue();
        else return Boolean.parseBoolean(node.textValue());
    }


    public JsonWrapper getArrayNode(int idx) {
        if (root != null && root.isArray()) {
            if (idx >= 0 && idx < root.size()) return new JsonWrapper(root.get(idx));
        }
        return null;
    }

    public String getArrayNodeValue(int idx) {
        if (root != null && root.isArray()) {
            if (idx >= 0 && idx < root.size()) {
                JsonNode node = root.get(idx);
                return (node == null ? null : node.textValue());
            }
        }
        return null;
    }

    public int getArrayNodeIntValue(int idx, int defaultValue) {
        if (root != null && root.isArray()) {
            if (idx >= 0 && idx < root.size()) {
                JsonNode node = root.get(idx);
                if (node == null) return defaultValue;
                else if (node instanceof NumericNode) return node.intValue();
                else return str2int(node.textValue(), defaultValue);
            }
        }
        return defaultValue;
    }

    public long getArrayNodeLongValue(int idx, long defaultValue) {
        if (root != null && root.isArray()) {
            if (idx >= 0 && idx < root.size()) {
                JsonNode node = root.get(idx);
                if (node == null) return defaultValue;
                else if (node instanceof NumericNode) return node.longValue();
                else return str2long(node.textValue(), defaultValue);
            }
        }
        return defaultValue;
    }

    public boolean isArray() {
        return (root != null && root.isArray());
    }

    public int size() {
        if (root != null) return root.size();
        else return 0;
    }

    public Map<String, String> values() {
        Map<String, String> map = new HashMap<String, String>();
        for (Iterator<String> iterator = root.fieldNames(); iterator.hasNext(); ) {
            String field = iterator.next();
            String value = null;
            JsonNode node = root.get(field);
            if (node != null) value = node.textValue();
            map.put(field, value);
        }
        return map;
    }

    public boolean isEmpty() {
        return (root == null || root.size() == 0);
    }

    public boolean isNull() {
        return (root == null);
    }

    public JsonNode getJsonNode(String name) {
        if (name == null || root == null) return null;
        JsonNode n = root.get(name);
        if (n != null) return n;
        JsonNode node = root;
        StringTokenizer st = new StringTokenizer(name, ".");
        while (st.hasMoreTokens()) {
            String key = st.nextToken().trim();
            if ((key == null || key.length() == 0) || (node = node.get(key)) == null)
                return null;
        }
        return node;
    }

    private int str2int(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private long str2long(String s, long defaultValue) {
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            try {
                double d = Double.parseDouble(s);
                return (long) d;
            } catch (Exception ex) {
                //ignore
            }
            return defaultValue;
        }
    }

    private float str2float(String s, float defaultValue) {
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            try {
                double d = Double.parseDouble(s);
                return (float) d;
            } catch (Exception ex) {
                //ignore
            }
            return defaultValue;
        }
    }

    private double str2double(String s, double defaultValue) {

        try {
            double d = Double.parseDouble(s);
            return (float) d;
        } catch (Exception ex) {
            //ignore
        }
        return defaultValue;

    }

    @Override
    public String toString() {
        return ((Object) this.root).toString();
    }

    public String[] getStringArr(String name) {
        List<JsonNode> result = getList(name);
        int size = result.size();
        String[] strs = new String[size];
        for (int i = 0; i < size; i++) {
            JsonNode node = result.get(i);
            strs[i] = node.textValue();
        }
        return strs;
    }

    public Long[] getLongArr(String name) {
        List<JsonNode> result = getList(name);
        int size = result.size();
        Long[] longs = new Long[size];
        for (int i = 0; i < size; i++) {
            JsonNode node = result.get(i);
            longs[i] = node.longValue();
        }
        return longs;
    }

    public long[] getlongArr(String name) {
        List<JsonNode> result = getList(name);
        int size = result.size();
        long[] longs = new long[size];
        for (int i = 0; i < size; i++) {
            JsonNode node = result.get(i);
            longs[i] = node.longValue();
        }
        return longs;
    }

    public HashSet<Long> getLongSet(String name) {
        List<JsonNode> result = getList(name);
        HashSet<Long> longs = new HashSet<Long>();
        int size = result.size();
        for (int i = 0; i < size; i++) {
            JsonNode node = result.get(i);
            longs.add(node.longValue());
        }
        return longs;
    }

    /** 编码解码辅助函数 */
    public String getDecodedString(String name){
        JsonNode node = getJsonNode(name);
        String result = null;
        if (!Utils.isNull(node)) {
            try {
                result =  URLDecoder.decode(URLEncoder.encode(node.asText(), UTF_8), UTF_8);
            } catch (UnsupportedEncodingException e) {
                Log.e("JsonWrapper", "getDecodedString:name:"+name+",node:"+node, e);
            } catch (Exception e){
                Log.e("JsonWrapper", "getDecodedString:name:"+name+",node:"+node, e);
            }
        }
        return result;
    }

    public static void main(String[] args){

    }
}

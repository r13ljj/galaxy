package com.jonex.dss.common.network;

import java.io.Serializable;
import java.util.Map;

/**
 * <pre>
 *
 *  File: URL.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/3/23				lijunjun				Initial.
 *
 * </pre>
 */
public class URL implements Serializable{

    private final String protocol;
    private final String host;
    private final int port;
    private final String path;
    private final Map<String, String> parameters;
    private transient volatile Map<String, Number> numbers;
    private transient volatile Map<String, URL> urls;
    private transient volatile String ip;
    private transient volatile String full;
    private transient volatile String identity;
    private transient volatile String parameter;
    private transient volatile String string;

    public URL(String protocol, String host, int port, String path, Map<String, String> parameters) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        this.parameters = parameters;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<String, Number> getNumbers() {
        return numbers;
    }

    public void setNumbers(Map<String, Number> numbers) {
        this.numbers = numbers;
    }

    public Map<String, URL> getUrls() {
        return urls;
    }

    public void setUrls(Map<String, URL> urls) {
        this.urls = urls;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}

package com.endicott.edu.api;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Arrays;

public abstract class ServletTemplate<T extends Serializable> extends javax.servlet.http.HttpServlet {

    /**
     * TODO: add docstring...
     * @param request
     * @param response
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] pathSegments = getPathSegments(request);
        if (pathSegments.length == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        T data = handleGet(pathSegments);
        T[] dataList = handleGetList(pathSegments);
        if (data == null && dataList == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        setResponseHeaders(response);
        if (data != null && dataList == null) {
            sendResponse(response, data);
        } else if (data == null) {
            sendResponse(response, dataList);
        }
    }

    // TODO: add support for POST requests...
    // protected void doPost() { }

    /**
     * Parse the path info of a request into an array of path segments.
     * @param request The request to parse.
     * @return An array of string path segments. For example, if a servlet is mapped to the url "localhost:8080/college",
     * and a request is received for "localhost:8080/college/EndicottCollege/123", the returned result is ["EndicottCollege", "123"].
     * If the request URL is for example "localhost:8080/college" or "localhost:8080/college/", an empty array is returned.
     */
    private String[] getPathSegments(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return new String[0];
        }
        String[] pathSegments = pathInfo.split("/");
        if (pathSegments.length < 2) {
            return pathSegments;
        }
        return Arrays.copyOfRange(pathSegments, 1, pathSegments.length);
    }

    private void setResponseHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
        response.setContentType("application/json");
    }

    /**
     * Serializes the given data to JSON and sends it in the given response.
     * @param response The response to send.
     * @param data The data of type T to serialize and send in the response.
     * @throws IOException if response fails to send.
     */
    private void sendResponse(HttpServletResponse response, T data) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        PrintWriter writer = response.getWriter();
        writer.print(json);
        writer.flush();
    }

    /**
     * Serializes the given data to JSON and sends it in the given response.
     * @param response The response to send.
     * @param dataList The array of data of type T to serialize and send in the response.
     * @throws IOException if response fails to send.
     */
    private void sendResponse(HttpServletResponse response, T[] dataList) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(dataList);
        PrintWriter writer = response.getWriter();
        writer.print(json);
        writer.flush();
    }

    protected abstract T handleGet(String[] pathSegments);

    protected abstract T[] handleGetList(String[] pathSegments);

    // TODO: add support for POST requests...
    // protected abstract void handlePost(String[] pathSegments);
}

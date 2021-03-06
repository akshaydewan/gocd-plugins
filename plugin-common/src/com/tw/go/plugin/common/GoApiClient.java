package com.tw.go.plugin.common;

import org.json.JSONArray;
import org.json.CDL;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

/**
 * Created by MarkusW on 17.11.2015.
 */
public class GoApiClient extends ApiRequestBase {


    public GoApiClient(String apiUrl) throws GeneralSecurityException{
        super(apiUrl, "", "", true);
    }

    public String getJobProperty(String pipelineName, String pipelineCounter, String stageName, String stageCounter, String jobName, String propertyName) throws IOException {
        String uri = getJobPropertyRequestUri(pipelineName, pipelineCounter, stageName, stageCounter, jobName, propertyName);
        String resultCSV = requestGet(uri);

        // result is delivered in CSV. Only for this api function. We want to use JSON, so convert the stuff
        JSONArray array = CDL.toJSONArray(resultCSV);
        JSONObject obj = array.getJSONObject(0);

        // get the request property value
        return obj.getString(propertyName);
    }

    public String setJobProperty(String pipelineName, String pipelineCounter, String stageName, String stageCounter, String jobName, String propertyName, String propertyValue) throws IOException {
        String uri = getJobPropertyRequestUri(pipelineName, pipelineCounter, stageName, stageCounter, jobName, propertyName);
        String urlParameters = "value=" + URLEncoder.encode(propertyValue, "UTF-8");

        requestPostFormUrlEncoded(uri, urlParameters);
        return getJobProperty(pipelineName, pipelineCounter, stageName, stageCounter, jobName, propertyName);
    }

    public JSONObject getJobProperties(String pipelineName, String pipelineCounter, String stageName, String stageCounter, String jobName) throws IOException {
        String uri = getJobPropertyRequestUri(pipelineName, pipelineCounter, stageName, stageCounter, jobName);
        String resultCSV = requestGet(uri);

        // result is delivered in CSV. Only for this api function. We want to use JSON, so convert the stuff
        JSONArray array = CDL.toJSONArray(resultCSV);
        return array.getJSONObject(0);
    }

    private String getJobPropertyRequestUri(String pipelineName, String pipelineCounter, String stageName, String stageCounter, String jobName, String propertyName){
        return getApiUrl() + "properties/" + pipelineName + "/" + pipelineCounter + "/" + stageName + "/" + stageCounter + "/" + jobName + "/" + propertyName;
    }

    private String getJobPropertyRequestUri(String pipelineName, String pipelineCounter, String stageName, String stageCounter, String jobName){
        return getApiUrl() + "properties/" + pipelineName + "/" + pipelineCounter + "/" + stageName + "/" + stageCounter + "/" + jobName;
    }

}

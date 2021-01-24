package com.commerce.backend.api;

import com.google.gson.Gson;
import com.commerce.backend.error.exception.ConnectionException;
import com.commerce.backend.error.exception.HTTPException;
import com.commerce.backend.error.exception.InvalidClientException;
import com.commerce.backend.model.instamojo.AccessToken;
import com.commerce.backend.util.Constants;
import com.commerce.backend.util.HttpUtils;
import org.apache.http.util.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class ApiContext.
 */
public class ApiContext {

    private static final Logger LOGGER = Logger.getLogger(ApiContext.class.getName());

    public enum Mode {
        TEST, LIVE
    }

    private volatile static ApiContext instance;

   
    private AccessToken accessToken;

    
    private long tokenCreationTime;

   
    private String clientId;

    
    private String clientSecret;

    private String username;

    private String password;

    private Mode mode;

    private ApiContext(String clientId, String clientSecret, Mode mode) {
        this(clientId, clientSecret, null, null, mode);
    }

    private ApiContext(String clientId, String clientSecret, String username, String password, Mode mode) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.mode = mode;
        this.username = username;
        this.password = password;
    }

 
    public static ApiContext create(String clientId, String clientSecret, Mode mode) {
        if (instance == null) {
            synchronized (ApiContext.class) {
                if (instance == null) {
                    instance = new ApiContext(clientId, clientSecret, mode);
                }
            }
        }

        return instance;
    }

   
    public static ApiContext create(String clientId, String clientSecret, String username, String password, Mode mode) {
        if (instance == null) {
            synchronized (ApiContext.class) {
                if (instance == null) {
                    instance = new ApiContext(clientId, clientSecret, username, password, mode);
                }
            }
        }

        return instance;
    }

    private boolean isTokenExpired() {
        long durationInSeconds = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - tokenCreationTime);
        return durationInSeconds >= (accessToken.getExpiresIn() - 300);
    }

    private void loadOrRefreshAccessToken() throws ConnectionException, HTTPException {
        if (accessToken == null) {
            fetchAccessToken();

        } else if (isTokenExpired()) {
            refreshAccessToken();
        }
    }

   
    private synchronized void fetchAccessToken() throws ConnectionException, HTTPException {
        if (accessToken != null) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_CLIENT_ID, clientId);
        params.put(Constants.PARAM_CLIENT_SECRET, clientSecret);

        if (isUserAuthFlow()) {
            params.put(Constants.PARAM_GRANT_TYPE, Constants.PARAM_PASSWORD);
            params.put(Constants.PARAM_USERNAME, username);
            params.put(Constants.PARAM_PASSWORD, password);

        } else {
            params.put(Constants.PARAM_GRANT_TYPE, Constants.GRANT_TYPE_CLIENT_CREDENTIALS);
        }

        loadAccessToken(params);
    }

   
    private synchronized void refreshAccessToken() throws ConnectionException, HTTPException {
        if (!isTokenExpired()) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_CLIENT_ID, clientId);
        params.put(Constants.PARAM_CLIENT_SECRET, clientSecret);

        if (isUserAuthFlow() && accessToken.getRefreshToken() != null) {
            // For user based authentication, refresh the token
            params.put(Constants.PARAM_GRANT_TYPE, Constants.GRANT_TYPE_REFRESH_TOKEN);
            params.put(Constants.PARAM_REFRESH_TOKEN, accessToken.getRefreshToken());

        } else {
            params.put(Constants.PARAM_GRANT_TYPE, Constants.GRANT_TYPE_CLIENT_CREDENTIALS);
        }

        loadAccessToken(params);
    }

    private void loadAccessToken(Map<String, String> params) throws ConnectionException, HTTPException {
        try {
            String response = HttpUtils.post(getAuthEndpoint(), null, params);

            AccessToken accessTokenResponse = new Gson().fromJson(response,
                    AccessToken.class);

            if (TextUtils.isEmpty(accessTokenResponse.getToken())) {
                throw new InvalidClientException(
                        "Could not get the access token due to " + accessTokenResponse.getError());
            }

            this.accessToken = accessTokenResponse;
            this.tokenCreationTime = System.nanoTime();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw new ConnectionException(e.toString(), e);
        }
    }

    /**
     */
    public static void ClearInstance() {
        if (instance != null) {
            synchronized (ApiContext.class) {
                instance = null;
            }
        }
    }

    /*
     */
    public String getAuthorization() throws ConnectionException, HTTPException {
        loadOrRefreshAccessToken();
        return accessToken.getTokenType() + " " + accessToken.getToken();
    }

    /*
     */
    public String getApiPath(String path) {
        String apiPath = getApiEndpoint() + path;

        if (!apiPath.endsWith("/")) {
            apiPath += Character.toString('/');
        }
        return apiPath;
    }

    private String getApiEndpoint() {
        if (mode == Mode.TEST) {
            return Constants.INSTAMOJO_TEST_API_ENDPOINT;
        }

        return Constants.INSTAMOJO_LIVE_API_ENDPOINT;
    }

    private String getAuthEndpoint() {
        if (mode == Mode.TEST) {
            return Constants.INSTAMOJO_TEST_AUTH_ENDPOINT;
        }

        return Constants.INSTAMOJO_LIVE_AUTH_ENDPOINT;
    }

    private boolean isUserAuthFlow() {
        return (username != null && password != null);
    }
}

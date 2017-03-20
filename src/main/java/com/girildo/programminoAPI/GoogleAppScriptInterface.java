package com.girildo.programminoAPI;

	

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import java.util.Map;
import com.google.api.services.script.model.*;
import com.google.api.services.script.Script;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GoogleAppScriptInterface {
	/** Application name. */
    private static final String APPLICATION_NAME = "Il Programmino";
	
    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/programmino");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/script-java-quickstart
     */
    private static final List<String> SCOPES =
        Arrays.asList("https://www.googleapis.com/auth/drive", "https://www.googleapis.com/auth/forms");

    static 
    {
        try 
        {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    private static Credential authorize() throws IOException 
    {
        // Load client secrets.
        InputStream in =
            MainWindow.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Create a HttpRequestInitializer from the given one, except set
     * the HTTP read timeout to be longer than the default (to allow
     * called scripts time to execute).
     *
     * @param {HttpRequestInitializer} requestInitializer the initializer
     *     to copy and adjust; typically a Credential object.
     * @return an initializer with an extended read timeout.
     */
    private static HttpRequestInitializer setHttpTimeout(
            final HttpRequestInitializer requestInitializer) 
    {
        return new HttpRequestInitializer() 
        {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException 
            {
                requestInitializer.initialize(httpRequest);
                // This allows the API to call (and avoid timing out on)
                // functions that take up to 6 minutes to complete (the maximum
                // allowed script run time), plus a little overhead.
                httpRequest.setReadTimeout(380000);
            }
        };
    }

    /**
     * Build and return an authorized Script client service.
     *
     * @param {Credential} credential an authorized Credential object
     * @return an authorized Script client service
     */
    private static Script getScriptService() throws IOException 
    {
        Credential credential = authorize();
        return new Script.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, setHttpTimeout(credential))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Interpret an error response returned by the API and return a String
     * summary.
     *
     * @param {Operation} op the Operation returning an error response
     * @return summary of error response, or null if Operation returned no
     *     error
     */
    @SuppressWarnings("unchecked")
	private static String getScriptError(Operation op) 
    {
        if (op.getError() == null) 
        {
            return null;
        }

        // Extract the first (and only) set of error details and cast as a Map.
        // The values of this map are the script's 'errorMessage' and
        // 'errorType', and an array of stack trace elements (which also need to
        // be cast as Maps).
        Map<String, Object> detail = op.getError().getDetails().get(0);
        List<Map<String, Object>> stacktrace =
                (List<Map<String, Object>>)detail.get("scriptStackTraceElements");

        java.lang.StringBuilder sb =
                new StringBuilder("\nScript error message: ");
        sb.append(detail.get("errorMessage"));
        sb.append("\nScript error type: ");
        sb.append(detail.get("errorType"));

        if (stacktrace != null) 
        {
            // There may not be a stacktrace if the script didn't start
            // executing.
            sb.append("\nScript error stacktrace:");
            for (Map<String, Object> elem : stacktrace) {
                sb.append("\n  ");
                sb.append(elem.get("function"));
                sb.append(":");
                sb.append(elem.get("lineNumber"));
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    public static String createGoogleForm(String name, int maxPhotos) throws IOException {
        // ID of the script to call. Acquire this from the Apps Script editor,
        // under Publish > Deploy as API executable.
        String scriptId = "MbQBrslbuTnbWOWchBZeu-No2Ik91b-Tj";
        Script service = getScriptService();

        // Create an execution request object.
        ExecutionRequest request = new ExecutionRequest()
                .setFunction("createForm").setParameters(Arrays.asList(name, maxPhotos)).setDevMode(true);
        Object url = null;
        try 
        {
            // Make the API request.
            Operation op =
                    service.scripts().run(scriptId, request).execute();
            
            
            
            // Print results of request.
            if (op.getError() != null) 
            {
                // The API executed, but the script returned an error.
                System.out.println(getScriptError(op));
                return null;
            }
            url = op.getResponse().get("result");
        } catch (GoogleJsonResponseException e) {
            // The API encountered a problem before the script was called.
            e.printStackTrace(System.out);
            return null;
        }
        retrieveFormResponses((String)url);
        return postProcessUrl(url);
    }
    
    public static void retrieveFormResponses(String url) throws IOException
    {
    	String scriptId = "MbQBrslbuTnbWOWchBZeu-No2Ik91b-Tj";
        Script service = getScriptService();

        // Create an execution request object.
        ExecutionRequest request = new ExecutionRequest()
                .setFunction("retrieveResponses").setParameters(Arrays.asList(url)).setDevMode(true);
        try 
        {
            // Make the API request.
            Operation op =
                    service.scripts().run(scriptId, request).execute();
            
            Object resp = op.getResponse().get("result");
            
            // Print results of request.
            if (op.getError() != null) 
            {
                // The API executed, but the script returned an error.
                System.out.println(getScriptError(op));
                return;
            }
        } catch (GoogleJsonResponseException e) {
            // The API encountered a problem before the script was called.
            e.printStackTrace(System.out);
            return;
        }
    }
    
    
    private static String postProcessUrl(Object in)
    {
    	String s = (String)in;
    	s = s.substring(0, s.lastIndexOf('/'));
    	s += "/viewform";
    	return s;
    }
}

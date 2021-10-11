package shane.testing.core.base;

import com.google.api.client.util.ArrayMap;
import com.qaprosoft.carina.core.foundation.utils.R;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import shane.testing.config.services.EnvironmentConstant;
import shane.testing.core.config.ScenarioContext;
import shane.testing.core.exception.TestContextException;
import shane.testing.core.logging.restassured.RestAssuredRequestFilter;
import shane.testing.core.logging.restassured.RestAssuredResponseFilter;

import java.util.Map;

public abstract class BaseRestService implements Service<RequestSpecification, Method, Response> {

    protected ScenarioContext scenarioContext;
    private String host;
    private String protocol = "https";
    private int port = 443;
    private Map<String, String> cookies = new ArrayMap<>();
    private Map<String, String> headers = new ArrayMap<>();

    protected BaseRestService(String protocol, String host, int port) {
        this(host);
        this.protocol = protocol;
        this.port = port;
    }

    protected BaseRestService(String host) {
        this();
        this.host = host;
    }

    protected BaseRestService() {
        this.scenarioContext = ScenarioContext.getInstance();
    }

    protected void setHost(String host) {
        this.host = host;
    }

    protected void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    protected void setPort(int port) {
        this.port = port;
    }

    protected RequestSpecification getDefaultRequestBuilder(String apiPath) {
        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.URLENC.withCharset(R.CONFIG.get(EnvironmentConstant.DEFAULT_CHARSET)))
                .baseUri(this.getServiceUrl()).basePath(apiPath)
                .relaxedHTTPSValidation().cookies(cookies).headers(headers)
                .filters(new RestAssuredRequestFilter(), new RestAssuredResponseFilter());
        return requestSpecification;
    }

    @Override
    public Response dispatchServiceRequest(RequestSpecification request, Method method) {
        switch (method) {
            case POST:
                return request.post().thenReturn();
            case PUT:
                return request.put().thenReturn();
            case GET:
                return request.get().thenReturn();
            case DELETE:
                return request.delete().thenReturn();
            case HEAD:
                return request.head().thenReturn();
            case OPTIONS:
                return request.options().thenReturn();
            case PATCH:
                return request.patch().thenReturn();
            default:
                throw new TestContextException(String.format("Not Support Request Method %s", method.name()));
        }
    }


    @Override
    public String getServiceUrl() {
        return String.format("%s://%s:%d", this.protocol, this.host, this.port);
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}

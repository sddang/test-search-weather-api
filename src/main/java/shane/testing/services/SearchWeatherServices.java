package shane.testing.services;

import com.qaprosoft.carina.core.foundation.utils.R;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import shane.testing.config.SearchWeatherConstant;
import shane.testing.core.base.BaseRestService;
import shane.testing.dto.request.SearchRequest;
import shane.testing.dto.response.search.SearchResponse;
import shane.testing.util.ObjectMapperUtils;

public class SearchWeatherServices extends BaseRestService {

    private static final String SEARCH_WEATHER_PATH = R.API.get(SearchWeatherConstant.SEARCH_URI);

    public SearchWeatherServices(String protocol, String host, int port) {
        super(protocol, host, port);
    }

    public SearchWeatherServices(String host) {
        super(host);
    }

    public SearchWeatherServices() {
        super();
        this.setHost(R.API.get(SearchWeatherConstant.DOMAIN_URI));
    }

    public SearchResponse submitSearch(SearchRequest submitSearch)
            throws IllegalArgumentException {
        RequestSpecification spec = this.getDefaultRequestBuilder(SEARCH_WEATHER_PATH)
                .params(submitSearch.getDefaultRequestParams());
        Response response = this.dispatchServiceRequest(spec, Method.GET);
        return ObjectMapperUtils.convertResponseToDTOObject(response.getBody().asString(),
                SearchResponse.class);
    }
}

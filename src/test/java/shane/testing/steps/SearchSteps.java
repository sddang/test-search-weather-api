package shane.testing.steps;

import com.qaprosoft.carina.core.foundation.utils.R;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import shane.testing.config.SearchWeatherConstant;
import shane.testing.dto.request.SearchRequest;
import shane.testing.dto.response.search.SearchResponse;
import shane.testing.dto.response.search.forecast.ForecastInfo;
import shane.testing.core.base.BaseSteps;
import shane.testing.util.ObjectMapperUtils;

import java.util.Map;


public class SearchSteps extends BaseSteps {

    private final String APP_ID = R.API.get(SearchWeatherConstant.API_KEY);

    public SearchSteps() {
        super();
    }

    @Given("I perform search with {string}")
    public void iPerformSearchWith(String value) {
        scenarioContext.setContext(SearchWeatherConstant.SEARCH_RESPONSE, searchWeatherServices.submitSearch(searchRequestDTO(value)));
    }

    @Then("The overall response status should be")
    public void assertResult(Map<String, String> dataTable) {
        SearchResponse createSearchData = ObjectMapperUtils.createDTOObjectByDataTable(SearchResponse.class, dataTable);
        String expectedMessage = createSearchData.getMessage();
        String expectedCod = createSearchData.getCod();
        String expectedCount = createSearchData.getCount();
        SearchResponse actualResponse = scenarioContext.getContext(SearchWeatherConstant.SEARCH_RESPONSE);
        String actualMessage = actualResponse.getMessage();
        String actualCode = actualResponse.getCod();
        String actualCount = actualResponse.getCount();
        boolean comparing = expectedMessage.equals(actualMessage) && expectedCod.equals(actualCode) && expectedCount.equals(actualCount);
        Assert.assertTrue(comparing, String.format("Verify overall status.\n" +
                                "Expected:[message]=%s, [cod]=%s, [count]=%s\nActual: [message]=%s, [cod]=%s, [count]=%s",
                        expectedMessage, expectedCod, expectedCount, actualMessage, actualCode, actualCount));
    }

    private SearchRequest searchRequestDTO(String searchValue) {
        return new SearchRequest(searchValue, APP_ID);
    }


    @And("Response city and country should be {string} and {string}")
    public void responseCityAndCountryShouldBeAnd(String city, String country) {
        String detachCityName = city.split(",")[0];
        SearchResponse actualResponse = scenarioContext.getContext(SearchWeatherConstant.SEARCH_RESPONSE);
        for(ForecastInfo forecastInfo : actualResponse.getForecastItems()){
            boolean comparing = forecastInfo.getName().contains(detachCityName) && forecastInfo.getSys().getCountry().equals(country);
            Assert.assertTrue(comparing, String.format("Verify item with [id]:%s.\n" +
                    "Expected: [name]=%s, [country]=%s\n" +
                    "Actual: [name]=%s, [country]=%s\n",
                    forecastInfo.getId(), city, country, forecastInfo.getName(), forecastInfo.getSys().getCountry()));
        }

    }

}

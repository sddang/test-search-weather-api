package shane.testing.dto.response.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import shane.testing.dto.Response;
import shane.testing.dto.response.search.forecast.ForecastInfo;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse implements Response {

    @JsonProperty("message")
    @SerializedName("message")
    private String message;

    @JsonProperty("cod")
    @SerializedName("cod")
    private String cod;

    @JsonProperty("count")
    @SerializedName("count")
    private String count;

    @JsonProperty("list")
    @SerializedName("list")
    private List<ForecastInfo> forecastItems;

}

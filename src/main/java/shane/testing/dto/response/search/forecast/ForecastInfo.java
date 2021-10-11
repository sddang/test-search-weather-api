package shane.testing.dto.response.search.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import shane.testing.dto.Response;
import shane.testing.dto.response.search.forecast.sub.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastInfo implements Response {

    @JsonProperty("id")
    @SerializedName("id")
    private String id;

    @JsonProperty("name")
    @SerializedName("name")
    private String name;

    @JsonProperty("coord")
    @SerializedName("coord")
    private Coordinates coordinates;

    @JsonProperty("main")
    @SerializedName("main")
    private Main main;

    @JsonProperty("dt")
    @SerializedName("dt")
    private String dt;

    @JsonProperty("wind")
    @SerializedName("wind")
    private Wind wind;

    @JsonProperty("sys")
    @SerializedName("sys")
    private Sys sys;

    @JsonProperty("snow")
    @SerializedName("snow")
    private String snow;

    @JsonProperty("clouds")
    @SerializedName("clouds")
    private Cloud clouds;

    @JsonProperty("weather")
    @SerializedName("weather")
    private List<Weather> weather;

}

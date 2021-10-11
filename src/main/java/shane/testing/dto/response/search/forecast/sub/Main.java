package shane.testing.dto.response.search.forecast.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import shane.testing.dto.Response;


@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Main implements Response {

    @JsonProperty("temp")
    @SerializedName("count")
    private String temperature;

    @JsonProperty("feels_like")
    @SerializedName("feels_like")
    private String feelLike;

    @JsonProperty("temp_min")
    @SerializedName("temp_min")
    private String temperatureMin;

    @JsonProperty("temp_max")
    @SerializedName("temp_max")
    private String temperatureMax;

    @JsonProperty("pressure")
    @SerializedName("pressure")
    private String pressure;

    @JsonProperty("humidity")
    @SerializedName("humidity")
    private String humidity;

}

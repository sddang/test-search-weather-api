package shane.testing.core.base;

import com.google.inject.Inject;
import shane.testing.services.SearchWeatherServices;

abstract class ServiceInjection {

    @Inject
    protected SearchWeatherServices searchWeatherServices;

}

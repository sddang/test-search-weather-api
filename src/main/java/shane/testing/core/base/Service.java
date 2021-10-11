package shane.testing.core.base;

public interface Service<R, T, A> {
    A dispatchServiceRequest(R serviceSpecification, T serviceType);

    String getServiceUrl();
}

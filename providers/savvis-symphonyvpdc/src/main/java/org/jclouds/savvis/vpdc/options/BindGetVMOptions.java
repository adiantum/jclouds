package org.jclouds.savvis.vpdc.options;

import static com.google.common.base.Preconditions.checkArgument;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;

import org.jclouds.http.HttpRequest;
import org.jclouds.rest.Binder;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class BindGetVMOptions implements Binder {
   private final Provider<UriBuilder> uriBuilder;

   @Inject
   public BindGetVMOptions(Provider<UriBuilder> uriBuilder) {
      this.uriBuilder = uriBuilder;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      checkArgument(input instanceof GetVMOptions[], "this binder is only valid for GetVAppOptions!");
      GetVMOptions[] options = GetVMOptions[].class.cast(input);
      if (options.length > 0 && options[0].isWithPowerState())
         return (R) request.toBuilder().endpoint(
                  uriBuilder.get().uri(request.getEndpoint()).path("withpowerstate").build()).build();
      else
         return request;
   }
}

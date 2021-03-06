/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.openstack.nova;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jclouds.openstack.nova.binders.BindBackupScheduleToJsonPayload;
import org.jclouds.openstack.nova.domain.Addresses;
import org.jclouds.openstack.nova.domain.BackupSchedule;
import org.jclouds.openstack.nova.domain.Flavor;
import org.jclouds.openstack.nova.domain.Image;
import org.jclouds.openstack.nova.domain.RebootType;
import org.jclouds.openstack.nova.domain.Server;
import org.jclouds.openstack.nova.domain.SharedIpGroup;
import org.jclouds.openstack.nova.options.CreateServerOptions;
import org.jclouds.openstack.nova.options.CreateSharedIpGroupOptions;
import org.jclouds.openstack.nova.options.ListOptions;
import org.jclouds.openstack.nova.options.RebuildServerOptions;
import org.jclouds.http.functions.ReturnFalseOn404;
import org.jclouds.openstack.filters.AddTimestampQuery;
import org.jclouds.openstack.filters.AuthenticateRequest;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.Endpoint;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.MapBinder;
import org.jclouds.rest.annotations.Payload;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.QueryParams;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.SkipEncoding;
import org.jclouds.rest.annotations.Unwrap;
import org.jclouds.rest.functions.ReturnEmptySetOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnFalseOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnVoidOnNotFoundOr404;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to OpenStack Nova via their REST API.
 * <p/>
 * All commands return a ListenableFuture of the result from OpenStack Nova. Any exceptions incurred
 * during processing will be wrapped in an {@link ExecutionException} as documented in
 * {@link ListenableFuture#get()}.
 * 
 * @see NovaClient
 * @see <a href="http://wiki.openstack.org/OpenStackAPI_1-1" />
 * @author Adrian Cole
 */
@SkipEncoding({ '/', '=' })
@RequestFilters({ AuthenticateRequest.class, AddTimestampQuery.class })
@Endpoint(ServerManagement.class)
public interface NovaAsyncClient {

   /**
    * @see NovaClient#listServers
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/servers")
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<? extends Set<Server>> listServers(ListOptions... options);

   /**
    * @see NovaClient#getServer
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   @Path("/servers/{id}")
   ListenableFuture<Server> getServer(@PathParam("id") int id);

   /**
    * @see NovaClient#deleteServer
    */
   @DELETE
   @ExceptionParser(ReturnFalseOnNotFoundOr404.class)
   @Path("/servers/{id}")
   ListenableFuture<Boolean> deleteServer(@PathParam("id") int id);

   /**
    * @see NovaClient#rebootServer
    */
   @POST
   @QueryParams(keys = "format", values = "json")
   @Path("/servers/{id}/action")
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("%7B\"reboot\":%7B\"type\":\"{type}\"%7D%7D")
   ListenableFuture<Void> rebootServer(@PathParam("id") int id, @PayloadParam("type") RebootType rebootType);

   /**
    * @see NovaClient#resizeServer
    */
   @POST
   @QueryParams(keys = "format", values = "json")
   @Path("/servers/{id}/action")
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("%7B\"resize\":%7B\"flavorId\":{flavorId}%7D%7D")
   ListenableFuture<Void> resizeServer(@PathParam("id") int id, @PayloadParam("flavorId") int flavorId);

   /**
    * @see NovaClient#confirmResizeServer
    */
   @POST
   @QueryParams(keys = "format", values = "json")
   @Path("/servers/{id}/action")
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("{\"confirmResize\":null}")
   ListenableFuture<Void> confirmResizeServer(@PathParam("id") int id);

   /**
    * @see NovaClient#revertResizeServer
    */
   @POST
   @QueryParams(keys = "format", values = "json")
   @Path("/servers/{id}/action")
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("{\"revertResize\":null}")
   ListenableFuture<Void> revertResizeServer(@PathParam("id") int id);

   /**
    * @see NovaClient#createServer
    */
   @POST
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/servers")
   @MapBinder(CreateServerOptions.class)
   ListenableFuture<Server> createServer(@PayloadParam("name") String name, @PayloadParam("imageId") int imageId,
         @PayloadParam("flavorId") int flavorId, CreateServerOptions... options);

   /**
    * @see NovaClient#rebuildServer
    */
   @POST
   @QueryParams(keys = "format", values = "json")
   @Path("/servers/{id}/action")
   @MapBinder(RebuildServerOptions.class)
   ListenableFuture<Void> rebuildServer(@PathParam("id") int id, RebuildServerOptions... options);

   /**
    * @see NovaClient#shareIp
    */
   @PUT
   @Path("/servers/{id}/ips/public/{address}")
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("%7B\"shareIp\":%7B\"sharedIpGroupId\":{sharedIpGroupId},\"configureServer\":{configureServer}%7D%7D")
   ListenableFuture<Void> shareIp(@PathParam("address") String addressToShare,
         @PathParam("id") int serverToTosignBindressTo, @PayloadParam("sharedIpGroupId") int sharedIpGroup,
         @PayloadParam("configureServer") boolean configureServer);

   /**
    * @see NovaClient#unshareIp
    */
   @DELETE
   @Path("/servers/{id}/ips/public/{address}")
   @ExceptionParser(ReturnVoidOnNotFoundOr404.class)
   ListenableFuture<Void> unshareIp(@PathParam("address") String addressToShare,
         @PathParam("id") int serverToTosignBindressTo);

   /**
    * @see NovaClient#changeAdminPass
    */
   @PUT
   @Path("/servers/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("%7B\"server\":%7B\"adminPass\":\"{adminPass}\"%7D%7D")
   ListenableFuture<Void> changeAdminPass(@PathParam("id") int id, @PayloadParam("adminPass") String adminPass);

   /**
    * @see NovaClient#renameServer
    */
   @PUT
   @Path("/servers/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("%7B\"server\":%7B\"name\":\"{name}\"%7D%7D")
   ListenableFuture<Void> renameServer(@PathParam("id") int id, @PayloadParam("name") String newName);

   /**
    * @see NovaClient#listFlavors
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/flavors")
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<? extends Set<Flavor>> listFlavors(ListOptions... options);

   /**
    * @see NovaClient#getFlavor
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/flavors/{id}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<Flavor> getFlavor(@PathParam("id") int id);

   /**
    * @see NovaClient#listImages
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/images")
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<? extends Set<Image>> listImages(ListOptions... options);

   /**
    * @see NovaClient#getImage
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   @QueryParams(keys = "format", values = "json")
   @Path("/images/{id}")
   ListenableFuture<Image> getImage(@PathParam("id") int id);

   /**
    * @see NovaClient#deleteImage
    */
   @DELETE
   @ExceptionParser(ReturnFalseOnNotFoundOr404.class)
   @Path("/images/{id}")
   ListenableFuture<Boolean> deleteImage(@PathParam("id") int id);

   /**
    * @see NovaClient#createImageFromServer
    */
   @POST
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/images")
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("%7B\"image\":%7B\"serverId\":{serverId},\"name\":\"{name}\"%7D%7D")
   ListenableFuture<Image> createImageFromServer(@PayloadParam("name") String imageName,
         @PayloadParam("serverId") int serverId);

   /**
    * @see NovaClient#listSharedIpGroups
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/shared_ip_groups")
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<? extends Set<SharedIpGroup>> listSharedIpGroups(ListOptions... options);

   /**
    * @see NovaClient#getSharedIpGroup
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/shared_ip_groups/{id}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<SharedIpGroup> getSharedIpGroup(@PathParam("id") int id);

   /**
    * @see NovaClient#createSharedIpGroup
    */
   @POST
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/shared_ip_groups")
   @MapBinder(CreateSharedIpGroupOptions.class)
   ListenableFuture<SharedIpGroup> createSharedIpGroup(@PayloadParam("name") String name,
         CreateSharedIpGroupOptions... options);

   /**
    * @see NovaClient#deleteSharedIpGroup
    */
   @DELETE
   @ExceptionParser(ReturnFalseOnNotFoundOr404.class)
   @Path("/shared_ip_groups/{id}")
   ListenableFuture<Boolean> deleteSharedIpGroup(@PathParam("id") int id);

   /**
    * @see NovaClient#listBackupSchedule
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/servers/{id}/backup_schedule")
   ListenableFuture<BackupSchedule> getBackupSchedule(@PathParam("id") int serverId);

   /**
    * @see NovaClient#deleteBackupSchedule
    */
   @DELETE
   @ExceptionParser(ReturnFalseOnNotFoundOr404.class)
   @Path("/servers/{id}/backup_schedule")
   ListenableFuture<Boolean> deleteBackupSchedule(@PathParam("id") int serverId);

   /**
    * @see NovaClient#replaceBackupSchedule
    */
   @POST
   @ExceptionParser(ReturnFalseOn404.class)
   @Path("/servers/{id}/backup_schedule")
   ListenableFuture<Void> replaceBackupSchedule(@PathParam("id") int id,
         @BinderParam(BindBackupScheduleToJsonPayload.class) BackupSchedule backupSchedule);

   /**
    * @see NovaClient#listAddresses
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/servers/{id}/ips")
   ListenableFuture<Addresses> getAddresses(@PathParam("id") int serverId);

   /**
    * @see NovaClient#listPublicAddresses
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/servers/{id}/ips/public")
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<? extends Set<String>> listPublicAddresses(@PathParam("id") int serverId);

   /**
    * @see NovaClient#listPrivateAddresses
    */
   @GET
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "format", values = "json")
   @Path("/servers/{id}/ips/private")
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<? extends Set<String>> listPrivateAddresses(@PathParam("id") int serverId);

}

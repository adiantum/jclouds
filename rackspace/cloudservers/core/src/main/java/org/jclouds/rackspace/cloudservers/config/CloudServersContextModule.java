/**
 *
 * Copyright (C) 2009 Global Cloud Specialists, Inc. <info@globalcloudspecialists.com>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
package org.jclouds.rackspace.cloudservers.config;

import java.net.URI;

import javax.inject.Named;

import org.jclouds.cloud.internal.CloudContextImpl;
import org.jclouds.http.RequiresHttp;
import org.jclouds.lifecycle.Closer;
import org.jclouds.rackspace.CloudServers;
import org.jclouds.rackspace.cloudservers.CloudServersConnection;
import org.jclouds.rackspace.cloudservers.CloudServersContext;
import org.jclouds.rackspace.reference.RackspaceConstants;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

@RequiresHttp
public class CloudServersContextModule extends AbstractModule {
   @Override
   protected void configure() {
      bind(CloudServersContext.class).to(CloudServersContextImpl.class).in(Scopes.SINGLETON);
   }

   public static class CloudServersContextImpl extends CloudContextImpl<CloudServersConnection>
            implements CloudServersContext {

      public CloudServersContextImpl(Closer closer, CloudServersConnection defaultApi,
               @CloudServers URI endPoint,
               @Named(RackspaceConstants.PROPERTY_RACKSPACE_USER) String account) {
         super(closer, defaultApi, endPoint, account);
      }
   }

}
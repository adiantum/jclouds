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

package org.jclouds.openstack.swift.domain;

import java.net.URI;
import java.util.Date;
import java.util.Map;

import org.jclouds.openstack.swift.domain.internal.MutableObjectInfoWithMetadataImpl;

import com.google.inject.ImplementedBy;

/**
 * 
 * @author Adrian Cole
 * 
 */
@ImplementedBy(MutableObjectInfoWithMetadataImpl.class)
public interface MutableObjectInfoWithMetadata extends ObjectInfo {

   void setName(String name);

   void setHash(byte[] hash);

   void setBytes(Long bytes);

   void setLastModified(Date lastModified);

   void setContentType(String contentType);

   void setContainer(String container);

   void setUri(URI uri);

   Map<String, String> getMetadata();

}

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

package org.jclouds.mezeo.pcs.domain.internal;

import java.net.URI;
import java.util.Date;

import org.jclouds.blobstore.domain.StorageType;
import org.jclouds.mezeo.pcs.domain.FileInfo;

/**
 * 
 * @author Adrian Cole
 * 
 */
public class FileInfoImpl extends ResourceInfoImpl implements FileInfo {

   private final Boolean isPublic;
   private final String mimeType;
   private final URI content;
   private final URI permissions;
   private final URI thumbnail;

   public FileInfoImpl(URI url, String name, Date created, boolean inProject,
 Date modified,
            String owner, int version, boolean shared, Date accessed,
            boolean isPublic, String mimeType, long bytes, URI content, URI parent,
            URI permissions, URI tags, URI metadata, URI thumbnail) {
      super(StorageType.BLOB, url, name, created, inProject, modified, owner, version, shared,
               accessed, bytes, tags, metadata, parent);
      this.isPublic = isPublic;
      this.mimeType = mimeType;
      this.content=content;
      this.permissions = permissions;
      this.thumbnail = thumbnail;
   }

   public Boolean isPublic() {
      return isPublic;
   }

   public String getMimeType() {
      return mimeType;
   }

   public URI getContent() {
      return content;
   }

   public URI getPermissions() {
      return permissions;
   }

   public URI getThumbnail() {
      return thumbnail;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((content == null) ? 0 : content.hashCode());
      result = prime * result + (isPublic ? 1231 : 1237);
      result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
      result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
      result = prime * result + ((thumbnail == null) ? 0 : thumbnail.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      FileInfoImpl other = (FileInfoImpl) obj;
      if (content == null) {
         if (other.content != null)
            return false;
      } else if (!content.equals(other.content))
         return false;
      if (isPublic != other.isPublic)
         return false;
      if (mimeType == null) {
         if (other.mimeType != null)
            return false;
      } else if (!mimeType.equals(other.mimeType))
         return false;
      if (permissions == null) {
         if (other.permissions != null)
            return false;
      } else if (!permissions.equals(other.permissions))
         return false;
      if (thumbnail == null) {
         if (other.thumbnail != null)
            return false;
      } else if (!thumbnail.equals(other.thumbnail))
         return false;
      return true;
   }

}

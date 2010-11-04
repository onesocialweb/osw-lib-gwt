/*
 *  Copyright 2010 Vodafone Group Services Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *    
 */
package org.onesocialweb.gwt.service.imp;

import org.onesocialweb.gwt.service.UploadStatus;

public class GwtUploadStatus implements UploadStatus {

	private String fileId;

	private String mimeType;

	private String requestId;

	private long size;

	private long bytesRead;

	private String status;

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setBytesRead(long bytesRead) {
		this.bytesRead = bytesRead;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getFileId() {
		return fileId;
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public String getRequestId() {
		return requestId;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public long getBytesRead() {
		return bytesRead;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public boolean hasFileId() {
		return fileId != null;
	}

	@Override
	public boolean hasStatus() {
		return status != null;
	}

	@Override
	public boolean hasMimeType() {
		return mimeType != null;
	}

	@Override
	public boolean hasRequestId() {
		return requestId != null;
	}

	@Override
	public boolean hasSize() {
		return size != 0;
	}

	@Override
	public boolean hasBytesRead() {
		return bytesRead != 0;
	}
}

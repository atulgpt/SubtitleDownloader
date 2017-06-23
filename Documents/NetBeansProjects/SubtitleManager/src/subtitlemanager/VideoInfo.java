/*
 * Copyright (C) 2017 atulgpt <atlgpt@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package subtitlemanager;

/**
 *
 * @author atulgpt <atlgpt@gmail.com>
 */
public class VideoInfo {

    public VideoInfo(String fullFilePath) {
        this.fullFilePath = fullFilePath;
    }

    private String fullFilePath;
    private String MD5hash;
    private String checksumHash;
    private long fileByteLength;
    private boolean downloaded = false;
    private String subtitle = "";

    public String getFullFilePath() {
        return fullFilePath;
    }

    public String getMD5hash() {
        return MD5hash;
    }

    public void setMD5hash(String MD5hash) {
        this.MD5hash = MD5hash;
    }

    public String getChecksumHash() {
        return checksumHash;
    }

    public void setChecksumHash(String checksumHash) {
        this.checksumHash = checksumHash;
    }

    public long getFileByteLength() {
        return fileByteLength;
    }

    public void setFileByteLength(long fileByteLength) {
        this.fileByteLength = fileByteLength;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

}

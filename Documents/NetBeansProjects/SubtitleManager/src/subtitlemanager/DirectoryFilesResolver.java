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

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author atulgpt <atlgpt@gmail.com>
 */
public class DirectoryFilesResolver {

    File[] fileList;
    String[] fullFileNameList;
    static final String REGEX_FOR_VID_FORMATS = "(.*)\\.(avi|AVI|mkv|MKV|mp4|MP4|mov|MOV|mpg|MPG|wmv|WMV|flv|FLV|mpeg|MPEG|m4v|M4V|3gp|3GP)$";
    static final String REGEX_FOR_SUB_FORMATS = "(.*)\\.(srt|sub|sbv)$";
    ArrayList<SubFileObj> subFileObjs = new ArrayList<>();
    ArrayList<VideoFileObj> videoFileObjs = new ArrayList<>();

    public DirectoryFilesResolver(String[] fullFileNameList) {
        this.fullFileNameList = fullFileNameList;
        fileList = new File[fullFileNameList.length];
        int i = 0;
        for (String fileName : fullFileNameList) {
            fileList[i] = new File(fileName);
            i++;
        }
    }

    public DirectoryFilesResolver(File[] fileList) {
        this.fileList = fileList;
    }

    private class VideoFileObj {

        String filePath;
        boolean isSubExists;
        String subFilePath;
    }

    private class SubFileObj {

        String filePath;
        boolean isVideoExist = false;
        String videoFilePath;
    }

    public String[] calculateAllVideoList() {
        if (videoFileObjs.isEmpty()) {
            operateOnFilesAndSubDirectories(this.fileList);
        }
        ArrayList<String> allVideoFileList = new ArrayList<>();
        this.videoFileObjs.forEach((videoFileObj) -> {
            allVideoFileList.add(videoFileObj.filePath);
        });
        return (String[]) allVideoFileList.toArray(new String[allVideoFileList.size()]);
    }

    public String[] calculateUnsubbedVideoList() {
        if (videoFileObjs.isEmpty()) {
            operateOnFilesAndSubDirectories(this.fileList);
        }
        ArrayList<String> allVideoFileList = new ArrayList<>();
        this.videoFileObjs.stream().filter((videoFileObj) -> (!videoFileObj.isSubExists)).forEachOrdered((videoFileObj) -> {
            allVideoFileList.add(videoFileObj.filePath);
        });
        return (String[]) allVideoFileList.toArray(new String[allVideoFileList.size()]);
    }

    private void operateOnFilesAndSubDirectories(File[] fileList) {
        for (File file : fileList) {
            if (file.isFile()) {
                String fileName = file.getAbsolutePath();
                if (fileName.matches(REGEX_FOR_SUB_FORMATS)) {
                    SubFileObj subFileObj = new SubFileObj();
                    subFileObj.filePath = file.getAbsolutePath();
                    this.subFileObjs.add(subFileObj);
                }
            }
        }
        for (File file : fileList) {
            if (file.isFile()) {
                String fileName = file.getAbsolutePath();
                if (fileName.matches(REGEX_FOR_VID_FORMATS)) {
                    VideoFileObj videoFileObj = new VideoFileObj();
                    videoFileObj.filePath = file.getAbsolutePath();
                    videoFileObj.isSubExists = isSubAvailableOrNot(fileName);
                    this.videoFileObjs.add(videoFileObj);
                }
            }
        }
        for (File file : fileList) {
            if (file.isDirectory()) {
                operateOnFilesAndSubDirectories(file.listFiles());
            }
        }
    }

    private String removeExtension(String mSavePath) {
        if (mSavePath.lastIndexOf(System.getProperty("file.separator")) < mSavePath.lastIndexOf(".")) {
            return mSavePath.substring(0, mSavePath.lastIndexOf("."));
        } else {
            return mSavePath;
        }
    }

    private boolean isSubAvailableOrNot(String videoFileName) {
        String nameForSearch = removeExtension(videoFileName);
        boolean answer = false;
        for (SubFileObj subFileObj : subFileObjs) {
            if (subFileObj.filePath.contains(nameForSearch)) {
                subFileObj.isVideoExist = true;
                subFileObj.videoFilePath = videoFileName;
                answer = true;
            }
        }
        return answer;
    }
}

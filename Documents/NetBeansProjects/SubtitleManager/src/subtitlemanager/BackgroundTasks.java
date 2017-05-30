/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitlemanager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import static subtitlemanager.SubtitleManager.HASH_ALGO;

/**
 *
 * @author atulgpt <atlgpt@gmail.com>
 */
public class BackgroundTasks {

    public class UploadSubtitles extends SwingWorker<Object, Object> {

        String[] filePathArray;
        SubtitleDownloaderUI subtitleDownloaderUI;
        String langs;

        public UploadSubtitles(String[] mFilePathArray, String lang, SubtitleDownloaderUI subtitleDownloaderUI) {
            this.filePathArray = mFilePathArray;
            this.subtitleDownloaderUI = subtitleDownloaderUI;
            this.langs = lang;
        }

        @Override
        protected Object doInBackground() throws Exception {
            ArrayList<String> videoHashArray;
            VideoHashCalc videoHashCalc = new VideoHashCalc(subtitleDownloaderUI);
            videoHashArray = videoHashCalc.getHash(filePathArray, HASH_ALGO);
            ArrayList<String> subtitleArray;
            System.out.println("Hash array: " + videoHashArray.toString());
            if (videoHashArray.size() > 0) {
                HTTPRequest httpRequest = new HTTPRequest(subtitleDownloaderUI);
                subtitleArray = httpRequest.sendDownloadRequests(videoHashArray, langs);
            } else {
                return null;
            }
            for (int i = 0; i < subtitleArray.size(); i++) {
                String mSavePath = filePathArray[i];
                mSavePath = removeExtension(mSavePath);
                if (!subtitleArray.get(i).equals("")) {
                    try {
                        if (UserPreferences.isEmbedLangInfo()) {
                            String lang = langs.split(",")[0];
                            try (BufferedWriter out = new BufferedWriter(new FileWriter(mSavePath + "." + lang + ".srt"))) {
                                out.write(subtitleArray.get(i));
                            }
                        } else {
                            try (BufferedWriter out = new BufferedWriter(new FileWriter(mSavePath + ".srt"))) {
                                out.write(subtitleArray.get(i));
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Exception: "+e);
                    }
                } else {
                    System.out.println("Subtitle found Null for file " + i);
                }
            }
            return null;
        }

        @Override
        protected void done() {
            super.done();
        }

        @Override
        protected void process(List<Object> chunks) {
            super.process(chunks);
        }

        private String removeExtension(String mSavePath) {
            if (mSavePath.lastIndexOf(System.getProperty("file.separator")) < mSavePath.lastIndexOf(".")) {
                return mSavePath.substring(0, mSavePath.lastIndexOf("."));
            } else {
                return mSavePath;
            }
        }
    }
}

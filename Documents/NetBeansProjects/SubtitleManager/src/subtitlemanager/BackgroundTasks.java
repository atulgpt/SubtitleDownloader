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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import static subtitlemanager.SubtitleManager.HASH_ALGO;

/**
 *
 * @author atulgpt <atlgpt@gmail.com>
 */
public class BackgroundTasks {

    public class DownloadSubtitles extends SwingWorker<Object, Object> {

        String[] filePathArray;
        SubtitleDownloaderUI subtitleDownloaderUI;
        String langs;

        public DownloadSubtitles(String[] filePathArray, String lang, SubtitleDownloaderUI subtitleDownloaderUI) {
            this.filePathArray = filePathArray;
            this.subtitleDownloaderUI = subtitleDownloaderUI;
            this.langs = lang;
        }

        @Override
        protected Object doInBackground() throws Exception {
            SwingUtilities.invokeLater(() -> {
                if (subtitleDownloaderUI != null) {
                    subtitleDownloaderUI.setProgressBar(1);
                }
            });
            ArrayList<String> videoHashArray;
            VideoHashCalc videoHashCalc = new VideoHashCalc(subtitleDownloaderUI);
            videoHashArray = videoHashCalc.getMD5Hash(filePathArray, HASH_ALGO);
            ArrayList<String> subtitleArray;
            System.out.println("Hash array: " + videoHashArray.toString());
            if (videoHashArray.size() > 0) {
                HTTPRequest httpRequest = new HTTPRequest(subtitleDownloaderUI);
                subtitleArray = httpRequest.sendDownloadRequests(videoHashArray, langs);
            } else {
                SwingUtilities.invokeLater(() -> {
                    if (subtitleDownloaderUI != null) {
                        subtitleDownloaderUI.setProgressBar(0);
                    }
                });
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
                        System.out.println("Exception: " + e);
                    }
                } else {
                    System.out.println("Subtitle found Null for file " + i);
                }
            }
            SwingUtilities.invokeLater(() -> {
                if (subtitleDownloaderUI != null) {
                    subtitleDownloaderUI.setProgressBar(0);
                }
            });
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

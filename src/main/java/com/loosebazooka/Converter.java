/*
 *  Copyright (c) 2016 Google Inc. All Right Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.loosebazooka;

import com.google.appengine.tools.admin.Application;
import com.google.apphosting.utils.config.CronXmlReader;
import com.google.apphosting.utils.config.DispatchXmlReader;
import com.google.apphosting.utils.config.DosXmlReader;
import com.google.apphosting.utils.config.IndexesXmlReader;
import com.google.apphosting.utils.config.QueueXmlReader;
import com.google.apphosting.utils.config.XmlUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Converter {

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      throw new IllegalArgumentException("missing argument <webapp dir>");
    }
    File appDir = new File(args[0]);
    if (!new File(appDir, "WEB-INF").exists()) {
      throw new IllegalArgumentException("WEB-INF dir not found in webapp dir");
    }

    CronXmlReader cronXmlReader = new CronXmlReader(appDir.getAbsolutePath());
    if (new File(cronXmlReader.getFilename()).exists()) {
      validateXml(cronXmlReader.getFilename(), "docs/cron.xsd");
      writeYaml(cronXmlReader.readCronXml().toYaml(), "cron.yaml");
    }

    QueueXmlReader queueXmlReader = new QueueXmlReader(appDir.getAbsolutePath());
    if (new File(queueXmlReader.getFilename()).exists()) {
      validateXml(queueXmlReader.getFilename(), "docs/queue.xsd");
      writeYaml(queueXmlReader.readQueueXml().toYaml(), "queue.yaml");
    }

    DispatchXmlReader dispatchXmlReader = new DispatchXmlReader(appDir.getAbsolutePath(), DispatchXmlReader.DEFAULT_RELATIVE_FILENAME);
    if (new File(dispatchXmlReader.getFilename()).exists()) {
      validateXml(dispatchXmlReader.getFilename(), "docs/dispatch.xsd");
      writeYaml(dispatchXmlReader.readDispatchXml().toYaml(), "dispatch.yaml");
    }

    DosXmlReader dosXmlReader = new DosXmlReader(appDir.getAbsolutePath());
    if (new File(dosXmlReader.getFilename()).exists()) {
      validateXml(dosXmlReader.getFilename(), "docs/dos.xsd");
      writeYaml(dosXmlReader.readDosXml().toYaml(), "dos.yaml");
    }

    IndexesXmlReader indexesXmlReader = new IndexesXmlReader(appDir.getAbsolutePath());
    if (new File(indexesXmlReader.getFilename()).exists()) {
      validateXml(indexesXmlReader.getFilename(), "docs/datastore-indexes.xsd");
      writeYaml(indexesXmlReader.readIndexesXml().toYaml(), "datastore-indexes.yaml");
    }
  }

  public static void validateXml(String filename, String xsd) {
    // this looks on the classpath for classes from appengine-tools-api.jar and locates the
    // appengine sdk root from there. So... only link that jar (in the sdk directory),
    // not some other jar you've downloaded
    File sdkRoot = new File(Application.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getParentFile();
    XmlUtils.validateXml(filename, new File(sdkRoot, xsd));
  }

  public static void writeYaml(String contents, String destination) throws FileNotFoundException {
    File root = new File(System.getProperty("yaml.out"));
    if (!root.exists()) {
      root.mkdir();
    }
    File outFile = new File(root, destination);
    if (outFile.exists()) {
      outFile.delete();
    }
    try (PrintWriter out = new PrintWriter(new File(root,destination))) {
      out.println(contents);
    }
  }

}

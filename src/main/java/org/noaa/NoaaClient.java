/*
 * ________________________________________________________________________
 * METRO.IO CONFIDENTIAL
 * ________________________________________________________________________
 *
 * Copyright (c) 2017.
 * Metro Labs Incorporated
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of Metro Labs Incorporated and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to Metro Labs Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Metro Labs Incorporated.
 */

package org.noaa;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.*;

public class NoaaClient {
    private static Logger LOG = LoggerFactory.getLogger(NoaaClient.class);
    public static final String BASE_URL = "https://www.ncei.noaa.gov/data/global-hourly/access/";
    public static final String DATA_SOURCE = "NOAA_ISD_LITE";

    public static  Collection<IntegratedSurfaceData> getIntegratedSurfaceData(int year, String station) {
        Map<String, IntegratedSurfaceData> collection = new LinkedHashMap<>();
        try (final Reader reader = new InputStreamReader(downloadNoaaData(year, station))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                IntegratedSurfaceData i = new IntegratedSurfaceData(record);

                String key = String.format("%d%d%d%d",
                        i.getObservationYear(),i.getObservationMonth(),
                        i.getObservationDay(), i.getObservationHour());

                if(!collection.containsKey(key)) collection.put(key, i);
            }
        }
        catch (Exception e) {
            LOG.error("Exception processing {} data", DATA_SOURCE);
            LOG.error(e.getMessage(), e);
        }

        return collection.values();
    }


    private static InputStream downloadNoaaData(int year, String station) throws IOException {
        String fileName = station.replace("-", "") + ".csv";
        String fileUrl = String.format("%s/%d/%s", BASE_URL, year, fileName);
        LOG.debug("Getting: {}", fileUrl);
        URL url = new URL(fileUrl);
        return url.openConnection().getInputStream();
    }
}

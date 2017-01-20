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

import com.google.common.io.CharStreams;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

public class NoaaClient {
    private static Logger LOG = Logger.getLogger(NoaaClient.class.getSimpleName());
    public static final String BASE_URL = "ftp://ftp.ncdc.noaa.gov/pub/data/noaa";
    public static final String DATA_SOURCE = "NOAA_ISD_LITE";

    /**
     * Download gzipped file, unpack it and return the data as a string
     *
     * @param year inventory year
     * @param station the station id
     * @return Delimited string
     */
    public static String downloadIntegratedSurfaceData(int year, String station) {
        String fileName = String.format("%s-%d.gz", station, year);
        String path = "isd-lite";
        String fileUrl = String.format("%s/%s/%d/%s", BASE_URL, path, year, fileName);

        String data = null;
        URLConnection connection;
        try {
            URL url = new URL(fileUrl);
            connection = url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0");

            try (final Reader reader = new InputStreamReader(new GZIPInputStream(connection.getInputStream()))) {
                data = CharStreams.toString(reader);
            }
            catch (Exception e) {
                LOG.log(Level.WARNING, e.getMessage(), e.getCause());
            }
            finally {
                connection.getInputStream().close();
            }
        }
        catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e.getCause());
        }

        return data;
    }

    /**
     * Downloads and marshal data into a collection of {@link IntegratedSurfaceData}
     *
     * @param year inventory year
     * @param station the station id
     * @return collection of {@link List<IntegratedSurfaceData>}
     */
    public static List<IntegratedSurfaceData> getIntegratedSurfaceData(int year, String station) {
        return getIntegratedSurfaceData(downloadIntegratedSurfaceData(year, station));
    }

    /**
     * Marshal data into a collection of {@link IntegratedSurfaceData}
     *
     * @param isdData Delimited string
     * @return {@link List<IntegratedSurfaceData>}
     */
    public static List<IntegratedSurfaceData> getIntegratedSurfaceData(String isdData) {
        String[] lines = isdData.split(System.getProperty("line.separator"));
        List<String> tokens;
        StringTokenizer tokenizer;
        List<IntegratedSurfaceData> integratedSurfaceDataList = new ArrayList<>();
        for (String line : lines) {
            tokenizer = new StringTokenizer(line, " ");
            tokens = new ArrayList<>();
            while (tokenizer.hasMoreElements()) {
                tokens.add((String)tokenizer.nextElement());
            }
            integratedSurfaceDataList.add(new IntegratedSurfaceData(tokens));
        }

        return integratedSurfaceDataList;
    }
}

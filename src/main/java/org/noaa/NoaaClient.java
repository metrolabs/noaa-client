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

import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

public class NoaaClient {
    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(NoaaClient.class);
    public static final String BASE_URL = "ftp://ftp.ncdc.noaa.gov/pub/data/noaa";
    public static final String ISD_HISTORY = "isd-history.csv";
    public static final String DATA_SOURCE = "NOAA_ISD_LITE";

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    private static final int HEADER_USAF    = 0;
    private static final int HEADER_WBAN    = 1;
    private static final int HEADER_STATION = 2;
    private static final int HEADER_CTRY    = 3;
    private static final int HEADER_STATE   = 4;
    private static final int HEADER_ICAO    = 5;
    private static final int HEADER_LAT     = 6;
    private static final int HEADER_LON     = 7;
    private static final int HEADER_ELEV    = 8;
    private static final int HEADER_BEGIN   = 9;
    private static final int HEADER_END     = 10;

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
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(fileUrl);
            connection = url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0");

            try (final Reader reader = new InputStreamReader(new GZIPInputStream(connection.getInputStream()))) {
                int c;
                while ((c = reader.read()) != -1) {
                    builder.append((char) c);
                }
            }
            catch (Exception e) {
                LOG.warn(e.getMessage(), e.getCause());
            }
            finally {
                connection.getInputStream().close();
            }
        }
        catch (Exception e) {
            LOG.warn(e.getMessage(), e.getCause());
        }

        return builder.toString();
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

    private static InputStream openNoaaStationHistory() {
        String fileUrl = String.format("%s/%s", BASE_URL, ISD_HISTORY);
        URLConnection connection;
        try {
            URL url = new URL(fileUrl);
            connection = url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0");
            return connection.getInputStream();
        }
        catch (Exception e) {
            LOG.warn(e.getMessage(), e.getCause());
        }

        return null;
    }

    public static String downloadNoaaStationHistory() {
        StringBuilder builder = new StringBuilder();
        try(final Reader reader = new InputStreamReader(openNoaaStationHistory())) {
            int c;
            while ((c = reader.read()) != -1) {
                builder.append((char) c);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e.getCause());
        }

        return builder.toString();
    }

    public static List<NoaaStationHistory> getNoaaStationHistory() {
        LOG.info("Downloading {}", ISD_HISTORY);
        List<NoaaStationHistory> noaaStationHistories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(openNoaaStationHistory()))) {
            LOG.info("Downloading Completed");
            List<String> items;
            LocalDate startDate, endDate, curDate = LocalDate.now(Clock.systemUTC());
            String line;
            while(null != (line = reader.readLine())) {
                items = parseLine(line);
                if (items.get(HEADER_USAF).equals("USAF")) continue;
                if (items.get(HEADER_CTRY) == null || items.get(HEADER_CTRY).isEmpty()) continue;
                if (items.get(HEADER_LAT) == null || items.get(HEADER_LAT).isEmpty()) continue;
                if (items.get(HEADER_LON) == null || items.get(HEADER_LON).isEmpty()) continue;

                startDate = LocalDate.parse(items.get(HEADER_BEGIN), DateTimeFormatter.BASIC_ISO_DATE);
                endDate   = LocalDate.parse(items.get(HEADER_END), DateTimeFormatter.BASIC_ISO_DATE);

                if (endDate.getYear() < (curDate.getYear()-3)) continue;

                noaaStationHistories.add(new NoaaStationHistory()
                        .setId(items.get(HEADER_USAF) + "-" + items.get(HEADER_WBAN))
                        .setUsaf(items.get(HEADER_USAF))
                        .setWban(items.get(HEADER_WBAN))
                        .setStationName(items.get(HEADER_STATION))
                        .setCountry(items.get(HEADER_CTRY))
                        .setRegion(items.get(HEADER_STATE))
                        .setLatitude(Double.parseDouble(items.get(HEADER_LAT)))
                        .setLongitude(Double.parseDouble(items.get(HEADER_LON)))
                        .setInventoryStart(startDate)
                        .setInventoryEnd(endDate)
                        .setInventoryStartYear(startDate.getYear())
                        .setInventoryEndYear(endDate.getYear())
                        .setInventoryYears((int)startDate.until(endDate, ChronoUnit.YEARS)));

            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e.getCause());
        }

        return noaaStationHistories;
    }

    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }


}

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

import org.junit.jupiter.api.Test;
import org.noaa.NoaaClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NoaaClientTest {

    //@Test
    public void downloadAndUnzip() {
        String stationId = "720259-63844";
        int year = 2017;

        assertNotNull(NoaaClient.getIntegratedSurfaceData(year, stationId));
    }

    @Test
    public void getNoaaStationHistory() {
        assertFalse(NoaaClient.getNoaaStationHistory().isEmpty());
    }

    //@Test
    public void downloadNoaaStationHistory() {
        assertNotNull(NoaaClient.downloadNoaaStationHistory());
    }
}

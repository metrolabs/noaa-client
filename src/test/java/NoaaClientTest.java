/*
 * ________________________________________________________________________
 * METRO.IO CONFIDENTIAL
 * ________________________________________________________________________
 *
 * Copyright (c) 2018.
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

import org.junit.Assert;
import org.junit.Test;
import org.noaa.IntegratedSurfaceData;
import org.noaa.NoaaClient;

import java.util.Collection;

public class NoaaClientTest {
    @Test
    public void downloadIntegratedSurfaceData() {
        int year = 2018;
        String station = "720545-00169";

        Collection<IntegratedSurfaceData> collection = NoaaClient.getIntegratedSurfaceData(year, station);
        IntegratedSurfaceData i = collection.iterator().next();
        Assert.assertTrue(i.getObservationYear() == 2018);
        Assert.assertTrue(i.getObservationMonth() == 1);
        Assert.assertTrue(i.getObservationDay() == 1);
        Assert.assertTrue(i.getObservationHour() == 0);
        Assert.assertTrue(i.getAirTemperature() == -15.0);
        Assert.assertTrue(i.getDewPointTemperature() == -24.0);
        Assert.assertTrue(i.getWindDirection() == 310.0);
        Assert.assertTrue(i.getWindSpeedRate() == 3.6);
        Assert.assertTrue(i.getSkyCondition() == 16093.0);
    }
}

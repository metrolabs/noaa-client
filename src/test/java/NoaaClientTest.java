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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NoaaClientTest {
    private Logger LOG = LoggerFactory.getLogger(NoaaClientTest.class);

    @Test
    public void downloadIntegratedSurfaceData() {
        int year = 2018;
        String station = "720545-00169";
        String data = NoaaClient.downloadIntegratedSurfaceData(year, station);
        Assert.assertNotNull(data);

        List<IntegratedSurfaceData> dataList = NoaaClient.getIntegratedSurfaceData(data);
        LOG.info(dataList.get(1).toString());
    }
}

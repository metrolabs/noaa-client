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

import java.math.BigDecimal;
import java.time.LocalDate;

public class NoaaStationHistory {
    private String id;
    private String usaf;
    private String wban;
    private String stationName;
    private String country;
    private String region;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDate inventoryStart;
    private LocalDate inventoryEnd;
    private int inventoryYears;
    private int inventoryStartYear;
    private int inventoryEndYear;
    
    public String getId() {
        return id;
    }

    public NoaaStationHistory setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsaf() {
        return usaf;
    }

    public NoaaStationHistory setUsaf(String usaf) {
        this.usaf = usaf;
        return this;
    }

    public String getWban() {
        return wban;
    }

    public NoaaStationHistory setWban(String wban) {
        this.wban = wban;
        return this;
    }

    public String getStationName() {
        return stationName;
    }

    public NoaaStationHistory setStationName(String stationName) {
        this.stationName = stationName;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public NoaaStationHistory setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public NoaaStationHistory setRegion(String region) {
        this.region = region;
        return this;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public NoaaStationHistory setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public NoaaStationHistory setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
    }

    public LocalDate getInventoryStart() {
        return inventoryStart;
    }

    public NoaaStationHistory setInventoryStart(LocalDate inventoryStart) {
        this.inventoryStart = inventoryStart;
        return this;
    }

    public LocalDate getInventoryEnd() {
        return inventoryEnd;
    }

    public NoaaStationHistory setInventoryEnd(LocalDate inventoryEnd) {
        this.inventoryEnd = inventoryEnd;
        return this;
    }

    public int getInventoryYears() {
        return inventoryYears;
    }

    public NoaaStationHistory setInventoryYears(int inventoryYears) {
        this.inventoryYears = inventoryYears;
        return this;
    }

    public int getInventoryStartYear() {
        return inventoryStartYear;
    }

    public NoaaStationHistory setInventoryStartYear(int inventoryStartYear) {
        this.inventoryStartYear = inventoryStartYear;
        return this;
    }

    public int getInventoryEndYear() {
        return inventoryEndYear;
    }

    public NoaaStationHistory setInventoryEndYear(int inventoryEndYear) {
        this.inventoryEndYear = inventoryEndYear;
        return this;
    }
}

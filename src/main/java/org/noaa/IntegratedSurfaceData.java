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

import org.apache.commons.csv.CSVRecord;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IntegratedSurfaceData {
    /**
     * Field 1: Pos 1-4, Length 4: Observation Year
     */
    private int observationYear;
    /**
     * Field 2: Pos 6-7, Length 2: Observation Month
     */
    private int observationMonth;
    /**
     * Field 3: Pos 9-11, Length 2: Observation Day, rounded to nearest whole hour
     */
    private int observationDay;
    /**
     * Field 4: Pos 12-13, Length 2: Observation Hour, rounded to nearest whole hour
     */
    private int observationHour;
    /**
     * Field 5: Pos 14-19, Length 6:  Air Temperature
     * The temperature of the air
     *
     * UNITS:  Degrees Celsius
     * SCALING FACTOR: 10
     * MISSING VALUE: -9999
     */
    private Double airTemperature;
    /**
     * Field 6: Pos 20-24, Length 6: Dew Point Temperature
     * The temperature to which a given parcel of air must be cooled at constant pressure and water
     * vapor content in order for saturation to occur.
     *
     * UNITS: Degrees Celsius
     * SCALING FACTOR: 10
     * MISSING VALUE: -9999
     */
    private Double dewPointTemperature;
    /**
     * Field 7: Pos 26-31, Length 6: Sea Level Pressure
     * The air pressure relative to Mean Sea Level (MSL).
     * UNITS:  Hectopascals
     * SCALING FACTOR: 10
     * MISSING VALUE: -9999
     */
    private Double seaLevelPressure;
    /**
     * Field 8: Pos 32-37, Length 6: Wind Direction
     * The angle, measured in a clockwise direction,
     * between true north and the direction from which the wind is blowing.
     *
     * UNITS: Angular Degrees
     * SCALING FACTOR: 1
     * MISSING VALUE: -9999
     * NOTE:  Wind direction for calm winds is coded as 0.
     */
    private Double windDirection;
    /**
     * Field 9: Pos 38-43, Length 6: Wind Speed Rate
     * The rate of horizontal travel of air past a fixed point.
     *
     * UNITS: meters per second
     * SCALING FACTOR: 10
     * MISSING VALUE: -9999
     */
    private Double windSpeedRate;
    /**
     * Field 10: Pos 44-49, Length 6: Sky Condition Total Coverage Code
     * The code that denotes the fraction of the total celestial dome covered by clouds or other obscuring phenomena.
     * MISSING VALUE: -9999
     * ${link: SkyCondition}
     */
    private Double skyCondition;
    /**
     * Field 11: Pos 50-55, Length 6: Liquid Precipitation Depth Dimension - One Hour Duration
     * The depth of liquid precipitation that is measured over a one hour accumulation period.
     *
     * UNITS: millimeters
     * SCALING FACTOR: 10
     * MISSING VALUE: -9999
     * NOTE:  Trace precipitation is coded as -1
     */
    private Double precipitationDepthOneHour;
    /**
     * 
     */
    private Double precipitationDepthSixHour;

    public IntegratedSurfaceData(CSVRecord c) {
        LocalDateTime date = DataHelper.toDate(c.get("DATE"));
        observationYear = date.getYear();
        observationMonth = date.getMonthValue();
        observationDay = date.getDayOfMonth();
        observationHour = date.getHour();
        /*
         * TMP: AIR-TEMPERATURE-OBSERVATION
         * AIR TEMPERATURE
         *  MIN: -0932 MAX: +0618 UNITS: Degrees Celsius
         *  SCALING FACTOR: 10
         *  INDEX: 0
         *
         * air temp.   quality code
         * ---------- -------------
         * -0021      1
         */
        String[] TMP = c.get("TMP").split(",");
        airTemperature = DataHelper.toDouble(TMP[0], 10d);
        /*
         * DEW: DEW POINT AIR-TEMPERATURE-OBSERVATION
         * AIR TEMPERATURE
         *  MIN: -0932 MAX: +0618 UNITS: Degrees Celsius
         *  SCALING FACTOR: 10
         *  INDEX: 0
         *
         * dew point  quality code
         * ---------- -------------
         * -0021      1
         */
        String[] DEW = c.get("DEW").split(",");
        dewPointTemperature = DataHelper.toDouble(DEW[0], 10d);
        /*
         * SLP: ATMOSPHERIC-PRESSURE-OBSERVATION
         * The air pressure relative to Mean Sea Level (MSL).
         *
         * SEA LEVEL PRESSURE
         *  MIN: 08600 MAX: 10900 UNITS: Hectopascals
         *  SCALING FACTOR: 10
         *  INDEX: 0
         *
         * dew point  quality code
         * ---------- -------------
         * 10070      1
         */
        String[] SLP = c.get("SLP").split(",");
        seaLevelPressure = DataHelper.toDouble(SLP[0], 10d);
        /*
         * WND: WIND-OBSERVATION
         *
         * DIRECTION ANGLE
         *  MIN: 001 MAX: 360 UNITS: Angular Degrees
         *  SCALING FACTOR: 1
         *  INDEX: 0
         *
         * SPEED RATE
         *  MIN: 0000 MAX: 0900 UNITS: meters per second
         *  SCALING FACTOR: 10
         *  INDEX: 3
         *
         * direction angle  direction quality code  type code        speed rate  speed quality code
         * ---------------- ----------------------- ---------------- ----------- -------------------
         * 145              1                       N                0086        1
         */
        String[] WND = c.get("WND").split(",");
        windDirection = DataHelper.toDouble(WND[0]);
        windSpeedRate = DataHelper.toDouble(WND[3], 10d);
        /*
         * VIS: SKY-CONDITION-OBSERVATION
         *
         * CEILING HEIGHT DIMENSION
         *  MIN: 00000 MAX: 22000 UNITS: Meters
         *  SCALING FACTOR: 1
         *  INDEX: 0
         *
         */
        String[] VIS = c.get("VIS").split(",");
        skyCondition = DataHelper.toDouble(VIS[0]);
        precipitationDepthOneHour = 0d;
        precipitationDepthSixHour = 0d;
    }

    public int getObservationYear() {
        return observationYear;
    }

    public IntegratedSurfaceData setObservationYear(int observationYear) {
        this.observationYear = observationYear;
        return this;
    }

    public int getObservationMonth() {
        return observationMonth;
    }

    public IntegratedSurfaceData setObservationMonth(int observationMonth) {
        this.observationMonth = observationMonth;
        return this;
    }

    public int getObservationDay() {
        return observationDay;
    }

    public IntegratedSurfaceData setObservationDay(int observationDay) {
        this.observationDay = observationDay;
        return this;
    }

    public int getObservationHour() {
        return observationHour;
    }

    public IntegratedSurfaceData setObservationHour(int observationHour) {
        this.observationHour = observationHour;
        return this;
    }

    public Double getAirTemperature() {
        return airTemperature;
    }

    public IntegratedSurfaceData setAirTemperature(Double airTemperature) {
        this.airTemperature = airTemperature;
        return this;
    }

    public Double getDewPointTemperature() {
        return dewPointTemperature;
    }

    public IntegratedSurfaceData setDewPointTemperature(Double dewPointTemperature) {
        this.dewPointTemperature = dewPointTemperature;
        return this;
    }

    public Double getSeaLevelPressure() {
        return seaLevelPressure;
    }

    public IntegratedSurfaceData setSeaLevelPressure(Double seaLevelPressure) {
        this.seaLevelPressure = seaLevelPressure;
        return this;
    }

    public Double getWindDirection() {
        return windDirection;
    }

    public IntegratedSurfaceData setWindDirection(Double windDirection) {
        this.windDirection = windDirection;
        return this;
    }

    public Double getWindSpeedRate() {
        return windSpeedRate;
    }

    public IntegratedSurfaceData setWindSpeedRate(Double windSpeedRate) {
        this.windSpeedRate = windSpeedRate;
        return this;
    }

    public Double getSkyCondition() {
        return skyCondition;
    }

    public IntegratedSurfaceData setSkyCondition(Double skyCondition) {
        this.skyCondition = skyCondition;
        return this;
    }

    public Double getPrecipitationDepthOneHour() {
        return precipitationDepthOneHour;
    }

    public IntegratedSurfaceData setPrecipitationDepthOneHour(Double precipitationDepthOneHour) {
        this.precipitationDepthOneHour = precipitationDepthOneHour;
        return this;
    }

    public Double getPrecipitationDepthSixHour() {
        return precipitationDepthSixHour;
    }

    public IntegratedSurfaceData setPrecipitationDepthSixHour(Double precipitationDepthSixHour) {
        this.precipitationDepthSixHour = precipitationDepthSixHour;
        return this;
    }

    public enum SkyCondition {
        NONE                (0, "None, SKC or CLR"),
        ONE_OKTA            (1, "1/10 or less but not zero"),
        TWO_OKTAS           (2, "2/10 - 3/10, or FEW"),
        THREE_OKTAS         (3, "4/10"),
        FOUR_OKTAS          (4, "5/10, or SCT"),
        FIVE_OKTAS          (5, "6/10"),
        SIX_OKTAS           (6, "7/10 - 8/10"),
        SEVEN_OKTAS         (7, "9/10 or more but not 10/10, or BKN"),
        EIGHT_OKTAS         (8, "10/10, or OVC"),
        SKY_OBSCURED        (9, "Sky obscured, or cloud amount cannot be estimated"),
        PARTIAL_OBSCURATION (10, "Partial obscuration"),
        THIN_SCATTERED      (11, "Thin scattered"),
        SCATTERED           (12, "Scattered"),
        DARK_SCATTERED      (13, "Dark scattered"),
        THIN_BROKEN         (14, "Thin broken"),
        BROKEN              (15, "Broken"),
        DARK_BROKEN         (16, "Dark broken"),
        THIN_OVERCAST       (17, "Thin overcast"),
        OVERCAST            (18, "Overcast"),
        DARK_OVERCAST       (19, "Dark overcast"),
        MISSING_VALUE       (-9999, "No data available");

        int code;
        String description;
        private static final Map<Integer, SkyCondition> lookupKeys = new HashMap<>();
        static {
            for (SkyCondition p : SkyCondition.values()){
                lookupKeys.put(p.getCode(), p);
            }
        }

        SkyCondition(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static SkyCondition code(int key) {
            return lookupKeys.get(key);
        }
    }

    public static class DataHelper {

        public static Double toDouble(String value) {
            Double x = -9999.0;
            Double d = Double.valueOf(value);
            if(d.compareTo(x) == 0) return null;

            return d;
        }

        public static Double toDouble(String value, double scale) {
            Double x = 9999d;
            Double d = Double.valueOf(value);
            if(d.compareTo(x) == 0) return null;

            return d/scale;
        }

        public static LocalDateTime toDate(String value) {
            return LocalDateTime.parse(value);
        }
    }

    @Override
    public String toString() {
        return "IntegratedSurfaceData{" +
                "observationYear=" + observationYear +
                ", observationMonth=" + observationMonth +
                ", observationDay=" + observationDay +
                ", observationHour=" + observationHour +
                ", airTemperature=" + airTemperature +
                ", dewPointTemperature=" + dewPointTemperature +
                ", seaLevelPressure=" + seaLevelPressure +
                ", windDirection=" + windDirection +
                ", windSpeedRate=" + windSpeedRate +
                ", skyCondition=" + skyCondition +
                ", precipitationDepthOneHour=" + precipitationDepthOneHour +
                ", precipitationDepthSixHour=" + precipitationDepthSixHour +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegratedSurfaceData that = (IntegratedSurfaceData) o;
        return observationYear == that.observationYear &&
                observationMonth == that.observationMonth &&
                observationDay == that.observationDay &&
                observationHour == that.observationHour;
    }

    @Override
    public int hashCode() {

        return Objects.hash(observationYear, observationMonth, observationDay, observationHour);
    }
}

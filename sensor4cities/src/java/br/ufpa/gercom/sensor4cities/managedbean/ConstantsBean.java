/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.managedbean;

import br.ufpa.gercom.sensor4cities.wsn.SensorConstants;
import java.io.Serializable;

/**
 *
 * @author leokassio
 */
public class ConstantsBean implements Serializable {
    
    public static final int ID_SUNSPOT = SensorConstants.ID_SUNSPOT;
    public static final int ID_MEMSIC = SensorConstants.ID_MEMSIC;
    public static final int ID_XBEE = SensorConstants.ID_XBEE;
    
    public static final String ICON_SUNSPOT = SensorConstants.ICON_SUNSPOT;
    public static final String ICON_MEMSIC = SensorConstants.ICON_MEMSIC;
    public static final String ICON_XBEE = SensorConstants.ICON_XBEE;
    public static final String ICON_SERVER = SensorConstants.ICON_SERVER;
    
    public static final String SUNSPOT = SensorConstants.SUNSPOT;
    public static final String MEMSIC = SensorConstants.MEMSIC;
    public static final String XBEE = SensorConstants.XBEE;
    public static final String SERVER = SensorConstants.SERVER;
    
    public static final int TEMPERATURE = SensorConstants.TEMPERATURE;
    public static final int HUMIDITY = SensorConstants.HUMIDITY;
    public static final int LUMINANCE = SensorConstants.LUMINANCE;
    public static final int CO2 = SensorConstants.CO2;
    public static final int WATTER_POLLUTION = SensorConstants.WATTER_POLLUTION;
    public static final int NOISE_POLLUTION = SensorConstants.NOISE_POLLUTION;
    
    public static int getID_MEMSIC() {
        return ID_MEMSIC;
    }

    public static int getID_SUNSPOT() {
        return ID_SUNSPOT;
    }

    public static int getID_XBEE() {
        return ID_XBEE;
    }

    public static String getMEMSIC() {
        return MEMSIC;
    }

    public static String getSUNSPOT() {
        return SUNSPOT;
    }

    public static String getXBEE() {
        return XBEE;
    }

    public static String getICON_MEMSIC() {
        return ICON_MEMSIC;
    }

    public static String getICON_SUNSPOT() {
        return ICON_SUNSPOT;
    }

    public static String getICON_XBEE() {
        return ICON_XBEE;
    }

    public static int getCO2() {
        return CO2;
    }

    public static int getLUMINANCE() {
        return LUMINANCE;
    }

    public static int getNOISE_POLLUTION() {
        return NOISE_POLLUTION;
    }

    public static int getTEMPERATURE() {
        return TEMPERATURE;
    }

    public static int getWATTER_POLLUTION() {
        return WATTER_POLLUTION;
    }
    
    public int getCo2() {
        return CO2;
    }

    public int getLuminance() {
        return LUMINANCE;
    }

    public int getNoisePollution() {
        return NOISE_POLLUTION;
    }

    public int getTemperature() {
        return TEMPERATURE;
    }

    public int getWatterPollution() {
        return WATTER_POLLUTION;
    }
    
    public int getHumidity() {
        return HUMIDITY;
    }

}

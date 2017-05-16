package rfidjava;

import com.impinj.octane.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReadTagsPeriodicTrigger {

    static Map<String, List<String>> map = new HashMap<>();
    JSONObject obj;

    public static void main(String[] args) {
 JSONObject obj;
        try {
            String hostname = (SampleProperties.hostname);

            if (hostname == null) {
                throw new Exception("Must specify the '"+ SampleProperties.hostname + "' property");
            }

            ImpinjReader reader = new ImpinjReader();
            System.out.println("Connecting");
            reader.connect(hostname);

            Settings settings = reader.queryDefaultSettings();

            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(new short[]{1, 2,3,4});
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(20.0);
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);

            antennas.getAntenna((short) 2).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 2).setIsMaxTxPower(false);
            antennas.getAntenna((short) 2).setTxPowerinDbm(20.0);
            antennas.getAntenna((short) 2).setRxSensitivityinDbm(-70);

            antennas.getAntenna((short) 3).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 3).setIsMaxTxPower(false);
            antennas.getAntenna((short) 3).setTxPowerinDbm(20.0);
            antennas.getAntenna((short) 3).setRxSensitivityinDbm(-70);
            
            antennas.getAntenna((short) 4).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 4).setIsMaxTxPower(false);
            antennas.getAntenna((short) 4).setTxPowerinDbm(20.0);
            antennas.getAntenna((short) 4).setRxSensitivityinDbm(-70);
            
            ReportConfig report = settings.getReport();
            report.setIncludeAntennaPortNumber(true);
            report.setIncludeLastSeenTime(true);
            report.setIncludeFirstSeenTime(true);
            report.setIncludeSeenCount(true);
            report.setMode(ReportMode.BatchAfterStop);
            
            settings.getAutoStart().setMode(AutoStartMode.Periodic);
            settings.getAutoStart().setPeriodInMs(3000);
            
            settings.getAutoStop().setMode(AutoStopMode.Duration);
            settings.getAutoStop().setDurationInMs(3000);

            reader.setTagReportListener(new TagReportListenerImplementation());

            System.out.println("Applying Settings");
            reader.applySettings(settings);

            System.out.println("Press Enter to exit.");
            Scanner s = new Scanner(System.in);
            s.nextLine();

            reader.stop();
            reader.disconnect();
        } catch (OctaneSdkException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
}

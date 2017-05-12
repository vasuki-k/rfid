package rfidjava;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import java.io.IOException;

import static java.lang.System.in;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import static rfidjava.ReadTagsPeriodicTrigger.map;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class TagReportListenerImplementation implements TagReportListener {

    JSONObject obj = new JSONObject();

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            Iterator i = entry.getValue().iterator();

            String cur_time = i.next().toString();
            String cur_uuid = entry.getKey();
            String cur_ant = i.next().toString();
            String s = cur_ant;
            long last_time = Long.parseLong(cur_time) - System.currentTimeMillis();
            if (last_time < 775000) {
                System.out.println("not in shelf");
                s = "{\"uuid\":\'" + cur_uuid + "\',\"reader\":\'FLR1SHF\',\"antenna\":\'" + cur_ant + "\',\"prevreadtime\":"+ 0 +",\"readtime\":" + Long.parseLong(cur_time) + ",\"status\":\'Exit\'}";
               // System.out.println(s);
                try {

                    obj = new JSONObject(s);
                    System.out.println(obj);
                    try {
                        SendToREST send = new SendToREST(obj);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(TagReportListenerImplementation.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(TagReportListenerImplementation.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(TagReportListenerImplementation.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JSONException ex) {
                        Logger.getLogger(TagReportListenerImplementation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (JSONException ex) {
                    // Logger.getLogger(RFIDLaunch.class.getName()).log(Level.SEVERE, null, ex);
                }


            } else {
                System.out.println("in shelf");
            }
        }
        for (Tag t : tags) {
            List<String> values = new ArrayList<String>();
            values.add(0, Long.toString(t.getLastSeenTime().getLocalDateTime().getTime()));
            values.add(1, new String(Integer.toString(t.getAntennaPortNumber())));
            if (!map.containsKey(t.getEpc().toString())) {
                map.put(t.getEpc().toString(), values);
                String st = "{\"uuid\":\"" + t.getEpc() + "\",\"antenna\":\"" + t.getAntennaPortNumber() + "\",\"readtime\":\"" + t.getFirstSeenTime().getLocalDateTime().getTime() + "\",\"status\":\"Entry\"}";
                try {

                    obj = new JSONObject(st);
                    System.out.println(obj);
                } catch (JSONException ex) {
                    // Logger.getLogger(RFIDLaunch.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                ArrayList<String> v = (ArrayList<String>) map.get(t.getEpc().toString());
                String lastseen = v.get(0);
                String ant = v.get(1);
                if (Integer.parseInt(ant) == (t.getAntennaPortNumber())) {
                    map.remove(t.getEpc().toString());
                    map.put(t.getEpc().toString(), values);
                    // String st = "{\"uuid\":\"" + t.getEpc() + "\",\"lastseen\":\"" + t.getLastSeenTime().getLocalDateTime().getTime() + "\",\"antenna\":\"" + t.getAntennaPortNumber() + "\",\"reader\":\"reader1 \"}";  

                } else {
                    map.remove(t.getEpc().toString());
                    map.put(t.getEpc().toString(), values);
                    String st = "{\"uuid\":\",\"reader\":\"FLR1SHF \"" + t.getEpc() + "\",\"antenna\":\"" + ant + "\",\"prevreadtime\":\"" + t.getLastSeenTime().getLocalDateTime().getTime() + "\",\"status\":\"Exit\"}";
                    String st1 = "{\"uuid\":\"" + t.getEpc() + "\",\"reader\":\"FLR1SHF \"" + ",\"antenna\":\"" + t.getAntennaPortNumber() + ",\"readtime\":\"" + t.getFirstSeenTime().getLocalDateTime().getTime() + "\",\"status\":\"Entry\"}";

                    try {
                        obj = new JSONObject(st);
                        System.out.println(st);
                        System.out.println(st1);
                    } catch (JSONException ex) {
                        //Logger.getLogger(RFIDLaunch.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
            // System.out.println("");
        }
    }
}

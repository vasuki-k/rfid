package rfidjava;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import static java.lang.System.in;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import static rfidjava.ReadTagsPeriodicTrigger.map;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class TagReportListenerImplementation implements TagReportListener {

    JSONObject obj = new JSONObject();

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            Iterator i = entry.getValue().iterator();
            long last_time=Long.parseLong(i.next().toString()) - System.currentTimeMillis();
          if(last_time<775000)
            System.out.println("not in shelf");
          else
              System.out.println("in shelf");
        }
        for (Tag t : tags) {
            List<String> values = new ArrayList<String>();
            values.add(0, Long.toString(t.getLastSeenTime().getLocalDateTime().getTime()));
            values.add(1, new String(Integer.toString(t.getAntennaPortNumber())));
            if (!map.containsKey(t.getEpc().toString())) {
                map.put(t.getEpc().toString(), values);
                String st = "{\"uuid\":\"" + t.getEpc()  + "\",\"antenna\":\"" + t.getAntennaPortNumber() + "\",\"readtime\":\"" + t.getFirstSeenTime().getLocalDateTime().getTime()+ "\",\"status\":\"Entry\"}";
                try {

                    obj = new JSONObject(st);
                    System.out.println(obj);
                } catch (JSONException ex) {
                    // Logger.getLogger(RFIDLaunch.class.getName()).log(Level.SEVERE, null, ex);
                }

//                try {
//                    //SendToREST send = new SendToREST(obj);
//                } catch (MalformedURLException ex) {
//                    Logger.getLogger(TagReportListenerImplementation.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(TagReportListenerImplementation.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (SQLException ex) {
//                    Logger.getLogger(TagReportListenerImplementation.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (JSONException ex) {
//                    Logger.getLogger(TagReportListenerImplementation.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//                done = true;
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
                    String st = "{\"uuid\":\",\"reader\":\"FLR1SHF \"" + t.getEpc() + "\",\"antenna\":\"" + ant+ "\",\"prevreadtime\":\"" + t.getLastSeenTime().getLocalDateTime().getTime()  + "\",\"status\":\"Exit\"}";
                    String st1 = "{\"uuid\":\"" + t.getEpc() + "\",\"reader\":\"FLR1SHF \""  + ",\"antenna\":\"" + t.getAntennaPortNumber()+",\"readtime\":\""+ t.getFirstSeenTime().getLocalDateTime().getTime() + "\",\"status\":\"Entry\"}";

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
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
          if(last_time<754000)
            System.out.println("not in shelf");
          else
              System.out.println("in shelf");
        }
        for (Tag t : tags) {
            // System.out.println(t.getTid());
            // System.out.println(" doppler: " + t.getRfDopplerFrequency());
            //t.getTagSeenCount();
//            System.out.print(" EPC: " + t.getEpc().toString());
//            if (reader.getName() != null) {
//                System.out.print(" Reader_name: " + reader.getName());
//            } else {
//                System.out.print(" Reader_ip: " + reader.getAddress());
//            }
//
//            if (t.isAntennaPortNumberPresent()) {
//                System.out.print(" antenna: " + t.getAntennaPortNumber());
//            }

//            if (t.isFirstSeenTimePresent()) {
//                System.out.print(" first: " + t.getFirstSeenTime().ToString());
//            }
//
//            if (t.isLastSeenTimePresent()) {
//                System.out.print(" last: " + t.getLastSeenTime().ToString());
//            }
//
//            if (t.isSeenCountPresent()) {
            // System.out.print(" count: " + t.getTagSeenCount());
//            }
            //System.out.println("");
            List<String> values = new ArrayList<String>();
            values.add(0, Long.toString(t.getLastSeenTime().getLocalDateTime().getTime()));
            values.add(1, new String(Integer.toString(t.getAntennaPortNumber())));
            if (!map.containsKey(t.getEpc().toString())) {
                map.put(t.getEpc().toString(), values);
                //System.out.println("check:" + t.getEpc() + "  value" + t.getAntennaPortNumber() + "\t" + t.getLastSeenTime().getLocalDateTime().getTime());
                String st = "{\"uuid\":\"" + t.getEpc() + "\",\"firstseen\":\"" + t.getFirstSeenTime().getLocalDateTime().getTime() + "\",\"antenna\":\"" + t.getAntennaPortNumber() + "\",\"status\":\"Entry\"}";
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
                    //System.out.println("*****************\n"+ant+" "+t.getAntennaPortNumber()+"\n************************\n");
                    map.remove(t.getEpc().toString());
                    map.put(t.getEpc().toString(), values);
                    // String st = "{\"uuid\":\"" + t.getEpc() + "\",\"lastseen\":\"" + t.getLastSeenTime().getLocalDateTime().getTime() + "\",\"antenna\":\"" + t.getAntennaPortNumber() + "\",\"reader\":\"reader1 \"}";  

                } else {
                    // System.out.println("*****************\n"+ant+" "+t.getAntennaPortNumber()+"\n************************\n");
                    map.remove(t.getEpc().toString());
                    map.put(t.getEpc().toString(), values);
                    String st = "{\"uuid\":\"" + t.getEpc() + "\",\"lastseen\":\"" + t.getLastSeenTime().getLocalDateTime().getTime() + "\",\"antenna\":\"" + ant + "\",\"reader\":\"reader1 \",\"status\":\"Exit\"}";
                    String st1 = "{\"uuid\":\"" + t.getEpc() + "\",\"firstseen\":\"" + t.getFirstSeenTime().getLocalDateTime().getTime() + "\",\"antenna\":\"" + t.getAntennaPortNumber() + "\",\"reader\":\"reader1 \",\"status\":\"Entry\"}";

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

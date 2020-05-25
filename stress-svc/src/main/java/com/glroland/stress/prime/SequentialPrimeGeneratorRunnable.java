package com.glroland.stress.prime;

import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import com.glroland.stress.ServiceUtils;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class SequentialPrimeGeneratorRunnable implements Runnable {
    private final AtomicBoolean runningFlag = new AtomicBoolean(false);
    private Thread thread = null;
    private StringBuilder results = null;
    private HashMap<String,Long> hosts = null;
    private Throwable error = null;

    public void start()
    {
        thread = new Thread(this);
        thread.start();
    }

    public void stop()
    {
        runningFlag.set(false);
    }

    public String getResults()
    {
        if (results == null)
            throw new RuntimeException("Illegal State - cannot get results from a thread that has never started");

        return results.toString();
    }

    public Map<String,Long> getHosts()
    {
        return hosts;
    }

    public Throwable getError()
    {
        return error;
    }

    public void run()
    {
        runningFlag.set(true);
        try
        {
            error = null;

            final String stressUrlFinal = ServiceUtils.getServiceUrl() + "/prime/next";

            long startPrime = 0;
            results = new StringBuilder();
            hosts = new HashMap<String,Long>();
            results.append(startPrime).append(",");

            while(true)
            {
                if (!runningFlag.get())
                    break;

                RestTemplateBuilder builder = new RestTemplateBuilder();
                RestTemplate template = builder.build();

                UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(stressUrlFinal)
                    .queryParam("startPrime", startPrime);

            // long nextPrime = template.getForObject(urlBuilder.toUriString(), Long.class);


                HttpHeaders headers = new HttpHeaders();
                HttpEntity request = new HttpEntity(headers);
                ResponseEntity<Long> response = template.exchange(urlBuilder.toUriString(), HttpMethod.GET, request, Long.class);
                long nextPrime = response.getBody();

                List<String> hostIpList = response.getHeaders().get(ServiceUtils.HEADER_HOST_IP);
                if ((hostIpList != null) && (hostIpList.size() > 0))
                {
                    if (hostIpList.size() > 1)
                        throw new RuntimeException("Too many host IPs in header.  Not sure how this could happen");
                    
                    String hostIp = hostIpList.get(0);
                    
                    Long count = hosts.get(hostIp);
                    if (count == null)
                        count = (long)1;
                    else 
                        count = count + 1;
                    hosts.put(hostIp, count);
                }

                results.append(nextPrime).append(",");
                startPrime = nextPrime;
            }
        }
        catch(Throwable e)
        {
            error = e;
            System.out.println("An error occurred during processing - " + e.toString());
            throw e;
        }
    }
}

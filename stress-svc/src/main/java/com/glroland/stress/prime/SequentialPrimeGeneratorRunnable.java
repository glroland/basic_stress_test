package com.glroland.stress.prime;

import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import com.glroland.stress.ServiceUtils;
import java.util.concurrent.atomic.AtomicBoolean;

public class SequentialPrimeGeneratorRunnable implements Runnable {
    private final AtomicBoolean runningFlag = new AtomicBoolean(false);
    private Thread thread = null;
    private StringBuilder results = null;

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

    public void run()
    {
        runningFlag.set(true);

        final String stressUrlFinal = ServiceUtils.getServiceUrl() + "/primeNext";

        long startPrime = 0;
        results = new StringBuilder();
        results.append(startPrime).append(",");

        while(true)
        {
            if (!runningFlag.get())
                break;

            RestTemplateBuilder builder = new RestTemplateBuilder();
            RestTemplate template = builder.build();

            UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(stressUrlFinal)
                .queryParam("startPrime", startPrime);

            long nextPrime = template.getForObject(urlBuilder.toUriString(), Long.class);
            results.append(nextPrime).append(",");
            startPrime = nextPrime;
        }
    }
}
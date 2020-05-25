package com.glroland.stress.prime;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import com.glroland.stress.ServiceUtils;

@RestController
@RequestMapping(path = "/prime")
public class PrimeServiceController {

    @CrossOrigin(origins = "*")
    @RequestMapping(path = "/", produces = "text/plain")
    public String prime()
    {
        SequentialPrimeGeneratorRunnable r = new SequentialPrimeGeneratorRunnable();
        r.start();

        StringBuilder results = new StringBuilder();

        try
        {
            Thread.sleep(ServiceUtils.getTimeLimitSeconds() * 1000);
            r.stop();
            Thread.sleep(500);
            if (r.getError() != null)
            {
                return "An error occurred during prime number calculation!  " + r.getError().toString();
            }

            results.append(r.getResults()).append("\n");
        }
        catch (InterruptedException e)
        {
            return e.toString();
        }

        Map<String,Long> hosts = r.getHosts();
        if (hosts != null)
        {
            results.append("\n");
            for (Map.Entry<String,Long> entry : hosts.entrySet())  
            {
                results.append(entry.getKey()).append(" called ").append(entry.getValue()).append(" times.\n");
            }
        }
        results.append("\n");

        return results.toString();
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/next")
    public long primeNext(@RequestParam(value="startPrime") long startPrime)
    {
        if (startPrime == 0)
            return 1;
        else if (startPrime == 1)
            return 2;

        long nextPrime = startPrime;
        boolean factorExists = false;
        do
        {
            nextPrime = nextPrime + 1;
            factorExists = false;
            for(long i=2; i < nextPrime; i++)
            {
                if ((nextPrime % i) == 0)
                {
                    factorExists = true;
                    break;
                }
            }            
        } while(factorExists);
    
        return nextPrime;
    }
}

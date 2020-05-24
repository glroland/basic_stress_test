package com.glroland.stress.prime;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PrimeServiceController {

    @CrossOrigin(origins = "*")
    @RequestMapping("/primeNext")
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

    @CrossOrigin(origins = "*")
    @RequestMapping("/prime")
    public String prime()
    {
        SequentialPrimeGeneratorRunnable r = new SequentialPrimeGeneratorRunnable();
        r.start();

        try
        {
            Thread.sleep(5 * 1000);
            r.stop();

            return r.getResults();
        }
        catch (InterruptedException e)
        {
            return e.toString();
        }
    }
}

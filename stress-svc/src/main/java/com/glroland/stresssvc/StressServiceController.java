package com.glroland.stresssvc;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.http.HttpStatus;
import java.io.PrintWriter;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import java.util.Date;

@RestController
public class StressServiceController {

    @RequestMapping("/")
    public String index() {
        return "Stress Me Out!";
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/fibo")
    public ResponseEntity<StreamingResponseBody> fibo()
    {
        return this.fiboResume("", 0, 1, Long.MAX_VALUE / 2);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/fiboLimit")
    public ResponseEntity<StreamingResponseBody> fiboLimit(@RequestParam(value="limit") long limit)
    {
        return this.fiboResume("", 0, 1, limit);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/fiboResume")
    public ResponseEntity<StreamingResponseBody> fiboResume(@RequestParam(value="pastSequenceNotIncludingN0andN1") String pastSequenceNotIncludingN0andN1, @RequestParam(value="n0") long n0, @RequestParam(value="n1") long n1, @RequestParam(value="limit") long limit)
    {
        System.out.println("Invocation - " + (new Date()).getTime());

//        System.out.println("pastSequenceNotIncludingN0andN1 = " + pastSequenceNotIncludingN0andN1);
//        System.out.println("N0 = " + n0);
//        System.out.println("N1 = " + n1);
//        System.out.println("Limit = " + limit);

        StreamingResponseBody stream = out -> {
            PrintWriter p = new PrintWriter(out);

            long dyn_n0 = n0;
            long dyn_n1 = n1;

            p.print(n0);
            p.print(",");
            p.print(n1);

            while(dyn_n1 <= limit)
            {
                long n1temp = dyn_n0 + dyn_n1;
                dyn_n0 = dyn_n1;
                dyn_n1 = n1temp;
            
                p.print(",");
                p.print(dyn_n1);

                p.flush();
            }

            p.close();
        };
        return new ResponseEntity<StreamingResponseBody>(stream, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/fiboStress")
    public void fiboStress(@RequestParam(value="threads") int threads, @RequestParam(value="loops") int loops)
    {
        String stressUrl = System.getenv("STRESS_URL");
        if ((stressUrl == null) || (stressUrl.length() == 0))
        {
            stressUrl = "http://localhost:8080/fibo";
        }
        final String stressUrlFinal = stressUrl;

        for (int t = 0; t < threads; t++)
        {
            Runnable r = new Runnable() {
                public void run()
                {
                    for (int l = 0; l < loops; l++)
                    {
                        RestTemplateBuilder builder = new RestTemplateBuilder();
                        RestTemplate template = builder.build();
                        String obj = template.getForObject(stressUrlFinal, String.class);
                    }
                }
            };

            Thread thread = new Thread(r);
            thread.start();            
        }
    }
}

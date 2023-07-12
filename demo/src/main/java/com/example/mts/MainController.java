package com.example.mts;

import static com.kount.ris.RisConfigurationConstants.PROPERTY_RIS_CONFIG_KEY;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kount.ris.Inquiry;
import com.kount.ris.KountRisClient;
import com.kount.ris.Response;
import com.kount.ris.util.CartItem;
import com.kount.ris.util.MerchantAcknowledgment;
import com.kount.ris.util.RisException;
import com.kount.ris.util.payment.Payment;

@RestController
public class MainController {

    @GetMapping("/hello-world")
    public String HelloWorld() {
        System.out.println("Call HelloWorld");
        return "Hello World";
    }

    @PostMapping("/start-ris")
    public String startRis() throws MalformedURLException {

        doRisCall();

        return "startRis";
    }

    private void doRisCall() throws MalformedURLException {
        System.out.println("Init call ris");
        URL url = new URL("https://risk.test.kount.net");
        String apiKey = "";

        KountRisClient ris = new KountRisClient(url, apiKey);
 
        Inquiry req = new Inquiry();
        req.setMerchantId(101010);

        /**
         * TODO 
         */
        req.setSessionId("0702ee4f2c874b39befc2f82dd0fc570");


        Payment payment = new Payment("CARD", "4111111111111111");
        req.setCurrency("USD");

        System.out.println("Config key: " + System.getProperty(PROPERTY_RIS_CONFIG_KEY));

        req.setPayment(payment);
        req.setTotal(125);
        req.setCustomerName("Jorge Rodriguez");
        req.setEmail("jmrodriguez@test.com");
        req.setIpAddress("127.0.0.1");
        req.setMerchantAcknowledgment(MerchantAcknowledgment.YES);
        req.setWebsite("DEFAULT");
        CartItem item0 = new CartItem("SURROUND SOUND", "HTP-2920", "Pioneer High Power", 1, 49999);
        CartItem item1 = new CartItem("BLU RAY PLAYER", "BDP-S500", "Sony Blu-Ray Disc", 1, 69999);
        
        Collection<CartItem> cart = new ArrayList<>();
        cart.add(item0);
        cart.add(item1);
        req.setCart(cart);
        try {
            // This is the first point at which the code actually calls the Kount
            // services. Everything prior to this is simply setting up the payload.

            System.out.println("Start call ris.process");
            Response response = ris.process(req);
            String responseText = "Transaction ID: " + response.getTransactionId() + "\n\n";
            responseText += response;
            System.out.println("Response RIS: " + responseText);
        } catch (RisException risException) {
            System.out.println("ERROR: " + risException.getMessage());
        }
    }
}
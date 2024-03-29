package com.stsf.aurora.services;

import com.stsf.aurora.requestmodel.Domain;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

@Service
public class WhitelistService {

    public String addWhitelistService(Domain domain) {

        try {


            //System.out.println("[DEBUG] Whitelist add request received");
            String command = "pihole -w " + domain.getDomain();

            Process p = Runtime.getRuntime().exec(command);


            BufferedReader commandOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = commandOutput.readLine()) != null) {

                if (line.toLowerCase(Locale.ROOT).contains("adding")) {

                    //System.out.println("[DEBUG] Added to whitelist successfully");
                    return"Added to whitelist: " + domain;

                } else if (line.toLowerCase(Locale.ROOT).contains("already exists")) {

                    //System.out.println("[DEBUG] Whitelist already contains domain: " + domain);
                    return "Whitelist already contains domain: " + domain;

                }

            }

            p.waitFor();
            p.destroy();


        } catch (IOException | InterruptedException e) {
            System.out.println("Unable to execute command");
            e.printStackTrace();
            return "Unable to add domain to whitelist";
        }


        return "Unable to add domain to whitelist";

    }

    public String removeWhitelistService(Domain domain) {

        try {


            //System.out.println("[DEBUG] Whitelist remove request received");
            String command = "pihole -w -d " + domain.getDomain();

            Process p = Runtime.getRuntime().exec(command);


            BufferedReader commandOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = commandOutput.readLine()) != null) {

                if (line.toLowerCase(Locale.ROOT).contains("removing")) {

                    //System.out.println("[DEBUG] Removed from whitelist successfully");
                    return "Removed from whitelist";

                } else if (line.toLowerCase(Locale.ROOT).contains("does not exists")) {

                    //System.out.println("[DEBUG] Whitelist doesn't contain domain: " + domain);
                    return "Whitelist doesn't contain domain: " + domain;

                }

            }

            p.waitFor();
            p.destroy();


        } catch (IOException | InterruptedException e) {
            System.out.println("Unable to execute command");
            e.printStackTrace();
            return "Unable to remove from whitelist";
        }

        return "Unable to remove from whitelist";

    }

    public Boolean getIfDomainAlreadyInWhitelistService(Domain domain) {

        String output = addWhitelistService(domain);

        if (output.toLowerCase(Locale.ROOT).contains("already contains")) {

            return true;

        } else if (output.toLowerCase(Locale.ROOT).contains("added")) {

            removeWhitelistService(domain);
            return false;

        } else return false;

    }

}

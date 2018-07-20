package citi.mscard;

import citi.points.ReturnInformation;

import java.sql.Timestamp;

public class BriefCard implements  Comparable<BriefCard>{
    private String merchantID;
    private String merchantLogoURL;
    private String merchantName;
    private int points;
    private double proportion;
    private String logoURL;

    public BriefCard(String merchantID, String merchantLogoURL, String merchantName, int points, double proportion, String logoURL) {
        this.merchantID = merchantID;
        this.merchantLogoURL = merchantLogoURL;
        this.merchantName = merchantName;
        this.points = points;
        this.proportion = proportion;
        this.logoURL = logoURL;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public String getMerchantLogoURL() {
        return merchantLogoURL;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public int getPoints() {
        return points;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public double getProportion() {
        return proportion;
    }

    @Override
    public int compareTo(BriefCard o) {
        if(o.points*o.proportion-this.points*this.proportion>0)
            return 1;
        return -1;
    }
}

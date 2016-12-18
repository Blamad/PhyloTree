package utils;

/**
 * Created by Blamad on 18.12.2016.
 */
public class PhyloTreeException extends Exception {

    private String message;

    public PhyloTreeException(String message)
    {
        this.message = message;
    }

    public String getInfo()
    {
        return message;
    }
}

package ppd.fip;

public class FIPEntry {
    int tokenCode;
    int tsPosition;

    public FIPEntry(int tokenCode, int tsPosition) {
        this.tokenCode = tokenCode;
        this.tsPosition = tsPosition;
    }

    public int getTokenCode() {
        return tokenCode;
    }

    public int getTsPosition() {
        return tsPosition;
    }
}
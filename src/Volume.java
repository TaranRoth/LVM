package src;
import java.util.UUID;

public class Volume {
    private String uuid;
    private int sizeGB;
    private String name;

    public Volume(int sizeGB, String name) {
        this.sizeGB = sizeGB;
        this.name = name;
        UUID u = UUID.randomUUID();
        uuid = u.toString();
    }

    public Volume(int sizeGB, String name, String uuid) {
        this.sizeGB = sizeGB;
        this.name = name;
        this.uuid = uuid;
    }

    public int getSizeGB() {
        return sizeGB;
    }

    public String getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setSizeGB(int newSize) {
        sizeGB = newSize;
    }
}

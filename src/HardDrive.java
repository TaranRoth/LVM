package src;
import java.util.ArrayList;

public class HardDrive {
    private int sizeGB;
    private String name;
    private PhysicalVolume volume;
    private static ArrayList<HardDrive> allDrives = new ArrayList<HardDrive>();

    public HardDrive(int sizeGB, String name) {
        this.sizeGB = sizeGB;
        this.name = name;
        allDrives.add(this);
    }

    public int getSizeGB() {
        return sizeGB;
    }

    public void setVolume(PhysicalVolume p) {
        volume = p;
    }

    public PhysicalVolume getVolume() {
        return volume;
    }

    public String getName() {
        return name;
    }

    public static ArrayList<HardDrive> getAllDrives() {
        return allDrives;
    }
}

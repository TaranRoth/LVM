package src;
import java.util.ArrayList;

public class PhysicalVolume extends Volume {
    private HardDrive drive;
    private VolumeGroup group;
    private static ArrayList<PhysicalVolume> allPhysicalVolumes = new ArrayList<PhysicalVolume>();

    public PhysicalVolume(String name, HardDrive h) {
        super(h.getSizeGB(), name);
        drive = h;
        allPhysicalVolumes.add(this);
    }

    public PhysicalVolume(String name) {
        super(0, name);
        allPhysicalVolumes.add(this);
    }

    public void setDrive(HardDrive h) {
        drive = h;
        setSizeGB(drive.getSizeGB());
    }

    public boolean setVolumeGroup(VolumeGroup g) {
        if (group != null) return false;
        group = g;
        return true;
    }

    public HardDrive getDrive() {
        return drive;
    }

    public static ArrayList<PhysicalVolume> getAllPhysicalVolumes() {
        return allPhysicalVolumes;
    }

    public VolumeGroup getGroup() {
        return group;
    }
}

package src;
import java.util.ArrayList;

public class LogicalVolume extends Volume {
    private VolumeGroup group;
    private static ArrayList<LogicalVolume> allLogicalVolumes = new ArrayList<LogicalVolume>();
    
    public LogicalVolume(int sizeGB, String name, VolumeGroup g) {
        super(sizeGB, name);
        this.group = g;
        allLogicalVolumes.add(this);
    }

    public LogicalVolume(int sizeGB, String name, String uuid) {
        super(sizeGB, name, uuid);
        allLogicalVolumes.add(this);
    }

    public void setVolumeGroup(VolumeGroup g) {
        group = g;
    }

    public VolumeGroup getGroup() {
        return group;
    }

    public static ArrayList<LogicalVolume> getAllLogicalVolumes() {
        return allLogicalVolumes;
    }
    
}

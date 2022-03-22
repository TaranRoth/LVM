package src;
import java.util.ArrayList;

public class VolumeGroup extends Volume {
    ArrayList<PhysicalVolume> physicalVolumes;
    ArrayList<LogicalVolume> logicalVolumes;
    private static ArrayList<VolumeGroup> allGroups = new ArrayList<VolumeGroup>();
    private int remainingSpace;

    public VolumeGroup(String name, PhysicalVolume p) {
        super(p.getSizeGB(), name);
        physicalVolumes = new ArrayList<PhysicalVolume>();
        physicalVolumes.add(p);
        p.setVolumeGroup(this);
        logicalVolumes = new ArrayList<LogicalVolume>();
        remainingSpace = 0;
        allGroups.add(this);
    }

    public VolumeGroup(String name, String uuid) {
        super(0, name, uuid);
        physicalVolumes = new ArrayList<PhysicalVolume>();
        logicalVolumes = new ArrayList<LogicalVolume>();
        remainingSpace = 0;
        allGroups.add(this);
    }

    public void addPhysicalVolume(PhysicalVolume p) {
        physicalVolumes.add(p);
        p.setVolumeGroup(this);
    }

    public void addLogicalVolume(LogicalVolume l) {
        logicalVolumes.add(l);
        l.setVolumeGroup(this);
    }
    
    public static ArrayList<VolumeGroup> getAllGroups() {
        return allGroups;
    }

    public void updateRemainingSpace() {
        int spaceTaken = 0;
        for (LogicalVolume l: logicalVolumes) {
            spaceTaken += l.getSizeGB();
        }
        remainingSpace = getSizeGB() - spaceTaken;
    }

    public void updateSizeGB() {
        int totalSpace = 0;
        for (PhysicalVolume p: physicalVolumes) {
            totalSpace += p.getSizeGB();
        }
        setSizeGB(totalSpace);
    }

    public int getRemainingSpace() {
        updateSizeGB();
        updateRemainingSpace();
        return remainingSpace;
    }

    @Override
    public int getSizeGB() {
        updateSizeGB();
        return super.getSizeGB();
    }

    public ArrayList<PhysicalVolume> getPhysicalVolumes() {
        return physicalVolumes;
    }

    public ArrayList<LogicalVolume> getLogicalVolumes() {
        return logicalVolumes;
    }

}

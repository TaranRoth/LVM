package src;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.parser.ParseException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

public class Controller {

    private static String getWord(String cmd, int wordNum) {
        String newStr = cmd;
        for (int i = 0; i < wordNum - 1; i++) {
            if (newStr.indexOf(" ") == -1) throw new IndexOutOfBoundsException();
            newStr = newStr.substring(newStr.indexOf(" ") + 1);
        }
        if (newStr.indexOf(" ") == -1) return newStr;
        return newStr.substring(0, newStr.indexOf(" "));
    }

    public static boolean hasWord(String cmd, int wordNum) {
        try {
            getWord(cmd, wordNum);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    public String installDrive(String cmd) {
        String name = getWord(cmd, 2);
        for (HardDrive h: HardDrive.getAllDrives()) {
            if (h.getName().equals(name)) return name + " is an already existing hard drive";
        }
        int sizeGB = Integer.parseInt(getWord(cmd, 3).substring(0, getWord(cmd, 3).indexOf("G")));
        HardDrive h = new HardDrive(sizeGB, name);
        return "Drive " + h.getName() + " installed.";
    }

    public String createPhysicalVolume(String cmd) {
        String name = getWord(cmd, 2);
        for (PhysicalVolume p: PhysicalVolume.getAllPhysicalVolumes()) {
            if (p.getName().equals(name)) return name + " is an already existing physical volume";
        }
        String driveName = getWord(cmd, 3);
        HardDrive selectedDrive = null;
        for (HardDrive h: HardDrive.getAllDrives()) {
            if (h.getName().equals(driveName)) selectedDrive = h;
        }
        if (selectedDrive == null) {
            return "No drive found named " + driveName; 
        }
        if (selectedDrive.getVolume() != null) {
            return "Drive " + driveName + " already has an associated physical volume";
        }
        PhysicalVolume p = new PhysicalVolume(name, selectedDrive);
        selectedDrive.setVolume(p);
        return "Physical volume " + p.getName() + " created and associated with drive " + selectedDrive.getName();
    }

    public String getPhysicalVolumes() {
        String listStr = "";
        for (PhysicalVolume p: PhysicalVolume.getAllPhysicalVolumes()) {
            String volString = p.getName() + ": [" + p.getSizeGB() + "G] [";
            if (p.getGroup() == null) {
                volString += p.getUUID() + "]";
            } else {
                volString += p.getGroup().getName() + "] [" + p.getUUID() + "]";
            }
            listStr += volString + "\n";
        }
        if (listStr.length() > 0) return listStr.substring(0, listStr.length() - 1);
        else return "No physical volumes to list";
    }

    public String createVolumeGroup(String cmd) {
        String name = getWord(cmd, 2);
        for (VolumeGroup v: VolumeGroup.getAllGroups()) {
            if (v.getName().equals(name)) return name + " is an already existing volume group";
        }
        String volumeName = getWord(cmd, 3);
        PhysicalVolume selectedVolume = null;
        for (PhysicalVolume p: PhysicalVolume.getAllPhysicalVolumes()) {
            if (p.getName().equals(volumeName)) selectedVolume = p;
        }
        if (selectedVolume == null) {
            return volumeName + " is not an existing physical volume";
        }
        if (selectedVolume.getGroup() != null) {
            return selectedVolume + " is already part of a volume group";
        }
        VolumeGroup v = new VolumeGroup(name, selectedVolume);
        return "Volume group " + v.getName() + " created";
    }

    public String extendVolumeGroup(String cmd) {
        String groupName = getWord(cmd, 2);
        VolumeGroup group = null;
        for (VolumeGroup v: VolumeGroup.getAllGroups()) {
            if (v.getName().equals(groupName)) group = v;
        }
        if (group == null) return groupName + " is not an existing volume group."; 
        String physicalName = getWord(cmd, 3);
        PhysicalVolume physical = null;
        for (PhysicalVolume p: PhysicalVolume.getAllPhysicalVolumes()) {
            if (p.getName().equals(physicalName)) physical = p;
        }
        if (physical == null) return physicalName + " is not an existing physical volume";
        if (physical.getGroup() != null) return physicalName + " is already part of a volume group";
        group.addPhysicalVolume(physical);
        return "Physical volume " + physicalName + " added to volume group " + groupName;
    }

    public String getVolumeGroups() {
        String listStr = "";
        for (VolumeGroup v: VolumeGroup.getAllGroups()) {
            listStr += v.getName() + ": total:[" + v.getSizeGB() + "G] available:[" + v.getRemainingSpace() + "G] [";
            for (PhysicalVolume p: v.getPhysicalVolumes()) {
                listStr += p.getName() + ",";
            }
            listStr = listStr.substring(0, listStr.length() - 1);
            if (v.getLogicalVolumes().size() > 0) {
                listStr += "] [";
                for (LogicalVolume l: v.getLogicalVolumes()) {
                    listStr += l.getName() + ",";
                }
                listStr = listStr.substring(0, listStr.length() - 1);
            }
            listStr += "] [" + v.getUUID() + "]\n";
        }
        if (listStr.length() > 0) return listStr.substring(0, listStr.length() - 1);
        else return "No volume groups to list";
    }

    public String getDrives() {
        String listStr = "";
        for (HardDrive h: HardDrive.getAllDrives()) {
            listStr += h.getName() + " [" + h.getSizeGB() + "G]\n";
        }
        if (listStr.length() > 0) return listStr.substring(0, listStr.length() - 1);
        else return "No drives to list";
    }

    public String createLogicalVolume(String cmd) {
        String name = getWord(cmd, 2);
        for (LogicalVolume l: LogicalVolume.getAllLogicalVolumes()) {
            if (l.getName().equals(name)) return "Logical volume " + name + " already exists";
        }
        String vgName = getWord(cmd, 4);
        VolumeGroup group = null;
        for (VolumeGroup v: VolumeGroup.getAllGroups()) {
            if (v.getName().equals(vgName)) group = v;
        }
        if (group == null) return vgName + " is not an existing volume group";
        String sizeStr = getWord(cmd, 3);
        int size = Integer.parseInt(sizeStr.substring(0, sizeStr.length() - 1));
        if (size > group.getRemainingSpace()) return "There is not enough space in volume group " + vgName + " to add a logical volume of that size";
        LogicalVolume l = new LogicalVolume(size, name, group);
        group.addLogicalVolume(l);
        return "Logical volume " + l.getName() + " created and associated with volume group " + group.getName();
    }

    public String getLogicalVolumes() {
        String listStr = "";
        for (LogicalVolume l: LogicalVolume.getAllLogicalVolumes()) {
            listStr += l.getName() + ": [" + l.getSizeGB() + "G] [" + l.getGroup().getName() + "] [" + l.getUUID() + "]\n";
        }
        if (listStr.length() > 0) return listStr.substring(0, listStr.length() - 1);
        else return "No logical volumes to list";
    }

    public void saveData() {
        JSONObject data = new JSONObject();
        JSONObject hardDrives = new JSONObject();
        data.put("hardDrives", hardDrives);
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Integer> sizes = new ArrayList<Integer>();
        ArrayList<String> connectedVolumes = new ArrayList<String>();
        for (HardDrive h: HardDrive.getAllDrives()) {
            names.add(h.getName());
            sizes.add(h.getSizeGB());
            if (h.getVolume() == null) connectedVolumes.add(null);
            else connectedVolumes.add(h.getVolume().getName());
        }
        hardDrives.put("names", names);
        hardDrives.put("sizes", sizes);
        hardDrives.put("physicalVolumes", connectedVolumes);
        JSONObject physicalVolumes = new JSONObject();
        data.put("physicalVolumes", physicalVolumes);
        names = new ArrayList<String>();
        sizes = new ArrayList<Integer>();
        ArrayList<String> drives = new ArrayList<String>();
        ArrayList<String> groups = new ArrayList<String>();
        ArrayList<String> uuids = new ArrayList<String>();
        for (PhysicalVolume p: PhysicalVolume.getAllPhysicalVolumes()) {
            names.add(p.getName());
            sizes.add(p.getSizeGB());
            drives.add(p.getDrive().getName());
            if (p.getGroup() == null) groups.add(null);
            else groups.add(p.getGroup().getName());
            uuids.add(p.getUUID());
        }
        physicalVolumes.put("names", names);
        physicalVolumes.put("sizes", sizes);
        physicalVolumes.put("drives", drives);
        physicalVolumes.put("groups", groups);
        physicalVolumes.put("uuids", uuids);
        JSONObject volumeGroups = new JSONObject();
        data.put("volumeGroups", volumeGroups);
        names = new ArrayList<String>();
        ArrayList<ArrayList<String>> pVolumes = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> lVolumes = new ArrayList<ArrayList<String>>();
        uuids = new ArrayList<String>();
        for (VolumeGroup v: VolumeGroup.getAllGroups()) {
            names.add(v.getName());
            ArrayList<String> pVols = new ArrayList<String>();
            for (PhysicalVolume p: v.getPhysicalVolumes()) pVols.add(p.getName());
            pVolumes.add(pVols);
            ArrayList<String> lVols = new ArrayList<String>(); 
            for (LogicalVolume l: v.getLogicalVolumes()) lVols.add(l.getName());
            lVolumes.add(lVols);
            uuids.add(v.getUUID());
        }
        volumeGroups.put("names", names);
        volumeGroups.put("physicalVolumes", pVolumes);
        volumeGroups.put("logicalVolumes", lVolumes);
        volumeGroups.put("uuids", uuids);
        JSONObject logicalVolumes = new JSONObject();
        data.put("logicalVolumes", logicalVolumes);
        names = new ArrayList<String>();
        sizes = new ArrayList<Integer>();
        groups = new ArrayList<String>();
        uuids = new ArrayList<String>();
        for (LogicalVolume l: LogicalVolume.getAllLogicalVolumes()) {
            names.add(l.getName());
            sizes.add(l.getSizeGB());
            groups.add(l.getGroup().getName());
            uuids.add(l.getUUID());
        }
        logicalVolumes.put("names", names);
        logicalVolumes.put("sizes", sizes);
        logicalVolumes.put("groups", groups);
        logicalVolumes.put("uuids", uuids);
        try {
            FileWriter file = new FileWriter("data.json");
            file.write(data.toJSONString());
            file.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
    }

    public void loadData() {
        JSONParser parser = new JSONParser();
        try (FileReader r = new FileReader("data.json")) {
            JSONObject data = (JSONObject) (parser.parse(r));
            JSONObject drives = (JSONObject) data.get("hardDrives");
            ArrayList<String> names = (ArrayList<String>) drives.get("names");
            ArrayList<Long> sizes = (ArrayList<Long>) drives.get("sizes");
            for (int i = 0; i < names.size(); i++) {
                new HardDrive(sizes.get(i).intValue(), names.get(i));
            }
            JSONObject physicalVolumes = (JSONObject) data.get("physicalVolumes");
            names = (ArrayList<String>) physicalVolumes.get("names");
            for (int i = 0; i < names.size(); i++) {
                new PhysicalVolume(names.get(i));
            }
            JSONObject volumeGroups = (JSONObject) data.get("volumeGroups");
            names = (ArrayList<String>) volumeGroups.get("names");
            for (int i = 0; i < names.size(); i++) {
                new VolumeGroup(names.get(i));
            }
            JSONObject logicalVolumes = (JSONObject) data.get("logicalVolumes");
            names = (ArrayList<String>) logicalVolumes.get("names");
            sizes = (ArrayList<Long>) drives.get("sizes");
            for (int i = 0; i < names.size(); i++) {
                new LogicalVolume(sizes.get(i).intValue(), names.get(i));
            }
            ArrayList<String> pVolumes = (ArrayList<String>) drives.get("physicalVolumes");
            for (int i = 0; i < pVolumes.size(); i++) {
                if (pVolumes.get(i) != null) {
                    for (PhysicalVolume p: PhysicalVolume.getAllPhysicalVolumes()) {
                        if (p.getName().equals(pVolumes.get(i))) HardDrive.getAllDrives().get(i).setVolume(p);
                    }
                }
            }
            ArrayList<String> hDrives = (ArrayList<String>) physicalVolumes.get("drives");
            for (int i = 0; i < hDrives.size(); i++) {
                for (HardDrive h: HardDrive.getAllDrives()) {
                    if (h.getName().equals(hDrives.get(i))) PhysicalVolume.getAllPhysicalVolumes().get(i).setDrive(h);
                }
            }
            ArrayList<ArrayList<String>> pVolumeList = (ArrayList<ArrayList<String>>) volumeGroups.get("physicalVolumes");
            ArrayList<ArrayList<String>> lVolumeList = (ArrayList<ArrayList<String>>) volumeGroups.get("logicalVolumes");
            for (int i = 0; i < pVolumeList.size(); i++) {
                for (int i2 = 0; i2 < pVolumeList.get(i).size(); i2++) {
                    for (PhysicalVolume p: PhysicalVolume.getAllPhysicalVolumes()) {
                        if (p.getName().equals(pVolumeList.get(i).get(i2))) VolumeGroup.getAllGroups().get(i).addPhysicalVolume(p);
                    }
                    for (LogicalVolume l: LogicalVolume.getAllLogicalVolumes()) {
                        if (l.getName().equals(lVolumeList.get(i).get(i2))) VolumeGroup.getAllGroups().get(i).addLogicalVolume(l);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
     }
}

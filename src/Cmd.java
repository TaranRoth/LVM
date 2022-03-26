package src;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Cmd {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        File dataFolder = new File("data");
        File[] listOfFiles = dataFolder.listFiles();
        int co = 1;
        String fileName = "";
        boolean emptyProfile = true;
        if (listOfFiles.length > 0) {
            System.out.println("Select a profile (enter listed number next to name):");
            for (File f: listOfFiles) {
                String name = f.getName();
                System.out.println(co + ": " + name.substring(0, name.indexOf(".")));
                co++;
            } 
            System.out.print("Or make a new profile (enter 0): ");
            int input = s.nextInt();
            if (input == 0) {
                System.out.print("Name your profile: ");
                String name = s.next();
                File dataFile = new File("data/" + name + ".json");
                try {
                    dataFile.createNewFile();
                    fileName = dataFile.getName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            } else {
                emptyProfile = false;
                fileName = listOfFiles[input - 1].getName();
            }
        }
        Controller c = new Controller(fileName);
        if (!emptyProfile) c.loadData();
        
        String command = "";
        while (!command.equals("exit")) {
            System.out.print("cmd# ");
            command = s.next();
            processCommand(command, c);
        }
        s.close();
    }
    
    private static void processCommand(String cmd, Controller c) {
        String firstWord;
        if (cmd.indexOf(" ") == -1) firstWord = cmd;
        else firstWord = cmd.substring(0, cmd.indexOf(" "));
        String res = "";
        if (firstWord.equals("install-drive") && Controller.hasWord(cmd, 3)) {
            res = c.installDrive(cmd);
        } else if (firstWord.equals("list-drives")) {
            res = c.getDrives();
        } else if (firstWord.equals("pvcreate") && Controller.hasWord(cmd, 3)) {
            res = c.createPhysicalVolume(cmd);
        } else if (firstWord.equals("pvlist")) {
            res = c.getPhysicalVolumes();
        } else if (firstWord.equals("vgcreate") && Controller.hasWord(cmd, 3)) {
            res = c.createVolumeGroup(cmd);
        } else if (firstWord.equals("vgextend") && Controller.hasWord(cmd, 3)) {
            res = c.extendVolumeGroup(cmd);
        } else if (firstWord.equals("vglist")) {
            res = c.getVolumeGroups();
        } else if (firstWord.equals("lvcreate") && Controller.hasWord(cmd, 4)) {
            res = c.createLogicalVolume(cmd);
        } else if (firstWord.equals("lvlist")) {
            res = c.getLogicalVolumes();
        } else if (firstWord.equals("exit")) {
            c.saveData();
            res = "Saving data. Good-bye!"; 
        } else if (firstWord.equals("help")) {
            res += "install-drive [driveName] [size]\n";
            res += "list-drives\n";
            res += "pvcreate [pvName] [driveName]\n";
            res += "pvlist\n";
            res += "vgcreate [vgName] [pvName]\n";
            res += "vgextend [vgName] [pvName]\n";
            res += "vglist\n";
            res += "lvcreate [lvName] [size] [vgName]\n";
            res += "lvlist";
        }    else {
            res = "That is not a recognizable command, please try again. (help to list commands)";
        }
        System.out.println(res);
    }
}
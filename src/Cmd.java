package src;
import java.util.Scanner;

public class Cmd {
    public static void main(String[] args) {
        Controller c = new Controller();
        c.loadData();
        Scanner s = new Scanner(System.in);
        String command = "";
        while (!command.equals("exit")) {
            System.out.print("cmd# ");
            command = s.nextLine();
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
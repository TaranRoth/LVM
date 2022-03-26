Model:

Class Volume:
- String UUID, represents volume's UUID
- int sizeGB, represents the volume's size in GB
- String name, represents user-given name
- Volume(int sizeGB, String name): Initializes sizeGB and name to parameters and intializes UUID to a randomly generated String, this constructor is used when volumes are created through a command
- Volume(int sizeGB, String name, String uuid): Intializes all instance variables according to parameters, this constructor is used when loading volumes created in previous runs of the program
- Getter methods for all instance variables
- Setter method for sizeGB

Class PhysicalVolume (super Volume):
- HardDrive drive, represents the drive associated with this object
- VolumeGroup group, represents the VolumeGroup associated with this object, null if no group
- static ArrayList<PhysicalVolume> allPhysicalVolumes, contains all PhysicalVolume instances currently loaded
- PhysicalVolume(String name, HardDrive h): Constructs this object with a preset name and connected HardDrive, sizeGB is set to the sizeGB of the h parameter. This constructor is only used when a user creates a PhysicalVolume through a command
- PhysicalVolume(String name, String uuid): Constructs this object with a preset name and UUID, sizeGB is temporarily set to 0. This constructor is only used for loading objects created in previous runs of the program
- Getter methods for all instance variables

Class VolumeGroup (super Volume):
- ArrayList<PhysicalVolume> physicalVolumes, contains all PhysicalVolumes associated with this group
- ArrayList<LogicalVolume> logicalVolumes, contains all LogicalVolumes associated with this group
- static ArrayList<VolumeGroup> allGroups, contains all VolumeGroup instances currently loaded
- int remainingSpace, represents how much more space all associated PhysicalVolumes have than all associated logicalVolumes
- VolumeGroup(String name, PhysicalVolume p): Constructs this object with a preset name and associated physicalVolume, this constructor is used when volumes are created through a command
- VolumeGroup(String name, String uuid): Constructs this object with a preset name and UUID, sizeGB is temporarily set to 0. This constructor is only used for loading objects created in previous runs of the program
- void addPhysicalVolume(PhysicalVolume p): Adds the parameter to the list of associated physical volumes 
- void addLogicalVolume(LogicalVolume l): Adds the parameter to the list of associated logical volumes
- void updateRemainingSpace(): Updates remainingSpace to the difference between the space of all associated logical volumes and the space of all associated physcal volumes
- void updateSizeGB(): Updates sizeGB to the sum of all associated physical volumes
- Getter methods for all instance variables, override of getSizeGB() getter method calls updateSizeGB() before returning sizeGB

Class LogicalVolume (super Volume):
- VolumeGroup group, represents the connected VolumeGroup
- static ArrayList<LogicalVolume> allLogicalVolumes, contains all LogicalVolume instances currently loaded
- LogicalVolume(int sizeGB, String name, VolumeGroup group): Constructs this object with a preset name, size, and connected VolumeGroup. This constructor is only used when a user creates a VolumeGroup through a command 
- LogicalVolume(int sizeGB, String name, String uuid): Constructs this object with a preset UUID, name, and sizeGB. This constructor is only used for loading objects created in previous runs of the program
- Setter method for the group instance variable
- Getter methods for both instance variables

Class HardDrive:
- int sizeGB, represents the drive's size in GB
- String name, represents the drive's user-given name
- PhysicalVolume volume, represents the PhysicalVolume associated with this object
- static ArrayList<HardDrive> allDrives, contains all HardDrive instances currently loaded
- public HardDrive(int sizeGB, String name): Initializes sizeGB and name to the parameters passed
- Getter methods for all instance variables
- Setter for the volume instance variable

View:

Runner class Cmd:

- static void main(): Continually asks the user for input untill the user enters "exit" or terminates the program, calls processCommand() on each user input
-String processCommand(String cmd): Parses the cmd parameter and calls the proper Controller method, or reports an error in processing the command

Controller:

Class Controller:
- static String getWord(String cmd, int wordNum): Returns the wordNum parameter word of the cmd parameter
- static boolean hasWord(String cmd, int wordNum): Returns if the cmd parameter has more than wordNum - 1 words
- String installDrive(String cmd): Creates a new HardDrive object based on specifications in the cmd parameter, and returns either a success or failure message
- String createPhysicalVolume(String cmd): Creates a new PhysicalVolume object based on specifications in the cmd parameter, and returns either a success or failure message
- String getPhysicalVolumes(): Returns a formatted string representing all PhysicalVolume instances and thier specifications
- String createVolumeGroup(String cmd): Creates a new VolumeGroup object based on specifications in the cmd parameter, and returns either a success or failure message
- String extendVolumeGroup(String cmd): Adds a PhysicalVolume to 
- String getVolumeGroups(): Returns a formatted string representing all VolumeGroup instances and their specifications
- String getDrives(): Returns a formatted string representing all hardDrive instances and their specifications
- String createLogicalVolume(String cmd): Creates a new LogicalVolume object based on specifications in the cmd parameter, and returns either a success or failure message
- String getLogicalVolumes(): Returns a formatted string representing all LogicalVolume instances and their specifications
- void saveData(): Saves all object instances to data.json
- void loadData(): Loads data from data.json and converts data into object instances


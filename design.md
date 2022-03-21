Model:

Class Volume
- String UUID, represents volume's UUID
- int sizeGB, represents the volume's size in GB
- String name, represents user-given name
- Getter methods for all instance variables
- Constructor that takes in UUID and sizeGB, and generates a unique UUID

Class PhysicalVolume (super Volume)
- HardDrive associatedDrive, represents the drive associated with this
- boolean setHardDrive(HardDrive drive), returns true and sets associatedDrive to drive if there is no

Class VolumeGroup (super Volume)

Class LogicalVolume (super Volume)


View:

Runner class Cmd:


Controller:

Class Controller:
- static String getWord(String cmd, intWordnum): 
- static boolean hasWord(String cmd, int wordNum):
- String installDrive(String cmd):
- String createPhysicalVolume(String cmd):
- String getPhysicalVolumes(): 
- String createVolumeGroup(String cmd):
- String extendVolumeGroup(String cmd):
- String getVolumeGroups(): 
- String getDrives():
- String createLogicalVolumes(String cmd)
- String getLogicalVolumes():
- void saveData(): Saves all objects to data.json
- void loadData(): 


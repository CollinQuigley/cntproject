# This is our CNT 4007 P2P File Sharing Project

## Getting Started
###Basic Info
Note that we have used the IntelliJ IDEA and there are many references
in this that may be IntelliJ specific but can still be replicated by
other means. The project also uses features that are part of [JDK 21](https://www.oracle.com/java/technologies/downloads/)
so you must configure your IDE of choice to do the same.

### Local Configurations
To begin setting up your local repository, begin by cloning the repository
into a clean directory. As per project specifications, the files included
in the GitHub repository are source files only so there is additional
setup needed for everything to work properly. After cloning the repository
you will want to download the [project_config_file_large.zip](https://ufl.instructure.com/courses/498392/files/folder/Project?preview=84231866)
from Canvas. After unzipping this file, move the contents into the **~/src/**
directory. Your file directory should resemble this:

![image of directory](https://media.discordapp.net/attachments/1217482952229912576/1217513227521495082/Screenshot_2024-03-13_123727.png?ex=66044c8d&is=65f1d78d&hm=3f8b99ae033ceb4d96e33bf45f2f6b86bc0bba9413924d6335fcda8028961679&=&format=webp&quality=lossless&width=2002&height=1090)


## Compiling and Running
### Compiling
At this point, assuming that you have properly cloned and setup the prject,
you can compile the code to run. In IntelliJ this can simply be done by
clicking the `Build Project`button (the hammer). This will generate
an **~/out/** directory with all the compiled files. If you are not using
IntelliJ this can be similarly achieved using the **javac** command from
command line or often within your IDE.

### Running
To execute the code on your local machine, you will first need to edit the
config file found at **~/src/project_config_file_large/PeerInfo.cfg**.
Each process is identified by one line in this file  in `[peer ID] [host name] [listening port] [has file or not]`
format. Edit the port numbers to be unique -- most will work above port `6000`.

To initialize and run peers, you will need to open six command
line terminals in **~/out/production/cntproject/**. To start a peer process,
run `java peerProcess [peer ID]`, where `peer ID` corresponds to a `peer ID` in **~/src/project_config_file_large/PeerInfo.cfg**.

### Possible Issues
If you experience errors when attempting to start the first peer, check that your
working directory in IntelliJ is set to **~/src/**. After making this change
you will likely need to recompile. If issues persist and you are not using
IntelliJ, the issues may be machine specific dependent on IDE settings.
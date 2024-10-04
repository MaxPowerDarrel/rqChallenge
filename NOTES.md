This file contains some of my thought processes and decisions and assumptions.

1. Because this is a largely blank project, I am assuming it is a new project.  Normally, in such a case I would use this opportunity to update the Java version (this is at 11, in the initial screening I was told 17 is being used) and Spring boot version (2.x has been EOL).   I initially started down this path, but stopped when I realized I would need to update the gradle wrapper, and with the ci file in the project I did not want to mess with that. I did change the java version to 17
2. While not present in the initial `build.gradle` I did add lombok as a compile time dependency.  It removes some boilerplate code.  Normally I wouldn't do this without a conversation

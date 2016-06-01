# exjobb

Files for my degree project (Bachleor thesis). Still a work in progress.  

In this repository are two applications for practicing words and their grammatical inflections. The folder 'desktop' contains a IntelliJ project with a JavaFX application. The folder 'android-app' contains a Android Studio project. This is a companion app, not usable without the desktop application.  

###Repo structure

    exjobb
    |
    |-----src *(All code)*
    	|
        |-----android_app *(Android studio project)*
                |-----... Regular android studio project structure
    	|-----desktop *(IntelliJ project, java program for regular pc:s)*
    		|
    		|-----test *(Junit tests for the java classes in project)*
    		|-----src  *(Source code and resources for the project)*
    			|
    			|-----main *(The 'M' in MVC, and other utilities)*
    			|-----controllers *(The 'C' in MVC)*
    			|-----grammar *(The underlying module for the MVC model. Think of it as a database manager, but for .gf files)*
    			|-----resources
    				|
    				|-----css
    				|-----images
    				|-----view *(The 'V' in MVC)*
    				|-----xml
    	


####Dependencies
GF http://www.grammaticalframework.org/
JPGF http://www.grammaticalframework.org/
ZBar https://github.com/ZBar/ZBar
ZXing https://github.com/zxing/zxing


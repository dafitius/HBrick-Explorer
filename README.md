# Files.TBLU viewer

Files.TBLU viewer is a javaFX application made to view the Files.TBLU files found in the HITMAN™: World of Assasination series.

## requirements
- Java RE 8
- python


# usage

Run **TBLUViewer.jar** and select a .Files.TBLU file.

The application can be configured inside of the setting.txt.

## controls

Left clicking is all there is to it. When navigating the tree additional information about a selected item will be shown.
If an item contains arrays with other items linked to it those arrays will be shown as pop-up. 
> Popups can be disabled inside the settings.txt

## settings

### Level of detail: <0 ~ 11>
This defines how "deep" into the Treeview you can look. 
> Setting this too high can result in a stackOverflow.

### python PATH var: <py/python/..>
Allows you to change the PATH variable used to run a python script.

### use old jsons: <true/false>
Checks if file to load has already been decoded and uses this data to build the treeview.
Decreases load time due to the application not having to decode the Files.TBLU file.

### default Files.TBLU path: <file path>
sets the filechooser's inital directory to this path.
Which can save some clicks :)

### enable dark-mode: <true/false>
enables dark mode if set to true.


### enable popups: <true/false>
enables the popups that show up if an item with linked arrays is clicked.
![](https://i.imgur.com/R5ZoR9d.gif)

### enable decoder prints: <true/false>
allows the decoder to print debug information


# internal workings

TBLUviewer uses a Python script to decode the Files.TBLU files. After decoding the file the scripts ouput is interpretetdby a JavaFX application that builds an ItemLibrary and displays its items inside a treeView.


internal flow:

![image](https://user-images.githubusercontent.com/70489995/109419230-bf050a80-79cc-11eb-9f65-a9327b04ed6a.png)

> Efficiency?
> More like I-Wish-ency..


## known bugs
- If a file has duplicate names a "loop" inside the treeView can occur, resulting in a crash.


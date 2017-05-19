# Custom Battery Notify
Utility for show state of batteries on devices with double batteries (battery + batteryjsr) (Innos D6000)

Allows you to display a notification containing the percentage and status of the battery, the data about which will be scanned from the files you specified. Files must be specified manually. It also allows you to run a notification when the system starts.

On most Android-smartphones this way
/sys/class/power_supply/battery(Here it can vary)/capacity - Battery percent
/sys/class/power_supply/battery(here may vary)/status - Battery status

Initially, the program was written under the Innos D6000 (device with external and internal batteries (Dock Battery)), on custom firmware, the charge of the external battery was not displayed in the status bar. The application allowed to indicate by hand the path to the second battery and display its status in the status bar.


![FIRST](https://raw.githubusercontent.com/olegsvs/ru.olegsvs.custombatterynotification/fabric/images/2.png)
![SECOND](https://raw.githubusercontent.com/olegsvs/ru.olegsvs.custombatterynotification/fabric/images/3.png)
![THIRD](https://raw.githubusercontent.com/olegsvs/ru.olegsvs.custombatterynotification/fabric/images/1.png)

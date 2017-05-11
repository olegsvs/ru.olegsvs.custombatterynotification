# Custom Battery Notify
Utility for show state of batteries on devices with double batteries (battery + batteryjsr)

Change this in https://github.com/olegsvs/ru.olegsvs.custombatterynotification/blob/master/app/src/main/java/ru/olegsvs/custombatterynotification/BatteryManager.java
```java
    public static final String SYS_BATTERY_CAPACITY = "/sys/class/power_supply/battery/capacity";
    public static final String SYS_BATTERY_STATUS = "/sys/class/power_supply/battery/status";
    public static final String SYS_BATTERY_CAPACITY_JSR = "/sys/class/power_supply/batteryjsr/capacity";
    public static final String SYS_BATTERY_STATUS_JSR = "/sys/class/power_supply/batteryjsr/status";
```

![FIRST](https://raw.githubusercontent.com/olegsvs/ru.olegsvs.custombatterynotification/master/images/1.png)
![SECOND](https://raw.githubusercontent.com/olegsvs/ru.olegsvs.custombatterynotification/master/images/2.png)
![THIRD](https://raw.githubusercontent.com/olegsvs/ru.olegsvs.custombatterynotification/master/images/3.png)
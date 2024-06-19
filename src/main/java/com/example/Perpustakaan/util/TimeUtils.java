package com.example.Perpustakaan.util;

import java.time.Duration;

public class TimeUtils {

    public static String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        if (days > 0) {
            return String.format("%d days, %d hours ago", days, hours);
        } else if (hours > 0) {
            return String.format("%d hours, %d minutes ago", hours, minutes);
        } else if (minutes > 0) {
            return String.format("%d minutes, %d seconds ago", minutes, seconds);
        } else {
            return String.format("%d seconds ago", seconds);
        }
    }
}

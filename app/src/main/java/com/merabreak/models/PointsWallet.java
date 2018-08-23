package com.merabreak.models;

/**
 * Created by ETPL-002 on 08-Nov-17.
 */

public class PointsWallet {

    private String total_points;
    private String promotional_total_points;
    private int spin_flag;
    private int spin_waiting_time;
    private int spin_count;

    public int getNotification_count() {
        return notification_count;
    }

    public void setNotification_count(int notification_count) {
        this.notification_count = notification_count;
    }

    private int notification_count;

    public PointsWallet() {
    }

    public String getTotal_points() {
        return total_points;
    }

    public void setTotal_points(String total_points) {
        this.total_points = total_points;
    }

    public String getPromotional_total_points() {
        return promotional_total_points;
    }

    public void setPromotional_total_points(String promotional_total_points) {
        this.promotional_total_points = promotional_total_points;
    }

    public int getSpin_flag() {
        return spin_flag;
    }

    public void setSpin_flag(int spin_flag) {
        this.spin_flag = spin_flag;
    }

    public int getSpin_waiting_time() {
        return spin_waiting_time;
    }

    public void setSpin_waiting_time(int spin_waiting_time) {
        this.spin_waiting_time = spin_waiting_time;
    }

    public int getSpin_count() {
        return spin_count;
    }

    public void setSpin_count(int spin_count) {
        this.spin_count = spin_count;
    }
}

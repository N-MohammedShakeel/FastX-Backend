package com.example.FastX.dto;

import lombok.Data;

@Data
public class OperatorStatsDTO {
    private int totalBus;
    private int totalRoute;
    private int totalBooking;
    private int totalRefund;
    private double totalAmountRefunded;
    private double totalRevenue;
}

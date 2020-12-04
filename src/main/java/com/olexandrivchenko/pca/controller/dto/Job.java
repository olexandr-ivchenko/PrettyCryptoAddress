package com.olexandrivchenko.pca.controller.dto;

import java.math.BigInteger;

/**
 * Simple DTO to represent job for ThreadExecutor
 */
public class Job {
    private BigInteger startPoint;
    private Long size;

    public Job(BigInteger startPoint, Long size) {
        this.startPoint = startPoint;
        this.size = size;
    }

    public BigInteger getStartPoint() {
        return startPoint;
    }

    public Long getSize() {
        return size;
    }
}

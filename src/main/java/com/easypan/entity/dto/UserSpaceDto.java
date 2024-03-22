package com.easypan.entity.dto;

import java.io.Serializable;

public class UserSpaceDto implements Serializable {
    private long useSpace;
    private long totalSpace;

    public long getUseSpace() {
        return useSpace;
    }

    public void setUseSpace(long useSpace) {
        this.useSpace = useSpace;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }
}

package com.easypan.component;

import com.easypan.entity.dto.SysSettingDto;
import com.easypan.entity.dto.UserSpaceDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisComponent {

    @Resource
    private RedisUtils redisUtils;

    public SysSettingDto getSysSettingDto() {
        SysSettingDto settingDto = (SysSettingDto) redisUtils.get("easypan:syssetting:");
        if (settingDto == null) {
            settingDto = new SysSettingDto();
            redisUtils.set("easypan:syssetting:", settingDto);
        }
        return settingDto;
    }

    public void saveUserSpace(String userId, UserSpaceDto userSpaceDto) {
        redisUtils.setex("easypan:user:userspace:" + userId, userSpaceDto, 60 * 60 * 24l);
    }

    public UserSpaceDto getUserSpace(String userId) {
        return (UserSpaceDto) redisUtils.get("easypan:user:userspace:" + userId);
    }

    public void saveFileTempSize(String userId, String fileId, Long fileSize) {
        Long currentSize = getFileTempSize(userId, fileId);
    }

    public Long getFileTempSize(String userId, String fileId) {
        Long currentSize = getFileSizeFromRedis("easy:user:file:temp:" + userId + fileId);
        return currentSize;
    }

    public Long getFileSizeFromRedis(String key) {
        Object sizeObj = redisUtils.get(key);
        if (sizeObj == null) {
            return 0L;
        }
        if (sizeObj instanceof Integer) {
            return ((Integer) sizeObj).longValue();
        } else if (sizeObj instanceof Long) {
            return (Long) sizeObj;
        }
        return 0L;
    }
}

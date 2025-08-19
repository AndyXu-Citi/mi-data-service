package com.marathon.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfo {
    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
    private String openId;
    private String tokenId;
}

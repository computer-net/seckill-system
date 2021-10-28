package com.seckill.service;


import com.seckill.service.model.PromoModel;

public interface PromoService {
    // 根据 itemId 获取即将进行的或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);

}

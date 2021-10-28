package com.seckill.service;

import com.seckill.error.BusinessException;
import com.seckill.service.model.OrderModel;

public interface OrderService {
    // 1. 通过前端 url 上传过来的秒杀活动 id，然后下单接口内校验对应的 id 是否属于对应的商品且活动已经开始
    // 2 直接在下单接口内判断对应的商品是否存在秒杀活动，若存在进行中的，则以秒杀价格下单
    OrderModel createOrder(Integer userId, Integer itemId, Integer amount, Integer promoId) throws BusinessException;

}

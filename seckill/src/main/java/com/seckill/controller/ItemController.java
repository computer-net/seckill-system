package com.seckill.controller;

import com.seckill.controller.viewobject.ItemVO;
import com.seckill.error.BusinessException;
import com.seckill.response.CommonReturnType;
import com.seckill.service.ItemService;
import com.seckill.service.model.ItemModel;
import com.seckill.service.model.PromoModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    // 创建商品的 Controller
    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "price")BigDecimal price,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl) throws BusinessException {
        // 封装 service 请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setPrice(price);
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);
        ItemModel itemModelForReturn = itemService.createItem(itemModel);
        ItemVO itemVO = convertItemVOFromModel(itemModelForReturn);
        return CommonReturnType.create(itemVO);
    }

    // 商品详情页浏览
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id")Integer id) {
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = convertItemVOFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    // 商品列表页面浏览
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem() {
        List<ItemModel> itemModels = itemService.listItem();
        List<ItemVO> itemVOS = itemModels.stream().map(itemModel -> {
            ItemVO itemVO = this.convertItemVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOS);
    }

    private ItemVO convertItemVOFromModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        if (itemModel.getPromoModel() != null) {
            PromoModel promoModel = itemModel.getPromoModel();
            // 有正在进行或即将进行的秒杀活动
            itemVO.setPromoStatus(promoModel.getStatus());
            itemVO.setPromoId(promoModel.getId());
            itemVO.setStartDate(promoModel.getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(promoModel.getPromoItemPrice());
        } else {
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }
}

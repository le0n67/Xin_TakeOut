package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

/**
 * Date：2024/9/13  9:12
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */
public interface ShoppingCartService {
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}

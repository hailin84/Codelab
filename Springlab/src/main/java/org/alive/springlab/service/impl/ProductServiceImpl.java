package org.alive.springlab.service.impl;

import org.alive.springlab.entity.Product;
import org.alive.springlab.mapper.ProductMapper;
import org.alive.springlab.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author hailin84
 * @since 2023-01-09
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}

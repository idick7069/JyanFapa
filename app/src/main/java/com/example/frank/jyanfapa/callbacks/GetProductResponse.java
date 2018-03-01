package com.example.frank.jyanfapa.callbacks;

/**
 * Created by Frank on 2018/1/29.
 */

import com.example.frank.jyanfapa.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class GetProductResponse{

    private List<Product> product = new ArrayList<Product>();

    public List<Product> getProduct() {
        return product;
    }
}


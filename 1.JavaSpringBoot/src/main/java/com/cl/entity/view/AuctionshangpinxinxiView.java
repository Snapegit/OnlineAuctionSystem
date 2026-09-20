package com.cl.entity.view;

import com.cl.entity.AuctionshangpinxinxiEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.cl.utils.EncryptUtil;
 

/**
 * 商品信息竞拍记录表
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2024-02-28 19:57:46
 */
@TableName("auctionshangpinxinxi")
public class AuctionshangpinxinxiView  extends AuctionshangpinxinxiEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public AuctionshangpinxinxiView(){
	}
 
 	public AuctionshangpinxinxiView(AuctionshangpinxinxiEntity auctionshangpinxinxiEntity){
 	try {
			BeanUtils.copyProperties(this, auctionshangpinxinxiEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}


}

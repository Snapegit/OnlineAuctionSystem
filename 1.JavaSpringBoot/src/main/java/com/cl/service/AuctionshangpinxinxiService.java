package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.AuctionshangpinxinxiEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.AuctionshangpinxinxiView;


/**
 * 商品信息竞拍记录表
 *
 * @author 
 * @email 
 * @date 2024-02-28 19:57:46
 */
public interface AuctionshangpinxinxiService extends IService<AuctionshangpinxinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<AuctionshangpinxinxiView> selectListView(Wrapper<AuctionshangpinxinxiEntity> wrapper);
   	
   	AuctionshangpinxinxiView selectView(@Param("ew") Wrapper<AuctionshangpinxinxiEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<AuctionshangpinxinxiEntity> wrapper);
   	

}


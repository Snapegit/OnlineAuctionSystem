package com.cl.dao;

import com.cl.entity.AuctionshangpinxinxiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.AuctionshangpinxinxiView;


/**
 * 商品信息竞拍记录表
 * 
 * @author 
 * @email 
 * @date 2024-02-28 19:57:46
 */
public interface AuctionshangpinxinxiDao extends BaseMapper<AuctionshangpinxinxiEntity> {
	
	List<AuctionshangpinxinxiView> selectListView(@Param("ew") Wrapper<AuctionshangpinxinxiEntity> wrapper);

	List<AuctionshangpinxinxiView> selectListView(Pagination page,@Param("ew") Wrapper<AuctionshangpinxinxiEntity> wrapper);
	
	AuctionshangpinxinxiView selectView(@Param("ew") Wrapper<AuctionshangpinxinxiEntity> wrapper);
	

}

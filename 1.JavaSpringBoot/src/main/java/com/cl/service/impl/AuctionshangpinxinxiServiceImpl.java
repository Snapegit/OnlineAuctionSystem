package com.cl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;


import com.cl.dao.AuctionshangpinxinxiDao;
import com.cl.entity.AuctionshangpinxinxiEntity;
import com.cl.service.AuctionshangpinxinxiService;
import com.cl.entity.view.AuctionshangpinxinxiView;

@Service("auctionshangpinxinxiService")
public class AuctionshangpinxinxiServiceImpl extends ServiceImpl<AuctionshangpinxinxiDao, AuctionshangpinxinxiEntity> implements AuctionshangpinxinxiService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AuctionshangpinxinxiEntity> page = this.selectPage(
                new Query<AuctionshangpinxinxiEntity>(params).getPage(),
                new EntityWrapper<AuctionshangpinxinxiEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<AuctionshangpinxinxiEntity> wrapper) {
		  Page<AuctionshangpinxinxiView> page =new Query<AuctionshangpinxinxiView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<AuctionshangpinxinxiView> selectListView(Wrapper<AuctionshangpinxinxiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public AuctionshangpinxinxiView selectView(Wrapper<AuctionshangpinxinxiEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}

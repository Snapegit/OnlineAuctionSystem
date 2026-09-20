<template>
	<div class="app-contain" :style='{"padding":"20px 0px","margin":"20px auto 40px","alignItems":"flex-start","borderRadius":"20px","flexWrap":"wrap","background":"#fff","display":"flex","width":"64%","position":"relative","justifyContent":"space-between"}'>
		<div class="bread_view">
			<el-breadcrumb separator="/" class="breadcrumb">
				<el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
				<el-breadcrumb-item class="second_breadcrumb" v-for="(item,index) in breadList" :key="index">{{item.name}}</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="back_view">
			<el-button class="back_btn" @click="backClick" type="primary">返回</el-button>
		</div>
		<div class="detail_view">
			<div class="swiper_view">
				<mySwiper :data="bannerList" :type="3"
				:loop="false"
				:navigation="false"
				:pagination="true"
				:paginationType="1"
				:scrollbar="false"
				:slidesPerView="1"
				:spaceBetween="20"
				:autoHeight="false"
				:centeredSlides="false"
				:freeMode="false"
				:effectType="10"
				:direction="horizontal"
				:autoplay="false"
				:slidesPerColumn="1">
				<template #default="scope">
					<img :style='{"objectFit":"contain","width":"100%","height":"480px"}' :src="scope.row?$config.url + scope.row:''">
				</template>
			</mySwiper>
			</div>
			
			<div class="info_view">
				<div class="title_view">
					<div class="detail_title">
						{{detail.shangpinmingcheng}}
					</div>
				</div>
				<div class="info_item">
					<div class="info_label">{{startTime<=endTime?'正在竞拍':'竞拍结束'}}</div>
					<count-down class="countDown" v-if="endTime&&startTime" :startTime="startTime" :endTime="endTime" :tipText="'距离开始'"
						tipTextEnd="距离结束 " :endText="`${detail.reversetime}竞拍结束`" dayTxt="天 " hourTxt="小时 "
						minutesTxt="分钟 " secondsTxt="秒">
					</count-down>
				</div>
				<div class="info_item">
					<div class="info_label">价格</div>
					<div class="info_price"><span>￥</span>{{detail.price}}</div>
				</div>
				<div class="info_item" v-if="detail.jf">
					<div class="info_label">积分</div>
					<div class="info_price">{{detail.jf}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">起拍价</div>
					<div class="info_price"><span>￥</span>{{detail.startprice}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">每次加价</div>
					<div class="info_price"><span>￥</span>{{detail.stepprice}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">一口价</div>
					<div class="info_price"><span>￥</span>{{detail.endprice}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">商品分类</div>
					<div  class="info_text" >{{detail.shangpinfenlei}}</div>
				</div>
				<div class="btn_view">
					<el-button class="action_btn" v-if="startTime<endTime" type="primary" @click="auctionClick">竞拍</el-button>
					<el-button class="sucAction_btn" v-else-if="auctionStatus&&startTime>endTime" type="primary" @click="buyNow">竞拍成功，立即付款</el-button>
					<el-button class="unAction_btn" v-else type="info" disabled>竞拍结束</el-button>
				</div>
				<div class="btn_view">
					<el-button v-if="centerType&&(detail.ispay=='未支付'||!detail.ispay)&&btnFrontAuth('shangpinxinxi','支付')" class="approval_btn" @click="payClick">支付</el-button>
					<el-button class="edit_btn" v-if="centerType&&btnAuth('shangpinxinxi','修改')" type="primary" @click="editClick">修改</el-button>
					<el-button class="del_btn" v-if="centerType&&btnAuth('shangpinxinxi','删除')" type="danger" @click="delClick">删除</el-button>
				</div>
			</div>
		</div>
		<el-tabs type="border-card" v-model="activeName" class="tabs_view">
			<el-tab-pane label="出价记录" name="auctionActive">
				<el-table :data="auctionList"  :stripe='false'>
					<el-table-column label="出价人" :resizable='true' align="left" header-align="left">
						<template #default="scope">
							{{scope.row.nickname}}
						</template>
					</el-table-column>
					<el-table-column label="头像" :resizable='true' align="left" header-align="left">
						<template #default="scope">
							<el-image :src="$config.url + scope.row.avatarurl" style="width: 60px;height: 60px;border-radius: 50%;" fit="cover"></el-image>
						</template>
					</el-table-column>
					<el-table-column label="价格" :resizable='true' align="left" header-align="left">
						<template #default="scope">
							{{scope.row.auctionprice}}
						</template>
					</el-table-column>
					<el-table-column label="状态" :resizable='true' align="left" header-align="left">
						<template #default="scope">
							<el-tag :type="scope.row.auctionstatus=='领先'?'success':'danger'">{{scope.row.auctionstatus}}</el-tag>
						</template>
					</el-table-column>
					<el-table-column label="出价时间" :resizable='true' align="left" header-align="left">
						<template #default="scope">
							{{scope.row.addtime}}
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="商品详情" name="first">
				<div v-html="detail.shangpinxiangqing"></div>
			</el-tab-pane>
			<el-tab-pane label="评论" name="commentActive">
				<div class="my_comment_view">
					<el-form ref="commentFormRef" :model="commentForm" class="my_comment_form"
						:rules="commentRules">
						<el-form-item prop="content">
							<el-input class="comment_inp" v-model="commentForm.content" type="textarea"
								placeholder="请输入评论内容"></el-input>
						</el-form-item>
					</el-form>
					<div class="comment_btn">
						<el-button class="add_btn" type="primary" @click="commentSave">立即评论</el-button>
						<el-button class="reset_btn" @click="resetForm">重置</el-button>
					</div>
				</div>
				<div class="comment_list">
					<div class="comment" v-for="(item,index) in commentList" :key="index">
						<div class="comment_top">
							<div class="comment_user">
								<div class="comment_user_img">
									<img :src="item.avatarurl?$config.url + item.avatarurl:'../../../assets/avatar.png'" alt="">
								</div>
								<div class="comment_user_info">
									{{item.nickname}}
								</div>
							</div>
							<div class="comment_time">{{item.addtime}}</div>
						</div>
						<div class="comment_bottom">
							<div class="comment_content">{{item.content}}</div>
							<div class="comment_reply" v-if="item.reply">
								回复：{{item.reply}}
							</div>
						</div>
					</div>
				</div>
				<el-pagination
					background 
					:layout="layouts.join(',')"
					:total="commentTotal" 
					:page-size="commentQuery.limit"
					prev-text="上一页"
					next-text="下一页"
					:hide-on-single-page="false"
					:style='{"border":"0px solid #eee","padding":"4px 0","margin":"10px 0 20px","whiteSpace":"nowrap","color":"#333","textAlign":"center","flexWrap":"wrap","background":"none","display":"flex","width":"100%","fontWeight":"500"}'
					@size-change="commentSizeChange"
					@current-change="commentCurrentChange" 
					@prev-click="commentPrevClick"
					@next-click="commentNextClick"  />
			</el-tab-pane>
		</el-tabs>
		<el-dialog v-model="auctionVisible" title="竞拍" width="30%">
			<el-form :model="auctionForm">
				<el-row>
					<el-col :span="24">
						<el-form-item label="当前价格">
							{{detail.price}}
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="我的出价">
							<el-input @input="auctionChange" type="number" v-model="auctionForm.offerPrice" placeholder="请输入价格" :min="Number(detail.price) + Number(detail.stepprice)" :max="Number(detail.endprice)"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				
				
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="auctionVisible = false">取消</el-button>
					<el-button type="primary" @click="auctionSave">
						出价
					</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>
<script setup>
	import axios from 'axios'
	import {
		ref,
		getCurrentInstance,
		watch,
		onUnmounted,
		onMounted,
		nextTick,
		computed
	} from 'vue';
	import {
		ElMessageBox
	} from 'element-plus'
	import countDown from '@/components/countDown'
	import {
		useRoute,
		useRouter
	} from 'vue-router';
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const route = useRoute()
	const router = useRouter()
	//基础信息
	const tableName = 'shangpinxinxi'
	const formName = '商品信息'
	//基础信息
	const breadList = ref([{
		name: formName
	}])
	//权限验证
	const btnAuth = (e,a)=>{
		if(centerType.value){
			return context?.$toolUtil.isBackAuth(e,a)
		}else{
			return context?.$toolUtil.isAuth(e,a)
		}
	}
	//查看权限验证
	const btnFrontAuth = (e,a)=>{
		if(centerType.value){
			return context?.$toolUtil.isBackAuth(e,a)
		}else{
			return context?.$toolUtil.isFrontAuth(e,a)
		}
	}
	// 返回
	const backClick = () =>{
		history.back()
	}
	// 轮播图
	const bannerList = ref([])
	// 详情
	const title = ref('')
	const detail = ref({})
    const activeName = ref('auctionActive')
	const startTime = ref('')
	const endTime = ref('')
	const getDetail = () => {
		context?.$http({
			url: `${tableName}/detail/${route.query.id}`,
			method: 'get'
		}).then(res => {
			title.value = res.data.data.shangpinmingcheng
			bannerList.value = res.data.data.shangpintupian?res.data.data.shangpintupian.split(','):[]
			startTime.value = new Date().getTime()
			endTime.value = new Date(res.data.data.reversetime).getTime()
			detail.value = res.data.data
		})
	}
	// 下载文件
	const downClick = (file) => {
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		let arr = file.replace(new RegExp('file/', "g"), "")
		axios.get((location.href.split(context?.$config.name).length>1 ? location.href.split(context?.$config.name)[0] :'') + context?.$config.name + '/file/download?fileName=' + arr, {
			headers: {
				token: context?.$toolUtil.storageGet('frontToken')
			},
			responseType: "blob"
		}).then(({
			data
		}) => {
			const binaryData = [];
			binaryData.push(data);
			const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
				type: 'application/pdf;chartset=UTF-8'
			}))
			const a = document.createElement('a')
			a.href = objectUrl
			a.download = arr
			// a.click()
			// 下面这个写法兼容火狐
			a.dispatchEvent(new MouseEvent('click', {
				bubbles: true,
				cancelable: true,
				view: window
			}))
			window.URL.revokeObjectURL(data)
		})
	}
	// 判断是否从个人中心跳转
	const centerType = ref(false)
	const init = () => {
		if(route.query.centerType){
			centerType.value = true
		}
		getDetail()
		// 竞拍
		getAuctionList()
		getAuctionStatus()
		// 评论
		getCommentList()
	}
	//竞拍
	const auctionForm = ref({})
	const auctionVisible = ref(false)
	const auctionClick = () => {
		auctionForm.value = {
			offerPrice: Number(detail.value.price) + Number(detail.value.stepprice)
		}
		auctionVisible.value = true
	}
	//出价
	const auctionSave = () => {
		if (!auctionForm.value.offerPrice && auctionForm.value.offerPrice == '') {
			context?.$toolUtil.message('请输入价格', 'error')
			return false
		}
		let params = {
			refid: detail.value.id,
			userid: context?.$toolUtil.storageGet('userid'),
			nickname: context?.$toolUtil.storageGet('frontName'),
			avatarurl: context?.$toolUtil.storageGet('headportrait') ? context?.$toolUtil.storageGet('headportrait') : '',
			auctionprice: auctionForm.value.offerPrice,
			auctionstatus: '领先'
		}
		context?.$http({
			url: `auction${tableName}/add`,
			method: 'post',
			data: params
		}).then(res => {
			detail.value.price = auctionForm.value.offerPrice
			context?.$http({
				url: `${tableName}/update`,
				method: 'post',
				data: detail.value
			})
			context?.$toolUtil.message('出价成功', 'success', () => {
				auctionVisible.value = false
				getAuctionList()
				context?.$http({
					url: 'storeup/list',
					method: 'get',
					params: {
						page: 1,
						limit: 1,
						type: 31,
						refid: detail.value.id,
						tablename: tableName,
						userid: context?.$toolUtil.storageGet('userid')
					}
				}).then(res1 => {
					if (res1.data.data.list.length) {
						let ids = []
						ids.push(res1.data.data.list[0].id)
						context?.$http({
							url: 'storeup/delete',
							method: 'post',
							data: ids
						})
					}
					let params = {
						name: title.value,
						picture: bannerList.value[0],
						refid: detail.value.id,
						type: 31,
						tablename: tableName,
						userid: context?.$toolUtil.storageGet('userid')
					}
					context?.$http({
						url: 'storeup/add',
						method: 'post',
						data: params
					})
				})
			})
		})
	}
	const auctionChange = (e) => {
		let max = Number(detail.value.endprice)
		let min = Number(detail.value.price) + Number(detail.value.stepprice)
		if (e > max) {
			auctionForm.value.offerPrice = max
			context?.$toolUtil.message('超过一口价', 'error')
		} else if (e < min) {
			auctionForm.value.offerPrice = min
			context?.$toolUtil.message('不能低于当前价格', 'error')
		}
	}
	const auctionList = ref([])
	//获取竞拍列表
	const getAuctionList = () => {
		context?.$http({
			url: `auction${tableName}/list`,
			method: 'get',
			params: {
				refid: route.query.id,
				sort: 'addtime',
				order: 'desc',
				limit: 1000
			}
		}).then(res => {
			auctionList.value = res.data.data.list
		})
	}
	//是否竞拍成功
	const auctionStatus = ref(false)
	const getAuctionStatus = () => {
		context?.$http({
			url: `auction${tableName}/list`,
			method: 'get',
			params: {
				refid: route.query.id,
				sort: 'addtime',
				order: 'desc',
				limit: 1000,
				userid: context?.$toolUtil.storageGet('userid')
			}
		}).then(res => {
			if (res.data.data.list.length && res.data.data.list[0].auctionstatus == '领先') {
				context?.$http({
					url: 'orders/list',
					method: 'get',
					params: {
						limit: 1,
						userid: context?.$toolUtil.storageGet('userid'),
						goodid: route.query.id
					}
				}).then(res1 => {
					if (res1.data.data.list.length == 0) {
						auctionStatus.value = true
					}
				})
			}
		})
	}
	//评论
	const commentForm = ref({
		content: '',
		refid: route.query.id,
		userid: context?.$toolUtil.storageGet('userid'),
		nickname: context?.$toolUtil.storageGet('frontName'),
		avatarurl: context?.$toolUtil.storageGet('headportrait') ? context?.$toolUtil.storageGet('headportrait') : ''
	})
	const commentRules = ref({
		content: [{
			required: true,
			message: '请输入',
			trigger: 'blur'
		}, ]
	})
	const commentQuery = ref({
		page: 1,
		limit: 10,
		refid: route.query.id
	})
	const layouts = ref(["prev","pager","next"])
	const commentList = ref([])
	const commentTotal = ref(0)
	const commentFormRef = ref(null)
	const commentSizeChange = (size) =>{
		commentQuery.value.limit = size
		getCommentList()
	}
	const commentCurrentChange = (page) =>{
		commentQuery.value.page = page
		getCommentList()
	}
	const commentPrevClick = () =>{
		commentQuery.value.page = commentQuery.value.page - 1
		getCommentList()
	}
	const commentNextClick = () =>{
		commentQuery.value.page = commentQuery.value.page + 1
		getCommentList()
	}
	const getCommentList = () => {
		context?.$http({
			url: `discuss${tableName}/list`,
			params: commentQuery.value,
			method: 'get'
		}).then(res => {
			commentList.value = res.data.data.list
			commentTotal.value = res.data.data.total
	
		})
	}
	//提交评论
	const commentSave = () => {
		let sensitiveWords = "";
		let sensitiveWordsArr = [];
		if(sensitiveWords) {
		    sensitiveWordsArr = sensitiveWords.split(",");
		}
		for(var i=0; i<sensitiveWordsArr.length; i++){
		    //全局替换
		    var reg = new RegExp(sensitiveWordsArr[i],"g");
		    //判断内容中是否包括敏感词
		    if (commentForm.value.content.indexOf(sensitiveWordsArr[i]) > -1) {
		        // 将敏感词替换为 **
		        commentForm.value.content = commentForm.value.content.replace(reg,"**");
		    }
		}
		commentFormRef.value.validate((valid) => {
			if (valid) {
				context?.$http({url:'orders/list',method:'get',params:{page:1,limit:1,status:'已完成',goodid:detail.value.id,userid:context?.$toolUtil.storageGet('userid')}}).then(res=>{
					if(res.data.data.list.length==0){
						context?.$toolUtil.message('请完成订单后再评论！','error')
						return false
					}
					context?.$http({
						url: `discuss${tableName}/add`,
						method: 'post',
						data: commentForm.value
					}).then(res => {
						context?.$toolUtil.message('评论成功', 'success', () => {
							resetForm()
							getCommentList()
						})
					})
				})
			}
		})
	}
	const resetForm = () => {
		commentFormRef.value.resetFields()
	}
	const buyNumber = ref(1)
	//立即购买
	const buyNow = () => {
		if(!auctionStatus.value && new Date().getTime() > new Date(detail.value.reversetime).getTime()){
			context?.$toolUtil.message('活动已结束','error')
			return false
		}
		let data = {
			tablename: tableName,
			goodid: detail.value.id,
			goodname: title.value,
			buynumber: buyNumber.value,
			userid: context?.$toolUtil.storageGet('userid'),
			discountprice: detail.value.vipprice ? detail.value.vipprice : 0,
			picture: bannerList.value[0],
			price: detail.value.price
		}
		context?.$toolUtil.storageSet('orders_good',JSON.stringify([data]))
		let query = {
			type: 1
		}
		router.push({path: '/index/order_confirm', query: query})
	}
	//修改
	const editClick = () => {
		router.push(`/index/${tableName}Add?id=${detail.value.id}&&type=edit`)
	}
	//删除
	const delClick = () => {
		ElMessageBox.confirm(`是否删除此${formName}？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(()=>{
			context?.$http({
				url: `${tableName}/delete`,
				method: 'post',
				data: [detail.value.id]
			}).then(res=>{
				context?.$toolUtil.message('删除成功','success',()=>{
					history.back()
				})
			})
			
		})
	}
	onMounted(()=>{
		init()
	})
</script>
<style lang="scss" scoped>
	// 返回盒子
	.back_view {
		border-radius: 0px;
		padding: 0 20px;
		margin: 20px auto;
		background: none;
		display: block;
		width: 100%;
		text-align: right;
		// 返回按钮
		.back_btn {
			border: 1px solid #eee;
			cursor: pointer;
			border-radius: 4px;
			padding: 0 30px;
			outline: none;
			color: #666;
			background: #f9f9f9;
			width: auto;
			font-size: 14px;
			height: 32px;
		}
		// 返回按钮-悬浮
		.back_btn:hover {
		}
	}
	// 面包屑盒子
	.bread_view {
		border-radius: 0px;
		padding: 10px 20px;
		margin: 0px auto;
		background: none;
		width: 100%;
		border-color: #eee;
		border-width: 0 0 1px;
		position: relative;
		border-style: solid;
		:deep(.breadcrumb) {
			font-size: 14px;
			line-height: 1;
			.el-breadcrumb__separator {
				margin: 0 9px;
				color: #999;
				font-weight: 500;
			}
			.first_breadcrumb {
				.el-breadcrumb__inner {
					color: #999;
					display: inline-block;
				}
			}
			.second_breadcrumb {
				.el-breadcrumb__inner {
					color: #999;
					display: inline-block;
				}
			}
		}
	}
	
	.detail_view{
		border-radius: 0;
		padding: 20px 0;
		background: linear-gradient(180deg, rgba(255,255,255,1) 0%, rgba(242,254,255,1) 100%);
		display: flex;
		width: 100%;
		border-color: #27bacc30;
		border-width: 1px;
		justify-content: space-between;
		position: relative;
		border-style: solid;
		flex-wrap: wrap;
		// 轮播图
		.swiper_view {
			padding: 0;
			margin: 0 auto;
			background: none;
			width: 80%;
			float: left;
			height: 480px;
		}
		
		// 文字区
		.info_view {
			border: 0px solid #eee;
			padding: 0px 7%;
			margin: 0 0 20px;
			background: none;
			width: 100%;
			box-sizing: border-box;
			float: right;
		
			.title_view {
				border: 0px solid #eee;
				padding: 0;
				margin: 0 0 12px;
				background: none;
				display: flex;
				width: 100%;
				line-height: 40px;
				justify-content: center;
				align-items: center;
		
				.detail_title {
					color: #666;
					font-weight: 600;
					font-size: 18px;
				}
				.follow {
					border: 0px solid #ffffff50;
					cursor: pointer;
					padding: 4px 10px;
					color: #fff;
					display: flex;
					line-height: 1;
					right: 20px;
					border-radius: 4px;
					background: none;
					width: auto;
					justify-content: center;
					align-items: center;
					position: absolute;
					.iconfont {
						margin: 0 4px 0 0;
						color: #999;
						font-size: 24px;
					}
					.iconfontActive {
						margin: 0 4px 0 0;
						color: #f79d1c;
						font-size: 24px;
					}
					span {
						color: #999;
						font-size: 16px;
					}
					.textActive {
						color: #f79d1c;
						font-size: 16px;
					}
				}
				.follow:hover {
				}
				.follow:active {
					transform: scale(0.9);
				}
			}
		
			.info_item {
					border-radius: 0px;
					padding: 0;
					margin: 0 0 10px;
					background: none;
					display: flex;
					border-color: #eee;
					border-width: 0px;
					align-items: center;
					border-style: solid;
		
				.info_label {
					margin: 0 12px 0 0;
					color: #666;
					font-weight: 500;
					width: auto;
				}
				.info_text {
				}
			}
			.btn_view {
				padding: 0;
				margin: 20px 0 20px;
				display: flex;
				flex-wrap: wrap;
				// 竞拍-按钮
				.action_btn {
					border: 1px solid rgba(250, 200, 88, 1);
					padding: 0 10px;
					color: rgba(250, 200, 88, 1);
					background: none;
					line-height: 32px;
					height: 32px;
				}
				// 悬浮
				.action_btn:hover {
				}
				// 竞拍成功-按钮
				.sucAction_btn {
					border: 1px solid rgba(145, 204, 117, 1);
					padding: 0 10px;
					color: rgba(145, 204, 117, 1);
					background: none;
					line-height: 32px;
					height: 32px;
				}
				// 悬浮
				.sucAction_btn:hover {
				}
				// 竞拍结束-按钮
				.unAction_btn {
					border: 1px solid rgba(84, 112, 198, 1);
					padding: 0 10px;
					color: rgba(84, 112, 198, 1);
					background: none;
					line-height: 32px;
					height: 32px;
				}
				// 悬浮
				.unAction_btn:hover {
				}
				// 修改-按钮
				.edit_btn {
					border: none;
					padding: 0 10px;
					color: #fff;
					background: #27bacc;
					line-height: 32px;
					height: 32px;
				}
				// 悬浮
				.edit_btn:hover {
				}
				// 删除-按钮
				.del_btn {
					border: none;
					padding: 0 10px;
					color: #fff;
					background: #f68b8b;
					line-height: 32px;
					height: 32px;
				}
				// 悬浮
				.del_btn:hover {
				}
			}
		}
	}
	

	//底部盒子
	.tabs_view {
		border: 0px solid #ddd;
		border-radius: 0px;
		padding: 0;
		box-shadow: none;
		margin: 20px auto;
		background: none;
		width: 100%;
		:deep(.el-tabs__header) {
			background: transparent;
			border: none;
		}
		// 头部
		:deep(.el-tabs__nav-scroll) {
			border-radius: 0;
			padding: 2px 20px 0;
			margin: 0;
			background: linear-gradient(180deg, rgba(255,255,255,1) 0%, rgba(243,243,243,1) 100%);
			display: flex;
			border-color: #ddd;
			border-width: 1px 1px 4px;
			justify-content: center;
			border-style: solid;
			height: 50px;
			.el-tabs__nav {
				.el-tabs__item {
					border: 0;
					padding: 0 30px;
					margin: 0 10px;
					color: #333;
					font-weight: 500;
					display: inline-block;
					font-size: 14px;
					line-height: 50px;
					background: none;
					position: relative;
					list-style: none;
					text-align: center;
					min-width: 60px;
					height: 50px;
				}
				.el-tabs__item:hover {
					border: 0;
					border-radius: 8px 8px 0 0;
					color: #fff;
					background: #27bacc;
				}
				.is-active {
					border: 0;
					border-radius: 8px 8px 0 0;
					padding: 0 30px;
					color: #fff;
					background: #27bacc;
					text-align: center;
					min-width: 60px;
				}
			}
		}
		// 内容区
		:deep(.el-tabs__content) {
			border-radius: 0 0 4px 4px;
			padding: 20px 0;
			color: #666;
			background: none;
			font-size: 14px;
			border-color: #eee;
			border-width: 0;
			border-style: solid;
		}
		// 表格样式
		.el-table {
			padding: 0;
			background: #fff;
			width: 100%;
			border-color: #eee;
			border-width: 1px 0 0 1px;
			border-style: solid;
			:deep(.el-table__header-wrapper) {
				thead {
					color: #999;
					background: #f6f6f6;
					font-weight: 500;
					width: 100%;
					tr {
						background: #f6f6f6;
						th {
							padding: 12px 0;
							background: none;
							border-color: #eee;
							border-width: 0 1px 1px 0;
							border-style: solid;
							text-align: left;
							.cell {
								padding: 0 10px;
								word-wrap: normal;
								color: #666;
								word-break: break-all;
								white-space: normal;
								font-weight: bold;
								display: inline-block;
								vertical-align: middle;
								width: 100%;
								line-height: 24px;
								position: relative;
								text-overflow: ellipsis;
							}
						}
					}
				}
			}
			:deep(.el-table__body-wrapper) {
				tbody {
					width: 100%;
					tr {
						background: #fff;
						td {
							padding: 12px 0;
							color: #666;
							background: #fff;
							border-color: #eee;
							border-width: 0 1px 1px 0;
							border-style: solid;
							text-align: left;
							.cell {
								padding: 0 10px;
								overflow: hidden;
								word-break: break-all;
								white-space: normal;
								line-height: 24px;
								text-overflow: ellipsis;
							}
						}
					}
					tr:hover {
						td {
							padding: 12px 0;
							color: #333;
							background: #f8f8f8;
							border-color: #eee;
							border-width: 0 1px 1px 0;
							border-style: solid;
							text-align: left;
						}
					}
				}
			}
		}
		//评论
		//我的评论
		.my_comment_view {
			border: 0px solid #eee;
			border-radius: 4px;
			box-shadow: none;
			padding: 0px;
			margin: 0px;
			background: none;
			width: 100%;
		
			.my_comment_form {
				border: 0px solid #eee;
				border-radius: 0px;
				padding: 0px;
				box-shadow: none;
				background: #fff;
				width: 100%;
				box-sizing: border-box;
				// 输入框
				:deep(.el-textarea__inner) {
					border: 1px solid #eee;
					border-radius: 0;
					padding: 12px;
					box-shadow: none;
					color: #333;
					width: 100%;
					font-size: 14px;
					min-height: 120px;
				}
			}
			// 按钮盒子
			.comment_btn {
				margin: 20px 0 0;
				display: flex;
				width: 100%;
				justify-content: center;
				align-items: center;
				// 评论按钮
				.add_btn {
					border: none;
					border-radius: 20px;
					padding: 0 20px;
					margin: 0 20px 0 0;
					color: #fff;
					background: #27bacc;
					line-height: 36px;
					height: 36px;
				}
				// 悬浮
				.add_btn:hover {
				}
				// 重置按钮
				.reset_btn {
					border: 1px solid #27bacc;
					border-radius: 20px;
					padding: 0 20px;
					color: #27bacc;
					background: none;
					line-height: 36px;
					min-width: 100px;
					height: 36px;
				}
				// 悬浮
				.reset_btn:hover {
				}
			}
		}
		
		//评论列表
		.comment_list {
			border: 0px solid #eee;
			border-radius: 4px;
			padding: 0px;
			margin: 30px auto;
			background: none;
			display: flex;
			width: 100%;
			justify-content: space-between;
			flex-wrap: wrap;
		
			.comment {
				border: 1px solid #27bacc20;
				border-radius: 4px;
				padding: 0 0 10px;
				margin: 0 0 20px;
				background: none;
				width: 48%;
				box-sizing: border-box;
				.comment_top {
					border-radius: 0px;
					padding: 0 0 6px;
					background: #27bacc10;
					display: flex;
					width: 100%;
					font-size: 14px;
					border-color: #27bacc20;
					border-width: 0 0 1px;
					justify-content: space-between;
					align-items: center;
					border-style: solid;
		
					.comment_user {
						border-radius: 4px;
						padding: 4px 30px 4px 10px;
						display: block;
						align-items: center;
						.comment_user_img {
							margin: 0 10px 0 0;
							font-size: 0;
							float: left;
		
							img {
								border-radius: 50%;
								width: 40px;
								height: 40px;
							}
						}
						.comment_user_info {
							color: #333;
							font-weight: 600;
							font-size: 14px;
							line-height: 40px;
							float: left;
						}
					}
					.comment_time{
						border: 0px solid #eee;
						border-radius: 4px;
						padding: 0px 20px;
						color: #999;
						font-size: 14px;
						float: right;
					}
				}
		
				.comment_bottom {
					border: 0px solid #eee;
					border-radius: 4px;
					padding: 0px 10px;
					margin: 10px 0 0;
					flex-direction: column;
					display: flex;
					width: 100%;
					align-items: flex-start;
					.comment_content {
						color: #666;
						font-weight: 600;
						font-size: 14px;
					}
					.comment_reply {
						padding: 10px 0 0;
						flex-direction: column;
						color: #999;
						text-indent: 2rem;
						display: flex;
						width: 100%;
						font-size: 14px;
						align-items: flex-start;
					}
				}
			}
		}
		// 分页器
		.el-pagination {
			// 总页码
			:deep(.el-pagination__total) {
				margin: 0 10px 0 0;
				color: #666;
				font-weight: 400;
				display: inline-block;
				vertical-align: top;
				font-size: 13px;
				line-height: 24px;
				height: 24px;
			}
			// 上一页
			:deep(.btn-prev) {
				border: 1px solid #ddd;
				border-radius: 4px;
				padding: 0 4px;
				margin: 0 2px;
				color: #666;
				background: none;
				display: inline-block;
				vertical-align: top;
				font-size: 13px;
				line-height: 24px;
				min-width: 24px;
				height: 24px;
			}
			// 下一页
			:deep(.btn-next) {
				border: 1px solid #ddd;
				border-radius: 4px;
				padding: 0 4px;
				margin: 0 2px;
				color: #666;
				background: none;
				display: inline-block;
				vertical-align: top;
				font-size: 14px;
				line-height: 24px;
				min-width: 24px;
				height: 24px;
			}
			// 上一页禁用
			:deep(.btn-prev:disabled) {
				border: 1px solid #ddd;
				cursor: not-allowed;
				padding: 0 4px;
				margin: 0 2px;
				color: #C0C4CC;
				display: inline-block;
				vertical-align: top;
				font-size: 14px;
				line-height: 24px;
				border-radius: 4px;
				background: none;
				min-width: 24px;
				height: 24px;
			}
			// 下一页禁用
			:deep(.btn-next:disabled) {
				border: 1px solid #ddd;
				cursor: not-allowed;
				padding: 0 4px;
				margin: 0 2px;
				color: #C0C4CC;
				display: inline-block;
				vertical-align: top;
				font-size: 14px;
				line-height: 24px;
				border-radius: 4px;
				background: none;
				min-width: 24px;
				height: 24px;
			}
			// 页码
			:deep(.el-pager) {
				padding: 0;
				margin: 0;
				display: inline-block;
				vertical-align: top;
				// 数字
				.number {
					cursor: pointer;
					padding: 0 4px;
					margin: 0 5px;
					color: #666;
					display: inline-block;
					vertical-align: top;
					font-size: 13px;
					line-height: 24px;
					border-radius: 100%;
					background: #f4f4f5;
					text-align: center;
					min-width: 24px;
					height: 24px;
				}
				// 数字悬浮
				.number:hover {
					cursor: pointer;
					padding: 0 4px;
					margin: 0 5px;
					color: #fff;
					display: inline-block;
					vertical-align: top;
					font-size: 13px;
					line-height: 24px;
					border-radius: 100%;
					background: #27bacc;
					text-align: center;
					min-width: 24px;
					height: 24px;
				}
				// 选中
				.number.is-active {
					cursor: default;
					padding: 0 4px;
					margin: 0 5px;
					color: #fff;
					display: inline-block;
					vertical-align: top;
					font-size: 13px;
					line-height: 24px;
					border-radius: 100%;
					background: #27bacc;
					text-align: center;
					min-width: 24px;
					height: 24px;
				}
			}
			// sizes
			:deep(.el-pagination__sizes) {
				box-shadow: none;
				margin: 0 0 0 5px;
				display: inline-block;
				vertical-align: top;
				font-size: 13px;
				line-height: 24px;
				height: 24px;
				.el-select {
					border: 0px solid #DCDFE6;
					cursor: pointer;
					padding: 0;
					color: #606266;
					display: inline-block;
					font-size: 13px;
					line-height: 24px;
					border-radius: 2px;
					outline: 0;
					background: #f4f4f5;
					width: 100%;
					text-align: center;
					height: 24px;
				//去掉默认样式
				.select-trigger{
					height: 100%;
					.el-input{
						height: 100%;
						.el-input__wrapper{
							border: none;
							box-shadow: none;
							background: none;
							border-radius: 0;
							height: 100%;
							padding: 0;
						}
						.is-focus {
							box-shadow: none !important;
						}
					}
				}
				}
			}
			// 跳页
			:deep(.el-pagination__jump) {
				margin: 0 0 0 24px;
				color: #606266;
				display: inline-block;
				vertical-align: top;
				font-size: 13px;
				line-height: 28px;
				height: 28px;
				// 输入框
				.el-input {
					border: 1px solid #DCDFE6;
					cursor: pointer;
					padding: 0 3px;
					margin: 0 6px;
					color: #606266;
					display: inline-block;
					font-size: 14px;
					line-height: 24px;
					border-radius: 3px;
					outline: 0;
					background: #FFF;
					width: 38px;
					text-align: center;
					height: 24px;
					//去掉默认样式
					.el-input__wrapper{
						border: none;
						box-shadow: none;
						background: none;
						border-radius: 0;
						height: 100%;
						padding: 0;
					}
					.is-focus {
						box-shadow: none !important;
					}
				}
			}
		}
	}
	


</style>
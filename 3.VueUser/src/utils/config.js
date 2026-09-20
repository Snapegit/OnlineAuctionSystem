const config = {
    get() {
        return {
            url : process.env.VUE_APP_BASE_API_URL + process.env.VUE_APP_BASE_API + '/',
            name: process.env.VUE_APP_BASE_API,
			menuList:[
				{
					name: '商品资讯管理',
					icon: '${frontMenu.fontClass}',
					child:[
						{
							name:'商品资讯',
							url:'/index/newsList'
						},
					]
				},
				{
					name: '商品信息管理',
					icon: '${frontMenu.fontClass}',
					child:[
						{
							name:'商品信息',
							url:'/index/shangpinxinxiList'
						},
					]
				},
			]
        }
    },
    getProjectName(){
        return {
            projectName: "基于SpringBoot的网上拍卖系统"
        } 
    }
}
export default config

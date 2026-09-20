<template>
	<div>
		<el-dialog v-model="formVisible" :title="formTitle" width="60%" destroy-on-close>
			<el-form class="add_form" :model="form" label-width="80px" ref="formRef" :rules="rules">
				<el-col :span="12">
					<el-form-item prop="name" label="联系人">
						<el-input class="list_inp" v-model="form.name" placeholder="请输入联系人"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item prop="phone" label="手机号">
						<el-input class="list_inp" v-model="form.phone" placeholder="请输入手机号"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item prop="address" label="地址">
						<el-input class="list_inp" v-model="form.address" placeholder="请输入地址"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item prop="isdefault" label="默认地址">
						<el-radio-group class="list_radio" v-model="form.isdefault">
							<el-radio label="是"></el-radio>
							<el-radio label="否"></el-radio>
						</el-radio-group>
					</el-form-item>
				</el-col>
			</el-form>
			<template #footer>
				<span class="formModel_btn_box">
					<el-button class="formModel_cancel" @click="formVisible=false">关闭</el-button>
					<el-button class="formModel_confirm" type="primary" @click="save">保存</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>
<script setup>
	import {
		ref,
		nextTick,
		getCurrentInstance,
		defineEmits
	} from 'vue';
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const id = ref(0)
	const form = ref({})
	const formRef = ref(null)
	const formVisible = ref(false)
	const formTitle = ref('')
	const resetForm = () => {
		form.value = {
			userid: context?.$toolUtil.storageGet('userid'),
			name: '',
			phone: '',
			isdefault: '是',
			address: ''
		}
	}
	//匹配手机号码
	const validateMobile = (rule, value, callback) => {
		if (!value) {
			callback();
		} else if (!context?.$toolUtil.isMobile(value)) {
			callback(new Error("请输入正确的手机号码"));
		} else {
			callback();
		}
	}
	const rules = ({
		name: [{
			required: true,
			message: '请输入联系人',
			trigger: 'blur'
		}],
		phone: [{
			required: true,
			message: '请输入联系人',
			trigger: 'blur'
		}, {
			required: true,
			validator: validateMobile,
			trigger: 'blur'
		}],
	})
	const save = () => {
		formRef.value.validate((valid) => {
			if (valid) {
				context?.$http({
					url: `address/${form.value.id?'update':'add'}`,
					method: 'post',
					data: form.value
				}).then(res => {
					context?.$toolUtil.message(`保存成功`, 'success', () => {
						formVisible.value = false
						emit('formModelChange')
					})
				})
			}
		})
	}
	//初始化
	const init = (refid=null,text) => {
		resetForm()
		if(refid){
			id.value = refid
			getInfo()
		}
		formTitle.value = text
		formVisible.value = true
	}
	//声明父级调用
	defineExpose({
		init
	})
	//回调父级方法
	const emit = defineEmits(['formModelChange'])
	const getInfo = () => {
		context?.$http({
			url: `address/detail/${id.value}`,
			method: 'get'
		}).then(res => {
			form.value = res.data.data
		})
	}
</script>
<style lang="scss" scoped>

	// 表单
	.add_form{
		border: 1px solid #27bacc30;
		border-radius: 0px;
		padding: 30px 7%;
		background: linear-gradient(180deg, rgba(255,255,255,1) 0%, rgba(242,254,255,1) 100%);
		// form item
		:deep(.el-form-item) {
			border: 0px solid #eee;
			padding: 6px 0;
			margin: 0 0 20px 0;
			background: none;
			display: flex;
			//label
			.el-form-item__label {
			 background: none;
			 display: block;
			 width: auto;
			 min-width: 150px;
			 text-align: right;
			}
			// 内容盒子
			.el-form-item__content {
				display: flex;
				width: calc(100% - 150px);
				justify-content: flex-start;
				align-items: center;
				flex-wrap: wrap;
				// 输入框
				.list_inp {
					padding: 0 10px;
					background: none;
					width: auto;
					border-color: #ddd;
					border-width: 0 0 1px;
					line-height: 36px;
					box-sizing: border-box;
					border-style: solid;
					min-width: 350px;
					height: 36px;
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
				//单选框样式
				.list_radio {
					display: flex;
					width: calc(100% - 120px);
					align-items: center;
					flex-wrap: wrap;
					//未选中样式
					.el-radio {
						margin: 0 20px 0 0;
						display: flex;
						width: auto;
						justify-content: center;
						align-items: center;
						//单选框
						.el-radio__inner {
							background: #fff;
							border-color: #999;
						}
						//提示文字
						.el-radio__label{
							color: #999;
						}
					}
					//选中样式
					.is-checked {
						//单选框
						.el-radio__inner {
							background: #27bacc;
							border-color: #27bacc;
						}
						//提示文字
						.el-radio__label{
							color: #27bacc;
						}
					}
				}
			}
		}
	}

	// 按钮盒子
	.formModel_btn_box {
		padding: 0 150px;
		display: flex;
		width: 100%;
		justify-content: center;
		align-items: center;
		.formModel_cancel {
			border: 1px solid #27bacc;
			cursor: pointer;
			border-radius: 8px;
			padding: 0 40px;
			margin: 0 20px 0 0;
			outline: none;
			color: #27bacc;
			background: none;
			width: auto;
			font-size: 14px;
			line-height: 40px;
			height: 40px;
		}
		.formModel_cancel:hover {
		}
		
		.formModel_confirm {
			border: 0;
			cursor: pointer;
			border-radius: 8px;
			padding: 0 40px;
			margin: 0 20px 0 0;
			outline: none;
			color: #fff;
			background: #27bacc;
			width: auto;
			font-size: 14px;
			line-height: 40px;
			height: 40px;
		}
		.formModel_confirm:hover {
		}
	}
</style>
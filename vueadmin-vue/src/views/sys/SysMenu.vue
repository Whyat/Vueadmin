<template>
  <div>
    <!--行内表单里的的新增按钮-->
    <!--点击事件：打开对话框-->
    <el-form :inline="true">
      <el-form-item>
        <el-button type="primary" @click="centerDialogVisible = true">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        :data="tableData"
        style="width: 100%;margin-bottom: 20px;"
        row-key="id"
        border
        stripe
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column
          prop="name"
          label="名称"
          sortable
          width="180">
      </el-table-column>

      <el-table-column
          prop="perms"
          label="权限编码"
          sortable
          width="180">
      </el-table-column>

      <el-table-column
          prop="icon"
          label="图标">
      </el-table-column>

      <el-table-column
          prop="类型"
          label="type">
        <!--使用了作用域插槽，自己渲染表格内的内容-->
        <template v-slot="scopedProps">
          <el-tag size="small" v-if="scopedProps.row.type === 0" type="warning">目录</el-tag>
          <el-tag size="small" v-else-if="scopedProps.row.type === 1" type="danger">菜单</el-tag>
          <el-tag size="small" v-else-if="scopedProps.row.type === 2" type="success">按钮</el-tag>
        </template>
      </el-table-column>

      <el-table-column
          prop="path"
          label="菜单url">
      </el-table-column>

      <el-table-column
          prop="component"
          label="菜单组件">
      </el-table-column>

      <el-table-column
          prop="orderNum"
          label="排序号">
      </el-table-column>

      <el-table-column
          prop="status"
          label="状态">
        <template v-slot="scopedProps">
          <el-tag size="small" v-if="scopedProps.row.status === 1" type="success">正常</el-tag>
          <el-tag size="small" v-else-if="scopedProps.row.status === 0" type="danger">禁用</el-tag>
        </template>
      </el-table-column>

      <el-table-column
          prop="ops"
          label="操作">

        <template v-slot="scopedProps">
          <el-button type="text" @click="editRow(scopedProps.row.id)">查看</el-button>
          <!--<el-divider direction="vertical"></el-divider>-->

          <!--<template>-->
          <!--  <el-popconfirm title="这是一段内容确定删除吗？" @confirm="deleteRow(scopedProps.row.id)">-->
          <!--    <el-button type="text" slot="reference">删除</el-button>-->
          <!--  </el-popconfirm>-->
          <!--</template>-->

        </template>
      </el-table-column>
    </el-table>

    <!--新增按钮的弹窗-->
    <el-dialog
        title="查看权限"
        :visible.sync="centerDialogVisible"
        width="50%"
        @close="dialogClosed">


      <el-form :model="editForm"  ref="editForm" label-width="100px" class="demo-ruleForm">


        <el-form-item label="上级菜单" prop="parentId">
          <el-select v-model="editForm.parentId" placeholder="请选择上级菜单">
            <template v-for="row in tableData">
              <el-option :label="row.name" :value="row.id"></el-option>
              <template v-for="rowChild in row.children">
                <el-option :label="rowChild.name" :value="rowChild.id">
                  <span> {{ '-' + rowChild.name }}</span>
                </el-option>
              </template>
            </template>
          </el-select>
        </el-form-item>

        <el-form-item label="菜单名称" prop="name" label-width="100px">
          <el-input v-model="editForm.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="权限编码" prop="perms" label-width="100px">
          <el-input v-model="editForm.perms" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="图标" prop="icon" label-width="100px">
          <el-input v-model="editForm.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="菜单URL" prop="path" label-width="100px">
          <el-input v-model="editForm.path" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="菜单组件" prop="component" label-width="100px">
          <el-input v-model="editForm.component" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="类型" prop="type" label-width="100px">
          <el-radio-group v-model="editForm.type">
            <el-radio :label=0>目录</el-radio>
            <el-radio :label=1>菜单</el-radio>
            <el-radio :label=2>按钮</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="状态" prop="status" label-width="100px">
          <el-radio-group v-model="editForm.status">
            <el-radio :label=0>禁用</el-radio>
            <el-radio :label=1>正常</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="排序号" prop="orderNum" label-width="100px">
          <el-input-number v-model="editForm.orderNum" :min="1" label="排序号">1</el-input-number>
        </el-form-item>


        <el-form-item>
          <el-button type="primary" v-show="!this.editForm.id"  @click="submitForm('editForm')">提交</el-button>
          <el-button v-show="!this.editForm.id" @click="resetForm('editForm')">重置</el-button>
        </el-form-item>
      </el-form>

    </el-dialog>
  </div>
</template>

<script>
import request from "@/util/request.js";

export default {
  name: "SysMenu",
  data() {
    return {
      //表格加载状态
      loading:true,
      //对话框的开关
      centerDialogVisible: false,
      editForm: {},
      //行内表单
      formInline: {
        user: '',
        region: ''
      },
      //表格数据
      tableData: [],
      editFormRules: {
        parentId: [
          {required: true, message: '请选择上级菜单', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入名称', trigger: 'blur'}
        ],
        perms: [
          {required: true, message: '请输入权限编码', trigger: 'blur'}
        ],
        type: [
          {required: true, message: '请选择状态', trigger: 'blur'}
        ],
        orderNum: [
          {required: true, message: '请填入排序号', trigger: 'blur'}
        ],
        status: [
          {required: true, message: '请选择状态', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.getMenuList();
  },
  methods: {
    //获取表格数据
    getMenuList() {
      this.loading = true;
      request.get('/sys/menu/list').then(res => {
        this.loading = false;
        this.tableData = res.data.data;
      });
    },
    //编辑表格的某一行
    editRow(rowId) {
      //需要调用api获取最新数据
      request.get('/sys/menu/info/' + rowId).then(res => {
        //拿去数据填充到编辑表单中
        console.log(res);
        this.editForm = res.data.data;
        //获取成功之后显示编辑表单对话框
        this.centerDialogVisible = true;
      });
    },
    deleteRow(rowId) {
      request.post('/sys/menu/delete/' + rowId).then(res => {
        console.log(res);
        //获取成功之后显示编辑表单对话框
        this.$message({
          message: '删除成功！',
          type: 'success',
          showClose: true,
          duration:700,
          onClose:()=>{
            //获取最新数据
            this.getMenuList();
          }
        });
      });
    },
    //对话框内的表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //根据表单元素中是否含有id判断当前表单是更新还是添加
          request.post('/sys/menu/' + (this.editForm.id ? 'update' : 'save'),this.editForm).then(
              res => {
                this.$message({
                  message: '恭喜你，提交成功！',
                  type: 'success',
                  showClose: true,
                  duration:700,
                  //消息消失或关闭的回调
                  onClose: (msgIns) => {
                    //1.更新表格
                    this.getMenuList();
                    //2.关闭对话框,触发关闭回调dialogClosed()
                    this.centerDialogVisible = false;
                    //3.清空表单数据

                    //并不是将表单中的props直接清空
                    //将所有字段值重置为开始修改之前的初始值并移除校验结果
                    //在这可调可不调
                    // this.resetForm(formName);
                    //需要手动清空
                    // this.editForm = {};
                  }
                });
              }
          )
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    //对话框关闭回调
    dialogClosed(){
      this.editForm = {}
    },
  },
}
</script>

<style scoped>

</style>

<template>
  <div>
    <el-form :inline="true" :model="searchForm" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchForm.name" placeholder="名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="info" @click="getUserList">搜索</el-button>
      </el-form-item>
      <el-form-item v-if="hasPerm('sys:user:save')">
        <el-button type="primary" @click="userDialogVisible=true">新增</el-button>
      </el-form-item>

      <el-form-item v-if="hasPerm('sys:user:delete')">
        <template>
          <el-popconfirm title="确定批量删除选中的数据吗？" @confirm="delRoleHandle">
            <el-button type="danger" slot="reference"
                       :disabled="multipleSelection.length === 0">批量删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-form-item>
    </el-form>

    <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        border
        stripe
        :header-cell-style="headerStyle"
        :cell-style="rowStyle"
        @selection-change="handleSelectionChange">

      <el-table-column
          type="selection"
          width="55">
      </el-table-column>

      <el-table-column
          prop="usernmae"
          label="用户名"
          width="120">
      </el-table-column>

      <el-table-column
          prop="avatar"
          label="头像"
          width="70">
        <template v-slot="scopedProps">
          <el-avatar size="small" :src="scopedProps.row.avatar"></el-avatar>
        </template>
      </el-table-column>

      <el-table-column
          prop="rolename"
          label="角色名">
        <template v-slot="scopedProps">
          <el-tag size="small" v-for="role in scopedProps.row.roles" type="info">{{ role.name }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column
          prop="email"
          label="邮箱"
          width="120">
      </el-table-column>

      <el-table-column
          prop="cellphone"
          label="手机号码"
          show-overflow-tooltip>
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
          prop="created"
          width="200"
          label="创建时间"
      >
      </el-table-column>
      <el-table-column
          prop="icon"
          width="260px"
          label="操作">

        <template slot-scope="scopedProps">

          <el-button type="text" @click="assignUserRoleHandle(scopedProps.row.id)">分配角色</el-button>
          <el-divider direction="vertical"></el-divider>

          <el-button type="text" @click="resetPassword(scopedProps.row.id,scopedProps.row.username)">重置密码</el-button>
          <el-divider direction="vertical"></el-divider>

          <el-button type="text" @click="editUser(scopedProps.row.id)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>

          <el-popconfirm title="这是一段内容确定删除吗？" @confirm="delRoleHandle(scopedProps.row.id)">
            <el-button type="text" slot="reference">删除</el-button>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>

    <!--分页控件-->
    <div class="block">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[10, 20, 50, 100]"
          :current-page="pagination.current"
          :page-size="pagination.size"
          :total="pagination.total">
      </el-pagination>
    </div>

    <!--用户[新增/编辑]弹窗-->
    <el-dialog
        title="提示"
        :visible.sync="userDialogVisible"
        width="50%"
        @close="dialogClosed">


      <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="100px" class="demo-ruleForm">

        <el-form :model="editForm" :rules="editFormRules" ref="editForm">
          <el-form-item label="用户名" prop="username" label-width="100px">
            <el-input v-model="editForm.username" autocomplete="off"></el-input>
            <el-alert
                title="初始密码为888888"
                :closable="false"
                type="info"
                style="line-height: 12px;"
            ></el-alert>
          </el-form-item>

          <el-form-item label="邮箱" prop="email" label-width="100px">
            <el-input v-model="editForm.email" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="手机号" prop="phone" label-width="100px">
            <el-input v-model="editForm.phone" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item label="状态" prop="statu" label-width="100px">
            <el-radio-group v-model="editForm.statu">
              <el-radio :label="0">禁用</el-radio>
              <el-radio :label="1">正常</el-radio>
            </el-radio-group>
          </el-form-item>

        </el-form>


        <el-form-item>
          <el-button type="primary" @click="submitForm('editForm')">提交</el-button>
          <el-button @click="resetForm('editForm')">重置</el-button>
        </el-form-item>
      </el-form>

    </el-dialog>

    <!--用户[分配角色]弹窗-->
    <el-dialog
        title="分配角色"
        :visible.sync="roleDialogVisible"
        width="50%"
        @close="dialogClosed">

      <el-form :model="roleForm" ref="editForm" label-width="100px" class="demo-ruleForm">
        <el-tree
            ref="roleTree"
            :data="roleForm.treeData"
            show-checkbox
            :default-expand-all="true"
            node-key="id"
            :props="roleForm.defaultProps">
        </el-tree>
      </el-form>

      <!--底部确定和取消按钮-->
      <span slot="footer" class="dialog-footer">
          <el-button @click="roleDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="roleFormSubmitHandle()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/util/request";

export default {
  name: "SysUser",
  data() {
    return {
      //搜索框
      searchForm: {
        username: ''
      },
      //新增、修改角色的表单
      editForm: {},
      //角色表单
      roleForm: {
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      },
      //角色展示表格
      tableData: [],
      multipleSelection: [],
      //弹窗开关
      userDialogVisible: false,
      roleDialogVisible: false,
      //分页
      pagination: {
        total: 0,
        size: 10,
        current: 1
      },
      //角色表单校验规则
      editFormRules: {
        username: [
          {required: true, message: '请输入用户名称', trigger: 'blur'}
        ],
        email: [
          {required: true, message: '请输入邮箱', trigger: 'blur'}
        ],
        statu: [
          {required: true, message: '请选择状态', trigger: 'blur'}
        ]
      },
    }
  },
  created() {
    //1.取到表格的值
    this.getUserList();
    //2.取得角色的数据
    request.get('/sys/role/list').then(res => {
      this.roleForm.treeData = res.data.data.records;
    });
  },
  methods: {
    /**
     * 表格
     **/
    //表头，表内容居中
    headerStyle() {
      return "text-align:center"
    },
    rowStyle() {
      return "text-align:center"
    },
    //获取表格数据
    getUserList() {
      request.get('/sys/user/list', {
        //如果表格和分页有值传过去
        params: {
          username: this.searchForm.username,
          pagination: {
            current: this.pagination.current,
            size: this.pagination.size
          }
        }
      }).then(
          res => {
            console.log(res);
            this.tableData = res.data.data.records;
            this.pagination = res.data.data.pagination;
          }
      )
    },
    //给用户行分配角色
    assignUserRoleHandle(userId) {
      //1.展示角色表单对话框
      //必须放在axios外层,可能是初始化需要时间
      this.roleDialogVisible = true;
      //2.获取该用户的角色
      request.get('/sys/user/info/' + userId).then(res => {
        console.log(res);
        //2.1将其有的角色渲染到角色树上
        this.$refs.roleTree.setCheckedKeys(res.data.data.roles)
        //2.2 把当前角色的角色数据赋值给角色表单保存
        this.roleForm.personalData = res.data.data.roles;
      });
    },
    //角色表单提交处理
    roleFormSubmitHandle() {
      //获取选中的角色树的数据
      //角色控件里面属性可以设置每个节点上面保存的是什么数据
      //如果我在控件上设置node-key="id",那么我调用getChecckedKeys()获取的就是id
      // this.permForm.personalData.roleIds = this.$refs.roleTree.getCheckedKeys();
      let checkedRoleIds = this.$refs.roleTree.getCheckedKeys();
      //向后台发起角色更新请求
      request.post('/sys/user/role/' + this.roleForm.personalData.id, checkedRoleIds).then(
          res => {
            //这里没有根据业务code判断
            this.$message.success('分配角色成功!');
            //刷新角色表的数据
            this.getUserList();
            //关闭角色表单对话框
            this.roleDialogVisible = false;
          }
      );
    },
    //重置用户密码
    resetPassword(userId, username) {
      console.log(userId, username);
      this.$confirm('将重置用户【' + username + '】的密码, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.post("/sys/user/resetpass", userId).then(res => {
          this.$message({
            showClose: true,
            message: '重置密码成功',
            type: 'success',
            onClose: () => {
            }
          });
        })
      })
    },
    //编辑表格的某一行
    editUser(rowId) {
      //需要调用api获取最新数据
      request.get('/sys/role/info/' + rowId).then(res => {
        //拿去数据填充到编辑表单中
        console.log(res);
        this.editForm = res.data.data;
        //获取成功之后显示编辑表单对话框
        this.userDialogVisible = true;
      });
    },
    //处理删除角色请求，可以是单条也可以是多条
    delRoleHandle(id) {
      let ids = [];
      //处理多条删除
      this.multipleSelection.forEach(row => {
        ids.push(row.id)
      });
      //处理单条删除
      if (id !== undefined)
        ids.push(id)

      //打印ids
      // console.log(ids);

      //发起请求
      request.post('/sys/role/delete', ids).then(res => {
        //获取成功之后显示编辑表单对话框
        this.$message({
          message: '删除成功！',
          type: 'success',
          showClose: true,
          duration: 700,
          //消息弹窗关闭回调
          onClose: () => {
            //获取最新数据
            this.getUserList();
          }
        });
      });
    },
    //表格切换选中
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    //选中改变的时候
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    /**
     *分页控件
     **/
    //分页条数改变的回调
    handleSizeChange(size) {
      this.pagination.size = size;
      this.getUserList();
    },
    //分页当前页码改变的回调
    handleCurrentChange(current) {
      this.pagination.current = current;
      this.getUserList();
    },
    /**对话框**/
    //对话框关闭回调
    dialogClosed() {
      this.editForm = {}
    },
    /**角色表单**/
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //根据表单元素中是否含有id判断当前表单是更新还是添加
          request.post('/sys/user/' + (this.editForm.id ? 'update' : 'save')).then(
              res => {
                this.$message({
                  message: '提交成功！',
                  type: 'success',
                  showClose: true,
                  duration: 700,
                  //消息消失或关闭的回调
                  onClose: (msgIns) => {
                    //1.更新表格
                    this.getUserList();
                    //2.关闭对话框
                    this.userDialogVisible = false;
                    //3.清空表单数据,消息关闭回调的时候处理

                    //并不是将表单中的props直接清空
                    //将所有字段值重置为开始修改之前的初始值并移除校验结果
                    //在这可调可不调
                    // this.resetForm(formName);
                    // 需要手动清空表单
                    // this.editForm = {};
                  }
                });
              }
          )

        } else {
          return false;
        }
      });
    },
    //重置表单项到打开时候的状态
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped>
.el-pagination {
  float: right;
  margin-top: 22px;
}
/*
标签之间的间隙
*/
.el-tag+.el-tag{
  margin-left:5px;
}

/*.el-table__header tr,*/
/*.el-table__header th {*/
/*  padding: 0;*/
/*  height: 30px;*/
/*  line-height: 30px;*/
/*}*/
</style>
